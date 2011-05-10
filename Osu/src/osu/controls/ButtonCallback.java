package osu.controls;

import osu.game.HOButton;

/**
 * Interface for an object that can receive notifications of interaction with an osu.controls.Button
 * 
 * @author dkilian
 */
public interface ButtonCallback 
{
	/**
	 * Notifies this object of a button interaction
	 * @param sender The button which was interacted with
	 * @param event The osu! event corresponding to the button that is sending this notification
	 */
	public void buttonEvent(Button sender, HOButton event);
}
