package osu.controls;

import android.graphics.Rect;
import dkilian.andy.Kernel;

public class Spinner implements Control
{
	private float _x;
	private float _y;
	private float _tbeg;
	private float _tend;
	private Rect _bounds;
	
	public Spinner()
	{
		_x = 0.f;
		_y = 0.f;
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
	}

	@Override
	public float getX() 
	{
		return _x;
	}

	@Override
	public void setX(float x) 
	{		
		_x = x;
	}

	@Override
	public float getY() 
	{
		return _y;
	}

	@Override
	public void setY(float y) 
	{
		_y = y;
	}

	@Override
	public float getStartTime() 
	{
		return _tbeg;
	}

	@Override
	public void setStartTime(float t) 
	{
		_tbeg = t;
	}

	@Override
	public float getEndTime() 
	{
		return _tend;
	}

	@Override
	public void setEndTime(float t) 
	{
		_tend = t;
	}

	@Override
	public boolean isVisible(float t) 
	{
		return t >= _tbeg && t <= _tend;
	}

	@Override
	public Rect getHitbox() 
	{
		return _bounds;
	}

	@Override
	public void interact(float x, float y) 
	{
	}

	@Override
	public void update(Kernel kernel, float dt) 
	{
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
	}
}
