package osu.beatmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

import osu.controls.Button;
import osu.controls.ButtonCallback;
import osu.controls.Control;
import osu.controls.Miss;
import osu.controls.Slider;
import osu.controls.SliderCallback;
import osu.controls.Spinner;
import osu.controls.SpinnerCallback;
import osu.game.BreakTiming;
import osu.game.HOButton;
import osu.game.HOSlider;
import osu.game.HOSpinner;
import osu.screen.ScoreScreen;

import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.PrerenderCache;
import dkilian.andy.PrerenderContext;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

/**
 * Encapsulates the logic of playing a beatmap, e.g. playing the audio, showing
 * timed interactions, and responding accordingly.
 * 
 * @author dkilian
 */
public class BeatmapPlayer implements ButtonCallback, SliderCallback, SpinnerCallback
{
	/** The 'grace period' before/after a control's timings for which input is accepted, in partial seconds */
	public static final float GRACE_PERIOD = .25f;
	/** The amount of HP gained when a hit object is hit */
	public static final float HP_HIT = .2f;
	/** The amount of HP lost when a hit object is missed */
	public static final float HP_MISS = .1f;
	/** The amount of HP lost per second */
	public static final float HP_DRAIN = .0125f;
	/** The score awarded for a button press */
	public static final int BUTTON_SCORE = 100;
	/** The score awarded for a slider per second */
	public static final int SLIDER_SCORE = 100;
	/** The score awarded for a spinner per second */
	public static final int SPINNER_SCORE = 100;
	
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
	/** The player that plays this beatmap's audio */
	private MediaPlayer _player;
	/** The health graphic */
	private TexturedQuad _healthFill;
	/** The health bar graphic */
	private TexturedQuad _healthBar;
	/** The low-health graphic */
	private TexturedQuad _healthDanger;
	/** The amount of health the player has */
	private float _health;
	/** All items that have been correctly interacted with, for health management */
	private HashSet<Control> _notMissed;
	/** The time at which the first control appears. Causes health to be built up at the beginning of the beatmap */
	private float _firstControlTime;
	/** Whether or not health is currently draining. Based on whether a spinner or slider is in play */
	private boolean _healthDrainEnabled;
	/** The player's current score */
	private int _score;
	/** The sprite containing the player's score */
	private TexturedQuad _scoreSprite;
	/** Whether or not the score needs to be re-rendered */
	private boolean _scoreDirty;
	/** The score rendering context */
	private PrerenderContext _scoreContext;
	/** The last dt recorded in an update() call */
	private float _dt;
	/** The numbers of hit objects that have been interacted with, for scoring */
	private int _numHit;
	/** The total number of hit objects, for scoring */
	private int _totalObjects;
	/** Mike used a linked list. Linked lists make garbage. Ergo this. */
	private ArrayList<BreakTiming> _breaks;
	
	/** Determines whether or not the game is in the middle of a break period */
	private boolean isBreak(float time)
	{
		if (_breaks == null)
		{
			_breaks = new ArrayList<BreakTiming>();
			for (BreakTiming bt : _beatmap.getBreakTimings())
				_breaks.add(bt);
		}
		
		time *= 1000.f;
		
		for (int i = 0; i < _breaks.size(); ++i)
		{
			BreakTiming bt = _breaks.get(i);
			if (time >= bt.getStartTime() && time <= bt.getEndTime())
				return true;
		}
		
		return false;
	}
	
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
		_player = new MediaPlayer();
		_health = 1.f;
		_notMissed = new HashSet<Control>();
		_firstControlTime = Float.MAX_VALUE;
		_healthDrainEnabled = true;
		_score = 0;
		_scoreDirty = false;
		_dt = 0.f;
		_numHit = 0;
		_totalObjects = 0;
		
		Paint p = new Paint();
		_textCache = new PrerenderCache(p);
		p.setColor(Color.WHITE);
		p.setTextSize(60.f);
		p.setAntiAlias(true);
		
		p = new Paint();
		p.setColor(Color.WHITE);
		p.setTextSize(40.f);
		p.setAntiAlias(true);
		_scoreContext = Prerender.contextForString("0000000", p);
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
	
	/** Gets the health graphic */
	public TexturedQuad getHealthFill()
	{
		return _healthFill;
	}
	
	/** Sets the health graphic */
	public void setHealthFill(TexturedQuad health)
	{
		_healthFill = health;
	}
	
	/** Gets the health bar graphic */
	public TexturedQuad getHealthBar()
	{
		return _healthBar;
	}
	
