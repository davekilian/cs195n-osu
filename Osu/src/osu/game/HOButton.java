package osu.game;

/**
 * The HOButton is one of the HitObject representations.
 * This is merely a button you press in time with the music that has no real
 * extra attributes over the standard hit objects.
 */
public class HOButton extends HitObject {
	
	public HOButton(int x_pos, int y_pos, long time_millis, boolean is_new_combo, int sound)
	{
		x = x_pos;
		y = y_pos;
		
		timing = time_millis;
		
		new_combo = is_new_combo;
		
		sound_type = sound;
	}

}
