package hello.jni;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	}
}
