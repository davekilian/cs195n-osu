package dkilian.andy;

/**
 * Common interface for classes that can produce frames of a flipbook (frame-by-frame) animation
 * 
 * @author dkilian
 */
public interface Flipbook 
{
	/** Gets the number of frames in this flipbook animation */
	int getFrameCount();
	
	/** Gets the frame at the given index */
	Sprite getFrame(int i);
}
