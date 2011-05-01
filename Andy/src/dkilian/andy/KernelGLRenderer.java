package dkilian.andy;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

/**
 * Offloads OpenGL render calls to the currently executing Screen
 * 
 * @author dkilian
 */
public class KernelGLRenderer implements GLSurfaceView.Renderer
{
	/** The currently executing kernel, which spawned this renderer */
	private Kernel _kernel;
	/** The OpenGL surface view that owns this renderer */
	private KernelGLView _view;
	/** The OpenGL context */
	private GL10 _gl;
	/** Whether or not the virtual screen transform should be recomputed next frame */
	private boolean _resized;
	
	/**
	 * Creates an OpenGL renderer
	 * @param kernel The executing kernel that spawned this renderer
	 * @param view The view that owns this renderer
	 */
	public KernelGLRenderer(Kernel kernel, KernelGLView view)
	{
		_kernel = kernel;
		_view = view;
		_resized = false;
	}
	
	/** Draws the current frame */
	@Override
	public void onDrawFrame(GL10 gl) 
	{
		_gl = gl;
		
		Screen s = _kernel.getScreen();
		if (s != null)
		{
			if (_resized)
			{
				_resized = false;
				_kernel.getVirtualScreen().setPhysicalDimensions(_view);
				_kernel.getVirtualScreen().computeTransform(true);
				// TODO: agl virtual transform
			}
			
			// TODO: agl begin frame
			
			s.draw(_kernel, _kernel.getDrawTimeDelta());
			
			// TODO: agl end frame
		}
		else
		{
			// TODO: paint an error message
		}
	}

	/** Handles surface resize events */
	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) 
	{
		_gl = gl;
		
		_kernel.getVirtualScreen().setPhysicalWidth(w);
		_kernel.getVirtualScreen().setPhysicalHeight(h);
		_kernel.getVirtualScreen().computeTransform(true);
	}

	/** Handles surface (re)creation events */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		_gl = gl;
	}
	
	/** 
	 * Causes this renderer to recompute and apply the virtual screen transform next frame. 
	 * Call after modifying the kernel's VirtualScreen object.
	 */
	public void queueResize()
	{
		_resized = true;
	}
	
	/** Gets the current OpenGL context */
	public GL10 getGL()
	{
		return _gl;
	}
}