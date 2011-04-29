package dkilian.andy;

/**
 * A flipbook consisting of the frames of a sprite sheet
 * 
 * @author dkilian
 */
public class SpriteSheetFlipbook implements Flipbook
{
	/** The sprite sheet containing the frames of this flipbook */
	private SpriteSheet _spriteSheet;
	/** The frames of this flipbook, cached to avoid needless garbage collection */
	private Sprite[] _cachedSprites;
	
	/**
	 * Creates a new sprite-sheet flipbook
	 * @param spriteSheet The sprite sheet containing the frames of this flipbook
	 */
	public SpriteSheetFlipbook(SpriteSheet spriteSheet)
	{
		setSpriteSheet(spriteSheet);
	}
	
	/** Creates the frames of this flipbook. Call this only if you modify this flipbook's sprite sheet after setting it */
	public void cacheSprites()
	{
		_cachedSprites = null;
		
		if (_spriteSheet != null)
		{
			_cachedSprites = new Sprite[_spriteSheet.getFrames().size()];
			for (int i = 0; i < _cachedSprites.length; ++i)
				_cachedSprites[i] = _spriteSheet.getSpriteForFrame(i);
		}
	}
	
	/** Gets the sprite sheet containing the frames of this sprite */
	public SpriteSheet getSpriteSheet()
	{
		return _spriteSheet;
	}
	
	/** Sets the sprite sheet containing the frames of this sprite */
	public void setSpriteSheet(SpriteSheet spriteSheet)
	{
		_spriteSheet = spriteSheet;
		cacheSprites();
	}

	/** Gets the number of frames in this flipbook */
	@Override
	public int getFrameCount() 
	{
		return _cachedSprites.length;
	}

	/** Gets a frame from this flipbook */
	@Override
	public Sprite getFrame(int i) 
	{
		return _cachedSprites[i];
	}
}
