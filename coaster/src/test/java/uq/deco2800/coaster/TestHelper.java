package uq.deco2800.coaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.game.world.BiomeType;
import uq.deco2800.coaster.game.world.Chunk;
import uq.deco2800.coaster.game.world.WorldTiles;

/**
 * Created by rcarrier on 12/08/2016.
 */
public class TestHelper {

	private static Logger logger = LoggerFactory.getLogger(TestHelper.class);
	private static boolean testsInitialised = false;
	private static final TestWindow w = new TestWindow();

	private static WorldTiles tiles;
	private static WorldTiles waterTiles;
	private static WorldTiles destructionTiles;

	/**
	 * loads the sprites by creating a new window and closing it straight after the sprites are cached.
	 */
	public static void load() {
		if (!testsInitialised) {
			w.begin();
			testsInitialised = true;
		}
		//System.gc();
	}

	/**
	 * manually reload the FXML screens
	 */
	public static void loadWithFXML() {
		load();
		w.loadFXML();
	}

	public static void manualSoundLoad() {
		w.manualSoundLoad();
	}

	/**
	 * Generic flat chunk with only a floor of two different heights
	 *
	 * @return test tile maps
	 */
	public static Chunk getFlatChunk(BiomeType biomeType, TileInfo tileType) {
		int[] topBlocks = new int[Chunk.CHUNK_WIDTH];
		WorldTiles tiles = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
		try {
			for (int x = 0; x < tiles.getWidth(); x++) {
				if (x <= tiles.getWidth() / 2) {
					topBlocks[x] = 10;
					tiles.get(x, topBlocks[x]).setTileType(tileType);
				} else {
					topBlocks[x] = 15;
					tiles.get(x, topBlocks[x]).setTileType(tileType);
				}
			}
		} catch (Exception e) {
			logger.debug("Need to load sprite cache most likely;\nuq.deco2800.coaster.TestHelper.load();");
		}
		return new Chunk(tiles, biomeType, 0, topBlocks);
	}

	/**
	 * Generic tile map with floor and wall on left
	 *
	 * @return test tile map
	 */
	public static WorldTiles getMobilityTestTiles() {
		if (tiles == null) {
			tiles = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
			try {
				for (int x = 0; x < 70; x++) {
					tiles.set(x, 31, TileInfo.get(Tiles.DIRT));
				}
				for (int y = 0; y < 32; y++) {
					tiles.set(0, y, TileInfo.get(Tiles.DIRT));
				}
				tiles.set(1, 28, TileInfo.get(Tiles.DIRT)); //Block used to test jumping immediately under a ceiling
				tiles.set(1, 24, TileInfo.get(Tiles.DIRT)); //Block used to test jumping and hitting
			} catch (Exception e) {
				logger.debug("Need to load sprite cache most likely;\nuq.deco2800.coaster.TestHelper.load();");
			}
		}
		return tiles;
	}

	/**
	 * Generic tile map with floor and wall on left
	 *
	 * @return test tile map
	 */
	public static WorldTiles getDestructionTestTiles() {
		if (destructionTiles == null) {
			destructionTiles = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
			try {
				for (int x = 0; x < 70; x++) {
					destructionTiles.set(x, 31, TileInfo.get(Tiles.DIRT));
				}
				for (int y = 0; y < 32; y++) {
					destructionTiles.set(0, y, TileInfo.get(Tiles.DIRT));
				}
				destructionTiles.set(1, 28, TileInfo.get(Tiles.DIRT)); //Block used to test jumping immediately under a ceiling
				destructionTiles.set(1, 24, TileInfo.get(Tiles.DIRT)); //Block used to test jumping and hitting
			} catch (Exception e) {
				logger.debug("Need to load sprite cache most likely;\nuq.deco2800.coaster.TestHelper.load();");
			}
		}
		return destructionTiles;
	}
	
	
	
	/**
	 * Generic tile map with water and dirt as the bottom layer
	 *
	 * @return water tile test map
	 */
	public static WorldTiles getWaterTiles() {
		if (waterTiles == null) {
			waterTiles = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
			try {
				for (int x = 0; x < waterTiles.getWidth(); x++) {
					for (int y = 0; y < waterTiles.getHeight() - 1; y++) {
						waterTiles.set(x, y, TileInfo.get(Tiles.WATER));
					}
					waterTiles.set(x, waterTiles.getHeight() - 1, TileInfo.get(Tiles.DIRT));
				}
			} catch (Exception e) {
				logger.debug("Need to load sprite cache most likely;\nTestHelper.load();");
			}
		}
		return waterTiles;
	}
}