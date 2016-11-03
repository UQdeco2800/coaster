package uq.deco2800.coaster.game.world;

import uq.deco2800.coaster.game.entities.npcs.DuckKingNPC;
import uq.deco2800.coaster.game.entities.puzzle.RoomDoor;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This will be the room that we fight the boss in
 */
public class BossRoom extends RoomWorld {
	// width and height of the room
	private static final int width = 40;
	private static final int height = 16;
	private Sprite background = null;

	/**
	 * Generate a room of a preset size, with a preset background
	 */
	public BossRoom() {
		tiles = new WorldTiles(width, height, width);
		background = new Sprite(SpriteList.BOSS_ROOM_BACKGROUND);
	}

	@Override
	public void populateRoom() {
		// Create the room
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (j >= 14) {
					tiles.set(i, j, TileInfo.get(Tiles.BOSS_ROOM_GROUND));
				} else if (i == 0 || i == width - 1) {
					tiles.set(i, j, TileInfo.get(Tiles.BOSS_ROOM_INVISIBLE_WALL));
				} else {
					tiles.set(i, j, TileInfo.get(Tiles.BOSS_ROOM_BACKGROUND));
				}
			}
		}
		this.addEntityToWorldPos(new DuckKingNPC(), 10, 10);
		this.addEntityToWorldPos(new RoomDoor(false), width - 2, 12);
	}

	/**
	 * @return the Sprite for this background
	 */
	public Sprite getBackground() {
		return background;
	}

	@Override
	float getStartingX() {
		return width - 10F;
	}

	@Override
	float getStartingY() {
		return 12;
	}
}
