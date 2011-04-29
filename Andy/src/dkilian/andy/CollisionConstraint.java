package dkilian.andy;

import java.util.ArrayList;

/**
 * Constrains the world such that no two entities collide
 * 
 * @author dkilian
 */
public class CollisionConstraint implements Constraint
{
	/** Preallocated for GC performance */
	private Vector2 _offset;
	private Vector2 _center;
	private CollisionTriangle _t;
	
	/** Creates a new collision constraint */
	public CollisionConstraint()
	{
		_offset = new Vector2();
		_center = new Vector2();
		_t = new CollisionTriangle();
	}
	
	/** Computes a collision between a rigid and non-rigid body */
	private boolean rigidNonRigid(Entity rigid, Entity nonRigid)
	{
		boolean ret = true;
	
		rigid.getShape().getTranslation().set(rigid.getPosition());
		
		_center.x = 0.f;
		_center.y = 0.f;
		for (int i = 0; i < nonRigid.getNonRigidPoints().size(); ++i)
		{
			Vector2 v = nonRigid.getNonRigidPoints().get(i).getPosition();
			_center.x += v.x;
			_center.y += v.y;
		}
		_center.multiply(1.f / nonRigid.getNonRigidPoints().size());
		
		_t.setX(2, _center.x);
		_t.setY(2, _center.y);
		
		for (int i = 0; i < nonRigid.getNonRigidPoints().size(); ++i)
		{			
			NonRigidBodyPoint p1 = nonRigid.getNonRigidPoints().get(i);
			NonRigidBodyPoint p2 = nonRigid.getNonRigidPoints().get((i + 1) % nonRigid.getNonRigidPoints().size());
			
			_t.setX(0, p1.getPosition().x);
			_t.setY(0, p1.getPosition().y);
			_t.setX(1, p2.getPosition().x);
			_t.setY(1, p2.getPosition().y);

			rigid.getShape().computeNonCollisionVector(_t, _offset);
			if (_offset.x != 0.f || _offset.y != 0.f)
			{
				ret = false;
				_offset.multiply(-.5f);
				p1.getPosition().add(_offset);
				p2.getPosition().add(_offset);
			}
		}
		
		return ret;
	}
	
	/** Applies this constraint */
	@Override
	public boolean apply(ArrayList<Entity> world) 
	{
		boolean ret = true;
		
		for (int i = 0; i < world.size(); ++i)
		{
			Entity e1 = world.get(i);
			if (e1.isNonRigid())
			{
				for (int j = i + 1; j < world.size(); ++j)
				{
					Entity e2 = world.get(j);
					if (e2.getShape() != null)
						if (!rigidNonRigid(e2, e1))
							ret = false;
				}
			}
			else if (e1.getShape() != null)
			{
				e1.getShape().getTranslation().set(e1.getPosition());
				for (int j = i + 1; j < world.size(); ++j)
				{
					Entity e2 = world.get(j);
					if (e2.isNonRigid())
					{
						if (!rigidNonRigid(e1, e2))
							ret = false;
					}
					else if (e2.getShape() != null)
					{
						e2.getShape().getTranslation().set(e2.getPosition());
						e1.getShape().computeNonCollisionVector(e2.getShape(), _offset);
						if (_offset.x != 0.f || _offset.y != 0.f)
						{
							ret = false;
							_offset.multiply(.5f);
							
							Vector2 tmp = _center;
							tmp.set(_offset).multiply(e1.getRestitution() + 1.f);
							e1.getPosition().add(tmp);
							
							tmp.set(_offset).multiply(e2.getRestitution() + 1.f);
							e2.getPosition().subtract(tmp);
						}
					}
				}
			}
		}
		
		return ret;
	}
}
