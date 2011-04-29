package dkilian.andy;

/**
 * Interface for a criterion used by a finite state machine to decide
 * which state to step to next
 * 
 * @author dkilian
 */
public interface TransitionCriterion 
{
	/** 
	 * Gets the priority of this transition, where lower values denote higher priority. 
	 * Higher priority transitions are evaluated by the finite state machine first when
	 * deciding which state to switch into. The first criterion met is the one used by
	 * the engine to switch into another state.
	 */
	public int getPriority();

	/** 
	 * Sets the priority of this transition, where lower values denote higher priority. 
	 * Higher priority transitions are evaluated by the finite state machine first when
	 * deciding which state to switch into. The first criterion met is the one used by
	 * the engine to switch into another state.
	 */
	public void setPriority(int p);
	
	/**
	 * Evaluates this criterion
	 * @param state The current state of the state machine
	 * @param o The value produced by the current state by calling step(). Do *not* call step in this method!
	 * @return True if this criterion is met
	 */
	public boolean isMet(FiniteState state, Object o);
}
