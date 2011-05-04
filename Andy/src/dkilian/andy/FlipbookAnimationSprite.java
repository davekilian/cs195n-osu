package dkilian.andy;

import android.graphics.Rect;

/**
 * A sprite implementation that renders the current frame of a flipbook animation
 * 
 * @author dkilian
 */
public class FlipbookAnimationSprite implements Sprite
{
	/** The animation this sprite renders */
	private FlipbookAnimation _animation;
	/** The translation between the origin and this sprite, in virtual coordinates */
	private Vector2 _translation;
	/** The rotation of this sprite around its center in degrees */
	private float _rotation;
	/** The scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	private Vector2 _scale;
	/** Whether or not the sprite obeys its clip rect property */
	private boolean _clipEnabled;
	/** The region of the sprite rendered during draw() calls */
	private Rect _clipRect;
	
	/**
	 * Creates a new flipbook animation sprite
	 * @param anim The flipbook animation this sprite renders
	 */
	public FlipbookAnimationSprite(FlipbookAnimation anim)
	{
		_translation = Vector2.Zero();
		_rotation = 0.f;
		_scale = Vector2.One();
		_animation = anim;
		_clipEnabled = false;
		_clipRect = new Rect();
	}
	
	/** Gets the flipbook animation this sprite renders */
	public FlipbookAnimation getAnimation()
	{
		return _animation;
	}
	
	/** Sets the flipbook animation this sprite renders */
	public void setAnimation(FlipbookAnimation anim)
	{
		_animation = anim;
	}

	/** Gets the width of this sprite, in pixels */
	@Override
	public int getWidth() 
	{
		return _animation.getFlipbook().getFrame(_animation.getFrame()).getWidth();
	}

	/** Gets the height of this sprite, in pixels */
	@Override
	public int getHeight()
	{
		return _animation.getFlipbook().getFrame(_animation.getFrame()).getHeight();
	}

	/** Gets a value indicating whether the clip rectangle is obeyed when rendering this sprite */
	@Override
	public boolean getClipRectEnabled() 
	{
		return _clipEnabled;
	}

	/** Sets a value indicating whether the clip rectangle is obeyed when rendering this sprite */
	@Override
	public void setClipRectEnabled(boolean b) 
	{
		_clipEnabled = b;
	}

	/** Gets the region of this sprite rendered when the clip rectangle is enabled */
	@Override
	public Rect getClipRect() 
	{
		return _clipRect;
	}

	/** Sets the region of this sprite rendered when the clip rectangle is enabled */
	@Override
	public void setClipRect(Rect r) 
	{
		_clipRect = r;
	}

	/** Renders this sprite */
	@Override
	public void draw(Kernel kernel) 
	{
		Sprite s = _animation.getFlipbook().getFrame(_animation.getFrame());
		s.getTranslation().set(_translation);
		s.setRotation(_rotation);
		s.getScale().set(_scale);
		s.setClipRectEnabled(_clipEnabled);
		s.setClipRect(_clipRect);
		s.draw(kernel);
	}

	/** Gets the translation between this sprite and the origin, in virtual coordinates */
	@Override
	public Vector2 getTranslation() 
	{
		return _translation;
	}

	/** Sets the translation between this sprite and the origin, in virtual coordinates */
	@Override
	public void setTranslation(Vector2 t) 
	{
		_translation = t;
	}

	/** Gets the world-space rotation of this sprite around its origin, in degrees */
	@Override
	public float getRotation() 
	{
		return _rotation;
	}

	/** Sets the world-space rotation of this sprite around its origin, in degrees */
	@Override
	public void setRotation(float rot) 
	{
		_rotation = rot;
	}

	/** Gets the scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	@Override
	public Vector2 getScale() 
	{
		return _scale;
	}

	/** Sets the scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	@Override
	public void setScale(Vector2 s) 
	{
		_scale = s;
	}
}
