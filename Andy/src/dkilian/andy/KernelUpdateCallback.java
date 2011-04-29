package dkilian.andy;

import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;

/**
 * Manages the kernel's update loop
 * 
 * @author dkilian
 */
public class KernelUpdateCallback implements Callback
{
	/** The currently executing kernel */
	private Kernel _kernel;
	
	/**
	 * Creates a new Kernel update-loop callback
	 * @param k The kernel whose update loop is to be managed
	 */
	public KernelUpdateCallback(Kernel k)
	{
		_kernel = k;
	}

	/** Runs a tick of the kernel update loop */
	@Override
	public boolean handleMessage(Message arg0)
	{
		Screen s = _kernel.getScreen();
		if (s != null)
		{
			float timestep = 1.f / _kernel._targetUpdatesPerSec;

			long now = SystemClock.uptimeMillis();
			if (_kernel._lastUpdate > 0)
				_kernel._updateDelta = (now - _kernel._lastUpdate) * 0.001f; // * .001 -> / 1000
			else
				_kernel._updateDelta = timestep;
			_kernel._lastUpdate = now;
			
			s.update(_kernel, _kernel._updateDelta);

			if (_kernel._running)
				_kernel._updateHandler.sendEmptyMessageAtTime(0, (long)(now + 1000 * (timestep - (_kernel._updateDelta - timestep))));
		}
		return true;
	}
}
