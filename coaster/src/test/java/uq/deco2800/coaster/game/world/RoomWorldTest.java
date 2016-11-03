package uq.deco2800.coaster.game.world;

import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RoomWorldTest {
	private PuzzleRoom puzzleRoom;
	private BossRoom bossRoom;

	@Before
	public void setUp() {
		TestHelper.load();
		puzzleRoom = new PuzzleRoom();
		bossRoom = new BossRoom();
	}

	@Test
	public void puzzleGetTiles() {
		WorldTiles tiles = puzzleRoom.getTiles();
		assertNotEquals(tiles, null);
		assertTrue(tiles.getWidth() > 0);
		assertTrue(tiles.getHeight() > 0);
	}

	@Test
	public void puzzleLoadAroundPlayer() {
		WorldTiles tiles = puzzleRoom.getTiles();
		Player player = new Player();
		player.setWorld(puzzleRoom);
		puzzleRoom.loadAroundPlayer(player);
		WorldTiles tilesAfter = puzzleRoom.getTiles();
		assertEquals(tiles.getHeight(), tilesAfter.getHeight());
		assertEquals(tiles.getWidth(), tilesAfter.getWidth());
	}

	@Test
	public void puzzleIsDecoGenEnabled() {
		assertFalse(puzzleRoom.isDecoGenEnabled());
	}

	@Test
	public void puzzleIsBuildingGenEnabled() {
		assertFalse(puzzleRoom.isBuildingGenEnabled());
	}

	@Test
	public void puzzleIsTotemGenEnabled() {
		assertFalse(puzzleRoom.isTotemGenEnabled());
	}

	@Test
	public void puzzleIsNpcGenEnabled() {
		assertFalse(puzzleRoom.isNpcGenEnabled());
	}

	@Test
	public void puzzleTempPlayerX() {
		assertEquals(puzzleRoom.getTempPlayerX(), 0, 0.0001F);
		puzzleRoom.setTempPlayerX(1);
		assertEquals(puzzleRoom.getTempPlayerX(), 1, 0.0001F);
	}

	@Test
	public void puzzleTempPlayerY() {
		assertEquals(puzzleRoom.getTempPlayerY(), 0, 0.0001F);
		puzzleRoom.setTempPlayerY(1);
		assertEquals(puzzleRoom.getTempPlayerY(), 1, 0.0001F);
	}

	@Test
	public void puzzleParentWorld() {
		assertNull(puzzleRoom.getParentWorld());
		puzzleRoom.setParentWorld(World.getInstance());
		assertEquals(puzzleRoom.getParentWorld(), World.getInstance());
	}

	@Test
	public void puzzleStartingX() {
		WorldTiles tiles = puzzleRoom.getTiles();
		assertTrue(tiles.getWidth() > puzzleRoom.getStartingX());
		assertTrue(puzzleRoom.getStartingX() > 0);
	}

	@Test
	public void puzzleStartingY() {
		WorldTiles tiles = puzzleRoom.getTiles();
		assertTrue(tiles.getHeight() > puzzleRoom.getStartingY());
		assertTrue(puzzleRoom.getStartingY() > 0);
	}

	@Test
	public void bossGetTiles() {
		WorldTiles tiles = bossRoom.getTiles();
		assertNotEquals(tiles, null);
		assertTrue(tiles.getWidth() > 0);
		assertTrue(tiles.getHeight() > 0);
	}

	@Test
	public void bossloadAroundPlayer() {
		WorldTiles tiles = bossRoom.getTiles();
		Player player = new Player();
		player.setWorld(bossRoom);
		bossRoom.loadAroundPlayer(player);
		WorldTiles tilesAfter = bossRoom.getTiles();
		assertEquals(tiles.getHeight(), tilesAfter.getHeight());
		assertEquals(tiles.getWidth(), tilesAfter.getWidth());
	}

	@Test
	public void bossIsDecoGenEnabled() {
		assertFalse(bossRoom.isDecoGenEnabled());
	}

	@Test
	public void bossIsBuildingGenEnabled() {
		assertFalse(bossRoom.isBuildingGenEnabled());
	}

	@Test
	public void bossIsTotemGenEnabled() {
		assertFalse(bossRoom.isTotemGenEnabled());
	}

	@Test
	public void bossIsNpcGenEnabled() {
		assertFalse(bossRoom.isNpcGenEnabled());
	}

	@Test
	public void bossTempPlayerX() {
		assertEquals(bossRoom.getTempPlayerX(), 0, 0.0001F);
		bossRoom.setTempPlayerX(1);
		assertEquals(bossRoom.getTempPlayerX(), 1, 0.0001F);
	}

	@Test
	public void bossTempPlayerY() {
		assertEquals(bossRoom.getTempPlayerY(), 0, 0.0001F);
		bossRoom.setTempPlayerY(1);
		assertEquals(bossRoom.getTempPlayerY(), 1, 0.0001F);
	}

	@Test
	public void bossParentWorld() {
		assertNull(bossRoom.getParentWorld());
		bossRoom.setParentWorld(World.getInstance());
		assertEquals(bossRoom.getParentWorld(), World.getInstance());
	}

	@Test
	public void bossStartingX() {
		WorldTiles tiles = bossRoom.getTiles();
		assertTrue(tiles.getWidth() > bossRoom.getStartingX());
		assertTrue(bossRoom.getStartingX() > 0);
	}

	@Test
	public void bossStartingY() {
		WorldTiles tiles = bossRoom.getTiles();
		assertTrue(tiles.getHeight() > bossRoom.getStartingY());
		assertTrue(bossRoom.getStartingY() > 0);
	}
}