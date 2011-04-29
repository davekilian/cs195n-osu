package dkilian.andy;

import java.util.HashMap;

/**
 * Keeps track of volume levels for different types of audio
 * (e.g. "dialogue", "sfx", "music")
 * 
 * @author dkilian
 */
public class VolumeRegistry 
{
	/** Maps volume classes to volume levels */
	private HashMap<String, Float> _volumes;
	
	/** Creates a new volume registry */
	public VolumeRegistry()
	{
		_volumes = new HashMap<String, Float>();
	}
	
	/**
	 * Gets the volume for a particular class of sounds (e.g. "dialogue", "sfx"),
	 * where a value of zero represents full attenuation (silence) and a value of
	 * one represents no attenuation (the original audio file)
	 * @param volumeClass The class of sounds to retrieve the volume for
	 */
	public final float getVolume(String volumeClass)
	{
		if (!_volumes.containsKey(volumeClass))
			return 1.0f;
		
		return _volumes.get(volumeClass);
	}
	
	/**
	 * Sets the volume for a particular class of sounds (e.g. "dialogue", "sfx"),
	 * where a value of zero represents full attenuation (silence) and a value of
	 * one represents no attenuation (the original audio file)
	 * @param volumeClass The class of sounds to set the volume of
	 * @param volume The volume to set, where 0 = silence and 1 = the original volume
	 * of the sound
	 */
	public final void setVolume(String volumeClass, float volume)
	{
		_volumes.put(volumeClass, volume);
	}
}
