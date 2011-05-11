package hello.agl;

import android.util.FloatMath;
import android.util.Log;
import dkilian.andy.Kernel;
import dkilian.andy.Particle;
import dkilian.andy.ParticleEmitter;
import dkilian.andy.ParticleSystem;
import dkilian.andy.Screen;
import dkilian.andy.TexturedQuad;
import dkilian.andy.Vector2;
import dkilian.andy.jni.agl;

public class MainScreen implements Screen
{
	private boolean _loaded = false;
	private float _time = 0;
	private boolean _first = true;
	private TexturedQuad _smoke;
	private ParticleSystem<Particle> _ps;

	@Override
	public boolean isLoaded() 
	{
		return _loaded;
	}

	@Override
	public void load(Kernel kernel) 
	{
		Log.v("MainScreen", "OpenGL: " + Integer.toString(kernel.getGLView().getGLVersion(), 16));
		
		float w = kernel.getVirtualScreen().getWidth();
		float h = kernel.getVirtualScreen().getHeight();
		
		_ps = new ParticleSystem<Particle>();
		
		ParticleEmitter<Particle> emit = new ParticleEmitter<Particle>();
		emit.setParticlesPerBatch(5, 10);
		emit.setSpawnPoint(new Vector2(.45f * w, .45f * h), new Vector2(.55f * w, .55f * h));
		emit.setSpawnInterval(0.1f, 0.5f);
		emit.setSpeed(0.f, 0.f);
		emit.setSpeed(10.f, 75.f);
		emit.setDirection(0.f, (float)(2.0 * Math.PI));
		emit.setAcceleration(0.f, 0.f);
		emit.setAcceleration(-2.5f, .25f * -50.f);
		emit.setAccelerationDirection(0.f, (float)(2.0 * Math.PI));
		emit.setRotation(0.f, 360.f);
		emit.setAngularVelocity(-90.f, 90.f);
		emit.setAngularAcceleration(0.f, 0.f);
		emit.setMass(.5f, 1.f);
		emit.setLifespan(1.f, 5.f);
		emit.setScale(Vector2.One(), Vector2.One().multiply(1.25f));
		_ps.add(emit);
		
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
		_ps.update(kernel, dt);
	}
	

	@Override
	public void draw(Kernel kernel, float dt) 
	{
		if (_first)
		{
			_first = false;
			agl.ClearColor(100.f / 255.f, 149.f / 255.f, 237.f / 255.f);
			
			_smoke = TexturedQuad.fromResource(kernel, R.drawable.smoke);
			for (int i = 0; i < 50; ++i)
			{
				Particle p = new Particle();
				p.setSprite(_smoke);
				_ps.add(p);
			}
			
			agl.BlendPremultiplied();
		}
		
		_ps.draw(kernel, dt);
		_smoke.getTranslation().x = _smoke.getTranslation().y = 0.f;
		_smoke.setRotation(0.f);
		_smoke.getScale().x = _smoke.getScale().y = 1.f;
		_smoke.setAlpha(.5f + .5f * FloatMath.sin(_time));
		_smoke.draw(kernel);
	}
}
