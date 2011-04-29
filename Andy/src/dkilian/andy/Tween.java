package dkilian.andy;

import java.util.ArrayList;

/**
 * Common interface for classes that perform
 * transformations on Sprites over time using
 * a linear interpolation function
 * 
 * @author dkilian
 */
public abstract class Tween 
{
	/** The targets affected by this tween */
	protected ArrayList<Sprite> _targets;
	/** The length of this animation in partial seconds */
	protected float _duration;
	/** The position of this animation, in partial seconds */
	private float _time;
	/** This animation's looping behavior */
	private Loop _loop;
	/** Whether or not this animation is playing */
	boolean _playing;
	/** If loop is Loop.BackAndForth, whether or not the animation is in the backward phase */
	boolean _goingBack;
	
	/** Creates a new tween */
	public Tween()
	{
		_targets = new ArrayList<Sprite>();
		_duration = 0;
		_time = 0;
		_loop = Loop.None;
		_playing = false;
		_goingBack = false;
	}
	
	/** Creates a new tween affecting the specified targets */
	public Tween(ArrayList<Sprite> targets)
	{
		_targets = new ArrayList<Sprite>();
		_duration = 0;
		_time = 0;
		_loop = Loop.None;
		_playing = false;
		_goingBack = false;
		
		for (Sprite s : targets)
			addTarget(s);
	}
	
	/** Incrementally applies this tween to this._targets */
	protected abstract void apply(float dt);
	
	/** Adds a sprite to the list of sprites this tween transforms over time */
	public void addTarget(Sprite target)
	{
		_targets.add(target);
	}
	
	/** Removes a sprite fromthe list of sprites this tween tranforms over time */
	public void removeTarget(Sprite target)
	{
		_targets.remove(target);
	}
	
	/** Gets the list of sprites this tween acts upon */
	public ArrayList<Sprite> getTargets()
	{
		return _targets;
	}
	
	/** Gets the duration of this animation, in partial seconds */
	public float getDuration()
	{
		return _duration;
	}
	
	/** Sets the duration of this animation, in partial seconds */
	public void setDuration(float duration)
	{
		_duration = duration;
	}
	
	/** Gets the current position of the animation, in partial seconds */
	public float getTime()
	{
		return _time;
	}
	
	/** Sets the current position of the animation, in partial seconds */
	public void setTime(float time)
	{
		_time = time;
	}
	
	/** Gets the looping behavior of this tween */
	public Loop getLoop()
	{
		return _loop;
	}
	
	/** Sets the looping behavior of this tween */
	public void setLoop(Loop loop)
	{
		_loop = loop;
	}
	
	/** Gets a value indicating whether this tween is playing */
	public boolean isPlaying()
	{
		return _playing;
	}
	
	/** Begins this animation */
	public void start()
	{
		_playing = true;
	}
	
	/** Pauses or resumes this animation */
	public void pause()
	{
		_playing = !_playing;
	}
	
	/** Stops this animation and resets the position to the beginning */
	public void stop()
	{
		_playing = false;
		reset();
	}
	
	/** Resets this animation */
	public void reset()
	{
		apply(-_time);
		_time = 0;
	}
	
	/** Updates this animation */
	public void update(float dt)
	{
		if (_playing)
		{
			float t0 = _time;

			if (_loop == Loop.None)
			{
				_time += dt;
				if (_time >= _duration)
				{
					_time = _duration;
					_playing = false;
				}
			}
			else if (_loop == Loop.Repeat)
			{
				_time = (_time + dt) % _duration;
			}
			else if (_loop == Loop.BackAndForth)
			{
				if (_goingBack)
				{
					_time -= dt;
					if (_time < 0)
					{
						_time *= -1;
						_goingBack = false;
					}
				}
				else
				{
					_time += dt;
					if (_time > _duration)
					{
						_time = _duration - (_time - _duration);
						_goingBack = true;
					}
				}
			}
			
			apply(_time - t0);
		}
	}
}
