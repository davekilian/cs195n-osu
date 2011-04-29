package dkilian.andy;

/**
 * Represents an outgoing edge from a PathfindingNode
 * 
 * @author dkilian
 */
public class PathfindingNodeEdge 
{
	/** The neighbor at which this edge ends */
	private PathfindingNode _neighbor;
	/** The cost of traversing this edge, from the incoming node to the outgoing node (_neighbor) */
	private int _cost;
	
	/** Creates a new edge */
	public PathfindingNodeEdge()
	{
		_neighbor = null;
		_cost = Integer.MAX_VALUE;
	}
	
	/** Creates a new edge */
	public PathfindingNodeEdge(PathfindingNode neighbor)
	{
		_neighbor = neighbor;
		_cost = 1;
	}
	
	/** Creates a new edge */
	public PathfindingNodeEdge(PathfindingNode neighbor, int cost)
	{
		_neighbor = neighbor;
		_cost = cost;
	}
	
	/** Gets the neighbor at the end of this edge */
	public PathfindingNode getNeighbor()
	{
		return _neighbor;
	}
	
	/** Sets the neighbor at the end of this edge */
	public void setNeighbor(PathfindingNode neighbor)
	{
		_neighbor = neighbor;
	}
	
	/** 
	 * Gets the cost of traversing this edge from the incoming node (the PathfindingNode 
	 * containing this PathfindingNodeEdge) to the outgoing node (see getNeighbor())
	 */
	public int getCost()
	{
		return _cost;
	}

	/** 
	 * Sets the cost of traversing this edge from the incoming node (the PathfindingNode 
	 * containing this PathfindingNodeEdge) to the outgoing node (see getNeighbor())
	 */
	public void setCost(int cost)
	{
		_cost = cost;
	}
}