	/** Sets the health bar graphic */
	public void setHealthBar(TexturedQuad health)
	{
		_healthBar = health;
	}
	
	/** Gets the low health graphic */
	public TexturedQuad getLowHealth()
	{
		return _healthDanger;
	}
	
	/** Sets the low-health graphic */
	public void setLowHealth(TexturedQuad low)
	{
		_healthDanger = low;
	}
	
	/** Gets the media player that play's this beatmap's audio */
	public MediaPlayer getMediaPlayer()
	{
		return _player;
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
		if (c.getClass() == Button.class)
		{
			((Button)c).register(this);
			((Button)c).getEvent().setGracePeriod(GRACE_PERIOD);
			
			Miss m = new Miss(((Button)c).getEvent(), _missIcon);
			m.setStartTime(((Button)c).getEndTime() + GRACE_PERIOD);
			m.setEndTime(m.getStartTime() + Miss.ANIMATION_TIME);
			
			_controls.add(m);
			_misses.put(c, m);
		}
		else if (c.getClass() == Slider.class)
		{
			Slider s = (Slider)c;
			s.register(this);
			s.getEvent().setGracePeriod(GRACE_PERIOD);
			
			HOSlider event = s.getEvent();
			
			Miss m = new Miss(event, _missIcon);
			m.setStartTime(s.getEndTime() + GRACE_PERIOD);
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
		
		if (_firstControlTime > c.getStartTime())
			_firstControlTime = c.getStartTime();

		_controls.add(c);
		++_totalObjects;
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
	public void begin()
	{
		Collections.sort(_controls, new Comparator<Control>() 
		{
			@Override
			public int compare(Control object1, Control object2) 
			{
				return (int)((object1.getStartTime() - object2.getStartTime()) * 1000.f);
			}
		});
		
		_player.start();
	}
	
	/** Ends playback prematurely (e.g. due to a game over/loss condition) */
	public void end() {}
	
	/** Does per-frame updating needed by this player */
	public void update(Kernel kernel, float t, float dt)
	{
		t = _player.getCurrentPosition() / 1000.f - GRACE_PERIOD;
		_dt = dt;
		
		// Manage health
		if (t < _firstControlTime)
		{
			_health = t / _firstControlTime;
		}
		else
		{
			_healthDrainEnabled = !isBreak(t);
			
			if (_healthDrainEnabled)
			{
				// Disable the drain if a slider or spinner is in effect
				for (int i = 0; i < _onDeck.size(); ++i)
				{
					Control c = _onDeck.get(i);
					if (c instanceof Slider)
					{
						Slider s = (Slider)c;
						if (t >= s.getStartTime() + Slider.FADE_IN_TIME + Slider.WAIT_TIME &&
						    t <= s.getEndTime() - Slider.FADE_OUT_TIME)
						{
							_healthDrainEnabled = false;
							break;
						}
					}
					else if (c instanceof Spinner)
					{
						Spinner s = (Spinner)c;
						if (t >= s.getStartTime() + Spinner.FADE_IN_TIME &&
							t <= s.getEndTime() - Spinner.FADE_OUT_TIME)
						{
							_healthDrainEnabled = false;
							break;
						}
					}
				}
				
				if (_healthDrainEnabled)
				{
					_health -= HP_DRAIN * dt;
					if (_health < 0.f) _health = 0.f;
				}
			}
			
			if (_health <= 0.f)	// game over, man! game over!
			{
				_player.stop();
				kernel.swapScreen(new ScoreScreen(0, 0, true));	// encapsulation? what's that?
				return;
			}
		}
		
		// Remove invisible on-deck controls
		for (int i = 0; i < _onDeck.size(); ++i)
		{
			Control c = _onDeck.get(i);
			if (c.getEndTime() + (c.getEvent() == null ? 0.f : c.getEvent().getGracePeriod()) < t)
			{
				_onDeck.remove(i);
				--i;
				boolean miss = !_notMissed.contains(c) && (c instanceof Slider || c instanceof Button || c instanceof Spinner);
				
				if (_healthDrainEnabled && miss)
				{
					_health -= HP_MISS;
					if (_health < 0.f) _health = 0.f;
				}
			}
		}
		
		// Put visible non-on-deck controls on-deck
		while (_nextControl < _controls.size() && _controls.get(_nextControl).getStartTime() < t)
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
		
		// Done?
		if (!_player.isPlaying() || (_onDeck.isEmpty() && _nextControl == _controls.size()))
		{
			_player.stop();
			kernel.swapScreen(new ScoreScreen(_numHit, _totalObjects, false));
			return;
		}
	}
	
	/** Renders the beatmap */
	public void draw(Kernel kernel, float t, float dt)
	{		
		t = _player.getCurrentPosition() / 1000.f - GRACE_PERIOD;
		
		// Background
		if (_background != null)
		{
			float w = kernel.getVirtualScreen().getWidth();
			float h = kernel.getVirtualScreen().getHeight();
			
			float scalex = w / _background.getWidth(), scaley = h / _background.getHeight(), scale = scalex < scaley ? scalex : scaley;
			_background.getTranslation().x = .5f * w;
			_background.getTranslation().y = .5f * h;
			_background.getScale().x = scale;
			_background.getScale().y = scale;
			_background.draw(kernel);
		}
		
		// Controls
		synchronized (_onDeck) 
		{
			for (int i = 0; i < _onDeck.size(); ++i)
				_onDeck.get(i).draw(kernel, t, dt);
		}
		
		// Health bar
		_healthBar.getTranslation().x = .5f * _healthBar.getWidth();
		_healthBar.getTranslation().y = .5f * _healthBar.getHeight();
		_healthBar.draw(kernel);
		
		_healthFill.getTranslation().x = .5f * _healthFill.getWidth();
		_healthFill.getTranslation().y = .5f * _healthFill.getHeight();
		agl.Clip(0, 0, (int)(_health * _healthFill.getWidth()), _healthFill.getHeight());
		_healthFill.draw(kernel);
		agl.Clip(0, 0, kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight());
		
		if (_health < .5f)
		{
			float alpha = 1.f - Math.max(0.f, (_health - .25f) / (.5f - .25f));

			_healthDanger.getTranslation().x = .5f * _healthBar.getWidth();
			_healthDanger.getTranslation().y = .5f * _healthBar.getHeight();
			_healthDanger.setAlpha(alpha);
			_healthDanger.draw(kernel);
		}
		
		// Score
		if (_scoreSprite == null)
		{
			int tex = agl.CreateEmptyTexture();
			_scoreSprite = new TexturedQuad(tex, _scoreContext.getBitmap().getWidth(), _scoreContext.getBitmap().getHeight());
			_scoreDirty = true;
		}
		if (_scoreDirty)
		{
			String score = String.format("%07d", _score);
			Prerender.string(score, _scoreContext, _scoreSprite);
			_scoreDirty = false;
		}
		_scoreSprite.getTranslation().x = kernel.getVirtualScreen().getWidth() - .5f * _scoreSprite.getWidth() - 5.f;
		_scoreSprite.getTranslation().y = kernel.getVirtualScreen().getHeight() - .5f * _scoreSprite.getHeight() - 5.f;
		_scoreSprite.draw(kernel);
	}

	@Override
	public void spinnerEvent(Spinner sender, HOSpinner event) 
	{
		if (!_notMissed.contains(sender))
		{
			_notMissed.add(sender);
			++_numHit;
			_health += HP_HIT;
			if (_health > 1.f) _health = 1.f;
		}
		
		_score += (int)(SPINNER_SCORE * _dt);
		_scoreDirty = true;
	}

	@Override
	public void sliderEvent(Slider sender, HOSlider event) 
	{
		float t = _player.getCurrentPosition() / 1000.f - GRACE_PERIOD;
		
		if (t >= sender.getStartTime() + (Slider.FADE_IN_TIME + Slider.WAIT_TIME) - GRACE_PERIOD &&
			t <= sender.getEndTime() - Slider.FADE_OUT_TIME + GRACE_PERIOD)
		{
			if (!_notMissed.contains(sender))
			{
				_notMissed.add(sender);
				_misses.get(sender).cancel();
				++_numHit;
				_health += HP_HIT;
				if (_health > 1.f) _health = 1.f;
			}
			
			_score += (int)(SLIDER_SCORE * _dt);
			_scoreDirty = true;
		}
	}

	@Override
	public void buttonEvent(Button sender, HOButton event) 
	{		
		float t = _player.getCurrentPosition() / 1000.f - GRACE_PERIOD;
		
		if (Math.abs(t - event.getTiming() / 1000.f) < event.getGracePeriod() && !_notMissed.contains(sender))
		{
			_notMissed.add(sender);
			_misses.get(sender).cancel();
			_health += HP_HIT;
			if (_health > 1.f) _health = 1.f;
			_score += BUTTON_SCORE;
			_scoreDirty = true;
			++_numHit;
		}
	}
}
