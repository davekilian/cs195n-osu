package osu.tutorial;

import osu.controls.Button;
import osu.controls.Ring;
import osu.graphics.BitmapTint;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class SlidingDemo extends DemoSlide
{
	private TexturedQuad cap, fill, nub;
	private TexturedQuad _ring;
	private float[] sliderPoints;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		_duration = 10.f;
		
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
			nub = new TexturedQuad(Button.render(down, shadow, chrome));
			
			float w = kernel.getVirtualScreen().getWidth();
			float h = kernel.getVirtualScreen().getHeight();
			sliderPoints = new float[]
         	{
				.25f * w,
				.25f * h,
				.75f * w,
				.25f * h
         	};

			Bitmap ring = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring, opt);
			Bitmap rshadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring_shadow, opt);

			_ring = new TexturedQuad(Ring.render(ring, rshadow, Color.GREEN));
		}
		
		agl.InstanceBitmapBezier(fill.getTexture(), fill.getWidth(), fill.getHeight(), sliderPoints, 2, 20, 0.f, 1.f, 0.f, 1.f, 1.f, 1.f);
		agl.DrawBitmapWithoutShaderTranslated(cap.getTexture(), cap.getWidth(), cap.getHeight(), sliderPoints[0], sliderPoints[1], 1.f);
		agl.DrawBitmapWithoutShaderTranslated(cap.getTexture(), cap.getWidth(), cap.getHeight(), sliderPoints[2], sliderPoints[3], 1.f);
		
		float x = 0.f, y = 0.f;
		float ralpha = 0.f, nubalpha = 0.f;
		float rscale = 1.f;
		if (slidetime < 7.5f)
		{
			float pos = slidetime / 7.5f;
			x = sliderPoints[0];
			y = sliderPoints[1];
			rscale = 4.f * (1.f - pos) + 1.f * pos;
			ralpha = pos;
			nubalpha = 0.f;
		}
		else
		{
			float pos = (slidetime - 7.5f) / 2.5f;
			x = (1.f - pos) * sliderPoints[0] + pos * sliderPoints[2];
			y = sliderPoints[1];
			rscale = 2.f;
			ralpha = 1.f;
			nubalpha = 1.f;
		}
		
		_ring.getTranslation().x = x;
		_ring.getTranslation().y = y;
		_ring.setAlpha(ralpha);
		_ring.getScale().x = rscale;
		_ring.getScale().y = rscale;
		_ring.draw(kernel);
		
		nub.getTranslation().x = x;
		nub.getTranslation().y = y;
		nub.setAlpha(nubalpha);
		nub.draw(kernel);
	}
}
