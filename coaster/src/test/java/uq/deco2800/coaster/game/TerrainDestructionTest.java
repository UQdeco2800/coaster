package uq.deco2800.coaster.game;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.MobilityTest;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.game.tiles.Tile;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TerrainDestructionTest {
	private static World world = World.getInstance();
	
	private static Logger logger = LoggerFactory.getLogger(TerrainDestructionTest.class);
	private static WorldTiles destructionTiles;

	@Before
	public void init() {
		TestHelper.load();
		if (destructionTiles == null) {
			logger.info("tiles are null");
			destructionTiles = TestHelper.getWaterTiles();
		}
	}

	public void initialise() {
		TestHelper.load();
		world.resetWorld();
	}

	@Test
	public void testBlockHP() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		world.getTiles().set(tileX, tileY, TileInfo.get(Tiles.DIRT));
		Tile tile = world.getTiles().get(tileX, tileY);
		tile.setHitPoints(3);
		assertEquals("Verifying tile HP is 3", tile.getHitPoints(), 3);
	}

	@Test
	public void testDamagedBlockVariant() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		world.getTiles().set(tileX, tileY, TileInfo.get(Tiles.DIRT));
		Tile tile = world.getTiles().get(tileX, tileY);
		tile.setHitPoints(3);
		TerrainDestruction.damageBlock(tileX, tileY, 0, false, true);
		assertEquals("Ensure tile has switched to damaged variant", tile.getTileType().getType(), Tiles.DIRT_DAMAGED);

	}

	@Test
	public void testOneDamageToBlock() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		world.getTiles().set(tileX, tileY, TileInfo.get(Tiles.DIRT));
		Tile tile = world.getTiles().get(tileX, tileY);
		tile.setHitPoints(3);
		TerrainDestruction.damageBlock(tileX, tileY, 0, false, true);
		assertEquals("Verifying HP is one less after taking 0 damaged", tile.getHitPoints(), 2);
	}

	@Test
	public void testTwoDamageToBlock() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		world.getTiles().set(tileX, tileY, TileInfo.get(Tiles.DIRT));
		Tile tile = world.getTiles().get(tileX, tileY);
		tile.setHitPoints(3);
		TerrainDestruction.damageBlock(tileX, tileY, 150, false, true);
		assertEquals("Verifying HP is two less after being damaged 150", 1, tile.getHitPoints());
	}

	@Test
	public void testBlockDestroyed() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		world.getTiles().set(tileX, tileY, TileInfo.get(Tiles.DIRT));
		Tile tile = world.getTiles().get(tileX, tileY);
		tile.setHitPoints(3);
		TerrainDestruction.damageBlock(tileX, tileY, 0, false, true);
		TerrainDestruction.damageBlock(tileX, tileY, 150, false, true);
		assertTrue("Verifying HP is 0", tile.isDestroyed());
	}

	@Test
	public void testBlockBackground() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		world.getTiles().set(tileX, tileY, TileInfo.get(Tiles.DIRT));
		Tile tile = world.getTiles().get(tileX, tileY);
		tile.setHitPoints(3);
		TerrainDestruction.damageBlock(tileX, tileY, 0, false, true);
		TerrainDestruction.damageBlock(tileX, tileY, 150, false, true);
		assertEquals("Ensure tile has switched to background variant", tile.getTileType().getType(),
				Tiles.DIRT_BACKGROUND);
	}

	@Ignore
	public void testCircleDestruction() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				world.getTiles().set(tileX + i, tileY + j, TileInfo.get(Tiles.DIRT));
				world.getTiles().get(tileX, tileY).setHitPoints(3);
			}
		}
		TerrainDestruction.damageCircle(tileX, tileY, 2, 300);
		assertTrue(world.getTiles().get(tileX + 1, tileY + 1).isDestroyed());
	}

	@Test
	public void testRectangleDestruction() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				world.getTiles().set(tileX + i, tileY + j, TileInfo.get(Tiles.DIRT));
				world.getTiles().get(tileX + i, tileY + j).setHitPoints(3);
			}
		}
		TerrainDestruction.damageRectangle(tileX, tileY, 4, 300);
		assertEquals("Verifying HP is 0  after being damaged 300", 0,
				world.getTiles().get(tileX + 1, tileY).getHitPoints());
	}

	@Test
	public void testColumnDestruction() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				world.getTiles().set(tileX + i, tileY + j, TileInfo.get(Tiles.DIRT));
				world.getTiles().get(tileX + i, tileY + j).setHitPoints(3);
			}
		}
		TerrainDestruction.destroyColumn(tileX, tileY + 3, 300, 4);
		assertEquals("Verifying HP is 0  after being damaged 300", 0,
				world.getTiles().get(tileX, tileY + 1).getHitPoints());
	}

	@Test
	public void testPlaceBlock() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				world.getTiles().set(tileX + i, tileY + j, TileInfo.get(Tiles.AIR));
			}
		}
		TerrainDestruction.placeBlock(new Player(), tileX + 2, tileY + 2, Side.RIGHT);
		assertEquals("Ensure block has been placed", Tiles.DIRT,
				world.getTiles().get(tileX + 3, tileY + 2).getTileType().getType());
	}

	@Test
	public void testPlaceEnemyBlock() {
		int tileX = 0;
		int tileY = 0;
		initialise();
		world.setTiles(destructionTiles);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				world.getTiles().set(tileX + i, tileY + j, TileInfo.get(Tiles.AIR));
			}
		}
		TerrainDestruction.placeEnemyBlock(new Player(), tileX + 2, tileY + 2, -1, 0);
		assertEquals("Ensure block has been placed", Tiles.DIRT,
				world.getTiles().get(tileX + 4, tileY + 2).getTileType().getType());
	}

}
