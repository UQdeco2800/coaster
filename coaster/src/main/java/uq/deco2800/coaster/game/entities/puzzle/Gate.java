package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * A gate that bars the player for reaching something in a puzzle.
 */
public class Gate extends TerrainEntity {
	public Gate() {
		initEntity(SpriteList.GATE, false, e -> false);
		enableGravity = false;
	}

	@Override
	protected void tick(long ms) {
		// Nothing needs to be done
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		// Nothing needs to be done
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// Nothing needs to be done
	}

	@Override
	protected void onDeath(Entity cause) {
		// Nothing needs to be done
	}

	@Override
	public boolean collidesWith(Entity e, Side side) {
		// TODO Auto-generated method stub
		return side == Side.LEFT || side == Side.RIGHT;
	}

	@Override
	public void applyAffect(Entity entity) {
		// TODO Auto-generated method stub
		
	}
}
