package uq.deco2800.coaster.game.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.Engine;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.debug.Debug;
import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Decoration;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.*;
import uq.deco2800.coaster.game.entities.npcs.mounts.Mount;
import uq.deco2800.coaster.game.entities.*;
import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.entities.puzzle.RoomDoor;
import uq.deco2800.coaster.game.entities.puzzle.TerrainEntity;
import uq.deco2800.coaster.game.entities.puzzle.Totem;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.mechanics.Difficulty;
import uq.deco2800.coaster.game.preservation.ExportableEntity;
import uq.deco2800.coaster.game.preservation.ExportableItem;
import uq.deco2800.coaster.game.preservation.ExportableMovingEntity;
import uq.deco2800.coaster.game.preservation.ExportablePlayer;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.game.weather.Lightning;
import uq.deco2800.coaster.graphics.Renderer;
import uq.deco2800.coaster.graphics.Viewport;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.notifications.IngameText;
import uq.deco2800.coaster.graphics.screens.GameScreen;
import uq.deco2800.coaster.graphics.screens.Screen;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.singularity.clients.coaster.CoasterClient;
import uq.deco2800.singularity.common.representations.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;


/**
 * A World class containing array for the underlying terrain as well as the list
 * of entities in the level.
 */
public class World {

	private static final Logger logger = LoggerFactory.getLogger(World.class);

	private static World world = new World(); // Singleton instance
	private WorldTiles tiles; // World tiles

	private Random randomGen = new Random(); // random number generator
	private static int mapSeed = 123456789; // default seed, use Integer.MAX_VALUE for a flat world! :)
	private long framesPerSecond; // Frames per second of the running game
	static final int NUM_MOBS_PER_CHUNK = 50; // default number of mobs that can be generated in a chunk
	private boolean renderHitboxes = false; // whether or not hit boxes are shown.
	private boolean isNpcGenEnabled = false; // whether or not npcs get generated.
	private boolean isDecoGenEnabled = false; // whether or not decorations get generated.
	private boolean isLightGenEnabled = false; // whether or not light gets generated.
	private boolean isBuildingGenEnabled = false; // whether or not decorations get generated.
	private boolean isTotemGenEnabled = false; // whether or not totems get generated
	private int npcSpawnChance = 10; // inverse of the chance of an npc spawning. If this is 50, then chance is 1/50
	private List<Entity> allEntities = new ArrayList<>(); // list of all entities
	private List<Player> playerEntities = new ArrayList<>(); // list of player entities
	private List<Entity> npcEntities = new ArrayList<>(); // list of npc entities
	private List<Entity> mountEntities = new ArrayList<>(); // list of npc entities
	private List<Decoration> decorationEntities = new ArrayList<>(); // list of decoration entities
	private List<CommerceNPC> storeEntities = new ArrayList<>(); // list of store entities
	private List<ItemEntity> itemEntities = new ArrayList<>(); // list of power up entities
	private List<TerrainEntity> terrainEntities = new ArrayList<>(); // list of terrain entities (e.g. moving platforms)
	private List<Entity> newEntities = new ArrayList<>(); // list of new entities to be added
	private List<Entity> deleteEntities = new ArrayList<>(); // list of deleted entities to be deleted
	private Debug debug = new Debug(); // debugger initialiser
	private List<IngameText> ingameTexts = new ArrayList<>(); // list of IngameText to be added
	private IngameText playerText; // IngameText to be declared
	private List<Waveform> terrainWaveforms = new ArrayList<>(); // list of waveforms that make up the world terrain
	private List<Waveform> caveWaveforms = new ArrayList<>(); // list of waveforms that make up the cave terrain
	private boolean isGameOver = false; // Property that keeps track of whether this world's game has ended.
	private boolean terrainDestructionEnabled = true; // boolean that states whether terrain destruction is allowed or not
	private boolean chunkGenerationEnabled = true; // boolean that states whether chunk generation is allowed or not
	private boolean randomSeedEnabled = false; // boolean that states whether a random seed is allowed or not
	private boolean destructionShadowUpdate = false;
	private boolean skillTreeScreen = false;

	private boolean lightingEnabled = true;
	private boolean tutorialMode = false;

	private double difficultyScale = 1.0; // Double value to scale according to world's set difficulty

	private int entityRenderDistance = Chunk.CHUNK_WIDTH; // default entity rendering distance

	// Singularity
	private CoasterClient client;
	private User user;
	// Rooms
	private boolean inRoom = false;
	private RoomWorld room = null;
	private boolean lightningEnabled = true;

	//lighting
	private int globalLightLevel = 100;

	/**
	 * Returns the singleton instance of World
	 */
	public static World getInstance() {
		return world.inRoom ? world.room : world;
	}

	/**
	 * Initialise an empty world instance
	 */
	World() {
		logger.info("new empty world instance");
		tiles = new WorldTiles();
	}

