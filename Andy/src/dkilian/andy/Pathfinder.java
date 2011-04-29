package dkilian.andy;

import java.util.ArrayList;

/**
 * Interface for classes that are able to traverse a graph of PathfindingNodes
 * to find the optimal path from one to the other
 * 
 * @author dkilian
 */
public interface Pathfinder 
{
	/**
	 * Generates a path from the given starting node to the end node.
	 * @param start The node at which pathfinding should being
	 * @param end The node at which to terminate the pathfinding operation
	 * @param path Receives the path from the start node to the end node, inclusive. If no path
	 *             can be found from the start node to the end node, this will instead contain
	 *             only the starting node
	 * @return A boolean value indicating whether or not a path was found
	 */
	public boolean findPath(PathfindingNode start, PathfindingNode end, ArrayList<PathfindingNode> path);
}
