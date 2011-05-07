package dkilian.andy;

import dkilian.andy.jni.agl;
import android.graphics.Paint;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Renders Canvas objects (i.e. text, circles, lines) to textures to
 * render using textured quads / the Andy OpenGL API
 * @author dkilian
 */
public class Prerender
{
	/** Because drawRoundRect() doesn't have an overload that just takes floats T_T */
	private static RectF _r = new RectF();
	
	/**
	 * Creates the smallest context that can hold the given string rendered with
	 * the given paint object's text-rendering parameters
	 * @param text The string the context must be able to hold
	 * @param p The paint object containing the text parameters that will be used
	 *          to draw text into the context created
	 * @return The smallest context able to hold the given string rendered using
	 *         the given paint object's parameters. The context returned will be
	 *         bound to the supplied paint object
	 */
	public static PrerenderContext contextForString(String text, Paint p)
	{
		int width = (int)(p.measureText(text) + 1.f);
		int height = (int)(p.getTextSize() + 1.f);
		
		return new PrerenderContext(width, height, p);
	}
	
	/**
	 * Creates the smallest context that can hold a circle with the given radius
	 * @param r The radius of the circle
	 * @param p The paint object to bind to the context
	 * @return The smallest context able to hold a circle with the given radius,
	 *         bound to the given paint object
	 */
	public static PrerenderContext contextForCircle(float r, Paint p)
	{
		return new PrerenderContext((int)(2.f * r), (int)(2.f * r), p);
	}
	
	/**
	 * Creates the smallest context that can hold the given line
	 * @param a One of the endpoints of the line
	 * @param b One of the endpoints of the line
	 * @param p The paint object to bind to the context
	 * @return The smallest context able to hold the given line, bound to the
	 *         given paint object.
	 */
	public static PrerenderContext contextForLine(Vector2 a, Vector2 b, Paint p)
	{
		return new PrerenderContext((int)Math.abs(a.x - b.x), (int)Math.abs(a.y - b.y), p);
	}
	
	/**
	 * Creates the smallest context that can hold the given rectangle
	 * @param w The width of the rectnagle
	 * @param h The height of the rectangle
	 * @param p The paint object to bind to the context
	 * @return The smallest context able to hold the given rectangle, bound to
	 *         the given paint object.
	 */
	public static PrerenderContext contextForRect(float w, float h, Paint p)
	{
		return new PrerenderContext((int)w, (int)h, p);
	}
	
	/**
	 * Creates the smallest context that can have the given rounded rectangle
	 * @param w The width of the rectangle
	 * @param h The height of the rectangle
	 * @param p The paint object to bind to the context
	 * @return The smallest context able to hold the given rounded rectangle,
	 *         bound to the given paint object
	 */
	public static PrerenderContext contextForRoundedRect(float w, float h, Paint p)
	{
		return contextForRect(w, h, p);
	}
	
	/**
	 * Renders a string to a textured quad. If you plan on calling this method every
	 * frame of your game, allocate and reuse a PrerenderContext (see contextForString)
	 * @param text The string to render
	 * @param p The parameters with which to render the text
	 * @return A textured quad containing the rendered text
	 */
	public static TexturedQuad string(String text, Paint p)
	{
		return string(text, contextForString(text, p));
	}
	
	/**
	 * Renders a string to a textured quad
	 * @param text The string to render
	 * @param context A rendering context to use to hold intermediate data
	 * @returnÊA textured quad containing the rendered text
	 */
	public static TexturedQuad string(String text, PrerenderContext context)
	{
		context.clear();
		context.getCanvas().drawText(text, 0.f, context.getBitmap().getHeight(), context.getPaint());
		
		return new TexturedQuad(context.getBitmap());
	}
	
