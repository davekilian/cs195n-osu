package osu.beatmap;

import java.io.File;
import java.util.HashMap;

import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import osu.controls.Button;
import osu.controls.Ring;
import osu.controls.Slider;
import osu.controls.Spinner;
import osu.game.ComboColor;
import osu.game.HOButton;
import osu.game.HOSlider;
import osu.game.HOSpinner;
import osu.game.HitObject;
import osu.graphics.BitmapTint;
import osu.main.R;
import osu.parser.Parser;
import osu.parser.ParserContainer;
import osu.parser.ParserUtil;

/**
 * Utility for loading and parsing a beatmap and its assets
 * on a separate thread so a loading screen can be shown on
 * the main thread.
 * 
 * @author dkilian
 */
public class BeatmapLoader 
{
	/** Allows the loading thread to ask the main thread to load a bitmap into an opengl texture in a draw event */
	private class CrossThreadLoad
	{
		/** The bitmap containing the data to load */
		public Bitmap bitmap;
		/** Receives the loaded data */
		public TexturedQuad quad;
	}
	
	private CrossThreadLoad _load = new CrossThreadLoad();
	
	/** Loads data on a separate thread, allowing a load screen to be shown on the main thread without blocking */
	private class LoadThread implements Runnable
	{
		/** The path to the beatmap to load */
		public String path;
		/** The beatmap loaded. Will be allocated by this thread */
		public Beatmap beatmap;
		/** The beatmap player that plays the beatmap loaded. Will be allocated by this thread. */
		public BeatmapPlayer player;
		/** The number of game assets that have been loaded by this thread so far */
		public int itemsLoaded;
		/** The number of game assets that this thread needs to load. Will be zero if the number of assets required is not yet known */
		public int itemsToLoad;
		/** The current progress string to display in a loading screen, if desired */
		public String progress;
		/** Whether or not this thread is running */
		public boolean running;
		/** Set to true to prematurely end loading when it is convenient for this thread to do so */
		public boolean cancelled;
		/** The kernel containing the resource cache to load assets from */
		public Kernel kernel;
		/** The highest number ever reached in a single combo. Used to pre-render text */
		public int highestCombo;
		
		/** Performs a cross-thread GL quad loading operation */
		private TexturedQuad crossload(Bitmap b)
		{
			synchronized (_load) 
			{
				_load.bitmap = b;
				_load.quad = null;
			}
			
			TexturedQuad quad = null;
			while (quad == null)
			{
				synchronized (_load)
				{
					quad = _load.quad;
				}
			}
			
			synchronized (_load)
			{
				_load.bitmap = null;
				_load.quad = null;
			}
			
			return quad;
		}
		
		/** Creates a new loading thread */
		public LoadThread()
		{
			path = "";
			running = false;
			init();
		}
		
		/** Prepares this thread for a new loading operation */
		public void init()
		{
			beatmap = null;
			player = null;
			itemsLoaded = 0;
			itemsToLoad = 0;
			progress = "Initializing osu! ...";
			cancelled = false;
			highestCombo = 0;
		}

		/** Entry point */
		@Override
		public void run() 
		{
			running = true;
			init();
			load();
			running = false;
		}
		
