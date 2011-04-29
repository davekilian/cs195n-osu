package dkilian.andy;

import java.util.ArrayList;

/**
 * A flipbook consisting of a list of other sprite objects
 * 
 * @author dkilian
 */
public class SpriteListFlipbook implements Flipbook
{
	/** The sprites in this flipbook */
	private ArrayList<Sprite> _sprites;
	
	/** Creates an empty sprite-list flipbook */
	public SpriteListFlipbook()
	{
		_sprites = new ArrayList<Sprite>();
	}
	
	/** Creates a new sprite-list flipbook */
	public SpriteListFlipbook(ArrayList<Sprite> sprites)
	{
		_sprites = sprites;
	}
	
	/** Gets the sprites in this flipbook */
	public ArrayList<Sprite> getSprites()
	{
		return _sprites;
	}
	
	/** Sets the sprites in this flipbook */
	public void setSprites(ArrayList<Sprite> sprites)
	{
		_sprites = sprites;
	}

	/** Gets the number of frames in this flipbook */
	@Override
	public int getFrameCount() 
	{
		return _sprites.size();
	}

	/** Gets a frame from this flipbook */
	@Override
	public Sprite getFrame(int i) 
	{
		return _sprites.get(i);
	}
}
