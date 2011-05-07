package dkilian.andy;

/**
 * Base class for a particle, a small graphical primitive which is
 * combined with many other particles to produce complex effects like
 * water or smoke.
 * 
 * This base class implements a particle with translational and rotational
 * kinematics, using Eulerian integration
 * 
 * @author dkilian
 */
public class Particle implements Poolable
{
	/** The origin of this particle, in virtual coordinates */
	protected Vector2 _pos;
	/** The velocity of this particle, in virtual coordinates per second */
	protected Vector2 _vel;
	/** The acceleration of this particle, in virtual coordinates per second per second */
	protected Vector2 _accel;
	/** The translation drag coefficient: B, in a = -Bv/m */
	protected float _drag;
	/** The rotation of this particle around its central axis, in degrees */
	protected float _rot;
	/** The rotational velocity of this particle around its central axis, in degrees per second */
	protected float _rotvel;
	/** The rotational acceleration of this particle around its central axis, in degrees per second per second */
	protected float _rotaccel;
	/** The rotational drag coefficient: B, in a = -Bv/m */
	protected float _rotdrag;
	/** The mass of this particle, in any unit system (just be consistent) */
	protected float _mass;
	/** The sprite this particle uses to draw itself. May (and should be!) shared across multiple particles */
	protected Sprite _sprite;
	/** The age of this particle (since spawn time) in partial seconds */
	protected float _lifetime;
	/** The age at which this particle dies, in partial seconds */
	protected float _lifespan;
	/** This particle's visual scale factors */
	protected Vector2 _scale;
	/** Temporary values to avoid runtime GC allocations */
	protected Vector2 _tmp;
	
	/** Creates a particle with no sprite */
	public Particle()
	{
		_pos = Vector2.Zero();
		_vel = Vector2.Zero();
		_accel = Vector2.Zero();
		_drag = 0.f;
		_rot = 0.f;
		_rotvel = 0.f;
		_rotaccel = 0.f;
		_rotdrag = 0.f;
		_mass = 1.f;
		_sprite = null;
		_lifetime = 0.f;
		_lifespan = 0.f;
		_scale = Vector2.One();
		_tmp = new Vector2();
	}
	
	/** Creates a particle that renders the given sprite */
	public Particle(Sprite sprite)
	{
		_pos = Vector2.Zero();
		_vel = Vector2.Zero();
		_accel = Vector2.Zero();
		_drag = 0.f;
		_rot = 0.f;
		_rotvel = 0.f;
		_rotaccel = 0.f;
		_rotdrag = 0.f;
		_mass = 1.f;
		_sprite = sprite;
		_lifetime = 0.f;
		_lifespan = 0.f;
		_scale = Vector2.One();
		_tmp = new Vector2();
	}
	
	/**
	 * Updates this sprite, doing its per-frame behavior
	 * @param kernel The currently executing kernel
	 * @param dt The elapsed time, in partial seconds
	 */
	public void update(Kernel kernel, float dt)
	{
		_lifetime += dt;
		
		if (_lifetime < _lifespan)
		{
			// x = x_0 + v_0 t
			_tmp.set(_vel);
			_tmp.multiply(dt);
			_pos.add(_tmp);
			
			_rot += _rotvel * dt;
		
			// v = v_0 + a_0 t
			_tmp.set(_accel);
			_tmp.multiply(dt);
			_vel.add(_tmp);
			
			_rotvel += _rotaccel * dt;
			
			// a = a_0 - (B v) / m
			_tmp.set(_vel);
			_tmp.multiply(-_drag / _mass);
			_accel.add(_tmp);
			
			_rotaccel -= _rotvel * _rotdrag / _mass;
		}
	}
	
	/**
	 * Draws this sprite, if it is alive
	 * @param kernel The currently executing kernel
	 * @param dt The elapsed time, in partial seconds
	 */
	public void draw(Kernel kernel, float dt)
	{
		_sprite.getScale().set(_scale);
		_sprite.setRotation(_rot);
		_sprite.getTranslation().set(_pos);
		_sprite.draw(kernel);
	}
	
	/**
	 * Applies a force to this particle. Note that this modifies this particle's
	 * acceleration property.
	 * @param force The force vector to apply
	 */
	public void apply(Vector2 force)
	{
		_tmp.set(force);
		_tmp.divide(_mass);
		_accel.add(_tmp);
	}
	
