package dkilian.andy;

/**
 * Interface for a timed keyframe in a network prediction system
 * 
 * @author dkilian
 */
public interface PredictionState 
{
	/** Gets the time this snapshot of the world state was taken, in partial seconds relative to the game clock */
	public float getTime();

	/** Sets the time this snapshot of the world state was taken, in partial seconds relative to the game clock */
	public void setTime(float time);
}
