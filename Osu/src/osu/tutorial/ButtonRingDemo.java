package osu.tutorial;

import osu.controls.Button;
import osu.controls.Ring;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

public class ButtonRingDemo extends DemoSlide
{
	private TexturedQuad _buttonUp;
	private TexturedQuad _buttonDown;
	private TexturedQuad _ring;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		_duration = 10.f;
		
		if (_buttonUp == null)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			Bitmap up = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			Bitmap down = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_down, opt);
			Bitmap chrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			Bitmap shadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);

			Bitmap ring = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring, opt);
			Bitmap rshadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.ring_shadow, opt);

			_buttonUp = new TexturedQuad(Button.render(up, shadow, chrome, Color.RED));
			_buttonDown = new TexturedQuad(Button.render(down, shadow, chrome, Color.RED));
			_ring = new TexturedQuad(Ring.render(ring, rshadow, Color.RED));
		}
		
		TexturedQuad s = null;
		slidetime -= Math.floor(slidetime);
		if (slidetime < .75f)
			s = _buttonUp;
		else
			s = _buttonDown;
		
		s.getTranslation().x = .5f * kernel.getVirtualScreen().getWidth();
		s.getTranslation().y = .25f * kernel.getVirtualScreen().getHeight();
		s.draw(kernel);
		
		if (slidetime < .75f)
		{
			float pos = slidetime / .75f;
			float scale = 4.f * (1.f - pos) + 1.f * pos;
			float alpha = pos;
			_ring.getTranslation().x = s.getTranslation().x;
			_ring.getTranslation().y = s.getTranslation().y;
			_ring.setAlpha(alpha);
			_ring.getScale().x = scale;
			_ring.getScale().y = scale;
			_ring.draw(kernel);
		}
	}
}