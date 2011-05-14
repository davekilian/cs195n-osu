package osu.controls;

import osu.game.HitObject;
import dkilian.andy.Kernel;
import android.graphics.Rect;

/**
 * Interface for classes that can be interacted with during
 * normal gameplay
 * 
 * @author dkilian
 */
public interface Control 
{
	/** Gets the X coordinate of this control's center, in virtual space */
	public float getX();
	/** Sets the X coordinate of this control's center, in virtual space */
	public void setX(float x);

	/** Gets the Y coordinate of this control's center, in virtual space */
	public float getY();
	/** Sets the Y coordinate of this control's center, in virtual space */
	public void setY(float y);
	
	/** Gets the time, in partial seconds, from the beginning of the song this control should be made visible */
	public float getStartTime();
	/** Sets the time, in partial seconds, from the beginning of the song this control should be made visible */
	public void setStartTime(float t);
	
	/** Gets the time, in partial seconds, from the beginning of the song this control should be hidden again */
	public float getEndTime();
	/** Sets the time, in partial seconds, from the beginning of the song this control should be hidden again */
	public void setEndTime(float t);
	
	/** Gets the hit object corresponding to this control */
	public HitObject getEvent();
	
	/**
	 * Determines whether or not this control should be visible at the given time
	 * @param t The current time in partial seconds from the beginning of the song
	 */
	public boolean isVisible(float t);
	
	/** Gets the bounds of this control. Any touch that lands inside this hitbox may trigger this control's interact() */
	public Rect getHitbox();
	
	/**
	 * Sends a touch event to this control. This method is called for every frame of the game in which
	 * the following criteria are met:
	 * 
	 * - The touch is inside the control's hitbox
	 * - The control is visible 
	 * - There are no nearby controls that are triggered at earlier times than this control
	 * 
	 * @param x The virtual space X coordinate of the user's touch
	 * @param y The virtual space Y coordinate of the user's touch
	 * @param t The game clock time at which the interaction occurred
	 */
	public void interact(float x, float y, float t);
	
	/**
	 * Does any per-frame maintenance on this control that is not related to receiving touch events
	 * @param kernel The currently executing kernel
	 * @param t The current game clock, in partial seconds
	 * @param dt The amount of time that passed between the previous and current frame, in partial seconds 
	 */
	public void update(Kernel kernel, float t, float dt);

	/**
	 * Renders this control
	 * @param kernel The currently executing kernel
	 * @param t The current game clock, in partial seconds
	 * @param dt The amount of time that passed between the previous and current frame, in partial seconds 
	 */
	public void draw(Kernel kernel, float t, float dt);
}
