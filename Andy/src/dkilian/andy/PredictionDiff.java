package dkilian.andy;

/**
 * Interface for a time change from the keyframe state that will affect prediction
 * 
 * @author dkilian
 */
public interface PredictionDiff 
{
	/** Gets the time this difference was applied, in partial seconds relative to the game clock */
	public float getTime();

	/** Sets the time this difference was applied, in partial seconds relative to the game clock */
	public void setTime(float time);
}
