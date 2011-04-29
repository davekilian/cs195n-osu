package dkilian.andy;

import java.util.ArrayList;

/**
 * Keeps two non-rigid body points a fixed distance from each other
 * @author dkilian
 */
public class NonRigidBodyStickConstraint implements Constraint
{
	/** One of the objects to constrain */
	private NonRigidBodyPoint _a;
	/** One of the objects to constrain */
	private NonRigidBodyPoint _b;
	/** The fixed distance between the two objects */
	private float _dist;
	/** Used in apply(). Preallocated for performance */
	private Vector2 _aToB;
	
	/** Creates a new stick constraint */
	public NonRigidBodyStickConstraint()
	{
		_dist = 0;
		_aToB = new Vector2();
	}

	/** Creates a new stick constraint */
	public NonRigidBodyStickConstraint(float distance)
	{
		_dist = distance;
		_aToB = new Vector2();
	}

	/** Creates a new stick constraint */
	public NonRigidBodyStickConstraint(NonRigidBodyPoint a, NonRigidBodyPoint b, float distance)
	{
		_a = a;
		_b = b;
		_dist = distance;
		_aToB = new Vector2();
	}

	/** Applies this stick constraint */
	@Override
	public boolean apply(ArrayList<Entity> world) 
	{		
		_aToB.set(_b.getPosition());
		_aToB.subtract(_a.getPosition());
		float distance = _aToB.length();
		_aToB.multiply(.5f * (distance - _dist) / distance);
		
		_a.getPosition().add(_aToB);
		_b.getPosition().subtract(_aToB);
		
		return Math.abs(distance - _dist) < 1e-6;
	}
}
