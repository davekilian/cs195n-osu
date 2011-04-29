package dkilian.andy;

/**
 * Performs collision detection on a triangle
 * 
 * @author dkilian
 */
public class CollisionTriangle implements CollisionShape
{
	/** The distance between this shape's origin and the world origin */
	private Vector2 _translation;
	/** This triangle's points */
	private Vector2[] _points;
	
	/** Creates a new collision triangle */
	public CollisionTriangle()
	{
		_translation = Vector2.Zero();
		_points = new Vector2[3];
		
		for (int i = 0; i < 3; i++)
		{
			_points[i] = new Vector2();
			_points[i].x = 0.f;
			_points[i].y = 0.f;
		}
	}
	
	/**
	 * Creates a new collision triangle
	 * @param x An array of size 3, whose values represent the X coordinates of points A, B, and C, respectively, in untransformed space
	 * @param y An array of size 3, whose values represent the Y coordinates of points A, B, and C, respectively, in untransformed space
	 */
	public CollisionTriangle(float[] x, float[] y)
	{
		_translation = Vector2.Zero();
		_points = new Vector2[3];
		
		for (int i = 0; i < 3; i++)
		{
			_points[i] = new Vector2();
			_points[i].x = x[i];
			_points[i].y = y[i];
		}
	}
	
	/** Creates a new collision triangle from the specified point coordinates, in untransformed space */
	public CollisionTriangle(float xa, float ya, float xb, float yb, float xc, float yc)
	{
		_translation = Vector2.Zero();
		_points = new Vector2[3];
		
		for (int i = 0; i < 3; i++)
			_points[i] = new Vector2();

		_points[0].x = xa;
		_points[0].y = ya;
		_points[1].x = xb;
		_points[1].y = yb;
		_points[2].x = xc;
		_points[2].y = yc;
	}
	
	/** Gets the X coordinate of point A, B, or C in untransformed space */
	public float getX(int i)
	{
		return _points[i].x;
	}

	/** Sets the X coordinate of point A, B, or C in untransformed space */
	public void setX(int i, float x)
	{
		_points[i].x = x;
	}

	/** Gets the Y coordinate of point A, B, or C in untransformed space */
	public float getY(int i)
	{
		return _points[i].y;
	}

	/** Sets the Y coordinate of point A, B, or C in untransformed space */
	public void setY(int i, float y)
	{
		_points[i].y = y;
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

	/** Checks whether a point intersects this triangle */
	@Override
	public boolean intersects(Vector2 vec) 
	{		
		float xp = vec.x - _translation.x;
		float yp = vec.y - _translation.y;

		float xa = _points[0].x;
		float ya = _points[0].y;
		float xb = _points[1].x;
		float yb = _points[1].y;
		float xc = _points[2].x;
		float yc = _points[2].y;
		
		float u = ((yc - ya) * (xp - xa) - (xc - xa) * (yp - ya)) / ((yc - ya) * (xb - xa) - (xc - xa) * (yb - ya));
		float v = ((xb - xa) * (yp - ya) - (yb - ya) * (xp - xa)) / ((xb - xa) * (yb - ya) - (yb - ya) * (xc - xa));
		
		return u >= 0.f && v >= 0.f && u + v <= 1.f;
	}

	/** Gets this triangle's points, in world space */
	@Override
	public Vector2[] getPoints() 
	{
		return _points;
	}

	/** Determines whether another shape intersects this one */
	@Override
	public boolean intersects(CollisionShape other) 
	{
		if (other.getClass() == CollisionCircle.class)
			return other.intersects(this);
		else
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
