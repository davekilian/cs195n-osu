package osu.controls;

import java.util.ArrayList;

import osu.game.HOSpinner;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

/**
 * Handles interaction with a spinner, wrapping a corresponding hit object
 * 
 * @author dkilian
 */
public class Spinner implements Control
{
	/** The amount of time it takes for a spinner to fade in, in partial seconds */
	public static final float FADE_IN_TIME  = .3f;
	/** The amount of time it takes for a spinner to fade out, in partial seconds */
	public static final float FADE_OUT_TIME = .7f;
	
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
	/** The amount of power that is drained per second */
	private float _powerDrain;
	/** The power in the power meter, between 0 and 1 */
	private float _power;
	/** The callbacks to notify upon interaction with this spinner */
	private ArrayList<SpinnerCallback> _callbacks;
	/** This spinner's approach ring */
	private Ring _approach;
	
	/**
	 * Creates a new spinner
	 * @param event The event this spinner wraps
	 * @param approach This spinner's optional approach ring
	 */
	public Spinner(HOSpinner event, Ring approach)
	{
		_event = event;
		_x = event.getX();
		_y = event.getY();
		_tbeg = event.getTiming() / 1000.f - FADE_IN_TIME;
		_tend = event.getEndTiming() / 1000.f + FADE_OUT_TIME;
		_bounds = new Rect();
		_prevX = _prevY = 0.f;
		_isDown = false;
		_rotation = 0.f;
		_chargePerRotation = .1f;
		_powerDrain = .1f;
		_power = 0.f;
		_callbacks = new ArrayList<SpinnerCallback>();
		_approach = approach;
	}
	
	/**
	 * Creates a new spinner
	 * @param event The event this spinner wraps
	 * @param spinner The graphic centered on the screen that is spun by the user
	 * @param noFill The graphic that covers the fill layer, making the power bar seem not filled. Will be clipped based on charge amount
	 * @param fill The graphic that makes the power bar seem filled.
	 * @param mask The mask that is drawn above the power bar and spinner graphic.
	 * @param approach This spinner's optional approach ring
	 */
	public Spinner(HOSpinner event, TexturedQuad spinner, TexturedQuad noFill, TexturedQuad fill, TexturedQuad mask, Ring approach)
	{
		_event = event;
		_x = event.getX();
		_y = event.getY();
		_tbeg = event.getTiming() / 1000.f - FADE_IN_TIME;
		_tend = event.getEndTiming() / 1000.f + FADE_OUT_TIME;
		_bounds = new Rect();
		_prevX = _prevY = 0.f;
		_isDown = false;
		_rotation = 0.f;
		_chargePerRotation = .05f;
		_powerDrain = .1f;
		_power = 0.f;
		_callbacks = new ArrayList<SpinnerCallback>();
		_approach = approach;
		
		_spinner = spinner;
		_noFill = noFill;
		_fill = fill;
		_mask = mask;
	}
	
	/** Registers an object to receive notifications from this spinner */
	public void register(SpinnerCallback callback)
	{
		_callbacks.add(callback);
	}
	
	/** Unregisters this object from receiving notifications from this spinner */
	public void unregister(SpinnerCallback callback)
	{
		_callbacks.remove(callback);
	}
	
	/** Gets this spinner's optional approach ring. May be null. */
	public Ring getApproachRing()
	{
		return _approach;
	}

	/** Sets this spinner's optional approach ring. May be null. */
	public void setApproachRing(Ring r)
	{
		_approach = r;
		
		if (_approach != null)
		{
			_approach.setAnimated(true);
			_approach.setStartAlpha(0.f);
			_approach.setEndAlpha(1.f);
			_approach.setStartScale(4.f);
			_approach.setEndScale(0.f);
			_approach.setStartTime(_tbeg);
			_approach.setEndTime(_tend);
		}
	}
	
	/** Gets the angle by which this spinner is rotated, in degrees */
	public float getRotation()
	{
		return _rotation;
	}
	
	/** Sets the angle by which this spinner is rotated, in degrees */
	public void setRotation(float rot)
	{
		_rotation = rot;
	}
	
	/** Gets the power in this spinner's power meter, in [0, 1] */
	public float getPower()
	{
		return _power;
	}

	/** Sets the power in this spinner's power meter, in [0, 1] */
	public void setPower(float power)
	{
		_power = power;
	}
	
	/** Gets the amount of power added per complete rotation of the spinner */
	public float getChargeRate()
	{
		return _chargePerRotation;
	}

	/** Sets the amount of power added per complete rotation of the spinner */
	public void setChargeRate(float chargePerRotation)
	{
		_chargePerRotation = chargePerRotation;
	}
	
	/** Gets the amount of power drained from the spinner per second */
	public float getDrainRate()
	{
		return _powerDrain;
	}

	/** Sets the amount of power drained from the spinner per second */
	public void setDrainRate(float drain)
	{
		_powerDrain = drain;
	}

