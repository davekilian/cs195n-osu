package dkilian.andy;

import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Common interface for 2D objects that can be arbitrarily transformed and rendered
 * @author dkilian
 */
public interface Sprite 
{
	/** Gets the matrix containing the concatenated transformations performed on this sprite */
	Matrix getTransform();
	
	/** Sets the matrix containing the concatenated transformations performed on this sprite */
	void setTransform(Matrix m);
	
	/** Gets this sprite's width, in pixels */
	int getWidth();
	
	/** Gets this sprite's height, in pixels */
	int getHeight();
	
	/** Gets a value indicating whether the sprite draws just the portion of itself inside the clip rect when drawn */
	boolean getClipRectEnabled();

	/** Sets a value indicating whether the sprite draws just the portion of itself inside the clip rect when drawn */
	void setClipRectEnabled(boolean b);
	
	/** Gets the portion of the sprite that is drawn when the clip rect is enabled */
	Rect getClipRect();
	
	/** Sets the portion of the sprite that is drawn when the clip rect is enabled */
	void setClipRect(Rect r);
	
	/** Draws this sprite */
	void draw(Kernel kernel);
}
