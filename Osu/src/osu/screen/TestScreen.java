package osu.screen;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import osu.controls.Ring;
import osu.controls.Spinner;
import osu.game.HOSpinner;
import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class TestScreen implements Screen
{
	private boolean _loaded = false;
	private boolean _first = true;
	private float _time = 0;
	private Spinner _spinner;

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
		
		if (_spinner != null)
			_spinner.update(kernel, _time, dt);
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
			
			Bitmap ring = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring, opt);
			Bitmap rshadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring_shadow, opt);

			TexturedQuad spiral = TexturedQuad.fromResource(kernel, R.drawable.spinner_spiral);
			TexturedQuad fill = TexturedQuad.fromResource(kernel, R.drawable.spinner_fill);
			TexturedQuad nofill = TexturedQuad.fromResource(kernel, R.drawable.spinner_nofill);
			TexturedQuad mask = TexturedQuad.fromResource(kernel, R.drawable.spinner_mask);
			
			HOSpinner event = new HOSpinner(0, 0, 3000, false, 0);
			event.setEndTiming(13000);
			
			_spinner = new Spinner(event, spiral, nofill, fill, mask, null);
			_spinner.setApproachRing(new Ring(Ring.render(ring, rshadow, Color.argb(255, 0, 100, 255))));
		}
		
		_spinner.draw(kernel, _time, dt);
	}
}
