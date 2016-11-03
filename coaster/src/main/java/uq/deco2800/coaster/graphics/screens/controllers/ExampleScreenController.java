package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ExampleScreenController {
	Logger logger = LoggerFactory.getLogger(ExampleScreenController.class);

	@FXML
	Button btnMain;
	@FXML
	Label lblMain;

	@FXML
	protected void onButtonClick(ActionEvent e) {
		lblMain.setText("Welcome to DECO2800 Coaster!");
		logger.debug("Button pressed!");
	}
}
