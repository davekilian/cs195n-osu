package dkilian.andy;

import java.util.ArrayList;

/**
 * Interpolates the rotation of one or more sprites over time
 * 
 * @author dkilian
 */
public class RotationTween extends Tween
{
	/** The rotation of the target sprites at the end of the tween, in degrees */
	private float _angle;
	
	/** 
	 * Creates a new rotation tween 
	 */
	public RotationTween()
	{
		super();
		
		_angle = 0;
	}
	
	/**
	 * Creates a new rotation tween
	 * @param angle The rotation applied to the target sprites by the end of the tween, in degrees
	 */
	public RotationTween(float angle)
	{
		super();
		
		_angle = angle;
	}
	
	/**
	 * Creates a new rotation tween
	 * @param targets The sprites affected by this rotation tween
	 */
	public RotationTween(ArrayList<Sprite> targets)
	{
		super(targets);
		
		_angle = 0;
	}
	
	/**
	 * Creates a new rotation tween
	 * @param targets The sprites affected by this sprite
	 * @param angle The rotation applied to the target sprites by the end of the tween, in degrees
	 */
	public RotationTween(ArrayList<Sprite> targets, float angle)
	{
		super(targets);
		
		_angle = angle;
	}
	
	/** Gets the rotation applied to the target sprites by the end of the tween, in degrees */
	public float getAngle()
	{
		return _angle;
	}

	/** Sets the rotation applied to the target sprites by the end of the tween, in degrees */
	public void setAngle(float angle)
	{
		_angle = angle;
	}
	
	/** Incrementally applies a rotation to the target sprites */
	@Override
	protected void apply(float dt) 
	{
		float dtheta = (_angle / _duration) * dt;
		
		for (int i = 0; i < _targets.size(); ++i)
		{
			Sprite target = _targets.get(i);
			target.setRotation(target.getRotation() + dtheta);
		}
	}
}
