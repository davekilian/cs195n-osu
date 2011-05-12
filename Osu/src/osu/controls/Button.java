package osu.controls;

import java.util.ArrayList;

import osu.game.ComboColor;
import osu.game.HOButton;
import osu.graphics.BitmapTint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

/**
 * Handles button interactions, wrapping a button hit-object.
 * 
 * @author dkilian
 */
public class Button implements Control
{
	/** The amount of time it takes for a button to fade in, in partial seconds */
	public static final float FADE_IN_TIME  = .3f;
	/** The amount of time it takes for a button to fade out, in partial seconds */
	public static final float FADE_OUT_TIME = .3f;
	/** The amount of time, after fading in, the button appears before its event timing, in partial seconds */
	public static final float WAIT_TIME = 1.f;
	/** The amount of time, in partial seconds, the button fades to white after being interacted with */
	public static final float INTERACTED_FADE_OUT_TIME = .5f;
	
	/** The X coordinate of this button's center in virtual space */
	private float _x;
	/** The Y coordinate of this button's center in virtual space */
	private float _y;
	/** The time this button begins to fade in */
	private float _tbeg;
	/** The time this button ends fading out */
	private float _tend;
	/** The bounds of this button in virtual coordinates */
	private Rect _bounds;
	/** The image this button displays when raised */
	private TexturedQuad _up;
	/** The image this button displays when pressed */
	private TexturedQuad _down;
	/** The event this button wraps */
	private HOButton _event;
	/** Callbacks to invoke when interacted with */
	private ArrayList<ButtonCallback> _callbacks;
	/** Whether or not the user has pressed this button */
	private boolean _pressed;
	/** This button's approach ring */
	private Ring _approach;
	
