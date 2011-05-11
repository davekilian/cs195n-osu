package osu.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import osu.game.*;

/**
 * Created to pass in the various game components to the Parser with ease.
 * Constructed once and essentially a struct with use for the game to take components as necessary.
 */
public class ParserContainer {

	// NOTE: Intentionally left public - to be used as a struct-like object //
	// General/Editor/Metadata/Difficulty
	public HashMap<Subsections, HashMap<String, String>> dict;
	
	// Events
	public Background background;
	public LinkedList<BreakTiming> break_timings;
	
	// Timing Points
	public LinkedList<TimingPoint> timing_points;
	
	// Colours
	public Vector<ComboColor> combo_colors;
	
	// Hit Objects	
	public LinkedList<HitObject> hit_objects;
	
	
	// *** CONSTRUCTOR *** //
	public ParserContainer()
	{
		// General/Editor/Metadata/Difficulty
		dict = null;
		
		// Events
		background = new Background();
		break_timings = new LinkedList<BreakTiming>();
		
		// Timing Points
		timing_points = new LinkedList<TimingPoint>();
		
		// Colours
		combo_colors = new Vector<ComboColor>();

		// Hit Objects
		hit_objects = new LinkedList<HitObject>();
	}
	
}
