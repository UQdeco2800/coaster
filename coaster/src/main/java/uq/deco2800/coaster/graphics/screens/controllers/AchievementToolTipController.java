package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import uq.deco2800.coaster.graphics.Window;

/**
 * Controller for the AchievementToolTip Screen
 *
 * @author Steven
 */
public class AchievementToolTipController {
	@FXML
	private HBox mainContainer;

	@FXML
	private ImageView icon;

	@FXML
	private Label name;

	@FXML
	private Label description;

	/**
	 * sets the position of the Screen
	 *
	 * @param x the current x position of the mouse
	 * @param y the current y position of the mouse
	 */
	public void setPosition(double x, double y) {
		double width = mainContainer.getWidth();
		double height = mainContainer.getHeight();

		// Determines the orientation of the tooltip
		// Top, Bottom, Left, Right of the mouse.
		if (width + x > Window.getResWidth()) {
			mainContainer.setLayoutX(x - width - 5);
		} else {
			mainContainer.setLayoutX(x + 5);
		}

		if (height + y > Window.getResHeight()) {
			mainContainer.setLayoutY(y - height - 5);
		} else {
			mainContainer.setLayoutY(y + 5);
		}
	}

	/**
	 * Sets the icon for the image
	 *
	 * @param image
	 */
	public void setIcon(Image image) {
		icon.setImage(image);
	}

	/**
	 * Sets the label text for the name
	 *
	 * @param text the String value to set
	 */
	public void setName(String text) {
		name.setText(text);
	}

	/**
	 * Sets the label text for the description
	 *
	 * @param text the String value to set
	 */
	public void setDescription(String text) {
		description.setText(text);
	}

	/**
	 * Initial loading.
	 */
	@FXML
	void initialize() {
		assert icon != null : "fx:id=\"icon\" was not injected: check your FXML file 'PauseMenu.fxml'.";
		assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'PauseMenu.fxml'.";
		assert description != null : "fx:id=\"description\" was not injected: check your FXML file 'PauseMenu.fxml'.";
		FXMLControllerRegister.register(AchievementToolTipController.class, this);
	}
}
