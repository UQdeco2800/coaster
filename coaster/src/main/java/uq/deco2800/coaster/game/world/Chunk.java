package uq.deco2800.coaster.game.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uq.deco2800.coaster.game.entities.Decoration;
import uq.deco2800.coaster.game.entities.lighting.Light;
import uq.deco2800.coaster.game.entities.npcs.CommerceNPC;
import uq.deco2800.coaster.game.entities.npcs.mounts.BatMount;
import uq.deco2800.coaster.game.entities.npcs.mounts.BirdMount;
import uq.deco2800.coaster.game.entities.npcs.mounts.DogMount;
import uq.deco2800.coaster.game.entities.npcs.mounts.ElephantMount;
import uq.deco2800.coaster.game.entities.npcs.mounts.JumpingMount;
import uq.deco2800.coaster.game.entities.npcs.mounts.Mount;
import uq.deco2800.coaster.game.entities.npcs.mounts.RhinoMount;
import uq.deco2800.coaster.game.entities.npcs.mounts.SaveFlag;
import uq.deco2800.coaster.game.entities.npcs.mounts.TurtleMount;
import uq.deco2800.coaster.game.entities.puzzle.Totem;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * A Chunk class containing array for the underlying terrain and information for chunks
 */
public class Chunk {
	public static final int CHUNK_WIDTH = 240; // Default chunk width
	public static final int CHUNK_HEIGHT = 180; // Default chunk height

	// Middle chunk position (exact centre chunk in map, half way between 0 and INT MAX).
	public static final int MIDDLE_CHUNK_POS = CHUNK_WIDTH * Math.round((float) (Integer.MAX_VALUE / 2) / CHUNK_WIDTH);

	private static final int BEDROCK_FRACTION = 10; // fraction of the sub ground which will be bedrock
	private static final int SUB_GROUND_FRACTION = 10; // fraction of the sub ground which will be sub ground

	private static final int MAX_ORES_PER_CHUNK = 200; // maximum number of ores that can occur in a chunk

	private static final int BLEND_WIDTH = 5; // Default blend width, amount of blocks that will blend between biomes

	private static final int WATER_LEVEL = 50; // Default water level (counted from the top of map)
	public static final int GROUND_LEVEL = 40; // Default ground level (counted from the top of map)
	public static final int GROUND_LEVEL_LIMIT = 100; // Default ground level limit (counted from the top of map)
	private static final int CAVE_LEVEL = 120; // Default cave level (counted from the top of map)

	private static final int CHECK_POINT_RARITY = 2; // number of chunks per every check point generation
	private static final int VILLAGE_RARITY = 5; // number of chunks per every village generation
	private static final int MOUNT_RARITY = 10; // number of chunks per every mount generation
	private static final int TOTEM_RARITY = 20; // number of chunks per every totem generation

	private int lightSourceSpawnChance = 10; // light source spawn chance
	private int decorationSpawnChance = 2; // decoration spawn chance
	
	private int copperRarity = 80;
	private int silverRarity = 60;
	private int goldRarity = 40;
	private int diamondRarity = 20;
	

	private int id; //unique chunk id
	private Random randomGen; //random generator linked to chunk
	private BiomeType biomeType; //biome type of chunk
	private int xPos; //x position of chunk
	private int[] topBlocks = new int[CHUNK_WIDTH]; // list of block heights correlating to the top block position
	private int[] bedrockBlocks = new int[CHUNK_WIDTH]; // list of block heights correlating to the bedrock block position
	private WorldTiles blocks; //array of blocks currently in chunk

	/**
	 * Chunk constructor mainly used for testing or specific special chunks.
	 *
	 * @param tileTemplate tile set to insert into chunk
	 * @param biomeType    biome type of chunk
	 * @param xPos         x position of chunk
	 * @param topBlocks    the top block height at each x coordinate in the chunk
	 */
	public Chunk(WorldTiles tileTemplate, BiomeType biomeType, int xPos, int[] topBlocks) {
		if (tileTemplate.getWidth() != CHUNK_WIDTH || tileTemplate.getHeight() != CHUNK_HEIGHT) {
			throw new IllegalArgumentException("Invalid chunk dimensions");
		}

		this.blocks = tileTemplate;
		this.biomeType = biomeType;
		this.xPos = xPos;
		this.topBlocks = topBlocks;
		this.id = xPos / CHUNK_WIDTH;
		this.randomGen = new Random();
	}

