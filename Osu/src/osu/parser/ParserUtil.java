package osu.parser;

import java.util.HashMap;

import osu.beatmap.*;

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
	public static Beatmap parserContainerToBeatmap(ParserContainer pc) throws ParseException
	{
		try {
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
		} catch (Exception ex) {
			throw new ParseException("The conversion to Beatmap could not be completed: " + ex.toString());
		}
	}
	
	
	private static void setGeneral(ParserContainer pc, Beatmap beatmap) throws NumberFormatException, NullPointerException
	{
		HashMap<String, String> map = pc.dict.get(Subsections.GENERAL);
		
		beatmap.setAudioFilename(map.get("audiofilename"));
		beatmap.setAudioLeadIn(Long.parseLong(map.get("audioleadin")));
		beatmap.setPreviewTime(Long.parseLong(map.get("previewtime")));
		beatmap.setCountdownStyle(Integer.parseInt(map.get("countdown")));
		beatmap.setSampleSet(map.get("sampleset"));
		beatmap.setStackLeniency(Float.parseFloat(map.get("stackleniency")));
		beatmap.setPlayMode(Integer.parseInt(map.get("mode")));
		beatmap.setLetterboxInBreaks(Integer.parseInt(map.get("letterboxinbreaks")) == 1 ? true : false);
		
		// The following attributes don't necessarily show up everytime
		String temp;
		
		beatmap.setSkinPreference(map.get("skinpreference"));
		
		temp = map.get("countdownoffset");
		if (temp != null)
			beatmap.setCountdownOffset(Integer.parseInt(temp));
		else
			beatmap.setCountdownOffset(0);
		
		temp = map.get("storyfireinfront");
		if (temp != null)
			beatmap.setStoryFireInFront(Integer.parseInt(temp) == 1 ? true : false);
		else
			beatmap.setStoryFireInFront(false);
		
		temp = map.get("epilepsywarning");
		if (temp != null)
			beatmap.setEpilepsyWarning(Integer.parseInt(temp) == 1 ? true : false);
		else
			beatmap.setEpilepsyWarning(false);
	}
	
	private static void setEditor(ParserContainer pc, Beatmap beatmap) throws NumberFormatException, NullPointerException
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
	
	private static void setDifficulty(ParserContainer pc, Beatmap beatmap) throws NumberFormatException, NullPointerException
	{
		HashMap<String, String> map = pc.dict.get(Subsections.DIFFICULTY);
		
		beatmap.setHPDrainRate(Integer.parseInt(map.get("hpdrainrate")));
		beatmap.setCircleSize(Integer.parseInt(map.get("circlesize")));
		beatmap.setOverallDifficulty(Integer.parseInt(map.get("overalldifficulty")));
		beatmap.setApproachRate(Integer.parseInt(map.get("approachrate")));
		beatmap.setSliderMultiplier(Float.parseFloat(map.get("slidermultiplier")));
		beatmap.setSliderTickRate(Float.parseFloat(map.get("slidertickrate")));
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
