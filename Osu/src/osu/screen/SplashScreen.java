package osu.screen;

import dkilian.andy.Kernel;
import dkilian.andy.Screen;

public class SplashScreen implements Screen
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
		// Testing in-game controls
		kernel.swapScreen(new TestScreen());
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		
	}
}
