package dkilian.andy;

import android.graphics.Rect;

/**
 * Describes a region of a sprite sheet
 * 
 * @author dkilian
 */
public class SpriteSheetFrame 
{
	/** The name of this region of the sprite sheet */
	private String _name;
	/** The bounds of this region */
	private Rect _bounds;
	
	/** Creates an untitled sprite sheet of zero size */
	public SpriteSheetFrame()
	{
		_name = "<untitled>";
		_bounds = new Rect();
	}
	
	/**
	 * Creates a new sprite sheet frame
	 * @param name The name to assign to the frame
	 * @param bounds The region of the sprite sheet this frame represents
	 */
	public SpriteSheetFrame(String name, Rect bounds)
	{
		_name = name;
		_bounds = bounds;
	}
	
	/**
	 * Creates a new sprite sheet frame
	 * @param name The name to assign to the frame
	 * @param x The X coordinate of the region of the sprite sheet this frame represents
	 * @param y The Y coordinate of the region of the sprite sheet this frame represents
	 * @param width The width of the region of the sprite sheet this frame represents
	 * @param height The height of the region of the sprite sheet this frame represents
	 */
	public SpriteSheetFrame(String name, int x, int y, int width, int height)
	{
		_name = name;
		_bounds = new Rect(x, y, x + width, y + height);
	}
	
	/** Gets the name assigned to this region of the sprite sheet */
	public String getName()
	{
		return _name;
	}

	/** Sets the name assigned to this region of the sprite sheet */
	public void setName(String name)
	{
		_name = name;
	}
	
	/** Gets the bounds of the region this sprite sheet frame represents */
	public Rect getBounds()
	{
		return _bounds;
	}
	
	/** Sets the bounds of the region this sprite sheet frame represents */
	public void setBounds(Rect r)
	{
		_bounds = r;
	}
}
