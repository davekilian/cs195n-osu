package osu.controls;

import graphics.BitmapTint;
import osu.game.ComboColor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import dkilian.andy.Kernel;
import dkilian.andy.Sprite;

/**
 * Handles button interactions, wrapping a button hit-object.
 * 
 * @author dkilian
 */
public class Button implements Control
{
	/** The X coordinate of this button's center in virtual space */
	private float _x;
	/** The Y coordinate of this button's center in virtual space */
	private float _y;
	/** The time this button begins to fade in */
	private float _tbeg;
	/** The time this button ends fading out */
	private float _tend;
	/** The bounds of this button in virtual coordinates */
	private Rect _bounds;
	/** The image this button displays when raised */
	private Sprite _up;
	/** The image this button displays when pressed */
	private Sprite _down;
	
	/**
	 * Renders a button from its components. All components should have the same pixel dimensions.
	 * @param button The fill component of this button
	 * @param shadow The drop shadow below the button
	 * @param chrome The chrome on top of the button
	 * @return A textured quad containing the drop shadow rendered under the button and the chrome rendered over the button
	 */
	public static Sprite render(Bitmap button, Bitmap shadow, Bitmap chrome)
	{
		Bitmap target = Bitmap.createBitmap(button.getWidth(), button.getHeight(), Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(target);
		Paint p = new Paint();
		
		c.drawBitmap(shadow, 0.f, 0.f, p);
		c.drawBitmap(button, 0.f, 0.f, p);
		c.drawBitmap(chrome, 0.f, 0.f, p);
		
		return null;
	}
	
	/**
	 * Tints and renders a button from its components. All components should have the same pixel dimensions.
	 * @param button The monochrome fill component to tint (note that this object will be modified in 
	 * place - reload or copy the original monochrome component to do a different tinting operation)
	 * @param shadow The drop shadow below the fill component
	 * @param chrome The chrome above the fill component
	 * @param color The color to tint the button's fill componenet
	 * @return A textured quad containing the final, tinted button
	 */
	public static Sprite render(Bitmap button, Bitmap shadow, Bitmap chrome, int color)
	{
		BitmapTint.apply(button, color);
		return render(button, shadow, chrome);
	}

	/**
	 * Tints and renders a button from its components. All components should have the same pixel dimensions.
	 * @param button The monochrome fill component to tint (note that this object will be modified in 
	 * place - reload or copy the original monochrome component to do a different tinting operation)
	 * @param shadow The drop shadow below the fill component
	 * @param chrome The chrome above the fill component
	 * @param color The color to tint the button's fill componenet
	 * @return A textured quad containing the final, tinted button
	 */
	public static Sprite render(Bitmap button, Bitmap shadow, Bitmap chrome, ComboColor color)
	{
		BitmapTint.apply(button, color);
		return render(button, shadow, chrome);
	}
	
	public Button()
	{
		_x = 0.f;
		_y = 0.f;
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
	}
	
	public Button(Sprite up, Sprite down)
	{
		_x = 0.f;
		_y = 0.f;
		_tbeg = 0.f;
		_tend = 0.f;
		_bounds = new Rect();
		_up = up;
		_down = down;
	}
	
	public Sprite getUpImage()
	{
		return _up;
	}
	
	public void setUpImage(Sprite up)
	{
		_up = up;
	}
	
	public Sprite getDownImage()
	{
		return _down;
	}
	
	public void setDownImage(Sprite down)
	{
		_down = down;
	}

	@Override
	public float getX() 
	{
		return _x;
	}

	@Override
	public void setX(float x) 
	{		
		_x = x;
	}

	@Override
	public float getY() 
	{
		return _y;
	}

	@Override
	public void setY(float y) 
	{
		_y = y;
	}

	@Override
	public float getStartTime() 
	{
		return _tbeg;
	}

	@Override
	public void setStartTime(float t) 
	{
		_tbeg = t;
	}

	@Override
	public float getEndTime() 
	{
		return _tend;
	}

	@Override
	public void setEndTime(float t) 
	{
		_tend = t;
	}

	@Override
	public boolean isVisible(float t) 
	{
		return t >= _tbeg && t <= _tend;
	}

	@Override
	public Rect getHitbox() 
	{
		return _bounds;
	}

	@Override
	public void interact(float x, float y) 
	{
	}

	@Override
	public void update(Kernel kernel, float dt) 
	{
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
	}
}
