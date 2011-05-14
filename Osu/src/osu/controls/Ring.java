package osu.controls;

import osu.game.ComboColor;
import osu.graphics.BitmapTint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

/**
 * An approach ring used to denote the timings of different controls
 * 
 * @author dkilian
 */
public class Ring implements Control
{
	/** The X coordinate of the center onto which this approach ring converges in virtual space */
	private float _x;
	/** The Y coordinate of the center onto which this approach ring converges in virtual space */
	private float _y;
	/** The time this ring appears */
	private float _tbeg;
	/** The time this ring disappears */
	private float _tend;
	/** Not used, since the ring cannot be interacted with */
	private Rect _bounds;
	/** The alpha value this ring has when it first appears, if animating */
	private float _alphabeg;
	/** The alpha value this ring has when it disappears, if animating */
	private float _alphaend;
	/** The scale factor this ring has when it first appears, if animating */
	private float _scalebeg;
	/** The scale factor htis ring has when it disappears, if animating */
	private float _scaleend;
	/** Whether this ring is being automatically animated or has its alpha/scale values manually set */
	private boolean _animating;
	/** The current alhpa value, if not animating */
	private float _alpha;
	/** The current scale value, if not animating */
	private float _scale;
	/** The sprite containing this ring */
	private TexturedQuad _sprite;
	