	/**
	 * Set the horizontal distance away from the Player that entities will still be processed
	 */
	public void setEntityRenderDistance(int entityRenderDistance) {
		this.entityRenderDistance = entityRenderDistance;
	}

	/**
	 * Return the horizontal distance away from the Player that entities will still be processed
	 */
	public int getEntityRenderDistance() {
		return entityRenderDistance;
	}

	/**
	 * Set the ambient light level of the world
	 */
	public void setGlobalLightLevel(int lightLevel) {
		this.globalLightLevel = lightLevel;
	}

	/**
	 * Return the ambient light level of the world
	 */
	public int getGlobalLightLevel() {
		return globalLightLevel;
	}

	/**
	 * Adds a waveform to the terrain waveform list
	 */
	public void addTerrainWaveform(Waveform waveform) {
		terrainWaveforms.add(waveform);
	}

	/**
	 * Gets the terrain waveform list
	 */
	List<Waveform> getTerrainWaveforms() {
		return terrainWaveforms;
	}

	/**
	 * Gets the cave waveform list
	 */
	List<Waveform> getCaveWaveforms() {
		return caveWaveforms;
	}

	/**
	 * Adds a waveform to the cave waveform list
	 */
	public void addCaveWaveform(Waveform waveform) {
		caveWaveforms.add(waveform);
	}

	/**
	 * Sets chunk generation to either disabled or enabled
	 */
	void setChunkGenerationEnabled(boolean chunkGenerationEnabled) {
		this.chunkGenerationEnabled = chunkGenerationEnabled;
	}

	/**
	 * Gets whether chunk generation is enabled or disabled
	 */
	public boolean isChunkGenerationEnabled() {
		return chunkGenerationEnabled;
	}

	/**
	 * Gets whether random seed is enabled or disabled
	 */
	public boolean isRandomSeedEnabled() {
		return randomSeedEnabled;
	}

	/**
	 * Sets random seed generator to either disabled or enabled
	 */
	public void setRandomSeedEnabled(boolean randomSeedEnabled) {
		this.randomSeedEnabled = randomSeedEnabled;
	}

	/**
	 * Gets the current map seed
	 */
	public static int getMapSeed() {
		return mapSeed;
	}

	/**
	 * Sets the map seed
	 */
	static void setMapSeed(int mapSeed) {
		World.mapSeed = mapSeed;
	}

	/**
	 * Replaces the current tiles in the world with a new tile set.
	 *
	 * @param tileTemplate The new set of tiles to replace the world's current ones
	 */
	public void setTiles(WorldTiles tileTemplate) {
		logger.info("tiles set");
		if (tileTemplate.getWidth() % Chunk.CHUNK_WIDTH != 0 || tileTemplate.getHeight() != Chunk.CHUNK_HEIGHT) {
			throw new IllegalArgumentException("Invalid chunk dimensions (" +
					tileTemplate.getWidth() + ", " + tileTemplate.getHeight() + ")");
		}
		tiles = tileTemplate;
	}

	/**
	 * Reset the world tiles to an empty state and remove all decoration
	 * entities.
	 */
	public void resetTiles() {
		logger.info("tiles reset");
		tiles = new WorldTiles();
		decorationEntities.clear();
	}

	/**
	 * Reset the world back to its empty state. Clears all entities and states.
	 */
	public void resetWorld() {
		if (this instanceof RoomWorld) {
			((RoomWorld) this).getParentWorld().resetWorld();
		}
		if (randomSeedEnabled) {
			randomGen.setSeed(System.currentTimeMillis());
			setMapSeed(randomGen.nextInt(1000000000)); //1000000000 produces nice terrain at that cap
		}
		// Firstly make sure to exit the room
		inRoom = false;
		room = null;
		clearEntities();
		isGameOver = false;
		skillTreeScreen = false;
		renderHitboxes = false;
		debug = new Debug();
		MiniMap.setVisibility(false);
		resetTiles();
	}

	/**
	 * Reset the world to a state for use in testing
	 */
	public void debugReset() {
		if (this instanceof RoomWorld) {
			((RoomWorld) this).getParentWorld().debugReset();
		}
		// Firstly make sure to exit the room
		inRoom = false;
		room = null;
		clearEntities();
		isGameOver = false;
		skillTreeScreen = false;
		difficultyScale = 1.0;
		renderHitboxes = false;
		isNpcGenEnabled = false;
		npcSpawnChance = 10;
		isDecoGenEnabled = false;
		isBuildingGenEnabled = false;
		isTotemGenEnabled = false;
		chunkGenerationEnabled = false;
		randomSeedEnabled = false;
		debug = new Debug();
		MiniMap.setVisibility(false);
		resetTiles();
	}

	/**
	 * Toggles the lighting in game
	 */
	public void setLighting(boolean lightingState) {
		lightingEnabled = lightingState;
	}

	/**
	 * @return true if lighting is on else false
	 */
	public boolean getLightingState() {
		return lightingEnabled;
	}


