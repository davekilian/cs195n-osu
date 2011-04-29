package dkilian.andy;

/**
 * Interface for a class that can send and receive data over a network connection
 * @author dkilian
 */
public interface NetworkConnection 
{
	/** Gets the implementation-specific identifier for the remote endpoint of this connection */
	public String getIdentifier();

	/** Sets the implementation-specific identifier for the remote endpoint of this connection */
	public void setIdentifier(String identifier);
	
	/** Begins an asynchronous connection attempt */
	public void beginConnect(Kernel kernel);
	
	/** Sets the remote endpoint and begins an asynchronous connection attempt */
	public void beginConnect(String id, Kernel kernel);
	
	/** Gets a value indicating whether this connection is attempting to establish */
	public boolean isConnecting();
	
	/** Gets a value indicating whether this connection has been successfully established */
	public boolean isConnected();
	
	/** Gets the number of bytes that are available to be read from this connection */
	public int getNumBytesAvailable();
	
	/** 
	 * Reads from this connection
	 * @param buf Receives the data read
	 * @param offset The offset, in bytes, into the buffer to start copying into
	 * @param len The maximum number of bytes to read
	 * @return The number of bytes actually read
	 */
	public int read(byte[] buf, int offset, int len);
	
	/**
	 * Writes data to this connection
	 * @param buf The data to write
	 * @param count The number of bytes to write
	 */
	public void write(byte[] buf, int count);
	
	/** Closes this connection if it is established */
	public void close();
}
