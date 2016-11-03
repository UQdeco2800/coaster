package uq.deco2800.coaster.game.tiles;

import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Entity;

public class TileShifter extends TileSolid {
	public TileShifter() {
		super(Tiles.SHIFTER);
		this.setFilename("FORWARD", "forward-shifter.png");
		this.setFilename("BACKWARD", "backward-shifter.png");
	}

	@Override
	public void onEntityOnTopOfBlock(Entity entity, Tile tile, long ms) {
		if (entity instanceof BasicMovingEntity) {
			if ("FORWARD".equals(tile.getVariant())) {
				entity.setVelocity(3 + entity.getVelX(), entity.getVelY());
			} else {
				entity.setVelocity(-3 + entity.getVelX(), entity.getVelY());
			}
		}
	}

	@Override
	public int getBlockWidth() {
		return 30;
	}

	@Override
	public int getBlockHeight() {
		return 30;
	}

	@Override
	public int getNumSpriteFrames() {
		return 6;
	}

	@Override
	public int getSpriteFrameDuration() {
		return 1000 / 6;
	}
}
