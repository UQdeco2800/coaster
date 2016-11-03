package uq.deco2800.coaster.game.world;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.FlyingNPC;
import uq.deco2800.coaster.game.entities.npcs.GhostShipNPC;
import uq.deco2800.coaster.game.entities.npcs.IceSpiritNPC;
import uq.deco2800.coaster.game.entities.npcs.MeleeEnemyNPC;
import uq.deco2800.coaster.game.entities.npcs.RangedEnemyNPC;
import uq.deco2800.coaster.game.entities.npcs.RatNPC;
import uq.deco2800.coaster.game.entities.npcs.RhinoNPC;
import uq.deco2800.coaster.game.entities.npcs.SkeletonNPC;
import uq.deco2800.coaster.game.entities.npcs.TestingNPC;
import uq.deco2800.coaster.game.entities.puzzle.RoomDoor;
import uq.deco2800.coaster.game.entities.puzzle.Totem;
import uq.deco2800.coaster.game.tiles.Tile;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.graphics.notifications.IngameText;

public class WorldTest {

	private World world = World.getInstance();

	@Test
	public void testWaveforms() {
		init();
		Waveform waveform = new Waveform(10, 15);
		assertEquals(15, waveform.getAmplitude());
		assertEquals(10, waveform.getPeriod());
		world.addTerrainWaveform(new Waveform(40, 80));
		world.addCaveWaveform(new Waveform(16, 8));
		assertEquals(80, world.getTerrainWaveforms().get(0).getAmplitude());
		assertEquals(8, world.getCaveWaveforms().get(0).getAmplitude());
	}

	@Test
	public void testAddEntity() {
		init();
		// Add a mob to the world
		TestingNPC npc1 = new TestingNPC();
		TestingNPC npc2 = new TestingNPC();
		world.addEntity(npc1);
		world.addEntity(npc2);
		world.gameLoop(1);
		// Check that there is 1 entity in the world and that it is the mob we
		// just added
		assert (world.getAllEntities().size() == 2);
		assert (world.getAllEntities().contains(npc1));
		assert (world.getAllEntities().contains(npc2));
	}

	@Test
	public void testDeleteEntity() {
		init();
		// Add a mob to the world
		TestingNPC npc1 = new TestingNPC();
		TestingNPC npc2 = new TestingNPC();
		world.addEntity(npc1);
		world.addEntity(npc2);
		world.gameLoop(1);
		world.deleteEntity(npc1);
		world.gameLoop(1);
		// Check that the entities list now only contains mob2
		assert (world.getAllEntities().size() == 1);
		assert (!world.getAllEntities().contains(npc1));
		assert (world.getAllEntities().contains(npc2));
	}

	@Test
	public void testGameOver() {
		init();
		world.setGameOver(false);
		assert (!world.isGameOver());
		world.setGameOver(true);
		assert (world.isGameOver());
	}

	@Test
	public void testWorldDifficulty() {
		init();
		// Default Difficulty is Difficulty.MEDIUM
		assert (world.getDifficulty() == 1.0);
	}

	@Test
	public void testNpcGenEnabled() {
		init();
		// Test when mob gen is enabled
		world.setNpcGenEnabled(false);
		assertFalse(world.isNpcGenEnabled());
		// Test when mob gen is not enabled
		world.setNpcGenEnabled(true);
		assertTrue(world.isNpcGenEnabled());
	}

