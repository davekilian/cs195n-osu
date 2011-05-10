package osu.game;

public class HOSlider extends HitObject {
	
	public HOSlider(int x_pos, int y_pos, long time_millis, boolean is_new_combo, int sound)
	{
		x = x_pos;
		y = y_pos;
		
		timing = time_millis;
		
		new_combo = is_new_combo;
		
		sound_type = sound;
	}

}
