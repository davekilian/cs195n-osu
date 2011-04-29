package dkilian.andy;

import java.util.ArrayList;

/**
 * Manages a pool of objects which
 * 
 * @author dkilian
 * @param <T> The type of object filling this pool
 */
public class Pool<T extends Poolable>
{
	/** The allocatable objects in this pool */
	private ArrayList<T> _data;
	
	/** Creates an empty pool */
	public Pool()
	{
		_data = new ArrayList<T>();
	}
	
	/** 
	 * Gets the maximum number of distinct objects that can be allocated in this pool 
	 * at any given point in time 
	 */
	public int getCapacity()
	{
		return _data.size();
	}
	
	/** Gets the number of objects in this pool that are currently allocated */
	public int getLiveObjectCount()
	{
		int c = 0;
		for (int i = 0; i < _data.size(); ++i)
			if (_data.get(i).isAlive())
				++c;
		return c;
	}
	
	/** Gets the number of objects in this pool that are not currently allocated */
	public int getFreeObjectCount()
	{
		int c = 0;
		for (int i = 0; i < _data.size(); ++i)
			if (!_data.get(i).isAlive())
				++c;
		return c;
	}
	
	/** Fills buf with each object in this pool that is currently allocated */
	public void getLiveObjects(ArrayList<T> buf)
	{
		for (int i = 0; i < _data.size(); ++i)
		{
			T t = _data.get(i);
			if (t.isAlive())
				buf.add(t);
		}
	}

	/** Fills buf with each object in this pool that is not currently allocated */
	public void getDeadObjects(ArrayList<T> buf)
	{
		for (int i = 0; i < _data.size(); ++i)
		{
			T t = _data.get(i);
			if (!t.isAlive())
				buf.add(t);
		}
	}
	
	/** Gets a reference to this pool's list of all objects, allocated or not */
	public ArrayList<T> getAll()
	{
		return _data;
	}
	
	/** Adds an object to this pool, increasing its capacity by one */
	public void add(T t)
	{
		_data.add(t);
	}
	
	/** Removes an object from this pool, decreasing its capacity by one */
	public void remove(T t)
	{
		_data.remove(t);
	}
	
	/** Removes all objects from this pool, setting its capacity to zero */
	public void clear()
	{
		_data.clear();
	}
	
	/** 
	 * Finds an object in this pool that is not currently allocated, allocates it,
	 * and returns it. If there are no free objects in this pool, null is returned 
	 * instead.
	 */
	public T alloc()
	{
		for (int i = 0; i < _data.size(); ++i)
		{
			T t = _data.get(i);
			if (!t.isAlive())
			{
				t.alloc();
				return t;
			}
		}
		
		return null;
	}
}
