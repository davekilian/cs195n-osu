package osu.tutorial;

import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

public class SpinnerDemo extends DemoSlide
{
	private TexturedQuad mask, nofill, spiral;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		if (mask == null)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			Bitmap bmask = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_mask, opt);
			Bitmap bnofill = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_nofill, opt);
			Bitmap bspiral = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.spinner_spiral, opt);
			
			mask = new TexturedQuad(bmask.copy(Bitmap.Config.ARGB_8888, false));
			nofill = new TexturedQuad(bnofill.copy(Bitmap.Config.ARGB_8888, false));
			spiral = new TexturedQuad(bspiral.copy(Bitmap.Config.ARGB_8888, false));
		}
		
		float x = .5f * kernel.getVirtualScreen().getWidth();
		float y = .5f * kernel.getVirtualScreen().getHeight();

		float scale = (float)kernel.getVirtualScreen().getWidth() / (float)mask.getWidth();
		
		spiral.getTranslation().x = x;
		spiral.getTranslation().y = y;
		spiral.getScale().x = scale;
		spiral.getScale().y = scale;
		spiral.draw(kernel);
		
		nofill.getTranslation().x = x;
		nofill.getTranslation().y = y;
		nofill.getScale().x = scale;
		nofill.getScale().y = scale;
		nofill.draw(kernel);
		
		mask.getTranslation().x = x;
		mask.getTranslation().y = y;
		mask.getScale().x = scale;
		mask.getScale().y = scale;
		mask.draw(kernel);
	}
}
