package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import uq.deco2800.coaster.game.debug.CommandDispatch;
import uq.deco2800.coaster.graphics.Window;

/**
 * The controller for the debug/developer console.
 *
 * Created by draganmarjanovic on 14/10/2016.
 */
public class ConsoleController {


	private CommandDispatch commandDispatch; // Dispatch class to process input

	@FXML
	private TextField prompt; // Get user input

	@FXML
	private TextArea screen; // Screen to display output

	/**
	 * Instantiates the Controller.
	 */
	public ConsoleController() {
		commandDispatch = new CommandDispatch();
	}

	/**
	 * Process the user input checking.
	 * If the input is a window related function the Controller handles it otherwise
	 * it is passed onto the CommandDispatch for further processing.
	 */
	@FXML
	public void onProcessCommand() {
		String command = prompt.getText().trim();
		if (!command.isEmpty()) {
			// Print what the user entered to the screen
			screen.appendText("> " + command + "\n");
			switch (command) {
				case "clear":
					screen.clear();
					break;
				default:
					// Print the output of the commandName
					screen.appendText(commandDispatch.processCommand(command) + "\n");
					break;
			}
			// Clear the prompt - ready for new input
			prompt.clear();
		}
	}

}
