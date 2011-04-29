package dkilian.andy;

/**
 * Interface for a server socket (i.e. which listens for incoming connections)
 * 
 * @author dkilian
 */
public interface NetworkListener 
{
	/**
	 * Gets the implementation-specific identifier for the target this listener 
	 * is listening on (e.g. the port number for a TCP socket).
	 */
	public String getIdentifier();

	/**
	 * Sets the implementation-specific identifier for the target this listener 
	 * is listening on (e.g. the port number for a TCP socket).
	 */
	public void setIdentifier(String identifier);
	
	/** Begins listening on the current identifier. */
	public void begin(Kernel kernel);
	
	/** Sets the current identifier to the given value and begins listening on it */
	public void begin(String identifier, Kernel kernel);
	
	/** Gets a value indicating whether or not this listener is currently in a listening state */
	public boolean isListening();
	
	/** Ends the current listening operation */
	public void end(Kernel kernel);
	
	/** Gets a value indicating whether there are incoming connections from this listener to be handled */
	public boolean hasMoreConnections();
	
	/** Gets the next as-yet-unhandled incoming connection and removes it from this listener's queue */
	public NetworkConnection takeConnection();
	
	/** Gets the next as-yet-unhandled incoming conenction without removing it from this listener's queue */
	public NetworkConnection peekConnection();
}
