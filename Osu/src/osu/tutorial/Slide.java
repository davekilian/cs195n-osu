package osu.tutorial;

import dkilian.andy.Kernel;

/**
 * Interface for a slide in a tutorial slideshow
 * @author dkilian
 */
public interface Slide 
{
	/** The default duration of a slide, in partial seconds */
	public static final float DEFAULT_DURATION = 5.f;
	
	/** Gets the amount of time this slide will be visible, in partial seconds */
	public float getDuration();
	/** Sets the amount of time this slide will be visible, in partial seconds */
	public void setDuration(float d);
	
	/**
	 * Renders this slide
	 * @param kernel The currently executing kernel
	 * @param t The current time into the slideshow, in partial seconds
	 * @param slidetime The current time into this slide, in partial seconds
	 */
	public void draw(Kernel kernel, float t, float slidetime);
}
