package uq.deco2800.coaster.graphics.screens.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import uq.deco2800.coaster.graphics.Window;

public class InGameTutorialScreenController {
	private static final Logger logger = LoggerFactory
			.getLogger(InGameTutorialScreenController.class);
	
	@FXML
	void onWeaponButtonClick(){
		logger.debug("Weapon Button pressed!");
		Window.getEngine().toggleTutorialMenu();
		Window.getEngine().toggleWeaponMenu();
	}
	
	@FXML
	void onPowerUpButtonClick(){
		logger.debug("Item Button pressed!");
		Window.getEngine().toggleTutorialMenu();
		Window.getEngine().toggleItemMenu();
	}
	@FXML
	void onCommerceButtonClick(){
		logger.debug("Commerce Button pressed!");
		Window.getEngine().toggleTutorialMenu();
		Window.getEngine().toggleCommerceMenu();
	}
	@FXML
	void onSkillsButtonClick(){
		logger.debug("Skills Button pressed!");
		Window.getEngine().toggleTutorialMenu();
		Window.getEngine().toggleSkillsMenu();
	}
	
	
	@FXML
	protected void onBackButtonClick() {
		logger.debug("Back Button pressed!");
		Window.getEngine().stop();
		Window.getEngine().toggleTutorialMenu();
		Window.getEngine().togglePause();
	}
	@FXML
	void initialize() {
		FXMLControllerRegister.register(InGameTutorialScreenController.class, this);
	}
}
