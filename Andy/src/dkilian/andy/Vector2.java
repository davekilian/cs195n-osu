package dkilian.andy;

import android.graphics.Matrix;
import android.util.FloatMath;

/**
 * A two-dimensional vector
 * 
 * @author dkilian
 */
public class Vector2 
{
	/** Gets the all-zeroes vector (0, 0) */
	public static Vector2 Zero()
	{
		return new Vector2(0.f, 0.f);
	}
	
	/** Gets the all-ones vector (1, 1) */
	public static Vector2 One()
	{
		return new Vector2(1.f, 1.f);
	}
	
	/** Gets the normal vector pointing down the X axis (1, 0) */
	public static Vector2 UnitX()
	{
		return new Vector2(1.f, 0.f);
	}
	
	/** Gets the normal vector pointing down the Y axis (0, 1) */
	public static Vector2 UnitY()
	{
		return new Vector2(0.f, 1.f);
	}
	
	/** Computes the length of a vector */
	public static float Length(Vector2 v)
	{
		return FloatMath.sqrt(v.x * v.x + v.y * v.y);
	}
	
	/** Computes the square of the length of a vector (less computationally expensive) */
	public static float LengthSquared(Vector2 v)
	{
		return v.x * v.x + v.y * v.y;
	}
	
	/** Computes the dot product between two vectors */
	public static float Dot(Vector2 v1, Vector2 v2)
	{
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	/** Computes the distance between two vectors */
	public static float Distance(Vector2 v1, Vector2 v2)
	{
		return FloatMath.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y));
	}
	
	/** Computes the square of the distance between two vectors (less computationally expensive) */
	public static float DistanceSquared(Vector2 v1, Vector2 v2)
	{
		return (v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y);
	}
	
	/**
	 * Computes the Manhattan distance between two vectors, which is computationally less expensive
	 * to compute than the distance or distance squared. The Manhattan distance is the sum of the
	 * X and Y offsets, i.e. the distance one would have to travel from A to B by walking the
	 * streets of Manhattan
	 */
	public static float ManhattanDistance(Vector2 v1, Vector2 v2)
	{
		return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y);
	}
	
	/** Computes a vector with the same direction as the given vector and a length of 1 */
	public static Vector2 Normalize(Vector2 v)
	{
		return Multiply(v, 1.f / Length(v));
	}
	
	/** Reflects this vector about another vector */
	public static Vector2 Reflect(Vector2 v, Vector2 n)
	{
		return Subtract(v, Multiply(n, 2.f * Dot(v, n)));
	}
	
	/** Checks whether two vectors are equal, accounting for floating point error */
	public static boolean Equal(Vector2 v1, Vector2 v2)
	{
		return Math.abs(v1.x - v2.x) < 1e-6 &&
			   Math.abs(v1.y - v2.y) < 1e-6;
	}
	
	/** Checks whether two vectors are equal, accounting for floating point error */
	public static boolean NotEqual(Vector2 v1, Vector2 v2)
	{
		return !Equal(v1, v2);
	}
	
	/** Computes the sum of two vectors */
	public static Vector2 Add(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}
	
	/** Computes the distance between two vectors */
	public static Vector2 Subtract(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}
	
	/** Computes the negation of a vector */
	public static Vector2 Negate(Vector2 v)
	{
		return new Vector2(-v.x, -v.y);
	}
	
	/** Computes the product between a vector and a scalar */
	public static Vector2 Multiply(Vector2 v, float amount)
	{
		return new Vector2(amount * v.x, amount * v.y);
	}
	
	/** Computes the quotient of a vector and a scalar */
	public static Vector2 Divide(Vector2 v, float amount)
	{
		amount = 1.f / amount;
		return new Vector2(amount * v.x, amount * v.y);
	}
	
	/** Computes a linear interpolation between two vectors */
	public static Vector2 Lerp(Vector2 v1, Vector2 v2, float amount)
	{
		return new Vector2((1 - amount) * v1.x + amount * v2.x,
						   (1 - amount) * v1.y + amount * v2.y);
	}
	
	/** Creates a vector whose components are clamped to the specified values */
	public static Vector2 Clamp(Vector2 v, Vector2 min, Vector2 max)
	{
		return new Vector2(v.x < min.x ? min.x : v.x > max.x ? max.x : v.x,
						   v.y < min.y ? min.y : v.y > max.y ? max.y : v.y);
	}
	
	/** Creates a vector whose components are each the minimum of two vectors' corresponding component */
	public static Vector2 Min(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x < v2.x ? v1.x : v2.x,
						   v1.y < v2.y ? v1.y : v2.y);
	}
	
	/** Creates a vector whose components are each the maximum of two vectors' corresponding component */
	public static Vector2 Max(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x > v2.x ? v1.x : v2.x,
				           v1.y > v2.y ? v1.y : v2.y);
	}
	
	/** Transforms a vector by a matrix */
	public static Vector2 Transform(Vector2 v, Matrix m)
	{
		float[] p = { v.x, v.y };
		m.mapPoints(p);
		return new Vector2(p[0], p[1]);
	}
	
	/** This vector's X component */
	public float x;
	/** This vector's Y component */
	public float y;
	
	/** Creates the zeroes vector */
	public Vector2()
	{
		x = 0.f;
		y = 0.f;
	}
	
	/** Creates a vector with the specified components */
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/** Computes the length of this vector */
	public final float length()
	{
		return FloatMath.sqrt(x * x + y * y);
	}
	
	/** Computes the square of the length of this vector */
	public final float lengthSquared()
	{
		return x * x + y * y;
	}
	
	/** Computes the dot product between this and another vector */
	public final float dot(Vector2 other)
	{
		return x * other.x + y * other.y;
	}
	
	/** Computes the distance between this and another vector */
	public final float distanceTo(Vector2 other)
	{
		return FloatMath.sqrt((x - other.x) * (x - other.x) + (y - other.y ) * (y - other.y));
	}
	
	/** Computes the square of this distance between this and another vector */
	public final float distanceSquaredTo(Vector2 other)
	{
		return (x - other.x) * (x - other.x) + (y - other.y ) * (y - other.y);
	}
	
	/** Computes the Manhattan distance between this and another vector */
	public final float manhattanDistanceTo(Vector2 other)
	{
		return Math.abs(x - other.x) + Math.abs(y - other.y );
	}

	/** Computes a vector with length 1 in the same direction as this given vector */
	public final Vector2 normalize()
	{
		return multiply(1.f / length());
	}
	
	/** Computes this vector reflected about a normal vector */
	public final Vector2 reflect(Vector2 n)
	{
		return subtract(n.multiply(2.f * dot(n)));
	}
	
	/** Gets a value indicating whether this vector has the same components as another vector */
	@Override
	public final boolean equals(Object other)
	{
		if (other.getClass() != Vector2.class)
			return false;
		
		Vector2 o = (Vector2)other;
		return Math.abs(x - o.x) < 1e-6 && Math.abs(y - o.y) < 1e-6;
	}
	
	/** Computes the sum of this and another vector */
	public final Vector2 add(Vector2 other)
	{
		x += other.x;
		y += other.y;
		return this;
//		return new Vector2(x + other.x, y + other.y);
	}
	
	/** Computes the difference between this and another vector */
	public final Vector2 subtract(Vector2 other)
	{
		x -= other.x;
		y -= other.y;
		return this;
//		return new Vector2(x - other.x, y - other.y);
	}
	
	/** Computes the component-wise negation of this vector */
	public final Vector2 negate()
	{
		x *= -1.f;
		y *= -1.f;
		return this;
//		return new Vector2(-x, -y);
	}
	
	/** Computes this vector multiplied by a scalar */
	public final Vector2 multiply(float amount)
	{
		x *= amount;
		y *= amount;
		return this;
//		return new Vector2(amount * x, amount * y);
	}
	
	/** Computes this vector divided by a scalar */
	public final Vector2 divide(float amount)
	{	
		amount = 1.f / amount;
	
		x *= amount;
		y *= amount;
		return this;
		
//		return new Vector2(amount * x, amount * y);
	}
	
	/** Transforms this vector by a matrix-vector multiplication */
	public final Vector2 transform(Matrix m)
	{
		float[] p = { x, y };
		m.mapPoints(p);
		x = p[0];
		y = p[1];
		return this;
//		return new Vector2(p[0], p[1]);
	}
	
	/** Sets the components of this vector equal to the components of the other vector */
	public final Vector2 set(Vector2 other)
	{
		x = other.x;
		y = other.y;
		return this;
	}
}
