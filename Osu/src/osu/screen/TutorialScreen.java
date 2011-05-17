package osu.screen;

import osu.main.R;
import osu.tutorial.OsuTutorial;
import osu.tutorial.SlidePlayer;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.jni.agl;

/**
 * Plays the tutorial
 * @author dkilian
 */
public class TutorialScreen implements Screen
{
	private SlidePlayer _tutorial;
	private TexturedQuad _background;

	@Override
	public boolean isLoaded() 
	{
		return _tutorial != null;
	}

	@Override
	public void load(Kernel kernel) 
	{
		_tutorial = OsuTutorial.makeSlides(kernel);
	}

	@Override
	public void unload(Kernel kernel) 
	{
		_tutorial = null;
	}

	@Override
	public void update(Kernel kernel, float dt) 
	{
		_tutorial.update(kernel, dt);
		if (_tutorial.isDone())
			kernel.swapScreen(new MainMenuScreen());
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_background == null)
		{
			_background = TexturedQuad.fromResource(kernel, R.drawable.tutorial_background);
			_background.getTranslation().x = .5f * kernel.getVirtualScreen().getWidth();
			_background.getTranslation().y = .5f * kernel.getVirtualScreen().getHeight();
		}
		
		agl.Clip(0, 0, kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight());
		
		_background.draw(kernel);
		_tutorial.draw(kernel, dt);
	}
}
