package osu.tutorial;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;

import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.TexturedQuad;

/**
 * A tutorial slide that shows one or more lines of text
 * 
 * @author dkilian
 */
public class TextSlide implements Slide
{
	/** The margin between lines of text, in virtual coordinates */
	public static final float LINE_MARGIN = 5.f;
	/** The time a slide takes fading in/out, in partial seconds */
	public static final float FADE_DURATION = .5f;
	
	/** The lines in this slide. Locked on first render */
	private ArrayList<String> _lines;
	/** The list of rendered text quads */
	private ArrayList<TexturedQuad> _rendered;
	/** The Y coordinate of the first line, in virtual coordinates */
	private float _y;
	/** The amount of time this slide is shown, in partial seconds */
	private float _duration;
	/** Gets this text slide's paint parameters */
	private Paint _paint;
	
	/** Creates a new text slide */
	public TextSlide()
	{
		_lines = new ArrayList<String>();
		_y = 0.f;
		_duration = DEFAULT_DURATION;
		_paint = new Paint();
		_paint.setTextSize(20.f);
		_paint.setAntiAlias(true);
		_paint.setColor(Color.WHITE);
	}
	
	/** Creates a new text slide */
	public TextSlide(String[] lines)
	{
		_lines = new ArrayList<String>();
		_y = 0.f;
		_duration = DEFAULT_DURATION;
		_paint = new Paint();
		_paint.setTextSize(20.f);
		_paint.setAntiAlias(true);
		_paint.setColor(Color.WHITE);
		
		for (int i = 0; i < lines.length; ++i)
			_lines.add(lines[i]);
	}

	/** Creates a new text slide */
	public TextSlide(float y)
	{
		_lines = new ArrayList<String>();
		_y = y;
		_duration = DEFAULT_DURATION;
		_paint = new Paint();
		_paint.setTextSize(20.f);
		_paint.setAntiAlias(true);
		_paint.setColor(Color.WHITE);
	}

	/** Creates a new text slide */
	public TextSlide(String[] lines, float y)
	{
		_lines = new ArrayList<String>();
		_y = y;
		_duration = DEFAULT_DURATION;
		_paint = new Paint();
		_paint.setTextSize(20.f);
		_paint.setAntiAlias(true);
		_paint.setColor(Color.WHITE);
		
		for (int i = 0; i < lines.length; ++i)
			_lines.add(lines[i]);
	}
	
	/** Gets the lines of text this slide shows */
	public ArrayList<String> getLines()
	{
		return _lines;
	}
	
	/** Sets the lines of text this slide shows */
	public void setLines(ArrayList<String> lines)
	{
		_lines = lines;
	}

	/** Gets the Y coordinate of the first line of text in this slide */
	public float getY()
	{
		return _y;
	}

	/** Sets the Y coordinate of the first line of text in this slide */
	public void setY(float y)
	{
		_y = y;
	}

	/** Gets the time this slide is shown, in seconds */
	@Override
	public float getDuration() 
	{
		return _duration;
	}

	/** Sets the time this slide is shown, in seconds */
	@Override
	public void setDuration(float d) 
	{
		_duration = d;
	}
	
	/** Gets this slide's paint parameters. Not used after the first render */
	public Paint getPaint()
	{
		return _paint;
	}

	/** Sets this slide's paint parameters. Not used after the first render */
	public void setPaint(Paint p)
	{
		_paint = p;
	}

	/** Renders this slide */
	@Override
	public void draw(Kernel kernel, float t, float slidetime) 
	{
		if (_rendered == null)
		{
			_rendered = new ArrayList<TexturedQuad>();
			for (int i = 0; i < _lines.size(); ++i)
				_rendered.add(Prerender.string(_lines.get(i), _paint));
		}

		float alpha = 1.f;
		if (slidetime < FADE_DURATION)
			alpha = slidetime / FADE_DURATION;
		else if (_duration - slidetime < FADE_DURATION)
			alpha = (_duration - slidetime) / FADE_DURATION;
		
		float y = _y;
		for (int i = 0; i < _lines.size(); ++i)
		{
			_rendered.get(i).getTranslation().x = .5f * kernel.getVirtualScreen().getWidth();
			_rendered.get(i).getTranslation().y = y;
			y += _rendered.get(i).getHeight();
			y += LINE_MARGIN;
			_rendered.get(i).setAlpha(alpha);
			_rendered.get(i).draw(kernel);
		}
	}
}