	/**
	 * Renders a button from its components. All components should have the same pixel dimensions.
	 * @param button The fill component of this button
	 * @param shadow The drop shadow below the button
	 * @param chrome The chrome on top of the button
	 * @return A textured quad containing the drop shadow rendered under the button and the chrome rendered over the button
	 */
	public static TexturedQuad render(Bitmap button, Bitmap shadow, Bitmap chrome)
	{
		Bitmap target = Bitmap.createBitmap(button.getWidth(), button.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(target);
		c.setDensity(button.getDensity());
		Paint p = new Paint();
		
		c.drawBitmap(shadow, 0.f, 0.f, p);
		c.drawBitmap(button, 0.f, 0.f, p);
		c.drawBitmap(chrome, 0.f, 0.f, p);
		
		return new TexturedQuad(target);
	}
	
	/**
	 * Tints and renders a button from its components. All components should have the same pixel dimensions.
	 * @param button The monochrome fill component to tint (note that this object will be modified in 
	 * place - reload or copy the original monochrome component to do a different tinting operation)
	 * @param shadow The drop shadow below the fill component
	 * @param chrome The chrome above the fill component
	 * @param color The color to tint the button's fill componenet
	 * @return A textured quad containing the final, tinted button
	 */
	public static TexturedQuad render(Bitmap button, Bitmap shadow, Bitmap chrome, int color)
	{
		button = BitmapTint.apply(button, color);
		return render(button, shadow, chrome);
	}

	/**
	 * Tints and renders a button from its components. All components should have the same pixel dimensions.
	 * @param button The monochrome fill component to tint (note that this object will be modified in 
	 * place - reload or copy the original monochrome component to do a different tinting operation)
	 * @param shadow The drop shadow below the fill component
	 * @param chrome The chrome above the fill component
	 * @param color The color to tint the button's fill componenet
	 * @return A textured quad containing the final, tinted button
	 */
	public static TexturedQuad render(Bitmap button, Bitmap shadow, Bitmap chrome, ComboColor color)
	{
		button = BitmapTint.apply(button, color);
		return render(button, shadow, chrome);
	}
	
	/** Updates the boundaries of this control using its image */
	private void calcBounds()
	{
		int w = 0, h = 0;
		
		if (_up != null)
		{
			w = _up.getWidth();
			h = _up.getHeight();
		}
		else if (_down != null)
		{
			w = _down.getWidth();
			h = _down.getHeight();
		}
		
		_bounds.left   = (int)(_x - w * .5f);
		_bounds.right  = (int)(_x + w * .5f);
		_bounds.top    = (int)(_y - h * .5f);
		_bounds.bottom = (int)(_y + h * .5f);
	}
	
	/** Creates a new button with no image and an optional approach ring */
	public Button(HOButton event, Ring approach)
	{
		_x = event.getX();
		_y = event.getY();
		_tbeg = event.getTiming() / 1000.f - FADE_IN_TIME - WAIT_TIME;
		_tend = event.getTiming() / 1000.f + FADE_OUT_TIME;
		_bounds = new Rect();
		_event = event;
		_callbacks = new ArrayList<ButtonCallback>();
		_pressed = false;
		_approach = approach;
	}
	
	/** Creates a new button consisting of the given images and an optional approach ring */
	public Button(HOButton event, TexturedQuad up, TexturedQuad down, Ring approach)
	{
		_x = event.getX();
		_y = event.getY();
		_tbeg = event.getTiming() / 1000.f - FADE_IN_TIME - WAIT_TIME;
		_tend = event.getTiming() / 1000.f + FADE_OUT_TIME;
		_bounds = new Rect();
		_up = up;
		_down = down;
		_event = event;
		_callbacks = new ArrayList<ButtonCallback>();
		_pressed = false;
		_approach = approach;
		calcBounds();
	}
	
	/** Gets this button's approach ring. May be null */
	public Ring getApproachRing()
	{
		return _approach;
	}

	/** Sets this button's approach ring. May be null */
	public void setApproachRing(Ring approach)
	{
		_approach = approach;
		if (_approach != null && _approach.isAnimated())
		{
			_approach.setX(getX());
			_approach.setY(getY());
			_approach.setStartTime(_tbeg);
			_approach.setEndTime(_event.getTiming() / 1000.f);
		}
	}
	
	public void register(ButtonCallback callback)
	{
		_callbacks.add(callback);
	}
	
	public void unregister(ButtonCallback callback)
	{
		_callbacks.remove(callback);
	}
	
	public HOButton getEvent()
	{
		return _event;
	}
	
	public void setEvent(HOButton event)
	{
		_event = event;
	}
	
	public TexturedQuad getUpImage()
	{
		return _up;
	}
	
	public void setUpImage(TexturedQuad up)
	{
		_up = up;
		calcBounds();
	}
	
	public TexturedQuad getDownImage()
	{
		return _down;
	}
	
	public void setDownImage(TexturedQuad down)
	{
		_down = down;
		calcBounds();
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
		calcBounds();
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
		calcBounds();
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
		if (isVisible(t))
		{
			if (x >= _bounds.left && x <= _bounds.right && y >= _bounds.top && y <= _bounds.bottom)
			{
				_pressed = true;
				
				for (int i = 0; i < _callbacks.size(); ++i)
					_callbacks.get(i).buttonEvent(this, _event);
			}
		}
	}

	@Override
	public void update(Kernel kernel, float t, float dt)
	{
		if (_approach != null)
			_approach.update(kernel, t, dt);
	}

	@Override
	public void draw(Kernel kernel, float t, float dt) 
	{
		if (_approach != null)
			_approach.draw(kernel, t, dt);
		
		if (isVisible(t))
		{
			float alpha = 1.f;
			
			if (t >= _tbeg && t <= _tbeg + FADE_IN_TIME)
				alpha = (t - _tbeg) / FADE_IN_TIME;
			else if (t <= _tend && t >= _tend - FADE_OUT_TIME)
				alpha = (_tend - t) / FADE_OUT_TIME;
			
			TexturedQuad s = _pressed ? _down : _up;
			s.getTranslation().x = _x;
			s.getTranslation().y = _y;
			s.setAlpha(alpha);
			s.draw(kernel);
		}
	}
}
