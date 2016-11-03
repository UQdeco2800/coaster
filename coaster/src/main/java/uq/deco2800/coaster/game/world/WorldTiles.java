package uq.deco2800.coaster.game.world;

import uq.deco2800.coaster.game.tiles.Tile;
import uq.deco2800.coaster.game.tiles.TileInfo;


/**
 * This class holds all data relating to tiles in a world
 */
@SuppressWarnings("unchecked")
public class WorldTiles {
	private ArrayDeque tiles;
	private int offset = 0;
	private int chunkHeight = Chunk.CHUNK_HEIGHT;

	/**
	 * Creates an empty world
	 */
	public WorldTiles() {
		tiles = new ArrayDeque();
	}

	/**
	 * Creates a world of a given size and fills it with default tiles
	 */
	public WorldTiles(int width, int height, int chunkWidth) {
		if (width % chunkWidth != 0) {
			throw new IllegalArgumentException("Invalid chunk width");
		}

		tiles = new ArrayDeque(width * height);
		int numChunks = width / chunkWidth;
		for (int i = 0; i < numChunks; i++) {
			addBlockRight(chunkWidth, height);
		}
		this.chunkHeight = height;
	}

	/**
	 * Create (width number) default tiles to the left
	 */
	public void addBlockLeft(int width, int height) {
		offset += width;
		for (int i = 0; i < width * height; i++) {
			tiles.addFirst(new Tile());
			Tile tile = (Tile) tiles.getFirst();
			tile.setVisited(false);
		}
	}


	/**
	 * Create (width number) default tiles to the right
	 */
	public void addBlockRight(int width, int height) {
		for (int i = 0; i < width * height; i++) {
			tiles.addLast(new Tile());
			Tile tile = (Tile) tiles.getLast();
			tile.setVisited(false);
		}
	}

	/**
	 * Create a chunk to the left, filled with default tiles
	 */
	public void addChunkLeft() {
		offset += Chunk.CHUNK_WIDTH;
		for (int i = 0; i < Chunk.CHUNK_HEIGHT * Chunk.CHUNK_WIDTH; i++) {
			tiles.addFirst(new Tile());
			Tile tile = (Tile) tiles.getFirst();
			tile.setVisited(false);
		}
	}


	/**
	 * Create a chunk to the right, filled with default tiles
	 */
	public void addChunkRight() {
		for (int i = 0; i < Chunk.CHUNK_HEIGHT * Chunk.CHUNK_WIDTH; i++) {
			tiles.addLast(new Tile());
			Tile tile = (Tile) tiles.getLast();
			tile.setVisited(false);
		}
	}

	/**
	 * Return the tile at the given position
	 */
	public Tile get(int x, int y) {
		return (Tile) tiles.get((x + offset) * getHeight() + y);
	}

	/**
	 * Set the tile at the given position to the given type of tile
	 */
	public void set(int x, int y, TileInfo tile) {
		get(x, y).setTileType(tile);
	}

	/**
	 * Test that given position is valid and within range
	 */
	public boolean test(int x, int y) {
		return !((x + offset) >= getWidth() || y >= getHeight() || (x + offset) < 0 || y < 0);
	}

	/**
	 * Returns if a given position has been visited or not
	 */
	public boolean getVisited(int x, int y) {
		return get(x, y).getVisited();
	}

	/**
	 * Sets visit value for given position to given value
	 */
	public void setVisited(int x, int y, boolean visited) {
		get(x, y).setVisited(visited);
	}

	/**
	 * Return total width of tiles being stored
	 */
	public int getWidth() {
		if (tiles.size() == 0) {
			return 0;
		}
		return tiles.size() / this.chunkHeight;
	}

	/**
	 * Return total height of tiles being stored
	 */
	public int getHeight() {
		if (tiles.size() == 0) {
			return 0;
		}
		return tiles.size() / getWidth();
	}

	/**
	 * Set current horizontal offset of WorldTiles
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Returns current horizontal offset of WorldTiles
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Return tiles stored as array
	 */
	public Object[] toArray() {
		return tiles.toArray();
	}
}
