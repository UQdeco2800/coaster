package uq.deco2800.coaster.game.world;

import org.junit.Test;
import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.TestingNPC;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

public class MiniMapTest {
	private World world = World.getInstance();

	@Test
	public void testVisibility() {
		// Check various visibility toggles
		assertEquals(MiniMap.getVisibility(), false);
		MiniMap.toggleVisibility();
		assertEquals(MiniMap.getVisibility(), true);
		MiniMap.toggleVisibility();
		assertEquals(MiniMap.getVisibility(), false);
		MiniMap.setVisibility(true);
		assertEquals(MiniMap.getVisibility(), true);
		MiniMap.setVisibility(true);
		assertEquals(MiniMap.getVisibility(), true);
		MiniMap.setVisibility(false);
		assertEquals(MiniMap.getVisibility(), false);
	}

	@Test
	public void testMapEntities() {
		init();
		Chunk chunk = TestHelper.getFlatChunk(BiomeType.ROCK, TileInfo.get(Tiles.ROCK));

		world.setDecoGenEnabled(true);
		chunk.setDecorationSpawnChance(1);
		chunk.generateDecorations();

		Player player = new Player();
		world.addEntity(player);

		TestingNPC npc1 = new TestingNPC();
		TestingNPC npc2 = new TestingNPC();
		world.addEntity(npc1);
		world.addEntity(npc2);
		//TODO: Does not test items
		world.gameLoop(1);
		int mapEntities = MiniMap.getMapEntities(world.getAllEntities()).size();
		assertEquals(mapEntities, 3); // Should not be counting decorations
	}

	@Test
	public void testMapVisiting() {
		init();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		assertEquals(World.getInstance().getTiles().getVisited(0, 0), false);
		MiniMap.updateVisited(player);
		assertEquals(World.getInstance().getTiles().getVisited(0, 0), true);
	}

	@Test
	public void testMapAllVisit() {
		init();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		assertEquals(World.getInstance().getTiles().getVisited(0, 0), false);
		MiniMap.visitChunk();
		assertEquals(World.getInstance().getTiles().getVisited(0, 0), true);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testConstructor() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
		init();
		final Constructor<MiniMap> constructor = MiniMap.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		try {
			constructor.newInstance();
		} catch (InvocationTargetException e) {
			throw (UnsupportedOperationException) e.getTargetException();
		}
	}

	/**
	 * Loads sprites, so they can be accessed by tiles to create a world.
	 */
	private void init() {
		TestHelper.load();
		world.debugReset();
		world.setTiles(World.getTutorialWorld());
	}
}
