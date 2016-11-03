package uq.deco2800.coaster.game.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteRelation;

public abstract class StateEntity extends Entity {
	// Map of EntityState -> Sprite
	protected Map<EntityState, Sprite> sprites = new HashMap<>();
	// The current state of the entity
	protected EntityState currentState;

	//Map of entityState -> Additional Sprites
	//Allows the entity to have different sets of additional sprites for different states
	protected Map<EntityState, Map<BodyPart, SpriteRelation>> additionalSpritesCache = new HashMap<>();
	
	// Map of state -> additional hitboxes
	protected Map<EntityState, List<AABB>> hitboxesCache = new HashMap<>();

	public StateEntity() {}

	@Override
	protected void tick(long ms) {
		stateUpdate(ms);
	}

	protected abstract void stateUpdate(long ms);

	/**
	 * Sets the entity's additional sprites to the input map
	 */
	public void setAdditionalSprites(Map<BodyPart, SpriteRelation> additionalSprites) {
		this.additionalSprites = additionalSprites;
	}
	
	public void setAdditionalHitboxes(List<AABB> hitboxes) {
		this.hitboxes = hitboxes;
	}
	
	/**
	 * Sets the entity's state to the input value. Also handles sprite changes
	 */
	public void setState(EntityState state) {
		currentState = state;
		setSprite(sprites.get(state));
		if (additionalSpritesCache.containsKey(state)) {
			setAdditionalSprites(additionalSpritesCache.get(state));
		} else {
			setAdditionalSprites(null);
		}
		if (hitboxesCache.containsKey(state)) {
			setAdditionalHitboxes(hitboxesCache.get(state));
		} else {
			setAdditionalHitboxes(null);
		}
	}
	
	@Override
	public void removeHitbox(BodyPart armourPart) {
		for (List<AABB> hitboxes : hitboxesCache.values()) {
			for (AABB hitbox : hitboxes) {
				if (hitbox.getBodyPart() == armourPart) {
					hitboxes.remove(hitbox);
				}
			}
		}
	}


	/**
	 * returns the current state
	 *
	 * @return the current state
	 */
	public EntityState getCurrentState() {
		return currentState;
	}
}
