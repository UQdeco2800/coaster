package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import uq.deco2800.coaster.core.input.ControlScheme;
import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.graphics.Window;

/**
 * This class controls the action events for the Controls Menu screen.
 */
public class ControlScreenController implements Initializable {
	Logger logger = LoggerFactory.getLogger(ControlScreenController.class);
	@FXML // fx:id="anchorPane"
	private AnchorPane anchorPane; // Value injected by FXMLLoader
	@FXML // fx:id="action"
	private Label action; // Value injected by FXMLLoader
	@FXML
	private Button basicAttack;
	@FXML
	private Button dash;
	@FXML
	private Button jump;
	@FXML
	private Button inventory;
	@FXML
	private Button moveDown;
	@FXML
	private Button moveUp;
	@FXML
	private Button moveLeft;
	@FXML
	private Button moveRight;
	@FXML
	private Button skillTreeMenu;
	@FXML
	private Button specialAttack;
	@FXML
	private Button slide;
	@FXML
	private Button w1;
	@FXML
	private Button w2;
	@FXML
	private Button w3;
	@FXML
	private Button w4;
	@FXML
	private Button w5;

	Boolean skipUI = false;

	public ControlScreenController() {
		this.registerInputManager();
	}

	/**
	 * Passes a reference of this controller to ControlsKeyMap
	 */
	void registerInputManager() {
		ControlsKeyMap.registerControlScreenController(this);
	}

	/**
	 * Loads the Default control scheme
	 *
	 * @param event activates on button press
	 */
	@FXML
	void buttonPress() {
		switchControlScheme(ControlScheme.DEFAULT);
	}

	/**
	 * Loads the 'Alternate_1' control scheme
	 *
	 * @param event activates on button press
	 */
	@FXML
	void loadAlternateControls() {
		switchControlScheme(ControlScheme.ALTERNATE_1);
	}

	/**
	 * Loads the specified control scheme.
	 *
	 * @param controlScheme the control scheme to load
	 */
	static void switchControlScheme(ControlScheme controlScheme) {
		InputManager.breakFromReMap();
		ControlsKeyMap.importControlScheme(controlScheme);
	}

