package osu.tutorial;

import dkilian.andy.Kernel;

/**
 * Generates a tutorial slider player corresponding to our tutorial script
 * (see /tutorial/script)
 * 
 * @author dkilian
 */
public class OsuTutorial 
{
	private static void text(SlidePlayer player, String[] lines, float y)
	{
		TextSlide slide = new TextSlide(lines, y);
		player.add("text", slide);
	}
	
	private static void text(SlidePlayer player, String[] lines, float y, float duration)
	{
		TextSlide slide = new TextSlide(lines, y);
		slide.setDuration(duration);
		player.add("text", slide);
	}
	
	/** Creates a slider player ready to play our osu! tutorial */
	public static SlidePlayer makeSlides(Kernel kernel)
	{
		SlidePlayer player = new SlidePlayer();
		
		float cy = .75f * kernel.getVirtualScreen().getHeight();
		
		player.createLayer("text");
		text(player, new String[] {"Welcome to osu!"}, cy);
		text(player, new String[] {"osu! is a music rhythm game where you get", "points for using your finger to hit various", "controls in time with the music."}, cy);
		text(player, new String[] {"There are three different controls:", "buttons, sliders, and spinners."}, cy);
		text(player, new String[] {"The button just needs to be tapped."}, cy);
		text(player, new String[] {"As the correct time approaches, a ring will", "slowly zoom in around the button. When the", "ring is the same size as the button, hit it!"}, cy, 10.f);
		text(player, new String[] {"Watch the ring, listen to the music,", "and trust your instincts!"}, cy);
		text(player, new String[] {"At first, sliders act like ordinary buttons."}, cy);
		text(player, new String[] {"However, after tapping a slider, you'll need", "to drag your finger along its path, following", "the nub to the end in time with the song."}, cy, 10.f);
		text(player, new String[] {"Some sliders can bounce back before finishing!", "Watch for repeat arrows at either end of a slider", "to know when you're done with it."}, cy, 10.f);
		text(player, new String[] {"Spinners are the third and final osu! control."}, cy);
		text(player, new String[] {"Spin each spinner as fast as you can. If you spin", "fast enough to fill up the power meter,", "you will gain credit for the section!"}, cy, 10.f);
		text(player, new String[] {"Be sure not to miss too many controls in a row,", "or you will lose!"}, cy);
		text(player, new String[] {"Each miss takes away a portion of your health.", "Your health will also drain slowly over time."}, cy, 10.f);
		text(player, new String[] {"Each hit restores some of your health, so be sure", "to stay on top of your game to stay alive!"}, cy);
		text(player, new String[] {"Some controls have numbers that show the order", "to hit them in. Use this in combination with", "timing rings to hit controls in time!"}, cy, 10.f);
		text(player, new String[] {"New combos start at \"1\" with a new color. Be sure", "to finish the current combo before starting the next!"}, cy, 10.f);
		text(player, new String[] {"Hitting controls correctly will give you points.", "Aim for the highest score possible!"}, cy);
		text(player, new String[] {"If a song proves too challenging,", "try it on a lower difficulty."}, cy);
		text(player, new String[] {"Songs are sorted in the library alphabetically.", "Each is available in multiplie difficulties, ordered from", "top to bottom increasing in difficulty."}, cy, 10.f);
		text(player, new String[] {"Good luck, and..."}, cy);
		text(player, new String[] {"osu!"}, cy, 2.f);
		
		player.createLayer("examples");
		
		return player;
	}
}
