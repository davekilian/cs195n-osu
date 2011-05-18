package osu.tutorial;

import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.TexturedQuad;

public class MissDemo extends DemoSlide
{
	private TexturedQuad miss;

	@Override
	public void draw(Kernel kernel, float time, float slidetime) 
	{
		if (miss == null)
			miss = TexturedQuad.fromResource(kernel, R.drawable.no);
		
		miss.getTranslation().x = .5f * kernel.getVirtualScreen().getWidth();
		miss.getTranslation().y = .25f * kernel.getVirtualScreen().getHeight();
		miss.draw(kernel);
	}
}