	/**
	 * Sets this particle's net force to zero. Note that this operation zeroes this
	 * particle's acceleration property
	 */
	public void resetForce()
	{
		_accel.x = _accel.y = 0.f;
	}
	
	/** Gets the sprite this particle draws */
	public Sprite getSprite()
	{
		return _sprite;
	}
	
	/** Sets the sprite this particle draws */
	public void setSprite(Sprite s)
	{
		_sprite = s;
	}
	
	/** Gets the position of this particle in virtual space */
	public Vector2 getPosition()
	{
		return _pos;
	}

	/** Sets the position of this particle in virtual space */
	public void setPosition(Vector2 p)
	{
		_pos = p;
	}
	
	/** Gets this particle's translational velocity, in virtual coordinates per second */
	public Vector2 getVelocity()
	{
		return _vel;
	}
	
	/** Sets this particle's translational velocity, in virtual coordinates per second */
	public void setVelocity(Vector2 v)
	{
		_vel = v;
	}
	
	/** Gets this particle's translational acceleration, in virtual coordinates per second per second */
	public Vector2 getAcceleration()
	{
		return _accel;
	}
	
	/** Sets this particle's translational acceleration, in virtual coordinates per second per second */
	public void setAcceleration(Vector2 a)
	{
		_accel = a;
	}
	
	/** Gets this particle's translational drag coefficient: B, in a = -Bv/m */
	public float getDrag()
	{
		return _drag;
	}
	
	/** Sets this particle's translational drag coefficient: B, in a = -Bv/m */
	public void setDrag(float d)
	{
		_drag = d;
	}
	
	/** Gets this particle's rotation about its center in degrees */
	public float getRotation()
	{
		return _rot;
	}
	
	/** Sets this particle's rotation about its center in degrees */
	public void setRotation(float r)
	{
		_rot = r;
	}
	
	/** Gets this particle's rotational velocity about its center in degrees per second */
	public float getAngularVelocity()
	{
		return _rotvel;
	}
	
	/** Sets this particle's rotational velocity about its center in degrees per second */
	public void setAngularVelocity(float v)
	{
		_rotvel = v;
	}
	
	/** Gets this particle's rotational acceleration about its center in degrees per second per second */
	public float getAngularAcceleration()
	{
		return _rotaccel;
	}

	/** Sets this particle's rotational acceleration about its center in degrees per second per second */
	public void setAngularAcceleration(float a)
	{
		_rotaccel = a;
	}
	
	/** Gets this particle's angular drag coefficient: B, in a = -Bv/m */
	public float getAngularDrag()
	{
		return _rotdrag;
	}
	
	/** Sets this particle's angular drag coefficient: B, in a = -Bv/m */
	public void setAngularDrag(float d)
	{
		_rotdrag = d;
	}
	
	/** Gets this particle's mass in mass units (which are arbitrary, just be consistent!) */
	public float getMass()
	{
		return _mass;
	}
	
	/** Sets this particle's mass in mass units (which are arbitrary, just be consistent!) */
	public void setMass(float m)
	{
		_mass = m;
	}
	
	/** Gets the amount of time in seconds that this particle has been alive (i.e. since it was spawned) */
	public float getLifetime()
	{
		return _lifetime;
	}
	
	/** Sets the amount of time in seconds that this particle has been alive (i.e. since it was spawned) */
	public void setLifetime(float t)
	{
		_lifetime = t;
	}
	
	/** Gets the span of time this particle stays alive in partial seconds */
	public float getLifespan()
	{
		return _lifespan;
	}
	
	/** Sets the span of time this particle stays alive in partial seconds */
	public void setLifespan(float span)
	{
		_lifespan = span;
	}
	
	/** Gets this particle's visual scale factors */
	public Vector2 getScale()
	{
		return _scale;
	}
	
	/** Sets this particle's visual scale factors */
	public void setScale(Vector2 s)
	{
		_scale = s;
	}

	/** Gets a value indicating whether this particle is alive */
	@Override
	public boolean isAlive() 
	{
		return _lifetime < _lifespan;
	}

	/** Allocates this particle. This operation resets this particle's lifetime to zero */
	@Override
	public void alloc() 
	{
		_lifetime = 0.f;
	}

	/** Un-allocates this particle. This operation modifies the particle's current lifetime */
	@Override
	public void free() 
	{
		_lifetime = _lifespan;
	}
}
