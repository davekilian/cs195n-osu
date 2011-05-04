package hello.agl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.jni.agl;

public class MainScreen implements Screen
{
	private boolean _loaded = false;
	private float _time = 0;
	private int _tex = 0;
	private int _w = 0, _h = 0;

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
		agl.DeleteTexture(_tex);
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
		if (_tex == 0)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
			Bitmap b = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.icon);
			_w = b.getWidth();
			_h = b.getHeight();
			_tex = agl.CreateEmptyTexture();
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
		}
		
		agl.ClearColor(FloatMath.sin(_time / 5), FloatMath.sin(_time / 2), FloatMath.sin(_time / 3));
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		float r = 100.f;
		float x = .5f * (w - w * .25f) + r * FloatMath.cos(_time * 2.f);
		float y = .5f * (h - h * .25f) + r * FloatMath.sin(_time * 2.f);
		float scale = 5 * (FloatMath.sin(_time * 5f) * .5f + .5f);
		agl.DrawBitmapWithoutShaderTransformed(_tex, _w, _h, x, y, _time * 16.f, scale, scale);
	}
}
