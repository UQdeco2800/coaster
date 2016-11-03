package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * A pushable crate for use in the puzzle room
 */
public class Crate extends TerrainEntity {
	private static final float MARGIN = 0.1f;
	
	public Crate() {
		initEntity(SpriteList.CRATE, true, e -> true);
	}

	/**
	 * Moves this entity if there is a collision
	 * Gives the appearance of being pushed.
	 * We check terrain to make sure we don't get pushed into a wall
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity.getBounds().posX() < this.bounds.posX()) {
				setX(entity.getBounds().right());
				basicRightEdgeCheck();
				
			} else if (entity.getBounds().posX() > this.bounds.posX()) {
				this.bounds.setRightX(entity.getBounds().left());
				setX(bounds.left());
				basicLeftEdgeCheck();

			}
			setX(bounds.left());
			setY(bounds.top());
		}
	}

	/**
	 * This allows entities to stand on the crate
	 */
	@Override
	public boolean collidesWith(Entity e, Side side) {
		return (side == Side.BOTTOM) &&	e.getBounds().bottom() < bounds.top() + MARGIN;
	}

}
