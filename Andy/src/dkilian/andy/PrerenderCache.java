package dkilian.andy;

import java.util.HashMap;

import android.graphics.Paint;

/**
 * Caches pre-rendered objects so that they can be rendered once and
 * used multiples times. Currently only supports strings.
 * 
 * Note that this cache only stores object based on their inherent attributes,
 * not their paint attributes; generally a single cache should have its own
 * paint instance that is never modified. Each cache should contain data that
 * uses the same paint parameters (e.g. text with the same color and font)
 * 
 * @author dkilian
 */
public class PrerenderCache 
{
	/** Maps already-rendered strings to the result of rendering that string */
	private HashMap<String, TexturedQuad> _strings;
	/** The paint object containing this cache's render parameters */
	private Paint _p;
	
	/** Creates a new pre-rendered sprite cache */
	public PrerenderCache(Paint p)
	{
		_strings = new HashMap<String, TexturedQuad>();
		_p = p;
	}
	
	/** Gets this cache's render parameters */
	public Paint getPaint()
	{
		return _p;
	}
	
	/** Sets this cache's render parameters */
	public void setPaint(Paint p)
	{
		_p = p;
	}
	
	/**
	 * Returns the cached copy of the given string if there is one, or creates one
	 * on the fly, stores, and returns it otherwise.
	 * @param text The text to copy
	 * @return A sprite containing the text data, possibly already rendered.
	 */
	public TexturedQuad string(String text)
	{
		if (!_strings.containsKey(text))
			_strings.put(text, Prerender.string(text, _p));
		
		return _strings.get(text);
	}
}
