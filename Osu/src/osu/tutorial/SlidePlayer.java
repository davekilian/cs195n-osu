package osu.tutorial;

import java.util.ArrayList;
import java.util.HashMap;

import dkilian.andy.Kernel;

/**
 * Plays a multi-layer slideshow
 * @author dkilian
 */
public class SlidePlayer 
{
	private class LayerPlaybackInfo
	{
		public LayerPlaybackInfo(SlideLayer layer)
		{
			this.layer = layer;
			currentSlide = 0;
			slideBegin = 0.f;
		}
		
		public SlideLayer layer;
		public int currentSlide;
		public float slideBegin;
	}
	
	private ArrayList<String> _layerNames; // Ordered
	private HashMap<String, SlideLayer> _layers;
	private HashMap<String, LayerPlaybackInfo> _playback;
	private float _time;
	
	public SlidePlayer()
	{
		_layerNames = new ArrayList<String>();
		_layers = new HashMap<String, SlideLayer>();
		_playback = new HashMap<String, SlidePlayer.LayerPlaybackInfo>();
		_time = 0.f;
	}
	
	public void createLayer(String name)
	{
		SlideLayer l = new SlideLayer(name);
		_layers.put(name, l);
		_layerNames.add(name);
		_playback.put(name, new LayerPlaybackInfo(l));
	}
	
	public void removeLayer(String name)
	{
		_layers.remove(name);
		_layerNames.remove(name);
		_playback.remove(name);
	}
	
	public SlideLayer getLayer(String name)
	{
		return _layers.get(name);
	}
	
	public void add(String layer, Slide slide)
	{
		getLayer(layer).getSlides().add(slide);
	}

	public void remove(String layer, Slide slide)
	{
		getLayer(layer).getSlides().remove(slide);
	}
	
	public boolean isDone()
	{
		for (int i = 0; i < _layerNames.size(); ++i)
			if (_playback.get(_layerNames.get(i)).currentSlide < _playback.get(_layerNames.get(i)).layer.getSlides().size())
				return false;
		
		return true;
	}
	
	public void update(Kernel kernel, float dt)
	{
		_time += dt;
		
		for (int i = 0; i < _layerNames.size(); ++i)
		{
			LayerPlaybackInfo play = _playback.get(_layerNames.get(i));
			if (play.currentSlide < play.layer.getSlides().size())
			{
				Slide s = play.layer.getSlides().get(play.currentSlide); 
				if (_time - play.slideBegin > s.getDuration())
				{
					play.slideBegin += s.getDuration();
					++play.currentSlide;
				}
			}
		}
	}
	
	public void draw(Kernel kernel, float dt)
	{
		for (int i = 0; i < _layerNames.size(); ++i)
		{
			LayerPlaybackInfo play = _playback.get(_layerNames.get(i));
			if (play.currentSlide < play.layer.getSlides().size())
				play.layer.getSlides().get(play.currentSlide).draw(kernel, _time, _time - play.slideBegin);
		}
	}
}
