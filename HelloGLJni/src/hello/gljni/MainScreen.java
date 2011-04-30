package hello.gljni;

import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.jni.Hello;

public class MainScreen implements Screen
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
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{		
		Hello.helloGL();
	}
}
