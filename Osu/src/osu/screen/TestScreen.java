package osu.screen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import osu.controls.Button;
import osu.controls.Ring;
import osu.game.HOButton;
import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.jni.agl;

public class TestScreen implements Screen
{
	private boolean _loaded = false;
	private boolean _first = true;
	private float _time = 0;
	private Button _button;

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
		
		if (_button != null)
			_button.update(kernel, _time, dt);
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

			Bitmap up = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			Bitmap down = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_down, opt);
			Bitmap chrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			Bitmap shadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);
			Bitmap ring = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring);
			Bitmap ringshadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring_shadow);
			
			HOButton event = new HOButton(kernel.getVirtualScreen().getWidth() / 2, kernel.getVirtualScreen().getHeight() / 2, 3000, false, 0);
			_button = new Button(event, Button.render(up, shadow, chrome, Color.GREEN), Button.render(down, shadow, down, Color.GREEN), null);
			_button.setApproachRing(new Ring(Ring.render(ring, ringshadow, Color.GREEN), 0.f, 0.f, 0.f, 1.f, 4.f, .75f, 0.f, 0.f));
		}

		_button.draw(kernel, _time, dt);
	}
}
