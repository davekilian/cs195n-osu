package osu.tutorial;

import osu.controls.Button;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

public class ButtonDemo extends DemoSlide
{
	private TexturedQuad _buttonUp;
	private TexturedQuad _buttonDown;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		if (_buttonUp == null)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			Bitmap up = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			Bitmap down = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_down, opt);
			Bitmap chrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			Bitmap shadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);

			_buttonUp = new TexturedQuad(Button.render(up, shadow, chrome, Color.RED));
			_buttonDown = new TexturedQuad(Button.render(down, shadow, chrome, Color.RED));
		}
		
		TexturedQuad s = null;;
		if (slidetime - Math.floor(slidetime) < .5f)
			s = _buttonUp;
		else
			s = _buttonDown;
		
		s.getTranslation().x = .5f * kernel.getVirtualScreen().getWidth();
		s.getTranslation().y = .25f * kernel.getVirtualScreen().getHeight();
		s.draw(kernel);
	}
}
