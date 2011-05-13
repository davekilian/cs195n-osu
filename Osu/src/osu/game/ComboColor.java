package osu.game;

import android.graphics.Color;

public class ComboColor {
	
	private int r, g, b;
	
	public ComboColor(int red, int green, int blue)
	{
		r = red;
		g = green;
		b = blue;
	}
	
	
	// *** ACCESSORS *** //
	public int getR() { return r; }
	public int getG() { return g; }
	public int getB() { return b; }
	
	public void setR(int red) { r = red; }
	public void setG(int green) { g = green; }
	public void setB(int blue) { b = blue; }
	
	@Override
	public int hashCode()
	{
		return Color.argb(255, r, g, b);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o.getClass() != ComboColor.class)
			return false;
		
		return hashCode() == o.hashCode();
	}
	
}
