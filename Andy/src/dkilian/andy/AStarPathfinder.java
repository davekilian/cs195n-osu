package dkilian.andy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Uses the A* search algorithm to quickly the compute the optimal path between two
 * PathfindingNodes, provided a good estimating heuristic is provided (see the 
 * documentation for AStarHeuristic)
 * 
 * @author dkilian
 */
public class AStarPathfinder implements Pathfinder
{	
	/** The A* heuristic currently in use */
	public AStarHeuristic _estimator;
	/** Used to order items in the priority queue */
	private AStarHeuristicComparator _comparator;
	/** Used in findPath(). Preallocated once and used multiple times */
	private HashSet<PathfindingNode> _visited;
	/** Used in findPath(). Preallocated once and used multiple times */
	private PriorityQueue<PathfindingNode> _toVisit;
	/** Used in findPath(). Preallocated once and used multiple times */
	private HashMap<PathfindingNode, PathfindingNode> _parents;
	/** Used in findPath(). Preallocated once and used multiple times */
	private HashMap<PathfindingNode, Integer> _costsFromStart;
	/** Used in findPath(). Preallocated once and used multiple times */
	private HashMap<PathfindingNode, Integer> _totalCosts;
	/** Used in findpath(). Preallocated once and used multiple times */
	private Stack<PathfindingNode> _reversePath;
	
	/**
	 * Compares nodes by their cost, using a fixed heuristic and destination node.
	 * Used to order items in the A* pathfinder's priority queue
	 * 
	 * @author dkilian
	 */
	private class AStarHeuristicComparator implements Comparator<PathfindingNode>
	{
		/** Compares two pathfinding nodes */
		@Override
		public int compare(PathfindingNode object1, PathfindingNode object2) 
		{
			return _totalCosts.get(object1) - _totalCosts.get(object2);
		}
	}
	
	/** Creates a new A* pathfinder */
	public AStarPathfinder()
	{
		super();
		
		_comparator = new AStarHeuristicComparator();
		_visited = new HashSet<PathfindingNode>();
		_toVisit = new PriorityQueue<PathfindingNode>(1, _comparator);
		_parents = new HashMap<PathfindingNode, PathfindingNode>();
		_costsFromStart = new HashMap<PathfindingNode, Integer>();
		_totalCosts = new HashMap<PathfindingNode, Integer>();
		_reversePath = new Stack<PathfindingNode>();
	}
	
	/** Gets the heuristic used to estimate the cost between two nodes */
	public AStarHeuristic getHeuristic()
	{
		return _estimator;
	}
	
	/** Sets the heuristic used to estimate the cost between two nodes */
	public void setAStarHeuristic(AStarHeuristic heuristic)
	{
		_estimator = heuristic;
	}

	/**
	 * Generates a path from the given starting node to the end node.
	 * @param start The node at which pathfinding should being
	 * @param end The node at which to terminate the pathfinding operation
	 * @param path Receives the path from the start node to the end node, inclusive. If no path
	 *             can be found from the start node to the end node, this will instead contain
	 *             only the starting node
	 * @return A boolean value indicating whether or not a path was found
	 */
	@Override
	public boolean findPath(PathfindingNode start, PathfindingNode end, ArrayList<PathfindingNode> path) 
	{
		path.clear();
		
		_visited.clear();
		_toVisit.clear();
		_parents.clear();
		_costsFromStart.clear();
		_totalCosts.clear();

		_costsFromStart.put(start, 0);
		_toVisit.add(start);
		_parents.put(start, null);
		
		while (!_toVisit.isEmpty() && !_toVisit.contains(end))
		{
			PathfindingNode m = _toVisit.remove();
			
			int mCost = _costsFromStart.get(m);
			
			for (int i = 0; i < m.getNeighbors().size(); ++i)
			{
				PathfindingNodeEdge n = m.getNeighbors().get(i);
				if (n.getNeighbor() != null && !_visited.contains(n.getNeighbor()))
				{
					int costFromSource = mCost + n.getCost();
					int totalCost = costFromSource + _estimator.estimate(n.getNeighbor(), end);
					if (!_toVisit.contains(n.getNeighbor()) || totalCost < _totalCosts.get(n.getNeighbor()))
					{
						_parents.put(n.getNeighbor(), m);
						_costsFromStart.put(n.getNeighbor(), costFromSource);
						_totalCosts.put(n.getNeighbor(), totalCost);
						if (_toVisit.contains(n.getNeighbor()))
							_toVisit.remove(n.getNeighbor());
						_toVisit.add(n.getNeighbor());
					}
				}
			}
			
			_visited.add(m);
		}
		
		if (_toVisit.contains(end))
		{
			_reversePath.clear();
			
			PathfindingNode current = end;
			while (current != null)
			{
				_reversePath.push(current);
				current = _parents.get(current);
			}
			
			while (!_reversePath.isEmpty())
				path.add(_reversePath.pop());
			
			return true;
		}
		else
		{
			path.add(start);
			return false;
		}
	}
}
