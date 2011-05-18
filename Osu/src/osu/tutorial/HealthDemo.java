package osu.tutorial;

import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

public class HealthDemo extends DemoSlide
{
	private TexturedQuad health, bar, danger;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		if (health == null)
		{
			health = TexturedQuad.fromResource(kernel, R.drawable.health);
			bar = TexturedQuad.fromResource(kernel, R.drawable.health_bar);
			danger = TexturedQuad.fromResource(kernel, R.drawable.health_danger);
		}
		
		float amount = 1.f - slidetime / _duration;
		float x = .5f * bar.getWidth(), y = .5f * bar.getHeight();
		
		bar.getTranslation().x = x;
		bar.getTranslation().y = y;
		bar.draw(kernel);
		
		agl.Clip(0, 0, (int)(bar.getWidth() * amount), (int)(bar.getHeight()));
		health.getTranslation().x = x;
		health.getTranslation().y = y;
		health.draw(kernel);
		agl.Clip(0, 0, kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight());
		
		if (amount < .5f)
		{
			danger.getTranslation().x = x;
			danger.getTranslation().y = y;
			danger.setAlpha(1.f - amount / .5f);
			danger.draw(kernel);		
		}
	}
}
