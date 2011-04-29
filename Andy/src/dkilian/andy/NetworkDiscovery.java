package dkilian.andy;

import java.util.ArrayList;

/**
 * Interface for a class that can find other devices on a local network
 * 
 * @author dkilian
 */
public interface NetworkDiscovery 
{
	/**
	 * Begins a network discovery operation. Note that network discovery
	 * operations can be very battery intensive, so be sure to call end()
	 * on this object as soon as possible.
	 */
	public void begin(Kernel kernel);
	
	/**
	 * Ends a network discovery operation
	 */
	public void end(Kernel kernel);
	
	/** Gets the list of identifiers of devices found so far */
	public ArrayList<String> getIdentifiers();
}
