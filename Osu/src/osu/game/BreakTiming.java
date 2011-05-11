package osu.game;

/**
 * Data structure to represent a break timing in Osu.
 * These are usually represented by a marker to say whether or
 * not you did well on the previous section (and stop the HP bar
 * from draining).
 */
public class BreakTiming {

	private int unknown; // Some number that I have no idea what it does
	
	private long start, end; // Start & end time in millis
	
	
	// *** CONSTRUCTOR *** //
	public BreakTiming(int un, long start_millis, long end_millis)
	{
		unknown = un;
		
		start = start_millis;
		end = end_millis;
	}
	
	
	// *** ACCESSORS *** //
	public void setUnknown(int i) { unknown = i; }
	
	public void setStartTime(long l) { start = l; }
	public void setEndTime(long l) { end = l; }
	
	
	public int getUnknown() { return unknown; }
	
	public long getStartTime() { return start; }
	public long getEndTime() { return end; }
	
}
