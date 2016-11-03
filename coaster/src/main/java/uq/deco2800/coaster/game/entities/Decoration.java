package uq.deco2800.coaster.game.entities;

import java.util.List;

import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.Sprite;

public class Decoration extends Entity {

	public Decoration(Sprite sprite) {
		setSprite(sprite);
		setBlocksOtherEntities(false);

		bounds = new AABB(posX, posY, sprite.getWidth() / 32, sprite.getHeight() / 32); // No collision for decoration

		setCollisionFilter(e -> false);
	}

	@Override
	protected void tick(long ms) {
		// Irrelevant
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		// Irrelevant
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// Irrelevant
	}

	@Override
	protected void onDeath(Entity cause) {
		// Irrelevant
	}
	
	@Override
	public void updatePhysics(long ms) {
		// Minimal physics on decorations (delete if above air)
		int tileHeight = this.getSprite().getHeight() / TileInfo.BLOCK_HEIGHT;
		TileInfo tile = World.getInstance().getTiles().get((int) posX, 
				(int) posY + tileHeight).getTileType(); 
		if (!tile.isObstacle()) {
			this.delete();
		}
	}
}
