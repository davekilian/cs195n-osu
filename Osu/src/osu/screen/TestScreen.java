package osu.screen;

import osu.controls.Button;
import osu.graphics.BitmapTint;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class TestScreen implements Screen
{
	private boolean _loaded = false;
	private boolean _first = true;
	private float _time = 0;
	private TexturedQuad _fill, _cap;
	private float[] _points = new float[0];
	private boolean _plotting = false;

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
		{
			if (!_plotting)
			{
				_plotting = true;
				float[] tmp = new float[_points.length + 2];
				for (int i = 0; i < _points.length; ++i)
					tmp[i] = _points[i];
				tmp[_points.length] = kernel.getTouch().getX();
				tmp[_points.length + 1] = kernel.getTouch().getY();
				_points = tmp;
			}
		}
		else if (_plotting)
		{
			_plotting = false;
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
			
			up = BitmapTint.apply(up, Color.BLUE);
			_fill = Button.render(up, shadow, up);
			_cap = Button.render(up, shadow, chrome);
		}
		
		if (_points.length >= 4)
		{
			agl.InstanceBitmapBezier(_fill.getTexture(), _fill.getWidth(), _fill.getHeight(), _points, _points.length / 2, 3 * _points.length, 0.f, 1.f, 1.f, 1.f);
			agl.DrawBitmapWithoutShaderTranslated(_cap.getTexture(), _cap.getWidth(), _cap.getHeight(), _points[0], _points[1], 1.f);
			agl.DrawBitmapWithoutShaderTranslated(_cap.getTexture(), _cap.getWidth(), _cap.getHeight(), _points[_points.length - 2], _points[_points.length - 1], 1.f);
		}
	}
}
