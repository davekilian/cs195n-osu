package osu.screen;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;

import osu.main.R;
import osu.menu.BeatmapDescriptor;
import osu.menu.BeatmapDir;
import osu.menu.BeatmapLibraryLoader;
import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class SelectScreen implements Screen
{
	public static final float MARGIN = 20.f;             // pixels
	public static final float SNAP_SPEED = 50.f;         // pixels/sec
	public static final float BOTTOM_MARGIN = 40.f;      // pixels
	public static final float DRAG_TAP_THRESHOLD = 10.f; // pixels moved before a tap becomes a drag (for hitting the play button)
	
	private boolean _loaded = false;
	private TexturedQuad _background, _arrow;
	private ArrayList<String> _beatmapNames;
	private HashMap<String, BeatmapDescriptor> _beatmaps;
	private HashMap<String, TexturedQuad> _renderedBeatmapNames;
	private float _scroll = 0.f;
	private int _selectedIndex = -1;
	private float _time = 0.f;
	private TexturedQuad _up, _down;
	private boolean _dragging;
	private float _lastY;
	private boolean _queuePlay = false;
	private float _totalDrag = 0.f;
	
	@Override
	public boolean isLoaded() 
	{
		return _loaded;
	}

	@Override
	public void load(Kernel kernel) 
	{
		_beatmapNames = new ArrayList<String>();
		_beatmaps = new HashMap<String, BeatmapDescriptor>();
		
		ArrayList<BeatmapDir> dirs = null;
		try
		{
			dirs = BeatmapLibraryLoader.getBeatmapDirs(Environment.getExternalStorageDirectory().getAbsolutePath() + "/osu/beatmaps");
		}
		catch (Exception ex)
		{
			Log.e("Beatmap scanner", "", ex);
			System.exit(0);
		}
		
		for (int i = 0; i < dirs.size(); ++i)
		{
			BeatmapDir d = dirs.get(i);
			for (int j = 0; j < d.getBeatmapDescriptors().size(); ++j)
			{
				BeatmapDescriptor desc = d.getBeatmapDescriptors().get(j);
				String name = desc.getMetadata().title + " [" + desc.getMetadata().version + "]";
				if (!_beatmaps.containsKey(name))
				{
					_beatmapNames.add(name);
					_beatmaps.put(name, desc);
				}
			}
		}
		
		_loaded = true;
	}

	@Override
	public void unload(Kernel kernel) 
	{
		_loaded = false;
	}

	@Override
	public void update(Kernel kernel, float dt) 
	{
		_time += dt;
		
		// Scrolling
		if (kernel.getTouch().isDown())
		{
			if (_dragging)
			{
				float y = kernel.getTouch().getY();
				_scroll += y - _lastY;
				_totalDrag += Math.abs(y - _lastY);
				_lastY = y;
			}
			else
			{				
				_dragging = true;
				_lastY = kernel.getTouch().getY();
				_totalDrag = 0.f;
				_queuePlay = (kernel.getTouch().getX() > .5f * kernel.getVirtualScreen().getWidth()
				           && kernel.getTouch().getY() > kernel.getVirtualScreen().getHeight() - MARGIN - BOTTOM_MARGIN);
			}
		}
		else
		{
			_dragging = false;
			
			if (_queuePlay && _totalDrag < DRAG_TAP_THRESHOLD)
			{
				kernel.swapScreen(new LoadScreen(_beatmaps.get(_beatmapNames.get(_selectedIndex)).getPath()));
				return;
			}
		}
		
		// Change the selected item to the one closest to the center of the screen
		_selectedIndex = -1;
		float distFromCenter = Float.MAX_VALUE;
		float signedDist = Float.MAX_VALUE;
		float y = kernel.getVirtualScreen().getHeight() * .5f;
		float center = y;
		y += _scroll;
		if (_background != null) 	// An actual 'sprite-loading completed' flag would be a better idea...
		{
			for (int i = 0; i < _beatmapNames.size(); ++i)
			{
				float dist = Math.abs(y - center);
				if (dist < distFromCenter)
				{
					_selectedIndex = i;
					distFromCenter = dist;
					signedDist = center - y;
				}
	
				TexturedQuad s = _renderedBeatmapNames.get(_beatmapNames.get(i));
				y += s.getHeight() + MARGIN;
			}
		}
		
		// Snap-into-place animation
		if (!_dragging && _selectedIndex >= 0 && distFromCenter > 1e-6)
		{
			float delta = Math.signum(signedDist) * SNAP_SPEED * dt;
			if (Math.abs(delta) > Math.abs(signedDist))
				delta = signedDist;
			_scroll += delta;
		}
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{				
		if (_background == null)
		{			
			Paint p = new Paint();
			p.setTextSize(30.f);
			p.setColor(Color.WHITE);
			p.setAntiAlias(true);
			
			_renderedBeatmapNames = new HashMap<String, TexturedQuad>();
			for (int i = 0; i < _beatmapNames.size(); ++i)
				_renderedBeatmapNames.put(_beatmapNames.get(i), Prerender.string(_beatmapNames.get(i), p));

			_background = TexturedQuad.fromResource(kernel, R.drawable.beatmap_background);
			_arrow = TexturedQuad.fromResource(kernel, R.drawable.beatmap_select);
			_up = TexturedQuad.fromResource(kernel, R.drawable.beatmap_select);
			_up.setRotation(90.f);
			_down = TexturedQuad.fromResource(kernel, R.drawable.beatmap_select);
			_down.setRotation(270.f);
		}
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		
		_background.getTranslation().x = .5f * w;
		_background.getTranslation().y = .5f * h;
		_background.draw(kernel);
		
		agl.Clip(0, 0, (int)w, (int)(h - BOTTOM_MARGIN));
		boolean up = false, down = false;
		float y = h * .5f;
		for (int i = 0; i < _beatmapNames.size(); ++i)
		{
			TexturedQuad s = _renderedBeatmapNames.get(_beatmapNames.get(i));
			s.getTranslation().x = MARGIN + .5f * s.getWidth() + _up.getWidth() + MARGIN;
			s.getTranslation().y = y + _scroll;
			y += s.getHeight() + MARGIN;
			
			if (_selectedIndex >= 0 && s.getTranslation().y + .5f * s.getHeight() > h - BOTTOM_MARGIN)
				down = true;
			else if (s.getTranslation().y < 0)
				up = true;
			
			if (i == _selectedIndex)
				s.setAlpha(.5f + .5f * FloatMath.sin(_time * 4.f));
			else
				s.setAlpha(1.f);
			
			s.draw(kernel);
		}
		agl.Clip(0, 0, (int)w, (int)h);
		
		if (up)
		{
			_up.getTranslation().x = .5f * _up.getWidth() + MARGIN;
			_up.getTranslation().y = .5f * _up.getHeight() + MARGIN;
			_up.setAlpha(.5f + .5f * FloatMath.sin(_time * 2.f));
			_up.draw(kernel);
		}
		
		if (down)
		{
			_down.getTranslation().x = .5f * _down.getWidth() + MARGIN;
			_down.getTranslation().y = h - .5f * _down.getHeight() - MARGIN;
			_down.setAlpha(.5f + .5f * FloatMath.sin(_time * 2.f));
			_down.draw(kernel);
		}
		
		_arrow.getTranslation().x = MARGIN + .5f * _arrow.getWidth();
		_arrow.getTranslation().y = .5f * h;
		_arrow.draw(kernel);
	}
}
