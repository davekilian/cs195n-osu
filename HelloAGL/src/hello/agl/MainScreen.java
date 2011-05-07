package hello.agl;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;
import android.util.Log;
import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.Vector2;
import dkilian.andy.jni.agl;

public class MainScreen implements Screen
{
	private boolean _loaded = false;
	private float _time = 0;
	private float _switchTimer = Float.MAX_VALUE;
	private int _type = 0;
	private TexturedQuad _quad;

	@Override
	public boolean isLoaded() 
	{
		return _loaded;
	}

	@Override
	public void load(Kernel kernel) 
	{
		Log.v("MainScreen", "OpenGL: " + Integer.toString(kernel.getGLView().getGLVersion(), 16));
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
		_switchTimer += dt;
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_switchTimer > 1.f)
		{
			Paint p = new Paint();
			p.setColor(Color.WHITE);
			p.setTextSize(40.f);
			p.setAntiAlias(true);
			
			switch (_type)
			{
			case 0:
				_quad = Prerender.string("Hello world!", p);
				break;
			case 1:
				_quad = Prerender.circle(100.f, p);
				break;
			case 2:
				_quad = Prerender.line(Vector2.Zero(), new Vector2(50.f, 100.f), p);
				break;
			case 3:
				_quad = Prerender.rectangle(50.f, 100.f, p);
				break;
			case 4:
				_quad = Prerender.roundedRectangle(100.f, 50.f, 20.f, p);
				break;
			}
			
			_switchTimer = 0;
			_type = (_type + 1) % 5;
		}
		
		agl.ClearColor(100.f / 255.f, 149.f / 255.f, 237.f / 255.f);
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		float r = 100.f;
		_quad.getTranslation().x = .5f * (w - _quad.getWidth()) + r * FloatMath.cos(_time * 2.f);
		_quad.getTranslation().y = .5f * (h - _quad.getHeight()) + r * FloatMath.sin(_time * 2.f);
		
		_quad.setRotation(_time * 16.f);
		
		float scale = 5 * (FloatMath.sin(_time * 5f) * .5f + .5f);
		_quad.getScale().x = scale;
		_quad.getScale().y = scale;

		_quad.draw(kernel);
	}
}
