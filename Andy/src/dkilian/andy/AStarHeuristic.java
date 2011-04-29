package dkilian.andy;

/**
 * Interface for a user-defined heuristic used to estimate the cost of traversing 
 * from a certain node to the end node, to be used in an A* pathfinding oepration. 
 * 
 * A* heuristics should be fast (e.g. constant time) and should use some knowledge
 * about your subclass of PathfindingNode to estimate the path cost. To ensure
 * optimality in the A* search, this heuristic should be admissible - that is,
 * the estimate produced should always be less than or equal to the actual cost
 * of traversing between the two nodes.
 * 
 * Heuristics that overestimate the cost of paths can cause A* to pick non-optimal 
 * paths. Heuristics that underestimate the cost will still allow A* to pick the
 * optimal path, but more nodes will need to be searched.
 * 
 * @author dkilian
 */
public interface AStarHeuristic 
{
	/**
	 * Produces an estimate for the total cost from the current node to the end node
	 * @param current The current node, at which to start the estimated path
	 * @param end The destination node, at which to end the estimated path
	 * @return An estimate for the total cost from [current] to [end]
	 */
	public int estimate(PathfindingNode current, PathfindingNode end);
}
