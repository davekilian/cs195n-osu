package hello.jni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import dkilian.andy.BitmapSprite;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.jni.Hello;

public class MainScreen implements Screen
{
	private boolean _loaded;

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
	
	BitmapSprite test = null;

	@Override
	public void draw(Kernel kernel, float dt) 
	{	
		Canvas c = kernel.getView().getCanvas();
		Paint p = kernel.getView().getPaint();
		
		p.setColor(Color.WHITE);
		p.setAntiAlias(true);
		p.setTextSize(24);
		
		String text = Hello.hello();
		float w = kernel.getVirtualScreen().getWidth(), h = kernel.getVirtualScreen().getHeight();
		c.drawText(text, .5f * (w - p.measureText(text)), .5f * (h - 16), p);
		
		if (test == null)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
			Bitmap b = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.icon);
			test = new BitmapSprite(b);
		}
		
		test.draw(kernel);
	}
}
