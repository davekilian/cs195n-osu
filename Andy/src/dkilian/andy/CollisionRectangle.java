package dkilian.andy;

import android.graphics.Rect;

/**
 * Performs collision detection on a rectangle
 * 
 * @author dkilian
 */
public class CollisionRectangle implements CollisionShape
{
	/** The translation between this shape's origin and the world origin */
	private Vector2 _translation;
	/** The bounds of this rectangle */
	private Rect _bounds;
	/** Returned in getPoints(). Preallocated for GC performance */
	private Vector2[] _points;

	/** Creates a new collision rectangle */
	public CollisionRectangle()
	{
		_translation = Vector2.Zero();
		_bounds = new Rect();
		_points = new Vector2[4];
		for (int i = 0; i < 4; ++i)
			_points[i] = new Vector2();
	}
	
	/** Creates a collision rectangle with the specified bounds */
	public CollisionRectangle(Rect bounds)
	{
		_translation = Vector2.Zero();
		_bounds = bounds;
		_points = new Vector2[4];
		for (int i = 0; i < 4; ++i)
			_points[i] = new Vector2();
	}
	
	/** Gets the bounds of this collision rectangle, in untransformed space */
	public Rect getBounds()
	{
		return _bounds;
	}
	
	/** Sets the bounds of this collision rectangle, in untransformed space */
	public void setBounds(Rect bounds)
	{
		_bounds = bounds;
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

	/** Checks whether a point intersects this shape */
	@Override
	public boolean intersects(Vector2 v) 
	{
		v = v.subtract(_translation);
		
		return (v.x >= _bounds.left && v.x <= _bounds.right) &&
			   (v.y >= _bounds.top  && v.y <= _bounds.bottom);
	}

	/** Gets this triangle's points, in world space */
	@Override
	public Vector2[] getPoints() 
	{
		for (int i = 0; i < 4; ++i)
			_points[i].set(_translation);

		_points[0].x += _bounds.left;
		_points[0].y += _bounds.top;
		
		_points[1].x += _bounds.left;
		_points[1].y += _bounds.bottom;
		
		_points[2].x += _bounds.right;
		_points[2].y += _bounds.top;
		
		_points[3].x += _bounds.right;
		_points[3].y += _bounds.bottom;
		
		return _points;
	}

	/** Determines whether another shape intersects this one */
	@Override
	public boolean intersects(CollisionShape other) 
	{
		if (other.getClass() == CollisionCircle.class)
			return other.intersects(this);
		
		return SeparatingAxisCollisionDetector.intersection(getPoints(), other.getPoints());
	}

	/** Computes the minimum vector of non-collision between this and another shape */
	@Override
	public void computeNonCollisionVector(CollisionShape other, Vector2 nonCollision) 
	{
		if (other.getClass() == CollisionCircle.class)
			other.computeNonCollisionVector(this, nonCollision);
		else
			SeparatingAxisCollisionDetector.computeNonCollisionVector(getPoints(), other.getPoints(), nonCollision);
	}
}
