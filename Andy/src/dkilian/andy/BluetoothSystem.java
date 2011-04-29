package dkilian.andy;

/**
 * Encapsulates network communication over Bluetooth
 * 
 * @author dkilian
 */
public class BluetoothSystem implements NetworkSystem
{
	/** Creates an object that can be used to identify other devices on the local network */
	@Override
	public NetworkDiscovery createDiscovery(Kernel kernel) 
	{
		return new BluetoothDiscovery();
	}

	/** Creates an object that can listen for and spawn new network connections */
	@Override
	public NetworkListener createListener(String identifier, Kernel kernel) 
	{
		return new BluetoothListener();
	}
	
	/** Creates an object that can read and write data over a network connection */
	@Override
	public NetworkConnection createConnection(String identifier, Kernel kernel) 
	{
		return new BluetoothConnection(identifier);
	}
}
