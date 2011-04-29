package dkilian.andy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for holding a circular (ring) buffer of bytes. Used
 * (for example) by the Bluetooth NetworkConnection implementation.
 * 
 * @author dkilian
 */
public class RingBuffer 
{
	/** Gets the buffer that stores this ring buffer's data */
	private byte[] _buf;
	/** The offset into the buffer of the current start pointer of this buffer */
	private int _start;
	/** The number of bytes after the start pointer (modulo the length of _buf) available for reading */
	private int _len;
	
	/** Creates a new ring buffer with the given capacity */
	public RingBuffer(int capacity)
	{
		resize(capacity);
	}
	
	/** Gets the capacity of this ring buffer, in bytes */
	public int getCapacity()
	{
		return _buf.length;
	}
	
	/** Resizes this buffer, deleting its contents in the process */
	public void resize(int capacity)
	{
		_buf = new byte[capacity];
		_start = 0;
		_len = 0;
	}
	
	/** Gets the number of bytes available to be read from this buffer */
	public int getLength()
	{
		return _len;
	}
	
	/**
	 * Reads data from this buffer into a byte array
	 * @param buf Receives the data read
	 * @param offset The offset into the supplied byte array to begin writing
	 * @param count The maximum number of bytes to read
	 * @return The actual number of bytes read
	 */
	public int read(byte[] buf, int offset, int count)
	{
		int amount = peek(buf, offset, count);
		_start += amount;
		_len -= amount;
		return amount;
	}
	
	/**
	 * Reads data from this buffer into a stream
	 * @param stream Receives the data read
	 * @param count The maximum number of bytes to read
	 * @return The actual number of bytes read
	 * @throws IOException If thrown by stream.write()
	 */
	public int read(OutputStream stream, int count) throws IOException
	{
		int amount = peek(stream, count);
		_start += amount;
		_len -= amount;
		return amount;
	}
	
	/**
	 * Reads data from this buffer into a byte array without removing it from this buffer
	 * @param buf Receives the data read
	 * @param offset The offset into the supplied byte array to begin writing
	 * @param count The maximum number of bytes to read
	 * @return The actual number of bytes read
	 */
	public int peek(byte[] buf, int offset, int count)
	{
		int destIndex = 0;
		
		for (int srcIndex = _start;
		     srcIndex < _start + _len && srcIndex < _buf.length && destIndex < count;
		     ++srcIndex, ++destIndex)
			buf[destIndex + offset] = _buf[srcIndex];
		
		for (int srcIndex = 0;
		     srcIndex < _start + _len - _buf.length && destIndex < count;
		     ++srcIndex, ++destIndex)
			buf[destIndex + offset] = _buf[srcIndex];
		
		return destIndex;
	}

	/**
	 * Reads data from this buffer into a stream without removing it from this buffer
	 * @param stream Receives the data read
	 * @param count The maximum number of bytes to read
	 * @return The actual number of bytes read
	 * @throws IOException If thrown by stream.write()
	 */
	public int peek(OutputStream stream, int count) throws IOException
	{
		int written = 0;
		
		for (int srcIndex = _start;
		     srcIndex < _start + _len && srcIndex < _buf.length && written < count;
		     ++srcIndex, ++written)
			stream.write(_buf[srcIndex]);
		
		for (int srcIndex = 0;
		     srcIndex < _start + _len - _buf.length && written < count;
		     ++srcIndex, ++written)
			stream.write(_buf[srcIndex]);
		
		return written;
	}
	
	/**
	 * Writes data to this buffer from a byte array
	 * @param buf The byte array to copy into this buffer
	 * @param offset The offset into the supplied byte array to begin reading
	 * @param count The number of bytes to write into this buffer
	 * @return The actual number of bytes written
	 */
	public int write(byte[] buf, int offset, int count)
	{
		int sourceIndex = 0;
		
		for (int destIndex = _start + _len; 
		     destIndex < _buf.length && sourceIndex < count; 
		     ++destIndex, ++sourceIndex)
			_buf[destIndex] = buf[sourceIndex + offset];
		
		for (int destIndex = 0;
		     destIndex < _start && sourceIndex < count;
		     ++destIndex, ++sourceIndex)
			_buf[destIndex] = buf[sourceIndex + offset];
		
		_len += sourceIndex;
		return sourceIndex;
	}

	/**
	 * Writes data to this buffer from a stream
	 * @param stream The stream to copy from
	 * @param count The number of bytes to write into this buffer
	 * @return The actual number of bytes written
	 */
	public int write(InputStream stream, int count) throws IOException
	{
		int written = 0;
		
		for (int destIndex = _start + _len; 
		     destIndex < _buf.length && written < count; 
		     ++destIndex, ++written)
		{
			_buf[destIndex] = (byte)stream.read();
			++_len;		// Decrement immediately in case an exception is thrown next time
		}
		
		for (int destIndex = 0;
		     destIndex < _start && written < count;
		     ++destIndex, ++written)
		{
			_buf[destIndex] = (byte)stream.read();
			++_len;
		}
		
		return written;
	}
}
