package hello.gl;

import android.opengl.GLES20;
import android.util.Log;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;

public class MainScreen implements Screen
{
	private boolean _loaded = false;

	@Override
	public boolean isLoaded() 
	{
		return _loaded;
	}

	@Override
	public void load(Kernel kernel) 
	{
		Log.v("MainScreen", "OpenGL: " + kernel.getGLView().getGLVersion());
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
		GLES20.glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), (float)Math.random());
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	}
}