	public void initMulti() {
		resetWorld();
		isNpcGenEnabled = false;
		npcSpawnChance = 0;
		chunkGenerationEnabled = false;
	}

	/**
	 * Sets the world difficulty
	 *
	 * @param option Difficulty option to set the world to
	 */
	public void setDifficulty(Difficulty option) {
		switch (option) {
			case EASY:
				difficultyScale = 0.5;
				break;
			case HARD:
				difficultyScale = 1.5;
				break;
			case INSANE:
				difficultyScale = 2.0;
				break;
			case MEDIUM:
				// default case
			default:
				difficultyScale = 1.0;
		}
	}

	/**
	 * inverts boolean value renderHitboxes
	 */
	public void toggleHitboxes() {
		renderHitboxes = !renderHitboxes;
	}

	/**
	 * returns value renderHitboxes
	 *
	 * @return boolean value renderHitbox variable
	 */
	public boolean renderHitboxes() {
		return renderHitboxes;
	}

	/**
	 * Adds an entity to a private Entity array newEntities Note that this does
	 * not add entity to the game, but addEntities will
	 *
	 * @param entity to be added
	 */
	public void addEntity(Entity entity) {
		newEntities.add(entity);
	}

	/**
	 * Adds an entity to a private Entity array deleteEntities Note that this
	 * does not delete entity from the game, but deleteEntities will
	 *
	 * @param entity to be
	 */
	public void deleteEntity(Entity entity) {
		deleteEntities.add(entity);
	}

	/**
	 * add IngameText object to an List<IngameText>
	 */
	public void addIngameText(IngameText ingameText) {
		ingameTexts.add(ingameText);
	}

	/**
	 * gets List<IngameText>
	 *
	 * @return List<IngameText>
	 */
	public List<IngameText> getIngameTexts() {
		return ingameTexts;
	}

	/**
	 * sets player text, used for player status
	 */
	public void setPlayerText(IngameText ingameText) {
		playerText = ingameText;
	}

	/**
	 * returns player Text
	 *
	 * @return playerText
	 */
	public IngameText getPlayerText() {
		return playerText;
	}

	/**
	 * Returns a controller for DebugScreen for this world object.
	 *
	 * @return Debug object
	 */
	public Debug getDebug() {
		return debug;
	}


	/**
	 * Returns a list of all Entities
	 *
	 * @return list of all Entities
	 */
	public List<Entity> getAllEntities() {
		return allEntities;
	}

	/**
	 * Returns a list of NPC Entities
	 *
	 * @return list of NPC Entities
	 */
	public List<Entity> getNpcEntities() {
		return npcEntities;
	}

	/**
	 * Returns a list of mount Entities
	 *
	 * @return list of mount Entities
	 */
	public List<Entity> getMountEntities() {
		return mountEntities;
	}

	/**
	 * Returns a list of Decoration Entities
	 *
	 * @return list of Decoration Entities
	 */
	public List<Decoration> getDecorationEntities() {
		return decorationEntities;
	}

	/**
	 * Returns a list of Item Entities
	 *
	 * @return list of Item Entities
	 */
	public List<ItemEntity> getItemEntities() {
		return itemEntities;
	}

	/**
	 * Returns a list of Player Entities
	 *
	 * @return list of Player Entities
	 */
	public List<Player> getPlayerEntities() {
		return playerEntities;
	}

	public List<TerrainEntity> getTerrainEntities() {
		return terrainEntities;
	}

	/**
	 * Returns the frames per second of the game
	 *
	 * @return frames per second represented as a long
	 */
	public long getFps() {
		return framesPerSecond;
	}

	/**
	 * Returns a list of Store Entities
	 *
	 * @return list of Store Entities
	 */
	List<CommerceNPC> getStoreEntities() {
		return storeEntities;
	}

	/**
	 * Returns WorldTiles object of this world
	 *
	 * @return WorldTiles
	 */
	public WorldTiles getTiles() {
		return tiles;
	}

	/**
	 * Returns boolean for a variable isGameOver, which determines if the game
	 * is over or not.
	 *
	 * @return true if game is over
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * Sets Private variable isGameover to a boolean value passed
	 */
	void setGameOver(boolean gameOver) {
		isGameOver = gameOver;
	}

	/**
	 * Returns whether the entity is near any empty (i.e. un-generated) chunks.
	 *
	 * @return int representing the border the entity is near, else if not
	 * within a chunk lengths distance of border will return 0.
	 */
	private int isNearEmpty(Entity entity) {
		if (!tiles.test(entity.getNearestChunkX() - Chunk.CHUNK_WIDTH, 0)) {
			return 1; // no chunks left
		} else if (!tiles.test(entity.getNearestChunkX(), 0)) {
			return 2; // no chunks underneath
		} else if (!tiles.test(entity.getNearestChunkX() + Chunk.CHUNK_WIDTH, 0)) {
			return 3; // no chunks right
		}
		return 0; // not near any empty chunks
	}

