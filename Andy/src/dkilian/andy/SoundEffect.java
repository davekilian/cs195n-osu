package dkilian.andy;

/**
 * Represents an instantiable (i.e. playable) sound
 * 
 * @author dkilian
 */
public class SoundEffect 
{
	/** The sound engine that loaded the sound this object represents */
	private SoundEngine _engine;
	/** The ID of the sound this object represents in the sound engine */
	private int _soundID;
	/** The volume class of this sound */
	private String _volumeClass;
	/** Whether or not this effect's sound is still loaded into memory */
	private boolean _loaded;
	
	/**
	 * Creates a new SoundEffect. Should never be called directly; use
	 * SoundEngine.load() instead.
	 * 
	 * @param engine The sound engine that spawns this sound effect
	 * @param soundID The ID of this object's sound in the sound engine
	 */
	public SoundEffect(SoundEngine engine, int soundID)
	{
		_engine = engine;
		_soundID = soundID;
		_volumeClass = "default";
		_loaded = true;
	}

	/**
	 * Creates a new SoundEffect. Should never be called directly; use
	 * SoundEngine.load() instead.
	 * 
	 * @param engine The sound engine that spawns this sound effect
	 * @param soundID The ID of this object's sound in the sound engine
	 * @param volumeClass The volume class of this sound
	 */
	public SoundEffect(SoundEngine engine, int soundID, String volumeClass)
	{
		_engine = engine;
		_soundID = soundID;
		_volumeClass = volumeClass;
		_loaded = true;
	}
	
	/** Gets the sound engine that spawned this sound effect */
	public final SoundEngine getSoundEngine()
	{
		return _engine;
	}
	
	/** Gets the volume class of this sound effect */
	public final String getVolumeClass()
	{
		return _volumeClass;
	}
	
	/** Sets the volume class of this sound effect */
	public final void setVolumeClass(String volumeClass)
	{
		_volumeClass = volumeClass;
	}
	
	/** Gets a value indicating whether this sound effect is still loaded into memory */
	public final boolean isLoaded()
	{
		return _loaded;
	}
	
	/**
	 * Unloads this sound effect. All SoundClips playing this sound will continue to work, 
	 * but will no longer be playing this sound.
	 */
	public final void unload()
	{
		if (_loaded)
		{
			_engine.getPool().unload(_soundID);
			_loaded = false;
		}
	}
	
	/** 
	 * Plays this sound effect once 
	 * @return A sound clip object that can be used to control playback of this sound
	 */
	public final SoundEffectInstance play()
	{
		return play(Integer.MAX_VALUE / 2);
	}
	
	/**
	 * Plays this sound effect once
	 * @param priority The priority of this sound effect, where 0 is the lowest. The lowest priority sound
	 * 				   is the one stopped when the engine is playing too many sounds simultaneously. 
	 * @return A sound clip object that can be used to control playback of this sound
	 */
	public final SoundEffectInstance play(int priority)
	{
		float volume = _engine.getVolumeRegistry().getVolume(_volumeClass);
		return new SoundEffectInstance(_engine, false, priority, _engine.getPool().play(_soundID, volume, volume, priority, 0, 1.0f));
	}
	
	/**
	 * Loops this sound effect indefinitely
	 * @return A sound clip object that can be used to control playback of this sound
	 */
	public final SoundEffectInstance loop()
	{
		return loop(Integer.MAX_VALUE / 2);
	}
	
	/**
	 * Loops this sound effect indefinitely
	 * @param priority The priority of this sound effect, where 0 is the lowest. The lowest priority sound
	 * 				   is the one stopped when the engine is playing too many sounds simultaneously. 
	 * @return A sound clip object that can be used to control playback of this sound
	 */
	public final SoundEffectInstance loop(int priority)
	{
		float volume = _engine.getVolumeRegistry().getVolume(_volumeClass);
		return new SoundEffectInstance(_engine, true, priority, _engine.getPool().play(_soundID, volume, volume, priority, -1, 1.0f));
	}
}
