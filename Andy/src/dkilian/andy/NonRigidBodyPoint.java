package dkilian.andy;

/**
 * Describes a vertex of a non-rigid body
 * @author dkilian
 */
public class NonRigidBodyPoint 
{
	/** The position of this point, in world space. */
	private Vector2 _position; 
	/** The information about this point stored by the parent entity's controller */
	private Object _tag;
	
	/**
	 * Creates a new non-rigid body point
	 */
	public NonRigidBodyPoint()
	{
		_position = Vector2.Zero();
	}

	/**
	 * Creates a new non-rigid body point
	 * @param pos The position of this point, in world space
	 */
	public NonRigidBodyPoint(Vector2 pos)
	{
		_position = pos;
	}

	/**
	 * Creates a new non-rigid body point
	 * @param tag Information about this point stored by the parent entity's controller
	 */
	public NonRigidBodyPoint(Object tag)
	{
		_position = Vector2.Zero();
		_tag = tag;
	}

	/**
	 * Creates a new non-rigid body point
	 * @param pos The position of this point, in world space
	 * @param tag Information about this point stored by the parent entity's controller
	 */
	public NonRigidBodyPoint(Vector2 pos, Object tag)
	{
		_position = pos;
		_tag = tag;
	}
	
	/** Gets the position of this point in world space */
	public Vector2 getPosition()
	{
		return _position;
	}
	
	/** Sets the position of this point in world space */
	public void setPosition(Vector2 pos)
	{
		_position = pos;
	}
	
	/** Gets information about this point stored by the parent entity's controller */
	public Object getTag()
	{
		return _tag;
	}

	/** Sets information about this point stored by the parent entity's controller */
	public void setTag(Object tag)
	{
		_tag = tag;
	}
}
