package dkilian.andy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * A context storing data used by TexturedQuad.render() overloads to
 * render data using the Canvas/Paint API so that it can be drawn 
 * using the OpenGL ES API.
 * 
 * When using TexturedQuad.render() methods, it is highly recommended
 * that you create one PrerenderContext and reuse it throughout the game
 * if you plan on pre-rendering data every frame of the game.
 * 
 * Pre-rendering is also considered expensive, and should be avoided
 * if at all possible. At the moment, pre-rendering is the only way
 * to render text using AGL
 *  
 * @author dkilian
 */
public class PrerenderContext 
{
	/** The bitmap to render to */
	private Bitmap _b;
	/** The canvas that renders to the bitmap */
	private Canvas _c;
	/** The render parameter context */
	private Paint _p;
	
	/**
	 * Creates a new pre-rendering context with its own Paint object and
	 * no backing Bitmap/Canvas pair
	 */
	public PrerenderContext()
	{
		_p = new Paint();
	}
	
	/**
	 * Creates a new pre-rendering context bound to the given Paint object,
	 * with no backing Bitmap/Canvas pair
	 * @param p The paint object to use when pre-rendering with this canvas
	 */
	public PrerenderContext(Paint p)
	{
		_p = p;
	}
	
	/**
	 * Creates a new pre-rendering context with its own Paint object and 
	 * a backing Bitmap/Canvas pair with the given dimensions
	 * @param w The width of the backing data store (also the width of sprites generated with this context)
	 * @param h The height of the backing data store (also the height of sprites generated with this context)
	 */
	public PrerenderContext(int w, int h)
	{
		_b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		_c = new Canvas(_b);
		_p = new Paint();
		
		clear();
	}
	
	/**
	 * Creates a new pre-rendering context bound to the given Paint object, with
	 * a backing Bitmap/Canvas pair with the given dimensions
	 * @param w The width of the backing data store (also the width of sprites generated with this context)
	 * @param h The height of the backing data store (also the height of sprites generated with this context)
	 * @param p The paint object to use when pre-rendering with this canvas
	 */
	public PrerenderContext(int w, int h, Paint p)
	{
		_b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		_c = new Canvas(_b);
		_p = p;
		
		clear();
	}
	
	/** Gets the bitmap containing data rendered to this context */
	public Bitmap getBitmap()
	{
		return _b;
	}
	
	/** Gets the canvas that draws data to this context */
	public Canvas getCanvas()
	{
		return _c;
	}
	
	/** Gets the rendering parameter context */
	public Paint getPaint()
	{
		return _p;
	}
	
	/**
	 * Sets the backing storage used by this context
	 * @param b The bitmap containing data rendered to this context
	 * @param c The canvas that writes render data to this context's bitmap
	 */
	public void setStorage(Bitmap b, Canvas c)
	{
		_b = b;
		_c = c;
	}
	
	/** Sets this canvas's render parameter context */
	public void setPaint(Paint p)
	{
		_p = p;
	}
	
	/** Clears this context to transparent black */
	public void clear()
	{
		_b.eraseColor(Color.TRANSPARENT);
	}
}
