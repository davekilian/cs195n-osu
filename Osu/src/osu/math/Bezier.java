package osu.math;

import android.graphics.PointF;

/**
 * A simple Bezier curve evaluator
 * @author dkilian
 */
public class Bezier 
{
	/** A preallocated temporary storage buffer, for GC performance */
	private static float[] buf;
	
	/**
	 * Evaluates an array of 2D points, arranged [x1 y1 x2 y2 ... xn yn]
	 * @param points The points to interpolate
	 * @param t The point along the curve to evaluate, between 0 and 1
	 * @param target Receives the X and Y coordinates of the evaluated path
	 */
	public static void evaluate2d(float[] points, float t, PointF target)
	{		
		if (buf == null || buf.length < points.length)
			buf = new float[points.length];
		
		for (int i = 0; i < buf.length; ++i)
			buf[i] = points[i];
		
		float oneMinusT = 1.f - t;
		int degree = points.length / 2 - 1;
		
		for (int i = 0; i < degree; ++i)
		{
			for (int j = 0; j < degree - i; ++j)
			{
				buf[2*j+0] = oneMinusT * buf[2*j+0] + t * buf[2*(j+1)+0];
				buf[2*j+1] = oneMinusT * buf[2*j+1] + t * buf[2*(j+1)+1];
			}
		}
		
		target.x = buf[0];
		target.y = buf[1];
	}
}