	/**
	 * Opens file chooser and loads the file selected
	 *
	 * @param event activates on button press
	 */
	@FXML
	void loadFile() {
		cancelRemap();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			ControlsKeyMap.loadMapping(file);
		}
	}

	/**
	 * Opens file chooser and saves to the selected directory
	 *
	 * @param event activates on button press
	 */
	@FXML
	void saveFile() {
		cancelRemap();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			ControlsKeyMap.saveMapping(file);
		}
	}

	/**
	 * Exits from the controls menu
	 *
	 * @param event activates on button press
	 */
	@FXML
	void exit() {
		cancelRemap();
		Window.toggleScreens("Options Menu", "Controls Screen");
	}

	/**
	 * Displays "Press any key..." once a remap button has been clicked.
	 *
	 * @param button activates on button press
	 */
	public void displayPressAnyKey(Button button) {
		refreshPressed();
		button.setText("Press any key...");
	}

	/**
	 * Cancels any current remapping.
	 */
	void cancelRemap() {
		InputManager.breakFromReMap();
		refreshPressed();
	}

	@FXML
	void remapBasicAttack() {
		InputManager.flagToReMap(GameAction.BASIC_ATTACK);
		displayPressAnyKey(basicAttack);
	}

	@FXML
	void remapDash() {
		InputManager.flagToReMap(GameAction.DASH);
		displayPressAnyKey(dash);
	}

	@FXML
	void remapJump() {
		InputManager.flagToReMap(GameAction.JUMP);
		displayPressAnyKey(jump);
	}

	@FXML
	void remapInventory() {
		InputManager.flagToReMap(GameAction.INVENTORY);
		displayPressAnyKey(inventory);
	}

	@FXML
	void remapMoveD() {
		InputManager.flagToReMap(GameAction.MOVE_DOWN);
		displayPressAnyKey(moveDown);
	}

	@FXML
	void remapMoveU() {
		InputManager.flagToReMap(GameAction.MOVE_UP);
		displayPressAnyKey(moveUp);
	}

	@FXML
	void remapMoveL() {
		InputManager.flagToReMap(GameAction.MOVE_LEFT);
		displayPressAnyKey(moveLeft);
	}

	@FXML
	void remapMoveR() {
		InputManager.flagToReMap(GameAction.MOVE_RIGHT);
		displayPressAnyKey(moveRight);
	}

	@FXML
	void remapSkillTree() {
		InputManager.flagToReMap(GameAction.SKILL_TREE_UI);
		displayPressAnyKey(skillTreeMenu);
	}

	@FXML
	void remapSpecialAttack() {
		InputManager.flagToReMap(GameAction.SPECIAL_ATTACK);
		displayPressAnyKey(specialAttack);
	}

	@FXML
	void remapSlide() {
		InputManager.flagToReMap(GameAction.SLIDE);
		displayPressAnyKey(slide);
	}

	@FXML
	void remapW1() {
		InputManager.flagToReMap(GameAction.WEAPON_ONE);
		displayPressAnyKey(w1);
	}

	@FXML
	void remapW2() {
		InputManager.flagToReMap(GameAction.WEAPON_TWO);
		displayPressAnyKey(w2);
	}

	@FXML
	void remapW3() {
		InputManager.flagToReMap(GameAction.WEAPON_THREE);
		displayPressAnyKey(w3);
	}

	@FXML
	void remapW4() {
		InputManager.flagToReMap(GameAction.WEAPON_FOUR);
		displayPressAnyKey(w4);
	}

	@FXML
	void remapW5() {
		InputManager.flagToReMap(GameAction.WEAPON_FIVE);
		displayPressAnyKey(w5);
	}

	/**
	 * Skips UI features for testing purposes.
	 */
	public void skipUI() {
		skipUI = true;
	}

	/**
	 * Refreshes the button text.
	 */
	@FXML
	public void refreshPressed() {
		if (skipUI) {
			return;
		}
		basicAttack.setText(ControlsKeyMap.getKeyCode(GameAction.BASIC_ATTACK).toString());
		dash.setText(ControlsKeyMap.getKeyCode(GameAction.DASH).toString());
		jump.setText(ControlsKeyMap.getKeyCode(GameAction.JUMP).toString());
		inventory.setText(ControlsKeyMap.getKeyCode(GameAction.INVENTORY).toString());
		moveDown.setText(ControlsKeyMap.getKeyCode(GameAction.MOVE_DOWN).toString());
		moveUp.setText(ControlsKeyMap.getKeyCode(GameAction.MOVE_UP).toString());
		moveLeft.setText(ControlsKeyMap.getKeyCode(GameAction.MOVE_LEFT).toString());
		moveRight.setText(ControlsKeyMap.getKeyCode(GameAction.MOVE_RIGHT).toString());
		skillTreeMenu.setText(ControlsKeyMap.getKeyCode(GameAction.SKILL_TREE_UI).toString());
		specialAttack.setText(ControlsKeyMap.getKeyCode(GameAction.SPECIAL_ATTACK).toString());
		slide.setText(ControlsKeyMap.getKeyCode(GameAction.SLIDE).toString());
		w1.setText(ControlsKeyMap.getKeyCode(GameAction.WEAPON_ONE).toString());
		w2.setText(ControlsKeyMap.getKeyCode(GameAction.WEAPON_TWO).toString());
		w3.setText(ControlsKeyMap.getKeyCode(GameAction.WEAPON_THREE).toString());
		w4.setText(ControlsKeyMap.getKeyCode(GameAction.WEAPON_FOUR).toString());
		w5.setText(ControlsKeyMap.getKeyCode(GameAction.WEAPON_FIVE).toString());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		refreshPressed();
		assert basicAttack != null : "fx:id=\"basicAttack\" was not injected: check your FXML file " +
				"'controlScreen.fxml'.";
		assert specialAttack != null : "fx:id=\"specialAttack\" was not injected: check your FXML file " +
				"'controlScreen.fxml'.";
		assert dash != null : "fx:id=\"dashAttack\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert jump != null : "fx:id=\"jump\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert moveUp != null : "fx:id=\"moveUp\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert moveDown != null : "fx:id=\"moveDown\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert moveLeft != null : "fx:id=\"moveLeft\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert moveRight != null : "fx:id=\"moveRight\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert slide != null : "fx:id=\"slide\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert skillTreeMenu != null : "fx:id=\"skillTreeMenu\" was not injected: check your FXML file " +
				"'controlScreen.fxml'.";
		assert w1 != null : "fx:id=\"w1\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert w2 != null : "fx:id=\"w2\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert w3 != null : "fx:id=\"w3\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert w4 != null : "fx:id=\"w4\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert w5 != null : "fx:id=\"w5\" was not injected: check your FXML file 'controlScreen.fxml'.";
		assert inventory != null : "fx:id=\"inventory\" was not injected: check your FXML file 'controlScreen.fxml'.";
	}

}
