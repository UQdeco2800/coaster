package uq.deco2800.coaster.graphics;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.Engine;
import uq.deco2800.coaster.core.Settings;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.core.sound.SoundLoad;
import uq.deco2800.coaster.game.achievements.Achievements;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.screens.*;
import uq.deco2800.coaster.graphics.sprites.SpriteCache;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;


/**
 * The main class of the game, which sets up the JavaFX canvas, stage and window
 * and creates and runs our engine.
 */
public class Window extends Application {
	private static final Logger logger = LoggerFactory.getLogger(Window.class);

	private Settings settings = new Settings();
	private static final Engine engine = new Engine();
	private static int resWidth;
	private static int resHeight;
	private boolean loaded;

	public Window() {
		resWidth = settings.getResWidth();
		resHeight = settings.getResHeight();
		// Put tile types in registry and load them into Tiles class.
		TileInfo.clearRegistry();
		TileInfo.registerTiles();
	}

	public void start(Stage stage) {
		// Load assets
		try {
			SpriteCache.loadAllSprites();
		} catch (IOException e) {
			logger.error("Error loading sprites", e);
			return;
		}

		try {
			SoundLoad.loadSound();
		} catch (IOException | InvalidMidiDataException e) {
			logger.error("Error loading sounds", e);
		}

		try {
			Achievements.initialize("achievements/AchievmentList.csv");
		} catch (IOException | NullPointerException e) {
			logger.error("Failed to load Achievments.", e);
		}

		// Init game
		final Viewport viewport = new Viewport(resWidth, resHeight);

		// Init JavaFX scene
		Canvas gameScreenCanvas = new Canvas(resWidth, resHeight);
		Canvas uiCanvas = new Canvas(resWidth, resHeight);
		Canvas debugCanvas = new Canvas(resWidth, resHeight);
		Canvas toasterCanvas = new Canvas(resWidth, resHeight);

		Group root = new Group();

		Scene scene = new Scene(root, resWidth, resHeight, Color.DARKGRAY);
		gameScreenCanvas.requestFocus();

		// Init input
		InputManager inputManager = new InputManager();
		scene.setOnKeyPressed(inputManager);
		scene.setOnKeyReleased(inputManager);

		// MouseEvents
		scene.setOnMouseMoved(inputManager);
		scene.setOnMouseDragged(inputManager);
		scene.setOnMousePressed(inputManager);
		scene.setOnMouseReleased(inputManager);

		// Start
		stage.setTitle("Coaster");
		stage.setScene(scene);

		// Add Fonts
		Font.loadFont(getClass().getClassLoader().getResource("font/Pixeled.ttf").toExternalForm(), 10);

		Renderer renderer = new Renderer(stage, viewport, engine);

		engine.setGraphicsOutput(renderer);

		Screen startScreen = new MenuScreen("screens/StartScreen.fxml");
		Screen regoScreen = new FXMLScreen("screens/coasterRego.fxml");

		Screen multiScreen = new MultiScreen("screens/MultiScreen.fxml");

		Screen difficultyScreen = new MenuScreen("screens/DifficultyScreen.fxml");
		Screen helpScreen = new MenuScreen("screens/AboutScreen.fxml");
		Screen leaderboard = new MenuScreen("screens/leaderboard.fxml");

		Screen gameScreen = new GameScreen(viewport, gameScreenCanvas);

		Screen uiScreen = new UIScreen(viewport, uiCanvas);
		Screen controlsScreen = new ControlsScreen("screens/controlScreen.fxml");
		Screen gameOverScreen = new MenuScreen("screens/gameOverScreen.fxml");
		Screen debugScreen = new DebugScreen(viewport, engine, debugCanvas);
		Screen invScreen = new FXMLScreen("screens/inventory.fxml");
		Screen skillTree = new FXMLScreen("screens/skillTree.fxml");
		Screen pauseMenu = new PauseMenuScreen("screens/PauseMenu.fxml");
		Screen optionsMenu = new OptionsMenuScreen("screens/OptionsMenu.fxml");
		ToasterScreen toasterScreen = new ToasterScreen(viewport, toasterCanvas);
		Screen storeScreen = new FXMLScreen("screens/store.fxml");
		Screen bankScreen = new FXMLScreen("screens/bank.fxml");
		Screen passiveInfo = new FXMLScreen("screens/passiveInfoPanel.fxml");
		Screen popUpScreen = new PopUpScreen("screens/PopUp.fxml");
		Screen debugConsoleScreen = new FXMLScreen("screens/debugConsole.fxml");
		Screen achievementsToolTipScreen = new FXMLScreen("screens/AchievementToolTipScreen.fxml");
		Screen achievementsScreen = new AchievementsScreen("screens/AchievementsScreen.fxml");
		Screen skillScreen = new FXMLScreen("screens/skillsTutorialScreen.fxml");
		Screen commerceScreen = new FXMLScreen("screens/commerceTutorialScreen.fxml");
		Screen tutorialScreen = new FXMLScreen("screens/inGameTutorialScreen.fxml");
		Screen weaponScreen = new FXMLScreen("screens/weaponTutorialScreen.fxml");
		Screen itemScreen = new FXMLScreen("screens/itemsTutorialScreen.fxml");

		// Center screens
		pauseMenu.centerScreenX(resWidth);
		pauseMenu.centerScreenY(resHeight);
		optionsMenu.centerScreenX(resWidth);
		optionsMenu.centerScreenY(resHeight);
		controlsScreen.setSize(723, 1068);
		controlsScreen.centerScreenX(resWidth);
		controlsScreen.centerScreenY(resHeight);
		storeScreen.setSize(600, 660);
		storeScreen.centerScreenX(resWidth);
		storeScreen.centerScreenY(resHeight);
		bankScreen.setSize(600, 660);
		bankScreen.centerScreenX(resWidth);
		bankScreen.centerScreenY(resHeight);
		popUpScreen.centerScreenX(resWidth);
		achievementsScreen.centerScreenX(resWidth);
		achievementsScreen.centerScreenY(resHeight);

		/*
		 * Change all the defaults to hidden in the future to remove all this.
		 * Set all to false for simplicity
		 */
		skillScreen.setSize(600, 1068);
		skillScreen.centerScreenX(resWidth);
		skillScreen.centerScreenY(resHeight);
		commerceScreen.setSize(600, 1068);
		commerceScreen.centerScreenX(resWidth);
		commerceScreen.centerScreenY(resHeight);
		itemScreen.setSize(600, 1068);
		itemScreen.centerScreenX(resWidth);
		itemScreen.centerScreenY(resHeight);
		tutorialScreen.setSize(600, 1068);
		tutorialScreen.centerScreenX(resWidth);
		tutorialScreen.centerScreenY(resHeight);
		weaponScreen.setSize(600, 1068);
		weaponScreen.centerScreenX(resWidth);
		weaponScreen.centerScreenY(resHeight);
		startScreen.setVisible(false);
		multiScreen.setVisible(false);
		regoScreen.setVisible(false);
		gameScreen.setVisible(false);
		difficultyScreen.setVisible(false);
		helpScreen.setVisible(false);
		leaderboard.setVisible(false);
		uiScreen.setVisible(false);
		controlsScreen.setVisible(false);
		gameOverScreen.setVisible(false);
		debugScreen.setVisible(false);
		invScreen.setVisible(false);
		skillTree.setVisible(false);
		pauseMenu.setVisible(false);
		toasterScreen.setVisible(false);
		popUpScreen.setVisible(false);
		bankScreen.setVisible(false);
		storeScreen.setVisible(false);
		debugConsoleScreen.setVisible(false);
		skillScreen.setVisible(false);
		commerceScreen.setVisible(false);
		itemScreen.setVisible(false);
		tutorialScreen.setVisible(false);
		weaponScreen.setVisible(false);

		// Add all screens to renderer
		renderer.addScreen("Skills Screen", skillScreen);
		renderer.addScreen("Commerce Screen", commerceScreen);
		renderer.addScreen("Item Screen", itemScreen);
		renderer.addScreen("Weapon Screen", weaponScreen);
		renderer.addScreen("Tutorial Screen", tutorialScreen);
		renderer.addScreen("Start Screen", startScreen);
		renderer.addScreen("Rego Screen", regoScreen);
		renderer.addScreen("Difficulty", difficultyScreen);
		renderer.addScreen("Help Screen", helpScreen);
		renderer.addScreen("Leaderboard", leaderboard);
		renderer.addScreen("Multi Screen", multiScreen);
		renderer.addScreen("Game", gameScreen);
		renderer.addScreen("UI", uiScreen);
		renderer.addScreen("Controls Screen", controlsScreen);
		renderer.addScreen("Game Over", gameOverScreen);
		renderer.addScreen("Debug", debugScreen);
		renderer.addScreen("Inventory", invScreen);
		renderer.addScreen("Skill Tree", skillTree);
		renderer.addScreen("Pause Menu", pauseMenu);
		renderer.addScreen("Options Menu", optionsMenu);
		renderer.addScreen("Toaster", toasterScreen);
		renderer.addScreen("Store", storeScreen);
		renderer.addScreen("Bank", bankScreen);
		renderer.addScreen("Passive", passiveInfo);
		renderer.addScreen("PopUp", popUpScreen);
		renderer.addScreen("Debug Console", debugConsoleScreen);
		renderer.addScreen("Achievements Tool Tip Screen", achievementsToolTipScreen);
		renderer.addScreen("Achievements Screen", achievementsScreen);

		renderer.enableScreen("Start Screen");

		// Start
		stage.show();
		addSizeListeners(stage, viewport, renderer);

		SoundCache.play("title");
		loaded = true;
	}

