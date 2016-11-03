package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PopUpController {
	private static final Logger logger = LoggerFactory
			.getLogger(PopUpController.class);

	@FXML
	private HBox mainContainer;

	@FXML
	private Label message;

	/**
	 * Getter method to access the mainContainer when resizing the screen
	 *
	 * @return
	 */
	public HBox getMainContainer() {
		return mainContainer;
	}

	/**
	 * Sets the text for the Label in the PopUpScreen
	 */
	public void setText(String text) {
		logger.debug("Popping with " + text);
		message.setText(text);
	}

	@FXML
	void initialize() {
		assert mainContainer != null : "fx:id=\"mainContainer\" was not injected: check your FXML file 'PopUp.fxml'.";
		assert message != null : "fx:id=\"message\" was not injected: check your FXML file 'PopUp.fxml'.";
		FXMLControllerRegister.register(PopUpController.class, this);
	}
}
