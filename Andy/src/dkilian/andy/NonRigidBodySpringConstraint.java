package dkilian.andy;

import java.util.ArrayList;

public class NonRigidBodySpringConstraint implements Constraint
{
	/** The entities to apply this constraint to */
	private NonRigidBodyPoint _a, _b;
	/** The zero-force distance between the two objects */
	private float _distance;
	/** The spring constant of this constraint */
	private float _springiness;
	/** Whether to use acceleration or the modified stick behavior */
	private boolean _useAcceleration;
	/** Used in apply(). Preallocated for performance */
	private Vector2 _aToB;
	
	/**
	 * Creates a new spring constraint
	 */
	public NonRigidBodySpringConstraint()
	{
		_distance = 1.f;
		_springiness = 1.f;
		_useAcceleration = false;
		_aToB = new Vector2();
	}
	
	/**
	 * Creates a new spring constraint
	 * @param a One of the entities to apply this constraint to
	 * @param b One of the entities to apply this constraint to
	 * @param distance The distance between the objects for which no force is applied to either
	 * @param springiness The spring constant of this constraint
	 */
	public NonRigidBodySpringConstraint(NonRigidBodyPoint a, NonRigidBodyPoint b, float distance, float springiness)
	{
		setA(a);
		setB(b);
		setDistance(distance);
		setSpringiness(springiness);
		_useAcceleration = false;
		_aToB = new Vector2();
	}
	
	/** Gets NonRigidBodyPoint of the entities to apply this constraint to */
	public NonRigidBodyPoint getA()
	{
		return _a;
	}

	/** Sets one of the entities to apply this constraint to */
	public void setA(NonRigidBodyPoint a)
	{
		_a = a;
	}

	/** Gets one of the entities to apply this constraint to */
	public NonRigidBodyPoint getB()
	{
		return _b;
	}

	/** Sets one of the entities to apply this constraint to */
	public void setB(NonRigidBodyPoint b)
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
		if (_useAcceleration)
		{
			Vector2 aToB = _b.getPosition().subtract(_a.getPosition());
			float distance = aToB.length();
			if (distance > 1e-6)
			{
				aToB = aToB.multiply(1.f / distance);
				float kx = _springiness * (distance - _distance);
				
				VerletTag tagA = (VerletTag)_a.getTag();
				tagA.setAcceleration(tagA.getAcceleration().add(aToB.multiply(kx)));
				
				VerletTag tagB = (VerletTag)_b.getTag();
				tagB.setAcceleration(tagB.getAcceleration().add(aToB.multiply(-kx)));
			}
	
			return true;
		}
		else
		{
			_aToB.set(_b.getPosition());
			_aToB.subtract(_a.getPosition());
			float distance = _aToB.length();
			_aToB.multiply(.5f * _springiness * (distance - _distance) / distance);
			
			_a.setPosition(_a.getPosition().add(_aToB));
			_b.setPosition(_b.getPosition().subtract(_aToB));
			
			return Math.abs(distance - _distance) < 1e-6;
		}
	}
}
