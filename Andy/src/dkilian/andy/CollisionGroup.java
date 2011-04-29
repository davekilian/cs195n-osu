package dkilian.andy;

import java.util.ArrayList;

/**
 * A collision 'primitive' consisting of other collision primitives
 * 
 * @author dkilian
 */
public class CollisionGroup implements CollisionShape
{
	/** The translation between this shape's origin and the world origin */
	private Vector2 _translation;	
	/** The child shapes that make up this shape group */
	private ArrayList<CollisionShape> _children;
	/** Preallocated for GC performance */
	private Vector2 _tmp;
	
	/** Creates a new collision shape group */
	public CollisionGroup()
	{
		_translation = Vector2.Zero();
		_children = new ArrayList<CollisionShape>();
		_tmp = new Vector2();
	}
	
	/** Creates a new collision shape group with the specified children */
	public CollisionGroup(ArrayList<CollisionShape> children)
	{
		_translation = Vector2.Zero();
		_children = children;
		_tmp = new Vector2();
	}
	
	/** Adds a shape to this collision shape group */
	public void add(CollisionShape child)
	{
		_children.add(child);
	}
	
	/** Removes a shape from this collision shape group */
	public void remove(CollisionShape child)
	{
		_children.remove(child);
	}
	
	/** Gets the collision shapes in this group */
	public ArrayList<CollisionShape> getChildren()
	{
		return _children;
	}

	/** Checks whether a point intersects this shape */
	@Override
	public boolean intersects(Vector2 v) 
	{
		v = v.subtract(_translation);
		
		for (CollisionShape shape : _children)
			if (shape.intersects(v))
				return true;
		
		return false;
	}

	/** Not supported, since the compound shape this object represents may not be convex */
	@Override
	public Vector2[] getPoints() 
	{
		return null;
	}

	/** Gets the translation between this shape's origin and the world origin */
	@Override
	public Vector2 getTranslation() 
	{
		return _translation;
	}

	/** Sets the translation between this shape's origin and the world origin */
	@Override
	public void setTranslation(Vector2 t) 
	{
		_translation = t;
	}

	/** Determines whether this shape intersects with another */
	@Override
	public boolean intersects(CollisionShape other) 
	{		
		for (int i = 0; i < _children.size(); ++i)
		{
			CollisionShape shape = _children.get(i);
			Vector2 tmp = shape.getTranslation();
			shape.setTranslation(tmp.add(_translation));
			
			boolean intersect = other.intersects(shape);
			
			shape.setTranslation(tmp);
			
			if (intersect)
				return true;
		}
		
		return false;
	}

	/** Computes the minimum vector of non-collision between this and another shape */
	@Override
	public void computeNonCollisionVector(CollisionShape other, Vector2 nonCollision) 
	{		
		Vector2 sum = nonCollision;
		sum.x = 0.f;
		sum.y = 0.f;
		
		for (int i = 0; i < _children.size(); ++i)
		{
			CollisionShape shape = _children.get(i);
			shape.getTranslation().add(_translation);
			
			shape.computeNonCollisionVector(other, _tmp);
			sum.add(_tmp);
			
			shape.getTranslation().subtract(_translation);
		}
	}
}
