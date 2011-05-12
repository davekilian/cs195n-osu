package osu.controls;

import osu.game.HOSlider;

/**
 * Interface for an object that can receive notifications of interaction with an osu.controls.Slider
 * 
 * @author dkilian
 */
public interface SliderCallback 
{
	/**
	 * Notifies this object of a slider interaction
	 * @param sender The slider control that was interacted with
	 * @param event The event the interacted slider is associated with
	 */
	public void sliderEvent(Slider sender, HOSlider event);
}
