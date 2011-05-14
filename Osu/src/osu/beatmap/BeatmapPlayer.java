package osu.beatmap;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import osu.controls.Button;
import osu.controls.ButtonCallback;
import osu.controls.Control;
import osu.controls.Miss;
import osu.controls.Slider;
import osu.controls.SliderCallback;
import osu.controls.Spinner;
import osu.controls.SpinnerCallback;
import osu.game.HOButton;
import osu.game.HOSlider;
import osu.game.HOSpinner;

import dkilian.andy.Kernel;
import dkilian.andy.PrerenderCache;
import dkilian.andy.TexturedQuad;

/**
 * Encapsulates the logic of playing a beatmap, e.g. playing the audio, showing
 * timed interactions, and responding accordingly.
 * 
 * @author dkilian
 */
public class BeatmapPlayer implements ButtonCallback, SliderCallback, SpinnerCallback
{
	/** The 'grace period' before/after a control's timings for which input is accepted, in beats. */
	public static final float GRACE_PERIOD = 1.f;
	
	/** The beatmap this player plays */
	private Beatmap _beatmap;
	/** The background shown behind the beatmap */
	private TexturedQuad _background;
	/** The interactable beatmap controls */
	private ArrayList<Control> _controls;
	/** The text pre-renderer */
	private PrerenderCache _textCache;
	/** The index into _controls of the next not-yet on-deck control */
	private int _nextControl;
	/** The list of controls that are currently visible */
	private ArrayList<Control> _onDeck;
	/** Maps each control to the 'missed' hit object that appears when the object is not interacted with at the correct time */
	private HashMap<Control, Miss> _misses;
	/** The icon shown when an item is missed */
	private TexturedQuad _missIcon;
	/** The current game time, in seconds */
	private float _time;
	
	/**
	 * Creates a new beatmap player
	 * @param beatmap The beatmap this player can play
	 */
	public BeatmapPlayer(Beatmap beatmap)
	{
		_beatmap = beatmap;
		_controls = new ArrayList<Control>();
		_onDeck = new ArrayList<Control>();
		_nextControl = 0;
		_misses = new HashMap<Control, Miss>();
		_time = 0.f;
		
		Paint p = new Paint();
		_textCache = new PrerenderCache(p);
		p.setColor(Color.WHITE);
		p.setTextSize(60.f);
		p.setAntiAlias(true);
	}
	
	/** Gets the beatmap this player plays */
	public Beatmap getBeatmap()
	{
		return _beatmap;
	}
	
	/** Sets the beatmap this player plays */
	public void setBeatmap(Beatmap bm)
	{
		_beatmap = bm;
	}
	
	/** Gets this beatmap's background image */
	public TexturedQuad getBackground()
	{
		return _background;
	}
	
	/** Sets this beatmap's background image */
	public void setBackground(TexturedQuad background)
	{
		_background = background;
	}
	
	/** Gets the icon shown when an interaction is missed */
	public TexturedQuad getMissIcon()
	{
		return _missIcon;
	}
	
	/** Sets the icon shown when an interaction is missed */
	public void setMissIcon(TexturedQuad miss)
	{
		_missIcon = miss;
	}
	
	/** Gets the text pre-renderer for all button/slider numbering */
	public PrerenderCache getTextCache()
	{
		return _textCache;
	}

	/** Sets the text pre-renderer for all button/slider numbering */
	public void setTextCache(PrerenderCache t)
	{
		_textCache = t;
	}
	
	/** Gets the controls in this beatmap */
	public ArrayList<Control> getControls()
	{
		return _controls;
	}
	
	/** Sets the controls in this beatmap */
	public void setControls(ArrayList<Control> c)
	{
		_controls = c;
	}
	
