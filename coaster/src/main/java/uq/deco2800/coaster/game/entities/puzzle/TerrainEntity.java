package uq.deco2800.coaster.game.entities.puzzle;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.Side;


/**
 * An entity that can act like terrain - collides with entities just like tiles provided you set them up right
 * The can have added functionality, e.g. moving platforms and drop through platforms
 * @author Cieran
 *
 */
public abstract class TerrainEntity extends Entity {
	List<Entity> affectedEntities = new ArrayList<>();
	
	/**
	 * Returns true if this terrain entity should collide with the input entity, if it approaches at the given side
	 * @param e				an entity we want to check if we should collide with
	 * @param side			the side the entity is colliding on
	 * @return 				true if there should be a collision
	 */
	public abstract boolean collidesWith(Entity e, Side side);
	
	/** 
	 * A vastly simplified tick
	 * <p>
	 * Applies physics, then applies its effects to the any entities who collided with it on the previous tick
	 * @param ms 		time since the last tick 
	 */
	@Override
	protected void tick(long ms) {
		updatePhysics(ms);
		for (Entity entity : affectedEntities) {
			applyAffect(entity);
		}
		affectedEntities.clear();
	}
		
	/**
	 * checks if we already know about the entity before adding it to our list of entities to deal with on next tick
	 * @param entity			an entity that is currently colliding with this terrain entity,
	 */
	public void addToList(Entity entity) {
		if (!affectedEntities.contains(entity)) {
			affectedEntities.add(entity);
		}
	}
	
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// Nothing needs to be done
	}
	
	@Override
	protected void onDeath(Entity cause) {
		// Nothing needs to be done
	}

	public void applyAffect(Entity entity) {
		// Nothing needs to be done
	}

}
