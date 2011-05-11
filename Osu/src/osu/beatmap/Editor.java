package osu.beatmap;

/**
 * Editor struct to store all the silly Editor information for a beatmap.
 */
public class Editor {
	
	// NOTE: Fields intentionally left public for struct-like interaction.
	public float distance_spacing;
	public int beat_divisor;
	public int grid_size;
	
	
	// *** CONSTRUCTORS *** //
	public Editor()
	{
		distance_spacing = -1;
		beat_divisor = -1;
		grid_size = -1;
	}

}
