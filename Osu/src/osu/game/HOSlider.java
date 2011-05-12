package osu.game;

import java.util.LinkedList;

import android.graphics.Point;

/**
 * HOSlider is one of the HitObject representations.
 * This object involves following along the object in time
 * (with a visible "ball" displayed underneath), to the end point.
 * Points and HP reductions should be enacted accordingly.<br><br>
 * 
 * NOTE: Sound should be ignored from HitObject - only sound_points should be used.
 * If it is empty, then all points are the generic sound effect.
 */
public class HOSlider extends HitObject {
	
	private HOSliderType slider_type;
	
	// Bezier variables
	private LinkedList<Point> path_points; // The points that make up the given path - used for bezier curves
	private LinkedList<Integer> sound_points; // The sound each of the points makes when hit them
	
	// General Variables (?)
	private int repeats; // Number of times this slider path should be followed back and forth
	private float path_length; // The entire path's length (divided in some weird measurement against BPM)
	
	
	// *** CONSTRUCTOR *** //
	public HOSlider(int x_pos, int y_pos, long time_millis, boolean is_new_combo, int sound)
	{
		x = x_pos;
		y = y_pos;
		
		timing = time_millis;
		
		new_combo = is_new_combo;
		
		sound_type = sound;
		
		path_points = new LinkedList<Point>();
		sound_points = new LinkedList<Integer>();
	}

	
	// *** ACCESSORS *** //
	public HOSliderType getSliderType() { return slider_type; }
	
	public LinkedList<Point> getPathPoints() { return path_points; }
	public LinkedList<Integer> getSoundPoints() { return sound_points; }
	
	public int getRepeats() { return repeats; }
	public float getPathLength() { return path_length; }
	
	
	/**
	 * Sets the slider type of the current HOSlider object whil doing error checking
	 * for slider types which are not currently supported by this version of the game.
	 */
	public void setSliderType(HOSliderType type) throws UnsupportedOperationException
	{
		switch (type)
		{
		case BEZIER:
			slider_type = type;
			break;
			
		// Currently unsupported
		case LINEAR:
		case CATMULL:
			throw new UnsupportedOperationException(type.toString() + " is not supported by this version of osu!");
			
		default:
			throw new UnsupportedOperationException(type.toString() + " is unrecognized by osu! in general.");
		}
	}
	
	public void setPathPoints(LinkedList<Point> points) { path_points = points; }
	public void setSoundPoints(LinkedList<Integer> points) { sound_points = points; }
	
	public void setRepeats(int reps) { repeats = reps; }
	public void setPathLength(float len) { path_length = len; }
	
}
