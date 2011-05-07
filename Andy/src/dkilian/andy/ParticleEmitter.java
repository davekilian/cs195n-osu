package dkilian.andy;

import java.util.Random;

import android.util.FloatMath;

/**
 * Base class for an object that emits particles new particles
 * into the system over time.
 * 
 * The base class implementation consists of a fully randomized
 * emitter, in which each parameter of the particles spawned (as
 * well the as the spawning behavior) is chosen from a range of
 * possible values using a random number generator.
 *
 * @param <T> The type of particle this emitter emits
 * @author dkilian
 */
public class ParticleEmitter<T extends Particle>
{
	/** Gets the particle system this emits particles into */
	protected ParticleSystem<T> _system;
	/** The number of particles that can be spawned in a single batch */
	protected Range<Integer> _particlesPerBatch;
	/** The interval between batches, in seconds */
	protected Range<Float> _spawnInterval;
	/** The point where new particles are spawned, in virtual space */
	protected Range<Vector2> _spawnPoint;
	/** The speed at which new particles are spawned, in virtual coordinates per second */
	protected Range<Float> _speed;
	/** The direction in which new particles move when spawned, in degrees */
	protected Range<Float> _direction;
	/** The acceleration at which new particles are spawned, in virtual coordinates per second per second */
	protected Range<Float> _acceleration;
	/** The direction in which new particles accelerate when spawned, in degrees  */
	protected Range<Float> _accelerationDirection;
	/** The rotation about the center at which particles are spawned */
	protected Range<Float> _rotation;
	/** The angular velocity at which new particles are spawned, in degrees per second */
	protected Range<Float> _angularVelocity;
	/** The angular acceleration at which new particles are spawned, in degrees per second per second */
	protected Range<Float> _angularAcceleration;
	/** The mass at which new particles are spawned */
	protected Range<Float> _mass;
	/** The lifespan of new particles, in seconds */
	protected Range<Float> _lifespan;
	/** The scale factors applied to new particles */
	protected Range<Vector2> _scale;
	
	/** The current random number generator */
	protected Random _random;
	/** The number of seconds that must elapse before more particles are spawned */
	protected float _timeUntilNextBatch;
	
	/** Linearly interpolates over a range of float values */
	protected float lerpf(Range<Float> r, float v)
	{
		return (1 - v) * r.getMin() + v * r.getMax();
	}
	
	/** Linearly interpolates over a range of integer values */
	protected int lerpi(Range<Integer> r, float v)
	{
		return (int)((1 - v) * r.getMin() + v * r.getMax());
	}
	
	/** Linearly interpolates over a range of 2D vector values */
	protected void lerpv(Range<Vector2> r, float v, Vector2 out)
	{
		out.x = (1 - v) * r.getMin().x + v * r.getMax().x;
		out.y = (1 - v) * r.getMin().y + v * r.getMax().y;
	}
	
	/** Chooses a random value within the given range */
	protected float randf(Range<Float> r)
	{
		return lerpf(r, _random.nextFloat());
	}

	/** Chooses a random value within the given range */
	protected int randi(Range<Integer> r)
	{
		return lerpi(r, _random.nextFloat());
	}

	/** Chooses a random value within the given range */
	protected void randv(Range<Vector2> r, Vector2 v)
	{
		lerpv(r, _random.nextFloat(), v);
	}
	
	/** Creates a new particle emitter */
	public ParticleEmitter()
	{
		_system = null;
		_particlesPerBatch = new Range<Integer>(0, 0);
		_spawnInterval = new Range<Float>(0.f, 0.f);
		_spawnPoint = new Range<Vector2>(Vector2.Zero(), Vector2.Zero());
		_speed = new Range<Float>(0.f, 0.f);
		_direction = new Range<Float>(0.f, 0.f);
		_acceleration = new Range<Float>(0.f, 0.f);
		_accelerationDirection = new Range<Float>(0.f, 0.f);
		_rotation = new Range<Float>(0.f, 0.f);
		_angularVelocity = new Range<Float>(0.f, 0.f);
		_angularAcceleration = new Range<Float>(0.f, 0.f);
		_mass = new Range<Float>(0.f, 0.f);
		_lifespan = new Range<Float>(0.f, 0.f);
		_scale = new Range<Vector2>(Vector2.One(), Vector2.One());
	}
	