		/** Loads the beatmap */
		private void load()
		{
			// Parse the beatmap
			ParserContainer pc = new ParserContainer();
			Parser parser = new Parser();
			try
			{
				progress = "Parsing .osu ...";
				parser.parseResource(path, pc);
				beatmap = ParserUtil.parserContainerToBeatmap(pc);
				progress += "done";
			}
			catch (Exception ex)
			{
				progress = "Can't load beatmap audio: " + ex.toString();
				Log.e("BeatmapLoader", progress, ex);
				return;
			}
			
			// SHIIIIIIT
			if (pc.combo_colors.size() == 0)
			{
				pc.combo_colors.add(new ComboColor(255,   0, 0));
				pc.combo_colors.add(new ComboColor(255, 128, 0));
			}
			
			// Count the assets to load
			int numCombos = beatmap.getComboColors().size();
			itemsToLoad += 4;            	// button up, down, chrome, shadow
			itemsToLoad += 2 * numCombos;	// button up/down recoloring/rendering
			itemsToLoad += 1;            	// slider return arrow
			itemsToLoad += 4 * numCombos;	// slider cap, fill, nub-up, nub-down recoloring/rendering
			itemsToLoad += 5;				// spinner mask, fill, no-fill, spiral, text
			itemsToLoad += 2;				// approach ring shadow and fill
			itemsToLoad += 2 * numCombos;	// approach ring recoloring/rendering 	
			itemsToLoad += 1;				// 'miss' icon
			itemsToLoad += 2;				// health fill and bar
			itemsToLoad += 1;				// Create all controls
			itemsToLoad += 3;            	// beatmap background, audio file	
			
			// Load control assets
			HashMap<ComboColor, TexturedQuad> buttonUps = new HashMap<ComboColor, TexturedQuad>();
			HashMap<ComboColor, TexturedQuad> buttonDowns = new HashMap<ComboColor, TexturedQuad>();
			HashMap<ComboColor, TexturedQuad> sliderCaps = new HashMap<ComboColor, TexturedQuad>();
			HashMap<ComboColor, TexturedQuad> sliderFills = new HashMap<ComboColor, TexturedQuad>();
			HashMap<ComboColor, TexturedQuad> sliderNubUps = new HashMap<ComboColor, TexturedQuad>();
			HashMap<ComboColor, TexturedQuad> sliderNubDowns = new HashMap<ComboColor, TexturedQuad>();
			HashMap<ComboColor, TexturedQuad> rings = new HashMap<ComboColor, TexturedQuad>();
			
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			progress = ":/drawable/button_up";
			Bitmap buttonUp = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/button_down";
			Bitmap buttonDown = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_down, opt);
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/button_shadow";
			Bitmap buttonShadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/button_chrome";
			Bitmap buttonChrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			++itemsLoaded; if (cancelled) return;
			
			for (int i = 0; i < beatmap.getComboColors().size(); ++i)
			{
				ComboColor c = beatmap.getComboColors().get(i);
				progress = String.format("Coloring button (%d, %d, %d)", c.getR(), c.getG(), c.getB());
				
				buttonUps.put(c, crossload(Button.render(buttonUp, buttonShadow, buttonChrome, c)));
				++itemsLoaded; if (cancelled) return;
				buttonDowns.put(c, crossload(Button.render(buttonDown, buttonShadow, buttonChrome, c)));
				++itemsLoaded; if (cancelled) return;
			}

