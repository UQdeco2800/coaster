package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.mechanics.Difficulty;
import uq.deco2800.coaster.graphics.Window;


public class DifficultyController {
	Logger logger = LoggerFactory.getLogger(DifficultyController.class);

	@FXML
	public Button tutorial;
	@FXML
	public Button easy;
	@FXML
	public Button medium;
	@FXML
	public Button hard;
	@FXML
	public Button insane;
	@FXML
	public Button backButton;
	@FXML
	public Button multiplayer;
	@FXML
	public AnchorPane difficultyContainer;
	@FXML
	public Pane background;

	/**
	 * Called when the tutorial button is pressed, calls the setTutorialMode
	 * method in the Window class and initiate the game mode
	 */
	@FXML
	void onTutorialSelect() {
		SoundCache.play("click");
		Window.getEngine().setTutorialMode(true);
		Window.initGame();
	}

	/**
	 * Set the difficulty of the world to Easy and start the game
	 */
	@FXML
	protected void onEasySelect() {
		SoundCache.play("click");
		start(Difficulty.EASY);
	}

	/**
	 * Set the difficulty of the world to Medium and start the game
	 */
	@FXML
	protected void onMediumSelect() {
		SoundCache.play("click");
		start(Difficulty.MEDIUM);
	}

	/**
	 * Set the difficulty of the world to Hard and start the game
	 */
	@FXML
	protected void onHardSelect() {
		SoundCache.play("click");
		start(Difficulty.HARD);
	}

	/**
	 * Set the difficulty of the world to Insane and start the game
	 */
	@FXML
	protected void onInsaneSelect() {
		SoundCache.play("click");
		start(Difficulty.INSANE);
	}


	/**
	 * Switches the screen back to the main menu.
	 */
	@FXML
	protected void onBackButton() {
		SoundCache.play("back");
		Window.goToScreen("Difficulty", "Start Screen");
	}

	@FXML
	protected void onMultiplayerButton() {
		Window.goToScreen("Start Screen", "Multi Screen");
	}

	@FXML
	void initialize() {
		assert difficultyContainer != null : "fx:id=\"difficultyContainer\" was not injected: check your " +
				"FXML file 'DifficultyScreen.fxml'.";
		FXMLControllerRegister.register(DifficultyController.class, this);
	}

	private void start(Difficulty d) {
		logger.info(d.name() + " Difficulty Selected");
		Window.getEngine().setTutorialMode(false);
		Window.getEngine().setDifficulty(d);
		Window.initGame();
	}
}
