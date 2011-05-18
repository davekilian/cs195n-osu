package osu.tutorial;

import osu.controls.Button;
import osu.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.TexturedQuad;

public class ComboDemo extends DemoSlide
{
	private TexturedQuad button, text1, text2;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		if (button == null)
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			Bitmap up = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_up, opt);
			Bitmap chrome = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_chrome, opt);
			Bitmap shadow = BitmapFactory.decodeResource(kernel.getActivity().getResources(), R.drawable.button_shadow, opt);

			button = new TexturedQuad(Button.render(up, shadow, chrome, Color.RED));
			
			Paint p = new Paint();
			p.setTextSize(80.f);
			p.setColor(Color.WHITE);
			p.setAntiAlias(true);

			text1 = Prerender.string("1", p);
			text2 = Prerender.string("2", p);
		}
		
		button.getTranslation().x = .25f * kernel.getVirtualScreen().getWidth();
		button.getTranslation().y = .25f * kernel.getVirtualScreen().getHeight();
		button.draw(kernel);
		
		text1.getTranslation().x = .25f * kernel.getVirtualScreen().getWidth();
		text1.getTranslation().y = .25f * kernel.getVirtualScreen().getHeight();
		text1.draw(kernel);
		
		button.getTranslation().x = .75f * kernel.getVirtualScreen().getWidth();
		button.getTranslation().y = .25f * kernel.getVirtualScreen().getHeight();
		button.draw(kernel);
		
		text2.getTranslation().x = .75f * kernel.getVirtualScreen().getWidth();
		text2.getTranslation().y = .25f * kernel.getVirtualScreen().getHeight();
		text2.draw(kernel);
	}
}
