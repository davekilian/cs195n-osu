package dkilian.andy;

/**
 * Plays a frame-by-frame animation from a Flipbook
 * 
 * @author dkilian
 */
public class FlipbookAnimation 
{
	/** True if the animation is playing, false if it is paused */
	private boolean _playing;
	/** True if loop is BackAndForth and the animation is currently in the backward phase */
	private boolean _goingBack;
	/** The flipbook containing the frames to play */
	private Flipbook _flipbook;
	/** The sprite used to render this animation */
	private FlipbookAnimationSprite _sprite;
	/** The current position of the animation, in seconds */
	private float _time;
	/** The speed of the animation, in frames per second */
	private float _framerate;
	/** The looping behavior of this animation */
	private Loop _loop;
	
	/**
	 * Creates a new flipbook animation
	 * @param flipbook The flipbook this animation plays
	 */
	public FlipbookAnimation(Flipbook flipbook)
	{
		_playing = false;
		_goingBack = false;
		_time = 0;
		_sprite = new FlipbookAnimationSprite(this);
		_framerate = 30;
		_loop = Loop.None;
		_flipbook = flipbook;
	}
	
	/**
	 * Creates a new flipbook animation
	 * @param framerate The speed of the animation, in frames per second
	 * @param loop The looping behavior of this animation
	 * @param flipbook The flipbook this animation plays
	 */
	public FlipbookAnimation(float framerate, Loop loop, Flipbook flipbook)
	{
		_playing = false;
		_goingBack = false;
		_time = 0;
		_sprite = new FlipbookAnimationSprite(this);
		_framerate = framerate;
		_loop = loop;
		_flipbook = flipbook;
	}
	
	/** Gets the flipbook this animation plays */
	public final Flipbook getFlipbook()
	{
		return _flipbook;
	}
	
	/** Sets the flipbook this animation plays */
	public final void setFlipbook(Flipbook flipbook)
	{
		_flipbook = flipbook;
	}
	
	/** Gets the current position of the animation, in partial seconds */
	public final float getTime()
	{
		return _time;
	}
	
	/** Sets the current position of the animation, in partial seconds */
	public final void setTime(float time)
	{
		_time = time;
	}
	
	/** Gets the index of the currently visible frame of the animation */
	public final int getFrame()
	{
		return (int)Math.min(Math.max(_time * _framerate, 0), _flipbook.getFrameCount() - 1);
	}
	
	/** Sets the index of the currently visible frame of the animation */
	public final void setFrame(int frame)
	{
		_time = Math.min(Math.max(frame / _framerate, 0), _flipbook.getFrameCount() / _framerate);
	}
	
	/** Gets the speed of this animation, in frames per second */
	public final float getFramerate()
	{
		return _framerate;
	}
	
	/** Sets the speed of this animation, in frames per second */
	public final void setFramerate(float framerate)
	{
		_framerate = framerate;
	}
	
	/** Gets the looping behavior of this animation */
	public final Loop getLoop()
	{
		return _loop;
	}
	
	/** Sets the looping behavior of this animation */
	public final void setLoop(Loop loop)
	{
		_loop = loop;
	}
	
	/** Gets the sprite to use to render this animation */
	public final Sprite getSprite()
	{
		return _sprite;
	}
	
	/** Gets a value indicating whether this animation is playing */
	public final boolean isPlaying()
	{
		return _playing;
	}
	
	/** Begins playing this animation */
	public final void play()
	{
		_playing = true;
	}
	
	/** Pauses or resume this animation */
	public final void pause()
	{
		_playing = !_playing;
	}
	
	/** Pauses the animation and returns to the first frame */
	public final void stop()
	{
		_playing = false;
		reset();
	}
	
	/** Returns this animation to the first frame */
	public final void reset()
	{
		_time = 0;
	}
	
	/** Updates the current position of this animation */
	public final void update(float dt)
	{
		if (_playing)
		{
			if (_loop == Loop.None)
			{
				_time += dt;
				if (_time * _framerate >= _flipbook.getFrameCount())
				{
					setFrame(_flipbook.getFrameCount() - 1);
					_playing = false;
				}
			}
			else if (_loop == Loop.Repeat)
			{
				_time += dt;
				while (_time * _framerate >= _flipbook.getFrameCount())
					_time -= _flipbook.getFrameCount() / _framerate;
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
					float duration = _flipbook.getFrameCount() / _framerate;
					if (_time > duration)
					{
						_time = duration - (_time - duration);
						_goingBack = true;
					}
				}
			}
		}
	}
}
