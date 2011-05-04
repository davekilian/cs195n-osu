package dkilian.andy;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap;

/**
 * A sprite implementation that wraps an Android Bitmap object.
 * Suitable for raster graphics.
 * 
 * @author dkilian
 */
public class BitmapSprite implements Sprite
{
	/** The bitmap containing the raster graphic this sprite renders */
	private Bitmap _bitmap;
	/** This sprite's transformation matrix */
	private Matrix _transform;
	/** Whether or not this sprite uses its clip rectangle */
	private boolean _clipEnabled;
	/** This sprite's clip rectangle */
	private Rect _clipRect;
	/** Temporary value used when drawing with the clip rectangle enabled. Preallocated to avoid needless garbage collection */
	private RectF _destRect;
	/** Temporary value used when drawing with the clip rectangle enabled. Preallocated to avoid needless garbage collection */
	private float[] _destPoints;
	/** The translation between the origin and this sprite, in virtual coordinates */
	private Vector2 _translation;
	/** The rotation of this sprite around its center in degrees */
	private float _rotation;
	/** The scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	private Vector2 _scale;
	
	/**
	 * Creates a new bitmap sprite
	 * @param bitmap The sprite's graphic
	 */
	public BitmapSprite(Bitmap bitmap)
	{
		_bitmap = bitmap;
		_transform = new Matrix();
		_translation = Vector2.Zero();
		_rotation = 0.f;
		_scale = Vector2.One();
		_clipEnabled = false;
		_clipRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		_destRect = new RectF();
		_destPoints = new float[4];
	}
	
	/** Gets this sprite's graphic */
	public final Bitmap getBitmap()
	{
		return _bitmap;
	}
	
	/** Sets this sprite's graphic */
	public final void setBitmap(Bitmap bitmap)
	{
		_bitmap = bitmap;
	}
	
	/** Gets this sprite's transformation matrix */
	public final Matrix getTransform() 
	{
		return _transform;
	}

	/** Sets this sprite's transformation matrix */
	public final void setTransform(Matrix m) 
	{
		_transform = m;
	}

	/** Gets the width of this sprite, in pixels */
	@Override
	public final int getWidth() 
	{
		return _bitmap.getWidth();
	}

	/** Gets the height of this sprite, in pixels */
	@Override
	public final int getHeight() 
	{
		return _bitmap.getHeight();
	}

	/** Gets a value indicating whether this sprite's clip rectangle is obeyed when rendering */
	@Override
	public final boolean getClipRectEnabled() 
	{
		return _clipEnabled;
	}

	/** Sets a value indicating whether this sprite's clip rectangle is obeyed when rendering */
	@Override
	public final void setClipRectEnabled(boolean b) 
	{
		_clipEnabled = b;
	}

	/** Gets the region of the sprite drawn when the clip rectangle is enabled */
	@Override
	public final Rect getClipRect() 
	{
		return _clipRect;
	}

	/** Sets the region of the sprite drawn when the clip rectangle is enabled */
	@Override
	public final void setClipRect(Rect r) 
	{
		_clipRect = r;
	}

	/** Renders this sprite */
	@Override
	public final void draw(Kernel kernel) 
	{
		if (_clipEnabled)
		{
			_transform.reset();
			_transform.postScale(_scale.x, _scale.y);
			_transform.postRotate(_rotation);
			_transform.postTranslate(_translation.x, _translation.y);
			
			_destPoints[0] = _clipRect.left;
			_destPoints[1] = _clipRect.top;
			_destPoints[2] = _clipRect.right;
			_destPoints[3] = _clipRect.bottom;
			_transform.mapPoints(_destPoints);
			_destRect.left = _destPoints[0];
			_destRect.top = _destPoints[1];
			_destRect.right = _destPoints[2];
			_destRect.bottom = _destPoints[3];
			
			kernel.getView().getCanvas().drawBitmap(_bitmap, _clipRect, _destRect, kernel.getView().getPaint());
		}
		else
		{
			kernel.getView().getCanvas().drawBitmap(_bitmap, _transform, kernel.getView().getPaint());
		}
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
