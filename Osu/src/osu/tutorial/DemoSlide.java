package osu.tutorial;

import dkilian.andy.Kernel;

/** Base class for a slide that demos something (usually a control */
public abstract class DemoSlide implements Slide
{
	private float _duration;
	
	public DemoSlide()
	{
		_duration = Slide.DEFAULT_DURATION;
	}

	@Override
	public float getDuration() 
	{
		return _duration;
	}

	@Override
	public void setDuration(float d) 
	{
		_duration = d;	
	}

	@Override
	public abstract void draw(Kernel kernel, float time, float slidetime);
}
