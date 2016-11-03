package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class DropThroughPlatform extends TerrainEntity {
	private static float margin = 0.1f; //broad bc we want all collisions from dropping down
	
	public DropThroughPlatform(float width, float height) {
		initEntity(SpriteList.PLATFORM_DROP, true, e -> true);
		enableGravity = false;
	}

	public boolean collidesWith(Entity e, Side side) {
		boolean crouching = true;
		if (e instanceof Player) {
			Player p = (Player) e;
			crouching = !(p.getCurrentState() == EntityState.CROUCHING
					|| p.getCurrentState() == EntityState.AIR_CROUCHING);
		}
		return (side == Side.BOTTOM) &&
				e.getBounds().bottom() < bounds.top() + margin && crouching;
	}
	
	@Override
	public void updatePhysics(long ms) {
		// do nothing
	}

	@Override
	public void applyAffect(Entity entity) {
		// do nothing
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		// do nothing
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// do nothing
	}

	@Override
	protected void onDeath(Entity cause) {
		// do nothing
	}

}
