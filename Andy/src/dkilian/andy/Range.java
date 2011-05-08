package dkilian.andy;

/**
 * Simple wrapper class for an inclusive range of values
 * 
 * @param <T> The type of value this range object contains
 * @author dkilian
 */
public class Range<T>
{
	/** The inclusive minimum bound of this range */
	private T _min;
	/** The inclusive maximum bound of this range */
	private T _max;
	
	/**
	 * Creates a new range
	 * @param min The inclusive minimum bound of this range
	 * @param max The inclusive maximum bound of this range
	 */
	public Range(T min, T max)
	{
		_min = min;
		_max = max;
	}
	
	/** Gets the inclusive minimum bound of this range */
	public T getMin()
	{
		return _min;
	}
	
	/** Sets the inclusive minimum bound of this range */
	public void setMin(T min)
	{
		_min = min;
	}
	
	/** Gets the inclusive maximum bound of this range */
	public T getMax()
	{
		return _max;
	}
	
	/** Sets the inclusive maximum bound of this range */
	public void setMax(T max)
	{
		_max = max;
	}
	
	/**
	 * Sets the bounds of this range
	 * @param min The minimum bound of this range
	 * @param max The maximum bound of this range
	 */
	public void set(T min, T max)
	{
		setMin(min);
		setMax(max);
	}
	
	/** Sets this range's values to the values in the specified range */
	public void copyFrom(Range<T> other)
	{
		_min = other._min;
		_max = other._max;
	}
	
	/** Sets the specified range's values to the values in this range */
	public void copyTo(Range<T> other)
	{
		other._min = _min;
		other._max = _max;
	}
}
