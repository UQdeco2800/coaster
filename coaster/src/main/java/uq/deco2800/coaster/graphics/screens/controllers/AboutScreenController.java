/**
 * Created by lukeabel94 on 13/09/2016.
 */
package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.graphics.Window;

public class AboutScreenController {

	@FXML //fx:id="helpScreen"
	public StackPane helpScreen;

	@FXML //fx:id="backbutton"
	public Button backButton;

	@FXML //fx:id="blurb"
	public Label blurb;

	@FXML // fx:id="startContainer"
	public AnchorPane startContainer;

	@FXML
	public ScrollPane blurbScroll;

	/**
	 * Called when the back button is pressed, called the goToMainMenu() method of the Window class and returns to the
	 * main menu.
	 */
	@FXML
	void onBackButton() {
		SoundCache.play("back");
		Window.goToScreen("Help Screen", "Start Screen");
	}

	public String getBlurb () {
		return blurb.getText();
	}

	@FXML
		//Ensure that the FXML elements are all inserted correctly
	void initialize() {
		assert helpScreen != null : "fx:id=\"helpScreen\" was not injected: check your FXML file 'AboutScreen.fxml'.";
		assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'AboutScreen.fxml'.";
		assert startContainer != null : "fx:id=\"startContainer\" was not injected: check your FXML file " +
				"'AboutScreen.fxml'.";
		FXMLControllerRegister.register(AboutScreenController.class, this);
	}
}
