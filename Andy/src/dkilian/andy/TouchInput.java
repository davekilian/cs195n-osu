package dkilian.andy;

import android.graphics.Matrix;

/**
 * Represents touch inputs (from the device's touchscreen)
 * gathered by the kernel.
 * 
 * @author dkilian
 */
public class TouchInput 
{	
	private Kernel _kernel;		// Reference to the running kernel
	private boolean _isDown;	// True if there is a touch, false otherwise
	private float _x;			// The physical X coordinate of the current touch
	private float _y;			// The physical Y coordinate of th current touch
	private Matrix _inverse;    // Preallocated for GC performance
	private float[] _points;	// Ditto
	
	/**
	 * Creates a new TouchInput object
	 */
	public TouchInput(Kernel k)
	{
		_kernel = k;
		_isDown = false;
		_x = _y = 0;
		_inverse = new Matrix();
		_points = new float[2];
	}
	
	/** Gets a value indicating whether a finger is touching the screen */
	public final boolean isDown()
	{
		return _isDown;
	}
	
	/** Gets the X coordinate of the current touch in physical screen coordinates */
	public final float getPhysicalX()
	{
		return _x;
	}
	
	/** Gets the Y coordinate of the current touch in physical screen coordinates */
	public final float getPhysicalY()
	{
		return _y;
	}
	
	/** Gets the X coordinate of the current touch in virtual screen coordinates */
	public final float getX()
	{
		_points[0] = _x;
		_points[1] = 0.f;
		_kernel.getVirtualScreen().getTransform().invert(_inverse);
		_inverse.mapPoints(_points);
		return _points[0];
	}
	
	/** Gets the Y coordinate of the current touch in virtual screen coordinates */
	public final float getY()
	{
		_points[0] = 0.f;
		_points[1] = _y;
		_kernel.getVirtualScreen().getTransform().invert(_inverse);
		_inverse.mapPoints(_points);
		return _points[1];
	}
	
	/** Called by the kernel when a touch event begins */
	final void onPress(float x, float y)
	{
		_isDown = true;
		_x = x;
		_y = y;
	}
	
	/** Called by the kernel when a finger touching the screen moves without lifting */
	final void onMove(float x, float y)
	{
		_x = x;
		_y = y;
	}
		
	/** Called by the kernel when a finger touching the screen is lifted */
	final void onRelease()
	{
		_isDown = false;
	}
}