	/** Adds a control to this beatmap */
	public void add(Control c, float beatLength)
	{	
		beatLength *= .001;	// ms -> s
		
		if (c.getClass() == Button.class)
		{
			((Button)c).register(this);
			((Button)c).getEvent().setGracePeriod(beatLength * GRACE_PERIOD);
			
			Miss m = new Miss(((Button)c).getEvent(), _missIcon);
			m.setStartTime(((Button)c).getEndTime() + beatLength * GRACE_PERIOD);
			m.setEndTime(m.getStartTime() + Miss.ANIMATION_TIME);
			
			_controls.add(m);
			_misses.put(c, m);
		}
		else if (c.getClass() == Slider.class)
		{
			Slider s = (Slider)c;
			s.register(this);
			s.getEvent().setGracePeriod(beatLength * GRACE_PERIOD);
			
			HOSlider event = s.getEvent();
			
			Miss m = new Miss(event, _missIcon);
			m.setStartTime(s.getEndTime() + beatLength * GRACE_PERIOD);
			m.setEndTime(m.getStartTime() + Miss.ANIMATION_TIME);
			
			if ((event.getRepeats() & 1) != 0)
			{
				m.setX(event.getPathPoints().get(event.getPathPoints().size() - 1).x);
				m.setY(event.getPathPoints().get(event.getPathPoints().size() - 1).y);
			}
			
			_controls.add(m);
			_misses.put(c, m);
		}
		else if (c.getClass() == Spinner.class)
		{
			((Spinner)c).register(this);
		}

		_controls.add(c);
	}

	/** Removes a control from this beatmap */
	public void remove(Control c)
	{
		_controls.add(c);
		
		if (c.getClass() == Button.class)
			((Button)c).unregister(this);
		else if (c.getClass() == Slider.class)
			((Slider)c).unregister(this);
		else if (c.getClass() == Spinner.class)
			((Spinner)c).unregister(this);
	}
	
	/** Initializes playback */
	public void begin() {}
	
	/** Ends playback prematurely (e.g. due to a game over/loss condition) */
	public void end() {}
	
	/** Does per-frame updating needed by this player */
	public void update(Kernel kernel, float t, float dt)
	{
		_time = t;
		
		// Remove invisible on-deck controls
		for (int i = 0; i < _onDeck.size(); ++i)
		{
			Control c = _onDeck.get(i);
			if (c.getEndTime() + (c.getEvent() == null ? 0.f : c.getEvent().getGracePeriod()) < t)
			{
				_onDeck.remove(i);
				--i;
			}
		}
		
		// Put visible non-on-deck controls on-deck
		while (_nextControl < _controls.size() && _controls.get(_nextControl).getStartTime() > t)
		{
			synchronized (_onDeck)
			{
				_onDeck.add(0, _controls.get(_nextControl));
			}
			++_nextControl;
		}
		
		// Update visible controls
		synchronized (_onDeck) 
		{
			for (int i = 0; i < _onDeck.size(); ++i)
				_onDeck.get(i).update(kernel, t, dt);
		}
		
		// Interactions with the current on-deck control
		if (!_onDeck.isEmpty() && kernel.getTouch().isDown())
		{
			float x = kernel.getTouch().getX();
			float y = kernel.getTouch().getY();
			for (int i = 0; i < _onDeck.size(); ++i)
			{
				Rect r = _onDeck.get(i).getHitbox();
				if (x >= r.left && x <= r.right && y >= r.top && y <= r.bottom)
					_onDeck.get(i).interact(x, y, t);
			}
		}
	}
	
	/** Renders the beatmap */
	public void draw(Kernel kernel, float t, float dt)
	{		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();

		float scalex = w / _background.getWidth(), scaley = h / _background.getHeight(), scale = scalex < scaley ? scalex : scaley;
		_background.getTranslation().x = .5f * w;
		_background.getTranslation().y = .5f * h;
		_background.getScale().x = scale;
		_background.getScale().y = scale;
		_background.draw(kernel);
		
		synchronized (_onDeck) 
		{
			for (int i = 0; i < _onDeck.size(); ++i)
				_onDeck.get(i).draw(kernel, t, dt);
		}
	}

	@Override
	public void spinnerEvent(Spinner spinner, HOSpinner event) 
	{
	}

	@Override
	public void sliderEvent(Slider sender, HOSlider event) 
	{
		if (Math.abs(_time - event.getTiming() / 1000.f) < event.getGracePeriod())
			_misses.get(sender).cancel();
	}

	@Override
	public void buttonEvent(Button sender, HOButton event) 
	{		
		if (Math.abs(_time - event.getTiming() / 1000.f) < event.getGracePeriod())
			_misses.get(sender).cancel();
	}
}
