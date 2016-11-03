package uq.deco2800.coaster.graphics.screens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import uq.deco2800.coaster.graphics.notifications.PopUps;
import uq.deco2800.coaster.graphics.notifications.PopUps.PopUp;
import uq.deco2800.coaster.graphics.screens.controllers.FXMLControllerRegister;
import uq.deco2800.coaster.graphics.screens.controllers.PopUpController;

/**
 * Screen to display the PopUp notifications.
 * 
 * @author Steven
 */
public class PopUpScreen extends Screen {
	private static final Logger logger = LoggerFactory.getLogger(PopUpScreen.class);
	private HBox mainContainer;
	private PopUpController controller;
	private boolean isPopping = false;
	private static final int SPEED = 3;
	private static final int OFFSET = 125;
	private PopUp currentPopUp = null;

	public PopUpScreen(String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));

			Node node = loader.load();
			rootNode = node;

			// main container for resizing
			controller = (PopUpController) FXMLControllerRegister.get(PopUpController.class);
			mainContainer = controller.getMainContainer();

			// Set min values as default
			setSize(640, 80);
			rootNode.setLayoutY(-OFFSET);

			// Default this screen to be disabled
			node.setVisible(false);
			node.setDisable(true);
		} catch (IOException e) {
			logger.error("Failed to load fxml file: {}", e);
		}
	}

	/**
	 * The method to determine if the PopUp should be moving up or down
	 */
	private void show() {
		double position = rootNode.getLayoutY();

		if (position >= -OFFSET && isPopping) {
			rootNode.setLayoutY(position + SPEED);
			if (position + SPEED >= 40) {
				isPopping = false;
			}

		} else {
			rootNode.setLayoutY(position - SPEED);
			if (position - SPEED <= -OFFSET) {
				currentPopUp = null;
				rootNode.setLayoutY(-OFFSET);
			}
		}
	}

	@Override
	public void render(long ms, boolean renderBackGround) {
		// If there are no PopUps to display and
		// the current PopUp has finished displaying do nothing
		if (!PopUps.hasPopUps() && currentPopUp == null) {
			return;
		}

		// If there are PopUps to display and
		// no PopUps is currently being displayed
		// display the next PopUps
		if (PopUps.hasPopUps() && currentPopUp == null) {
			currentPopUp = PopUps.getPopUp();
			controller.setText(currentPopUp.getText());
			isPopping = true;
		}

		// If the popup has reached the peak then it will
		// maintain that position until the delay reaches 0
		// then it will start to hide.
		if (currentPopUp != null) {
			if (!isPopping && currentPopUp.getDelay() > 0) {
				currentPopUp.reduceDelay(ms);
			} else {
				show();
			}
		}
	}

	@Override
	public void setWidth(int newWidth) {
		// Resize by a factor of .5 of the main window width
		int width = (int) (newWidth * 0.5);
		if (width > mainContainer.getMinWidth()) {
			screenWidth = width;
			mainContainer.setPrefWidth(screenWidth);
		}
		centerScreenX(newWidth);
	}

	@Override
	public void setHeight(int newHeight) {
		// Don't need
	}
}
