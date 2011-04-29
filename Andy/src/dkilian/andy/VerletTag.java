package dkilian.andy;

/**
 * The attributes stored in any Entity objects controlled by a VerletPhysicsController
 * 
 * @author dkilian
 */
public class VerletTag 
{
	/** The mass of this entity */
	private float _mass;
	/** The net force currently being applied to this entity */
	private Vector2 _force;
	/** The position of this entity at the previous timestep (used in Verlet integration) */
	private Vector2 _previousPosition;
	
	/** Creates a new Verlet tag */
	public VerletTag()
	{
		_mass = 1.f;
		_force = Vector2.Zero();
		_previousPosition = Vector2.Zero();
	}
	
	/** Gets this entity's mass */
	public final float getMass()
	{
		return _mass;
	}
	
	/** Sets this entity's mass */
	public final void setMass(float mass)
	{
		_mass = mass;
	}
	
	/** Zeroes this entity's net-force-applied vector */
	public final void resetForce()
	{
		_force = Vector2.Zero();
	}
	
	/** Adds the given force to this entity's net-force-applied vector */
	public final void applyForce(Vector2 force)
	{
		_force = _force.add(force);
	}
	
	/** Gets the net force applied to this entity */
	public final Vector2 getForce()
	{
		return _force;
	}
	
	/** Sets the net force applied to this entity */
	public final void setForce(Vector2 force)
	{
		_force = force;
	}
	
	/** Gets this entity's current acceleration vector */
	public final Vector2 getAcceleration() 
	{
		return _force.multiply(1.f / _mass);
	}
	
	/** Sets this entity's current acceleration vector */
	public final void setAcceleration(Vector2 accel)
	{
		_force = accel.multiply(_mass);
	}
	
	/** Gets the position of this entity during the previous timestep (used in Verlet integration) */
	public final Vector2 getPreviousPosition()
	{
		return _previousPosition;
	}

	/** Sets the position of this entity during the previous timestep (used in Verlet integration) */
	public final void setPreviousPosition(Vector2 p)
	{
		_previousPosition = p;
	}
}
