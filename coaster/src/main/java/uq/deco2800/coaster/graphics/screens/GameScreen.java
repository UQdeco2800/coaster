package uq.deco2800.coaster.graphics.screens;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.weapons.ProjectileType;
import uq.deco2800.coaster.game.inventory.Inventory;
import uq.deco2800.coaster.game.items.Weapon;
import uq.deco2800.coaster.game.mechanics.TimeOfDay;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.game.weather.Lightning;
import uq.deco2800.coaster.game.world.*;
import uq.deco2800.coaster.graphics.LayerList;
import uq.deco2800.coaster.graphics.Viewport;
import uq.deco2800.coaster.graphics.notifications.IngameText;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameScreen extends Screen {
	private Canvas canvas;
	private GraphicsContext gc;
	private Viewport viewport;
	private Sprite crosshair = new Sprite(SpriteList.CROSSHAIR);

	private Image moon = new Image("/sprites/moon.png");
	private Image sun = new Image("/sprites/sun.png");
	private Image stars = new Image("/sprites/stars.png");

	private Image ship1 = new Image("/sprites/ship-1.png");
	private Image ship2 = new Image("/sprites/ship-2.png");
	private Image planet1 = new Image("/sprites/planet-1.png");
	private Image planet2 = new Image("/sprites/planet-2.png");

	private Glow glow = new Glow(1);

	private long backgroundTimer;
	private long time;
	private TimeOfDay timePhase;
	private Map<TimeOfDay, LinearGradient> backgrounds;
	private int lastLightLevel;

	private List<Tiles> lightSources = new ArrayList<>();

	private Map<Integer, Integer> shadows = new HashMap<>();

	private Lightning currentLightning;
	private final Color LIGHTNING_OVERLAY = new Color(0, 0, 0, 0.5);
	private final Color LIGHTNING_COLOR = new Color(0.76, 0.92, 1, 1);
	private final int LIGHTNING_LINE_WIDTH = 3;
	private int currentLightningRenderTime = 0;
	private int lightningRenderTime = 120; //2 frames

	Logger logger = LoggerFactory.getLogger(GameScreen.class);
	boolean drawF;
	Sprite fSprite;
	boolean drawQ;
	Sprite qSprite;
	boolean drawE;
	Sprite eSprite;
	boolean drawR;
	Sprite rSprite;

	int lastLeft = 0;
	int lastTop = 0;
	int lastRight = 0;
	int lastBottom = 0;
	float firstTileSize = 0;
	boolean destructionShadowUpdate = false;
	WritableImage shadowMap;


	public GameScreen(Viewport viewport, Canvas canvas) {
		super(canvas);
		this.viewport = viewport;
		this.setVisible(true);
		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();


		this.backgroundTimer = 0;
		this.time = TimeOfDay.HOUR_LENGTH * 4;
		timePhase = TimeOfDay.DAY;
		World.getInstance().setGlobalLightLevel(timePhase.getLightLevel());

		loadGradients();

		//set which tiles do not "hide" blocks
		lightSources.add(Tiles.AIR);
		lightSources.add(Tiles.WATER);
		lightSources.add(Tiles.WINDOW);
		lightSources.add(Tiles.ROOF);
		lightSources.add(Tiles.DOOR);
		lightSources.add(Tiles.WALLS);
		lightSources.add(Tiles.PUZZLE_BACKGROUND_BRICK);
		lightSources.add(Tiles.PUZZLE_SOLID_BRICK);
		lightSources.add(Tiles.DIRT_BACKGROUND);
		lightSources.add(Tiles.SNOW_BACKGROUND);
		lightSources.add(Tiles.ROCK_BACKGROUND);
		lightSources.add(Tiles.SAND_BACKGROUND);
		lightSources.add(Tiles.BOSS_ROOM_BACKGROUND);
		lightSources.add(Tiles.BOSS_ROOM_GROUND);
		lightSources.add(Tiles.BOSS_ROOM_INVISIBLE_WALL);

		//create shadow colors
		for (int i = 0; i <= 100; i++) {
			shadows.put(i, new java.awt.Color(0, 0, 0, i / 100f).getRGB());
		}

	}

	public void setRenderingLightning(Lightning inputLightning) {
		//TODO: won't do as you expect if a lightning is already striking, so add a queue
		//It'll currently just override the current lightning if it's in progress.
		//Honestly not noticeable since each lightning strike is only 2 frames right now.
		currentLightning = inputLightning;
		currentLightningRenderTime = lightningRenderTime;
	}

	@Override
	public void setWidth(int newWidth) {
		canvas.setWidth(newWidth);
	}

	@Override
	public void setHeight(int newHeight) {
		canvas.setHeight(newHeight);
	}


	public void render(long ms, boolean renderBackground) {
		World world = World.getInstance();
		if (world instanceof BossRoom) {
			drawBossRoomBackground((BossRoom) world);
		} else {
			//Background
			dayAndNightCycle(ms, renderBackground);

			//Render tiles
			renderTerrain(World.getInstance().getTiles());
		}

		//Render entities
		renderEntities(World.getInstance().getAllEntities(), ms);

		//render texts
		renderTexts(World.getInstance().getIngameTexts(), ms);

		if (!(world instanceof BossRoom)) {
			//Render minimap
			renderMap();
		}

		//Draw crosshair on screen
		gc.drawImage(crosshair.getFrame(), InputManager.getMousePixelX() - crosshair.getWidth() / 2,
				InputManager.getMousePixelY() - crosshair.getHeight() / 2);

		if (currentLightningRenderTime > 0) {
			renderLightning(ms);
		}
	}

	/**
	 * Will draw the background for the boss room. Has a lot of magic numbers,
	 * sorry.
	 *
	 * @param world
	 */

	private void drawBossRoomBackground(BossRoom world) {
		gc.setFill(new Color((float) 88 / 255, (float) 171 / 255, 1, 1));
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		float tileSize = viewport.getTileSideLength();
		int topBorder = viewport.getTopBorder();
		int top = (int) Math.floor(viewport.getTop());
		float subTileShiftY = (viewport.getTop() - top) * tileSize;
		float y = (14 - top) * tileSize + topBorder - subTileShiftY;

		Sprite background = world.getBackground();
		float ratio = y / 255;
		int width = (int) (background.getWidth() * ratio);
		int height = (int) (background.getHeight() * ratio);

		gc.drawImage(background.getFrame(), 0, 0, width, height);
		gc.drawImage(background.getFrame(), width, 0, width, height);
	}

	/**
	 * renders texts on positions inside world
	 *
	 * @param ingameTexts List<IngameText>
	 * @param ms
	 */
	private void renderTexts(List<IngameText> ingameTexts, long ms) {

		World world = World.getInstance();
		if (world.getPlayerText() != null) {
			world.getPlayerText().render(gc, viewport, ms);

		}
		for (IngameText ingameText : ingameTexts) {
			ingameText.render(gc, viewport, ms);
		}
	}

	/**
	 * Renders the current lightning to the screen, applying a dark overlay to
	 * the rest of the screen.
	 *
	 * @param ms
	 */

	private void renderLightning(long ms) {
		//Render lightning overlay
		gc.setFill(LIGHTNING_OVERLAY);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		//Render lightning
		float[] xPositions = currentLightning.getXPositions();
		float[] yPositions = currentLightning.getYPositions();
		gc.setLineWidth(LIGHTNING_LINE_WIDTH);
		for (int i = 1; i < xPositions.length; i++) {
			float lastX = viewport.getPixelCoordX(xPositions[i - 1]);
			float lastY = viewport.getPixelCoordY(yPositions[i - 1]);

			float thisX = viewport.getPixelCoordX(xPositions[i]);
			float thisY = viewport.getPixelCoordY(yPositions[i]);

			//Render a line from the previous point to the current point
			gc.setStroke(LIGHTNING_COLOR);
			gc.strokeLine(lastX, lastY, thisX, thisY);
		}

		currentLightningRenderTime -= ms;
	}


	/**
	 * This function uses timers to change the current in game time by seconds
	 * and changes the gradient of the background to fit the right
	 * Day/Night/Dawn/Dusk setting
	 */
	private void dayAndNightCycle(long ms, boolean renderBackground) {
		clearBackground();

		if (renderBackground) {
			backgroundTimer += ms;
			time += ms;
		}


		if (backgroundTimer > timePhase.getDuration()) {
			timePhase = timePhase.next();
			backgroundTimer = 0;
		}

		if (time > TimeOfDay.DAY_LENGTH) {
			time = 0;
		}

		drawGradient(backgrounds.get(timePhase));
		if (timePhase == TimeOfDay.NIGHT || timePhase == TimeOfDay.PRE_DAWN) {
			gc.drawImage(stars, 0, 0, viewport.getResWidth(), viewport.getResHeight());
		}

		double fraction = backgroundTimer / (double) timePhase.getDuration();
		gc.save();
		gc.setGlobalAlpha(fraction);
		drawGradient(backgrounds.get(timePhase.next()));
		if (timePhase.next() == TimeOfDay.NIGHT || timePhase.next() == TimeOfDay.PRE_DAWN) {
			gc.drawImage(stars, 0, 0, viewport.getResWidth(), viewport.getResHeight());

		}
		int lightLevel = timePhase.getLightGradient(fraction);
		if (lightLevel != lastLightLevel) {
			World.getInstance().setGlobalLightLevel(lightLevel);
			World.getInstance().setDestructionShadowUpdate(true);
			lastLightLevel = lightLevel;
		}
		gc.restore();

		runSunAnimation();
		runMoonAnimation();
//		runShipAnimation();
//		runOtherShipAnimation();
//		runPlanetAnimation();
	}


	/**
	 * Clears the background image
	 */
	public void clearBackground() {
		gc.clearRect(0, 0, viewport.getResWidth(), viewport.getResHeight());
	}


	/**
	 * Creates all the gradient background colours
	 */
	private void loadGradients() {

		LinearGradient dawn = new LinearGradient(
				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0f, Color.rgb(116, 86, 105)),
				new Stop(0.25f, Color.rgb(116, 86, 105)),
				new Stop(1.0f, Color.rgb(251, 165, 90))

		);

		LinearGradient day = new LinearGradient(
				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0f, Color.SKYBLUE),
				new Stop(0.5f, Color.WHITESMOKE),
				new Stop(0.75f, Color.DARKGREY),
				new Stop(1.0f, Color.ROSYBROWN)

		);
		LinearGradient dusk = new LinearGradient(
				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0f, Color.rgb(72, 42, 146)),
				new Stop(0.5f, Color.rgb(98, 46, 141)),
				new Stop(0.75f, Color.rgb(144, 63, 136)),
				new Stop(1.0f, Color.rgb(248, 98, 121))

		);
		LinearGradient night = new LinearGradient(

				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0.25f, Color.rgb(0, 24, 72)),
				new Stop(0.5f, Color.rgb(48, 24, 96)),
				new Stop(0.75f, Color.rgb(72, 48, 120)),
				new Stop(1.0f, Color.ROSYBROWN)

		);
		LinearGradient afternoon = new LinearGradient(
				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0.25f, Color.rgb(104, 124, 198)),
				new Stop(0.5f, Color.rgb(147, 112, 176)),
				new Stop(0.75f, Color.rgb(152, 98, 147)),
				new Stop(1.0f, Color.ROSYBROWN)

		);

		LinearGradient evening = new LinearGradient(
				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0.25f, Color.rgb(36, 33, 109)),
				new Stop(0.5f, Color.rgb(73, 35, 119)),
				new Stop(0.75f, Color.rgb(108, 56, 128)),
				new Stop(0.9f, Color.ROSYBROWN)

		);

		LinearGradient preDawn = new LinearGradient(
				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0.25f, Color.rgb(77, 65, 94)),
				new Stop(0.5f, Color.rgb(93, 65, 102)),
				new Stop(0.75f, Color.rgb(191, 126, 100)),
				new Stop(0.9f, Color.ROSYBROWN)

		);

		LinearGradient morning = new LinearGradient(

				0.0,
				0.0,
				0.0,
				1.0,
				true,
				CycleMethod.NO_CYCLE,
				new Stop(0.25f, Color.rgb(129, 166, 202)),
				new Stop(0.5f, Color.rgb(202, 192, 198)),
				new Stop(0.75f, Color.rgb(210, 167, 130)),
				new Stop(0.9f, Color.ROSYBROWN)

		);

		backgrounds = new HashMap<>();
		backgrounds.put(TimeOfDay.DAWN, dawn);
		backgrounds.put(TimeOfDay.MORNING, morning);
		backgrounds.put(TimeOfDay.DAY, day);
		backgrounds.put(TimeOfDay.AFTERNOON, afternoon);
		backgrounds.put(TimeOfDay.DUSK, dusk);
		backgrounds.put(TimeOfDay.EVENING, evening);
		backgrounds.put(TimeOfDay.NIGHT, night);
		backgrounds.put(TimeOfDay.PRE_DAWN, preDawn);

	}

	/**
	 * draws the gradient background on canvas
	 *
	 * @param background which
	 */

	private void drawGradient(LinearGradient background) {
		gc.setFill(background);
		gc.fillRect(0, 0, viewport.getResWidth(), viewport.getResHeight());
		gc.setStroke(Color.WHITE);
		gc.setFont(new Font(20));
	}


	/**
	 * Draws the sun in an arc in the sky
	 */
	private void runSunAnimation() {
		if (time < TimeOfDay.SUN_RISE || time > TimeOfDay.SUN_SET) {
			return;
		}
		// center on viewport.getWidth()/2, viewPort.get
		double position = Math.PI * (time - TimeOfDay.SUN_RISE) / (TimeOfDay.SUN_SET - TimeOfDay.SUN_RISE);
		double xPos = viewport.getResWidth() / 2 + viewport.getResWidth() * 0.75 * Math.cos(position);
		double yPos = viewport.getResHeight() + viewport.getResHeight() * -Math.sin(position);
		gc.save();
		gc.setEffect(glow);
		gc.drawImage(sun, xPos, yPos);
		gc.restore();
	}

	/**
	 * Draws the moon in an arc in the sky
	 */
	private void runMoonAnimation() {
		if (time < TimeOfDay.MOON_RISE || time > TimeOfDay.MOON_SET) {
			return;
		}
		// center on viewport.getWidth()/2, viewPort.get
		double position = Math.PI * (time - TimeOfDay.MOON_RISE) / (TimeOfDay.MOON_SET - TimeOfDay.MOON_RISE);
		double xPos = viewport.getResWidth() / 2 + viewport.getResWidth() * 0.75 * Math.cos(position);
		double yPos = viewport.getResHeight() + viewport.getResHeight() * -Math.sin(position);
		gc.save();
		gc.setEffect(glow);
		gc.drawImage(moon, xPos, yPos);
		gc.restore();
	}

	/**
	 * Draws some ships moving right to left for the first fifth of each hour
	 */
	private void runShipAnimation() {
		if (time % TimeOfDay.HOUR_LENGTH > TimeOfDay.HOUR_LENGTH / 5) {
			return;
		}
		long animationTime = (time % TimeOfDay.HOUR_LENGTH);
		long animationLength = TimeOfDay.HOUR_LENGTH / 5;

		double position = (animationTime) / (double) animationLength;

		double xPos = viewport.getResWidth() * 1.05 - (position * viewport.getResWidth() * 1.05);
		double yPos = 80;

		gc.save();
		gc.drawImage(ship1, xPos, yPos);
		gc.drawImage(ship1, xPos + 30, yPos + 25);
		gc.drawImage(ship1, xPos + 30, yPos - 25);
		gc.restore();
	}

	/**
	 * Draws some ships moving left to right for the 4th sixth of each hour
	 */
	private void runOtherShipAnimation() {
		if ((time % TimeOfDay.HOUR_LENGTH > TimeOfDay.HOUR_LENGTH * 4 / 6)
				|| (time % TimeOfDay.HOUR_LENGTH < TimeOfDay.HOUR_LENGTH * 3 / 6)) {
			return;
		}
		long animationTime = (time % TimeOfDay.HOUR_LENGTH) - TimeOfDay.HOUR_LENGTH * 3 / 6;
		long animationLength = TimeOfDay.HOUR_LENGTH / 6;

		double position = (animationTime) / (double) animationLength;

		double xPos = position * viewport.getResWidth() * 1.05;
		double yPos = 100;

		gc.save();
		gc.drawImage(ship2, xPos, yPos);
		gc.drawImage(ship2, xPos - 40, yPos + 25);
		gc.drawImage(ship2, xPos - 40, yPos - 25);
		gc.drawImage(ship2, xPos - 80, yPos);
		gc.drawImage(ship2, xPos - 80, yPos + 50);
		gc.drawImage(ship2, xPos - 80, yPos - 50);
		gc.restore();
	}


	/**
	 * Draws some planets moving left to right over the course of each hour at night
	 */
	private void runPlanetAnimation() {
		if (time < TimeOfDay.MOON_RISE || time > TimeOfDay.MOON_SET) {
			return;
		}
		long animationTime = (time % TimeOfDay.HOUR_LENGTH);
		long animationLength = TimeOfDay.HOUR_LENGTH;

		double position = (animationTime) / (double) animationLength;

		double xPos = position * viewport.getResWidth() * 1.5;
		double yPos = 100;

		gc.save();
		gc.drawImage(planet1, xPos, yPos);
		gc.drawImage(planet2, xPos - 500, yPos + 60);
		gc.drawImage(planet1, xPos - 150, yPos - 41);
		gc.drawImage(planet2, xPos + 300, yPos - 27);
		gc.drawImage(planet1, xPos + 100, yPos + 50);
		gc.drawImage(planet2, xPos + 800, yPos + 68);
		gc.restore();
	}

	private void renderTerrain(WorldTiles tiles) {

		float tileSize = viewport.getTileSideLength();
		int leftBorder = viewport.getLeftBorder();
		int topBorder = viewport.getTopBorder();

		int left = (int) Math.floor(viewport.getLeft());
		int top = (int) Math.floor(viewport.getTop());
		int right = (int) Math.ceil(viewport.getRight());
		int bottom = (int) Math.ceil(viewport.getBottom());

		float subTileShiftX = (viewport.getLeft() - left) * tileSize;
		float subTileShiftY = (viewport.getTop() - top) * tileSize;

		int extraFogSpace = 5;
		//fog calculations
		for (int x = left - extraFogSpace; x <= right + extraFogSpace; x++) {
			for (int y = top - extraFogSpace; y <= bottom + extraFogSpace; y++) {
				if (!tiles.test(x, y)) {
					// check for invalid lookups
					continue;
				}
				if (World.getInstance().getLightingState()) {
					calculateFog(x, y, tiles);
				}
			}
		}

		int padding = 1;
		boolean needToRenderShadows;
		if (firstTileSize == 0) {
			firstTileSize = tileSize;
		}
		if (World.getInstance().getLightingState()
				&& !(lastLeft == left && lastRight == right && lastBottom == bottom & lastTop == top)) {
			shadowMap = new WritableImage((int) firstTileSize * (right - left + 1 + 2 * padding),
					(int) firstTileSize * (bottom - top + 1 + 2 * padding));
			lastLeft = left;
			lastRight = right;
			lastBottom = bottom;
			lastTop = top;
			needToRenderShadows = true;
			World.getInstance().setDestructionShadowUpdate(false);
		} else if (World.getInstance().getDestructionShadowUpdate()) {
			needToRenderShadows = true;
			World.getInstance().setDestructionShadowUpdate(false);
		} else {
			needToRenderShadows = false;
		}
		int[] pixels = new int[(int) (firstTileSize) * (int) (firstTileSize)];
		WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
		//display sprites and light level
		for (int x = left - padding; x <= right + padding; x++) {
			for (int y = top - padding; y <= bottom + padding; y++) {
				//We still want to iterate over "negative" tiles even if we don't render so we can center the map
				boolean invalidTile = false;
				if (!tiles.test(x, y)) {
					invalidTile = true;
				}

				if (!invalidTile) {
					Sprite sprite = tiles.get(x, y).getSprite();
					float xPos = (x - left) * tileSize + leftBorder;
					float yPos = (y - top) * tileSize + topBorder;
					gc.drawImage(sprite.getFrame(), xPos - subTileShiftX, yPos - subTileShiftY, tileSize, tileSize);
				}
				if (World.getInstance().getLightingState()) {

					int totalLightLevel;
					if (!invalidTile) {
						totalLightLevel = tiles.get(x, y).getFogAndLight();
						if (lightSources.contains(tiles.get(x, y).getTileType().getType())) {
							totalLightLevel = tiles.get(x, y).getLightLevel();
						}
					} else {
						totalLightLevel = World.getInstance().getGlobalLightLevel();

					}
					if (totalLightLevel == 0) {
						continue;
					}
					if (needToRenderShadows) {
						for (int i = 0; i < firstTileSize; i++) {
							for (int j = 0; j < firstTileSize; j++) {
								int arrayLocation = (int) ((i) + (firstTileSize * j));
								pixels[arrayLocation] = shadows.get(totalLightLevel);
							}
						}
						shadowMap.getPixelWriter().setPixels((int) ((x - (left - padding)) * firstTileSize),
								(int) ((y - (top - padding)) * (firstTileSize)), (int) firstTileSize,
								(int) firstTileSize, format, pixels, 0, (int) firstTileSize);
					}
				}
			}
		}
		if (World.getInstance().getLightingState()) {
			gc.drawImage(shadowMap, -subTileShiftX - tileSize, -subTileShiftY - tileSize,
					tileSize * (right - left + 1 + 2 * padding), tileSize * (bottom - top + 1 + 2 * padding));
		}
	}

	private void calculateFog(int x, int y, WorldTiles tiles) {
		for (int tileX = -1; tileX < 2; tileX++) {
			for (int tileY = -1; tileY < 2; tileY++) {
				boolean invalidTile = false;
				if (!tiles.test(x + tileX, y + tileY)) {
					// check for invalid lookups
					invalidTile = true;
				}

				if (invalidTile) {
					tiles.get(x, y).setBlockFog(13);
				} else if (tiles.get(x, y).getFogCheck() == 2) {
					continue;
				} else if (lightSources.contains(tiles.get(x + tileX, y + tileY).getTileType().getType())) {
					tiles.get(x, y).setBlockFog(0); //show block completely
					tiles.get(x, y).setFogCheck(2);
					break; //stop checking for smooth fog effect, since it will be completely visible anyway
				} else if (tiles.get(x + tileX, y + tileY).getFog() < tiles.get(x, y).getFog()) {
					//visibility scales based on "brightest" nearby block
					tiles.get(x, y).setBlockFog(tiles.get(x + tileX, y + tileY).getFog() + 13);
				}
			}
		}
		if (tiles.get(x, y).getFogCheck() == 0) {
			tiles.get(x, y).setFogCheck(1);
		}
	}

	private void renderEntities(List<Entity> entities, long ms) {
		float left = viewport.getLeft();
		float right = viewport.getRight();
		float top = viewport.getTop();
		float bottom = viewport.getBottom();
		Map<LayerList, List<Entity>> layers = new HashMap<>();
		for (LayerList layer: LayerList.values()) {
			layers.put(layer, new ArrayList<>());
		}
		for (Entity entity : entities) {
			if (entity.getX() + entity.getWidth() > left && entity.getX() < right
					&& entity.getY() + entity.getHeight() > top && entity.getY() < bottom) {
				if (entity.getLayer() == null) {
					System.exit(0);
				}
				layers.get(entity.getLayer()).add(entity);
			}
		}
		
		for (LayerList layer: LayerList.values()) {
			List<Entity> layerEntities = layers.get(layer);
			for (Entity entity: layerEntities) {
				entity.render(gc, viewport, ms);
				if (entity instanceof Player) {
					Player player = (Player) entity;
					renderHud(player);
					player.render(gc, viewport, ms);
					renderSelectedWeapon(player);
					if (player.getOnMountStatus()) {
						player.getMount().render(gc, viewport, ms);
						;
					}
					if (player.getSkillSwap()) {
						player.setUpdateHud(true);
						player.setSpellKey(player.getSpellKey2());
						player.setDrawSKill(player.getDrawSkill2());
						;
						player.setSpellKey2("");
						player.setSkillSwap(false);
						player.setDrawSKill2(null);

					}
				}
			}
		}
	}

	private void renderMap() {
		if (!MiniMap.getVisibility()) {
			return;
		}

		WorldTiles tiles = World.getInstance().getTiles();

		float tileSize = 1;
		int leftBorder = viewport.getLeftBorder();
		int topBorder = viewport.getTopBorder();

		int top = (int) Math.floor(viewport.getTop() - (Chunk.CHUNK_HEIGHT / 4));
		int left = (int) Math.floor(viewport.getLeft()) - (Chunk.CHUNK_WIDTH / 2);
		int right = (int) Math.floor(viewport.getRight()) + (Chunk.CHUNK_WIDTH / 2);
		int bottom = (int) Math.floor(viewport.getBottom() + (Chunk.CHUNK_HEIGHT / 4));

		double alpha = gc.getGlobalAlpha();
		gc.setGlobalAlpha(0.5);
		for (int x = left; x <= right; x++) {
			for (int y = top; y <= bottom; y++) {
				// We still want to iterate over "negative" tiles even if we don't render so we can center the map
				if (!tiles.test(x, y) || !tiles.getVisited(x, y)) {
					gc.setFill(Color.BLACK);
					gc.fillRect(leftBorder + x - left + MiniMap.MAP_PADDING, topBorder + y - top + MiniMap.MAP_PADDING,
							tileSize, tileSize);
					continue;
				}

				Sprite sprite = tiles.get(x, y).getSprite();
				gc.drawImage(sprite.getFrame(), 0, 0, tileSize, tileSize, leftBorder + x - left + MiniMap.MAP_PADDING,
						topBorder + y - top + MiniMap.MAP_PADDING, tileSize, tileSize);

			}
		}

		List<Entity> mapEntities = MiniMap.getMapEntities(World.getInstance().getAllEntities());

		for (Entity entity : mapEntities) {
			renderMapEntity(entity);
		}
		gc.setGlobalAlpha(alpha);


	}

	private void renderMapEntity(Entity entity) {
		if (entity == null) {
			return;
		}

		float entitySize = 5; //square pixel size
		float tileSize = viewport.getTileSideLength();
		int leftBorder = viewport.getLeftBorder();
		int topBorder = viewport.getTopBorder();

		int mapTop = (int) Math.floor(viewport.getTop() - (Chunk.CHUNK_HEIGHT / 4));
		int mapLeft = (int) Math.floor(viewport.getLeft()) - (Chunk.CHUNK_WIDTH / 2);
		int mapRight = (int) Math.ceil(viewport.getRight()) + (Chunk.CHUNK_WIDTH / 2);
		int mapBottom = (int) Math.floor(viewport.getBottom() + (Chunk.CHUNK_HEIGHT / 4));
		int mapMidX = (mapRight - mapLeft) / 2;
		int mapMidY = (mapBottom - mapTop) / 2;

		int left = (int) Math.floor(viewport.getLeft());
		int right = (int) Math.floor(viewport.getRight());
		int top = (int) Math.floor(viewport.getTop());
		int bottom = (int) Math.floor(viewport.getBottom());
		int midX = (right - left) / 2;
		int midY = (bottom - top) / 2;

		float subTileShiftX = (viewport.getLeft() - left) * tileSize;
		WorldTiles tiles = World.getInstance().getTiles();

		if (entity.getX() < mapLeft || entity.getX() > mapRight || entity.getY() < mapTop || entity.getY() > mapBottom
				|| (tiles.test((int) entity.getX(), (int) entity.getY()))

				&& !tiles.getVisited((int) entity.getX(), (int) entity.getY())) {

			return;
		}

		float x = (entity.getX() - midX - left) * tileSize + leftBorder - subTileShiftX;
		float y = (entity.getY() - midY - top) * tileSize + topBorder - subTileShiftX;

		x /= tileSize;
		y /= tileSize;
		x += mapMidX + MiniMap.MAP_PADDING;
		y += mapMidY + MiniMap.MAP_PADDING;

		double alpha = gc.getGlobalAlpha();
		gc.setGlobalAlpha(0.5);

		if (entity instanceof Player) {
			gc.setFill(Color.BLUE);
		} else if (entity instanceof BaseNPC) {
			gc.setFill(Color.RED);
		} else if (entity instanceof ItemEntity) {
			gc.setFill(Color.YELLOW);
		}
		gc.fillRect(x, y, entitySize, entitySize);

		gc.setGlobalAlpha(alpha);

	}

	/**
	 * Method used to render the experience bar, health bar and mana bar over
	 * the top over the player hud. All three of these bars will be updated when
	 * their value changes; e.g. the health bar will get smaller when health has
	 * been lost.
	 * <p>
	 * Currently the bars don't stay over top of the player HUD when the screen
	 * is stretched but this is being worked on.
	 *
	 * @param entity The player entity is parsed into the method to allow us to
	 *               get their health, mana and experience so we can update the
	 *               bars when necessary.
	 */
	private void renderPlayerBars(Entity entity, double hudx, double hudy) {
		//Values to calculate how full each bar is
		float maxExperience = ((Player) entity).getPlayerLevel() * 20;
		long currentExperience = ((Player) entity).getExperiencePoints();
		float experiencePercent = currentExperience / maxExperience;
		float healthPercent = ((Player) entity).getCurrentHealth() / (float) ((Player) entity).getMaxHealth();
		float manaPercent = ((Player) entity).getCurrentMana() / (float) ((Player) entity).getMaxMana();
		// Experience bar
		gc.setFill(new Color(0.3, 0.3, 0.3, 1));
		gc.fillRect(hudx + 51.2, hudy + 130.32, 87.04, 15);

		// Purple bit
		gc.setFill(new Color(0.5, 0, 0.9, 1));
		gc.fillRect(hudx + 51.2, hudy + 130.32, 87.04 * experiencePercent, 15);

		// Text to show experience values
		gc.setFont(new Font(12));
		int intMaxExperience = ((Player) entity).getPlayerLevel() * 20;
		gc.setFill(new Color(1, 1, 1, 1));
		gc.fillText(currentExperience + "/" + intMaxExperience, hudx + 80.64, hudy + 142.64);
		gc.fillText("Level: " + ((Player) entity).getPlayerLevel(), hudx + 78.07999, hudy + 126.8);

		//  Health bar
		gc.setFill(new Color(0.3, 0.3, 0.3, 1));
		gc.fillRect(hudx + 144.64, hudy + 51.28, 321.28, 17);

		// Green bit
		gc.setFill(new Color(0, 1, 0, 1));
		gc.fillRect(hudx + 144.64, hudy + 51.28, 321.28 * healthPercent, 17);

		// Text to show health values
		gc.setFont(new Font(14));
		gc.setFill(new Color(0, 0, 0, 1));
		gc.fillText(((Player) entity).getCurrentHealth() + "/" + ((Player) entity).getMaxHealth(), hudx + 268.8,
				hudy + 65.28);

		//  Mana bar Empty bit
		gc.setFill(new Color(0.3, 0.3, 0.3, 1));
		gc.fillRect(hudx + 144.64, hudy + 71.28, 321.28, 17);

		// Mana bar blue bit
		gc.setFill(new Color(0, 0, 1, 1));
		gc.fillRect(hudx + 144.64, hudy + 71.28, 321.28 * manaPercent, 17);
		//Text
		gc.setFill(new Color(0, 0, 0, 1));
		gc.fillText(((Player) entity).getCurrentMana() + "/" + ((Player) entity).getMaxMana(), hudx + 268.8,
				hudy + 84.96);
	}

	/**
	 * Method used to render the Weapon HUD in the bottom left corner of the
	 * screen. The name of the currently selected weapon and the weapon sprite
	 * is render over a bar.
	 * <p>
	 * The ammunition count is still to be implemented into this.
	 *
	 * @param entity the player entity to allow for the selected weapon to be
	 *               known. as well as potentially the ammo count.
	 */
	private void renderSelectedWeapon(Entity entity) {
		//Get Current Weapon
		Weapon equippedWeapon = ((Player) entity).getEquippedWeapon();
		String currentWeapon = ((Player) entity).getEquippedWeapon().getName();
		Sprite weaponSprite = ((Player) entity).getEquippedWeapon().getSprite();
		Inventory inv = ((Player) entity).getInventory();
		//ammo check
		int ammoCount;
		int shotCount;
		String shotString;
		if ((equippedWeapon.getProjectileType() == ProjectileType.GRENADE)
				|| (equippedWeapon.getProjectileType() == ProjectileType.ROCKET)) {
			ammoCount = inv.getAmount("ex_ammo");
			shotCount = ammoCount / equippedWeapon.getAmmoDeduction();
			shotString = Integer.toString(shotCount);
		} else if (equippedWeapon.getProjectileType() == ProjectileType.MELEE) {
			shotString = "Melee";
		} else {
			ammoCount = inv.getAmount("ammo");
			shotCount = ammoCount / equippedWeapon.getAmmoDeduction();
			shotString = Integer.toString(shotCount);
		}

		//draw bar
		double barx = viewport.getResWidth() * 0.02;
		double bary = viewport.getResHeight() * 0.85;
		Sprite weaponBar = new Sprite(SpriteList.CURRENT_WEAPON);
		gc.drawImage(weaponBar.getFrame(), barx, bary, 300, 86);

		// Draw text
		gc.setFont(new Font(19));
		gc.setFill(new Color(1, 1, 1, 1));
		gc.fillText(currentWeapon, barx + 87, bary + 32);
		gc.fillText(shotString, barx + 87, bary + 65);

		//Draw weapon sprite if one exists
		if (weaponSprite != null) {

			gc.drawImage(weaponSprite.getFrame(), barx + 16,
					(bary + 6) + ((65 - (65 * weaponSprite.getHeight() / weaponSprite.getWidth())) / 2), 65,
					65 * weaponSprite.getHeight() / weaponSprite.getWidth());

		} else {
			gc.fillRect(viewport.getResWidth() * 0.03, viewport.getResHeight() * 0.865, 65, 65);
		}
	}

	/**
	 * Method used to render the player HUD at the bottom of the screen. The HUD
	 * is just an image drawn onto the screen which has spots for the currently
	 * unlocked spells to be drawn over the top. These spells also have their
	 * cooldown timer over the top of them.
	 * <p>
	 * The cooldown timer is currently not implemented into the spell logic
	 * because the cooldowns have not been decided yet.
	 *
	 * @param entity The player entity is parsed in so that we can get the
	 *               currently unlocked skills as well when see when they player
	 *               casts them.
	 */
	private void renderHud(Entity entity) {
		// Draw the HUD
		double hudx = viewport.getResWidth() * 0.35;
		double hudy = viewport.getResHeight() * 0.79;
		gc.drawImage(new Sprite(SpriteList.HUD).getFrame(), hudx, hudy, 831 / 1.4, 215 / 1.4);
		// Call the bars the be rendered on top of the HUD
		renderPlayerBars(entity, hudx, hudy);
		// Variables for spell sprites and cooldowns
		gc.setFont(new Font(18));
		gc.setFill(new Color(1, 0, 0, 1));
		new Sprite(SpriteList.DEATH_BLOSSOM);
		new Sprite(SpriteList.HIGH_NOON);
		Sprite splitShot = new Sprite(SpriteList.SPLIT_SHOT);
		Sprite timeLock = new Sprite(SpriteList.TIME_LOCK);
		double qCooldown = ((Player) entity).getCooldown(0);
		double eCooldown = ((Player) entity).getCooldown(1);
		double rCooldown = ((Player) entity).getCooldown(2);
		double wCooldown = ((Player) entity).getCooldown(3);
		DecimalFormat df = new DecimalFormat("#.##");
		// Draws each spell over the specificed spot on the HUD and places the cooldown over the top
		// Q
		if (((Player) entity).getUpdateHud() && ((Player) entity).getSpellKey().equals("0")) {
			qSprite = ((Player) entity).getDrawSkill();
			drawQ = true;
			((Player) entity).setUpdateHud(false);
			((Player) entity).setSpellKey("");
			((Player) entity).setDrawSKill(null);
		}
		if (drawQ && qSprite != null) {
			gc.drawImage(qSprite.getFrame(), hudx + 169, hudy + 99, 128 / 3.1, 128 / 3.1);
		}
		if (qCooldown > 0) {
			gc.fillText("" + df.format(qCooldown), hudx + 171.52, hudy + 127.44);
		}
		// E
		if (((Player) entity).getUpdateHud() && ((Player) entity).getSpellKey().equals("1")) {
			eSprite = ((Player) entity).getDrawSkill();
			drawE = true;
			((Player) entity).setUpdateHud(false);
			((Player) entity).setSpellKey("");
			((Player) entity).setDrawSKill(null);
		}
		if (drawE && eSprite != null) {
			gc.drawImage(eSprite.getFrame(), hudx + 245, hudy + 99, 128 / 3.1, 128 / 3.1);
		}
		if (eCooldown > 0) {
			gc.fillText("" + df.format(eCooldown), hudx + 249.6, hudy + 127.44);
		}
		//R
		if (((Player) entity).getUpdateHud() && ((Player) entity).getSpellKey().equals("2")) {
			rSprite = ((Player) entity).getDrawSkill();
			drawR = true;
			((Player) entity).setUpdateHud(false);
			((Player) entity).setSpellKey("");
			((Player) entity).setDrawSKill(null);
		}
		if (drawR && rSprite != null) {
			gc.drawImage(rSprite.getFrame(), hudx + 323, hudy + 98, 128 / 3.1, 128 / 3.1);
		}
		if (rCooldown > 0) {
			gc.fillText("" + df.format(rCooldown), hudx + 327.68, hudy + 127.44);
		}

		if (((Player) entity).getUpdateHud() && ((Player) entity).getSpellKey().equalsIgnoreCase("3")) {
			fSprite = ((Player) entity).getDrawSkill();
			drawF = true;
			((Player) entity).setUpdateHud(false);
			((Player) entity).setSpellKey("");
			((Player) entity).setDrawSKill(null);
		}
		if (drawF && fSprite != null) {
			gc.drawImage(fSprite.getFrame(), hudx + 402, hudy + 98.5, 128 / 3.1, 128 / 3.1);
		}
		if (wCooldown > 0) {
			gc.fillText("" + df.format(wCooldown), hudx + 405, hudy + 127.44);
		}

		for (int i = 0; i < 4; i++) {
			if (i == 0) {
				((Player) entity).setSkillSprites(qSprite, i);
			}
			if (i == 1) {
				((Player) entity).setSkillSprites(eSprite, i);
			}
			if (i == 2) {
				((Player) entity).setSkillSprites(rSprite, i);
			}
			if (i == 3) {
				((Player) entity).setSkillSprites(fSprite, i);
			}
		}
	}
}
