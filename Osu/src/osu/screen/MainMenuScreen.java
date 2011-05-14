package osu.screen;

import dkilian.andy.Kernel;
import dkilian.andy.Screen;

public class MainMenuScreen implements Screen
{
	private boolean _loaded = false;

	@Override
	public boolean isLoaded() 
	{
		return _loaded;
	}

	@Override
	public void load(Kernel kernel) 
	{
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
		kernel.swapScreen(new LoadScreen("/osu/beatmaps/tsugaru/test-tsugaru.osu"));
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		
	}
}