	/**
	 * Returns the number of mobs in the chunk specified by a position
	 */
	int getNumMobsInChunk(int x) {
		int chunkPos = Chunk.CHUNK_WIDTH * (x / Chunk.CHUNK_WIDTH); // rounds to
		// nearest
		// chunk
		int mobCount = 0;

		for (Entity mob : npcEntities) {
			// increments mob count if mob is within the specified chunk
			if (mob.getX() >= chunkPos && mob.getX() < chunkPos + Chunk.CHUNK_WIDTH) {
				mobCount++;
			}
		}

		return mobCount;
	}

	/**
	 * @return A list of entities within the viewport.
	 */
	private List<Entity> getEntitiesInViewport(Predicate<Entity> condition) {
		List<Entity> viewportEntities = new ArrayList<>();

		Viewport viewport = Window.getEngine().getRenderer().getViewport();
		for (Entity e : allEntities) {
			if (e.getX() > viewport.getLeft() && e.getX() < viewport.getLeft() + viewport.getWidth()
					&& e.getY() > viewport.getTop() && e.getY() < viewport.getTop() + viewport.getHeight()) {
				if (condition.test(e)) {
					viewportEntities.add(e);
				}
			}
		}

		return viewportEntities;
	}

	/**
	 * Generates mobs at random and places them within the vicinity of the given
	 * player.
	 */
	void npcGenerator(Player player, int spawnRate, boolean randomSpawn, int mobX, BiomeType biomeType) {
		if (isNpcGenEnabled() && randomGen.nextInt(npcSpawnChance) == 0) {

			// checks if a mob should be positioned randomly
			if (randomSpawn) {
				// X position of mob placement between -CHUNK_WIDTH and
				// -CHUNK_WIDTH / 2 or CHUNK_WIDTH / 2 and
				// CHUNK_WIDTH
				mobX = (int) player.getX() + randomGen.nextInt(Chunk.CHUNK_WIDTH)
						+ (Chunk.CHUNK_WIDTH / 2) * (randomGen.nextBoolean() ? 1 : -3);
			}

			// checks if a biome has been specified, otherwise specify one based
			// off of position
			if (biomeType == null) {
				biomeType = Chunk.getBiomeTypeOfX(mobX);
			}

			// if number mobs exceeds limit, prevent generation
			if (getNumMobsInChunk(mobX) >= NUM_MOBS_PER_CHUNK) {
				return;
			}

			BaseNPC mob;
			if (spawnRate == 0) {
				mob = new RatNPC();
			} else {
				switch (biomeType) {
					case SNOW:
						mob = new IceSpiritNPC();
						break;
					case PLAIN:
						if (spawnRate < 99) {
							mob = new RhinoNPC();
						} else {
							mob = new SpaceSlimeNPC();
						}
						break;
					case DESERT:
						if (spawnRate < 5) {
							mob = new GhostShipNPC();
						} else if (spawnRate < 25) {
							mob = new MeleeEnemyNPC();
						} else {
							mob = new EyeballNPC();
						}
						break;
					case ROCK:
						if (spawnRate < 20) {
							mob = new RangedEnemyNPC();
						} else if (spawnRate < 60){
							mob= new GrenadierNPC();
						}else {
							mob = new SkeletonNPC();
						}
						break;
					case FOREST:
						mob = new BatNPC();
						break;
					default:
						mob = new MeleeEnemyNPC();
						break;
				}
			}
			if (!(mob instanceof GhostShipNPC) && (new Random()).nextFloat() < 0.03) {
				mob.setBoss();
			}
			this.addEntity(mob);
			mob.setPosition(mobX, -10);
			if (mob instanceof SpaceSlimeNPC) {
				((SpaceSlimeNPC) mob).spawnMob();
			}
		}
	}


	/**
	 * Loads chunk around player when the entity is near empty
	 */
	public void loadAroundPlayer(Player player) {
		while (isNearEmpty(player) != 0 && chunkGenerationEnabled) {
			loadChunk(player.getNearestChunkX(), isNearEmpty(player));
		}
	}

	/**
	 * This is called every game tick, and calls down into our list of entities
	 * and calls tick on them as well.
	 */
	public void gameLoop(long ms) {

		// converts to frames per second
		framesPerSecond = ms == 0 ? Long.MAX_VALUE : 1000 / ms;
		if (!isGameOver()) {
			processEntities(ms);
			addEntities();
			removeEntities();
			if (this.lightningEnabled) {
				processLightning();
			}
		}
	}


	public void gameLoopMulti(long ms) {
		framesPerSecond = ms == 0 ? Long.MAX_VALUE : 1000 / ms;
		if (!isGameOver()) {
			processEntitiesMulti(ms);
			addEntitiesMulti();
			removeEntities();
			if (this.lightningEnabled) {
				processLightning();
			}

		}
	}

