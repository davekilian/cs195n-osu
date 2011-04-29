package dkilian.andy;

import java.util.ArrayList;

/**
 * Represents a set of tile sprites bundled onto a single image
 * 
 * @author dkilian
 */
public class SpriteSheet 
{
	/** The sub-regions in this sprite sheet */
	private ArrayList<SpriteSheetFrame>  _frames;
	/** The sprite containing the sprite sheet graphic */
	private Sprite _source;
	
	/**
	 * Creates a new sprite sheet
	 * @param source The sprite containing the sprite sheet graphic
	 */
	public SpriteSheet(Sprite source)
	{
		_source = source;
		_frames = new ArrayList<SpriteSheetFrame>();
	}
	
	/**
	 * Creates a new sprite sheet
	 * @param source The sprite containing the sprite sheet graphic
	 * @param frames The sub-regions in this sprite sheet
	 */
	public SpriteSheet(Sprite source, ArrayList<SpriteSheetFrame> frames)
	{
		_source = source;
		_frames = frames;
	}
	
	/** Gets the sprite containing the sprite sheet graphic */
	public Sprite getSource()
	{
		return _source;
	}
	
	/** Sets the sprite containing the sprite sheet graphic */
	public void setSource(Sprite source)
	{
		_source = source;
	}
	
	/** Gets the sub-regions in this sprite sheet */
	public ArrayList<SpriteSheetFrame> getFrames()
	{
		return _frames;
	}
	
	/** Sets the sub-regions in this sprite sheet */
	public void setFrames(ArrayList<SpriteSheetFrame> frames)
	{
		_frames = frames;
	}
	
	/** Gets the region with the specified name */
	public SpriteSheetFrame getFrame(String name)
	{
		for (int i = 0; i < _frames.size(); ++i)
		{
			SpriteSheetFrame f = _frames.get(i);
			if (name.equals(f.getName()))
				return f;
		}

		return null;
	}
	
	/** Gets the region at the specified index */
	public SpriteSheetFrame getFrame(int i )
	{
		return _frames.get(i);
	}
	
	/** Creates a sprite that renders the frame with the given name */
	public Sprite getSpriteForFrame(String name)
	{
		return getSpriteForFrame(getFrame(name));
	}
	
	/** Creates a sprite that renders the frame at the given index */
	public Sprite getSpriteForFrame(int i)
	{
		return getSpriteForFrame(getFrame(i));
	}
	
	/** Creates a sprite that renders the given frame */
	public Sprite getSpriteForFrame(SpriteSheetFrame f)
	{
		return new SpriteSheetSprite(this, f);
	}
}
