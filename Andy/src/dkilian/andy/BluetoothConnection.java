package dkilian.andy;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Implements network communication over Bluetooth
 * 
 * @author dkilian
 */
public class BluetoothConnection implements NetworkConnection
{
	/** Manages a connection attempt on a separate thread */
	private class ConnectThread implements Runnable
	{		
		public Kernel _kernel;
		
		@Override
		public void run() 
		{
			_connecting = true;
			
			String deviceAddr = _identifier.substring(_identifier.indexOf("@") + 1);
			BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddr);
			
			try
			{
				_socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Bluetooth.getUUID()));
				
				boolean tmp = _kernel.getActivity().getQuitOnPause();
				_kernel.getActivity().setQuitOnPause(false);
				_socket.connect();
				_kernel.getActivity().setQuitOnPause(tmp);
				
				_connected = true;
				new Thread(_read).start();
			}
			catch (Exception ex)
			{
				_socket = null;
				_connected = false;
			}
			
			_connecting = false;
		}	
	}
	
	/** Manages blocking socket reads on a separate thread */
	private class ReadThread implements Runnable
	{
		public static final int READ_BUFFER_SIZE = 1024 * 16;
		
		private byte[] _buffer;
		public RingBuffer _ring;
		public boolean _killFlag;
		
		public ReadThread()
		{
			_buffer = new byte[READ_BUFFER_SIZE];
			_ring = new RingBuffer(READ_BUFFER_SIZE);
			_killFlag = false;
		}
		
		@Override
		public void run() 
		{
			while (!_killFlag)
			{
				try
				{
					int count = _socket.getInputStream().read(_buffer);
					
					while (count > 0)
					{
						synchronized (_ring)
						{
							count -= _ring.write(_buffer, 0, count);
						}
						
						try
						{
							Thread.sleep(10);
						}
						catch (InterruptedException e) {}
					}
				}
				catch (IOException e) {}
			}
		}	
	}
	
	/** This connection's identifier: devicename@deviceaddr */
	private String _identifier;
	/** The socket this connection reads from and writes to */
	private BluetoothSocket _socket;
	/** Indicates whether this connection is attempting to be established */
	private boolean _connecting;
	/** Indicates whether this connection is established */
	private boolean _connected;
	/** The thread used to establish connections */
	private ConnectThread _connect;
	/** The thread used to read from the socket */
	private ReadThread _read;
	
	/** Creates a new Bluetooth network connection without an endpoint */
	public BluetoothConnection()
	{
		_identifier = "";
		_connecting = false;
		_connected = false;
		_connect = new ConnectThread();
		_read = new ReadThread();
	}
	
	/** Creates a new Bluetooth network connection with the specified endpoint without attempting to connect */
	public BluetoothConnection(String identifier)
	{
		_identifier = identifier;
		_connecting = false;
		_connected = false;
		_connect = new ConnectThread();
		_read = new ReadThread();
	}

	/** Creates a new Bluetooth network connection from an existing endpoint (used by BluetoothListener) */
	public BluetoothConnection(String identifier, BluetoothSocket socket)
	{
		_identifier = identifier;
		_socket = socket;
		_connecting = false;
		_connected = true;
		_connect = new ConnectThread();
		_read = new ReadThread();
		new Thread(_read).start();
	}

	/** Gets the identifier signifying the remote endpoint: devicename@deviceaddr */
	@Override
	public String getIdentifier() 
	{
		return _identifier;
	}

	/** Sets the identifier signifying the remote endpoint: devicename@deviceaddr */
	@Override
	public void setIdentifier(String identifier) 
	{
		_identifier = identifier;
	}

	/** Begins asynchronously trying to connect to the remote endpoint signified by this object's identifier */
	@Override
	public void beginConnect(Kernel kernel) 
	{
		if (_socket != null)
		{
			close();
			_socket = null;
			_connected = false;
			_connecting = true;
		}
		
		_connect._kernel = kernel;
		new Thread(_connect).start();
	}

	/** Sets the identifier to the given value and then begins asynchronously trying to connect to the remote endpoint */
	@Override
	public void beginConnect(String id, Kernel kernel) 
	{
		setIdentifier(id);
		beginConnect(kernel);
	}

	/** Gets a value indicating whether this object is attempting to establish a connection */
	@Override
	public boolean isConnecting() 
	{
		return _connecting;
	}

	/** Gets a value indicating whether this object has an established connection */
	@Override
	public boolean isConnected() 
	{
		return _connected;
	}

	/** Gets the number of bytes available to be read from this connection */
	@Override
	public int getNumBytesAvailable() 
	{
		int available = 0;
		synchronized (_read._ring)
		{
			available = _read._ring.getLength();
		}
		return available;
	}

	/**
	 * Reads data from this connection into a byte array
	 * @param buf The buffer to copy into
	 * @param len The maximum number of bytes to copy
	 * @return The number of bytes actually read
	 */
	@Override
	public int read(byte[] buf, int offset, int len) 
	{
		int count = 0;
		synchronized (_read._ring)
		{
			count = _read._ring.read(buf, offset, len);
		}
		return count;
	}

	/**
	 * Writes data to this connection from a  byte array
	 * @param buf The buffer to copy from
	 * @param count The number of bytes to write
	 */
	@Override
	public void write(byte[] buf, int count) 
	{
		try
		{
			_socket.getOutputStream().write(buf, 0, count);
			_socket.getOutputStream().flush();
		}
		catch (Exception ex) {}
	}

	/** Closes and invalidates this connection */
	@Override
	public void close() 
	{
		if (_connected)
		{
			_connected = false;
			try
			{
				_socket.close();
			}
			catch (Exception ex) {}
			_read._killFlag = true;
		}
	}
}
