package dkilian.andy;

/**
 * An Entity controlled by a VerletPhysicsController, with convenience methods for
 * accessing the attributes stored in the Entity's VerletTag
 * 
 * @author dkilian
 */
public class VerletEntity extends Entity
{
	/**
	 * Creates a new VerletEntity
	 * @param controller The object that controls this entity
	 */
	public VerletEntity(VerletPhysicsController controller)
	{
		super(controller);
	}
	
	/**
	 * Creates a new VerletEntity
	 * @param controller The object that controls this entity
	 * @param shape A coarse description of this entity's shape. Used for any required collision detection.
	 * @param position The position of this entity (i.e. the position of the origin of this entity's shape) in world space
	 */
	public VerletEntity(VerletPhysicsController controller, CollisionShape shape, Vector2 position)
	{
		super(controller, shape, position);
	}
	
	/** Very convenient */
	private final VerletTag tag()
	{
		if (_tag == null)
			return null;
		if (_tag.getClass() != VerletTag.class)
			return null;
		
		return (VerletTag)_tag;
	}
	
	/** Gets this entity's mass */
	public float getMass()
	{
		return _tag == null ? 0 : tag().getMass();
	}
	
	/** Sets this entity's mass */
	public void setMass(float mass)
	{
		if (_tag != null)
			tag().setMass(mass);
	}
	
	/** Zeroes this entity's net-force-applied vector */
	public void resetForce()
	{
		if (_tag != null)
			tag().resetForce();
	}
	
	/** Adds the given force to this entity's net-force-applied vector */
	public void applyForce(Vector2 force)
	{
		if (_tag != null)
			tag().applyForce(force);
	}
	
	/** Gets the net force applied to this entity */
	public Vector2 getForce()
	{
		return _tag == null ? Vector2.Zero() : tag().getForce();
	}
	
	/** Sets the net force applied to this entity */
	public void setForce(Vector2 force)
	{
		if (_tag != null)
			tag().setForce(force);
	}
	
	/** Gets this entity's current acceleration vector */
	public Vector2 getAcceleration() 
	{
		return _tag == null ? Vector2.Zero() : tag().getAcceleration();
	}
	
	/** Sets this entity's current acceleration vector */
	public void setAcceleration(Vector2 accel)
	{
		if (_tag != null)
			tag().setAcceleration(accel);
	}
	
	/** Gets the position of this entity during the previous timestep (used in Verlet integration) */
	public Vector2 getPreviousPosition()
	{
		return _tag == null ? Vector2.Zero() : tag().getPreviousPosition();
	}

	/** Sets the position of this entity during the previous timestep (used in Verlet integration) */
	public void setPreviousPosition(Vector2 p)
	{
		if (_tag != null)
			tag().setPreviousPosition(p);
	}
}
