package uq.deco2800.coaster.game.entities.lighting;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.Decoration;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.tiles.Tile;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.Sprite;

/**
 * Created by Callum on 6/10/2016.
 */
public class Light extends Decoration {
	private int radius;
	int maxLight = 0;
	List<TileLight> affectedTiles = new ArrayList<>();

	public Light(Sprite sprite, int radius) {
		super(sprite);
		this.radius = radius;

		illuminate();
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		illuminate();
	}

	@Override
	protected void tick(long ms) {
		illuminate();
	}

	@Override
	public void updatePhysics(long ms) {
		// Minimal physics on decorations (delete if above air)
		int tileHeight = this.getSprite().getHeight() / TileInfo.BLOCK_HEIGHT;
		TileInfo tile = World.getInstance().getTiles().get((int) posX,
				(int) posY + tileHeight).getTileType();
		if (!tile.isObstacle()) {
			for (TileLight oldTile : affectedTiles) {
				oldTile.getTile().removeLightLevel(oldTile.getLight());
			}
			this.delete();
		}
	}

	private void illuminate() {
		List<TileLight> oldTiles = new ArrayList<>(affectedTiles);
		affectedTiles = new ArrayList<>();
		int flooredLeft = (int) Math.floor(this.bounds.ownerLeft());
		int ceilRight = (int) Math.ceil(this.bounds.ownerRight());
		int flooredTop = (int) Math.floor(this.bounds.ownerTop());
		int ceilBottom = (int) Math.ceil(this.bounds.ownerBottom());
		for (int x = flooredLeft - radius; x < ceilRight + radius; x++) {
			for (int y = flooredTop - radius; y < ceilBottom + radius; y++) {
				if (!world.getTiles().test(x, y)) {
					// check for invalid lookups
					continue;
				}
				Tile currentTile = world.getTiles().get(x, y);
				float deltaX = x - this.getX();
				float deltaY = y - this.getY();
				double distance = Math.sqrt((Math.pow(deltaX, 2)) + (Math.pow(deltaY, 2)));
				int light = (int)(maxLight + 30*(distance/Math.sqrt(2*radius^2)));
				if (!oldTiles.contains(currentTile)) {
					currentTile.setLightLevel(light);
				}
				affectedTiles.add(new TileLight(currentTile, light));
			}
		}
		for (TileLight oldTile : oldTiles) {
			if (!affectedTiles.contains(oldTile)) {
				oldTile.getTile().removeLightLevel(oldTile.getLight());
			}
		}
	}
}
