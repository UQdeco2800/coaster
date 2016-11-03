package uq.deco2800.coaster.game.world;

import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;

/**
 * Class handling Buildings structures
 * 0 is air
 * 1 is roof
 * 2 is wall
 * 3 is window
 */
public class Building {

	/*
	 * Blueprints for assorted buildings
	 */
	private static int[][][] buildingArray = new int[][][]{
			{
					{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
					{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
					{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
					{0, 0, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 0, 0},
					{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
					{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0}
			}, {
			{0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
			{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0}
	}, {
			{0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
			{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0}
	}, {
			{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
			{0, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 2, 0},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0}
	}, {
			{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
			{0, 2, 3, 2, 2, 2, 3, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 2, 2, 2, 3, 2, 0},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
			{0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0}
	}, {
			{0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 3, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
			{0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0}
	}
	};

	private int buildingWidth; // width of building
	private int buildingHeight; // height of building
	private int buildingIndex; // index of building

	/**
	 * Creates a reference to a building structure.
	 *
	 * @param index index of building in building array
	 */
	public Building(int index) {
		buildingIndex = index;
		buildingHeight = buildingArray[buildingIndex].length;
		buildingWidth = buildingArray[buildingIndex][0].length;
	}

	/**
	 * Builds from bottom left to top right in given chunk
	 */
	public void build(int leftX, int bottomY, Chunk chunk) {
		int x;
		TileInfo tile;
		for (x = 0; x < buildingWidth; x++) {
			for (int y = 0; y < buildingHeight; y++) {
				tile = getTileInfo(buildingArray[buildingIndex][y][x]);
				chunk.set(leftX + x, y + bottomY - buildingHeight, tile);
			}
		}
	}

	/**
	 * Determine appropriate TileType in blueprint
	 */
	public static TileInfo getTileInfo(int id) {
		TileInfo tile;
		switch (id) {
			case 0:
				tile = TileInfo.get(Tiles.AIR);
				break;
			case 1:
				tile = TileInfo.get(Tiles.ROOF);
				break;
			case 2:
				tile = TileInfo.get(Tiles.WALLS);
				break;
			case 3:
				tile = TileInfo.get(Tiles.WINDOW);
				break;
			default:
				tile = TileInfo.get(Tiles.AIR);
				break;
		}
		return tile;
	}

	/**
	 * Return width of current building
	 */
	public int getWidth() {
		return buildingWidth;
	}

	/**
	 * Return width of current building
	 */
	public int getHeight() {
		return buildingHeight;
	}

	/**
	 * Return index of current building
	 */
	public int getIndex() {
		return buildingIndex;
	}

	/**
	 * Return length of building array
	 */
	public static int getNumBuildings() {
		return buildingArray.length;
	}

	/**
	 * Return building array of single building
	 *
	 * @param index index of building to get
	 * @return building array
	 */
	public static int[][] getBuildingArray(int index) {
		return buildingArray[index];
	}
}