	/*
	 * A set of basic tests to check whether the right mobs are being spawned in
	 * each biome
	 */
	@Test //Fix, occasionally fails!
	public void testNpcGenerator() {
		init();

		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		// No NPCs in the world yet
		assertEquals(0, world.getNpcEntities().size());
		// Test setting the mob spawn chance
		world.setNpcSpawnChance(1);
		assertEquals(1, world.getNpcSpawnChance());

		// With mob generation disabled, try generate a mob (but it shouldn't)
		int size = world.getNpcEntities().size();
		world.setNpcGenEnabled(false);
		world.gameLoop(1);
		assertTrue(size == world.getNpcEntities().size());

		// Create some biomes so we can test if the correct mobs are generated
		// for each biome the player can visit.

		//spawnNumber is 0 so in any biome a treasure npc is spawned!
		world.setNpcGenEnabled(true);
		world.npcGenerator(player, 0, true, 0, BiomeType.FOREST);
		world.setNpcGenEnabled(false); // So it doesn't add another mob during the gameloop
		world.gameLoop(1);
		assertTrue(world.getNpcEntities().stream().anyMatch(e -> e instanceof RatNPC));

		//Melee enemies live in rock
		world.setNpcGenEnabled(true);
		world.npcGenerator(player, 61, true, 0, BiomeType.ROCK);
		world.setNpcGenEnabled(false); // So it doesn't add another mob during the gameloop
		world.gameLoop(1);
		assertTrue(world.getNpcEntities().stream().anyMatch(e -> e instanceof SkeletonNPC));

		//Ice spirits live in snow
		world.setNpcGenEnabled(true);
		world.npcGenerator(player, 1, true, 0, BiomeType.SNOW);
		world.setNpcGenEnabled(false); // So it doesn't add another mob during the gameloop
		world.gameLoop(1);
		assertTrue(world.getNpcEntities().stream().anyMatch(e -> e instanceof IceSpiritNPC));

		//Bats live in forests
		world.setNpcGenEnabled(true);
		world.npcGenerator(player, 1, true, 0, BiomeType.FOREST);
		world.setNpcGenEnabled(false); // So it doesn't add another mob during the gameloop
		world.gameLoop(1);
		assertTrue(world.getNpcEntities().stream().anyMatch(e -> e instanceof FlyingNPC));

		//Rhinos live in the plains
		world.setNpcGenEnabled(true);
		world.npcGenerator(player, 1, true, 0, BiomeType.PLAIN);
		world.setNpcGenEnabled(false); // So it doesn't add another mob during the gameloop
		world.gameLoop(1);
		assertTrue(world.getNpcEntities().stream().anyMatch(e -> e instanceof RhinoNPC));

		//GhostShipNPC live in desert
		world.setNpcGenEnabled(true);
		world.npcGenerator(player, 1, true, 0, BiomeType.DESERT);
		world.setNpcGenEnabled(false); // So it doesn't add another mob during the gameloop
		world.gameLoop(1);
		assertTrue(world.getNpcEntities().stream().anyMatch(e -> e instanceof GhostShipNPC));
	}

	@Test
	public void testNumMobsInChunk() {
		init();

		// add player
		Player player = new Player();
		world.addEntity(player);

		// No Mob in the world yet
		assertEquals(0, world.getNpcEntities().size());

		// setup generation
		world.setNpcSpawnChance(1);
		world.setNpcGenEnabled(true);

		// check no mobs in chunk 0 and chunk -1 from (0,0)
		assertEquals(0, world.getNumMobsInChunk(Chunk.CHUNK_WIDTH / 2));
		assertEquals(0, world.getNumMobsInChunk(-Chunk.CHUNK_WIDTH / 2));

		// generate one mob in chunk 0 from (0,0)
		world.npcGenerator(player, 1, false, Chunk.CHUNK_WIDTH / 2, BiomeType.SNOW);
		world.setNpcGenEnabled(false);
		world.gameLoop(1);
		world.setNpcGenEnabled(true);
		assertEquals(1, world.getNumMobsInChunk(Chunk.CHUNK_WIDTH / 2));

		// generate one mob in chunk -1 from (0,0)
		world.npcGenerator(player, 1, false, -Chunk.CHUNK_WIDTH / 2, BiomeType.SNOW);
		world.setNpcGenEnabled(false);
		world.gameLoop(1);
		world.setNpcGenEnabled(true);
		assertEquals(1, world.getNumMobsInChunk(Chunk.CHUNK_WIDTH / 2));
		assertEquals(1, world.getNumMobsInChunk(-Chunk.CHUNK_WIDTH / 2));

		//generate lots of mobs in chunk 0 from (0,0)
		for (int i = 0; i < 10; i++) {
			world.npcGenerator(player, 1, false, Chunk.CHUNK_WIDTH / 2, BiomeType.SNOW);
		}
		world.setNpcGenEnabled(false);
		world.gameLoop(1);
		world.setNpcGenEnabled(true);
		assertEquals(11, world.getNumMobsInChunk(Chunk.CHUNK_WIDTH / 2));

		//generate lots more mobs in chunk 0 from (0,0) to hit the limit
		for (int i = 0; i < 9; i++) {
			world.npcGenerator(player, 1, false, Chunk.CHUNK_WIDTH / 2, BiomeType.SNOW);
		}
		world.setNpcGenEnabled(false);
		world.gameLoop(1);
		world.setNpcGenEnabled(true);
		assertEquals(20, world.getNumMobsInChunk(Chunk.CHUNK_WIDTH / 2));
		for (int i = 0; i < World.NUM_MOBS_PER_CHUNK; i++) {
			world.npcGenerator(player, 1, false, Chunk.CHUNK_WIDTH / 2, BiomeType.SNOW);
			world.setNpcGenEnabled(false);
			world.gameLoop(1);
			world.setNpcGenEnabled(true);
		}
		assertEquals(World.NUM_MOBS_PER_CHUNK, world.getNumMobsInChunk(Chunk.CHUNK_WIDTH / 2));
	}

