package osu.controls;

import osu.game.HOSpinner;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

/**
 * Handles interaction with a spinner, wrapping a corresponding hit object
 * 
 * @author dkilian
 */
public class Spinner implements Control
{
	/** The amount of time it takes for a button to fade in, in partial seconds */
	public static final float FADE_IN_TIME  = .3f;
	/** The amount of time it takes for a button to fade out, in partial seconds */
	public static final float FADE_OUT_TIME = .1f;
	
	/** Unused */
	private float _x;
	/** Unused */
	private float _y;
	/** The time this spinner begins fading in */
	private float _tbeg;
	/** The time this spinner finishes fading out */
	private float _tend;
	/** The bounds of this spinner */
	private Rect _bounds;
	/** This spinner's graphics */
	private TexturedQuad _spinner, _noFill, _fill, _mask;
	/** The previous touch point */
	private float _prevX, _prevY;
	/** Whether or not this spinner is being dragged */
	private boolean _isDown;
	/** The angle, in degrees, this spinner has been rotated so far */
	private float _rotation;
	/** The event this spinner wraps */
	private HOSpinner _event;
	/** The charge level gained by this spinner per full CCW rotation */
	private float _chargePerRotation;
	
	/**
	 * Creates a new spinner
	 * @param event The event this spinner wraps
	 */
	public Spinner(HOSpinner event)
	{
		_event = event;
		_x = event.getX();
		_y = event.getY();
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
		_prevX = _prevY = 0.f;
		_isDown = false;
		_rotation = 0.f;
	}
	
	/**
	 * Creates a new spinner
	 * @param event The event this spinner wraps
	 * @param spinner The graphic centered on the screen that is spun by the user
	 * @param noFill The graphic that covers the fill layer, making the power bar seem not filled. Will be clipped based on charge amount
	 * @param fill The graphic that makes the power bar seem filled.
	 * @param mask The mask that is drawn above the power bar and spinner graphic.
	 */
	public Spinner(HOSpinner event, TexturedQuad spinner, TexturedQuad noFill, TexturedQuad fill, TexturedQuad mask)
	{
		_event = event;
		_x = event.getX();
		_y = event.getY();
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
		_prevX = _prevY = 0.f;
		_isDown = false;
		_rotation = 0.f;
		
		_spinner = spinner;
		_noFill = noFill;
		_fill = fill;
		_mask = mask;
	}

	@Override
	public float getX() 
	{
		return _x;
	}

	@Override
	public void setX(float x) 
	{		
		_x = x;
	}

	@Override
	public float getY() 
	{
		return _y;
	}

	@Override
	public void setY(float y) 
	{
		_y = y;
	}

	@Override
	public float getStartTime() 
	{
		return _tbeg;
	}

	@Override
	public void setStartTime(float t) 
	{
		_tbeg = t;
	}

	@Override
	public float getEndTime() 
	{
		return _tend;
	}

	@Override
	public void setEndTime(float t) 
	{
		_tend = t;
	}

	@Override
	public boolean isVisible(float t) 
	{
		return t >= _tbeg && t <= _tend;
	}

	@Override
	public Rect getHitbox() 
	{
		return _bounds;
	}

	@Override
	public void interact(float x, float y, float t) 
	{
	}

	@Override
	public void update(Kernel kernel, float t, float dt) 
	{
	}

	@Override
	public void draw(Kernel kernel, float t, float dt) 
	{
	}
}
