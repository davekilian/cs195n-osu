package osu.screen;

import osu.controls.Button;
import osu.game.HOButton;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.jni.agl;

public class TestScreen implements Screen
{
	private boolean _loaded = false;
	private boolean _first = true;
	private float _time = 0;
	private Button _button = null;

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
		
		if (kernel.getTouch().isDown() && _button != null)
			_button.interact(kernel.getTouch().getX(), kernel.getTouch().getY(), _time);
		
		// TODO: change this demo
		// - Load the button bitmap directly
		// - Whenever the user clicks, add a bezier control point there
		// - When we have enough points, draw the curve by instancing the button bitmap
		// - Then get to work on sliders.
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_first)
		{
			_first = false;

			agl.ClearColor(100.f / 255.f, 149.f / 255.f, 237.f / 255.f);
			agl.BlendPremultiplied();
			
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
			
			Bitmap shadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);
			Bitmap up = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			Bitmap down = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_down, opt);
			Bitmap chrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			
			HOButton event = new HOButton((kernel.getVirtualScreen().getWidth() - up.getWidth()) / 2,
					                      (kernel.getVirtualScreen().getHeight() - up.getHeight()) / 2,
					                      3000, false, 0);
			
			_button = new Button(event, Button.render(up, shadow, chrome, Color.MAGENTA), Button.render(down, shadow, chrome, Color.MAGENTA));
		}
		
		_button.draw(kernel, _time, dt);
	}
}
