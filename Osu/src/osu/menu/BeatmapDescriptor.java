package osu.menu;

import osu.beatmap.Metadata;

/**
 * Used to describe a single beatmap file on disk (only one for one difficulty).
 * Contains the file's metadata and the path to the file.
 */
public class BeatmapDescriptor implements Comparable<BeatmapDescriptor> {

	private Metadata metadata;
	private String path; // Absolute path
	
	private int overall_difficulty;
	
	
	// *** CONSTRUCTORS *** //
	public BeatmapDescriptor(String file_path)
	{
		metadata = new Metadata();
		
		path = file_path;
		
		overall_difficulty = -1;
	}
	
	
	// *** IMPORTANT METHODS *** //
	/** Returns comparison of BeatmapDescriptors by overall difficulty */
	@Override
	public int compareTo(BeatmapDescriptor another)
	{
		if (this.overall_difficulty < another.overall_difficulty)
			return -1;
		else if (this.overall_difficulty > another.overall_difficulty)
			return 1;
		return 0;
	}
	
	
	// *** ACCESSORS *** //
	public void setMetadata(Metadata meta) { metadata = meta; }
	public void setOverallDifficulty(int i) { overall_difficulty = i; }
	
	
	public Metadata getMetadata() { return metadata; }
	/** Returns the absolute path to the file */
	public String getPath() { return path; }
	public int getOverallDifficulty() { return overall_difficulty; }
	
}
