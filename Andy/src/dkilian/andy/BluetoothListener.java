package dkilian.andy;

import java.io.IOException;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

/**
 * Implements network listening over Bluetooth
 * 
 * @author dkilian
 */
public class BluetoothListener implements NetworkListener
{	
	/**
	 * Handles reading from the server socket asynchronously
	 * @author dkilian
	 */
	private class ListenerThread implements Runnable
	{
		public Queue<BluetoothConnection> _incoming;
		public BluetoothServerSocket _socket;
		public boolean _isRunning;
		public boolean _killFlag;

		public ListenerThread()
		{
			_incoming = new LinkedBlockingQueue<BluetoothConnection>();
			_isRunning = false;
			_killFlag = false;
		}
		
		@Override
		public void run() 
		{
			_isRunning = true;
			
			while (!_killFlag)
			{
				try
				{
					BluetoothSocket socket = _socket.accept();
					_incoming.add(new BluetoothConnection(socket.getRemoteDevice().getName() + "@" + socket.getRemoteDevice().getAddress(), socket));
				}
				catch (IOException ex) {}
			}
			
			_isRunning = false;
		}
	}
	
	/** The thread used to listen for new incoming connections */
	private ListenerThread _listener;
	
	/** Creates a new Bluetooth listener */
	public BluetoothListener()
	{
		_listener = new ListenerThread();
	}
	
	/** Not used */
	@Override
	public String getIdentifier() 
	{
		return null;
	}

	/** Not used */
	@Override
	public void setIdentifier(String identifier) {}

	/** Begins listening for incoming Bluetooth connections */
	@Override
	public void begin(Kernel kernel) 
	{
		try
		{
			_listener._incoming.clear();
			_listener._isRunning = true;
			_listener._killFlag = false;
			_listener._socket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(Bluetooth.getServiceName(), UUID.fromString(Bluetooth.getUUID()));
			new Thread(_listener).start();
		}
		catch (IOException ex)
		{
			_listener._socket = null;
			_listener._isRunning = false;
		}
	}

	/** Begins listening for incoming Bluetooth connections using the given service identifier */
	@Override
	public void begin(String identifier, Kernel kernel) 
	{
		setIdentifier(identifier);
		begin(kernel);
	}

	/** Gets a value indicating whether or not this object is currently listening for incoming connections */
	@Override
	public boolean isListening() 
	{
		return _listener._isRunning;
	}

	/** Stops listening for incoming Bluetooth connections */
	@Override
	public void end(Kernel kernel) 
	{
		if (isListening())
		{
			try
			{
				_listener._socket.close();
			}
			catch (Exception ex) {}
			_listener._killFlag = true;
		}
	}

	/** Gets a value indicating whether there are unhandled incoming connections in this listener's queue */
	@Override
	public boolean hasMoreConnections() 
	{
		return !_listener._incoming.isEmpty();
	}

	/** Removes a value from this listener's unhandled incoming connection queue and returns it */
	@Override
	public NetworkConnection takeConnection() 
	{
		return _listener._incoming.remove();
	}

	/** Returns a reference to this listener's unhandled incoming connection queue without removing it */
	@Override
	public NetworkConnection peekConnection() 
	{
		return _listener._incoming.peek();
	}
}
