package osu.game;

public class TimingPoint {

	private boolean inherit_previous_timings;
	
	private boolean kiai_mode;
	
	// Time-Based
	private long offset; // In millis - when this section begins
	private double bpm; // 60 bpm = 1000; 120 bpm = 500; I dunno!
	private int time_signature; // The top part of the time signature
	
	// Sound-Based (probably-unneeded)
	private int volume; // The volume percent (of 100) of the current part's sounds
	private int sample_set; // Which sound set to use (regular or soft)
	private int sample_set_custom; // Which custom set to use
	
	
	// *** CONSTRUCTOR *** //
	public TimingPoint() { } // Do Nothing! Use the getters and setters!
	
	
	// *** ACCESSORS *** //
	public boolean getInheritance() { return inherit_previous_timings; }
	public boolean getKiai() { return kiai_mode; }
	
	public long getOffset() { return offset; }
	public double getBPM() { return bpm; }
	public int getTimeSignature() { return time_signature; }
	
	public int getVolume() { return volume; }
	public int getSampleSet() { return sample_set; }
	public int getCustomSampleSet() { return sample_set_custom; }
	
	
	public void setInheritance(boolean b) { inherit_previous_timings = b; }
	public void setKiai(boolean b) { kiai_mode = b; }
	
	public void setOffset(long l) { offset = l; }
	public void setBPM(double d) { bpm = d; }
	public void setTimeSignature(int i) { time_signature = i; }
	
	public void setVolume(int i) { volume = i; }
	public void setSampleSet(int i) { sample_set = i; }
	public void setCustomSampleSet(int i) { sample_set_custom = i; }
	
}
