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
		agl.ClearColor(FloatMath.sin(_time / 5), FloatMath.sin(_time / 2), FloatMath.sin(_time / 3));
	}
}
