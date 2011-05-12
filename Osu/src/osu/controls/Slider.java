package osu.controls;

import java.util.ArrayList;

import osu.game.HOSlider;
import android.graphics.Point;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

/**
 * Handles interaction with a slider, using a slider hit-object
 * 
 * @author dkilian
 */
public class Slider implements Control
{
	/** The number of instances of the fill graphic drawn for a slider divided by the number of control points in the curve */
	public static final int STEPS_PER_CONTROL_POINT = 6;
	/** The amount of time it takes for a button to fade in, in partial seconds */
	public static final float FADE_IN_TIME  = .3f;
	/** The amount of time it takes for a button to fade out, in partial seconds */
	public static final float FADE_OUT_TIME = .1f;
	/** The amount of time, after fading in, the button appears before its event timing, in partial seconds */
	public static final float WAIT_TIME = 1.f;
	/** The amount of time, in partial seconds, the button fades to white after being interacted with */
	public static final float INTERACTED_FADE_OUT_TIME = .5f;
	
	/** The X coordinate of the initial cap in virtual space */
	private float _x;
	/** The Y coordinate of the initial cap in virtual space */
	private float _y;
	/** The time this entire slider begins to fade in */
	private float _tbeg;
	/** The time this entire slider finishes fading out */
	private float _tend;
	/** The bounds of this slider's nub */
	private Rect _bounds;
	/** The images the slider itself consists of */
	private TexturedQuad _cap, _fill, _nubUp, _nubDown;
	/** The event this slider corresponds to */
	private HOSlider _event;
	/** A list of objects this slider sends notifications to when its nub is interacted with */
	private ArrayList<SliderCallback> _callbacks;
	/** The number of times the nub has traveled across this slider */
	private int _repeatIteration;
	/** The bezier points describing this slider, in the format [x1 y1 x2 y2 ... xn yn] */
	private float[] _bezier;
	/** The movement speed in Bezier t values per second (i.e. 1.f = the entire path in one second) */
	private float _velocity;
	/** The current position along this slider, between 0 and 1 */
	private float _t;
	
	/**
	 * Creates a new slider with no graphics
	 * @param event The event this slider corresponds to
	 */
	public Slider(HOSlider event)
	{
		_event = event;
		_velocity = 1.f; // TODO: figure out what in god's name the beatmaps are doing
		_x = event.getPathPoints().getFirst().x;
		_y = event.getPathPoints().getFirst().y;
		_tbeg = _event.getTiming() / 1000.f - (FADE_IN_TIME + WAIT_TIME);
		_tend = _event.getTiming() / 1000.f + _event.getRepeats() * _velocity + FADE_OUT_TIME;
		_bounds = new Rect();
		_callbacks = new ArrayList<SliderCallback>();
		_repeatIteration = 0;
		_t = 0;
		
		_bezier = new float[_event.getPathPoints().size() * 2];
		int i = 0;
		for (Point p : _event.getPathPoints())
		{
			_bezier[i++] = p.x;
			_bezier[i++] = p.y;
		}
	}
	
	/**
	 * Creates a new slider 
	 * @param event The event this slider corresponds to
	 * @param cap The graphic drawn at each end of the slider
	 * @param fill The graphic drawn across the entire slider
	 * @param nubUp The graphic drawn when the nub is not being pressed
	 * @param nubDown The graphic drawn when the nub is being pressed
	 */
	public Slider(HOSlider event, TexturedQuad cap, TexturedQuad fill, TexturedQuad nubUp, TexturedQuad nubDown)
	{
		_event = event;
		_velocity = 1.f; // TODO: figure out what in god's name the beatmaps are doing
		_x = event.getPathPoints().getFirst().x;
		_y = event.getPathPoints().getFirst().y;
		_tbeg = _event.getTiming() / 1000.f - (FADE_IN_TIME + WAIT_TIME);
		_tend = _event.getTiming() / 1000.f + _event.getRepeats() * _velocity + FADE_OUT_TIME;
		_bounds = new Rect();
		_callbacks = new ArrayList<SliderCallback>();
		_cap = cap;
		_fill = fill;
		_nubUp = nubUp;
		_nubDown = nubDown;
		_repeatIteration = 0;
		_t = 0;
		
		_bezier = new float[_event.getPathPoints().size() * 2];
		int i = 0;
		for (Point p : _event.getPathPoints())
		{
			_bezier[i++] = p.x;
			_bezier[i++] = p.y;
		}
	}
	
	/** Gets the position of the nub along this slider, in the range [0,1] */
	public float getNubPosition()
	{
		return _t;
	}

	/** Sets the position of the nub along this slider, in the range [0,1] */
	public void setNubPosition(float t)
	{
		_t = t;
	}
	
