package osu.screen;

import android.graphics.Color;
import android.graphics.Paint;
import osu.beatmap.BeatmapLoader;
import osu.beatmap.BeatmapPlayer;
import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.PrerenderContext;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

/**
 * Shown when the game is loading a beatmap
 * 
 * @author dkilian
 */
public class LoadScreen implements Screen
{
	private boolean _loaded = false;
	
	private String _path;
	
	private BeatmapLoader _loader;
	
	private PrerenderContext _context;
	
	private TexturedQuad _text;
	
	private boolean _renderedCombos = false;
	
	private TexturedQuad _background;
	
	private TexturedQuad _progress;

	private boolean _drawing = false;
	
	public LoadScreen(String path)
	{
		_path = path;
	}

	@Override
	public boolean isLoaded() 
	{
		return _loaded;
	}

	@Override
	public void load(Kernel kernel) 
	{
		_loader = new BeatmapLoader(kernel, _path);
		_loader.begin();
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
		if (_loader != null && !_loader.isLoading() && _renderedCombos)
		{
			// Clear out all load-time temporary resources before the game starts
			BeatmapPlayer bp = _loader.getBeatmap();
			while (_drawing)
			{
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException ex) {}
			}
			synchronized (_loader)
			{
				_path = null;
				_loader = null;
				_context = null;
				_text = null;
				_background = null;
				_progress = null;
			}
			System.gc();
			
			kernel.swapScreen(new PlayScreen(bp));
			return;
		}
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		_drawing = true;
		
		if (_context == null)
		{
			Paint p = new Paint();
			p.setTextSize(20.f);
			p.setColor(Color.WHITE);
			p.setAntiAlias(true);
			
			_context = new PrerenderContext(500, 20, p);
			_text = new TexturedQuad(agl.CreateEmptyTexture(), 500, 20);
			
			_background = TexturedQuad.fromResource(kernel, R.drawable.loading_screen);
			_progress = TexturedQuad.fromResource(kernel, R.drawable.loading_screen_progress);
		}
		
		float progress = 0;
		if (_loader != null)
		{
			synchronized (_loader)
			{
				if (_loader != null && !_loader.isLoading() && !_renderedCombos)
				{				
					for (int combo = 1; combo <= _loader.getHighestCombo(); ++combo)
						_loader.getBeatmap().getTextCache().string(Integer.toString(combo));
					_renderedCombos = true;
				}
				
				progress = _loader.getProgress();
				Prerender.string(_loader.getProgressString(), _context, _text);
				_loader.doGLTasks();
			}
		}
		
		float x = .5f * kernel.getVirtualScreen().getWidth();
		float y = .5f * kernel.getVirtualScreen().getHeight();
		_background.getTranslation().x = x;
		_background.getTranslation().y = y;
		_background.draw(kernel);

		agl.Clip(0, 0, (int)(kernel.getVirtualScreen().getWidth() * progress), kernel.getVirtualScreen().getHeight());
		_progress.getTranslation().x = x;
		_progress.getTranslation().y = y;
		_progress.draw(kernel);
		agl.Clip(0, 0, kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight());
		
		_text.getTranslation().x = kernel.getVirtualScreen().getWidth() * .5f;
		_text.getTranslation().y = kernel.getVirtualScreen().getHeight() * .75f;
		_text.draw(kernel);
		
		_drawing = false;
	}
}
