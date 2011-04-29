package dkilian.andy;

import java.util.LinkedList;

/**
 * Interface for a class that can predict the state of the world given a past keyframe and
 * a set of differences that have occurred since then
 * @author dkilian
 * @param <StateType> The type of state this predictor operators on
 */
public interface Predictor<StateType extends PredictionState, DiffType extends PredictionDiff>
{
	/**
	 * Predicts the state of the world at a given point in time after a point in time at which
	 * the state of the world is known.
	 * @param time The time to simulate to
	 * @param dest Receives the predict state
	 * @param src The most recent known world state
	 * @param diffs Things that may affect the prediction algorithm that have occurred since the 
	 *              time the keyframe was created, ordered by ascending timestamp
	 */
	public void predictTo(float time, StateType dest, StateType src, LinkedList<? extends DiffType> diffs);
}
