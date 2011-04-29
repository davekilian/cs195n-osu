package dkilian.andy;

/**
 * A transition criterion that always evaluates to true.
 * Used to always one state after another.
 * 
 * @author dkilian
 */
public class VoidTransitionCriterion implements TransitionCriterion
{
	/** The priority of this transition criterion */
	private int _priority;
	
	/** Creates a new void transition */
	public VoidTransitionCriterion()
	{
		setPriority(0);
	}

	/** Creates a new void transition with the specified priority */
	public VoidTransitionCriterion(int priority)
	{
		setPriority(priority);
	}

	/** 
	 * Gets the priority of this transition, where lower values denote higher priority. 
	 * Higher priority transitions are evaluated by the finite state machine first when
	 * deciding which state to switch into. The first criterion met is the one used by
	 * the engine to switch into another state.
	 */
	@Override
	public int getPriority() 
	{
		return _priority;
	}

	/** 
	 * Sets the priority of this transition, where lower values denote higher priority. 
	 * Higher priority transitions are evaluated by the finite state machine first when
	 * deciding which state to switch into. The first criterion met is the one used by
	 * the engine to switch into another state.
	 */
	@Override
	public void setPriority(int p) 
	{
		_priority = p;
	}

	/**
	 * Evaluates this criterion
	 * @param state The current state of the state machine
	 * @param o The value produced by the current state by calling step(). Do *not* call step in this method!
	 * @return True if this criterion is met (always true for VoidTransitionCriterion)
	 */
	@Override
	public boolean isMet(FiniteState state, Object o) 
	{
		return true;
	}
}