	/**
	 * Gets the number of times the nub has traveled across the slider, in either direction
	 * (i.e. so the nub traveling from one end of the slider to another and back counts as
	 * two repeats). A slider dies when the repeat iteration = the number of repeats the
	 * slider uses.
	 */
	public int getRepeatIteration()
	{
		return _repeatIteration;
	}
	
	/** Gets the event this slider corresponds to */
	public HOSlider getEvent()
	{
		return _event;
	}
	
	/** Sets the event this slider corresponds to */
	public void setEvent(HOSlider event)
	{
		_event = event;
	}
	
	/** Gets the graphic drawn at each cap of this slider */
	public TexturedQuad getCap()
	{
		return _cap;
	}
	
	/** Sets the graphic drawn at each cap of this slider */
	public void setCap(TexturedQuad cap)
	{
		_cap = cap;
	}
	
	/** Gets the graphic drawn along this slider's path */
	public TexturedQuad getFill()
	{
		return _fill;
	}
	
	/** Sets the grphic drawn along this slider's path */
	public void setFill(TexturedQuad fill)
	{
		_fill = fill;
	}
	
	/** Gets the graphic drawn when the slider nub is not being pressed */
	public TexturedQuad getNubUp()
	{
		return _nubUp;
	}

	/** Sets the graphic drawn when the slider nub is not being pressed */
	public void setNubUp(TexturedQuad up)
	{
		_nubUp = up;
	}

	/** Gets the graphic drawn when the slider nub is being pressed */
	public TexturedQuad getNubDown()
	{
		return _nubDown;
	}

	/** Sets the graphic drawn when the slider nub is being pressed */
	public void setNubDown(TexturedQuad nubDown)
	{
		_nubDown = nubDown;
	}
	
	/** Registers a callback to receive notifications of user interaction with this slider's nub */
	public void register(SliderCallback callback)
	{
		_callbacks.add(callback);
	}
	
	/** Unregisters a callback from receiving notifications of user interaction with this slider's nub */
	public void unregister(SliderCallback callback)
	{
		_callbacks.remove(callback);
	}

	/** Gets the X coordinate of this slider's initial cap */
	@Override
	public float getX() 
	{
		return _x;
	}

	/** Has no effect */
	@Override
	public void setX(float x) 
	{		
		_x = x;
	}

	/** Gets the Y coordinate of this slider's initial cap */
	@Override
	public float getY() 
	{
		return _y;
	}

	/** Has no effect */
	@Override
	public void setY(float y) 
	{
		_y = y;
	}

	/** Gets the time this slider begins fading in */
	@Override
	public float getStartTime() 
	{
		return _tbeg;
	}

	/** Sets the time this slider begins fading in */
	@Override
	public void setStartTime(float t) 
	{
		_tbeg = t;
	}

	/** Gets the time this slider finishes fading out */
	@Override
	public float getEndTime() 
	{
		return _tend;
	}

	/** Sets the time this slider finished fading out */
	@Override
	public void setEndTime(float t) 
	{
		_tend = t;
	}

	/** Gets a value indicating whether this slider is visible */
	@Override
	public boolean isVisible(float t) 
	{
		return t >= _tbeg && t <= _tend;
	}

	/** Gets the hitbox describing this slider's nub */
	@Override
	public Rect getHitbox() 
	{
		return _bounds;
	}

	/** Registers a touch event with this slider */
	@Override
	public void interact(float x, float y, float t) 
	{
		// Nub/finger interaction logic
	}

	/** Does per-frame updating required by this slider */
	@Override
	public void update(Kernel kernel, float t, float dt) 
	{
		// Move bounds based on nub position
	}

	/** Renders this slider */
	@Override
	public void draw(Kernel kernel, float t, float dt) 
	{
		if (isVisible(t))
		{
			agl.InstanceBitmapBezier(_fill.getTexture(), _fill.getWidth(), _fill.getHeight(), _bezier, _bezier.length / 2, STEPS_PER_CONTROL_POINT * _bezier.length / 2, 0.f, 1.f, 1.f, 1.f);
			_cap.getTranslation().x = _bezier[0];
			_cap.getTranslation().y = _bezier[1];
			_cap.draw(kernel);
			_cap.getTranslation().x = _bezier[_bezier.length - 2];
			_cap.getTranslation().y = _bezier[_bezier.length - 1];
			_cap.draw(kernel);
			
			TexturedQuad nub = _nubUp;
			agl.DrawAlongBezierPath(nub.getTexture(), nub.getWidth(), nub.getHeight(), _bezier, _bezier.length / 2, _t, 0.f, 1.f, 1.f, 1.f);
		}
	}
}

