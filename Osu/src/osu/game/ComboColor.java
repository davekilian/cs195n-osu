package osu.game;

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
	
}
