package dkilian.andy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Kernel helper for rendering the game
 * 
 * @author dkilian
 */
public class KernelView extends View 
{
	/** The cached paint object */
	private Paint _paint;
	/** The cached canvas. Invalided after onDraw() returns */
	private Canvas _canvas;
	/** The executing kernel */
	private Kernel _kernel;
	/** True if the virtual screen transform should be recomputed soon, false otherwise */
	private boolean _resized;
	
	/**
	 * Creates a new KernelView. Games inheriting KernelActivity don't need to call this.
	 * @param kernel The executing kernel
	 * @param context The activity spawning this view
	 */
	public KernelView(Kernel kernel, Context context) 
	{
		super(context);
		_paint = new Paint();
		_kernel = kernel;
		_resized = false;
	}

	/** Renders the current screen */
	@Override
	public final void onDraw(Canvas c)
	{
		_canvas = c;
		
		Screen s = _kernel.getScreen();
		if (s != null)
		{
			if (_resized)
			{
				_resized = false;
				_kernel.getVirtualScreen().setPhysicalDimensions(this);
				_kernel.getVirtualScreen().computeTransform(true);
			}
			
			_canvas.save();
			_canvas.concat(_kernel.getVirtualScreen().getTransform());
			s.draw(_kernel, _kernel.getDrawTimeDelta());
			_canvas.restore();
		}
		else
		{
			_paint.setAntiAlias(true);
			_paint.setColor(Color.WHITE);
			_paint.setTextSize(16);
			String text = "Empty Screen stack. gg yo.";
			_canvas.drawText(text, .5f * (getWidth() - _paint.measureText(text)), .5f * (getHeight() - 16), _paint);
		}
		
		_canvas = null;
	}
	
	/** Gets the Paint object to use for custom drawing operations */
	public final Paint getPaint()
	{
		return _paint;
	}
	
	/** Gets the Canvas object to use for custom drawing operations */
	public final Canvas getCanvas()
	{
		return _canvas;
	}
	
	/** Causes the virtual screen transform to be recomputed next onDraw() */
	public void queueResize()
	{
		_resized = true;
	}
}
