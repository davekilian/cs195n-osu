package dkilian.andy;

import java.util.ArrayList;

/**
 * Constrains a pair of entities so they do not collide. 
 * Used for performance enhancements when every object does not need
 * to be checked against every other object.
 * Currently only implemented for rigid body collisions.
 * 
 * @author dkilian
 */
public class CollisionPairConstraint implements Constraint
{
	private Entity _e1, _e2;
	private Vector2 _offset;
	private Vector2 _center;
	
	public CollisionPairConstraint(Entity e1, Entity e2)
	{
		_e1 = e1;
		_e2 = e2;
		_offset = new Vector2();
		_center = new Vector2();
	}
	
	public Entity getEntity1()
	{
		return _e1;
	}
	
	public void setEntity1(Entity e)
	{
		_e1 = e;
	}
	
	public Entity getEntity2()
	{
		return _e2;
	}
	
	public void setEntity2(Entity e)
	{
		_e2 = e;
	}

	@Override
	public boolean apply(ArrayList<Entity> world) 
	{
		boolean ret = true;
		
		_e1.getShape().getTranslation().set(_e1.getPosition());
		_e2.getShape().getTranslation().set(_e2.getPosition());
		
		_e1.getShape().computeNonCollisionVector(_e2.getShape(), _offset);

		if (_offset.x != 0.f || _offset.y != 0.f)
		{
			ret = false;
			_offset.multiply(.5f);
			
			Vector2 tmp = _center;
			tmp.set(_offset).multiply(_e1.getRestitution() + 1.f);
			_e1.getPosition().add(tmp);
			
			tmp.set(_offset).multiply(_e2.getRestitution() + 1.f);
			_e2.getPosition().subtract(tmp);
		}
		
		return ret;
	}
}
