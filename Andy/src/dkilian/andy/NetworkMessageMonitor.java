package dkilian.andy;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import android.util.Log;

/**
 * Manages use of Andy's network messaging protocol over a NetworkConnection
 * @author dkilian
 */
public class NetworkMessageMonitor 
{
	/** The list of messages that can be unpacked into */
	private ArrayList<NetworkMessage> _messages;
	/** The connection to read messages from and write messages to */
	private NetworkConnection _connection;
	/** The queue of messages that have been parsed but not taken yet */
	private Queue<NetworkMessage> _parsedMessages;
	/** The temporary buffer used to store each packed message before sending it */
	private byte[] _outgoingBuffer;

	// Temporary variables for parsing
	/** Contains received data that can not yet be unpacked */
	private byte[] _messageBuffer;
	/** The number of 'valid' bytes in the _messageBUffer */
	private int _bytesInMessageBuffer;
	/** The number of payload bytes that still need to be downloaded, if applicable */
	private int _payloadBytesRemaining;
	/** The type of the next message, if the header has already been parsed */
	private int _nextMessageType;
	/** The timestamp of the next message, if the header has already been parsed */
	private float _nextMessageTimestamp;
	/** The length of the payload of the next message, if the header has already been aprsed */
	private int _nextMessageLength;
	/** The distance, in bytes, into the _messageBuffer where the next message's payload begins */
	private int _payloadOffset;
	/** Whether or not the next message's header has been parsed */
	private boolean _haveHeader;
	
	/** Creates a new message monitor */
	public NetworkMessageMonitor()
	{
		_messages = new ArrayList<NetworkMessage>();
		_connection = null;
		_parsedMessages = new ArrayBlockingQueue<NetworkMessage>(1);
		_outgoingBuffer = new byte[1024];
		
		_messageBuffer = new byte[1024];
		_bytesInMessageBuffer = 0;
		_payloadBytesRemaining = 0;
		_nextMessageType = 0;
		_nextMessageTimestamp = 0;
		_nextMessageLength = 0;
		_payloadOffset = 0;
		_haveHeader = false;
	}
	
	/** 
	 * Registers a message with the monitor. Whenever a message with the matching type ID is
	 * received, it will be unpacked into the given object
	 */
	public void register(NetworkMessage m)
	{
		_messages.add(m);
	}
	
	/**
	 * Unregisters a message with the monitor. The monitor will no longer be able to parse 
	 * messages with that message's type ID
	 */
	public void unregister(NetworkMessage m)
	{
		_messages.remove(m);
	}
	
	/** Gets the connection this monitor sends and receives messages over */
	public NetworkConnection getConnection()
	{
		return _connection;
	}

	/** Sets the connection this monitor sends and receives messages over */
	public void setConnection(NetworkConnection conn)
	{
		_connection = conn;
	}
	
	/** Gets a value indicatign whether or not this monitor has unhandled but successfully parsed messages in its inbound queue */
	public boolean hasMessage()
	{
		return !_parsedMessages.isEmpty();
	}
	
	/** Blocks until there is an unhandled but successfully parsed message in the inbound queue, removes the message, and returns it */
	public NetworkMessage readMessage()
	{
		while (!hasMessage()) 
			poll();
		
		return _parsedMessages.remove();
	}

	/** Blocks until there is an unhandled but successfully parsed message in the inbound queue and returns it without removing it */
	public NetworkMessage peekMessage()
	{
		while (!hasMessage()) 
			poll();
		
		return _parsedMessages.peek();
	}

