package dkilian.andy;

/**
 * Controls an instance of a SoundEffect being played back
 * 
 * @author dkilian
 */
public class SoundEffectInstance 
{
	/** The sound engine responsible for playing this instance */
	private SoundEngine _engine;
	/** The ID of this instance in the sound engine */
	private int _streamID;
	/** The volume class of the sound effect this sound is an instance of */
	private String _volumeClass;
	/** The volume of this sound instance */
	private float _volume;
	/** Whether or not this instance is currently playing */
	private boolean _playing;
	/** Whether or not this instance is looping */
	private boolean _looping;
	/** The priority of this instance */
	private int _priority;
	
	/**
	 * Creates a new sound instance. See SoundEffect.play()/loop(), and SoundEngine.load()
	 * 
	 * @param engine The sound engine playing this instance
	 * @param looping Whether or not this instance started out looping
	 * @param priority The priority of this instance, where 0 is max priority. 
	 *                 Low-priority streams are stopped when the engine is playing too many simultaneous streams.
	 * @param streamID The ID of this instance in the sound engine
	 */
	public SoundEffectInstance(SoundEngine engine, boolean looping, int priority, int streamID)
	{
		_engine = engine;
		_streamID = streamID;
		_volumeClass = "default";
		_volume = _engine.getVolumeRegistry().getVolume(_volumeClass);
		_playing = true;
		_looping = looping;
		_priority = priority;
	}

	/**
	 * Creates a new sound instance. See SoundEffect.play()/loop(), and SoundEngine.load()
	 * 
	 * @param engine The sound engine playing this instance
	 * @param looping Whether or not this instance started out looping
	 * @param priority The priority of this instance, where 0 is max priority. 
	 *                 Low-priority streams are stopped when the engine is playing too many simultaneous streams.
	 * @param streamID The ID of this instance in the sound engine
	 * @param volumeClass The type of audio this instance is; used to auto-determine the default volume level.
	 */
	public SoundEffectInstance(SoundEngine engine, boolean looping, int priority, int streamID, String volumeClass)
	{
		_engine = engine;
		_streamID = streamID;
		_volumeClass = volumeClass;
		_volume = _engine.getVolumeRegistry().getVolume(_volumeClass);
		_playing = true;
		_looping = looping;
		_priority = priority;
	}
	
	/** Gets the sound engine playing this sound */
	public final SoundEngine getEngine()
	{
		return _engine;
	}
	
	/** 
	 * Gets a value indicating whether this sound is playing.
	 * 
	 * Note that this value will continue to return true even if the sound instance is actually no longer playing.
	 * It does, however, keep track of whether or not this instance has been paused or stopped.
	 */
	public final boolean isPlaying()
	{
		return _playing;
	}
	
	/** Gets a value indicating whether this sound is looping indefinitely */
	public final boolean isLooping()
	{
		return _looping;
	}
	
	/** Sets a value indicating whether this sound loops indefinitely */
	public final void setLooping(boolean looping)
	{
		if (looping != _looping)
		{
			_looping = looping;
			_engine.getPool().setLoop(_streamID, looping ? -1 : 0);
		}
	}
	
	/**
	 * Gets this instance's priority, where 0 is the lowest priority. The lowest priority instance
	 * is the one stopped when the engine is asked to play too many sounds simultaneously.
	 */
	public final int getPriority()
	{
		return _priority;
	}

	/**
	 * Sets this instance's priority, where 0 is the lowest priority. The lowest priority instance
	 * is the one stopped when the engine is asked to play too many sounds simultaneously.
	 */
	public final void setPriority(int priority)
	{
		_priority = priority;
		getEngine().getPool().setPriority(_streamID, priority);
	}
	
	/** Gets this instance's volume class */
	public final String getVolumeClass()
	{
		return _volumeClass;
	}
	
	/** 
	 * Sets this instance's volume class, and sets this instance's volume to the current value of that class
	 * in the engine's volume registry.
	 */
	public final void setVolumeClass(String volumeClass)
	{
		_volumeClass = volumeClass;
		setVolume(getEngine().getVolumeRegistry().getVolume(volumeClass));
	}
	
	/** Gets this sound's relative volume */
	public final float getVolume()
	{
		return _volume;
	}

	/** Sets this sound's relative volume */
	public final void setVolume(float volume)
	{
		_volume = volume;
		getEngine().getPool().setVolume(_streamID, volume, volume);
	}
	
	/** Pauses or resumes this sound */
	public final void pause()
	{
		_playing = !_playing;
		if (!_playing)
			getEngine().getPool().pause(_streamID);
		else
			getEngine().getPool().resume(_streamID);
	}
	
	/** Stops this instance permanently. Any further action on this SoundEffectInstance will not throw an error, but will also have no effect */
	public final void stop()
	{
		_playing = false;
		getEngine().getPool().stop(_streamID);
	}
}
