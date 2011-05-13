package osu.screen;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import osu.controls.Button;
import osu.controls.Ring;
import osu.controls.Slider;
import osu.game.HOSlider;
import osu.graphics.BitmapTint;
import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.PrerenderCache;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class TestScreen implements Screen
{	
	private boolean _loaded = false;
	private boolean _first = true;
	private float _time = 0;
	private Slider _slider;
	private PrerenderCache _textCache;

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
			_slider.update(kernel, _time, dt);
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_first)
		{
			_first = false;
			
			agl.ClearColor(100.f / 255.f, 149.f / 255.f, 237.f / 255.f);
			agl.BlendPremultiplied();
			
			Paint p = new Paint();
			_textCache = new PrerenderCache(p);
			p.setColor(Color.WHITE);
			p.setTextSize(60.f);
			p.setAntiAlias(true);

			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
			
			Bitmap up = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			Bitmap down = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_down, opt);
			Bitmap chrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			Bitmap shadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);
			Bitmap ring = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring);
			Bitmap ringshadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring_shadow);
 			
			up = BitmapTint.apply(up, Color.GREEN);
			down = BitmapTint.apply(down, Color.GREEN);
			ring = BitmapTint.apply(ring, Color.GREEN);

			LinkedList<Point> points = new LinkedList<Point>();
			points.add(new Point(                                          64,                                         64));
			points.add(new Point(kernel.getVirtualScreen().getWidth() * 1 / 3, kernel.getVirtualScreen().getHeight() - 64));
			points.add(new Point(kernel.getVirtualScreen().getWidth() * 2 / 3,                                         64));
			points.add(new Point(   kernel.getVirtualScreen().getWidth() - 64, kernel.getVirtualScreen().getHeight() - 64));
			
			HOSlider event = new HOSlider(0, 0, 3000, false, 0);
			event.setPathPoints(points);
			event.setRepeats(4);
 			
			_slider = new Slider(event, 1000.f, 1.f, 340.f,
								 Button.render(up, shadow, chrome), 
								 Button.render(up, shadow, up), 
								 Button.render(up, shadow, chrome), 
					             Button.render(down, shadow, chrome), 
					             TexturedQuad.fromResource(kernel, R.drawable.slider_return),
					             null,
					             _textCache,
					             "2");
			_slider.setApproachRing(new Ring(Ring.render(ring, ringshadow)));
		}
		
		_slider.draw(kernel, _time, dt);
	}
}
