package osu.tutorial;

import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class SpinningDemo extends DemoSlide
{
	private TexturedQuad mask, fill, nofill, spiral;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		if (mask == null)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			Bitmap bmask = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_mask, opt);
			Bitmap bfill = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_fill, opt);
			Bitmap bnofill = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_nofill, opt);
			Bitmap bspiral = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_spiral, opt);
			
			mask = new TexturedQuad(bmask.copy(Bitmap.Config.ARGB_8888, false));
			nofill = new TexturedQuad(bnofill.copy(Bitmap.Config.ARGB_8888, false));
			fill = new TexturedQuad(bfill.copy(Bitmap.Config.ARGB_8888, false));
			spiral = new TexturedQuad(bspiral.copy(Bitmap.Config.ARGB_8888, false));
		}
		
		float x = .5f * kernel.getVirtualScreen().getWidth();
		float y = .5f * kernel.getVirtualScreen().getHeight();

		float scale = (float)kernel.getVirtualScreen().getWidth() / (float)mask.getWidth();
		
		spiral.getTranslation().x = x;
		spiral.getTranslation().y = y;
		spiral.getScale().x = scale;
		spiral.getScale().y = scale;
		spiral.setRotation(360.f * slidetime * 2.f);
		spiral.draw(kernel);
		
		fill.getTranslation().x = x;
		fill.getTranslation().y = y;
		fill.getScale().x = scale;
		fill.getScale().y = scale;
		fill.draw(kernel);
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		float power = slidetime / _duration;

		agl.Clip(0, 0, (int)w, (int)(((1.f - power) * h)));
		nofill.getTranslation().x = x;
		nofill.getTranslation().y = y;
		nofill.getScale().x = scale;
		nofill.getScale().y = scale;
		nofill.draw(kernel);
		agl.Clip(0, 0, (int)w, (int)h);
		
		mask.getTranslation().x = x;
		mask.getTranslation().y = y;
		mask.getScale().x = scale;
		mask.getScale().y = scale;
		mask.draw(kernel);
	}
}