	/**
	 * Renders a ring from its constituent parts
	 * @param ring The ring image, which is normally colored the same as the hit object it belongs to
	 * @param shadow The drop shadow below the ring image
	 * @return The final ring image
	 */
	public static Bitmap render(Bitmap ring, Bitmap shadow)
	{
		Bitmap target = Bitmap.createBitmap(ring.getWidth(), ring.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(target);
		c.setDensity(ring.getDensity());
		Paint p = new Paint();
		
		c.drawBitmap(shadow, 0.f, 0.f, p);
		c.drawBitmap(ring, 0.f, 0.f, p);
		
		return target;
	}

	/**
	 * Renders a ring from its constituent parts
	 * @param ring The ring image, which is normally colored the same as the hit object it belongs to
	 * @param shadow The drop shadow below the ring image
	 * @param color The ARGB color to ting the ring image
	 * @return The final ring image
	 */
	public static Bitmap render(Bitmap ring, Bitmap shadow, int color)
	{
		ring = BitmapTint.apply(ring, color);
		return render(ring, shadow);
	}

	/**
	 * Renders a ring from its constituent parts
	 * @param ring The ring image, which is normally colored the same as the hit object it belongs to
	 * @param shadow The drop shadow below the ring image
	 * @param color The ARGB color to ting the ring image
	 * @return The final ring image
	 */
	public static Bitmap render(Bitmap ring, Bitmap shadow, ComboColor color)
	{
		ring = BitmapTint.apply(ring, color);
		return render(ring, shadow);
	}
	
	/**
	 * Creates an animated approach ring
	 * @param sprite This ring's image
	 */
	public Ring(TexturedQuad sprite)
	{
		_x = 0.f;
		_y = 0.f;
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
		_alphabeg = 0.f;
		_alphaend = 1.f;
		_scalebeg = 3.f;
		_scaleend = 1.f;
		_animating = true;
		_alpha = 1.f;
		_scale = 0.f;
		_sprite = sprite;
	}
	
	/**
	 * Creates an approach ring that is not animated
	 * @param sprite This ring's image
	 * @param x The X coordinate of the center of this ring, in virtual space
	 * @param y The Y coordinate of the center of this ring, in virtual space
	 * @param alpha This ring's alpha factor, in [0, 1]
	 * @param scale This ring's uniform scale factor
	 */
	public Ring(TexturedQuad sprite, float x, float y, float alpha, float scale)
	{
		_x = x;
		_y = y;
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
		_alphabeg = 0.f;
		_alphaend = 1.f;
		_scalebeg = 3.f;
		_scaleend = 1.f;
		_animating = false;
		_alpha = alpha;
		_scale = scale;
		_sprite = sprite;
	}
	
	/**
	 * Creates an animated approach ring
	 * @param sprite This ring's image
	 * @param x The X coordinate of the center of this ring, in virtual space
	 * @param y The Y coordinate of the center of this ring, in virtual space
	 * @param alphabeg This ring's alpha factor, in [0, 1], at the beginning of the animation 
	 * @param alphaend This ring's alpha factor, in [0, 1], at the end of the animation
	 * @param scalebeg This ring's scale factor, in [0, 1], at the beginning of the animation
	 * @param scaleend This ring's scale factor, in [0, 1], at the end of the animation
	 * @param start The animation's start time, in partial seconds
	 * @param end The animation's end time, in partial seconds
	 */
	public Ring(TexturedQuad sprite, float x, float y, float alphabeg, float alphaend, float scalebeg, float scaleend, float start, float end)
	{
		_x = x;
		_y = y;
		_tbeg = start;
		_tend = end;
		_bounds = new Rect();
		_alphabeg = alphabeg;
		_alphaend = alphaend;
		_scalebeg = scalebeg;
		_scaleend = scaleend;
		_animating = true;
		_alpha = 1.f;
		_scale = 1.f;
		_sprite = sprite;
	}
	
	/** Gets the sprite containing this ring's image */
	public TexturedQuad getSprite()
	{
		return _sprite;
	}
	
	/** Sets the sprite containing this ring's image */
	public void setSprite(TexturedQuad sprite)
	{
		_sprite = sprite;
	}
	
	/** Gets a value indicating whether this ring's visibility, alpha value, and scale are affected by animation criteria */
	public boolean isAnimated()
	{
		return _animating;
	}

	/** Sets a value indicating whether this ring's visibility, alpha value, and scale are affected by animation criteria */
	public void setAnimated(boolean anim)
	{
		_animating = anim;
	}
	
	/** Gets this ring's alpha value, if it is not animating */
	public float getAlpha()
	{
		return _alpha;
	}

	/** Sets this ring's alpha value, if it is not animating */
	public void setAlpha(float a)
	{
		_alpha = a;
	}
	
	/** Gets this ring's uniform scale factor, if it is not animating */
	public float getScale()
	{
		return _scale;
	}

	/** Sets this ring's uniform scale factor, if it is not animating */
	public void setScale(float scale)
	{
		_scale = scale;
	}
	
	/** Gets this ring's alpha at the beginning of its animation, if applicable */
	public float getStartAlpha()
	{
		return _alphabeg;
	}

	/** Sets this ring's alpha at the beginning of its animation, if applicable */
	public void setStartAlpha(float alpha)
	{
		_alphabeg = alpha;
	}

	/** Gets this ring's alpha at the end of its animation, if applicable */
	public float getEndAlpha()
	{
		return _alphaend;
	}

	/** Sets this ring's alpha at the end of its animation, if applicable */
	public void setEndAlpha(float alpha)
	{
		_alphaend = alpha;
	}
	
	/** Gets this ring's uniform scale factor at the beginning of its animation, if applicable */
	public float getStartScale()
	{
		return _scalebeg;
	}

	/** Sets this ring's uniform scale factor at the beginning of its animation, if applicable */
	public void setStartScale(float scale)
	{
		_scalebeg = scale;
	}

	/** Gets this ring's uniform scale factor at the end of its animation, if applicable */
	public float getEndScale()
	{
		return _scaleend;
	}

	/** Sets this ring's uniform scale factor at the end of its animation, if applicable */
	public void setEndScale(float scale)
	{
		_scaleend = scale;
	}

	/** Gets the X coordinate of the center of this ring, in virtual space */
	@Override
	public float getX() 
	{
		return _x;
	}

	/** Sets the X coordinate of the center of this ring, in virtual space */
	@Override
	public void setX(float x) 
	{		
		_x = x;
	}

	/** Gets the Y coordinate of the center of this ring, in virtual space */
	@Override
	public float getY() 
	{
		return _y;
	}

	/** Sets the Y coordinate of the center of this ring, in virtual space */
	@Override
	public void setY(float y) 
	{
		_y = y;
	}

	/** Gets the time this ring's animation begins, if it is animated */
	@Override
	public float getStartTime() 
	{
		return _tbeg;
	}

	/** Sets the time this ring's animation begins, if it is animated */
	@Override
	public void setStartTime(float t) 
	{
		_tbeg = t;
	}

	/** Gets the time this ring's animation ends, if it is animated */
	@Override
	public float getEndTime() 
	{
		return _tend;
	}

	/** Sets the time this ring's animation ends, if it is animated */
	@Override
	public void setEndTime(float t) 
	{
		_tend = t;
	}

	/** Determines whether this ring is visible. Note that the ring is always visible if it isn't animated */
	@Override
	public boolean isVisible(float t) 
	{
		return !_animating || (t >= _tbeg && t <= _tend);
	}

	/** Not used, since rings cannot be interacted with */
	@Override
	public Rect getHitbox() 
	{
		return _bounds;
	}

	/** Not used, since rings cannot be interacted with */
	@Override
	public void interact(float x, float y, float t) {}

	/** Does per-frame updating required by this ring */
	@Override
	public void update(Kernel kernel, float t, float dt) {}

	/** Renders this ring */
	@Override
	public void draw(Kernel kernel, float t, float dt) 
	{
		if (isVisible(t))
		{
			float alpha = _alpha, scale = _scale;
			if (_animating)
			{
				float pos = (t - _tbeg) / (_tend - _tbeg);
				float oneMinusPos = 1.f - pos;
				
				alpha = oneMinusPos * _alphabeg + pos * _alphaend;
				scale = oneMinusPos * _scalebeg + pos * _scaleend;
			}
			
			_sprite.setAlpha(alpha);
			_sprite.getTranslation().x = _x;
			_sprite.getTranslation().y = _y;
			_sprite.getScale().x = scale;
			_sprite.getScale().y = scale;
			_sprite.draw(kernel);
		}
	}
}
