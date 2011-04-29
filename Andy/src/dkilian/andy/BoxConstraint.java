package dkilian.andy;

import java.util.ArrayList;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Constraints objects to be within an axis-aligned bounding box
 * 
 * @author dkilian
 */
public class BoxConstraint implements Constraint
{
	/** The box to constrain objects to */
	private Rect _r;
	/** Preallocated for GC performance */
	private RectF _bounds;
	
	/** Creates a new box constraint */
	public BoxConstraint()
	{
		_r = new Rect(0, 0, 0, 0);
		_bounds = new RectF(0.f, 0.f, 0.f, 0.f);
	}

	/** Creates a new box constraint */
	public BoxConstraint(Rect r)
	{
		_r = r;
		_bounds = new RectF(0.f, 0.f, 0.f, 0.f);
	}
	
	/** Gets the box to constrain objects to */
	public Rect getBounds()
	{
		return _r;
	}
	
	/** Sets the box to constrain objects to */
	public void setBounds(Rect r)
	{
		_r = r;
	}

	/** Applies this constraint to the given entities */
	@Override
	public boolean apply(ArrayList<Entity> world) 
	{
		boolean ret = true;
		_bounds.top = 0.f;
		_bounds.left = 0.f;
		_bounds.right = 0.f;
		_bounds.bottom = 0.f;
		
		for (int i = 0; i < world.size(); ++i)
		{
			Entity e = world.get(i);
			if (e.isNonRigid())
			{
				for (int j = 0; j < e.getNonRigidPoints().size(); ++j)
				{
					NonRigidBodyPoint pt = e.getNonRigidPoints().get(j);
					Vector2 p = pt.getPosition();
					
					if (p.x < _r.left)
					{
						ret = false;
						p.x = _r.left;
					}
					
					if (p.x > _r.right)
					{
						ret = false;
						p.x = _r.right;
					}
					
					if (p.y < _r.top)
					{
						ret = false;
						p.y = _r.top;
					}
					
					if (p.y > _r.bottom)
					{
						ret = false;
						p.y = _r.bottom;
					}
				}
			}
			else if (e.getShape() != null)
			{
				e.getShape().getTranslation().set(e.getPosition());
				
				if (e.getShape().getClass() == CollisionCircle.class)
				{
					CollisionCircle c = (CollisionCircle)e.getShape();
					_bounds.top = e.getPosition().y - c.getRadius();
					_bounds.bottom = e.getPosition().y + c.getRadius();
					_bounds.left = e.getPosition().x - c.getRadius();
					_bounds.right = e.getPosition().x + c.getRadius();
				}
				else
				{
					_bounds.top = Float.MAX_VALUE;
					_bounds.bottom = -Float.MAX_VALUE;
					_bounds.left = Float.MAX_VALUE;
					_bounds.right = -Float.MAX_VALUE;
					
					Vector2[] p = e.getShape().getPoints();
					if (p != null)
					{
						for (Vector2 v : p)
						{
							if (v.x < _bounds.left)
								_bounds.left = v.x;
							if (v.x > _bounds.right)
								_bounds.right = v.x;
							if (v.y < _bounds.top)
								_bounds.top = v.y;
							if (v.y > _bounds.bottom)
								_bounds.bottom = v.y;
						}
					}
				}
						
				if (_bounds.width() > _r.width() || _bounds.height() > _r.height())
					continue;		// Bounding rect too small
				
				float restitution = Math.max(0, e.getRestitution()) + 1.f;
				
				if (_bounds.left < _r.left)
				{
					ret = false;
					e.getPosition().x += (_r.left - _bounds.left) * restitution;
				}
				
				if (_bounds.right > _r.right)
				{
					ret = false;
					e.getPosition().x += (_r.right - _bounds.right) * restitution;
				}
				
				if (_bounds.top < _r.top)
				{
					ret = false;
					e.getPosition().y += (_r.top - _bounds.top) * restitution;
				}
				
				if (_bounds.bottom > _r.bottom)
				{
					ret = false;
					e.getPosition().y += (_r.bottom - _bounds.bottom) * restitution;
				}
				
				e.getShape().getTranslation().set(e.getPosition());
			}
		}
		
		return ret;
	}
}
