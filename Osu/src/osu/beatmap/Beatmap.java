package osu.beatmap;

import java.util.LinkedList;
import java.util.Vector;

import osu.game.Background;
import osu.game.BreakTiming;
import osu.game.ComboColor;
import osu.game.TimingPoint;

/**
 * A class to represent a single beatmap object within the game.
 * This includes the controls, colours, etc.
 */
public class Beatmap {
	
	private String audio_filename;
	private long audio_lead_in;
	private long preview_time; // ?
	private int countdown_style;
	private String sample_set; // The type of sounds used on hits. To enum?
	private float stack_leniency;
	private int play_mode; // Style of game played - should be 0
	private boolean letterbox_in_breaks; // Display a letterbox in break timings
	private String skin_preference;
	private int countdown_offset; // In number of beats (don't know direction - think it's early)
	private boolean story_fire_in_front; // ?
	private boolean epilepsy_warning;
	
	private Editor editor; // Struct
	
	private Metadata metadata; // Struct
	
	private int hp_drain_rate;
	private int circle_size;
	private int overall_difficulty; // Timing windows, harshness of spinners
	private int approach_rate; // How quickly the hitboxes appear on screen
	private float slider_multiplier; // Changes rate of speed of sliders
	private int slider_tick_rate; // Divides the sliders into this many point components
	
	private Background background; // Class with many background attributes
	private LinkedList<BreakTiming> break_timings; // List of breaks in the song (no controls or HP drain)
	
	private LinkedList<TimingPoint> timing_points; // Changes in tempo, BPM, and the like
	
	private Vector<ComboColor> combo_colors; // Control colors
	
	
	// *** CONSTRUCTORS *** //
	public Beatmap()
	{
		// General
		audio_filename = null;
		audio_lead_in = -1;
		preview_time = -1;
		countdown_style = -1;
		sample_set = null;
		stack_leniency = -1;
		play_mode = -1;
		letterbox_in_breaks = false;
		skin_preference = null;
		countdown_offset = -1;
		story_fire_in_front = false;
		epilepsy_warning = false;
		
		// Editor
		editor = null;
		
		// Metadata
		metadata = null;
		
		// Difficulty
		hp_drain_rate = -1;
		circle_size = -1;
		overall_difficulty = -1;
		approach_rate = -1;
		slider_multiplier = -1;
		slider_tick_rate = -1;
		
		// Events
		background = null;
		break_timings = null;
		
		// Timing Points
		timing_points = null;
		
		// Colours
		combo_colors = null;
		
		// TODO: Hit Objects
	}
	
	
	// *** ACCESSORS - SET *** // ('mutators' -Dave)
	public void setAudioFilename(String s) { audio_filename = s; }
	public void setAudioLeadIn(long l) { audio_lead_in = l; }
	public void setPreviewTime(long l) { preview_time = l; }
	public void setCountdownStyle(int i) { countdown_style = i; }
	public void setSampleSet(String s) { sample_set = s; }
	public void setStackLeniency(float f) { stack_leniency = f; }
	public void setPlayMode(int i) { play_mode = i; }
	public void setLetterboxInBreaks(boolean b) { letterbox_in_breaks = b; }
	public void setSkinPreference(String s) { skin_preference = s; }
	public void setCountdownOffset(int i) { countdown_offset = i; }
	public void setStoryFireInFront(boolean b) { story_fire_in_front = b; }
	public void setEpilepsyWarning(boolean b) { epilepsy_warning = b; }
	
	public void setEditor(Editor e) { editor = e; }
	
	public void setMetadata(Metadata m) { metadata = m; }
	
	public void setHPDrainRate(int i) { hp_drain_rate = i; }
	public void setCircleSize(int i) { circle_size = i; }
	public void setOverallDifficulty(int i) { overall_difficulty = i; }
	public void setApproachRate(int i) { approach_rate = i; }
	public void setSliderMultiplier(float f) { slider_multiplier = f; }
	public void setSliderTickRate(int i) { slider_tick_rate = i; }
	
	public void setBackground(Background back) { background = back; }
	public void setBreakTimings(LinkedList<BreakTiming> timings) { break_timings = timings; }
	
	public void setTimingPoints(LinkedList<TimingPoint> points) { timing_points = points; }
	
	public void setComboColors(Vector<ComboColor> colors) { combo_colors = colors; }
	
	
	// *** ACCESSORS - GET *** //
	public String getAudioFilename() { return audio_filename; }
	public long getAudioLeadIn() { return audio_lead_in; }
	public long getPreviewTime() { return preview_time; }
	public int getCountdownStyle() { return countdown_style; }
	public String getSampleSet() { return sample_set; }
	public float getStackLeniency() { return stack_leniency; }
	public int getPlayMode() { return play_mode; }
	public boolean getLetterboxInBreaks() { return letterbox_in_breaks; }
	public String getSkinPreference() { return skin_preference; }
	public int getCountdownOffset() { return countdown_offset; }
	public boolean getStoryFireInFront() { return story_fire_in_front; }
	public boolean getEpilepsyWarning() { return epilepsy_warning; }
	
	public Editor getEditor() { return editor; }
	
	public Metadata getMetadata() { return metadata; }
	
	public int getHPDrainRate() { return hp_drain_rate; }
	public int getCircleSize() { return circle_size; }
	public int getOverallDifficulty() { return overall_difficulty; }
	public int getApproachRate() { return approach_rate; }
	public float getSliderMultiplier() { return slider_multiplier; }
	public int getSliderTickRate() { return slider_tick_rate; }
	
	public Background getBackground() { return background; }
	public LinkedList<BreakTiming> getBreakTimings() { return break_timings; }
	
	public LinkedList<TimingPoint> getTimingPoint() { return timing_points; }
	
	public Vector<ComboColor> getComboColors() { return combo_colors; }
	
}
