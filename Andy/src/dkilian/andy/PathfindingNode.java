package dkilian.andy;

import java.util.ArrayList;

/**
 * A node of a graph in which a Pathfinder may find an optimal path between two nodes
 * @author dkilian
 */
public class PathfindingNode 
{
	/** A list containing all of the nodes neighboring this one */
	protected ArrayList<PathfindingNodeEdge> _neighbors;
	
	/** Creates a new node */
	public PathfindingNode()
	{
		_neighbors = new ArrayList<PathfindingNodeEdge>();
	}
	
	/** Gets the list of the nodes that can be traversed to from this node */
	public ArrayList<PathfindingNodeEdge> getNeighbors()
	{
		return _neighbors;
	}
	
	/** Sets the list of the nodes that can be traversed to from this node */
	public void setNeighbors(ArrayList<PathfindingNodeEdge> neighbors)
	{
		_neighbors = neighbors;
	}
}
