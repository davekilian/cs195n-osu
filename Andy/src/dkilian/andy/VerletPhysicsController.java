package dkilian.andy;

import java.util.ArrayList;

/**
 * Applies particle kinematics to game world entities using Verlet integration
 * @author dkilian
 */
public class VerletPhysicsController implements EntityController
{
	/** The length of a single Verlet timestep, in update() */
	public static final float DEFAULT_TIMESTEP = 0.01f;
	/** The number of times the constraint relaxation loop will attempt to fulfill an attempt before abandoning it, in update() */
	public static final int DEFAULT_CONSTRAINT_ATTEMPTS = 1;
	
	/** The targets affected by this physics controller */
	private ArrayList<Entity> _targets;
	/** The length of a single Verlet timestep */
	private float _timestep;
	/** The number of times the constraint relaxation loop will attempt to fulfill an attempt before abandoning it, in update() */
	private int _constraintAttempts;
	/** The drag coefficient of the system */
	private float _drag;
	
	/** Temporary calculation vectors, preallocated for performance */
	Vector2 _tmp1;
	Vector2 _tmp2;
	Vector2 _tmp3;
	
	/** Creates a new Verlet integration-based Entity controller */
	public VerletPhysicsController()
	{
		_targets = new ArrayList<Entity>();
		_timestep = DEFAULT_TIMESTEP;
		_constraintAttempts = DEFAULT_CONSTRAINT_ATTEMPTS;
		_drag = 0.f;
		_tmp1 = new Vector2();
		_tmp2 = new Vector2();
		_tmp3 = new Vector2();
	}
	
	/** Gets the length of a single Verlet timestep, in partial seconds */
	public float getTimestep()
	{
		return _timestep;
	}

	/** Sets the length of a single Verlet timestep, in partial seconds */
	public void setTimestep(float timestep)
	{
		_timestep = timestep;
	}
	
	/** Gets the maximum number of times the controller will go through the constraint relaxation loop before returning from update() */
	public int getConstraintAttemtps()
	{
		return _constraintAttempts;
	}

	/** Sets the maximum number of times the controller will go through the constraint relaxation loop before returning from update() */
	public void setConstraintAttempts(int attempts)
	{
		_constraintAttempts = attempts;
	}
	
	/** Gets the system's drag coefficent */
	public float getDrag()
	{
		return _drag;
	}

	/** Sets the system's drag coefficent */
	public void setDrag(float drag)
	{
		_drag = drag;
	}
	
	/** Adds an entity to the list of entities controlled by this controller */
	@Override
	public void add(Entity target) 
	{
		if (target.getController() != this)
		{
			if (target.getController() != null)
				target.getController().remove(target);
			
			_targets.add(target);
			
			target.setController(this);
			if (target.getTag() == null || target.getTag().getClass() != VerletTag.class)
				target.setTag(new VerletTag());
			
			if (target.isNonRigid())
			{
				for (int i = 0; i < target.getNonRigidPoints().size(); ++i)
				{
					NonRigidBodyPoint pt = target.getNonRigidPoints().get(i);
					if (pt.getTag() == null || pt.getTag().getClass() != VerletTag.class)
						pt.setTag(new VerletTag());
				}
			}
		}
	}

	/** Removes an entity from the list of entities controlled by this controller */
	@Override
	public void remove(Entity target) 
	{
		_targets.remove(target);
		target.setController(null);
	}

	/** Gets the collection of entities controlled by this controller */
	@Override
	public ArrayList<Entity> getTargets() 
	{
		return _targets;
	}

	/** Updates this controller's target entities */
	@Override
	public void update(Kernel kernel, float dt, ArrayList<Entity> world, ArrayList<Constraint> constraints) 
	{		
		int count = (int)(dt / _timestep);
		
		for (int i = 0; i < count; ++i)
		{
			for (int j = 0; j < _targets.size(); ++j)
			{
				Entity e = _targets.get(j);
				if (e.isNonRigid())
				{
					for (int k = 0; k < e.getNonRigidPoints().size(); ++k)
					{
						NonRigidBodyPoint pt = e.getNonRigidPoints().get(k);
						VerletTag t = (VerletTag)pt.getTag();
						
						Vector2 pos = pt.getPosition();
						Vector2 prev = t.getPreviousPosition();
						
						_tmp1.set(t.getAcceleration());
						_tmp2.set(prev);
						
						prev.set(pos);
						pos.multiply(2.f - _drag).subtract(_tmp2.multiply(1.f - _drag)).add(_tmp1.multiply(_timestep * _timestep));
					}
				}
				else
				{
					VerletTag t = (VerletTag)e.getTag();
					
					Vector2 pos = e.getPosition();
					Vector2 prev = t.getPreviousPosition();
					
					_tmp1.set(t.getAcceleration());
					_tmp2.set(prev);
					
					prev.set(pos);
					pos.multiply(2.f - _drag).subtract(_tmp2.multiply(1.f - _drag)).add(_tmp1.multiply(_timestep * _timestep));
				}
			}
			
			for (int j = 0; j < _constraintAttempts; ++j)
			{
				for (int k = 0; k < constraints.size(); ++k)
					constraints.get(k).apply(world);
				
				for (int k = 0; k < _targets.size(); ++k)
				{
					Entity e = _targets.get(k);
					if (e.isNonRigid())
						for (int l = 0; l < e.getSprings().size(); ++l)	// lol
							e.getSprings().get(l).apply(world);
				}
			}
		}
	}
}
