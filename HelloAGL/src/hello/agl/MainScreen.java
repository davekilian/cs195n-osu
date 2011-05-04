package hello.agl;

import android.util.FloatMath;
import android.util.Log;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.jni.agl;

public class MainScreen implements Screen
{
	private boolean _loaded = false;
	private float _time = 0;

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
//		agl.ClearColor(100.f / 255.f, 149.f / 255.f, 237.f / 255.f);
		agl.ClearColor(FloatMath.sin(_time / 5), FloatMath.sin(_time / 2), FloatMath.sin(_time / 3));
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		float r = 100.f;
		float x = .5f * (w - w * .25f) + r * FloatMath.cos(_time * 10.f);
		float y = .5f * (h - h * .25f) + r * FloatMath.sin(_time * 10.f);
		agl.DrawBitmapWithoutShaderTransformed(0, w, h, x, y, _time * 20.f, .25f, .25f);
		
//		agl.LoadIdentity();
//		agl.Scalef(kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight(), 0.f);
//		agl.UseQuadShader();
//		agl.TexturedQuad();
//		agl.ClearShader();
	}
}