	/**
	 * Renders a string to a textured quad
	 * @param text The string to render
	 * @param context A rendering context to use to hold intermediate data
	 * @param target The quad to render to. This quad must have the same dimensions as the
	 *               context being used. Any existing data in the quad will be discarded.
	 */
	public static void string(String text, PrerenderContext context, TexturedQuad target)
	{
		context.clear();
		context.getCanvas().drawText(text, 0.f, 0.f, context.getPaint());
		
		agl.BindTexture(target.getTexture());
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, context.getBitmap(), 0);
	}
	
	/**
	 * Draws a circle to a textured quad
	 * @param r The radius of the circle
	 * @param p The paint object with which to render the circle
	 * @return A quad containing a circle with the given radius rendered using the given
	 *         paint object's parameters
	 */
	public static TexturedQuad circle(float r, Paint p)
	{
		return circle(r, contextForCircle(r, p));
	}
	
	/**
	 * Draws a circle to a textured quad
	 * @param r The radius of the circle
	 * @param context A rendering context to use to hold intermediate data
	 * @return A quad containing a circle with the given radius rendered using the given
	 *         paint object's parameters
	 */
	public static TexturedQuad circle(float r, PrerenderContext context)
	{
		context.clear();
		context.getCanvas().drawCircle(r, r, r, context.getPaint());
		
		return new TexturedQuad(context.getBitmap());
	}
	
	/**
	 * Draws a circle to a textured quad
	 * @param r The radius of the circle
	 * @param context A rendering context to use to hold intermediate data
	 * @param target A quad to receive the rendered circle. Must have the same dimensions as
	 *               the given context. Any existing data will be discarded.
	 */
	public static void circle(float r, PrerenderContext context, TexturedQuad target)
	{
		context.clear();
		context.getCanvas().drawCircle(r, r, r, context.getPaint());
		
		agl.BindTexture(target.getTexture());
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, context.getBitmap(), 0);
	}
	
	/**
	 * Draws a line. Any offset from the origin will be ignored (i.e. (0, 0) -> (1, 1)
	 * prdocues the same results as (50, 50) -> (51, 51))
	 * @param a One of the endpoints of the line to draw
	 * @param b One of the endpoints of the line to draw
	 * @param p The paint with which to draw the line
	 * @return A quad containing the line drawn
	 */
	public static TexturedQuad line(Vector2 a, Vector2 b, Paint p)
	{
		return line(a, b, contextForLine(a, b, p));
	}

	/**
	 * Draws a line. Any offset from the origin will be ignored (i.e. (0, 0) -> (1, 1)
	 * prdocues the same results as (50, 50) -> (51, 51))
	 * @param a One of the endpoints of the line to draw
	 * @param b One of the endpoints of the line to draw
	 * @param context A rendering context to use to hold intermediate data
	 * @return A quad containing the line drawn
	 */
	public static TexturedQuad line(Vector2 a, Vector2 b, PrerenderContext context)
	{
		float x1 = a.x, y1 = a.y, x2 = b.x, y2 = b.y;
		if (x1 > x2)
		{
			x1 -= x2;
			x2 = 0.f;
		}
		else
		{
			x2 -= x1;
			x1 = 0.f;
		}
		
		if (y1 > y2)
		{
			y1 -= y2;
			y2 = 0.f;
		}
		else
		{
			y2 -= y1;
			y2 = 0.f;
		}
		
		context.clear();
		context.getCanvas().drawLine(x1, y1, x2, y2, context.getPaint());
		
		return new TexturedQuad(context.getBitmap());
	}

	/**
	 * Draws a line. Any offset from the origin will be ignored (i.e. (0, 0) -> (1, 1)
	 * prdocues the same results as (50, 50) -> (51, 51))
	 * @param a One of the endpoints of the line to draw
	 * @param b One of the endpoints of the line to draw
	 * @param context A rendering context to use to hold intermediate data
	 * @param target Receives the line drawn. Must have the same dimensions as the
	 *               given context. Any existing data will be discarded.
	 */
	public static void line(Vector2 a, Vector2 b, PrerenderContext context, TexturedQuad target)
	{
		float x1 = a.x, y1 = a.y, x2 = b.x, y2 = b.y;
		if (x1 > x2)
		{
			x1 -= x2;
			x2 = 0.f;
		}
		else
		{
			x2 -= x1;
			x1 = 0.f;
		}
		
		if (y1 > y2)
		{
			y1 -= y2;
			y2 = 0.f;
		}
		else
		{
			y2 -= y1;
			y2 = 0.f;
		}
		
		context.clear();
		context.getCanvas().drawLine(x1, y1, x2, y2, context.getPaint());
		
		agl.BindTexture(target.getTexture());
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, context.getBitmap(), 0);
	}
	
	/**
	 * Renders a rectangle
	 * @param w The width of the rectangle
	 * @param h The height of the rectangle
	 * @param p The paint with which to draw the rectangle
	 * @return A textured quad containing the rectangle rendered
	 */
	public TexturedQuad rectangle(float w, float h, Paint p)
	{
		return rectangle(w, h, contextForRect(w, h, p));
	}
	
	/**
	 * Renders a rectangle
	 * @param w The width of the rectangle
	 * @param h The height of the rectangle
	 * @param context A rendering context to use to hold intermediate data
	 * @return A textured quad containing the rectangle rendered
	 */
	public TexturedQuad rectangle(float w, float h, PrerenderContext context)
	{
		context.clear();
		context.getCanvas().drawRect(0, 0, w, h, context.getPaint());
		
		return new TexturedQuad(context.getBitmap());
	}
	
	/**
	 * Renders a rectangle
	 * @param w The width of the rectangle
	 * @param h The height of the rectangle
	 * @param context A rendering context to use to hold intermediate data
	 * @param target The textured quad to receive the rectangle rendered. Must have the
	 *               same dimensions as the supplied context. Any existing data will be
	 *               discarded
	 */
	public void rectangle(float w, float h, PrerenderContext context, TexturedQuad target)
	{
		context.clear();
		context.getCanvas().drawRect(0, 0, w, h, context.getPaint());
		
		agl.BindTexture(target.getTexture());
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, context.getBitmap(), 0);
	}
	
	/**
	 * Renders a rounded rectangle
	 * @param w The width of the rectangle
	 * @param h The height of the rectangle
	 * @param r The radius of the rounded corners
	 * @param p The paint with which to render the rounded rectangle
	 * @return A texture containing the rounded rectangle
	 */
	public TexturedQuad roundedRectangle(float w, float h, float r, Paint p)
	{
		return roundedRectangle(w, h, r, contextForRoundedRect(w, h, p));
	}

	/**
	 * Renders a rounded rectangle
	 * @param w The width of the rectangle
	 * @param h The height of the rectangle
	 * @param r The radius of the rounded corners
	 * @param context A rendering context to use to hold intermediate data
	 * @return A texture containing the rounded rectangle
	 */
	public TexturedQuad roundedRectangle(float w, float h, float r, PrerenderContext context)
	{	
		_r.top = 0.f;
		_r.left = 0.f;
		_r.right = w;
		_r.bottom = h;
		
		context.clear();
		context.getCanvas().drawRoundRect(_r, r, r, context.getPaint());
		
		return new TexturedQuad(context.getBitmap());
	}

	/**
	 * Renders a rounded rectangle
	 * @param w The width of the rectangle
	 * @param h The height of the rectangle
	 * @param r The radius of the rounded corners
	 * @param context A rendering context to use to hold intermediate data
	 * @param target A texture containing the rounded rectangle
	 */
	public void roundedRectangle(float w, float h, float r, PrerenderContext context, TexturedQuad target)
	{
		_r.top = 0.f;
		_r.left = 0.f;
		_r.right = w;
		_r.bottom = h;
		
		context.clear();
		context.getCanvas().drawRoundRect(_r, r, r, context.getPaint());
		
		agl.BindTexture(target.getTexture());
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, context.getBitmap(), 0);
	}
}
