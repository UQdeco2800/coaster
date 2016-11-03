package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Block that deals damage to any BasicMovingEntities that touch it. Used in the puzzle room
 */
public class Spikes extends Entity {

	public Spikes() {
		initEntity(SpriteList.SPIKES, false, e -> true);
	}

	@Override
	protected void tick(long ms) {
		// Nothing needs to be done
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		entities.stream()
				.filter(entity -> entity instanceof BasicMovingEntity)
				.forEach(entity -> ((BasicMovingEntity) entity).addHealth(-1, this));
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// Nothing needs to be done
	}

	@Override
	protected void onDeath(Entity cause) {
		// Nothing needs to be done
	}
}