	/*
	 * Return whether the game has been loaded
	 */
	public boolean getLoaded() {
		return loaded;
	}


	public static boolean initGameMulti(String name) {
		return initGameMulti(name, -1);
	}

	public static boolean initGameMulti(String name, int tr) {
		logger.debug("Game multi initiation started");
		boolean ok = engine.initEngineMulti(name, tr);
		if (!ok) {
			return ok;
		}
		Renderer r = engine.getRenderer();
		r.getScreen("Start Screen").setDisable(true);

		// Set the original 'defaults'
		// This is repeated code, but will not be 'repeated' later
		r.hideAllScreens();
		r.getScreen("Game").setVisible(true);
		r.getScreen("Toaster").setVisible(true);
		r.getScreen("PopUp").setVisible(true);
		SoundCache.getInstance().play("game");
		engine.start();
		return true;
	}

	/**
	 * Initiate the game, called when start button is pressed. Disables the
	 * start screen, enables the difficulty screen and starts the engine.
	 */
	public static void initGame() {
		logger.debug("Game initiation started");
		engine.initEngine();
		Renderer r = engine.getRenderer();
		r.getScreen("Start Screen").setDisable(true);

		// Set the original 'defaults'
		// This is repeated code, but will not be 'repeated' later
		r.hideAllScreens();
		r.getScreen("Game").setVisible(true);
		r.getScreen("Toaster").setVisible(true);
		r.getScreen("PopUp").setVisible(true);
		SoundCache.play("game");
		engine.start();
	}

