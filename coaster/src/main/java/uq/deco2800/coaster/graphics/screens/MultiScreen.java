package uq.deco2800.coaster.graphics.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.graphics.screens.controllers.FXMLControllerRegister;
import uq.deco2800.coaster.graphics.screens.controllers.MultiScreenController;

import java.io.IOException;


public class MultiScreen extends Screen {
	private static Logger logger = LoggerFactory.getLogger(MultiScreen.class);
	private AnchorPane startContainer;

	public MultiScreen(String fxmlPath) {
		try {
			Node node = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlPath));
			rootNode = node;

			//Default this screen to be disabled
			startContainer = ((MultiScreenController) FXMLControllerRegister
					.get(MultiScreenController.class)).getStartContainer();
			node.setVisible(false);
			node.setDisable(true);
		} catch (IOException e) {
			logger.error("Failed to load fxml file: {}", e);
		}
	}

	@Override
	public void render(long ms, boolean ok) {

	}

	@Override
	public void setWidth(int newWidth) {
		screenWidth = newWidth;
		startContainer.setPrefWidth(screenWidth);
	}


	@Override
	public void setHeight(int newHeight) {
		screenHeight = newHeight;
		startContainer.setPrefHeight(screenHeight);
	}
}

