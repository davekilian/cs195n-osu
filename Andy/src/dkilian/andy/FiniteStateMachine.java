package dkilian.andy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Manages a finite machine of FiniteStates
 * 
 * @author dkilian
 */
public class FiniteStateMachine 
{
	/** The current state of the machine */
	private FiniteState _current;
	/** The list of all known states */
	private ArrayList<FiniteState> _states;
	/** Maps states to lists of outgoing edges (with their respective criteria), sorted by descending priority of the criteria */
	private HashMap<FiniteState, LinkedList<TransitionCriterion>> _outgoingEdges;
	/** Maps each outgoing edge (with its respective criterion) to the state for which that edge is incoming */
	private HashMap<TransitionCriterion, FiniteState> _incomingEdges;
	
	/** Creates a new FSM */
	public FiniteStateMachine()
	{
		_states = new ArrayList<FiniteState>();
		_outgoingEdges = new HashMap<FiniteState, LinkedList<TransitionCriterion>>();
		_incomingEdges = new HashMap<TransitionCriterion, FiniteState>();
	}
	
	/**
	 * Adds a new state to the machine. The state will initially be an accepting state (no outgoing edges) 
	 * that cannot be reached (no incoming edges) 
	 */
	public void addState(FiniteState state)
	{
		if (!_states.contains(state))
			_states.add(state);
	}
	
	/**
	 * Removes a state from this machine, along with all edges to and from this state 
	 */
	public void removeState(FiniteState state)
	{
		_states.remove(state);

		// Remove (both ends of) all edges {originating at | leaving} the state to remove
		if (_outgoingEdges.containsKey(state))
		{
			for (ListIterator<TransitionCriterion> it = _outgoingEdges.get(state).listIterator(); it.hasNext(); )
				_incomingEdges.remove(it.next());
			_outgoingEdges.remove(state);
		}
		
		// Remove (both ends of) all edges {ending at | entering} the state to remove
		ArrayList<TransitionCriterion> tmp = new ArrayList<TransitionCriterion>();
		for (TransitionCriterion tc : _incomingEdges.keySet())
			if (_incomingEdges.get(tc) == state)
				tmp.add(tc);
		for (int i = 0; i < tmp.size(); ++i)
		{
			_incomingEdges.remove(tmp.get(i));
			for (FiniteState fs : _outgoingEdges.keySet())
				_outgoingEdges.get(fs).remove(tmp.get(i));
		}
	}
	
	/**
	 * Adds an edge in this machine, outging from the source state into the dest state, that is followed when the given criterion is met.
     * This call will silently fail if one of source or dest isn't a state in the machine, or if the given criterion is already used by an
     * edge in the machine
	 * @param source The 'start' node of the edge, for which the edge is outgoing
	 * @param dest The 'end' node of the edge, for which the edge is incoming
	 * @param criterion The criterion used to determine when this edge is followed
	 */
	public void addTransition(FiniteState source, FiniteState dest, TransitionCriterion criterion)
	{
		if (_states.contains(source) && _states.contains(dest) && !_incomingEdges.containsKey(criterion))
		{
			if (!_outgoingEdges.containsKey(source))
				_outgoingEdges.put(source, new LinkedList<TransitionCriterion>());
			
			LinkedList<TransitionCriterion> outgoing = _outgoingEdges.get(source);
			int index = 0;
			for (ListIterator<TransitionCriterion> it = outgoing.listIterator(); it.hasNext(); ++index)
				if (criterion.getPriority() < it.next().getPriority())
					break;
			outgoing.add(index, criterion);
			
			_incomingEdges.put(criterion, dest);
		}
	}
	
	/** 
	 * Removes the edge outgoing from source into dest if one exists 
	 */
	public void removeTransition(FiniteState source, FiniteState dest)
	{
		TransitionCriterion target = null;
		for (TransitionCriterion tc : _incomingEdges.keySet())
		{
			if (_incomingEdges.get(tc) == dest)
			{
				target = tc;
				break;
			}
		}
		
		if (target != null)
		{
			_incomingEdges.remove(target);
			_outgoingEdges.get(source).remove(target);
		}
	}
	
	/** Sets the current state of this FSM */
	public void begin(FiniteState state)
	{
		setCurrentState(state);
	}
	
	/**
	 * Performs one step of the machine, consisting of a step() call to the current FiniteState and a transition, 
	 * either to the same state or to another state 
	 */
	public void step()
	{
		if (_current != null)
		{
			Object arg = _current.step(this);
			for (ListIterator<TransitionCriterion> it = _outgoingEdges.get(_current).listIterator(); it.hasNext();)
			{
				TransitionCriterion t = it.next();
				if (t.isMet(_current, arg))
				{
					_current = _incomingEdges.get(t);
					break;
				}
			}
		}
	}
	
	/** Gets the current state of this FSM */
	public FiniteState getCurrentState()
	{
		return _current;
	}

	/** Sets the current state of this FSM */
	public void setCurrentState(FiniteState state)
	{
		_current = state;
	}
}
