package dkilian.andy;

/**
 * Common interface for collision primitives
 * 
 * @author dkilian
 */
public interface CollisionShape 
{
	/** Gets the translation vector between this primitive's origin and the world origin */
	public Vector2 getTranslation();

	/** Sets the translation vector between this primitive's origin and the world origin */
	public void setTranslation(Vector2 t);
	
	/** Gets the points in this polygon in world space, or returns null if not applicable (e.g. circles) */
	public Vector2[] getPoints();
	
	/**
	 * Checks whether a point intersects with this primitive, inclusively
	 * @param pt The hit-point to test
	 * @return True if the point intersects this primitive (inclusively) or false otherwise
	 */
	public boolean intersects(Vector2 pt);
	
	/**
	 * Checks whether two shapes intersect
	 * @param other The other shape to test of intersection
	 * @return True if this and the other shape intersect, false otherwise
	 */
	public boolean intersects(CollisionShape other);
	
	/**
	 * Computes the minimum vector of non-collision between this and another shape
	 * @param other The other shape
	 * @param nonCollision Receives the minimum vector needed to separate this and the other shape, or (0, 0) if the shapes don't intersect
	 */
	public void computeNonCollisionVector(CollisionShape other, Vector2 nonCollision);
}
