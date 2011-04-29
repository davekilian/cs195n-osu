package dkilian.andy;

import java.util.ArrayList;

/**
 * Interpolates the scale factors of one or more sprites over time
 * 
 * @author dkilian
 */
public class ScaleTween extends Tween
{
	/** The final horizontal scale at the end of this tween */
	private float _x;
	/** The final vertical scale at the end of this tween */
	private float _y;
	
	/**
	 * Creates a new scale tween
	 */
	public ScaleTween()
	{
		super();
		
		_x = 1;
		_y = 1;
	}
	
	/**
	 * Creates a new scale tween
	 * @param x The horizontal scale at the end of this tween
	 * @param y The vertical scale at the end of this tween
	 */
	public ScaleTween(float x, float y)
	{
		super();
		
		_x = x;
		_y = y;
	}
	
	/**
	 * Creates a new scale tween
	 * @param targets The sprites to apply this tween to
	 */
	public ScaleTween(ArrayList<Sprite> targets)
	{
		super(targets);
		
		_x = 1;
		_y = 1;
	}
	
	/**
	 * Creates a new scale tween
	 * @param x The horizontal scale at the end of this tween
	 * @param y The vertical scale at the end of this tween
	 * @param targets The sprites to apply this tween to
	 */
	public ScaleTween(float x, float y, ArrayList<Sprite> targets)
	{
		super(targets);
		
		_x = x;
		_y = y;
	}
	
	/** Gets the horizontal scale at the end of this tween */
	public float getScaleX()
	{
		return _x;
	}
	
	/** Sets the horizontal scale at the end of this tween */
	public void setScaleX(float x)
	{
		_x = x;
	}
	
	/** Gets the vertical scale at the end of this tween */
	public float getScaleY()
	{
		return _y;
	}
	
	/** Sets the vertical scale at the end of this tween */
	public void setScaleY(float y)
	{
		_y = y;
	}
	
	/** Sets the horizontal and vertial scales at the end of this tween */
	public void setScale(float x, float y)
	{
		_x = x;
		_y = y;
	}

	/** Incrementally applies this tween to the target sprites */
	@Override
	protected void apply(float dt)
	{
		float dx = (_x / _duration) * dt;
		float dy = (_y / _duration) * dt;
		
		for (int i = 0; i < _targets.size(); ++i)
		{
			Sprite target = _targets.get(i);
			target.getTransform().postScale(dx, dy);
		}
	}
}
