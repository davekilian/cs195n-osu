package osu.screen;

import android.graphics.Color;
import android.graphics.Paint;
import osu.main.R;
import dkilian.andy.Kernel;
import dkilian.andy.Prerender;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;

/**
 * Shown after a beatmap is completed
 * @author dkilian
 */
public class ScoreScreen implements Screen
{
	public static final float GRADE_FADE_IN_TIME = 1.f;	// In seconds
	public static final float GRADE_SS = .999f;	// Accuracy rating (num hit / total objects)
	public static final float GRADE_S =   .96f;	// "
	public static final float GRADE_A =   .90f;	// "
	public static final float GRADE_B =   .80f;	// "
	public static final float GRADE_C =   .70f;	// "
	public static final float FADE_OUT_TIME = 1.f;
	
	private boolean _failed;
	private String _gradestr, _percentstr, _ratiostr;
	private TexturedQuad _background;
	private TexturedQuad _grade;		// SABCDF
	private TexturedQuad _percent;		// XYZ%
	private TexturedQuad _ratio;		// (objects not missed) / (total objects)
	private TexturedQuad _fade;
	private float _fadeStart = 0.f;
	private float _time = 0.f;
	
	public ScoreScreen(int hit, int total, boolean failed)
	{
		_failed = failed;
		
		if (!failed)
		{
			float ratio = (float)hit / (float)total;
			if (ratio >= GRADE_SS)
				_gradestr = "SS";
			else if (ratio >= GRADE_S)
				_gradestr = "S";
			else if (ratio >= GRADE_A)
				_gradestr = "A";
			else if (ratio >= GRADE_B)
				_gradestr = "B";
			else if (ratio >= GRADE_C)
				_gradestr = "C";
			else
				_gradestr = "F";
			_percentstr = String.format("%.0f%%", 100.f * ratio);
			_ratiostr = String.format("Hit %d / %d", hit, total);
		}
		else
		{
			_gradestr = "X";
		}
	}
	
	@Override
	public boolean isLoaded() 
	{
		return false;
	}

	@Override
	public void load(Kernel kernel) 
	{
	}

	@Override
	public void unload(Kernel kernel) 
	{
	}

	@Override
	public void update(Kernel kernel, float dt) 
	{
		_time += dt;
		
		if (_time > GRADE_FADE_IN_TIME && kernel.getTouch().isDown())
		{
			_fadeStart = _time;
		}
		
		if (_fadeStart > 0.f)
		{
			float fade = (_time - _fadeStart) / FADE_OUT_TIME;
			if (fade >= 1.f)
			{
				kernel.swapScreen(new MainMenuScreen());
				return;
			}
		}
	}

	@Override
	public void draw(Kernel kernel, float dt) 
	{		
		if (_background == null)
		{
			_background = TexturedQuad.fromResource(kernel, R.drawable.score_screen);
			
			Paint p = new Paint();
			p.setColor(Color.WHITE);
			p.setAntiAlias(true);
			
			p.setTextSize(250.f);
			_grade = Prerender.string(_gradestr, p);

			if (!_failed)
			{
				p.setTextSize(30.f);
				_percent = Prerender.string(_percentstr, p);
				_ratio = Prerender.string(_ratiostr, p);
			}
			
			p.setColor(Color.BLACK);
			_fade = Prerender.rectangle(kernel.getVirtualScreen().getWidth(), kernel.getVirtualScreen().getHeight(), p);
		}
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		
		_background.getTranslation().x = .5f * w;
		_background.getTranslation().y = .5f * h;
		_background.draw(kernel);
				
		if (!_failed)
		{		
			_grade.getTranslation().x = _grade.getWidth() * .5f + 50.f;
			_grade.getTranslation().y = _grade.getHeight() * .5f + 50.f;
			if (_time < GRADE_FADE_IN_TIME)
			{
				float pos = _time / GRADE_FADE_IN_TIME;
				float alpha = pos;
				float scale = 8.f * (1.f - pos) + 1.f * pos;
				_grade.setAlpha(alpha);
				_grade.getScale().x = scale;
				_grade.getScale().y = scale;
			}
			_grade.draw(kernel);
			
			float dx = _grade.getTranslation().x + _grade.getWidth();
			
			_percent.getTranslation().x = dx + .5f * (w - dx);
			if (_percent.getTranslation().x + .5f * _percent.getWidth() > w)
				_percent.getTranslation().x = w - .5f * _percent.getWidth();
			_percent.getTranslation().y = 100.f;
			_percent.draw(kernel);

			_ratio.getTranslation().x = dx + .5f * (w - dx);
			if (_ratio.getTranslation().x + .5f * _ratio.getWidth() > w)
				_ratio.getTranslation().x = w - .5f * _ratio.getWidth();
			_ratio.getTranslation().y = 200.f;
			_ratio.draw(kernel);
		}
		else
		{
			_grade.getTranslation().x = .5f * w;
			_grade.getTranslation().y = .5f * h;
			if (_time < GRADE_FADE_IN_TIME)
			{
				float pos = _time / GRADE_FADE_IN_TIME;
				float alpha = pos;
				float scale = 8.f * (1.f - pos) + 1.f * pos;
				_grade.setAlpha(alpha);
				_grade.getScale().x = scale;
				_grade.getScale().y = scale;
			}
			_grade.draw(kernel);
		}

		if (_fadeStart > 0.f)
		{
			float fade = (_time - _fadeStart) / FADE_OUT_TIME;
			_fade.setAlpha(fade);
			_fade.getTranslation().x = .5f * w;
			_fade.getTranslation().y = .5f * h;
			_fade.draw(kernel);
		}
	}
}