	private void addEntitiesMulti() {
		for (Entity entity : newEntities) {
			entity.setWorld(this);
			if (entity instanceof PlayerMultiDummy) {
				logger.info("Adding dummy");
				// playerEntities.add((PlayerMultiDummy) entity);
				allEntities.add(entity);
			} else if (entity instanceof PlayerMulti) {
				logger.info("Adding Multi");
				playerEntities.add((PlayerMulti) entity);
				allEntities.add(entity);
			} else if (entity instanceof GenericBullet) {
				allEntities.add(entity);
			} else {
				newEntities.clear();
				throw new IllegalArgumentException("cannot add non PlayerMulti or GenericBullet during multiplayer");
			}
		}
		newEntities.clear();
	}

	/**
	 * This method is used to set lightning on and off
	 */

	public void setLightning(boolean input) {
		this.lightningEnabled = input;
	}

	/**
	 * Generate a lightning strike randomly near the player, hitting a random
	 * mob and dealing 20% of their health.
	 */
	private void processLightning() {
		// TODO: turn the mob into a skeleton for the few frames that the
		// lightning strike is displayed?

		Engine engine = Window.getEngine();
		if (engine == null) {
			return;
		}

		Renderer renderer = engine.getRenderer();
		if (renderer == null) {
			return;
		}

		Viewport viewport = renderer.getViewport();

		Random random = new Random();
		if (random.nextFloat() > 0.99) {
			// Select a random mob in the viewport to hit
			List<Entity> viewportEntities = getEntitiesInViewport(
					e -> e instanceof BaseNPC && !(e instanceof CompanionNPC) && !(e instanceof DuckKingNPC));
			if (viewportEntities.size() == 0) {
				return;
			}

			Entity entity = viewportEntities.get(random.nextInt(viewportEntities.size()));

			// The lightning is spawned directly above the entity, +/- 5 tiles
			// on the x-axis
			float x = entity.getX() + (random.nextFloat() * 10 - 5);
			Lightning lightning = new Lightning(x, viewport.getTop() - 5, entity.getX(), entity.getY());

			Screen screen = Window.getEngine().getRenderer().getScreen("Game");
			if (screen != null) {
				GameScreen gs = (GameScreen) screen;
				gs.setRenderingLightning(lightning);
				SoundCache.play("lightning");
				BaseNPC npc = (BaseNPC) entity;
				if (!(npc instanceof GhostShipNPC)) {
					npc.receiveDamageForce((int) (npc.getMaxHealth() * 0.2));
				}
			}
		}
	}

	/**
	 * adds entities in list entities and playerEntities from that exists in
	 * newEntities
	 */
	private void addEntities() {
		for (Entity entity : newEntities) {
			allEntities.add(entity);
			if (entity instanceof PlayerMulti) {
				playerEntities.add((PlayerMulti) entity);
			} else if (entity instanceof Player) {
				playerEntities.add((Player) entity);
			} else if (entity instanceof BaseNPC) {
				npcEntities.add(entity);
			} else if (entity instanceof Decoration) {
				decorationEntities.add((Decoration) entity);
			} else if (entity instanceof CommerceNPC) {
				storeEntities.add((CommerceNPC) entity);
			} else if (entity instanceof ItemEntity) {
				itemEntities.add((ItemEntity) entity);
			} else if (entity instanceof Mount) {
				mountEntities.add(entity);
			} else if (entity instanceof TerrainEntity) {
				terrainEntities.add((TerrainEntity) entity);
			}
		}
		newEntities.clear();
	}

	/**
	 * removes entities in list entities and playerEntities from that exists in
	 * deleteEntities
	 */
	private void removeEntities() {
		for (Entity entity : deleteEntities) {
			allEntities.remove(entity);
			if (entity instanceof PlayerMulti) {
				playerEntities.remove(entity);
			} else if (entity instanceof Player) {
				playerEntities.remove(entity);
			} else if (entity instanceof BaseNPC) {
				npcEntities.remove(entity);
			} else if (entity instanceof Decoration) {
				decorationEntities.remove(entity);
			} else if (entity instanceof CommerceNPC) {
				storeEntities.remove(entity);
			} else if (entity instanceof ItemEntity) {
				itemEntities.remove(entity);
			} else if (entity instanceof Mount) {
				mountEntities.remove(entity);
			} else if (entity instanceof TerrainEntity) {
				terrainEntities.remove(entity);
			}
		}
		deleteEntities.clear();
	}

	private void processEntitiesMulti(long ms) {
		for (Player p : playerEntities) {
			if (p instanceof PlayerMulti) {
				//logger.info("PlayerMulti loop");
				((PlayerMulti) p).entityLoop(ms);
			} else {
				logger.error("Player in multiplayer that isn't of right class " + p.getClass().toGenericString());
			}
		}
		for (Entity entity : allEntities) {
			if (entity instanceof GenericBullet) {
				entity.entityLoop(ms);
			} else if (entity instanceof PlayerMultiDummy) {
				((PlayerMultiDummy) entity).entityLoop(ms);
			}
		}

	}


