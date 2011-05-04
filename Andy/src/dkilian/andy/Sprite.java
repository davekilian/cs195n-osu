package dkilian.andy;

import android.graphics.Rect;

/**
 * Common interface for 2D objects that can be arbitrarily transformed and rendered
 * @author dkilian
 */
public interface Sprite 
{
	/** Gets the translation between this sprite and the origin, in virtual coordinates */
	Vector2 getTranslation();
	
	/** Sets the translation between this sprite and the origin, in virtual coordinates */
	void setTranslation(Vector2 t);
	
	/** Gets the world-space rotation of this sprite around its origin, in degrees */
	float getRotation();

	/** Sets the world-space rotation of this sprite around its origin, in degrees */
	void setRotation(float rot);
	
	/** Gets the scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	Vector2 getScale();

	/** Sets the scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	void setScale(Vector2 s);
	
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
