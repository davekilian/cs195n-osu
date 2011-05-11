package osu.parser;

import java.util.HashMap;

import osu.beatmap.*;
import osu.game.Background;
import osu.game.ComboColor;

/**
 * A variety of static parsing related utilities.
 */
public class ParserUtil {

	
	// *** PARSER CONTAINER TO BEATMAP *** //
	/**
	 * Converts the ParserContainer and converts it into a usable Beatmap format.
	 * 
	 * @param pc The ParserContainer into which all the information was stored
	 * @return A Beatmap representing a game
	 */
	public static Beatmap parserContainerToBeatmap(ParserContainer pc)
	{
		Beatmap beatmap = new Beatmap();
		
		setGeneral(pc, beatmap);
		setEditor(pc, beatmap);
		setMetadata(pc, beatmap);
		setDifficulty(pc, beatmap);
		setEvents(pc, beatmap);
		setTimingPoints(pc, beatmap);
		setColours(pc, beatmap);
		setHitObjects(pc, beatmap);
		
		return beatmap;
	}
	
	
	private static void setGeneral(ParserContainer pc, Beatmap beatmap)
	{
		// TODO: FILL ME IN
	}
	
	private static void setEditor(ParserContainer pc, Beatmap beatmap)
	{
		HashMap<String, String> map = pc.dict.get(Subsections.EDITOR);
		
		Editor editor = new Editor();
		editor.distance_spacing = Float.parseFloat(map.get("distancespacing"));
		editor.beat_divisor = Integer.parseInt(map.get("beatdivisor"));
		editor.grid_size = Integer.parseInt(map.get("gridsize"));
		
		beatmap.setEditor(editor);
	}
	
	private static void setMetadata(ParserContainer pc, Beatmap beatmap)
	{
		HashMap<String, String> map = pc.dict.get(Subsections.METADATA);
		
		Metadata metadata = new Metadata();
		metadata.title = map.get("title");
		metadata.artist = map.get("artist");
		metadata.creator = map.get("creator");
		metadata.version = map.get("version");
		metadata.source = map.get("source");
		metadata.tags = map.get("tags");
		
		beatmap.setMetadata(metadata);
	}
	
	private static void setDifficulty(ParserContainer pc, Beatmap beatmap)
	{
		// TODO: FILL ME IN
	}
	
	private static void setEvents(ParserContainer pc, Beatmap beatmap)
	{
		beatmap.setBackground(pc.background);
		beatmap.setBreakTimings(pc.break_timings);
	}
	
	private static void setTimingPoints(ParserContainer pc, Beatmap beatmap) { beatmap.setTimingPoints(pc.timing_points); }
	
	private static void setColours(ParserContainer pc, Beatmap beatmap) { beatmap.setComboColors(pc.combo_colors); }
	
	private static void setHitObjects(ParserContainer pc, Beatmap beatmap)
	{
		// TODO: FILL ME IN
		// Convert HitObjects to controls (wrap dat shit!)
	}
	
}
