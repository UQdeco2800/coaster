package uq.deco2800.coaster.graphics.screens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import uq.deco2800.coaster.graphics.screens.controllers.AchievementsController;
import uq.deco2800.coaster.graphics.screens.controllers.FXMLControllerRegister;

/**
 * Screen to display the Achievements.
 *
 * @author Steven
 */
public class AchievementsScreen extends Screen {
	private static final Logger logger = LoggerFactory.getLogger(AchievementsScreen.class);

	private AchievementsController controller;
	private ScrollPane mainContainer;

	public AchievementsScreen(String fxmlPath) {
		try {
			Node node = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlPath));

			rootNode = node;

			// Maincontainer for resizing
			controller = (AchievementsController) FXMLControllerRegister.get(AchievementsController.class);
			mainContainer = controller.getMainContainer();

			// Set min values as default
			setSize(636, 600);

			// Default this screen to be disabled
			node.setVisible(false);
			node.setDisable(true);
		} catch (IOException e) {
			logger.error("Failed to load fxml file: {}", e);
		}
	}

	@Override
	public void render(long ms, boolean renderBackGround) {
		// Don't need
	}

	@Override
	public void setWidth(int newWidth) {
		int width = (int) (newWidth * 0.496875);
		if (width > mainContainer.getMinWidth()) {
			screenWidth = width;
			mainContainer.setPrefWidth(screenWidth);
			controller.getAchievementsContainer().setPrefWidth(screenWidth - 16);
		}
		centerScreenX(newWidth);
	}

	@Override
	public void setHeight(int newHeight) {
		// For this Screen we don't want to resize the height
		centerScreenY(newHeight - 30);
	}
}
