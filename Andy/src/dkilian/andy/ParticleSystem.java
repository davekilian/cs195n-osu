package dkilian.andy;

import java.util.ArrayList;

/**
 * Manages a system of particles and objects that modify the behavior
 * of particles within the system
 * 
 * @param <T> The type of particle contained in this system
 * @author dkilian
 */
public class ParticleSystem<T extends Particle>
{
	/** The pool of free and available particles */
	private Pool<T> _particles;
	/** Every particle emitter in this system */
	private ArrayList<ParticleEmitter<T>> _emitters;
	/** Every particle attractor in this system */
	private ArrayList<ParticleAttractor<T>> _attractors;
	
	/** Creates a new particle system */
	public ParticleSystem()
	{
		_particles = new Pool<T>();
		_emitters = new ArrayList<ParticleEmitter<T>>();
		_attractors = new ArrayList<ParticleAttractor<T>>();
	}
	
	/**
	 * Runs a step of the particle simulation, emitting, attracting, and
	 * updating individual particles as necessary
	 * @param kernel The currently executing kernel
	 * @param dt The time elapsed, in partial seconds
	 */
	public void update(Kernel kernel, float dt)
	{
		// bool resetsForces(), to decide whether accel is fixed or force-dependent
		// if resetsForces(), resetForce() on each particle before running attractors
		
		// Emitters
		// Particle force reset
		// Attractors
		// Particle update
	}
	
	/**
	 * Draws each live particle in this simulation
	 * @param kernel The currently executing kernel
	 * @param dt The time elapsed, in partial seconds
	 */
	public void draw(Kernel kernel, float dt)
	{
		
	}
	
	/** Gets a reference to this system's internal list of particles, both live and free */
	public ArrayList<T> getParticles()
	{
		return _particles.getAll();
	}
	
	/** 
	 * Spawns a new particle. The particle retains its non-lifetime related state from when it 
	 * was last used. Usually called by ParticleEmitters rather than manually by the game
	 */
	public T spawn()
	{
		return _particles.alloc();
	}
	
	/** Adds a new slot to the pool of particles in this system, increasing the capacity of this system by one */
	public void add(T particle)
	{
		_particles.add(particle);
	}
	
	/** Removes a slot from the pool of particles in this sytem, decreasing the capacity of this sytem by one */
	public void remote(T particle)
	{
		_particles.remove(particle);
	}
	
	/** Adds an emitter to this system */
	public void add(ParticleEmitter<T> e)
	{
		//e.setSystem(this);
		_emitters.add(e);
	}
	
	/** Removes an emitter from this system */
	public void remote(ParticleEmitter<T> e)
	{
		//if (e.getSystem() == this)
		//    e.setSystem(null);
		
		_emitters.remove(e);
	}
	
	/** Adds an attractor/repulsor to this sytem */
	public void add(ParticleAttractor<T> a)
	{
		a.setSystem(this);
		_attractors.add(a);
	}
	
	/** Removes an attractor/repulsor from this styem */
	public void remote(ParticleAttractor<T> a)
	{
		if (a.getSystem() == this)
			a.setSystem(null);
		
		_attractors.remove(a);
	}
}