	/**
	 * Updates this emitter, allowing it to emit particles if it wants to
	 * @param kernel The currently executing kernel
	 * @param dt The elapsed time, in partial seconds
	 */
	public void update(Kernel kernel, float dt)
	{
		_timeUntilNextBatch -= dt;
		
		if (_timeUntilNextBatch < 0)
		{
			_timeUntilNextBatch = randf(_spawnInterval);
			
			int batchSize = randi(_particlesPerBatch);
			for (int i = 0; i < batchSize; ++i)
			{
				T p = _system.spawn();
				if (p == null)
					break;
				
				randv(_spawnPoint, p.getPosition());
				
				float vdir = randf(_direction) * 180.f / (float)Math.PI;
				p.getVelocity().x = FloatMath.cos(vdir);
				p.getVelocity().y = FloatMath.sin(vdir);
				p.getVelocity().multiply(randf(_speed));
				
				float adir = randf(_accelerationDirection) * 180.f / (float)Math.PI;
				p.getAcceleration().x = FloatMath.cos(adir);
				p.getAcceleration().y = FloatMath.sin(adir);
				p.getAcceleration().multiply(randf(_acceleration));
				
				p.setRotation(randf(_rotation));
				p.setAngularVelocity(randf(_angularVelocity));
				p.setAngularAcceleration(randf(_angularAcceleration));
				
				p.setMass(randf(_mass));
				p.setLifespan(randf(_lifespan));
				
				randv(_scale, p.getScale());
			}
		}
	}
	
	/** Gets the random number generator driving this emitter */
	public Random getRandom()
	{
		return _random;
	}
	
	/** Sets the random number generator driving this emitter */
	public void setRandom(Random r)
	{
		_random = r;
	}
	
	/** Gets the particle system this emitter emits particles into */
	public ParticleSystem<T> getSystem()
	{
		return _system;
	}
	
	/** Sets the particle system this emitter emits particles into */
	public void setSystem(ParticleSystem<T> system)
	{
		_system = system;
	}
	
	/** Gets the number of particles emitted in a single batch of this emitter */
	public Range<Integer> getParticlesPerBatch()
	{
		return _particlesPerBatch;
	}

	/** Sets the number of particles emitted in a single batch of this emitter */
	public void setParticlesPerBatch(Range<Integer> r)
	{
		_particlesPerBatch.copyFrom(r);
	}

	/** Sets the number of particles emitted in a single batch of this emitter */
	public void setParticlesPerBatch(int min, int max)
	{
		_particlesPerBatch.set(min, max);
	}
	
	/** Gets the time, in seconds, between particle batches */
	public Range<Float> getSpawnInterval()
	{
		return _spawnInterval;
	}

	/** Sets the time, in seconds, between particle batches */
	public void setSpawnInterval(Range<Float> r)
	{
		_spawnInterval.copyFrom(r);
	}

	/** Sets the time, in seconds, between particle batches */
	public void setSpawnInterval(float min, float max)
	{
		_spawnInterval.set(min, max);
	}
	
	/** Gets the point at which particles emitted by this object spawn */
	public Range<Vector2> getSpawnPoint()
	{
		return _spawnPoint;
	}

	/** Sets the point at which particles emitted by this object spawn */
	public void setSpawnPoint(Range<Vector2> r)
	{
		_spawnPoint.copyFrom(r);
	}
	
	/** Sets the point at which particles emitted by this object spawn */
	public void setSpawnPoint(Vector2 min, Vector2 max)
	{
		_spawnPoint.set(min, max);
	}
	
	/** Gets the speed, in virtual coordinates per second, at which particles spawned by this emitter move */
	public Range<Float> getSpeed()
	{
		return _speed;
	}

	/** Sets the speed, in virtual coordinates per second, at which particles spawned by this emitter move */
	public void setSpeed(Range<Float> r)
	{
		_speed.copyFrom(r);
	}

	/** Sets the speed, in virtual coordinates per second, at which particles spawned by this emitter move */
	public void setSpeed(float min, float max)
	{
		_speed.set(min, max);
	}
	
	/** Gets the direction, in degrees, in which particles spawned by this emitter move */
	public Range<Float> getDirection()
	{
		return _direction;
	}

	/** Sets the direction, in degrees, in which particles spawned by this emitter move */
	public void setDirection(Range<Float> r)
	{
		_direction.copyFrom(r);
	}

