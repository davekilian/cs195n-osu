package dkilian.andy;

/**
 * Interface for an object that can be allocated once and then reused. For objects
 * that are to be grouped into a single Pool, to be 'allocated' and 'freed' as necessary,
 * without requiring any GC activity
 * 
 * @author dkilian
 */
public interface Poolable 
{
	/** Gets a value indicating whether this object is allocated / in use */
	public boolean isAlive();
	
	/** 
	 * Initializes this object and marks it as alive. If the object was already alive when 
	 * alloc() was called, this method should be a no-op
	 */
	public void alloc();
	
	/**
	 * Frees resources in use by this object and marks this object as no longer alive. If the
	 * object wasn't already alive when free() was called, this method should be a no-op
	 */
	public void free();
}
