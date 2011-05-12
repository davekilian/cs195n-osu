package osu.controls;

import osu.game.HOSpinner;

/**
 * Interface for an object that can receive notifications of interactions with an osu.control.Spinner
 * 
 * @author dkilian
 */
public interface SpinnerCallback 
{
	/**
	 * Notifies this object of a spinner interaction
	 * @param spinner The spinner control that was interacted with
	 * @param event The event the interacted spinner is associated with
	 */
	public void spinnerEvent(Spinner spinner, HOSpinner event);
}

