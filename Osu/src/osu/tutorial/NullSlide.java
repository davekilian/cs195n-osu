package osu.tutorial;

import dkilian.andy.Kernel;

/**
 * Fills time
 * @author dkilian
 */
public class NullSlide implements Slide
{
	private float _len;
	
	public NullSlide()
	{
		_len = Slide.DEFAULT_DURATION;
	}
	
	public NullSlide(float duration)
	{
		_len = duration;
	}

	@Override
	public float getDuration() 
	{
		return _len;
	}

	@Override
	public void setDuration(float d) 
	{
		_len = d;
	}

	@Override
	public void draw(Kernel kernel, float t, float slidetime) {}
}
