package osu.beatmap;

import java.util.LinkedList;
import java.util.Vector;

import osu.game.Background;
import osu.game.BreakTiming;
import osu.game.ComboColor;
import osu.game.TimingPoint;

/**
 * A class to represent a single beatmap object within the game.
 * This includes the controls, colours, etc.
 */
public class Beatmap {
	
	private Editor editor; // Struct
	
	private Metadata metadata; // Struct
	
	private Background background; // Class with many background attributes
	private LinkedList<BreakTiming> break_timings; // List of breaks in the song (no controls or HP drain)
	
	private LinkedList<TimingPoint> timing_points; // Changes in tempo, BPM, and the like
	
	private Vector<ComboColor> combo_colors; // Control colors
	
	
	// *** CONSTRUCTORS *** //
	public Beatmap()
	{
		// TODO: Set shit to null and stuff
		
		// General
		
		// Editor
		editor = null;
		
		// Metadata
		metadata = null;
		
		// Difficulty
		
		// Events
		background = null;
		break_timings = null;
		
		// Timing Points
		timing_points = null;
		
		// Colours
		combo_colors = null;
		
		// Hit Objects
	}
	
	
	// *** ACCESSORS - SET *** //
	public void setEditor(Editor e) { editor = e; }
	
	public void setMetadata(Metadata m) { metadata = m; }
	
	public void setBackground(Background back) { background = back; }
	public void setBreakTimings(LinkedList<BreakTiming> timings) { break_timings = timings; }
	
	public void setTimingPoints(LinkedList<TimingPoint> points) { timing_points = points; }
	
	public void setComboColors(Vector<ComboColor> colors) { combo_colors = colors; }
	
	
	// *** ACCESSORS - GET *** //
	public Editor getEditor() { return editor; }
	
	public Metadata getMetadata() { return metadata; }
	
	public Background getBackground() { return background; }
	public LinkedList<BreakTiming> getBreakTimings() { return break_timings; }
	
	public LinkedList<TimingPoint> getTimingPoint() { return timing_points; }
	
	public Vector<ComboColor> getComboColors() { return combo_colors; }
	
}
