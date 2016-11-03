package uq.deco2800.coaster;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.core.sound.SoundLoad;
import uq.deco2800.coaster.game.achievements.Achievements;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.graphics.screens.AchievementsScreen;
import uq.deco2800.coaster.graphics.screens.FXMLScreen;
import uq.deco2800.coaster.graphics.screens.OptionsMenuScreen;
import uq.deco2800.coaster.graphics.screens.PauseMenuScreen;
import uq.deco2800.coaster.graphics.screens.PopUpScreen;
import uq.deco2800.coaster.graphics.screens.MenuScreen;
import uq.deco2800.coaster.graphics.screens.controllers.FXMLControllerRegister;
import uq.deco2800.coaster.graphics.sprites.SpriteCache;

//The main class of the game, which sets up the JavaFX canvas, stage and window and creates and runs our engine.
public class TestWindow extends Application {

	public TestWindow() {
		// Put tile types in registry and load them into Tiles class.
	}

	public void start(Stage stage) {
		// Load assets
		try {
			if (!TileInfo.hasLoaded()) {
				TileInfo.clearRegistry();
				TileInfo.registerTiles();
			}
			if (!SpriteCache.hasLoaded()) {
				SpriteCache.loadAllSprites();
			}
			SoundLoad.loadSound();
			SoundCache.getInstance();
			SoundCache.mute();
			Achievements.initialize("achievements/AchievmentList.csv");
			loadFXML();
			this.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		stage.close();
		Platform.exit();
	}

	public void begin() {
		launch();
	}

	public void manualSoundLoad() {
		try {
			SoundLoad.loadSound();
			SoundCache.getInstance();
			SoundCache.mute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void loadFXML() {
		if (FXMLControllerRegister.getReferences().isEmpty()) {
			// Any non default is ommitted (and example)
			new FXMLScreen("screens/coasterRego.fxml").render(1, false);
			new FXMLScreen("screens/StartScreen.fxml").render(1, false);
			new FXMLScreen("screens/AboutScreen.fxml").render(1, false);
			new FXMLScreen("screens/DifficultyScreen.fxml").render(1, false);
			new PauseMenuScreen("screens/PauseMenu.fxml").render(1, false);
			new OptionsMenuScreen("screens/OptionsMenu.fxml").render(1, false);
			new FXMLScreen("screens/AchievementToolTipScreen.fxml").render(1, false);
			new AchievementsScreen("screens/AchievementsScreen.fxml").render(1, false);
			// new FXMLScreen("screens/leaderboard.fxml");
			new FXMLScreen("screens/bank.fxml").render(1, false);
			// new FXMLScreen("screens/inventory.fxml").render(1);
			new FXMLScreen("screens/skillTree.fxml").render(1, false);
			new FXMLScreen("screens/passiveInfoPanel.fxml").render(1, false);
			new PopUpScreen("screens/PopUp.fxml").render(1, false);
		}
	}
}
