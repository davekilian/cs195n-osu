package dkilian.andy;

import java.util.ArrayList;

/**
 * Constrains the distance between two entities as if they were connected by a spring
 * 
 * @author dkilian
 */
public class SpringConstraint implements Constraint
{
	/** The entities to apply this constraint to */
	private Entity _a, _b;
	/** The zero-force distance between the two objects */
	private float _distance;
	/** The spring constant of this constraint */
	private float _springiness;
	
	/**
	 * Creates a new spring constraint
	 */
	public SpringConstraint()
	{
		_distance = 1.f;
		_springiness = 1.f;
	}
	
	/**
	 * Creates a new spring constraint
	 * @param a One of the entities to apply this constraint to
	 * @param b One of the entities to apply this constraint to
	 * @param distance The distance between the objects for which no force is applied to either
	 * @param springiness The spring constant of this constraint
	 */
	public SpringConstraint(Entity a, Entity b, float distance, float springiness)
	{
		setA(a);
		setB(b);
		setDistance(distance);
		setSpringiness(springiness);
	}
	
	/** Gets one of the entities to apply this constraint to */
	public Entity getA()
	{
		return _a;
	}

	/** Sets one of the entities to apply this constraint to */
	public void setA(Entity a)
	{
		_a = a;
	}

	/** Gets one of the entities to apply this constraint to */
	public Entity getB()
	{
		return _b;
	}

	/** Sets one of the entities to apply this constraint to */
	public void setB(Entity b)
	{
		_b = b;
	}
	
	/** Gets the distance between the two objects for which the spring produces zero force */
	public float getDistance()
	{
		return _distance;
	}

	/** Sets the distance between the two objects for which the spring produces zero force */
	public void setDistance(float d)
	{
		_distance = d;
	}
	
	/** Gets this constraint's spring constant */
	public float getSpringiness()
	{
		return _springiness;
	}

	/** Sets this constraint's spring constant */
	public void setSpringiness(float springiness)
	{
		_springiness = springiness;
	}

	/** Applies this constraint to the two entities it applies to */
	@Override
	public boolean apply(ArrayList<Entity> world) 
	{
		if (_a != null && _b != null)
		{
			Vector2 aToB = _b.getPosition().subtract(_a.getPosition());
			float distance = aToB.length();
			aToB = aToB.multiply(1.f / distance);
			float kx = _springiness * (distance - _distance);
			
			VerletTag tagA = (VerletTag)_a.getTag();
			tagA.setAcceleration(tagA.getAcceleration().add(aToB.multiply(kx)));
			
			VerletTag tagB = (VerletTag)_b.getTag();
			tagB.setAcceleration(tagB.getAcceleration().add(aToB.multiply(-kx)));
		}

		return true;
	}
}
