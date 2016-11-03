package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Renderer;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.screens.GameScreen;

public class GameOverScreenController {

	public GameOverScreenController() {
	}

	@FXML
	Button restartButton;

	@FXML
	Button quitButton;

	/**
	 * Called when the restart button is pressed. It will restart the game and
	 * world and put the player straight into the game
	 */
	@FXML
	protected void restartButtonAction() {
		Renderer renderer = Window.getEngine().getRenderer();
		SoundCache.play("click");
		renderer.toggleScreen("Game Over");
		renderer.hideAllScreens();
		renderer.getScreen("Game").setVisible(true);
		renderer.getScreen("Toaster").setVisible(true);
		renderer.getScreen("PopUp").setVisible(true);
		World.getInstance().resetWorld();
//		GameScreen.resetGameStart();
		Window.getEngine().initEngine();
	}

	/**
	 * Called when the quit button is pressed, closes the application
	 */
	@FXML
	void quitButtonAction() {
		Window.exit();
	}
}
