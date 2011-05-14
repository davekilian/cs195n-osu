package osu.controls;

import osu.game.HitObject;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

/**
 * The image that fades in/out briefly when the player misses a hit. 
 * 
 * Each miss control corresponds to a single button. When the button is
 * interacted with, the handler should set this control's cancelled 
 * property to true, so that it does not appear when the button 
 * disappears. 
 * 
 * @author dkilian
 */
public class Miss implements Control
{
	/** The time in partial seconds the miss icon appears on screen for a missed button */
	public static final float ANIMATION_TIME = 1.f;
	
	/** The virtual space location of this miss icon's origin */
	private float _x, _y;
	/** The times in partial seconds this icon appears and disappears */
	private float _tbeg, _tend;
	/** Not used since this icon cannot be interacted with */
	private Rect _bounds;
	/** Whether or not this miss icon's button has been pressed */
	private boolean _buttonHit;
	/** This miss icon's image */
	private TexturedQuad _sprite;
	
	/** Creates a new miss icon corresponding to a button event */
	public Miss(HitObject event)
	{
		_x = event.getX();
		_y = event.getY();
		_tbeg = event.getTiming() / 1000.f;
		_tend = _tbeg + ANIMATION_TIME;
		_bounds = new Rect();
		_buttonHit = false;
	}
	
	/** Creates a new miss icon corresponding to a button event */
	public Miss(HitObject event, TexturedQuad sprite)
	{
		_x = event.getX();
		_y = event.getY();
		_tbeg = event.getTiming() / 1000.f;
		_tend = _tbeg + ANIMATION_TIME;
		_bounds = new Rect();
		_buttonHit = false;
		_sprite = sprite;
	}

	/** Gets a value indicating whether this icon's appearance has been cancelled (e.g. because the button has been pressed) */
	public boolean isCancelled()
	{
		return _buttonHit;
	}
	
	/** Cancels this icon (e.g. because the button has been pressed) */
	public void cancel()
	{
		_buttonHit = true;
	}
	
	/** Gets the X coordinate of this icon's origin in virtual space */
	@Override
	public float getX() 
	{
		return _x;
	}

	/** Sets the X coordinate of this icon's origin in virtual space */
	@Override
	public void setX(float x) 
	{
		_x = x;
	}

	/** Gets the Y coordinate of this icon's origin in virtual space */
	@Override
	public float getY() 
	{
		return _y;
	}

	/** Sets the Y coordinate of this icon's origin in virtual space */
	@Override
	public void setY(float y) 
	{
		_y = y;
	}

	/** Gets the time this icon appears, if not cancelled, in partial seconds */
	@Override
	public float getStartTime()
	{
		return _tbeg;
	}

	/** Sets the time this icon appears, if not cancelled, in partial seconds */
	@Override
	public void setStartTime(float t) 
	{
		_tbeg = t;
	}

	/** Gets the time this icon disappears, if not cancelled, in partial seconds */
	@Override
	public float getEndTime() 
	{
		return _tend;
	}

	/** Sets the time this icon disappears, if not cancelled, in partial seconds */
	@Override
	public void setEndTime(float t) 
	{
		_tend = t;
	}

	/** Determines whether this icon is currently visible */
	@Override
	public boolean isVisible(float t) 
	{
		return !_buttonHit && _tbeg <= t && t <= _tend;
	}

	/** Not used, since this icon cannot be interacted with */
	@Override
	public Rect getHitbox() 
	{
		return _bounds;
	}

	/** Not used, since this icon cannot be interacted with */
	@Override
	public void interact(float x, float y, float t) {}

	/** Performs per-frame actions required by this icon */
	@Override
	public void update(Kernel kernel, float t, float dt) {}

	@Override
	public void draw(Kernel kernel, float t, float dt) 
	{		
		if (isVisible(t))
		{
			float alpha = 1.f - (t - _tbeg) / (_tend - _tbeg);
			_sprite.setAlpha(alpha);
			_sprite.getTranslation().x = _x;
			_sprite.getTranslation().y = _y;
			_sprite.getScale().x = _sprite.getScale().y = .75f;
			_sprite.draw(kernel);
		}
	}

	/** Not used */
	@Override
	public HitObject getEvent() 
	{
		return null;
	}
}
