package dkilian.andy;

import android.os.Message;
import android.os.SystemClock;
import android.os.Handler.Callback;

/**
 * Manages the kernel's draw loop
 * 
 * @author dkilian
 */
public class KernelDrawCallback implements Callback
{
	/** The currently executing kernel */
	private Kernel _kernel;

	/**
	 * Creates a new Kernel draw-loop callback
	 * @param k The kernel whose draw loop is to be managed
	 */
	public KernelDrawCallback(Kernel k)
	{
		_kernel = k;
	}

	/** Runs a tick of the kernel draw loop */
	@Override
	public boolean handleMessage(Message arg0)
	{
		Screen s = _kernel.getScreen();
		if (s != null)
		{
			float timestep = 1.f / _kernel._targetDrawsPerSec;
			
			long now = SystemClock.uptimeMillis();
			if (_kernel._lastDraw > 0)
				_kernel._drawDelta = (now - _kernel._lastDraw) * .001f; // * .001 -> / 1000
			else
				_kernel._drawDelta = timestep;
			_kernel._lastDraw = now;
			
			if (_kernel.isOpenGLEnabled())
				_kernel.getGLView().invalidate();
			else
				_kernel.getView().invalidate();
			
			if (_kernel._running)
				_kernel._drawHandler.sendEmptyMessageAtTime(0, (long)(now + 1000 * (timestep - (_kernel._updateDelta - timestep))));
		}
		return true;
	}
}
