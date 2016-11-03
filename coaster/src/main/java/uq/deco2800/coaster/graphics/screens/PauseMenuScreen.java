package uq.deco2800.coaster.graphics.screens;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import uq.deco2800.coaster.graphics.screens.controllers.FXMLControllerRegister;
import uq.deco2800.coaster.graphics.screens.controllers.PauseMenuController;

/**
 * Screen to display the PauseMenu
 *
 * @author Steven
 */
public class PauseMenuScreen extends Screen {
	private static final Logger logger = LoggerFactory.getLogger(PauseMenuScreen.class);
	private VBox mainContainer;

	public PauseMenuScreen(String fxmlPath) {
		try {
			Node node = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlPath));

			rootNode = node;

			// Maincontainer for resizing
			mainContainer = ((PauseMenuController) FXMLControllerRegister.get(PauseMenuController.class))
					.getMainContainer();

			// Set min values as default
			setSize(512, 650);

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
		// Resize the PauseMenuScreen by a factor of .4 of the main window width
		int width = (int) (newWidth * 0.4);
		if (width > mainContainer.getMinWidth()) {
			screenWidth = width;
			mainContainer.setPrefWidth(screenWidth);
		}
		centerScreenX(newWidth);
	}

	@Override
	public void setHeight(int newHeight) {
		// For this Screen we don't want to resize the height
		centerScreenY(newHeight);
	}
}
