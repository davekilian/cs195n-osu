package dkilian.andy;

import java.util.ArrayList;

/**
 * Base class for an object that can appear in the game world.
 *  
 * @author dkilian
 */
public class Entity 
{
	/** A coarse description of this object's shape */
	protected CollisionShape _shape;
	/** The position of this object, relative to the origin of its shape, in world coordinates */
	protected Vector2 _position;
	/** Controls this Entity's behavior, i.e. via physics, user input, or AI */
	protected EntityController _controller;
	/** An object that stores parameters about this Entity used by this Entity's EntityController */
	protected Object _tag;
	/** The object's coefficient of restitution */
	protected float _restitution;
	/** True if this is a non-rigid body, false if this is a rigid body */
	protected boolean _isNonRigid;
	/** The non-rigid description of this body */
	protected ArrayList<NonRigidBodyPoint> _points;
	/** The springs that hold this non-rigid body together */
	protected ArrayList<NonRigidBodySpringConstraint> _springs;
		
	/**
	 * Creates a new Entity
	 */
	public Entity()
	{
		_shape = null;
		_position = Vector2.Zero();
		_controller = null;
		_tag = null;
		_restitution = 0;
		_isNonRigid = false;
		_points = new ArrayList<NonRigidBodyPoint>();
		_springs = new ArrayList<NonRigidBodySpringConstraint>();
	}
	
	/**
	 * Creates a new Entity
	 * @param controller The object that controls this entity
	 */
	public Entity(EntityController controller)
	{
		_shape = null;
		_position = Vector2.Zero();
		_tag = null;
		_controller = controller;
		_controller.add(this);
		_restitution = 0;
		_isNonRigid = false;
		_points = new ArrayList<NonRigidBodyPoint>();
		_springs = new ArrayList<NonRigidBodySpringConstraint>();
	}
	
	/**
	 * Creates a new Entity
	 * @param shape A coarse description of this rigid body's shape. Used for any required collision detection.
	 * @param position The position of this rigid body (i.e. the position of the origin of this entity's shape) in world space
	 */
	public Entity(CollisionShape shape, Vector2 position)
	{
		_shape = shape;
		_position = position;
		_tag = null;
		_restitution = 0;
		_isNonRigid = false;
		_points = new ArrayList<NonRigidBodyPoint>();
		_springs = new ArrayList<NonRigidBodySpringConstraint>();
	}

	/**
	 * Creates a new Entity
	 * @param controller The object that controls this entity
	 * @param shape A coarse description of this rigid body's shape. Used for any required collision detection.
	 * @param position The position of this rigid body (i.e. the position of the origin of this entity's shape) in world space
	 */
	public Entity(EntityController controller, CollisionShape shape, Vector2 position)
	{
		_shape = shape;
		_position = position;
		_tag = null;
		_controller = controller;
		_controller.add(this);
		_restitution = 0;
		_isNonRigid = false;
		_points = new ArrayList<NonRigidBodyPoint>();
		_springs = new ArrayList<NonRigidBodySpringConstraint>();
	}
	
	/** Gets a coarse description of this rigid body's shape. Used for any required collision detection. */
	public CollisionShape getShape()
	{
		return _shape;
	}

	/** Sets a coarse description of this rigid body's shape. Used for any required collision detection. */
	public void setShape(CollisionShape shape)
	{
		_shape = shape;
	}
	
	/** Gets the position of this rigid body (i.e. the position of the origin of this entity's shape) in world space */
	public Vector2 getPosition()
	{		
		return _position;
	}

	/** Sets the position of this rigid body (i.e. the position of the origin of this entity's shape) in world space */
	public void setPosition(Vector2 position)
	{		
		_position = position;
	}
	
	/** Gets this rigid body's coefficient of restitution */
	public float getRestitution()
	{
		return _restitution;
	}
	
	/** Sets this rigid body's coefficient of restitution */
	public void setRestitution(float restitution)
	{
		_restitution = restitution;
	}
	
	/** Gets the object that controls this entity */
	public EntityController getController()
	{
		return _controller;
	}

	/** Sets the object that controls this entity. Note that you probably mean to call YourEntityController.add() instead of this directly. */
	public void setController(EntityController controller)
	{
		_controller = controller;
	}
	
	/** Gets an object storing data about this entity specific to this entity's EntityController */
	public Object getTag()
	{
		return _tag;
	}

	/** Sets an object storing data about this entity specific to this entity's EntityController */
	public void setTag(Object tag)
	{
		_tag = tag;
	}
	
	/** Gets whether this body is rigid or non-rigid */
	public boolean isNonRigid()
	{
		return _isNonRigid;
	}

	/** Sets whether this body is rigid or non-rigid */
	public void setNonRigid(boolean nonRigid)
	{
		_isNonRigid = nonRigid;
	}
	
	/** Gets the points that comprise this non-rigid body */
	public ArrayList<NonRigidBodyPoint> getNonRigidPoints()
	{
		return _points;
	}

	/** Sets the points that comprise this non-rigid body */
	public void setNonRigidPoints(ArrayList<NonRigidBodyPoint> points)
	{
		_points = points;
	}

	/** Gets the springs that hold this non-rigid body together */
	public ArrayList<NonRigidBodySpringConstraint> getSprings()
	{
		return _springs;
	}

	/** Sets the springs that hold this non-rigid body together */
	public void setSprings(ArrayList<NonRigidBodySpringConstraint> springs)
	{
		_springs = springs;
	}
}
