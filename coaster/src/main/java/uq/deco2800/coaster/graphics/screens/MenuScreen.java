package uq.deco2800.coaster.graphics.screens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class MenuScreen extends Screen {
	private static Logger logger = LoggerFactory.getLogger(MenuScreen.class);
	private AnchorPane mainPane;

	public MenuScreen(String fxmlPath) {
		try {
			Node node = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlPath));
			rootNode = node;
			mainPane = (AnchorPane) rootNode;
			node.setVisible(false);
			node.setDisable(true);
		} catch (IOException e) {
			logger.error("Failed to load fxml file: {}", e);
		}
	}

	@Override
	public void render(long ms, boolean renderBackGround) {
		// Irrelevant
	}
	@Override
	public void setWidth(int newWidth) {
		screenWidth = newWidth;
		mainPane.setPrefWidth(screenWidth);
	}

	@Override
	public void setHeight(int newHeight) {
		screenHeight = newHeight;
		mainPane.setPrefHeight(screenHeight);
	}
}

