package osu.screen;

import osu.main.R;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;
import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;

public class MainMenuScreen implements Screen
{
	public static final float FADE_IN_TIME = 1.f;
	
	private enum Action
	{
		TUTORIAL,
		PLAY,
		CALIBRATE
	}
	
	private boolean _loaded = false;
	private boolean _queued = false;
	private Action _action;
	private TexturedQuad _background;
	private TexturedQuad _sheen;
	private TexturedQuad _fade;
	private float _time = 0.f;
	private boolean _fadeIn = false;
	
	public MainMenuScreen() {}
	
	public MainMenuScreen(boolean fade) { _fadeIn = fade; }

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
		
		if (kernel.getTouch().isDown() && kernel.getTouch().getX() > .5f * kernel.getVirtualScreen().getWidth())
		{
			float y = kernel.getTouch().getY();
			
			if (y >= 50 && y <= 150)
			{
				_queued = true;
				_action = Action.TUTORIAL;
			}
			else if (y >= 150.f && y <= 250.f)
			{
				_queued = true;
				_action = Action.PLAY;
			}
			else if (y >= 250.f && y <= 350.f)
			{
				_queued = true;
				_action = Action.CALIBRATE;
			}			
		}
		else if (_queued)
		{
			if (_action == Action.TUTORIAL)
				kernel.swapScreen(new TutorialScreen());
			else if (_action == Action.PLAY)
				kernel.swapScreen(new SelectScreen());
		}
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_background == null)
		{
			_background = TexturedQuad.fromResource(kernel, R.drawable.main_menu);
			_sheen = TexturedQuad.fromResource(kernel, R.drawable.main_menu_sheen);
			Paint p = new Paint();
			p.setColor(Color.BLACK);
			_fade = Prerender.rectangle(kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight(), p);
		}
		
		float x = kernel.getVirtualScreen().getWidth() * .5f;
		float y = kernel.getVirtualScreen().getHeight() * .5f;
		
		_background.getTranslation().x = x;
		_background.getTranslation().y = y;
		_background.draw(kernel);
		
		float sharpness = 30.f;
		float alpha = Math.max(0.f, sharpness * (.5f + .5f * FloatMath.sin(_time * 2.f)) - (sharpness - 1.f));
		
		_sheen.getTranslation().x = x;
		_sheen.getTranslation().y = y;
		_sheen.setAlpha(alpha);
		_sheen.draw(kernel);
		
		if (_fadeIn && _time < FADE_IN_TIME)
		{
			_fade.setAlpha(1.f - _time / FADE_IN_TIME);
			_fade.getTranslation().x = x;
			_fade.getTranslation().y = y;
			_fade.draw(kernel);
		}
	}
}
