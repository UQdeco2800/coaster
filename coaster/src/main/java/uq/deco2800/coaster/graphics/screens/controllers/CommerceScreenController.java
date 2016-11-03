package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import uq.deco2800.coaster.graphics.Window;

public class CommerceScreenController {
	private static final Logger logger = LoggerFactory
			.getLogger(CommerceScreenController.class);
	
	@FXML
	protected void onBackButtonClick() {
		logger.debug("Back Button pressed!");
		Window.getEngine().stop();
		Window.getEngine().toggleCommerceMenu();
		Window.getEngine().toggleTutorialMenu();
	}

}