			progress = ":/drawable/slider_return";
			TexturedQuad sliderReturn = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.slider_return, opt));
			++itemsLoaded; if (cancelled) return;
			
			for (int i = 0; i < beatmap.getComboColors().size(); ++i)
			{
				ComboColor c = beatmap.getComboColors().get(i);
				progress = String.format("Coloring slider (%d, %d, %d)" ,c.getR(), c.getG(), c.getB());
				
				Bitmap sliderUp = BitmapTint.apply(buttonUp, c);
				Bitmap sliderDown = BitmapTint.apply(buttonDown, c);
				Bitmap sliderChrome = buttonChrome;
				Bitmap sliderShadow = buttonShadow;
				
				sliderCaps.put(c, crossload(Button.render(sliderUp, sliderShadow, sliderChrome)));
				++itemsLoaded; if (cancelled) return;
				sliderFills.put(c, crossload(Button.render(sliderUp, sliderShadow, sliderUp)));
				++itemsLoaded; if (cancelled) return;
				sliderNubUps.put(c, crossload(Button.render(sliderUp, sliderShadow, sliderChrome)));
				++itemsLoaded; if (cancelled) return;
				sliderNubDowns.put(c, crossload(Button.render(sliderDown, sliderShadow, sliderChrome)));
				++itemsLoaded; if (cancelled) return;
			}

			progress = ":/drawable/spinner_spiral";
			TexturedQuad spinnerSpiral = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_spiral, opt));
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/spinner_fill";
			TexturedQuad spinnerFill = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_fill, opt));
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/spinner_nofill";
			TexturedQuad spinnerNoFill = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_nofill, opt));
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/spinner_mask";
			TexturedQuad spinnerMask = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_mask, opt));
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/spinner_text";
			TexturedQuad spinnerText = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_text, opt));
			++itemsLoaded; if (cancelled) return;

			progress = ":/drawable/ring";
			Bitmap ring = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring, opt);
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/ring_shadow";
			Bitmap ringShadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring_shadow, opt);
			++itemsLoaded; if (cancelled) return;
			
			for (int i = 0; i < beatmap.getComboColors().size(); ++i)
			{
				ComboColor c = beatmap.getComboColors().get(i);
				progress = String.format("Coloring ring (%d, %d, %d)", c.getR(), c.getG(), c.getB());
				
				rings.put(c, crossload(Ring.render(ring, ringShadow, c)));
				++itemsLoaded; if (cancelled) return;
			}
			
			progress = ":/drawable/no";
			TexturedQuad missIcon = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.no, opt));
			++itemsLoaded; if (cancelled) return;

			progress = ":/drawable/health";
			TexturedQuad healthFill = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.health, opt));
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/health_bar";
			TexturedQuad healthBar = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.health_bar, opt));
			++itemsLoaded; if (cancelled) return;
			progress = ":/drawable/health_danger";
			TexturedQuad healthDanger = crossload(BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.health_danger, opt));
			++itemsLoaded; if (cancelled) return;

			// Create controls
			progress = "Initializing UI ...";
			player = new BeatmapPlayer(beatmap);
			player.setMissIcon(missIcon);
			player.setHealthBar(healthBar);
			player.setHealthFill(healthFill);
			player.setLowHealth(healthDanger);
			
			int combo = 0;
			int comboNumber = 1;
			int timingPoint = 0;
			float lastbpm = (float)beatmap.getTimingPoint().get(0).getBPM();
			
			for (int i = 0; i < pc.hit_objects.size(); ++i)
			{
				if (highestCombo < comboNumber)
					highestCombo = comboNumber;
				
				HitObject ho = pc.hit_objects.get(i);
				if (ho.getNewCombo())
				{
					combo = (combo + 1) % beatmap.getComboColors().size();
					comboNumber = 1;
				}
				
				ComboColor color = beatmap.getComboColors().get(combo);
				
				while (timingPoint+1 < beatmap.getTimingPoint().size() && beatmap.getTimingPoint().get(timingPoint+1).getOffset() < ho.getTiming())
				{
					++timingPoint;
					float bpm = (float)beatmap.getTimingPoint().get(timingPoint).getBPM();
					if (bpm < 0)
					{
//						lastbpm *= bpm / -100.f;
						lastbpm *= -100.f / bpm; // derp
						Log.v("BeatmapLoader", "Now at timing point " + timingPoint + ", with relative beat length " + (bpm / -100.f));
						Log.v("BeatmapLoader", "Beatlength is now " + lastbpm);
					}
					else
					{
						lastbpm = bpm;
						Log.v("BeatmapLoader", "Now at timing point " + timingPoint + ", with absolute beat length " + bpm);
					}
				}
				
				// What happens when an object's start time happens to be negative?
				if (ho.getClass() == HOButton.class)
				{
					HOButton event = (HOButton)ho;
					Button b = new Button(event, buttonUps.get(color), buttonDowns.get(color), null, player.getTextCache(), Integer.toString(comboNumber++));
					b.setApproachRing(new Ring(rings.get(color)));
					player.add(b, lastbpm);
				}
				else if (ho.getClass() == HOSlider.class)
				{
					HOSlider event = (HOSlider)ho;
					Slider s = new Slider(event, lastbpm, beatmap.getSliderMultiplier(), event.getPathLength(), sliderCaps.get(color),
							              sliderFills.get(color), sliderNubUps.get(color), sliderNubDowns.get(color), sliderReturn, null,
							              player.getTextCache(), Integer.toString(comboNumber++));
					s.setApproachRing(new Ring(rings.get(color)));
					player.add(s, lastbpm);
				}
				else if (ho.getClass() == HOSpinner.class)
				{
					HOSpinner event = (HOSpinner)ho;
					Spinner s = new Spinner(event, spinnerSpiral, spinnerNoFill, spinnerFill, spinnerMask, spinnerText, null);
					s.setApproachRing(new Ring(rings.get(color)));
					player.add(s, lastbpm);
				}
				else
				{
					Log.w("BeatmapLoader", "Unrecognized hitobject: " + ho.getClass().toString());
				}
			}
			++itemsLoaded; if (cancelled) return;
			
			// Load beatmap's assets
			progress = beatmap.getBackground().getImagePath();
			String folder = path.contains(File.separator) ? path.substring(0, path.lastIndexOf(File.separator)) : path;
			String bgpath = folder + File.separator + beatmap.getBackground().getImagePath();
			if (!new File(bgpath).exists())
			{
				progress = "Background image doesn't exist: " + bgpath;
				Log.e("BeatmapLoader", progress);
			}
			else
			{
				player.setBackground(crossload(BitmapFactory.decodeFile(bgpath).copy(Bitmap.Config.ARGB_8888, false)));
				++itemsLoaded; if (cancelled) return;
			}
			
			progress = beatmap.getAudioFilename();
			String audiopath = folder + File.separator + beatmap.getAudioFilename();
			try
			{
				player.getMediaPlayer().setDataSource(audiopath);
				player.getMediaPlayer().prepare();
			}
			catch (Exception ex)
			{
				progress = "Can't load beatmap audio: " + ex.toString();
				Log.e("BeatmapLoader", progress, ex);
				return;
			}
			++itemsLoaded; if (cancelled) return;
			
			progress = "done!";
		}
	}
	
	/** The thread that loads data for this loaded */
	private LoadThread _thread;
	
	/**
	 * Creates a new asynchronous beatmap laoded
	 * @param path The path to the .osu file containing the beatmap to load
	 */
	public BeatmapLoader(Kernel kernel, String path)
	{
		_thread = new LoadThread();
		_thread.kernel = kernel;
		_thread.path = path;
	}
	
	/** Begins asynchronously loading a beatmap */
	public void begin()
	{
		_thread.running = true;
		new Thread(_thread).start();
	}
	
	/** Gets a value indicating whether a beatmap is being loaded by this object */
	public boolean isLoading()
	{
		return _thread.running;
	}
	
	/** Gets the progress between 0 and 1, or -1 if the progress is not known */
	public float getProgress()
	{
		if (_thread.itemsToLoad == 0)
			return -1.f;
		return (float)(1 + _thread.itemsLoaded) / (float)_thread.itemsToLoad;
	}
	
	/** Gets the progress string a load screen may display to show the user what aspect of the load process is executing */
	public String getProgressString()
	{
		return _thread.progress;
	}
	
	/** Does tasks that can only be done on the main thread, during a draw() call */
	public void doGLTasks()
	{
		synchronized (_load) 
		{
			if (_load.bitmap != null && _load.quad == null)
				_load.quad = new TexturedQuad(_load.bitmap.copy(Bitmap.Config.ARGB_8888, false));
		}
	}
	
	/** Prematurely ends the loading process. The thread may not immediately end. */
	public void cancel()
	{
		_thread.cancelled = true;
	}
	
	/** Gets the beatmap loaded by this loader */
	public BeatmapPlayer getBeatmap()
	{
		return _thread.player;
	}
	
	/** Gets the highest combo number any control receives */
	public int getHighestCombo()
	{
		return _thread.highestCombo;
	}
}