	/** Sets the direction, in degrees, in which particles spawned by this emitter move */
	public void setDirection(float min, float max)
	{
		_direction.set(min, max);
	}
	
	/** Gets the acceleration, in virtual coordinates per second per second, at which particles spawned by this emitter move */
	public Range<Float> getAcceleration()
	{
		return _acceleration;
	}

	/** Sets the acceleration, in virtual coordinates per second per second, at which particles spawned by this emitter move */
	public void setAcceleration(Range<Float> r)
	{
		_acceleration.copyFrom(r);
	}

	/** Sets the acceleration, in virtual coordinates per second per second, at which particles spawned by this emitter move */
	public void setAcceleration(float min, float max)
	{
		_acceleration.set(min, max);
	}
	
	/** Gets the direction in which particles spawned by this emitter accelerate */
	public Range<Float> getAccelerationDirection()
	{
		return _accelerationDirection;
	}

	/** Sets the direction in which particles spawned by this emitter accelerate */
	public void setAccelerationDirection(Range<Float> r)
	{
		_accelerationDirection.copyFrom(r);
	}

	/** Sets the direction in which particles spawned by this emitter accelerate */
	public void setAccelerationDirection(float min, float max)
	{
		_accelerationDirection.set(min, max);
	}
	
	/** Gets the amount by which particles spawned by this emitter are rotated, in degrees */
	public Range<Float> getRotation()
	{
		return _rotation;
	}

	/** Sets the amount by which particles spawned by this emitter are rotated, in degrees */
	public void setRotation(Range<Float> r)
	{
		_rotation.copyFrom(r);
	}

	/** Sets the amount by which particles spawned by this emitter are rotated, in degrees */
	public void setRotation(float min, float max)
	{
		_rotation.set(min, max);
	}
	
	/** Gets the speed in which particles spawned by this emitter rotate, in degrees per second */
	public Range<Float> getAngularVelocity()
	{
		return _angularVelocity;
	}

	/** Sets the speed in which particles spawned by this emitter rotate, in degrees per second */
	public void setAngularVelocity(Range<Float> r)
	{
		_angularVelocity.copyFrom(r);
	}

	/** Sets the speed in which particles spawned by this emitter rotate, in degrees per second */
	public void setAngularVelocity(float min, float max)
	{
		_angularVelocity.set(min, max);
	}
	
	/** Gets the rate at which particles spawned by this emitter change in rotation speed, in degrees per second per second */
	public Range<Float> getAngularAcceleration()
	{
		return _angularAcceleration;
	}

	/** Sets the rate at which particles spawned by this emitter change in rotation speed, in degrees per second per second */
	public void setAngularAcceleration(Range<Float> r)
	{
		_angularAcceleration.copyFrom(r);
	}

	/** Sets the rate at which particles spawned by this emitter change in rotation speed, in degrees per second per second */
	public void setAngularAcceleration(float min, float max)
	{
		_angularAcceleration.set(min, max);
	}
	
	/** Gets the mass of particles spawned by this emitter */
	public Range<Float> getMass()
	{
		return _mass;
	}
	
	/** Sets the mass of particles spawned by this emitter */
	public void setMass(Range<Float> m)
	{
		_mass.copyFrom(m);
	}
	
	/** Sets the mass of particles spawned by this emitter */
	public void setMass(float min, float max)
	{
		_mass.set(min, max);
	}
	
	/** Gets the lifespan of particles spawned by this emitter, in seconds */
	public Range<Float> getLifespan()
	{
		return _lifespan;
	}

	/** Sets the lifespan of particles spawned by this emitter, in seconds */
	public void setLifespan(Range<Float> r)
	{
		_lifespan.copyFrom(r);
	}
	
	/** Sets the lifespan of particles spawned by this emitter, in seconds */
	public void setLifespan(float min, float max)
	{
		_lifespan.set(min, max);
	}
	
	/** Gets the visual scale factors of particles spawned by this emitter */
	public Range<Vector2> getScale()
	{
		return _scale;
	}

	/** Sets the visual scale factors of particles spawned by this emitter */
	public void setScale(Range<Vector2> r)
	{
		_scale.copyFrom(r);
	}

	/** Sets the visual scale factors of particles spawned by this emitter */
	public void setScale(Vector2 min, Vector2 max)
	{
		_scale.set(min, max);
	}
}
