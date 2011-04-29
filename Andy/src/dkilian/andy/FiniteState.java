package dkilian.andy;

/**
 * Interface for a state in a finite state machine
 * 
 * @author dkilian
 */
public interface FiniteState 
{
	/**
	 * Performs duties as dictated by this state, and returns the argument passed to
	 * transition criteria to determine which state to move to
	 * @param machine The machine running this state
	 * @return The argument to pass to the transition criteria out of this state to
	 *         determine which state will be used next call to FiniteStateMachine.step().
	 */
	public Object step(FiniteStateMachine machine);
}
