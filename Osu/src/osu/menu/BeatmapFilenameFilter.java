package osu.menu;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A FilenameFilter implementation that only accepts files with the suffix ".osu",
 * or Osu beatmap files.
 */
public class BeatmapFilenameFilter implements FilenameFilter {

	// *** CONSTRUCTOR *** //
	public BeatmapFilenameFilter() { }
	
	
	// *** IMPORTANT METHODS *** //
	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith(".osu"))
			return true;
		
		return false;
	}

}
