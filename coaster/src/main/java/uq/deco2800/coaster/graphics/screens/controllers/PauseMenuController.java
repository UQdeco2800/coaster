package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.screens.GameScreen;

public class PauseMenuController {

	@FXML
	private VBox mainContainer;

	/**
	 * Getter method to access the mainContainer when resizing the screen
	 *
	 * @return
	 */
	public VBox getMainContainer() {
		return mainContainer;
	}

	/**
	 * Toggles the PauseMenu, this will switch it off as the PauseMenu will be
	 * open
	 */
	@FXML
	protected void onResumeButtonClick() {
		SoundCache.play("back");
		Window.getEngine().togglePause();
	}

	/**
	 * Run the save method in engine on button press
	 */
	@FXML
	protected void onSaveButtonClick() {
		SoundCache.play("click");
		Window.getEngine().save(); //
	}

	/**
	 * Load the saved data on button press
	 */
	@FXML
	protected void onLoadButtonClick() {
		SoundCache.play("click");
		Window.getEngine().load();
		Window.getEngine().togglePause();
	}

	/**
	 * Open Achievements Screen
	 */
	@FXML
	protected void onAchievementsButtonClick() {
		SoundCache.play("click");
		((AchievementsController) FXMLControllerRegister.get(AchievementsController.class)).updateAllAchievementButtons(
				World.getInstance().getFirstPlayer().getPlayerStatsClass().getAchievements().getAllAchievements());
		((AchievementsController) FXMLControllerRegister.get(AchievementsController.class)).updateAllAchievementButtons(
				World.getInstance().getFirstPlayer().getPlayerStatsClass().getAchievements().getUnlocked());
		Window.toggleScreens("Pause Menu", "Achievements Screen");
	}

	/**
	 * Open the options menu.
	 */
	@FXML
	protected void onOptionsButtonClick() {
		SoundCache.play("click");
		Window.toggleScreens("Pause Menu", "Options Menu");
	}

	/**
	 * Quit the application on button press
	 */
	@FXML
	protected void onQuitButtonClick() {
		SoundCache.play("title");
//		GameScreen.resetGameStart();
		Window.getEngine().stop();
		Window.getEngine().togglePause();
		Window.getEngine().getRenderer().enableScreen("Start Screen");
	}
	
	@FXML
	protected void onTutorialButtonClick(){
		Window.getEngine().togglePause();
		Window.getEngine().toggleTutorialMenu();
	}
	/**
	 * Initial Loading.
	 */
	@FXML
	void initialize() {
		assert mainContainer != null : "fx:id=\"mainContainer\" was not injected: check your FXML file " +
				"'PauseMenu.fxml'.";
		FXMLControllerRegister.register(PauseMenuController.class, this);
	}
}
