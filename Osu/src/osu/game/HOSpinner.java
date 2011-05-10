package osu.game;

/**
 * HOSpinner is one of the HitObject representations.
 * This is an object you need to spin around in a circle as
 * many times as possible in the given duration to pass it.
 */
public class HOSpinner extends HitObject {
	
	private long end_time;
	// Do we need # of spins and such?
	
	
	// *** CONSTRUCTOR *** //
	public HOSpinner(int x_pos, int y_pos, long time_millis, boolean is_new_combo, int sound)
	{
		x = x_pos;
		y = y_pos;
		
		timing = time_millis;
		
		new_combo = is_new_combo;
		
		sound_type = sound;
	}
	
	
	// *** ACCESSORS *** //
	public long getEndTiming() { return end_time; }
	
	
	public void setEndTiming(long l) { end_time = l; } 

}
