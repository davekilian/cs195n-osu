package dkilian.andy;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Provides auxiliary Bluetooth services and configuration
 * 
 * @author dkilian
 */
public class Bluetooth 
{
	private static String _serviceName, _serviceUUID;
	
	static
	{
		_serviceName = "Unidentified Andy-Powered Application";
		_serviceUUID = "bfaf8a90-6880-11e0-ae3e-0800200c9a66";
	}
	
	/**
	 * Enables Bluetooth on this device if possible. Your application must have the
	 * BLUETOOTH and BLUETOOTH_ADMIN permissions in its manifest file. 
	 * You do not need to call this if you consistently plan on using Bluetooth
	 * discovery, since enabling discovery also enables Bluetooth as a side
	 * effect.
	 * @param kernel The currently executing kernel
	 * @return True if Bluetooth was successfully enabled, or false otherwise
	 */
	public static boolean enable(Kernel kernel)
	{
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null)
			return false;	// Bluetooth not supported
		
		if (!adapter.isEnabled())
			kernel.getActivity().startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
		
		return true;
	}
	
	/**
	 * Gets your application's service name. This is an arbitrary, human-readable
	 * string that is specific to your application, but is the same across all 
	 * instances of it. Your application's name is a good choice for this.
	 */
	public static String getServiceName()
	{
		return _serviceName;
	}

	/**
	 * Sets your application's service name. This is an arbitrary, human-readable
	 * string that is specific to your application, but is the same across all 
	 * instances of it. Your application's name is a good choice for this.
	 */
	public static void setServiceName(String service)
	{
		_serviceName = service;
	}
	
	/**
	 * Gets your application's UUID string. This UUID is specific to your application,
	 * but is the same across all instances of it. It is used when searching for
	 * other devices that are running your game on a local Bluetooth network.
	 */
	public static String getUUID()
	{
		return _serviceUUID;
	}

	/**
	 * Sets your application's UUID string. This UUID is specific to your application,
	 * but is the same across all instances of it. It is used when searching for
	 * other devices that are running your game on a local Bluetooth network.
	 */
	public static void setUUID(String uuid)
	{
		_serviceUUID = uuid;
	}
}
