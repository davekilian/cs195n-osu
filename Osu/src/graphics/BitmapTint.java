package graphics;

import osu.game.ComboColor;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Applies a tint to a bitmap image by multiplying each pixel value by a constant tint color;
 * a tint color of white leaves the source image unchanged, and a tint of black turns the 
 * entire image black. Alpha values are preserved in image tinting operations
 * 
 * @author dkilian
 */
public class BitmapTint 
{
	/**
	 * Tints an image
	 * @param img The image to tint
	 * @param c The ARGB color by which to tint the image.
	 */
	public static void apply(Bitmap img, int c)
	{
		apply(img, Color.red(c), Color.green(c), Color.blue(c));
	}
	
	/**
	 * Tints an image
	 * @param img The image to tint
	 * @param c The color by which to tint the image
	 */
	public static void apply(Bitmap img, ComboColor c)
	{
		apply(img, c.getR(), c.getG(), c.getB());
	}
	
	/**
	 * Tints an image
	 * @param img The image to tint
	 * @param r The red component of the color by which to tint the image, in [0,255]
	 * @param g The green component of the color by which to tint the image, in [0,255]
	 * @param b The blue component of the color by which to tint the image, in [0,255]
	 */
	public static void apply(Bitmap img, int r, int g, int b)
	{
		float div = 1.f / 255.f;
		
		apply(img, r * div, g * div, b * div);
	}
	
	/**
	 * Tints an image
	 * @param img The image to tint
	 * @param r The red component of the color by which to tint the image, in [0,1]
	 * @param g The green component of the color by which to tint the image, in [0,1]
	 * @param b The blue component of the color by which to tint the image, in [0,1]
	 */
	public static void apply(Bitmap img, float r, float g, float b)
	{
		float norm = 1.f / 255.f;
		float unnorm = 255.f;
		
		int[] pixels = new int[img.getWidth() * img.getHeight()];
		for (int i = 0; i < pixels.length; ++i)
		{
			float red = r * (norm * Color.red(pixels[i]));
			float green = g * (norm * Color.green(pixels[i]));
			float blue = b * (norm * Color.blue(pixels[i]));
			
			pixels[i] = Color.argb(Color.alpha(pixels[i]), (int)(red * unnorm), (int)(green * unnorm), (int)(blue * unnorm));
		}
	}
}
