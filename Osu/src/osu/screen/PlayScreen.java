package osu.screen;

import osu.beatmap.BeatmapPlayer;
import dkilian.andy.Kernel;
import dkilian.andy.Screen;

public class PlayScreen implements Screen
{
	private boolean _loaded = false;
	private BeatmapPlayer _player;
	private float _time;
	
	public PlayScreen(BeatmapPlayer player)
	{
		_player = player;
		_player.begin();
		_time = 0.f;
	}

	@Override
	public boolean isLoaded() 
	{
		return _loaded;
	}

	@Override
	public void load(Kernel kernel) 
	{
		_loaded = true;
	}

	@Override
	public void unload(Kernel kernel) 
	{
		_loaded = false;
	}

	@Override
	public void update(Kernel kernel, float dt) 
	{	
		_time += dt;
		_player.update(kernel, _time, dt);
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		_player.draw(kernel, _time, dt);
	}
}
