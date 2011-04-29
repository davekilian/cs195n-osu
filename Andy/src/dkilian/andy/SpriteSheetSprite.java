package dkilian.andy;

import android.graphics.Matrix;
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
	/** This sprite's transformation matrix */
	private Matrix _transform;
	/** Not supported */
	private boolean _clipEnabled;
	/** Not supported */
	private Rect _clipRect;
	/** Temp value used in drawing. Preallocated to avoid garbage collection */
	private Matrix _world;
	
	/**
	 * Creates a new sprite-sheet sprite
	 * @param source The sprite sheet this sprite reads from
	 * @param frame The portion of the sprite sheet this sprite wraps
	 */
	public SpriteSheetSprite(SpriteSheet source, SpriteSheetFrame frame)
	{
		_source = source;
		_frame = frame;
		_transform = new Matrix();
		_world = new Matrix();
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

	/** Gets this sprite's transformation matrix */
	@Override
	public Matrix getTransform() 
	{
		return _transform;
	}

	/** Sets this sprite's transformation matrix */
	@Override
	public void setTransform(Matrix m) 
	{
		_transform = m;
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
		
		_world.reset();
		_world.postTranslate(-_frame.getBounds().left, -_frame.getBounds().top);
		_world.postConcat(_transform);
		
		_source.getSource().setTransform(_world);
		_source.getSource().setClipRectEnabled(true);
		_source.getSource().setClipRect(_frame.getBounds());
		_source.getSource().draw(kernel);
	}
}
