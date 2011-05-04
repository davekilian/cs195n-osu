package dkilian.andy;

import android.graphics.Rect;

/**
 * A sprite impementation that wraps a portion of another sprite via a sprite sheet
 * 
 * @author dkilian
 */
public class SpriteSheetSprite implements Sprite
{
	/** The sprite sheet containing the graphic to draw */
	private SpriteSheet _source;
	/** The portion of the source this sprite draws */
	private SpriteSheetFrame _frame;
	/** Not supported */
	private boolean _clipEnabled;
	/** Not supported */
	private Rect _clipRect;
	/** The translation between the origin and this sprite, in virtual coordinates */
	private Vector2 _translation;
	/** The rotation of this sprite around its center in degrees */
	private float _rotation;
	/** The scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	private Vector2 _scale;
	/** Temporary value used in rendering. Preallocated for GC performance */
	private Vector2 _tmp;
	
	/**
	 * Creates a new sprite-sheet sprite
	 * @param source The sprite sheet this sprite reads from
	 * @param frame The portion of the sprite sheet this sprite wraps
	 */
	public SpriteSheetSprite(SpriteSheet source, SpriteSheetFrame frame)
	{
		_source = source;
		_frame = frame;
		_translation = Vector2.Zero();
		_rotation = 0.f;
		_scale = Vector2.One();
		_tmp = Vector2.Zero();
	}
	
	/** Gets the sprite sheet this sprite reads from */
	public SpriteSheet getSource()
	{
		return _source;
	}
	
	/** Sets the sprite sheet this sprite reads from */
	public void setSource(SpriteSheet source)
	{
		_source = source;
	}
	
	/** Gets the region of the sprite sheet this sprite reads from */
	public SpriteSheetFrame getFrame()
	{
		return _frame;
	}
	
	/** Sets the region of the sprite sheet this sprite reads from */
	public void setFrame(SpriteSheetFrame frame)
	{
		_frame = frame;
	}

	/** Gets the width of this sprite, in pixels */
	@Override
	public int getWidth() 
	{
		return _frame.getBounds().width();
	}

	/** Gets the height of this sprite, in pixels */
	@Override
	public int getHeight() 
	{
		return _frame.getBounds().height();
	}

	/** Not supported */
	@Override
	public boolean getClipRectEnabled() 
	{
		return _clipEnabled;
	}

	/** Not supported */
	@Override
	public void setClipRectEnabled(boolean b) 
	{
		_clipEnabled = b;
	}

	/** Not supported */
	@Override
	public Rect getClipRect() 
	{
		return _clipRect;
	}

	/** Not supported */
	@Override
	public void setClipRect(Rect r) 
	{
		_clipRect = r;
	}

	/** Draws this sprite */
	@Override
	public void draw(Kernel kernel) 
	{
		// Technically we could do some kind of union on the cliprect and frame rect to support clip rects.
		// But I'm tired and I don't wanna.
		
		_tmp.set(_translation);
		_tmp.x -= _frame.getBounds().left;
		_tmp.y -= _frame.getBounds().top;
		
		_source.getSource().getTranslation().set(_tmp);
		_source.getSource().setRotation(_rotation);
		_source.getSource().getScale().set(_scale);
		
		_source.getSource().setClipRectEnabled(true);
		_source.getSource().setClipRect(_frame.getBounds());
		_source.getSource().draw(kernel);
	}

	/** Gets the translation between this sprite and the origin, in virtual coordinates */
	@Override
	public Vector2 getTranslation() 
	{
		return _translation;
	}

	/** Sets the translation between this sprite and the origin, in virtual coordinates */
	@Override
	public void setTranslation(Vector2 t) 
	{
		_translation = t;
	}

	/** Gets the world-space rotation of this sprite around its origin, in degrees */
	@Override
	public float getRotation() 
	{
		return _rotation;
	}

	/** Sets the world-space rotation of this sprite around its origin, in degrees */
	@Override
	public void setRotation(float rot) 
	{
		_rotation = rot;
	}

	/** Gets the scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	@Override
	public Vector2 getScale() 
	{
		return _scale;
	}

	/** Sets the scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	@Override
	public void setScale(Vector2 s) 
	{
		_scale = s;
	}
}
