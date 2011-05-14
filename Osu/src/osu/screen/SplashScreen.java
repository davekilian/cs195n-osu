package osu.screen;

import android.util.FloatMath;
import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;

public class SplashScreen implements Screen
{
	private boolean _loaded = false;
	
	private boolean _queued = false;
	
	private TexturedQuad _splash;
	
	private TexturedQuad _sheen;
	
	private float _time = 0.f;

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
		_time += dt;
		
		if (kernel.getTouch().isDown())
			_queued = true;
		else if (_queued)
			kernel.swapScreen(new MainMenuScreen());
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_splash == null)
		{
			_splash = TexturedQuad.fromResource(kernel, R.drawable.splash);
			_sheen = TexturedQuad.fromResource(kernel, R.drawable.splash_sheen);
		}
		
		float x = kernel.getVirtualScreen().getWidth() * .5f;
		float y = kernel.getVirtualScreen().getHeight() * .5f;
		
		_splash.getTranslation().x = x;
		_splash.getTranslation().y = y;
		_splash.draw(kernel);
		
		float sharpness = 30.f;
		float alpha = Math.max(0.f, sharpness * (.5f + .5f * FloatMath.sin(_time * 2.f)) - (sharpness - 1.f));
		
		_sheen.getTranslation().x = x;
		_sheen.getTranslation().y = y;
		_sheen.setAlpha(alpha);
		_sheen.draw(kernel);
	}
}
