package dkilian.andy;

import java.util.ArrayList;

/**
 * Common interface for a constraint that can be applied to the movement of Entity objects
 * @author dkilian
 */
public interface Constraint 
{
	/**
	 * Applies this constraint
	 * @param world The game world to apply this constraint to
	 * @return True if no action was required, false otherwise
	 */
	public boolean apply(ArrayList<Entity> world);
}