	/**
	 * Controls flow of the game, such as: make entities move generates terrain
	 * if any entity is near an empty chunk generates npc near player if entity
	 * is an instance of player
	 */

	private void processEntities(long ms) {
		Player player = getFirstPlayer();
		for (TerrainEntity platform : terrainEntities) {
			platform.entityLoop(ms);
		}
		for (Entity entity : allEntities) {
			if (!inRoom && player == null || (entity.getX() > (player.getX() - entityRenderDistance)
					&& entity.getX() < (player.getX() + entityRenderDistance))) {
				if (entity instanceof TerrainEntity) {
					continue;
				}
				entity.entityLoop(ms);
			}
		}
		if (player == null) {
			return;
		}

		loadAroundPlayer(player);
		npcGenerator(player, randomGen.nextInt(100), true, 0, null);
		setGameOver(player.getCurrentState() == EntityState.DEAD);
	}

	/**
	 * Updates the current player with the data loaded, it's important to note
	 * that only the position and health is loaded currently.
	 *
	 * @param ep the ExportablePlayer to update information from
	 */
	public void loadPlayer(ExportablePlayer ep) {
		// List WILL be len 1
		if (this.playerEntities.isEmpty()) {
			throw new IllegalStateException("No player to load over");
		}
		// Set new position
		getFirstPlayer().setPosition(ep.posX, ep.posY);
		// Fill up health to max
		getFirstPlayer().addHealth(getFirstPlayer().getMaxHealth());
		// Decrement health to current health in save
		getFirstPlayer().addHealth(ep.currentHealth - getFirstPlayer().getMaxHealth());
		// Set gold
		getFirstPlayer().getCommerce().addGold(ep.currentGold);
		allEntities.add(getFirstPlayer());
		// TODO log changes
	}

	/**
	 * replaces all current entities with the ExportableEntity (s) provided
	 *
	 * @param ee the exportable entities to be imported
	 */
	public void loadEntities(List<ExportableEntity> ee) {
		allEntities = new ArrayList<>();
		if (!ee.isEmpty()) {
			ee.forEach(this::loadEntity);
		}
	}

	/**
	 * adds a single entity
	 *
	 * @param ee the ExportableEntity to be imported
	 */
	private void loadEntity(ExportableEntity ee) {
		Entity e = new MeleeEnemyNPC();
		if (ee instanceof ExportableItem) {
			e = new ItemEntity(ItemRegistry.getItem(((ExportableItem) ee).itemReference));
			((ItemEntity) e).setRemoveDelay(((ExportableItem) ee).removeDelay);
		} else if (ee instanceof ExportableMovingEntity) {
			try {
				ExportableMovingEntity exportableMovingEntity = (ExportableMovingEntity) ee;
				BasicMovingEntity bme = (BasicMovingEntity) e;
				bme.addHealth(bme.getMaxHealth());
				bme.addHealth(exportableMovingEntity.currentHealth - bme.getMaxHealth());
				e = bme;
			} catch (ClassCastException exception) {
				return;
				//fix errors
			}
		} else {
			if (ee.decoration) {
				e = new Decoration(new Sprite(ee.spriteID));
			}
			// else normal entity
		}
		e.setPosition(ee.posX, ee.posY);
		allEntities.add(e);
	}

