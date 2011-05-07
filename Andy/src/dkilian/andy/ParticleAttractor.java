package dkilian.andy;

import java.util.ArrayList;

/**
 * Base class for an object that attracts or repels
 * particles in a particle system 
 * 
 * The base implementation works by applying a 
 * gravitational force (positive or negative) to
 * each particle in the system.
 *
 * @param <T> The type of particle this attractor operates on
 * @author dkilian
 */
public class ParticleAttractor<T extends Particle>
{
	/** The gravitational constant, G = 6.67 x 10e-11 */
	public static final float G = (float)6.67e-11;
	
	/** The particle system containing the particles modified by this attractor */
	protected ParticleSystem<T> _system;
	/** The position of this particle attractor in virtual space */
	protected Vector2 _pos;
	/** The mass of this attractor, in mass units */
	protected float _mass;
	/** The current force vector. Precomputed to avoid GC allocations */
	private Vector2 _F;
	
	/** Creates a new gravity-based particle attractor */
	public ParticleAttractor()
	{
		_system = null;
		_pos = Vector2.Zero();
		_mass = 0.f;
	}
	
	/** Applies this attractor to each particle in the system */
	public void update(Kernel kernel, float dt)
	{
		if (_system != null)
		{
			ArrayList<T> all = _system.getParticles();
			
			for (int i = 0; i < all.size(); ++i)
			{
				// F = [G m1 m2 / r^2] * normalize(off)
				// F = [G m1 m2 / r^2] * (off / r)
				// F = [G m1 m2 / r^3] * off
				
				T p = all.get(i);	
				
				_F.set(_pos);
				_F.subtract(p.getPosition());
				float r = _F.length();
				_F.multiply(G * _mass * p.getMass() / (r * r * r));
				
				p.apply(_F);
			}
		}
	}
	
	/** Gets the particle system whose particles this attractor affects */
	public ParticleSystem<T> getSystem()
	{
		return _system;
	}
	
	/** Sets the particle system whose particles this attractor affects */
	public void setSystem(ParticleSystem<T> s)
	{
		_system = s;
	}
	
	/** Gets the position of this attractor in virtual space */
	public Vector2 getPosition()
	{
		return _pos;
	}
	
	/** Sets the position of this attractor in virtual space */
	public void setPosition(Vector2 pos)
	{
		_pos = pos;
	}
	
	/** Gets the mass of this attractor */
	public float getMass()
	{
		return _mass;
	}
	
	/** Sets the mass of this attractor */
	public void setMass(float m)
	{
		_mass = m;
	}
}
