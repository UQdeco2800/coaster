package uq.deco2800.coaster.game.world;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.puzzle.*;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A room which the player must solve a puzzle
 */
public class PuzzleRoom extends RoomWorld {
	// Outlines what are walls and what is not walls in this room
	private static final int[][][] rooms = {
			{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
					{1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 8, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 6, 0, 1},
					{1, 0, 0, 0, 4, 0, 0, 1, 8, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 1, 1, 1, 1, 1, 0, 1, -1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 0, 0, 5, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 7, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 1},
					{1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 9, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 3, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}},

			{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
					{1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 8, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 6, 0, 1},
					{1, 0, 0, 0, 4, 0, 0, 1, 8, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 1, 1, 1, 1, 1, 0, 1, -1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 0, 0, 5, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 7, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 1},
					{1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 9, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}},
	};

	private static final int[][][] roomEntities = {
			{{10, 16, 10, 10, 3000},
					{16, 16, 7, 3, 3000}},


			{{12, 16, 10, 10, 1500},
					{16, 16, 7, 3, 1500}},
	};

	private List<List<Gate>> gatesList = new ArrayList<>();
	private List<Gate> gates = null;
	private Random randomGen = new Random();

	// Width and height of the room
	private static final int width = 22;
	private static final int height = 14;

	PuzzleRoom() {
		tiles = new WorldTiles(width, height, width);

	}

	@Override
	public void populateRoom() {
		int index = randomGen.nextInt(rooms.length);
		int[][] room = rooms[index];

		// Create the room
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles.set(x, y, TileInfo.get(Tiles.PUZZLE_BACKGROUND_BRICK));

				switch (rooms[index][y][x]) {
					case 0: // background
						break;
					case 1: // wall
						tiles.set(x, y, TileInfo.get(Tiles.PUZZLE_SOLID_BRICK));
						break;
					case 2: // spikes
						this.addEntityToWorldPos(new Spikes(), x, y);
						break;
					case 3: // button
						this.addEntityToWorldPos(new RedButton(), x, y);
						break;
					case 4: // crate
						this.addEntityToWorldPos(new Crate(), x, y);
						break;
					case 5: // gate
						Gate gate = (Gate) this.addEntityToWorldPos(new Gate(), x, y);
						if (room[y - 1][x] != 5) { // if tile above isn't gate, start new gates list
							gates = new ArrayList<>();
							gatesList.add(gates);
						}
						gates.add(gate);
						break;
					case 6: // lever
						this.addEntityToWorldPos(new Lever(true), x, y);
						break;
					case 7: // key
						ItemEntity key = (ItemEntity) this.addEntityToWorldPos(
								new ItemEntity(ItemRegistry.getItem("puzzle_key")), x, y);
						key.setPermanent(true);
						break;
					case 8: // door
						boolean closed = (room[y + 1][x] == -1); // if tile below is -1, door is closed upon load
						this.addEntityToWorldPos(new RoomDoor(closed), x + 0.5f, y);
						break;
					case 9: // platform
						int[] entityInfo = getEntityAtPosition(index, x, y);
						if (entityInfo == null) {
							continue;
						}

						MovingPlatform platform = (MovingPlatform) this.addEntityToWorldPos(new MovingPlatform(true), x, y);
						platform.setPath(entityInfo[0], entityInfo[1], entityInfo[2], entityInfo[3], entityInfo[4]);
					default:
						break;
				}
			}
		}
	}

	/**
	 * Remove a gate from the world
	 *
	 * @param gateNumber the number of the gate to be removed
	 */
	public void removeGate(int gateNumber) {
		gatesList.get(gateNumber).forEach(Entity::delete);
	}

	private int[] getEntityAtPosition(int index, int x, int y) {
		for (int[] entity : roomEntities[index]) {
			if (entity[0] == x && entity[2] == y) {
				return entity;
			}
		}
		return null;
	}

	@Override
	float getStartingX() {
		return 1;
	}

	@Override
	float getStartingY() {
		return 1;
	}
}
