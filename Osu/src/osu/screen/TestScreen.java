package osu.screen;

import java.util.LinkedList;

import osu.controls.Button;
import osu.controls.Slider;
import osu.game.HOSlider;
import osu.graphics.BitmapTint;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.jni.agl;

public class TestScreen implements Screen
{
	private boolean _loaded = false;
	private boolean _first = true;
	private float _time = 0;
	private Slider _slider;
	private HOSlider _event;

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
		
		if (_slider != null)
		{
			_slider.update(kernel, _time, dt);
			_slider.setNubPosition(.5f + .5f * FloatMath.sin(_time));
		}
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
			
			LinkedList<Point> points = new LinkedList<Point>();
			points.add(new Point(                                          64,                                         64));
			points.add(new Point(kernel.getVirtualScreen().getWidth() * 1 / 3, kernel.getVirtualScreen().getHeight() - 64));
			points.add(new Point(kernel.getVirtualScreen().getWidth() * 2 / 3,                                         64));
			points.add(new Point(   kernel.getVirtualScreen().getWidth() - 64, kernel.getVirtualScreen().getHeight() - 64));
			
			_event = new HOSlider(15, 15, 0, true, 0);
			_event.setRepeats(Integer.MAX_VALUE);
			_event.setPathPoints(points);
			
			up = BitmapTint.apply(up, Color.RED);
			down = BitmapTint.apply(down, Color.RED);
			
			_slider = new Slider(_event, Button.render(up, shadow, chrome), Button.render(up, shadow, up), Button.render(up, shadow, chrome), Button.render(down, shadow, down));
		}
		
		_slider.draw(kernel, _time, dt);
	}
}
