package dkilian.andy;

/** 
 * Performs collision detection on a circle
 * 
 * @author dkilian
 */
public class CollisionCircle implements CollisionShape
{
	/** The translation between this shape's origin and the world origin */
	private Vector2 _translation;
	/** The radius of this circle */
	private float _r;
	/** Temporary value preallocated for GC performance */
	private Vector2 _tmp1;
	private Vector2 _tmp2;
	private Vector2 _tmp3;
	private Vector2 _tmp4;
	private Vector2[] _points;
	
	/** Creates a new collision circle centered at the origin with a radius of 1 */
	public CollisionCircle()
	{
		_translation = Vector2.Zero();
		_r = 1.f;
		_tmp1 = new Vector2();
		_tmp2 = new Vector2();
		_tmp3 = new Vector2();
		_tmp4 = new Vector2();
		_points = new Vector2[3];
		for (int i = 0; i < _points.length; ++i)
			_points[i] = new Vector2();
	}
	
	/** Creates a new collision circle centered at the origin with the specified radius */
	public CollisionCircle(float r)
	{
		_translation = Vector2.Zero();
		_r = r;
		_tmp1 = new Vector2();
		_tmp2 = new Vector2();
		_tmp3 = new Vector2();
		_tmp4 = new Vector2();
		_points = new Vector2[3];
		for (int i = 0; i < _points.length; ++i)
			_points[i] = new Vector2();
	}
	
	/** Gets this circle's radius */
	public float getRadius()
	{
		return _r;
	}
	
	/** Sets this circle's radius */
	public void setRadius(float r)
	{
		_r = r;
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
		return v.distanceSquaredTo(_translation) <= _r * _r;
	}

	/** Not supported (not applicable to circles) */
	@Override
	public Vector2[] getPoints() 
	{
		return null;
	}

	/** Checks whether this circle intersects another shape */
	@Override
	public boolean intersects(CollisionShape other) 
	{
		if (other.getClass() == CollisionCircle.class)
		{
			CollisionCircle o = (CollisionCircle)other;
			
			return other.getTranslation().distanceSquaredTo(_translation) <= _r * _r + o._r * o._r;
		}
		
		Vector2 center = _translation;
		
		Vector2[] op = other.getPoints();
		Vector2 closest = null;
		float minDist = Float.MAX_VALUE;
		for (Vector2 v : op)
		{
			float d = v.distanceSquaredTo(center);
			if (d < minDist)
			{
				closest = v;
				minDist = d;
			}
		}
		
		Vector2 axis = center.subtract(closest).normalize();
		Vector2[] p = { center, center.add(axis.multiply(_r)), center.subtract(axis.multiply(_r)) };
		
		return SeparatingAxisCollisionDetector.intersection(p, op, axis);
	}

	/** Computes the minimum vector of non-collision between this and another shape */
	@Override
	public void computeNonCollisionVector(CollisionShape other, Vector2 nonCollision) 
	{
		if (other.getClass() == CollisionCircle.class)
		{
			CollisionCircle o = (CollisionCircle)other;
			
			Vector2 delta = _tmp1;
			delta.set(_translation).subtract(o._translation);
			float len = delta.length();
			if (len > _r + o._r || len < 1e-6)
			{
				nonCollision.x = 0.f;
				nonCollision.y = 0.f;
				return;
			}
			
			nonCollision.set(delta.multiply((_r + o._r - len) / len));
			return;
		}
		
		Vector2 center = _tmp1;
		center.set(_translation);
		
		Vector2[] op = other.getPoints();
		Vector2 closest = null;
		float minDist = Float.MAX_VALUE;
		for (Vector2 v : op)
		{
			float d = v.distanceSquaredTo(center);
			if (d < minDist)
			{
				closest = v;
				minDist = d;
			}
		}
		
		Vector2 axis = _tmp2;
		axis.set(center).subtract(closest).normalize();
		_points[0].set(center);
		_points[1].set(axis).multiply(_r).add(center);
		_points[2].set(axis).multiply(-_r).add(center);
		
		Vector2 min = _tmp3;
		float minLength = Float.MAX_VALUE;
		SeparatingAxisCollisionDetector.computeNonCollisionVector(_points, op, axis, min);
		minLength = min.lengthSquared();
		
		for (int i = 0; i < op.length; ++i)
		{
			axis.set(op[(i + 1) % op.length]).subtract(op[i]).normalize();
			_points[0].set(center);
			_points[1].set(axis).multiply(_r).add(center);
			_points[2].set(axis).multiply(-_r).add(center);
			
			Vector2 v = _tmp4;
			SeparatingAxisCollisionDetector.computeNonCollisionVector(_points, op, axis, v);
			float len = v.lengthSquared();
			if (len < minLength)
			{
				min.set(v);
				minLength = len;
			}
		}
		
		nonCollision.set(min);
	}
}
