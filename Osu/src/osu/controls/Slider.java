package osu.controls;

import java.util.ArrayList;

import osu.game.HOSlider;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

/**
 * Handles interaction with a slider, using a slider hit-object
 * 
 * @author dkilian
 */
public class Slider implements Control
{
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
	private TexturedQuad _cap, _fill;
	/** The event this slider corresponds to */
	private HOSlider _event;
	/** A list of objects this slider sends notifications to when its nub is interacted with */
	private ArrayList<SliderCallback> _callbacks;
	/** The number of times the nub has traveled across this slider */
	private int _repeatIteration;
	
	/**
	 * Creates a new slider with no graphics
	 * @param event The event this slider corresponds to
	 */
	public Slider(HOSlider event)
	{
		_event = event;
		_x = event.getPathPoints().getFirst().x;
		_y = event.getPathPoints().getFirst().y;
		// What does event.timing mean in the case of sliders? I'm guessing ...
		_tbeg = 0.f; // slider.timing - (fade_in_time + wait_time)
		_tend = 0.f; // slider.timing + numrepeats * (length / nub_velocity) + fade_out_time
		_bounds = new Rect();
		_callbacks = new ArrayList<SliderCallback>();
		_repeatIteration = 0;
		// TODO: copy the point[] into a float[] for fast rendering
	}
	
	/**
	 * Creates a new slider 
	 * @param event The event this slider corresponds to
	 * @param cap The graphic drawn at each end of the slider
	 * @param fill The graphic drawn across the entire slider
	 */
	public Slider(HOSlider event, TexturedQuad cap, TexturedQuad fill)
	{
		_event = event;
		_x = event.getPathPoints().getFirst().x;
		_y = event.getPathPoints().getFirst().y;
		// What does event.timing mean in the case of sliders?
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
		_callbacks = new ArrayList<SliderCallback>();
		_cap = cap;
		_fill = fill;
		_repeatIteration = 0;
		// TODO: copy the point[] into a float[] for fast rendering
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
	
	public TexturedQuad getFill()
	{
		return _fill;
	}
	
	public void setFill(TexturedQuad fill)
	{
		_fill = fill;
	}
	
	public void register(SliderCallback callback)
	{
		_callbacks.add(callback);
	}
	
	public void unregister(SliderCallback callback)
	{
		_callbacks.remove(callback);
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
		// Move bounds based on nub position
	}

	@Override
	public void draw(Kernel kernel, float t, float dt) 
	{
		// Draw slider
		
		// Draw nub
	}
}

