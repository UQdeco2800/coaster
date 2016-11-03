package uq.deco2800.coaster.graphics.screens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class TutorialScreen extends Screen {
	private static Logger logger = LoggerFactory.getLogger(FXMLScreen.class);

	public TutorialScreen(String fxmlPath) {
		try {
			Node node = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlPath));
			rootNode = node;
			//Default this screen to be disabled

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
		// Irrelevant
	}

	@Override
	public void setHeight(int newHeight) {
		// Irrelevant
	}

}
