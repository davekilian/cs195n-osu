package hello.agl;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;
import android.util.Log;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class MainScreen implements Screen
{
	private boolean _loaded = false;
	private float _time = 0;
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
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_quad == null)
		{
			Paint p = new Paint();
			p.setColor(Color.WHITE);
			p.setTextSize(40.f);
			p.setAntiAlias(true);
			
			_quad = TexturedQuad.render("Hello world! Hello world!", p);
		}
		
		agl.ClearColor(FloatMath.sin(_time / 5), FloatMath.sin(_time / 2), FloatMath.sin(_time / 3));
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		float r = 0.f; //100.f;
		_quad.getTranslation().x = .5f * (w - _quad.getWidth()) + r * FloatMath.cos(_time * 2.f);
		_quad.getTranslation().y = .5f * (h - _quad.getHeight()) + r * FloatMath.sin(_time * 2.f);
		
		_quad.setRotation(90.f);
//		_quad.setRotation(_time * 16.f);
		
		float scale = 1.f; //5 * (FloatMath.sin(_time * 5f) * .5f + .5f);
		_quad.getScale().x = scale;
		_quad.getScale().y = scale;

		_quad.draw(kernel);
	}
}
