package dkilian.andy;

import android.graphics.Matrix;
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
	/** The transformation matrix of this sprite */
	private Matrix _transform;
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
		_animation = anim;
		_transform = new Matrix();
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

	/** Gets this sprite's transformation matrix */
	@Override
	public Matrix getTransform() 
	{
		return _transform;
	}

	/** Sets this sprite's transformation matrix */
	@Override
	public void setTransform(Matrix m) 
	{
		_transform = m;
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
		s.setTransform(_transform);
		s.setClipRectEnabled(_clipEnabled);
		s.setClipRect(_clipRect);
		s.draw(kernel);
	}
}
