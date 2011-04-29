package dkilian.andy;

/**
 * Common interfaces for game screens. 
 * 
 * Screens are logically distinct portions of a 
 * game. For example, the main menu, a submenu, 
 * and the main game might all be different
 * screens.
 * 
 * @author dkilian
 */
public interface Screen 
{
	/** Gets a value indicating whether this screen needs to be loaded */
	boolean isLoaded();
	
	/** Loads resources required by this screen */
	void load(Kernel kernel);
	
	/** Unloads resources in use by this screen */
	void unload(Kernel kernel);
	
	/**
	 * Allows the screen to update itself. Any time-based animations, 
	 * game logic, and input processing should occur in your screen's
	 * update()
	 * 
	 * @param kernel A reference to the kernel showing this screen
	 * @param dt The amount of time, in partial seconds, that has passed
	 * 			 since the last time update() was called by the kernel.
	 */
	void update(Kernel kernel, float dt);
	
	/**
	 * Allows the screen to render itself. Rendering logic should
	 * occur in your screen's draw(0
	 * 
	 * @param kernel A reference to the kernel showing this screen
	 * @param dt The amount of time, in partial seconds, that has passed
	 * 			 since the last time draw() was called by the kernel.
	 */
	void draw(Kernel kernel, float dt);
}
