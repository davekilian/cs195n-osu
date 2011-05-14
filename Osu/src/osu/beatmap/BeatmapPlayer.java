package osu.beatmap;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

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
import dkilian.andy.jni.agl;

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
	
	private TexturedQuad _background;
	
	private ArrayList<Control> _controls;
	
	private PrerenderCache _textCache;
	
	/**
	 * Creates a new beatmap player
	 * @param beatmap The beatmap this player can play
	 */
	public BeatmapPlayer(Beatmap beatmap)
	{
		_beatmap = beatmap;
		_controls = new ArrayList<Control>();
		
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
	
	public TexturedQuad getBackground()
	{
		return _background;
	}
	
	public void setBackground(TexturedQuad background)
	{
		_background = background;
	}
	
	public PrerenderCache getTextCache()
	{
		return _textCache;
	}
	
	public void setTextCache(PrerenderCache t)
	{
		_textCache = t;
	}
	
	public ArrayList<Control> getControls()
	{
		return _controls;
	}
	
	public void setControls(ArrayList<Control> c)
	{
		_controls = c;
	}
	
	public void add(Control c)
	{	
		if (c.getClass() == Button.class)
			((Button)c).register(this);
		else if (c.getClass() == Slider.class)
			((Slider)c).register(this);
		else if (c.getClass() == Spinner.class)
			((Spinner)c).register(this);

		_controls.add(c);
	}
	
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
	}
	
	/** Ends playback prematurely (e.g. due to a game over/loss condition) */
	public void end()
	{
		
	}
	
	/** Does per-frame updating needed by this player */
	public void update(Kernel kernel, float t, float dt)
	{
		
	}
	
	/** Renders the beatmap */
	public void draw(Kernel kernel, float t, float dt)
	{		
		agl.ClearColor(100.f / 255.f, 149.f / 255.f, 237.f / 255.f);
		agl.BlendPremultiplied();
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();

		float scalex = w / _background.getWidth(), scaley = h / _background.getHeight(), scale = scalex < scaley ? scalex : scaley;
		_background.getTranslation().x = .5f * w;
		_background.getTranslation().y = .5f * h;
		_background.getScale().x = scale;
		_background.getScale().y = scale;
		_background.draw(kernel);

		// Well fuck. Do we need to run the loader synchronously in the draw thread? Or somehow delay bitmap generation until draw-time?
		// -> that way sounds good. It sucks, but basically we need to defer all quad generation until a draw call. For now, just create
		//    some LoadQuadRequest object and synchronize that on both threads, and call _loader.doGLTasks() in LoadScreen's draw()
		// -> Write a small helper utility in the thread's class that takes a bitmap, returns a thread, and does any required waiting.
		// Yay threads.
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
