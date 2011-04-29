package dkilian.andy;

import java.util.ArrayList;

/**
 * Interface for an object that controls one or more game world entities
 * 
 * @author dkilian
 */
public interface EntityController 
{
	/** Adds a target to this controller and makes this object the target entity's controller */
	public void add(Entity target);
	
	/** Removes a target from this controller and unsets its controller */
	public void remove(Entity target);
	
	/** Gets a collection of entities that are controlled by this object */
	public ArrayList<Entity> getTargets();
	
	/** Updates all objects managed by this controller */
	public void update(Kernel kernel, float dt, ArrayList<Entity> world, ArrayList<Constraint> constraints);
}