	/**
	 * Loads the next row of chunks relative to the players chunk location (aka
	 * movement).
	 */
	private void loadChunk(int chunkLocation, int chunkPlacement) {
		if (this instanceof RoomWorld) {
			return;
		}
		int left = chunkLocation;

		switch (chunkPlacement) {
			case 1: // empty chunk left
				left -= Chunk.CHUNK_WIDTH; // rounded to nearest beginning of chunk
				tiles.addChunkLeft();
				break;

			case 2: // empty chunk under
				tiles.addChunkRight();
				break;

			case 3: // empty chunk to right
				left += Chunk.CHUNK_WIDTH; // rounded to nearest beginning of chunk
				tiles.addChunkRight();
				break;

			default:
				break;
		}

		Chunk chunk = new Chunk(left, mapSeed);

		for (int x = left; x < left + Chunk.CHUNK_WIDTH; x++) {
			for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
				if (!tiles.test(x, y)) {
					return;
				}

				TileInfo chunkTile = chunk.getBlocks().get(x - left, y).getTileType();
				tiles.get(x, y).setTileType(chunkTile);
			}
		}
	}


	public boolean getSkillTreeScreen() {
		return skillTreeScreen;
	}

	/**
	 * gets the set difficulty scale value
	 *
	 * @return double value of difficultyScale
	 */
	public double getDifficulty() {
		return difficultyScale;
	}

	/**
	 * Will get the inverse of the chance of a mob spawning
	 *
	 * @return inverse of the chance of a mob spawning
	 */
	int getNpcSpawnChance() {
		return npcSpawnChance;
	}

	/**
	 * Set the chance of a mob spawning
	 *
	 * @param npcSpawnChance the inverse of the desired chance of the mob
	 *                       spawning.
	 */
	void setNpcSpawnChance(int npcSpawnChance) {
		this.npcSpawnChance = npcSpawnChance;
	}

	/**
	 * gets whether or not mob generation is enabled
	 *
	 * @return whether mob generation is enabled
	 */
	public boolean isNpcGenEnabled() {
		return isNpcGenEnabled;
	}

	/**
	 * Will set whether or not mob generation is enabled
	 *
	 * @param npcGenEnabled true if mob generation is enabled, false if not
	 */
	public void setNpcGenEnabled(boolean npcGenEnabled) {
		isNpcGenEnabled = npcGenEnabled;
	}

	/**
	 * gets whether or not decoration generation is enabled
	 *
	 * @return whether decoration generation is enabled
	 */
	boolean isLightGenEnabled() {
		return isLightGenEnabled;
	}

	/**
	 * Will set whether or not decoration generation is enabled
	 * <p>
	 *
	 * @param enabled true if decoration generation is enabled, false if not
	 */
	public void setLightGenEnabled(boolean enabled) {
		this.isLightGenEnabled = enabled;
	}

	/**
	 * gets whether or not decoration generation is enabled
	 *
	 * @return whether decoration generation is enabled
	 */
	public boolean isDecoGenEnabled() {
		return isDecoGenEnabled;
	}

	/**
	 * Will set whether or not decoration generation is enabled
	 * <p>
	 *
	 * @param enabled true if decoration generation is enabled, false if not
	 */
	public void setDecoGenEnabled(boolean enabled) {
		this.isDecoGenEnabled = enabled;
	}

	/**
	 * gets whether or not building generation is enabled
	 *
	 * @return whether building generation is enabled
	 */
	public boolean isBuildingGenEnabled() {
		return isBuildingGenEnabled;
	}

	/**
	 * Will set whether or not building generation is enabled
	 *
	 * @param enabled true if building generation is enabled, false if not Will
	 *                set whether or not decoration generation is enabled
	 */
	public void setBuildingGenEnabled(boolean enabled) {
		this.isBuildingGenEnabled = enabled;
	}

	/**
	 * gets whether or not totem generation is enabled
	 *
	 * @return whether totem generation is enabled
	 */
	public boolean isTotemGenEnabled() {
		return isTotemGenEnabled;
	}

	/**
	 * Will set whether or not totem generation is enabled
	 *
	 * @param enabled true if totem generation is enabled, false if not
	 */
	public void setTotemGenEnabled(boolean enabled) {
		this.isTotemGenEnabled = enabled;
	}

	/**
	 * @return the client
	 */
	public CoasterClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(CoasterClient client) {
		this.client = client;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the first player that was added to the world. Currently, should
	 * be the only player.
	 */
	public Player getFirstPlayer() {
		if (playerEntities.isEmpty() && newEntities.isEmpty()) {
			return null;
		} else if (!newEntities.isEmpty() && newEntities.get(0) instanceof Player) {
			return (Player) newEntities.get(0);
		}
		return playerEntities.isEmpty() ? null : playerEntities.get(0);
	}

	/**
	 * Sets the game into tutorial mode
	 */

	public void setTutorialMode(boolean tutorialMode) {
		this.tutorialMode = tutorialMode;
	}

	/**
	 * @return the game's tutorial mode status
	 */
	public boolean getTutorialMode() {
		return this.tutorialMode;
	}

	/**
	 * Sets the game into tutorial mode
	 */
	public void setTerrainDestruction(boolean input) {
		this.terrainDestructionEnabled = input;
	}

	/**
	 * @return the game's terrain Destruction mode status
	 */
	public boolean getTerrainDestructionMode() {
		return this.terrainDestructionEnabled;
	}

	/**
	 * Tutorial world
	 */
	public static WorldTiles getTutorialWorld() {
		WorldTiles flatTiles = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
		for (int x = 0; x < flatTiles.getWidth(); x++) {
			for (int y = 0; y < flatTiles.getHeight(); y++) {
				flatTiles.set(x, y, TileInfo.get(Tiles.DIRT_BACKGROUND));
			}
		}
		for (int x = 0; x < flatTiles.getWidth(); x++) {
			flatTiles.set(x, 120, TileInfo.get(Tiles.GRASS));
			for (int y = 121; y < flatTiles.getHeight(); y++) {
				flatTiles.set(x, y, TileInfo.get(Tiles.DIRT));
			}
			for (int y = 121; y < flatTiles.getHeight(); y++) {
				if ((y + x) % 2 == 1) {
					continue;
				}
				flatTiles.set(x, y, TileInfo.get(Tiles.DIRT_BACKGROUND));
			}
		}
		return flatTiles;
	}

	public WorldTiles getMultiWorld() {
		WorldTiles flatTiles = new WorldTiles(Chunk.CHUNK_WIDTH, Chunk.CHUNK_HEIGHT, Chunk.CHUNK_WIDTH);
		for (int x = 0; x < 51; x++) {
			flatTiles.set(x, 0, TileInfo.get(Tiles.ROCK));
			flatTiles.set(x, 120, TileInfo.get(Tiles.BEDROCK));
			for (int y = 121; y < flatTiles.getHeight(); y++) {
				flatTiles.set(x, 120, TileInfo.get(Tiles.DIRT));
			}
			flatTiles.set(x, 120, TileInfo.get(Tiles.BEDROCK));
		}
		for (int y = 0; y < 120; y++) {
			flatTiles.set(0, y, TileInfo.get(Tiles.ROCK));
		}
		for (int y = 0; y < 120; y++) {
			flatTiles.set(50, y, TileInfo.get(Tiles.ROCK));
		}
		return flatTiles;
	}


	/**
	 * Update the physics for all decorations in the world
	 */
	public void checkDecorations() {
		for (Entity entity : decorationEntities) {
			entity.updatePhysics(0);
		}

	}

	public boolean getDestructionShadowUpdate() {
		return destructionShadowUpdate;
	}

	public void setDestructionShadowUpdate(boolean update) {
		destructionShadowUpdate = update;
	}

	/**
	 * This method will add an entity to this world and set it's position.
	 *
	 * @param entity the entity to be added
	 * @param x      the x coordinate to place it
	 * @param y      the y coordinate to place it
	 * @return the entity that was added.
	 */
	Entity addEntityToWorldPos(Entity entity, float x, float y) {
		entity.setWorld(this);
		entity.setPosition(x, y);
		this.addEntity(entity);
		return entity;
	}

	/**
	 * This is called when the player presses the Enter Room key. It will handle
	 * the relevant action.
	 */
	public static void handleEnterRoom() {
		World world = World.getInstance();
		// Change to a room
		if (world instanceof RoomWorld) {
			Player player = world.getFirstPlayer();
			for (Entity entity : player.getNearbyEntities(2)) {
				if (entity instanceof RoomDoor) {
					useDoor((RoomWorld) world, player, (RoomDoor) entity);
					break;
				}
			}
		} else {
			Player player = world.getFirstPlayer();
			if (player.getOnMountStatus()) {
				return;
			}
			for (Entity entity : player.getNearbyEntities(2)) {
				if (entity instanceof Totem) {
					world.setRoom(true);
					world.deleteEntity(entity);
					break;
				}
			}
		}
	}

	/**
	 * Try make the player leave through the door in the given world
	 *
	 * @param world  the room the door and player are in
	 * @param player the player using the door
	 * @param door   the door being used
	 */
	private static void useDoor(RoomWorld world, Player player, RoomDoor door) {
		if (!door.isLocked() || !player.getInventory().itemExists("puzzle_key")) {
			if (!door.isLocked()) {
				// Remove all keys from player before returning to world
				while (player.getInventory().itemExists("puzzle_key")) {
					player.getInventory().removeItem(1, "puzzle_key");
				}
				world.getParentWorld().setRoom(false);
			}
		} else {
			door.setLocked(false);
			player.getInventory().removeItem(1, "puzzle_key");
			// add experience points is broken, rip
			for (int i = 0; i < 100; i++) {
				player.addExperiencePoint(10);
			}
			player.getInventory().addItem(1000, "ammo");
		}
	}

	/**
	 * Clears every entity list in the current world
	 */
	private void clearEntities() {
		ingameTexts.clear();
		allEntities.clear();
		decorationEntities.clear();
		npcEntities.clear();
		mountEntities.clear();
		playerEntities.clear();
		storeEntities.clear();
		itemEntities.clear();
		newEntities.clear();
		terrainEntities.clear();
		deleteEntities.clear();
	}

	/**
	 * Will handle entering or leaving a room.
	 *
	 * @param inRoom true if changing to a room, false if leaving a room
	 */
	private void setRoom(boolean inRoom) {
		this.inRoom = inRoom;
		Player firstPlayer = getFirstPlayer();
		if (inRoom) {
			if (firstPlayer != null && firstPlayer.getInventory().itemExists("duck_king_key")) {
				room = new BossRoom();
				firstPlayer.getInventory().removeItem(1, "duck_king_key");
			} else {
				room = new PuzzleRoom();
			}
			room.populateRoom();
			room.setParentWorld(this);
			if (firstPlayer != null) {
				firstPlayer.setWorld(room);
				room.setTempPlayerX(firstPlayer.getX());
				room.setTempPlayerY(firstPlayer.getY());
				room.addEntity(firstPlayer);
				firstPlayer.setX(room.getStartingX());
				firstPlayer.setY(room.getStartingY());
			}
		} else {
			if (firstPlayer != null && room != null) {
				firstPlayer.setWorld(room.getParentWorld());
				firstPlayer.setX(room.getTempPlayerX());
				firstPlayer.setY(room.getTempPlayerY() - 1);
			}
			room = null;
		}
	}
}