	@Test
	public void testChangeTile() {
		init();
		world.getTiles().addChunkRight();

		world.getTiles().set(0, 30, TileInfo.get(Tiles.DIRT));
		assertEquals(TileInfo.get(Tiles.DIRT), world.getTiles().get(0, 30).getTileType());

		world.getTiles().set(0, 30, TileInfo.get(Tiles.ROCK));
		assertEquals(TileInfo.get(Tiles.ROCK), world.getTiles().get(0, 30).getTileType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidWorld() {
		init();
		WorldTiles array = new WorldTiles(10, 10, 7);
		world.setTiles(array);
	}

	@Test
	public void testHitBoxes() {
		init();
		assertFalse(world.renderHitboxes());
		world.toggleHitboxes();
		assertTrue(world.renderHitboxes());
	}

	@Test
	public void testLightingGenEnabled() {
		init();
		world.setLightGenEnabled(true);
		assertEquals(true, world.isLightGenEnabled());
		world.setLightGenEnabled(false);
		assertEquals(false, world.isLightGenEnabled());
	}

	@Test
	public void testDecorationGenEnabled() {
		init();
		// Test when Decoration gen is enabled
		world.setDecoGenEnabled(true);
		assertEquals(true, world.isDecoGenEnabled());
		// Test when Decoration gen is not enabled
		world.setDecoGenEnabled(false);
		assertEquals(false, world.isDecoGenEnabled());
	}

	@Test
	public void testLightGenerator() {
		init();
		Chunk chunk = TestHelper.getFlatChunk(BiomeType.ROCK, TileInfo.get(Tiles.ROCK));

		world.setLightGenEnabled(true);
		assertEquals(true, world.isLightGenEnabled());
		assertEquals(0, world.getAllEntities().size());
		chunk.setLightSourceSpawnChance(1);
		chunk.generateDecorations();
		world.gameLoop(1);
		assertEquals(Chunk.CHUNK_WIDTH, world.getAllEntities().size());
	}

	@Test
	public void testChunkDecorationGenerator() {
		init();
		Chunk chunk = TestHelper.getFlatChunk(BiomeType.ROCK, TileInfo.get(Tiles.ROCK));

		world.setDecoGenEnabled(true);
		assertEquals(true, world.isDecoGenEnabled());
		assertEquals(0, world.getAllEntities().size());
		chunk.setDecorationSpawnChance(1);
		chunk.generateDecorations();
		world.gameLoop(1);
		assertEquals(Chunk.CHUNK_WIDTH, world.getAllEntities().size());

		//set DecorationGenEnabled to false
		world.setDecoGenEnabled(false);
		Chunk chunk2 = TestHelper.getFlatChunk(BiomeType.ROCK, TileInfo.get(Tiles.ROCK));
		chunk2.generateDecorations();
		world.gameLoop(1);
		assertEquals(Chunk.CHUNK_WIDTH, world.getAllEntities().size());

		//set DecorationGenEnabled back to true and add forest
		world.setDecoGenEnabled(true);
		world.getTiles().addChunkRight();
		Chunk chunk3 = TestHelper.getFlatChunk(BiomeType.FOREST, TileInfo.get(Tiles.DIRT));
		chunk3.setDecorationSpawnChance(1);
		chunk3.generateDecorations();
		world.gameLoop(1);
		assertEquals(Chunk.CHUNK_WIDTH * 2, world.getAllEntities().size());
	}

	@Test
	public void testChunkBiomeType() {
		init();
		int[] topBlocks = { 0 };
		WorldTiles array = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
		Chunk chunk = new Chunk(array, BiomeType.ROCK, 0, topBlocks);
		assertEquals(BiomeType.ROCK, chunk.getBiomeType());

		Chunk chunk2 = new Chunk(array, BiomeType.FOREST, 0, topBlocks);
		assertEquals(BiomeType.FOREST, chunk2.getBiomeType());
	}

	@Test
	public void testChunkXpos() {
		init();
		int[] topBlocks = { 0 };
		WorldTiles array = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
		Chunk chunk = new Chunk(array, BiomeType.ROCK, 0, topBlocks);
		assertEquals(0, chunk.getX());
		chunk = new Chunk(array, BiomeType.ROCK, 80, topBlocks);
		assertEquals(80, chunk.getX());
		chunk = new Chunk(array, BiomeType.ROCK, 8000, topBlocks);
		assertEquals(8000, chunk.getX());
	}

	@Test
	public void testChunkTopBlock() {
		init();
		Chunk chunk = TestHelper.getFlatChunk(BiomeType.ROCK, TileInfo.get(Tiles.DIRT));
		assertEquals(10, chunk.getTopBlockFromChunk(0));
		assertEquals(15, chunk.getTopBlockFromChunk(Chunk.CHUNK_WIDTH - 1));
	}

	@Test
	public void testBuilding() {
		init();
		Chunk chunk = TestHelper.getFlatChunk(BiomeType.ROCK, TileInfo.get(Tiles.AIR));
		for (int i = 0; i < Building.getNumBuildings(); i++) {
			Building building = new Building(i);
			assertEquals(i, building.getIndex());
			building.build(0, building.getHeight(), chunk);
			for (int j = 0; j < building.getHeight(); j++) {
				for (int k = 0; k < building.getWidth(); k++) {
					int buildingTile = Building.getBuildingArray(i)[j][k];
					TileInfo tileInfo = Building.getTileInfo(buildingTile);
					assertEquals(tileInfo, chunk.getBlocks().get(k, j).getTileType());
				}
			}
		}
	}

	@Test
	public void testChunkId() {
		init();
		int[] topBlocks = { 0 };
		WorldTiles array = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
		BiomeType rock = BiomeType.ROCK;
		Chunk chunk = new Chunk(array, rock, 0, topBlocks);
		assertEquals(0, chunk.getId());
		Chunk chunk2 = new Chunk(array, rock, Chunk.CHUNK_WIDTH, topBlocks);
		assertEquals(1, chunk2.getId());
		Chunk chunk3 = new Chunk(array, rock, Chunk.CHUNK_WIDTH * 100, topBlocks);
		assertEquals(Chunk.CHUNK_WIDTH * 100 / Chunk.CHUNK_WIDTH, chunk3.getId());
	}

	@Test
	public void testChunkBlock() {
		init();
		int[] topBlocks = { 0 };
		WorldTiles array = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
		Chunk chunk = new Chunk(array, BiomeType.ROCK, 80, topBlocks);
		assertTrue(chunk.getBlocks() == array);
	}

	@Test
	public void testGetTopBlockFromWaveform() {
		init();
		world.addTerrainWaveform(new Waveform(40, 80)); // mountainous waveform
		int topBlock = Chunk.getTopBlockFromWaveForms(Chunk.MIDDLE_CHUNK_POS, 50, 0, 100, world.getTerrainWaveforms());
		System.out.println(topBlock);
		assertTrue(topBlock >= 0 && topBlock <= 100);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testBuildingGeneration() {
		init();
		world.setChunkGenerationEnabled(true);
		world.setMapSeed(Integer.MAX_VALUE);
		world.setBuildingGenEnabled(true);
		world.resetWorld();
		assertEquals(0, world.getTiles().getHeight());
		assertEquals(0, world.getTiles().getWidth());
		Player player = new Player();
		world.addEntity(player);
		player.setPosition(0, 0);
		player.setX(Chunk.CHUNK_WIDTH * 2);
		world.loadAroundPlayer(player);
		assertEquals(Chunk.CHUNK_HEIGHT, world.getTiles().getHeight());
		assertEquals(Chunk.CHUNK_WIDTH * 5, world.getTiles().getWidth());
		List worldTiles = Arrays.asList(world.getTiles().toArray());
		Predicate<Tile> predicate = c -> c.getTileType().equals(TileInfo.get(Tiles.ROOF));
		assertTrue(worldTiles.stream().filter(predicate).findFirst().isPresent());
		world.gameLoop(1);
		assertFalse(world.getStoreEntities().isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidWorldTiles() {
		init();
		int[] topBlocks = { 0 };
		new WorldTiles(17, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidChunk() {
		init();
		int[] topBlocks = { 0 };
		WorldTiles array = new WorldTiles(Chunk.CHUNK_WIDTH / 2, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH / 2);
		new Chunk(array, BiomeType.ROCK, 80, topBlocks);
	}

	@Test
	public void testLoadAroundPlayer() {
		init();
		assertEquals(0, world.getTiles().getHeight());
		assertEquals(0, world.getTiles().getWidth());
		Player player = new Player();
		world.setChunkGenerationEnabled(true);
		world.addEntity(player);
		player.setPosition(0, 0);
		world.loadAroundPlayer(player);
		assertEquals(Chunk.CHUNK_HEIGHT, world.getTiles().getHeight());
		assertEquals(Chunk.CHUNK_WIDTH * 3, world.getTiles().getWidth());

		Object[] beforeMovement = world.getTiles().toArray();
		player.setPosition(Chunk.CHUNK_WIDTH * 10, 0); // move small distance right
		world.gameLoop(1);
		world.gameLoop(1);
		Object[] afterMovement = world.getTiles().toArray();
		assertEquals(Chunk.CHUNK_HEIGHT, world.getTiles().getHeight());
		assertEquals(Chunk.CHUNK_WIDTH * 3, world.getTiles().getWidth());
		assertNotEquals(beforeMovement, afterMovement);

		beforeMovement = world.getTiles().toArray();
		player.setPosition(Chunk.CHUNK_WIDTH * 100000, 0); // move large distance right
		world.gameLoop(1);
		world.gameLoop(1);
		afterMovement = world.getTiles().toArray();
		assertEquals(Chunk.CHUNK_HEIGHT, world.getTiles().getHeight());
		assertEquals(Chunk.CHUNK_WIDTH * 3, world.getTiles().getWidth());
		assertNotEquals(beforeMovement, afterMovement);

		beforeMovement = world.getTiles().toArray();
		player.setPosition(-Chunk.CHUNK_WIDTH * 10, 0); // move small distance left
		world.gameLoop(1);
		world.gameLoop(1);
		afterMovement = world.getTiles().toArray();
		assertEquals(Chunk.CHUNK_HEIGHT, world.getTiles().getHeight());
		assertEquals(Chunk.CHUNK_WIDTH * 3, world.getTiles().getWidth());
		assertNotEquals(beforeMovement, afterMovement);

		beforeMovement = world.getTiles().toArray();
		player.setPosition(-Chunk.CHUNK_WIDTH * 100000, 0); // move large distance left
		world.gameLoop(1);
		world.gameLoop(1);
		afterMovement = world.getTiles().toArray();
		assertEquals(Chunk.CHUNK_HEIGHT, world.getTiles().getHeight());
		assertEquals(Chunk.CHUNK_WIDTH * 3, world.getTiles().getWidth());
		assertNotEquals(beforeMovement, afterMovement);
	}

	@Test
	public void testWorldTilesAddBlocks() {
		init();
		WorldTiles tiles = new WorldTiles(20, 20, 20);
		tiles.addBlockRight(20, 20);
		tiles.addBlockLeft(40, 20);
		assertEquals(80, tiles.getWidth());
		assertEquals(20, tiles.getHeight());
	}

	@Test
	public void testOffset() {
		init();
		world.getTiles().setOffset(0);
		assertEquals(0, world.getTiles().getOffset());
		world.getTiles().setOffset(10);
		assertEquals(10, world.getTiles().getOffset());
	}

	@Test
	public void testNearbyEntities() {
		init();

		Player player = new Player();
		MeleeEnemyNPC meleeEnemyNPC = new MeleeEnemyNPC();

		world.addEntity(player);
		world.addEntity(meleeEnemyNPC);
		world.gameLoop(1);

		assertEquals(1, player.getNearbyEntities(100, MeleeEnemyNPC.class).size());
		assertEquals(1, player.getNearbyEntities(100).size());
		assertEquals(0, player.getNearbyEntities(100, RangedEnemyNPC.class).size());

	}

	@Test
	public void testGetFirstPlayer() {
		init();

		assertEquals(null, world.getFirstPlayer());

		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		assertEquals(player, world.getFirstPlayer());

		Player player2 = new Player();
		world.addEntity(player2);
		world.gameLoop(1);
		assertEquals(player, world.getFirstPlayer());
	}

	@Test
	public void testIngameText() {
		init();
		assertTrue(world.getIngameTexts().isEmpty());
		IngameText ingameText = new IngameText("+ SPD", 10, 10, 2000, IngameText.textType.STATIC, 1, 1, 1, 1);
		IngameText ingameText2 = new IngameText("+ SPD", 20, 20, 2000, IngameText.textType.DYNAMIC, 1, 1, 1, 1);
		world.setPlayerText(ingameText);
		world.addIngameText(ingameText2);
		world.gameLoop(1);
		world.gameLoop(1);
		assertTrue(world.getPlayerText().equals(ingameText));
		assertTrue(world.getIngameTexts().contains(ingameText2));
	}

	@Test
	public void testChangeToPuzzleRoom() {
		init();
		// Add player to world, then try to enter room and fail
		Player player = (Player) World.getInstance().addEntityToWorldPos(new Player(), 0, 0);
		World.getInstance().gameLoop(1);
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertFalse(World.getInstance() instanceof RoomWorld);
		// Send player to a room by placing a totem
		World.getInstance().addEntityToWorldPos(new Totem(), 0, 0);
		World.getInstance().gameLoop(1);
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertTrue(World.getInstance() instanceof PuzzleRoom);
		// set player to -5, -5 coordinates (where there shouldn't be any tiles)
		player.setPosition(-5, -5);
		// Test that entering the room again does nothing as there is no door to enter back through
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertTrue(World.getInstance() instanceof PuzzleRoom);
		// Add a locked door and again see that it does nothing
		RoomDoor door = (RoomDoor) World.getInstance().addEntityToWorldPos(new RoomDoor(true), -5, -5);
		World.getInstance().gameLoop(1);
		player.setPosition(door.getX(), door.getY());
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertTrue(World.getInstance() instanceof PuzzleRoom);
		assertTrue(door.isLocked());
		// Give the player a key and try again, seeing if the door unlocks
		player.getInventory().addItem(1, "puzzle_key");
		player.setPosition(door.getX(), door.getY());
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertTrue(World.getInstance() instanceof PuzzleRoom);
		assertFalse(door.isLocked());
		// Now try again and the player should have exited the world.
		player.setPosition(door.getX(), door.getY());
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertFalse(World.getInstance() instanceof RoomWorld);
	}

	@Test
	public void testChangeToBossRoom() {
		init();
		// Add player to world, then try to enter room and fail
		Player player = (Player) World.getInstance().addEntityToWorldPos(new Player(), 0, 0);
		World.getInstance().gameLoop(1);
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertFalse(World.getInstance() instanceof RoomWorld);
		// Send player to the boss room by placing a totem and giving them a duck king key
		player.getInventory().addItem(1, "duck_king_key");
		World.getInstance().addEntityToWorldPos(new Totem(), 0, 0);
		World.getInstance().gameLoop(1);
		World.handleEnterRoom();
		World.getInstance().gameLoop(1);
		assertTrue(World.getInstance() instanceof BossRoom);
	}

	@Test
	public void testSeeding() {
		init();
		world.setMapSeed(0);
		world.setChunkGenerationEnabled(true);
		Player player = new Player();
		world.addEntity(player);
		player.setPosition(0, 0);
		Object[] seed1 = world.getTiles().toArray();
		world.resetWorld();
		world.setMapSeed(1);
		world.addEntity(player);
		player.setPosition(0, 0);
		Object[] seed2 = world.getTiles().toArray();
		assertNotEquals(Arrays.asList(seed1), Arrays.asList(seed2));
	}

	/**
	 * Loads sprites, so they can be accessed by tiles to create a world.
	 */
	private void init() {
		TestHelper.load();
		world.debugReset();
	}
}
