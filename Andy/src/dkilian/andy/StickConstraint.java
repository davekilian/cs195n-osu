package dkilian.andy;

import java.util.ArrayList;

/**
 * Fixes two objects at a certain distance from each other
 * @author dkilian
 */
public class StickConstraint implements Constraint
{
	/** One of the objects to constrain */
	private Entity _a;
	/** One of the objects to constrain */
	private Entity _b;
	/** The fixed distance between the two objects */
	private float _dist;
	
	/** Creates a new stick constraint */
	public StickConstraint()
	{
		_dist = 0;
	}

	/** Creates a new stick constraint */
	public StickConstraint(float distance)
	{
		_dist = distance;
	}

	/** Creates a new stick constraint */
	public StickConstraint(Entity a, Entity b, float distance)
	{
		_a = a;
		_b = b;
		_dist = distance;
	}

	/** Applies this stick constraint */
	@Override
	public boolean apply(ArrayList<Entity> world) 
	{
		Vector2 v = _a.getPosition().subtract(_b.getPosition());
		float d = v.lengthSquared();
		
		if (Math.abs(d - _dist) < 1e-6)
		{
			return true;
		}
		else
		{
			v = v.normalize().multiply((_dist - d) * .5f);
			_b.setPosition(_b.getPosition().add(v));
			_a.setPosition(_a.getPosition().subtract(v));
			
			return false;
		}
	}
}
