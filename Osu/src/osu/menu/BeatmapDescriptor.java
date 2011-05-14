package osu.menu;

import osu.beatmap.Metadata;

/**
 * Used to describe a single beatmap file on disk (only one for one difficulty).
 * Contains the file's metadata and the path to the file.
 */
public class BeatmapDescriptor {

	private Metadata metadata;
	private String path; // Absolute path
	
	
	// *** CONSTRUCTORS *** //
	public BeatmapDescriptor(String file_path)
	{
		metadata = new Metadata();
		
		path = file_path;
	}
	
	
	// *** ACCESSORS *** //
	public void setMetadata(Metadata meta) { metadata = meta; }
	
	
	public Metadata getMetadata() { return metadata; }
	/** Returns the absolute path to the file */
	public String getPath() { return path; }
	
}
