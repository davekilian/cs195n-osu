package dkilian.andy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Calculates the transformation between virtual screen coordinates and
 * physical device coordinates
 * 
 * @author dkilian
 */
public class VirtualScreen 
{
	private int _vw, _vh;		  // The width and height of the virtual screen
	private int _pw, _ph;		  // The width and height of the device's physical screen
	private Matrix _transform;	  // The precomputed transform from virtual to physical coords
	private RectF[] _letterboxes; // The precomputed letterboxes, if applicable
	
	/** Creates a new virtual screen */
	public VirtualScreen()
	{
		_vw = _vh = 1;
		_pw = _ph = 0;
		_transform = new Matrix();
		_letterboxes = new RectF[2];
		_letterboxes[0] = new RectF();
		_letterboxes[1] = new RectF();
	}
	
	/** Gets the width of the virtual screen */
	public final int getWidth()
	{
		return _vw;
	}
	
	/** Sets the width of the virtual screen */
	public final void setWidth(int w)
	{
		_vw = w;
	}
	
	/** Gets the height of the virtual screen */
	public final int getHeight()
	{
		return _vh;
	}
	
	/** Sets the height of the virtual screen */
	public final void setHeight(int h)
	{
		_vh = h;
	}
	
	/** Calculates the aspect ratio of the virtual screen */
	public final float getAspect()
	{
		return (float)_vw / (float)_vh;
	}
	
	/** Gets the width of the device's physical screen */
	public final int getPhysicalWidth()
	{
		return _pw;
	}
	
	/** Sets the width used by this object as the device's physical screen's width */
	public final void setPhysicalWidth(int w)
	{
		_pw = w;
	}
	
	/** Gets the height of the device's physical screen */
	public final int getPhysicalHeight()
	{
		return _ph;
	}
	
	/** Sets the height used by this object as the device's physical screen's height */
	public final void setPhysicalHeight(int h)
	{
		_ph = h;
	}
	
	/** Computes the aspect ratio of the device's physical screen */
	public final float getPhysicalAspect()
	{
		return (float)_pw / (float)_ph;
	}
	
	/** Sets the dimensions of the physical screen by querying the current view */
	public final void setPhysicalDimensions(KernelView view)
	{
		_pw = view.getWidth();
		_ph = view.getHeight();
	}

	/** Sets the dimensions of the physical screen by querying the current view */
	public final void setPhysicalDimensions(KernelGLView view)
	{
		_pw = view.getWidth();
		_ph = view.getHeight();
	}
	
	/**
	 * Computes the transformation from virtual coordinates to physical screen
	 * coordinates. This should only be done when the dimensions are changed, or
	 * letterboxing is toggled. 
	 * @param letterboxingEnabled If true, the transformation will cause letterboxes
	 * 							  to appear on screens with different aspect ratios
	 * 							  than the virtual screen. If false, the transformation
	 * 							  will stretch the virtual screen to fill the physical screen
	 */
	public final void computeTransform(boolean letterboxingEnabled)
	{
		if (letterboxingEnabled)
		{
			_transform.reset();
			
			float scalew = (float)_pw / (float)_vw, scaleh = (float)_ph / (float)_vh;
			if (scalew < scaleh)
			{
				_transform.setScale(scalew, scalew);
				
				float dy = .5f * (_ph - scalew * _vh);
				_transform.postTranslate(0, dy);
				
				_letterboxes[0].top = -dy;
				_letterboxes[0].bottom = 0;
				_letterboxes[0].left = 0;
				_letterboxes[0].right = _vw;
				
				_letterboxes[1].top = _vh;
				_letterboxes[1].bottom = _vh + dy;
				_letterboxes[1].left = 0;
				_letterboxes[1].right = _vw;
			}
			else
			{
				_transform.setScale(scaleh, scaleh);
				
				float dx = .5f * (_pw - scaleh * _vw);
				_transform.postTranslate(dx, 0);
				
				_letterboxes[0].top = 0;
				_letterboxes[0].bottom = _vh;
				_letterboxes[0].left = -dx;
				_letterboxes[0].right = 0;
				
				_letterboxes[1].top = 0;
				_letterboxes[1].bottom = _vh;
				_letterboxes[1].left = _vw;
				_letterboxes[1].right = _vw + dx;
			}
		}
		else
		{
			_transform.reset();
			_transform.setScale(_pw / _vw, _ph / _vh);
		}
	}
	
	/** Gets the transformation from virtual coordinates to physical coordinates */
	public final Matrix getTransform()
	{
		return _transform;
	}
	
	/** Draws black letterboxes on the screen */
	public final void drawLetterbox(Kernel k)
	{
		Paint p = k.getView().getPaint();
		Canvas c = k.getView().getCanvas();
		
		p.setColor(Color.BLACK);
		c.drawRect(_letterboxes[0], p);
		c.drawRect(_letterboxes[1], p);
	}
}
