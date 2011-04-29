package dkilian.andy;

/**
 * A transition which is met when the argument returned by FiniteState.step()
 * is equal (using .equals()) to a static value
 * 
 * @author dkilian
 */
public class EqualityTransitionCriterion implements TransitionCriterion
{
	/** The priority of this transition criterion */
	private int _priority;
	/** The object which must .equals() the object returned by a state for this criterion to be met */
	private Object _argument;
	
	/** Creates a new equality transition criterion */
	public EqualityTransitionCriterion(Object arg)
	{
		setPriority(0);
		setArgument(arg);
	}

	/** Creates a new equality transition criterion */
	public EqualityTransitionCriterion(Object arg, int priority)
	{
		setPriority(priority);
		setArgument(arg);
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

	/** Gets the object which must .equals() the object returned by a state for this criterion to be met */
	public Object getArgument()
	{
		return _argument;
	}

	/** Sets the object which must .equals() the object returned by a state for this criterion to be met */
	public void setArgument(Object argument)
	{
		_argument = argument;
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
		return o != null && _argument != null && o.equals(_argument);
	}
}