	/**
	 * Standard chunk constructor. Receives a starting X position and a seed and will randomly generate the rest in a
	 * deterministic manner.
	 *
	 * @param startingX starting x position of the chunk
	 * @param mapSeed   map seed of the world
	 */
	public Chunk(int startingX, int mapSeed) {
		blocks = new WorldTiles(CHUNK_WIDTH, CHUNK_HEIGHT, CHUNK_WIDTH);

		this.xPos = CHUNK_WIDTH * ((MIDDLE_CHUNK_POS + startingX) / CHUNK_WIDTH);
		this.id = this.xPos / CHUNK_WIDTH;

		this.randomGen = new Random(CantorPairing.pair(id, mapSeed));
		this.biomeType = BiomeType.values()[randomGen.nextInt(BiomeType.values().length)];
		generateChunk();
	}

	/**
	 * Returns tile info of Biome
	 */
	private TileInfo getBiomeTileInfo(BiomeType biome) {
		switch (biome) {
			case PLAIN:
				return TileInfo.get(Tiles.GRASS);
			case DESERT:
				return TileInfo.get(Tiles.SAND);
			case ROCK:
				return TileInfo.get(Tiles.ROCK);
			case SNOW:
				return TileInfo.get(Tiles.SNOW);
			case FOREST:
			default:
				return TileInfo.get(Tiles.DIRT);
		}
	}

	/**
	 * This function generates the different biomes Forest, Desert, Plain, Rock, Grass, snow
	 */
	private void generateChunk() {
		TileInfo currentTileType = getBiomeTileInfo(this.biomeType);
		BiomeType leftChunk = getBiomeTypeOfX(xPos - MIDDLE_CHUNK_POS - CHUNK_WIDTH);
		TileInfo leftTileType = getBiomeTileInfo(leftChunk); //previous chunk

		for (int x = 0; x < CHUNK_WIDTH; x++) {
			// gets the top block placement
			this.topBlocks[x] = getTopBlockFromWaveForms(xPos + x, GROUND_LEVEL, 0, GROUND_LEVEL_LIMIT,
					World.getInstance().getTerrainWaveforms());

			// fill in water blocks if below water level
			if (topBlocks[x] >= WATER_LEVEL) {
				fillVerticalSpace(x, WATER_LEVEL, topBlocks[x], TileInfo.get(Tiles.WATER));
			}

			// specifies block thicknesses for bedrock and sub ground layers
			int bedRockThickness = (Chunk.CHUNK_HEIGHT - topBlocks[x]) / BEDROCK_FRACTION;
			int subGroundThickness = (Chunk.CHUNK_HEIGHT - topBlocks[x]) / SUB_GROUND_FRACTION;
			bedRockThickness -= randomGen.nextInt(bedRockThickness / 2) + bedRockThickness / 4;
			subGroundThickness -= randomGen.nextInt(subGroundThickness / 2) + subGroundThickness / 4;

			// defines the starting positions of the layers
			int subGroundStart = topBlocks[x] + subGroundThickness;
			int bedRockStart = Chunk.CHUNK_HEIGHT - bedRockThickness;
			bedrockBlocks[x] = bedRockStart;

			// insert sub ground layering
			fillVerticalSpace(x, topBlocks[x], subGroundStart, currentTileType);
			fillVerticalSpace(x, subGroundStart, bedRockStart, TileInfo.get(Tiles.ROCK));
			fillVerticalSpace(x, bedRockStart, CHUNK_HEIGHT - 1, TileInfo.get(Tiles.BEDROCK));

			// blend the current biome with the biome to the left
			if (x < BLEND_WIDTH) {
				fillVerticalSpace(x, topBlocks[x] + x, subGroundStart, leftTileType);
			}
		}

		generateOres();
		generateCaves();
		generateBuildings();
		generateDecorations();
		generateSpecial();
	}

