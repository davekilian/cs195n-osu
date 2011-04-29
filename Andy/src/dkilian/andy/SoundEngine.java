package dkilian.andy;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Loads sound effects and manages playback of multiple simultaneous sounds
 * 
 * @author dkilian
 */
public class SoundEngine 
{
	/** The default maximum number of sounds a sound engine supports playing back simultaneously */
	public static final int DEFAULT_MAX_SOUNDS = 16;
	
	/** The android sound pool containing all the loaded sounds */
	private SoundPool _pool;
	/** Maps types of audio (e.g. "dialogue", "sfx") to the relative volume for that type of audio */
	private VolumeRegistry _volume;
	
	/**
	 * Creates a new sound engine that can play a maximum of DEFAULT_MAX_SOUNDS
	 * instances of sound effects simultaneously
	 */
	public SoundEngine()
	{
		_pool = new SoundPool(DEFAULT_MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
		_volume = new VolumeRegistry();
	}
	
	/**
	 * Creates a new sound engine
	 * @param maxSounds The maximum number of sound effect instances this engine can play simultaneously
	 */
	public SoundEngine(int maxSounds)
	{
		_pool = new SoundPool(maxSounds, AudioManager.STREAM_MUSIC, 0);
		_volume = new VolumeRegistry();
	}
	
	/** Releases resources in use by this engine */
	protected void finalize() throws Throwable
	{
		try
		{
			release();
		}
		finally
		{
			super.finalize();
		}
	}
	
	/**
	 * Loads an audio clip from application resources
	 * @param context The resource context containing the sound
	 * @param resource The resource ID of the sound
	 * @return A sound effect than can be used to play new instances of the sound using this engine
	 */
	public final SoundEffect load(Context context, int resource)
	{
		return new SoundEffect(this, _pool.load(context, resource, 0));
	}
	
	/**
	 * Loads an audio clip from application resources
	 * @param kernel The executing kernel
	 * @param resource The resource ID of the sound
	 * @return A sound effect that can be used to play new instances of the sound using this engine
	 */
	public final SoundEffect load(Kernel kernel, int resource)
	{
		return load(kernel.getActivity(), resource);
	}
	
	/**
	 * Loads an audio clip from a file
	 * @param path The URI to the file
	 * @return A sound effect that can be used to play new instances of the sound using this engine
	 */
	public final SoundEffect load(String path)
	{
		return new SoundEffect(this, _pool.load(path, 0));
	}
	
	/**
	 * Loads an audio clip from application resources
	 * @param context The resource context containing the sound
	 * @param resource The resource ID of the sound
	 * @param volumeClass The volume class of this sound. The sound effect instances spawned will use the volume set for
	 * 					  this class by this engine's volume registry 
	 * @return A sound effect than can be used to play new instances of the sound using this engine
	 */
	public final SoundEffect load(Context context, int resource, String volumeClass)
	{
		return new SoundEffect(this, _pool.load(context, resource, 0), volumeClass);
	}

	/**
	 * Loads an audio clip from application resources
	 * @param kernel The executing kernel
	 * @param resource The resource ID of the sound
	 * @param volumeClass The volume class of this sound. The sound effect instances spawned will use the volume set for
	 * 					  this class by this engine's volume registry 
	 * @return A sound effect that can be used to play new instances of the sound using this engine
	 */
	public final SoundEffect load(Kernel kernel, int resource, String volumeClass)
	{
		return load(kernel.getActivity(), resource, volumeClass);
	}

	/**
	 * Loads an audio clip from a file
	 * @param path The URI to the file
	 * @param volumeClass The volume class of this sound. The sound effect instances spawned will use the volume set for
	 * 					  this class by this engine's volume registry 
	 * @return A sound effect that can be used to play new instances of the sound using this engine
	 */
	public final SoundEffect load(String path, String volumeClass)
	{
		return new SoundEffect(this, _pool.load(path, 0), volumeClass);
	}
	
	/** Gets this engine's mapping from volume class to relative volume for audio in that volume class */
	public final VolumeRegistry getVolumeRegistry()
	{
		return _volume;
	}

	/** Sets this engine's mapping from volume class to relative volume for audio in that volume class */
	public final void setVolumeRegistry(VolumeRegistry vr)
	{
		_volume = vr;
	}
	
	/** 
	 * Unloads all audio clips in use by this engine.
	 * 
	 * Subsequent uses of SoundEffect and SoundClip objects related to this engine may continue to be used without
	 * error, but no sound will be played. 
	 */
	public final void release()
	{
		_pool.release();
		_pool = null;
	}
	
	/** Pauses all playing sounds */
	public final void pauseAll()
	{
		_pool.autoPause();
	}
	
	/** Resumes playing all sounds stopped by a pauseAll() call */
	public final void resumeAll()
	{
		_pool.autoResume();
	}
	
	/** Gets the android SoundPool object being used by this engine to store and play loaded sounds */
	public final SoundPool getPool()
	{
		return _pool;
	}
}
