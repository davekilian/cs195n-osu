package osu.game;

/**
 * Abstract representation of an ingame HitObject.
 * Contains all the information that is available to be parsed from a
 * given beatmap file.
 */
public abstract class HitObject {
	
	int x, y;
	long timing; // Timing in millis
	
	boolean new_combo; // Changes the combo colors, numbers, and points
	
	// Probably unneeded
	int sound_type; // Sound this object makes when hit
	
	
	// *** ACCESSORS *** //
	public int getX() { return x; }
	public int getY() { return y; }
	
	public long getTiming() { return timing; }
	
	public boolean getNewCombo() { return new_combo; }
	
	public int getSoundType() { return sound_type; }
	
	
	public void setX(int i) { x = i; }
	public void setY(int i) { y = i; }
	
	public void setTiming(long l) { timing = l; }
	
	public void setNewCombo(boolean b) { new_combo = b; }
	
	public void setSoundType(int i) { sound_type = i; }

}
