package dkilian.andy;

import java.util.ArrayList;

/**
 * Interpolates the position of one or more sprites over time
 * 
 * @author dkilian
 */
public class MotionTween extends Tween
{
	/** The horizontal translation of sprites at the end of the tween */
	private float _x;
	/** The vertical translation of sprites at the end of the tween */
	private float _y;
	
	/**
	 * Creates a new motion tween
	 */
	public MotionTween()
	{
		super();
		
		_x = 0;
		_y = 0;
	}
	
	/**
	 * Creates a new motion tween
	 * @param x The horizontal translation of sprites at the end of this tween
	 * @param y The vertical translation of sprites at the end of this tween
	 */
	public MotionTween(float x, float y)
	{
		super();
		
		_x = x;
		_y = y;
	}
	
	/**
	 * Creates a new motion tween
	 * @param targets The sprites affected by this tween
	 */
	public MotionTween(ArrayList<Sprite> targets)
	{
		super(targets);
		
		_x = 0;
		_y = 0;
	}
	
	/**
	 * Creates a new motion tween
	 * @param x The horizontal translation of sprites at the end of this tween
	 * @param y The vertical translation of sprites at the end of this tween
	 * @param targets The sprites affected by this tween
	 */
	public MotionTween(float x, float y, ArrayList<Sprite> targets)
	{
		super(targets);
		
		_x = x;
		_y = y;
	}
	
	/** Gets the horizontal translation of sprites at the end of this tween */
	public float getOffsetX()
	{
		return _x;
	}
	
	/** Sets the horizontal translation of sprites at the end of this tween */
	public void setOffsetX(float x)
	{
		_x = x;
	}
	
	/** Gets the vertical translation of sprites at the end of this tween */
	public float getOffsetY()
	{
		return _y;
	}
	
	/** Sets the vertical translation of sprites at the end of this tween */
	public void setOffsetY(float y)
	{
		_y = y;
	}
	
	/** Sets the translation of sprites at the end of this tween */
	public void setOffset(float x, float y)
	{
		setOffsetX(x);
		setOffsetY(y);
	}

	/** Applies a translation to each of the target sprites */
	@Override
	protected void apply(float dt) 
	{
		float dx = (_x / _duration) * dt;
		float dy = (_y / _duration) * dt;

		for (int i = 0; i < _targets.size(); ++i)
		{
			Sprite target = _targets.get(i);
			target.getTranslation().x += dx;
			target.getTranslation().y += dy;
		}
	}
}
