package osu.tutorial;

import java.util.ArrayList;

/**
 * A slideshow layer on which slides may be shown
 * 
 * @author dkilian
 */
public class SlideLayer 
{
	private String _name;
	private ArrayList<Slide> _slides;
	
	public SlideLayer()
	{
		_name = "<unnamed>";
		_slides = new ArrayList<Slide>();
	}
	
	public SlideLayer(String name)
	{
		_name = name;
		_slides = new ArrayList<Slide>();
	}
	
	public SlideLayer(String name, ArrayList<Slide> slides)
	{
		_name = name;
		_slides = slides;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public ArrayList<Slide> getSlides()
	{
		return _slides;
	}
	
	public void setSlides(ArrayList<Slide> slides)
	{
		_slides = slides;
	}
}
