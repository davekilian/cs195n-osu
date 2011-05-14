package osu.beatmap;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import osu.controls.Button;
import osu.controls.ButtonCallback;
import osu.controls.Control;
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
	public void add(Control c)
	{	
		if (c.getClass() == Button.class)
		{
			((Button)c).register(this);
			// TODO: miss icon
		}
		else if (c.getClass() == Slider.class)
		{
			((Slider)c).register(this);
			// TODO: miss icon
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
		// Remove invisible on-deck controls
		for (int i = 0; i < _onDeck.size(); ++i)
		{
			Control c = _onDeck.get(i);
			if (c.getEndTime() < t)
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
			Rect r = _onDeck.get(_onDeck.size() - 1).getHitbox();
			if (x >= r.left && x <= r.right && y >= r.top && y <= r.bottom)
				_onDeck.get(_onDeck.size() - 1).interact(x, y, t);
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
	}

	@Override
	public void buttonEvent(Button sender, HOButton event) 
	{
	}
}
