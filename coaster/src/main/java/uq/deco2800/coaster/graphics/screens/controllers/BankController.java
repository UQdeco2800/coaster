package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

import static java.lang.Math.abs;

/**
 * The Controller for the Bank Screen.
 *
 * Created by draganmarjanovic on 18/09/2016.
 */
public class BankController {

	private Player player;

	@FXML
	private Label bankGoldCount;
	@FXML
	private Label playerGoldCount;
	@FXML
	private TextField depositField;
	@FXML
	private TextField withdrawField;

	/**
	 * Initialises the Bank view controller.
	 */
	public BankController() {
		FXMLControllerRegister.register(BankController.class, this);
	}

	/**
	 * Updates the Bank view.
	 */
	public void updateView() {
		player = getPlayer();
		bankGoldCount.setText(Integer.toString(player.getCommerce().getBank().getGold()));
		playerGoldCount.setText(Integer.toString(player.getCommerce().getGold()));
	}

	/**
	 * Handles the deposit button being pressed.
	 */
	@FXML
	protected void onDepositPressed() {
		SoundCache.play("click");
		// Convert the input to an integer
		Integer depositAmount = getNumber(depositField.getText());
		// The main transaction
		depositAmount = player.getCommerce().reduceGold(depositAmount);
		player.getCommerce().getBank().addGold(depositAmount);
		// Finally, re-update the view
		updateView();
	}

	/**
	 * Handles the withdraw button being pressed.
	 */
	@FXML
	protected void onWithdrawPressed() {
		SoundCache.play("click");
		Integer withdrawalAmount = getNumber(withdrawField.getText());
		// The Main transaction
		withdrawalAmount = player.getCommerce().getBank().withdrawGold(withdrawalAmount);
		player.getCommerce().addGold(withdrawalAmount);
		// View update
		updateView();
	}

	/**
	 * Converts a string to a number if possible, if not, returns 0.
	 *
	 * @return the number if possible, 0 otherwise.
	 */
	private Integer getNumber(String string) {
		Integer returnValue = 0;
		try {
			returnValue = abs(Integer.parseInt(string));
		} catch (NumberFormatException e) {
			// Discard NumberFormatException
		}
		return returnValue;
	}

	/**
	 * Gets the player from the game.
	 *
	 * @return the player.
	 */
	private Player getPlayer() {
		return World.getInstance().getFirstPlayer();
	}

	void initialize() {
		assert bankGoldCount != null : "fx:id=\"bankGoldCount\" was not injected: check your FXML file 'bank.fxml'.";
		assert playerGoldCount != null : "fx:id=\"playerGoldCount\" was not injected: check your FXML file " +
				"'bank.fxml'.";
		assert depositField != null : "fx:id=\"depositField\" was not injected: check your FXML file 'bank.fxml'.";
		assert withdrawField != null : "fx:id=\"withdrawField\" was not injected: check your FXML file 'bank.fxml'.";
		FXMLControllerRegister.register(PauseMenuController.class, this);
	}
}
