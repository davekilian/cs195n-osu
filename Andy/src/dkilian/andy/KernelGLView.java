package dkilian.andy;

import javax.microedition.khronos.opengles.GL10;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Renders the game using OpenGL ES
 * 
 * @author dkilian
 */
public class KernelGLView extends GLSurfaceView
{
	/** The currently executing kernel, which spawned this renderer */
	private Kernel _kernel;
	/** The object that handles OpenGL render requests */
	private KernelGLRenderer _renderer;

	/**
	 * Creates a new OpenGL-based kernel view
	 * @param kernel The currently executing kernel
	 * @param context The activity spawning this view
	 */
	public KernelGLView(Kernel kernel, Context context) 
	{
		super(context);
		_kernel = kernel;
		
		if (((KernelActivity)context).getMinimumGLVersion() >= 0x20000)
			setEGLContextClientVersion(2);
		
		_renderer = new KernelGLRenderer(kernel, this);
		setRenderer(_renderer);
	}

	/** Causes the virtual screen transform to be recomputed and applied next draw call */
	public void queueResize()
	{
		_renderer.queueResize();
	}
	
	/** Gets the current OpenGL context */
	public GL10 getGL()
	{
		return _renderer.getGL();
	}
	
	/**
	 * Gets the version of OpenGL being used by this view. The upper 16 bits denote the major version and the
	 * lower 16 bits denote the mintor version; e.g. 0x10001 corresponds to OpenGL 1.1 and 0x20000 corresponds
	 * to OpenGL 2.0
	 */
	public int getGLVersion()
	{
		ActivityManager man = (ActivityManager)_kernel.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		return man.getDeviceConfigurationInfo().reqGlEsVersion;
	}
}
