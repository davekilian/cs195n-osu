package dkilian.andy;

/**
 * Uses the separating-axis theorem (SAT) to calculate
 * convex polygon collisions
 * 
 * @author dkilian
 */
public class SeparatingAxisCollisionDetector 
{
	/** Temporary values preallocated for GC performance */
	private static Vector2 _tmp1;
	private static Vector2 _tmp2;
	
	static
	{
		_tmp1 = new Vector2();
		_tmp2 = new Vector2();
	}
	
	
	/** Determines whether two polygons overlap when projected onto an axis */
	public static boolean intersection(Vector2[] p1, Vector2[] p2, Vector2 axis)
	{
		float min1 = Float.MAX_VALUE, max1 = -Float.MAX_VALUE;
		float min2 = Float.MAX_VALUE, max2 = -Float.MAX_VALUE;
		
		for (int i = 0; i < p1.length; ++i)
		{
			float p = axis.dot(p1[i]);
			if (p < min1) min1 = p;
			if (p > max1) max1 = p;
		}
		
		for (int i = 0; i < p2.length; ++i)
		{
			float p = axis.dot(p2[i]);
			if (p < min2) min2 = p;
			if (p > max2) max2 = p;
		}
		
		return (min1 >= min2 && min1 <= max2) || (max1 >= min2 && max1 <= max2) ||
		       (min2 >= min1 && min2 <= max1) || (max2 >= min1 && max2 <= max1);
	}
	
	/**
	 * Determines whether two polygons are intersecting.
	 * @param p1 The points of the first polygon, transformed to world space
	 * @param p2 The points of the second polygon, transformed to world space
	 * @return True if the polygons intersect or false if not
	 */
	public static boolean intersection(Vector2[] p1, Vector2[] p2)
	{
		for (int i = 0; i < p1.length; ++i)
			if (!intersection(p1, p2, p1[(i + 1) % p1.length].subtract(p1[i])))	// No need to normalize axis for simple intersection tests
				return false;
		
		for (int i = 0; i < p2.length; ++i)
			if (!intersection(p1, p2, p2[(i + 1) % p2.length].subtract(p2[i])))
				return false;
		
		return true;
	}
	
	/**
	 * Computes the minimum vector of non-collision between two shapes along a given axis
	 * @param p1 The points of the first polygon, transformed to world space
	 * @param p2 The points of the second polygon, transformed to world space
	 * @param axis The axis to project along
	 * @return The minimum vector of non-collision, or the all-zeroes vector if no collision was encountered
	 */
	public static void computeNonCollisionVector(Vector2[] p1, Vector2[] p2, Vector2 axis, Vector2 nonCollision)
	{
		float min1 = Float.MAX_VALUE, max1 = -Float.MAX_VALUE;
		float min2 = Float.MAX_VALUE, max2 = -Float.MAX_VALUE;
		float avg1 = 0, avg2 = 0;
		
		for (int i = 0; i < p1.length; ++i)
		{
			float p = axis.dot(p1[i]);
			if (p < min1) min1 = p;
			if (p > max1) max1 = p;
			avg1 += p;
		}
		
		for (int i = 0; i < p2.length; ++i)
		{
			float p = axis.dot(p2[i]);
			if (p < min2) min2 = p;
			if (p > max2) max2 = p;
			avg2 += p;
		}
		
		if ((min1 < min2 && min1 > max2) || (max1 < min2 && max1 > max2) ||
	        (min2 < min1 && min2 > max1) || (max2 < min1 && max2 > max1))
		{
			nonCollision.x = 0;
			nonCollision.y = 0;
			return;
		}
		
		float dist = max1 - min2;
		if (max2 - min1 < dist)
			dist = max2 - min1;
		
		if (dist < 0)
		{
			nonCollision.x = 0;
			nonCollision.y = 0;
			return;
		}
		
		avg1 /= p1.length;
		avg2 /= p2.length;
		
		nonCollision.set(axis.multiply(avg1 < avg2 ? -dist : dist));
	}
	
	/**
	 * Computes the minimum vector of non-collision between two shapes
	 * @param p1 The points of the first polygon, transformed to world space
	 * @param p2 The points of the second polygon, transformed to world space
	 * @param nonCollision Receives the minimum vector of non-collision, or the all-zeroes vector if no collision was encountered
	 */
	public static void computeNonCollisionVector(Vector2[] p1, Vector2[] p2, Vector2 nonCollision)
	{
		float minLength = Float.MAX_VALUE;
		
		for (int i = 0; i < p1.length; ++i)
		{
			_tmp1.set(p1[(i + 1) % p1.length]).subtract(p1[i]).normalize();
			computeNonCollisionVector(p1, p2, _tmp1, _tmp2);
			
			float len = _tmp2.lengthSquared();
			if (len < minLength)
			{
				nonCollision.set(_tmp2);
				minLength = len;
			}
		}
		
		for (int i = 0; i < p2.length; ++i)
		{
			_tmp1.set(p2[(i + 1) % p2.length]).subtract(p2[i]).normalize();			
			computeNonCollisionVector(p1, p2, _tmp1, _tmp2);
			
			float len = _tmp2.lengthSquared();
			if (len < minLength)
			{
				nonCollision.set(_tmp2);
				minLength = len;
			}
		}
	}
}
