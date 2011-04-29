package dkilian.andy;

/**
 * Encapsulates network communication over a specific 
 * subsystem, such as TCP sockets or Bluetooth
 * @author dkilian
 */
public interface NetworkSystem 
{
	/** Creates an object that can be used to identify other devices on the local network */
	public NetworkDiscovery createDiscovery(Kernel kernel);
	
	/** Creates an object that can listen for and spawn new network connections */
	public NetworkListener createListener(String identifier, Kernel kernel);
	
	/** Creates an object that can read and write data over a network connection */
	public NetworkConnection createConnection(String identifier, Kernel kernel);
}
