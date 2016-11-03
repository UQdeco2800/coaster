package uq.deco2800.coaster.game.tiles;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import uq.deco2800.coaster.TestHelper;

public class TileInfoTest {
	private TileInfo testGasTile;
	private TileInfo testSolidTile;
	private TileInfo testLiquidTile;

	/**
	 * This will run the tile registry code. For the purposes of testing, we will assume that there will always be - an
	 * test gas tile - a test solid tile - a test liquid tile This is so that each of the three block types (TileSolid,
	 * TileLiquid, and TileGas) can be tested.
	 */
	@Before
	public void initialise() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		TileInfo.clearRegistry();
		// Load tiles and check the hasLoaded method
		assert (!TileInfo.hasLoaded());
		if (!TileInfo.hasLoaded()) {
			TileInfo.registerTiles();
		}
		TestHelper.load();
		assert (TileInfo.hasLoaded());
		// Overwrite some of the tiles
		testGasTile = (new TileGas(Tiles.AIR)).setDisplayName("Test Gas").setDefaultFilename("air.png").setNaturalSurface(false);
		testSolidTile = (new TileSolid(Tiles.DIRT)).setDisplayName("Test Solid").setDefaultFilename("dirt.png").setNaturalSurface(true);
		testLiquidTile = (new TileLiquid(Tiles.WATER)).setDisplayName("Test Liquid").setDefaultFilename("water.png");
		// Reflection to allow me to register tiles. If this suddently breaks, double check that registerTile still
		// exists in TileInfo. http://stackoverflow.com/a/34658 for more information
		Method registerTileMethod = TileInfo.class.getDeclaredMethod("registerTile", TileInfo.class);
		registerTileMethod.setAccessible(true);
		registerTileMethod.invoke(null, testGasTile);
		registerTileMethod.invoke(null, testSolidTile);
		registerTileMethod.invoke(null, testLiquidTile);
		registerTileMethod.setAccessible(false);
	}

	@Test
	public void getTileRegistry() {
		Map<Tiles, TileInfo> registry = TileInfo.getTileRegistry();
		// Check that the three expected tiles are in the registry
		assert (registry.size() > 0);
		assert (registry.containsValue(testGasTile));
		assert (registry.containsValue(testSolidTile));
		assert (registry.containsValue(testLiquidTile));
		// Check that we can't add items to the registry
		TileInfo newTile = new TileSolid(Tiles.AIR);
		Exception ex = null;
		try {
			registry.put(Tiles.AIR, newTile);
		} catch (UnsupportedOperationException e) {
			ex = e;
		}
		assert (ex != null);
	}

	@Test
	public void getSpriteFilenames() {
		Map<String, String> fileNames = testGasTile.getSpriteFilenames();
		assert (fileNames.keySet().size() == 1);
		assert (fileNames.keySet().contains("DEFAULT"));
		assert (fileNames.get("DEFAULT").equals("sprites/tiles/air.png"));
	}

	@Test
	public void getBlockWidth() {
		assert (testGasTile.getBlockWidth() == 32);
	}

	@Test
	public void getBlockHeight() {
		assert (testGasTile.getBlockHeight() == 32);
	}

	@Test
	public void getNumSpriteFrames() {
		assert (testGasTile.getNumSpriteFrames() == 1);
	}

	@Test
	public void getSpriteFrameDuration() {
		assert (testGasTile.getSpriteFrameDuration() == 1);
	}

	@Test
	public void getType() {
		assert (testGasTile.getType().equals(Tiles.AIR));
	}

	@Test
	public void getDisplayName() {
		assert (testGasTile.getDisplayName().equals("Test Gas"));
	}

	@Test
	public void isNaturalSurface() {
		assert (!testGasTile.isNaturalSurface());
		assert (testSolidTile.isNaturalSurface());
		assert (!testLiquidTile.isNaturalSurface());
	}

	@Test
	public void isObstacle() {
		assert (!testGasTile.isObstacle());
		assert (testSolidTile.isObstacle());
		assert (!testLiquidTile.isObstacle());
	}

	@Test
	public void testEquals() {
		// Two TileInfo's are equal if they have the same name
		assert (testGasTile.equals(new TileSolid(Tiles.AIR)));
		assert (!testGasTile.equals(testSolidTile));
		assert (!testGasTile.equals(new TileSolid(Tiles.DIRT)));
		// Do checks for hashcode
		assert (testGasTile.hashCode() == testGasTile.hashCode());
		assert (testGasTile.hashCode() == (new TileSolid(Tiles.AIR)).hashCode());
		assert (testGasTile.hashCode() != testSolidTile.hashCode());
		assert (testGasTile.hashCode() != (new TileGas(Tiles.DIRT)).hashCode());
	}
}