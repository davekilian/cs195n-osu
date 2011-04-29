package dkilian.andy;

/**
 * Enumerates loop behaviors supported by Andy frame-by-frame and tween animations
 * 
 * @author dkilian
 */
public enum Loop 
{
	/** Play through; do not repeat */
	None,
	
	/** Play through, then go back to the first frame */
	Repeat,
	
	/** Play forward, then backward, then forward, ... */
	BackAndForth
}