	private void generateCaves() {
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			// gets the cave's top block placement
			int topBlock = getTopBlockFromWaveForms(xPos + x, CAVE_LEVEL, GROUND_LEVEL_LIMIT, CHUNK_HEIGHT,
					World.getInstance().getTerrainWaveforms());

			// gets the cave's bottom block placement
			int bottomBlock = getTopBlockFromWaveForms(xPos + x + 256, CAVE_LEVEL + 5, GROUND_LEVEL_LIMIT, CHUNK_HEIGHT,
					World.getInstance().getCaveWaveforms());

			fillVerticalSpace(x, topBlock, bottomBlock, TileInfo.get(Tiles.ROCK_BACKGROUND));
		}
	}


	/**
	 * Generates ore at random underground positions. What an ore deposit consists of depends on depth and rarity
	 */
	private void generateOres() {
		TileInfo randomOre = null;
		int numDeposits = randomGen.nextInt(MAX_ORES_PER_CHUNK);

		for (int i = 0; i < numDeposits; i++) {
			int x = randomGen.nextInt(CHUNK_WIDTH);
			int start = topBlocks[x];
			int end = bedrockBlocks[x];
			int padding = (end - start) / 16; // places ores away from surface
			int y = Math.min(end, randomGen.nextInt(end - start) + start + padding); // Keep underground
			int rarity = randomGen.nextInt(100);

			if (y < end * 5 / 10) {
				if (rarity < copperRarity) {
					randomOre = TileInfo.get(Tiles.COPPER);
				}

			} else if (y < end * 7 / 10) {
				if (rarity < silverRarity) {
					randomOre = TileInfo.get(Tiles.SILVER);
				} else if (rarity < copperRarity) {
					randomOre = TileInfo.get(Tiles.COPPER);
				}

			} else if (y < end * 9 / 10) {
				if (rarity < goldRarity) {
					randomOre = TileInfo.get(Tiles.GOLD);
				} else if (rarity < silverRarity) {
					randomOre = TileInfo.get(Tiles.SILVER);
				}

			} else {
				if (rarity < diamondRarity) {
					randomOre = TileInfo.get(Tiles.DIAMOND);
				} else if (rarity < goldRarity) {
					randomOre = TileInfo.get(Tiles.GOLD);
				} else if (rarity < silverRarity) {
					randomOre = TileInfo.get(Tiles.SILVER);
				}
			}
			if (randomOre == null) {
				continue;
			}
			generateDeposit(x, y, randomOre);
		}
	}
	
	/**
	 * From a starting position, grow an ore deposit using a simple algorithm
	 */
	private void generateDeposit(int startX, int startY, TileInfo ore) {
		int maxSize = randomGen.nextInt(10); //0 - 10
		int position = 0;
		int x = startX;
		int y = startY;
		List<Integer> xList = new ArrayList<>();
		List<Integer> yList = new ArrayList<>();
		xList.add(x);
		yList.add(y);
		int tempX;
		int tempY;
		int size = 0;
		while (size < maxSize && position < xList.size()) {
			x = xList.get(position);
			y = yList.get(position);
			
			if (randomGen.nextBoolean()) {
				blocks.set(x, y, ore);	
				size++;	
			}
			
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					tempX = x + i - 1;
					tempY = y + j - 1;
					tempX = Math.min(Math.max(tempX, 0), CHUNK_WIDTH);
					tempY = Math.min(Math.max(tempY, 0), CHUNK_HEIGHT);
					
					if (blocks.get(tempX, tempY).getTileType().getType() == Tiles.ROCK) {
						xList.add(tempX);
						yList.add(tempY);
					}		
				}
			}
			position++;
		}
	}


	/**
	 * Where applicable, generates a building into given world
	 */
	private void generateBuildings() {
		if (World.getInstance().isBuildingGenEnabled()) {
			int odds = this.xPos / CHUNK_WIDTH;
			if ((odds % VILLAGE_RARITY) == 0) {
				generateVillage();
			}
			if ((odds % CHECK_POINT_RARITY) == 0) {
				generateCheckpoint();
			}
		}
	}

	/**
	 * Generates raised platform with stairs, store, and bank
	 */
	private void generateCheckpoint() {
		World world = World.getInstance();
		int midX = CHUNK_WIDTH / 2; // middle of chunk
		int adjustedMidX = xPos - MIDDLE_CHUNK_POS + midX; // mid point adjusted for global positioning
		int maxY = topBlocks[midX] - 2; // max height of checkpoint (4 blocks above top block)
		int leftX = midX - 5; // left side of check point
		int rightX = midX + 5; // rights side of check point
		int flagX = adjustedMidX;
		int storeX = adjustedMidX + 3; // X position of store NPC
		int bankerX = adjustedMidX - 3; // X position of bank NPC
		float bankX = adjustedMidX - 4.7f; // X position of bank

		// return if under water
		if (maxY >= WATER_LEVEL) {
			return;
		}

		//Generate flat section of checkpoint
		generateFlat(leftX, rightX, maxY, TileInfo.get(Tiles.WOOD), true);

		//Inserts the bank building sprite
		SpriteList bankImage;
		switch(getBiomeType()) {
			case DESERT:
				bankImage = SpriteList.BANK_DESERT;
				break;
			case SNOW:
				bankImage = SpriteList.BANK_SNOW;
				break;
			default:
				bankImage = SpriteList.BANK;
		}

		Sprite bankSprite = new Sprite(bankImage);
		Decoration bankDecoration = new Decoration(bankSprite);
		bankDecoration.setPosition(bankX, maxY - bankDecoration.getHeight());
		world.addEntity(bankDecoration);

		//Inserts the bank and store trader NPC sprites
		CommerceNPC bankerNPC = new CommerceNPC(CommerceNPC.TraderType.BANK);
		CommerceNPC storeNPC = new CommerceNPC(CommerceNPC.TraderType.STORE);
		SaveFlag flag = new SaveFlag();
		bankerNPC.setPosition(bankerX, maxY - bankerNPC.getHeight());
		storeNPC.setPosition(storeX, maxY - storeNPC.getHeight());
		flag.setPosition(flagX, maxY - flag.getHeight());
		world.addEntity(bankerNPC);
		world.addEntity(storeNPC);
		world.addEntity(flag);
		
		generateStairsLeft(leftX, maxY);
		generateStairsRight(rightX, maxY);
	}


	/**
	 * Generate 1-3 buildings onto flat ground
	 */
	private void generateVillage() {
		int buildingGap = 3; //3 block gap padding buildings
		int villageWidth = buildingGap; //Dynamically growing width of village, starting with left gap

		int buildingCount = randomGen.nextInt(3) + 1; // number of buildings

		// adds buildings to buildings list
		Building building;
		List<Building> buildings = new ArrayList<>();
		for (int i = 0; i < buildingCount; i++) {
			int index = randomGen.nextInt(Building.getNumBuildings());

			building = new Building(index);
			if (villageWidth + building.getWidth() + buildingGap < CHUNK_WIDTH) {
				villageWidth += building.getWidth() + buildingGap;
				buildings.add(building);
			}
		}

		// get village position
		int villageX = randomGen.nextInt(CHUNK_WIDTH - villageWidth); // X position of Village
		int villageY = topBlocks[villageX]; // Y position of village

		if (villageY > WATER_LEVEL) {
			return;
		}

		// fill blocks under village
		TileInfo blockUnder = getBiomeTileInfo(this.biomeType);
		generateFlat(villageX, villageX + villageWidth, villageY, blockUnder, true);
		generateFlat(villageX, villageX + villageWidth, villageY, TileInfo.get(Tiles.WOOD), false);

		// place buildings
		int buildingPlacement = villageX + buildingGap; // X position placement of building
		for (Building buildingCurrent : buildings) {
			buildingCurrent.build(buildingPlacement, villageY, this);

			// Adjust building placement for next building
			buildingPlacement += buildingGap;
			buildingPlacement += buildingCurrent.getWidth();
		}
	}

	/**
	 * Generate flat landscape in given range
	 */
	private void generateFlat(int startX, int endX, int y, TileInfo tile, boolean filled) {
		for (int x = startX; x < endX; x++) {
			blocks.get(x, y).setTileType(tile);

			if (topBlocks[x] < y) {
				fillVerticalSpace(x, topBlocks[x], y, TileInfo.get(Tiles.AIR));

			} else if (filled) {
				fillVerticalSpace(x, y, topBlocks[x], tile);
			}

			topBlocks[x] = y;
		}
	}

	/**
	 * Generate stairs moving down one block every x coordinate (to the left), until ground is reached
	 */
	private void generateStairsLeft(int startX, int startY) {
		int x = startX;
		int y = startY;
		do {
			fillVerticalSpace(x, y, topBlocks[x], TileInfo.get(Tiles.WOOD));
			topBlocks[x] = y;
			x--;
			y++;
		} while (y < topBlocks[x]);
	}

	/**
	 * Generate stairs moving down one block every x coordinate (to the right), until ground is reached
	 */
	private void generateStairsRight(int startX, int startY) {
		int x = startX;
		int y = startY;
		do {
			fillVerticalSpace(x, y, topBlocks[x], TileInfo.get(Tiles.WOOD));
			topBlocks[x] = y;
			x++;
			y++;
		} while (y < topBlocks[x]);
	}

	/**
	 * Make a pillar of given material in given range
	 */
	private void fillVerticalSpace(int x, int startY, int endY, TileInfo newTile) {
		for (int y = startY; y < endY; y++) {
			blocks.set(x, y, newTile);
		}
	}

	/**
	 * Returns if the top block of given x is "natural" (as defined by this function)
	 */
	private boolean isNaturalSurface(int x) {
		TileInfo tile = blocks.get(x, topBlocks[x]).getTileType();
		return tile.isNaturalSurface();
	}

	/**
	 * Generate decoration at every position in chunk that is a) Natural (as defined by isNaturalSurface()) b) Is above
	 * sea level and c) fits the decorationSpawnChance
	 */
	public void generateDecorations() {
		if (World.getInstance().isDecoGenEnabled()) {
			for (int x = 0; x < CHUNK_WIDTH; x++) {
				if (topBlocks[x] < WATER_LEVEL && isNaturalSurface(x)) {
					decorationGenerator(x, getTopBlockFromChunk(x));
				}
			}
		}
		if (World.getInstance().isLightGenEnabled()) {
			for (int x = 0; x < CHUNK_WIDTH; x++) {
				if (topBlocks[x] < WATER_LEVEL && isNaturalSurface(x)) {
					lightSourceGenerator(x, getTopBlockFromChunk(x));
				}
			}
		}
	}

	/**
	 * Adds a random biome-appropriate decoration at the given position
	 */
	private void decorationGenerator(int x, int y) {
		if (randomGen.nextInt(decorationSpawnChance) == 0) {
			List<SpriteList> decorations = new ArrayList<>();
			switch (this.getBiomeType()) {
				case ROCK:
					decorations.add(SpriteList.BOULDER);
					decorations.add(SpriteList.TREE_DEAD);
					break;
				case FOREST:
					decorations.add(SpriteList.TREE_TALL_1);
					decorations.add(SpriteList.TREE_SHORT_1);
					break;
				case DESERT:
					decorations.add(SpriteList.TREE_PALM);
					break;
				case SNOW:
					decorations.add(SpriteList.TREE_DEAD);
					decorations.add(SpriteList.TREE_SNOW);
					break;
				case PLAIN:
					decorations.add(SpriteList.FLOWERPINK);
					decorations.add(SpriteList.FLOWERBLUE);
					decorations.add(SpriteList.FLOWER);
					decorations.add(SpriteList.GRASS);
					break;
				default:
					break;
			}

			if (!decorations.isEmpty()) {
				SpriteList image = decorations.get(randomGen.nextInt(decorations.size()));
				World world = World.getInstance();
				Sprite sprite = new Sprite(image);
				Decoration decoration = new Decoration(sprite);
				world.addEntity(decoration);
				decoration.setPosition(xPos - MIDDLE_CHUNK_POS + x, y - decoration.getHeight());
			}
		}
	}

	/**
	 * In certain chunks, generate Mount or Totem
	 */
	private void generateSpecial() {
		int odds = this.xPos / CHUNK_WIDTH;
		if (World.getInstance().isTotemGenEnabled() && (odds % TOTEM_RARITY) == 0) {
			generateTotem();
		}
		if (World.getInstance().isTotemGenEnabled() && (odds % MOUNT_RARITY) == 0) {
			generateMount();
		}
	}

	/**
	 * Place totem at random position in chunk
	 */
	private void generateTotem() {
		int spawnX = randomGen.nextInt(CHUNK_WIDTH);
		if (isNaturalSurface(spawnX)) {
			Totem totem = new Totem();
			World.getInstance().addEntity(totem);
			totem.setPosition((xPos - MIDDLE_CHUNK_POS) + spawnX, topBlocks[spawnX] - totem.getHeight());
		}
	}

	/**
	 * Place biome-specific mount at random position in chunk
	 */
	private void generateMount() {
		int spawnX = randomGen.nextInt(CHUNK_WIDTH);
		int evenSplit = randomGen.nextInt(100);
		Mount mount;
		if (isNaturalSurface(spawnX) && topBlocks[spawnX] < WATER_LEVEL) {
			switch (biomeType) {
				case SNOW:
					mount = new DogMount();
				case PLAIN:
					if(evenSplit>50){
						mount = new JumpingMount();
					} else {
						mount = new BirdMount();
					}
					break;
				case ROCK:
					mount = new TurtleMount();
				case FOREST:
					if(evenSplit>50){
						mount = new BatMount();
					} else {
						mount = new ElephantMount();
					}
					break;
				case DESERT:
					mount = new RhinoMount();
				default:
					mount = new RhinoMount();
					break;
			}
			
			generateFlat(spawnX, spawnX + (int) mount.getWidth(), topBlocks[spawnX],TileInfo.get(Tiles.WOOD), true);
			World.getInstance().addEntity(mount);
			mount.setPosition((xPos - MIDDLE_CHUNK_POS) + spawnX, topBlocks[spawnX] - mount.getHeight());
		}
	}

	/**
	 * Adds a light source at the given position
	 */
	private void lightSourceGenerator(int x, int y) {
		if (randomGen.nextInt(lightSourceSpawnChance) == 0) {
			Sprite sprite = new Sprite(SpriteList.TORCH);
			Light lightSource = new Light(sprite, 8);
			World.getInstance().addEntity(lightSource);
			lightSource.setPosition(xPos - MIDDLE_CHUNK_POS + x, y - lightSource.getHeight());
		}
	}

	/**
	 * Returns the biome-type of the chunk closest to the given x coordinate
	 *
	 * @param x x coordinate to get biome type of
	 * @return return the biome type at the given x coordinate
	 */
	public static BiomeType getBiomeTypeOfX(int x) {
		int position = (MIDDLE_CHUNK_POS + x) / CHUNK_WIDTH;
		Random randomGen = new Random(CantorPairing.pair(position, World.getMapSeed()));
		return BiomeType.values()[randomGen.nextInt(BiomeType.values().length)];
	}

	/**
	 * Set block at position to given tile
	 */
	public void set(int x, int y, TileInfo tile) {
		blocks.get(x, y).setTileType(tile);
	}

	/**
	 * Returns the chunk's x position on map
	 *
	 * @return chunk's x position
	 */
	public int getX() {
		return xPos;
	}

	/**
	 * This returns the value of the top Y value of the certain X coordinate
	 *
	 * @param x X-coordinate
	 * @return returns value of the top block
	 */
	public int getTopBlockFromChunk(int x) {
		return topBlocks[x];
	}

	/**
	 * Programmatically determine what the top block in a given position will be
	 */
	public static int getTopBlockFromWaveForms(int x, int offset, int upperBound, int lowerBound, List<Waveform> waveforms) {
		if (waveforms.isEmpty()) {
			return Math.min(Math.max(offset, upperBound), lowerBound);
		}

		// get unique cantor pair from x position of block and the world seed, scale it to fit with 0 and 1.
		double blockX = (double)CantorPairing.pair(x, World.getMapSeed()) / Integer.MAX_VALUE
				/ (CantorPairing.pair(Integer.MAX_VALUE, World.getMapSeed()) / Integer.MAX_VALUE );

		// iterate over the waveforms and sum their outputs, average the result.
		int combinedNoise = 0;
		for (Waveform waveform : waveforms) {
			combinedNoise += NoiseGenerator.generate(blockX, waveform.getPeriod(), waveform.getAmplitude());
		}
		combinedNoise /= waveforms.size();

		return Math.min(Math.max(offset + combinedNoise, upperBound), lowerBound);
	}


	/**
	 * This returns the biome type that would be generated
	 *
	 * @return returns the random biome type
	 */
	public BiomeType getBiomeType() {
		return biomeType;
	}

	/**
	 * Returns the chunk's id
	 *
	 * @return chunk id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the chunks tile set of blocks
	 *
	 * @return chunk's block tileset
	 */
	public WorldTiles getBlocks() {
		return blocks;
	}

	/**
	 * Sets spawn chance of mob generation
	 */
	public void setDecorationSpawnChance(int spawnChance) {
		this.decorationSpawnChance = spawnChance;
	}

	/**
	 * Sets spawn chance of light source
	 */
	public void setLightSourceSpawnChance(int spawnChance) {
		this.lightSourceSpawnChance = spawnChance;
	}
}