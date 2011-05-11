package osu.beatmap;

/**
 * Metadata struct used to store all the silly Metadata information for a beatmap.
 */
public class Metadata {

	// NOTE: Fields intentionally left public for struct-like interaction.
	public String title;
	public String artist;
	public String creator;
	public String version; // Difficulty version (ex: Standard)
	public String source; // Original music/beatmap source
	public String tags; // Linking tags
	
	
	// *** CONSTRUCTORS *** //
	public Metadata()
	{
		title = null;
		artist = null;
		creator = null;
		version = null;
		source = null;
		tags = null;
	}
	
}
