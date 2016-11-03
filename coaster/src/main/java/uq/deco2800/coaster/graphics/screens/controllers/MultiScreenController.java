package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.Multiplayer;
import uq.deco2800.coaster.game.mechanics.Difficulty;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.notifications.Toaster;

import static uq.deco2800.coaster.graphics.Window.initGameMulti;

/**
 * Created by lukeabel on 20/10/2016.
 */
public class MultiScreenController {
	Logger logger = LoggerFactory.getLogger(DifficultyController.class);

	@FXML //fx:id="name"
	private TextArea name;

	@FXML //fx:id="multiScreen"
	private StackPane multiScreen;

	@FXML //fx:id="backbutton"
	public Button backButton;

	@FXML //fx:id="hostButton"
	public Button hostButton;

	@FXML //fx:id="joinButton"
	public Button joinButton;

	@FXML // fx:id="startContainer"
	private AnchorPane startContainer;

	public AnchorPane getStartContainer() {
		return startContainer;
	}

	/**
	 * Called when the host button is pressed
	 */

	@FXML
	void onHostButton() {
		if ((name.getText().length()) > 0) {
			logger.debug("Host");
			logger.debug(String.valueOf(name.getText().length()));
			Window.getEngine().setTutorialMode(false);
			initGameMulti(name.getText(), Multiplayer.getDefaultTick());
		} else {
			logger.debug("Please add Player name");
		}
	}


	/**
	 * Called when the join button is pressed
	 */
	@FXML
	void onJoinButton() {
		if ((name.getText().length()) > 0) {
			logger.debug("Join");
			Window.getEngine().setTutorialMode(false);
			boolean ok = Window.initGameMulti(name.getText());
			if (!ok) {
				Toaster.toast("Failed to join, try hosting instead");
			}
		}
	}

	/**
	 * Method that returns the text in the name text box
	 *
	 * @return the text the player input into the text box
	 */
	public String getNameText() {
		return name.getText();
	}

	/**
	 * Called when the back button is pressed, called the goToMainMenu() method of the Window class and returns to the
	 * main menu.
	 */
	@FXML
	void backButtonAction() {
		Window.goToScreen("Multi Screen", "Start Screen");
	}

	@FXML
		//Ensure that the FXML elements are all inserted correctly
	void initialize() {
		assert multiScreen != null : "fx:id=\"multiScreen\" was not injected: check your FXML file 'MultiScreen.fxml'.";
		assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'MultiScreen.fxml'.";
		assert hostButton != null : "fx:id=\"hostButton\" was not injected: check your FXML file 'MultiScreen.fxml'.";
		assert joinButton != null : "fx:id=\"joinButton\" was not injected: check your FXML file 'MultiScreen.fxml'.";
		assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'MultiScreen.fxml'.";
		FXMLControllerRegister.register(MultiScreenController.class, this);

	}

	private void start(Difficulty d) {
		logger.debug(d.name() + " Difficulty Selected");
		Window.getEngine().setDifficulty(d);
		Window.initGame();
	}
}
