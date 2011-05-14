package osu.menu;

import java.util.ArrayList;

import osu.beatmap.Metadata;

/**
 * Holds several BeatmapDescriptors to represent a single song on the disk.
 * File paths to the beatmaps are found in the beatmaps themselves while the path
 * to the whole directory is found in this Object itself.<br><br>
 * 
 * NOTE: The metadata information is all relevant except for the version which is the beatmap difficulty<br><br>
 * 
 * NOTE: Assumes non-malformed BeatmapDescriptors
 */
public class BeatmapDir {

	private boolean exists; // Tells whether or not any beatmap files exist in this dir
	
	private Metadata metadata; // Metadata taken from the first file found - all information useful EXCEPT version (that's difficulty)
	private ArrayList<BeatmapDescriptor> beatmaps;
	private String path; // Absolute path
	
	
	// *** CONSTRUCTORS *** //
	public BeatmapDir(String dir_path)
	{
		exists = false;
		metadata = null;
		
		beatmaps = new ArrayList<BeatmapDescriptor>();
		dir_path = path;
	}
	
	
	// *** ACCESSORS *** //
	public void addBeatmapDescriptor(BeatmapDescriptor beatmap_des)
	{
		if (metadata == null) // Set metadata if it does not exist
		{
			exists = true;
			metadata = beatmap_des.getMetadata();
		}
		
		beatmaps.add(beatmap_des);
	}
	
	
	/** Says whether or not any beatmap files exist in this dir */
	public boolean exists() { return exists; }
	
	public Metadata getMetadata() { return metadata; }
	public ArrayList<BeatmapDescriptor> getBeatmapDescriptors() { return beatmaps; }
	/** Returns the absolute path to the file */
	public String getPath() { return path; }
	
}
