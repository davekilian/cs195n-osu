package osu.screen;

import android.graphics.Color;
import android.graphics.Paint;
import osu.beatmap.BeatmapLoader;
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
			kernel.swapScreen(new PlayScreen(_loader.getBeatmap()));
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_context == null)
		{
			Paint p = new Paint();
			p.setTextSize(20.f);
			p.setColor(Color.WHITE);
			p.setAntiAlias(true);
			
			_context = new PrerenderContext(500, 20, p);
			_text = new TexturedQuad(agl.CreateEmptyTexture(), 500, 20);
		}
		
		if (_loader != null && !_loader.isLoading() && !_renderedCombos)
		{				
			for (int combo = 1; combo <= _loader.getHighestCombo(); ++combo)
				_loader.getBeatmap().getTextCache().string(Integer.toString(combo));
			_renderedCombos = true;
		}
		
		Prerender.string(_loader.getProgressString(), _context, _text);
		
		_text.getTranslation().x = kernel.getVirtualScreen().getWidth() * .5f;
		_text.getTranslation().y = kernel.getVirtualScreen().getHeight() * .5f;
		_text.draw(kernel);
		
		_loader.doGLTasks();
		
		agl.Clip(0, 0, kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight());
	}
}
