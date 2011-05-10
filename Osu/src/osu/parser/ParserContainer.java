package osu.parser;

import java.util.LinkedList;
import java.util.Vector;

import osu.game.*;

/**
 * Created to pass in the various game components to the Parser with ease.
 * Constructed once and essentially a struct with use for the game to take components as necessary.
 */
public class ParserContainer {

	// Intentionally set public - to be used as a struct-like object
	public Vector<ComboColor> combo_colors;
	public LinkedList<TimingPoint> timing_points;
	
	public LinkedList<HitObject> hit_objects; 
	
	
	// *** CONSTRUCTOR *** //
	public ParserContainer()
	{
		combo_colors = new Vector<ComboColor>();
		timing_points = new LinkedList<TimingPoint>();
		
		hit_objects = new LinkedList<HitObject>();
	}
	
}