	private boolean parseHeader()
	{
		// Parsing is fun!
		
		String textContents = new String(_messageBuffer);
		int endOfHeader = textContents.indexOf("\n\n");
		
		if (endOfHeader >= 0)		// Have entire header, parse and start receiving payload
		{
			String[] lines = textContents.substring(0, endOfHeader).split("\n");
			String idstr = null, timestr = null, lengthstr = null;
			for (int i = 0; i < lines.length; ++i)
			{
				String tmp = lines[i].toLowerCase();
				if (tmp.startsWith("id="))
					idstr = lines[i].substring("id=".length());
				else if (tmp.startsWith("timestamp="))
					timestr = lines[i].substring("timestamp=".length());
				else if (tmp.startsWith("length="))
					lengthstr = lines[i].substring("length=".length());
			}
			
			if (idstr == null || timestr == null || lengthstr == null)
			{
				Log.v("NetworkMessageMonitor", ("Message header is missing a required field:\n" + textContents).replace("\n", "\\n"));
				_bytesInMessageBuffer = 0;
			}
			else
			{
				try
				{
					_nextMessageType = Integer.parseInt(idstr);
					_nextMessageTimestamp = Float.parseFloat(timestr);
					_nextMessageLength = Integer.parseInt(lengthstr);
					_payloadOffset = textContents.substring(0, endOfHeader + "\n\n".length()).getBytes().length;
					_payloadBytesRemaining = _nextMessageLength - (_bytesInMessageBuffer - _payloadOffset); 
					
					return true;
				}
				catch (Exception e)
				{
					Log.v("NetworkMessageMonitor", "Malformed message header:\n" + textContents + "\n\nCaused:\n" + e.toString());
					_bytesInMessageBuffer = 0;
				}
			}
		}
		
		return false;
	}
	
	private boolean havePayload()
	{
		return _payloadBytesRemaining <= 0;
	}
	
	private void recoverMessage()
	{
		// Find the correct message to unpack into
		NetworkMessage m = null;
		for (int i = 0; i < _messages.size(); ++i)
		{
			if (_messages.get(i).getTypeID() == _nextMessageType)
			{
				m = _messages.get(i);
				break;
			}
		}
		
		// Unpack and queue the message
		if (m != null)
		{
			m.unpack(_messageBuffer, _payloadOffset, _nextMessageLength, _nextMessageTimestamp);
			_parsedMessages.add(m);
		}
		else
		{
			Log.v("NetworkMessageMonitor", "Unknown message type ID: " + _nextMessageType);
			_bytesInMessageBuffer = 0;
		}
		
		// Remove the contents of the message from the buffer
		int count = _payloadOffset + _nextMessageLength;
		for (int i = 0; i < _messageBuffer.length - count; ++i)
			_messageBuffer[i] = _messageBuffer[count + i];
		
		// Miscellaneous state updates
		_bytesInMessageBuffer -= count;
		if (_bytesInMessageBuffer < 0)
		{
			_bytesInMessageBuffer += 0;
		}
		_haveHeader = false;
	}
	
	/** Uses bytes that have been received since the last call to poll() to attempt to parse an inbound message */
	public void poll()
	{
		if (_connection.getNumBytesAvailable() > 0)
		{
			// Read from the socket
			int numBytesAdded = _connection.getNumBytesAvailable();
			int totalBytes = _bytesInMessageBuffer + numBytesAdded;
			if (totalBytes > _messageBuffer.length)
			{
				byte[] old = _messageBuffer;
				_messageBuffer = new byte[totalBytes];
				for (int i = 0; i < _bytesInMessageBuffer; ++i)
					_messageBuffer[i] = old[i];
			}

			if (_bytesInMessageBuffer < 0)
			{
				_bytesInMessageBuffer += 0;
			}
			int read = _connection.read(_messageBuffer, _bytesInMessageBuffer, numBytesAdded);
			_bytesInMessageBuffer += read;
			
			// Parse whatever parts of the incoming message(s) the previous read brought in
			if (!_haveHeader)
				_haveHeader = parseHeader();
			if (_haveHeader && havePayload())
				recoverMessage();
		}
	}
	
	/** Sends a message over this monitor's connection */
	public void sendMessage(NetworkMessage msg, float timestamp)
	{
		byte[] payload = msg.pack();
		
		String header = "ID=" + msg.getTypeID() + "\n"
					  + "Timestamp=" + timestamp + "\n"
					  + "Length=" + payload.length + "\n"
					  + "\n";
		byte[] head = header.getBytes();
		
		int count = head.length + payload.length;
		if (_outgoingBuffer.length < count)
			_outgoingBuffer = new byte[count];
		
		int index = 0;
		for (int i = 0; i < head.length; ++i)
			_outgoingBuffer[index++] = head[i];
		for (int i = 0; i < payload.length; ++i)
			_outgoingBuffer[index++] = payload[i];
		
		_connection.write(_outgoingBuffer, count);
	}
}
