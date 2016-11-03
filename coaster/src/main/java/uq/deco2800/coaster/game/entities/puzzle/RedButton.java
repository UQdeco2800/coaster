package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.world.PuzzleRoom;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Big red button that when an entity lands on top of, will activate.
 */
public class RedButton extends TerrainEntity {
	public RedButton() {
		initEntity(SpriteList.RED_BUTTON, true, e -> true);
	}

	@Override
	public boolean collidesWith(Entity e, Side side) {
		return false;
	}

	@Override
	protected void tick(long ms) {
		// Nothing needs to be done
	}

	@Override
	public void applyAffect(Entity entity) {
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (entities.stream().anyMatch(e -> e instanceof Crate)) {
			((PuzzleRoom)World.getInstance()).removeGate(0);
		}
	}
}
