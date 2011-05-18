package osu.tutorial;

import osu.controls.Button;
import osu.graphics.BitmapTint;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class SliderDemo extends DemoSlide
{
	private TexturedQuad cap, fill;
	private float[] sliderPoints;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		if (cap == null)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			Bitmap up = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			Bitmap down = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_down, opt);
			Bitmap chrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			Bitmap shadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);

			up = BitmapTint.apply(up, Color.GREEN);
			down = BitmapTint.apply(down, Color.GREEN);
			
			cap = new TexturedQuad(Button.render(up, shadow, chrome));
			fill = new TexturedQuad(Button.render(up, shadow, up));
			
			float w = kernel.getVirtualScreen().getWidth();
			float h = kernel.getVirtualScreen().getHeight();
			sliderPoints = new float[]
         	{
				.25f * w,
				.25f * h,
				.75f * w,
				.25f * h
         	};
		}
		
		agl.InstanceBitmapBezier(fill.getTexture(), fill.getWidth(), fill.getHeight(), sliderPoints, 2, 20, 0.f, 1.f, 0.f, 1.f, 1.f, 1.f);
		agl.DrawBitmapWithoutShaderTranslated(cap.getTexture(), cap.getWidth(), cap.getHeight(), sliderPoints[0], sliderPoints[1], 1.f);
		agl.DrawBitmapWithoutShaderTranslated(cap.getTexture(), cap.getWidth(), cap.getHeight(), sliderPoints[2], sliderPoints[3], 1.f);
	}
}
