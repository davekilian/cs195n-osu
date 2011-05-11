package osu.game;

public class Background {

	// Background and Video Events
	private float bve_un1, bve_un2; // Unknown values
	private String image_name;
	
	// Background Colour Transformations
	private float bct_un1, bct_un2; // Unknown values
	private int r, g, b;
	
	
	// *** CONSTRUCTORS *** //
	public Background()
	{
		// Background and Video Events
		bve_un1 = 0;
		bve_un2 = 0;
		
		image_name = null;
		
		// Background Colour Transformations
		bct_un1 = 0;
		bct_un2 = 0;
		
		r = 0;
		g = 200;
		b = 0;
	}
	
	
	// *** ACCESSORS *** //
	public void setBveUn1(float f) { bve_un1 = f; }
	public void setBveUn2(float f) { bve_un2 = f; }
	
	public void setImagePath(String s) { image_name = s; }
	
	public void setBctUn1(float f) { bct_un1 = f; }
	public void setBctUn2(float f) { bct_un2 = f; }
	
	public void setRGB(int red, int green, int blue)
	{
		r = red;
		g = green;
		b = blue;
	}
	public void setR(int red) { r = red; }
	public void setG(int green) { g = green; }
	public void setB(int blue) { b = blue; }
	
	
	public float getBveUn1() { return bve_un1; }
	public float getBveUn2() { return bve_un2; }
	
	public String getImagePath() { return image_name; }
	
	public float getBctUn1() { return bct_un1; }
	public float getBctUn2() { return bct_un2; }
	
	public int getR() { return r; }
	public int getG() { return g; }
	public int getB() { return b; }
	
}