	/**
	 * Function to control loading from the main menu
	 */
	public static void loadFromMenu() {
		initGame();
		World world = World.getInstance();
		world.gameLoop(1);

		engine.load();
	}

	/**
	 * Switch between the current screen and a specified screen
	 *
	 * @param currentScreenID StringID of the current Screen
	 * @param newScreenID     StringID of the desiredScreen
	 */
	public static void goToScreen(String currentScreenID, String newScreenID) {
		Renderer r = engine.getRenderer();
		r.disableScreen(currentScreenID);
		r.enableScreen(newScreenID);

	}

	/**
	 * Switches between screens depending on which one is currently active
	 *
	 * @param screenID      screenID of selected Screen
	 * @param otherScreenID screenID of another selected Screen
	 */
	public static void toggleScreens(String screenID, String otherScreenID) {
		if (engine.getRenderer().isActiveScreen(screenID)) {
			goToScreen(screenID, otherScreenID);
		} else {
			goToScreen(otherScreenID, screenID);
		}
	}

	/**
	 * A method that returns the engine, which is a singleton
	 *
	 * @return the engine
	 */
	public static Engine getEngine() {
		return engine;
	}


	private static void addSizeListeners(Stage stage, Viewport viewport, Renderer renderer) {

		World world = World.getInstance();
		stage.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				resWidth = newValue.intValue();
				viewport.initViewport(resWidth, resHeight);
				renderer.setWidth(resWidth);

				viewport.calculateBorders(world.getTiles().getWidth(), world.getTiles().getHeight());

				engine.getRenderer().getScreen("Store").centerScreenX(resWidth);
				engine.getRenderer().getScreen("Bank").centerScreenX(resWidth);
				engine.getRenderer().getScreen("Controls Screen").centerScreenX(resWidth);

			}
		});
		stage.heightProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number
					oldValue, Number newValue) {

				resHeight = newValue.intValue();
				viewport.initViewport(resWidth, resHeight);
				renderer.setHeight(resHeight);

				viewport.calculateBorders(world.getTiles().getWidth(), world.getTiles().getHeight());

				engine.getRenderer().getScreen("Store").centerScreenY(resHeight);
				engine.getRenderer().getScreen("Bank").centerScreenY(resHeight);
				engine.getRenderer().getScreen("Controls Screen").centerScreenY(resHeight);
			}
		});
	}

	public static int getResWidth() {
		return resWidth;
	}

	public static int getResHeight() {
		return resHeight;
	}

	public void begin() {
		launch();
	}

	public static void exit() {
		Platform.exit();
	}

	public void stop() {
		SoundCache.getInstance();
		SoundCache.closeMidi();
		SoundCache.getInstance().stopAll();
	}

}
