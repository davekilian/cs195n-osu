package dkilian.andy;

import dkilian.andy.jni.agl;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;

/**
 * Wraps an OpenGL textured quad (using the AGL native wrapper library)
 * as a standard Andy sprite
 * @author dkilian
 */
public class TexturedQuad implements Sprite
{
	/** A handle to this quad's texture in graphics memory. Used for rendering */
	private int _texture;
	/** The width of this sprite's texture in texels */
	private int _width;
	/** The height of this sprite's texture in texels */
	private int _height;
	/** The translation between the origin and this sprite, in virtual coordinates */
	private Vector2 _translation;
	/** The rotation of this sprite around its center in degrees */
	private float _rotation;
	/** The scaling factor between this sprite's object coordinates and the virtual coordinates in which this sprite is displayed */
	private Vector2 _scale;
	/** Whether the current shader should be used to render the quad instead of AGL's built-in quad shader */
	private boolean _overrideShader;
	/** The shader to use to render the quad instead of AGL's built-in quad shader if applicable */
	private int _shader;
	
	/**
	 * Loads a textured quad from an image in the apk's resource storage
	 * @param kernel The currently executing kernel
	 * @param resource The ID of the resource to load
	 * @return A textured quad containing the given resource image as its texture
	 */
	public static TexturedQuad fromResource(Kernel kernel, int resource)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		
		Bitmap b = BitmapFactory.decodeResource(kernel.getActivity().getResources(), resource);
		
		int tex = agl.CreateEmptyTexture();
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
		
		return new TexturedQuad(tex, b.getWidth(), b.getHeight());
	}
	
	/**
	 * Loads a textured quad using an image from a resource store
	 * @param res The resource store to read the image data from
	 * @param id The ID of the resource (in the resource store) containing the image data to load
	 * @return A textured quad containing the given resource image as its texture
	 */
	public static TexturedQuad fromResources(Resources res, int id)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		
		Bitmap b = BitmapFactory.decodeResource(res, id);
		
		int tex = agl.CreateEmptyTexture();
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
		
		return new TexturedQuad(tex, b.getWidth(), b.getHeight());
	}
	
	/**
	 * Creates a new textured quad from an existing texture
	 * @param tex A handle to this quad's texture in graphics memory
	 * @param w The width of this sprite's texture in texels
	 * @param h The height of this sprite's texture in texels
	 */
	public TexturedQuad(int tex, int w, int h)
	{
		_texture = tex;
		_width = w;
		_height = h;
		_translation = Vector2.Zero();
		_rotation = 0.f;
		_scale = Vector2.One();
		_overrideShader = false;
		_shader = 0;
	}
	
	/**
	 * Creates a new textured quad containg a copy of the data in the specified bitmap
	 * @param b The bitmap containing the color data to copy to the texture
	 */
	public TexturedQuad(Bitmap b)
	{
		_width = b.getWidth();
		_height = b.getHeight();
		
		_translation = Vector2.Zero();
		_rotation = 0.f;
		_scale = Vector2.One();
		_overrideShader = false;
		_shader = 0;

		_texture = agl.CreateTexture(_width, _height);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
	}
	
	/** Gets a handle to the texture this quad renders */
	public int getTexture()
	{
		return _texture;
	}
	
	/**
	 * Sets this quad's texture
	 * @param texture A handle to the texture to set
	 * @param w The width of the texture, in pixels
	 * @param h The height of the texture, in pixels
	 */
	public void setTexture(int texture, int w, int h)
	{
		_texture = texture;
		_width = w;
		_height = h;
	}
	
	/** Gets the shader that is used to render this sprite if using a custom shader */
	public int getShader()
	{
		return _shader;
	}
	
	/** Sets the shader used to render this sprite if using a custom shader */
	public void setShader(int shader)
	{
		_shader = shader;
	}
	
	/** Gets a value indicating whether this sprite's custom shader is used for rendering rather than AGL's built-in quad renderer */
	public boolean useCustomShader()
	{
		return _overrideShader;
	}

	/** Sets a value indicating whether this sprite's custom shader is used for rendering rather than AGL's built-in quad renderer */
	public void setUseCustomShader(boolean useCustom)
	{
		_overrideShader = useCustom;
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

	/** Gets the width of this sprite, in texels */
	@Override
	public int getWidth() 
	{
		return _width;
	}

	/** Gets the height of this sprite, in texels */
	@Override
	public int getHeight() 
	{
		return _height;
	}

	/** Not currently supported. It's possible but I'm lazy */
	@Override
	public boolean getClipRectEnabled() { return false; }

	/** Not currently supported. It's possible but I'm lazy */
	@Override
	public void setClipRectEnabled(boolean b) {}

	/** Not currently supported. It's possible but I'm lazy */
	@Override
	public Rect getClipRect() { return null; }

	/** Not currently supported. It's possible but I'm lazy */
	@Override
	public void setClipRect(Rect r) {}

	/** Renders this sprite */
	@Override
	public void draw(Kernel kernel) 
	{
		if (_overrideShader)
			agl.DrawBitmapWithShaderTransformed(_texture, _width, _height, _shader, _translation.x, _translation.y, _rotation, _scale.x, _scale.y);
		else
			agl.DrawBitmapWithoutShaderTransformed(_texture, _width, _height, _translation.x, _translation.y, _rotation, _scale.x, _scale.y);
	}
}