	/** Unused */
	@Override
	public float getX() 
	{
		return _x;
	}

	/** Unused */
	@Override
	public void setX(float x) 
	{		
		_x = x;
	}

	/** Unused */
	@Override
	public float getY() 
	{
		return _y;
	}

	/** Unused */
	@Override
	public void setY(float y) 
	{
		_y = y;
	}

	/** Gets the time this spinner begins fading in */
	@Override
	public float getStartTime() 
	{
		return _tbeg;
	}

	/** Sets the time this spinner begins fading in */
	@Override
	public void setStartTime(float t) 
	{
		_tbeg = t;
	}

	/** Gets the time this spinner finishes fading out */
	@Override
	public float getEndTime() 
	{
		return _tend;
	}

	/** Sets the time this spinner finishes fading out */
	@Override
	public void setEndTime(float t) 
	{
		_tend = t;
	}

	/** Determines whether this spinner is currently visible */
	@Override
	public boolean isVisible(float t) 
	{
		return t >= _tbeg && t <= _tend;
	}

	/** Gets the hitbox for which this control can get touch input */
	@Override
	public Rect getHitbox() 
	{
		return _bounds;
	}

	/** Notifies this spinner of a user's touch input */
	@Override
	public void interact(float x, float y, float t) 
	{
		if (_isDown)
			for (int i = 0; i < _callbacks.size(); ++i)
				_callbacks.get(i).spinnerEvent(this, _event);
	}

	/** Does required per-frame updating of this spinner */
	@Override
	public void update(Kernel kernel, float t, float dt) 
	{
		if (_approach != null && isVisible(t))
			_approach.update(kernel, t, dt);
		
		if (isVisible(t) && kernel.getTouch().isDown())
		{
			if (!_isDown)
			{
				_isDown = true;
				_prevX = kernel.getTouch().getX();
				_prevY = kernel.getTouch().getY();
			}
			else
			{
				float x = kernel.getTouch().getX();
				float y = kernel.getTouch().getY();
				
				float cx = .5f * kernel.getVirtualScreen().getWidth();
				float cy = .5f * kernel.getVirtualScreen().getHeight();
				
				double theta0 = Math.atan2(_prevY - cy, _prevX - cx);
				double theta  = Math.atan2(y - cy, x - cx);
				
				float delta = (float)((theta - theta0) * 180.0 / Math.PI);
				_rotation -= delta;
				_power += Math.abs(delta / 360.f * _chargePerRotation);
				if (_power > 1.f) _power = 1.f;
				
				_prevX = x;
				_prevY = y;
			}
		}
		else
		{
			_isDown = false;
		}
		
		_power -= _powerDrain * dt;
		if (_power < 0.f) _power = 0.f;
	}

	/** Draws this spinner */
	@Override
	public void draw(Kernel kernel, float t, float dt) 
	{		
		if (isVisible(t))
		{
			float scale = 512.f / _mask.getWidth();
			
			float alpha = 1.f;
			
			if (t >= _tbeg && t <= _tbeg + FADE_IN_TIME)
				alpha = (t - _tbeg) / FADE_IN_TIME;
			else if (t <= _tend && t >= _tend - FADE_OUT_TIME)
				alpha = (_tend - t) / FADE_OUT_TIME;
			
			float cx = .5f * kernel.getVirtualScreen().getWidth();
			float cy = .5f * kernel.getVirtualScreen().getHeight();
			
			_spinner.getTranslation().x = cx;
			_spinner.getTranslation().y = cy;
			_spinner.setRotation(_rotation);
			_spinner.getScale().x = scale;
			_spinner.getScale().y = scale;
			_spinner.setAlpha(alpha);
			_spinner.draw(kernel);
			
			_fill.getTranslation().x = cx;
			_fill.getTranslation().y = cy;
			_fill.getScale().x = scale;
			_fill.getScale().y = scale;
			_fill.setAlpha(alpha);
			_fill.draw(kernel);
			
			agl.Clip(0, 0, kernel.getVirtualScreen().getWidth(), (int)(((1.f - _power) * kernel.getVirtualScreen().getHeight())));
			_noFill.getTranslation().x = cx;
			_noFill.getTranslation().y = cy;
			_noFill.getScale().x = scale;
			_noFill.getScale().y = scale;
			_noFill.setAlpha(alpha);
			_noFill.draw(kernel);
			agl.Clip(0, 0, kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight());
			
			_mask.getTranslation().x = cx;
			_mask.getTranslation().y = cy;
			_mask.getScale().x = scale;
			_mask.getScale().y = scale;
			_mask.setAlpha(alpha);
			_mask.draw(kernel);

			if (_approach != null)
			{
				_approach.setX(cx);
				_approach.setY(cy);
				_approach.draw(kernel, t, dt);
			}
		}
	}
}
