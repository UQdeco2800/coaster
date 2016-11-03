/*
 * 'StartScreen.fxml' Controller Class
 */

package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.graphics.Window;

public class StartScreenController {

	@FXML // fx:id="startScreenPane"
	private StackPane startScreenPane; // Value injected by FXMLLoader

	@FXML // fx:id="startButton"
	public Button startButton; // Value injected by FXMLLoader

	@FXML // fx:id="loadButton"
	public Button loadButton; // Value injected by FXMLLoader

	@FXML // fx:id="helpButton"
	private Button aboutButton; // Value injected by FXMLLoader

	@FXML // fx:id="quitButton"
	public Button quitButton; // Value injected by FXMLLoader

	@FXML // fx:id="achievementsButton"
	public Button achievementsButton; // Value injected by FXMLLoader

	@FXML // fx:id="leaderboardButton"
	public Button leaderboardButton; // Value injected by FXMLLoader

	@FXML // fx:id="optionsButton"
	public Button optionsButton;

	private boolean saveExists = true;
	private String startScreenID = "Start Screen";

	@FXML
	private AnchorPane startContainer;

	private static final Logger logger = LoggerFactory.getLogger(StartScreenController.class);


	/**
	 * Called when the about button is clicked, calls the goToHelpScreen method
	 * of the Window class and displays the about screen
	 */
	@FXML
	void aboutButtonAction() {
		SoundCache.play("click");
		Window.goToScreen(startScreenID, "Help Screen");
	}

	/**
	 * Called when the load button is pressed, loads the game
	 */
	@FXML

	void loadButtonAction() {
		SoundCache.play("click");
		if (saveExists) {
			Window.loadFromMenu();
		}
	}

	/**
	 * Called when the quit button is pressed, closes the application
	 */
	@FXML
	void quitButtonAction() {
		Window.exit();
	}

	/**
	 * Called when the start button is pressed, calls the goToDifficulty method
	 * of the Window class and goes to the difficulty screen
	 */
	@FXML
	void startButtonAction() {
		SoundCache.play("click");
		Window.goToScreen("Start Screen", "Difficulty");
	}

	@FXML
	void achievementsButtonAction() {
		SoundCache.play("click");
		((AchievementsController) FXMLControllerRegister.get(AchievementsController.class))
				.updateAllAchievementButtons();
		Window.getEngine().getRenderer().enableScreen("Achievements Screen");
	}

	/**
	 * Called when the leaderboard button is pressed, calls the goToLeaderboard
	 * method of the Window class and goes to the leaderboard screen
	 */
	@FXML
	void leaderboardButtonAction() {
		SoundCache.play("click");
		Window.goToScreen(startScreenID, "Leaderboard");
	}

	/**
	 * Called when the controls button is pressed. Calls the toggleContolsMenu
	 * method of the Engine class and toggles the controls screen
	 */
	@FXML
	void optionsButtonAction() {
		SoundCache.play("click");
		Window.getEngine().getRenderer().enableScreen("Options Menu");
	}

	public StackPane getStackPane() {
		return startScreenPane;
	}

	public void updateLoad() {
		loadButton.getStyleClass().add("loadGrey");
		saveExists = false;
	}


	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert startScreenPane != null : "fx:id=\"startScreenPane\" was not injected: check your FXML file "
				+ "'StartScreen.fxml'.";
		assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'StartScreen.fxml'.";
		assert aboutButton != null : "fx:id=\"aboutButton\" was not injected: check your FXML file 'StartScreen.fxml'.";
		assert loadButton != null : "fx:id=\"loadButton\" was not injected: check your FXML file 'StartScreen.fxml'.";
		assert leaderboardButton != null : "fx:id=\"leaderboardButton\" was not injected: check your FXML file " +
				"'StartScreen.fxml'.";
		assert achievementsButton != null : "fx:id=\"achievementsButton\" was not injected: check your FXML " +
				"file 'StartScreen.fxml'.";
		assert optionsButton != null : "fx:id=\"optionsButton\" was not injected: check your FXML file " +
				"'StartScreen.fxml'.";
		assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'StartScreen.fxml'.";
		assert startContainer != null : "fx:id=\"startContainer\" was not injected: check your FXML file" +
				" 'StartScreen.fxml'.";
		FXMLControllerRegister.register(StartScreenController.class, this);
	}
}
