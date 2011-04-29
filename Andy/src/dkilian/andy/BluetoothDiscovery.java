package dkilian.andy;

import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Implements local network discovery over Bluetooth
 * 
 * @author dkilian
 */
public class BluetoothDiscovery extends BroadcastReceiver implements NetworkDiscovery
{
	/** Gets the period for which the device is discoverable, in seconds */
	public static final int DISCOVERABLE_PERIOD = 120;
	
	/** The list of devices found so far */
	private ArrayList<String> _identifiers;
	/** The old quit-on-pause behavior of the kernel's main activity */
	private boolean _oldQuitBehavior;
	
	/** Creates a new Bluetooth network discovery object */
	public BluetoothDiscovery()
	{
		_identifiers = new ArrayList<String>();
	}

	/** Begins a Bluetooth discovery operation */
	@Override
	public void begin(Kernel kernel)
	{
		_identifiers.clear();
		
		// Don't quit when this intent is paused
		_oldQuitBehavior = kernel.getActivity().getQuitOnPause();
		kernel.getActivity().setQuitOnPause(false);
		
		// Make this device discoverable
		Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_PERIOD);
		kernel.getActivity().startActivity(discoverable);
		
		// Start looking for other devices
		kernel.getActivity().registerReceiver(this, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		BluetoothAdapter.getDefaultAdapter().startDiscovery();
	}

	/** Stops the Bluetooth discovery operation in progress */
	@Override
	public void end(Kernel kernel) 
	{
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		kernel.getActivity().unregisterReceiver(this);
		kernel.getActivity().setQuitOnPause(_oldQuitBehavior);
	}

	/** Gets the list of identifiers of devices found by this object so far */
	@Override
	public ArrayList<String> getIdentifiers() 
	{
		return _identifiers;
	}

	/** Used internally to implement network discovery */
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND))
		{
			BluetoothDevice d = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			_identifiers.add(d.getName() + "@" + d.getAddress());
		}
	}
}
