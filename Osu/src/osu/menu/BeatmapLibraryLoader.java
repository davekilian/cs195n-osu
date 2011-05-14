package osu.menu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import osu.beatmap.Metadata;
import osu.parser.ParseException;
import osu.parser.Parser;

import android.util.Log;

/**
 * Given a path to the beatmap library directory, loads all BeatmapDirs in that path,
 * and their subsequent BeatampDiscriptors.
 */
public class BeatmapLibraryLoader {
	
	/**
	 * Returns an ArrayList of BeatmapDir to represent the osu! beatmap library at the given path.
	 * 
	 * @param lib_path The path to the dir where dirs storing beatmap files are located.
	 * @return An ArrayList of all Beatmap Files found.
	 * @throws IOException If the specified lib_path is not a directory.
	 */
	public static ArrayList<BeatmapDir> getBeatmapDirs(String lib_path) throws IOException
	{
		Parser parser = new Parser();
		ArrayList<BeatmapDir> lib = new ArrayList<BeatmapDir>();
		
		// Open library dir
		File lib_dir = new File(lib_path);
		if (!lib_dir.isDirectory())
			throw new IOException("Specified lib_path is not a directory.");
		
		// Iterate through beatmap dirs in lib
		for (File cur : lib_dir.listFiles())
		{
			if (!cur.isDirectory()) // Only get directories
				continue;
			
			try {
				// Access & add the internal beatmap
				lib.add(handleBeatmapDir(parser, cur));
			} catch (IOException ex) {
				Log.e("BeatmapLibraryLoader.getBeatmapDirs", "Error loading beatmap files: " + ex.toString());
			}
		}
		
		// Return the list of beatmaps
		return lib;
	}
	
	
	// *** HELPER METHODS *** //
	/**
	 * Searches into a single beatmap directory and creates/returns the BeatmapDir object to represent it.
	 */
	private static BeatmapDir handleBeatmapDir(Parser parser, File dir) throws IOException
	{
		BeatmapDir beatmap_dir = new BeatmapDir(dir.getAbsolutePath());
		
		// Iterate over all beatmaps (".osu") in the directory
		for (File beatmap : dir.listFiles(new BeatmapFilenameFilter()))
		{
			if (!beatmap.isFile() && !beatmap.canRead()) // Only get readable files
				continue;
			
			try {
				// Get metadata for files, store them
				Metadata metadata = parser.parseMetadataResource(beatmap.getAbsolutePath());
				BeatmapDescriptor des = new BeatmapDescriptor(beatmap.getAbsolutePath());
				
				des.setMetadata(metadata);
				
				beatmap_dir.addBeatmapDescriptor(des);
			} catch (ParseException ex) {
				Log.e("BeatmapLibraryLoader.handleBeatmapDir", "Error parsing beatmap metadata for " + beatmap.getAbsolutePath() + ": " + ex.toString());
			}
		}
		
		if (!beatmap_dir.exists()) // No beatmap files found in the dir
			throw new IOException("Beatmap Dir, \"" + dir.getAbsolutePath() + "\", contains no beatmap files.");
		
		return beatmap_dir;
	}
	
}
