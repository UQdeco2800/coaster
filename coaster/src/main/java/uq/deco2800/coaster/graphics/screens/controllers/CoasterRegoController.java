/**
 * 'CoasterRego.fxml' Controller Class
 */

package uq.deco2800.coaster.graphics.screens.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.singularity.clients.coaster.CoasterClient;
import uq.deco2800.singularity.common.representations.User;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import java.net.URL;
import java.util.ResourceBundle;


public class CoasterRegoController {
	private static final Logger logger = LoggerFactory.getLogger(CoasterRegoController.class);

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="regoScreen"
	private StackPane regoScreen; // Value injected by FXMLLoader

	@FXML // fx:id="userLabel"
	private Label userLabel; // Value injected by FXMLLoader

	@FXML // fx:id="userField"
	private TextField userField; // Value injected by FXMLLoader

	@FXML // fx:id="passwordLabel"
	private Label passwordLabel; // Value injected by FXMLLoader

	@FXML // fx:id="passwordField"
	private PasswordField passwordField; // Value injected by FXMLLoader

	@FXML // fx:id="loginButton"
	private Button loginButton; // Value injected by FXMLLoader

	@FXML // fx:id="backgroundImage"
	public ImageView backgroundImage;

	@FXML
	CoasterClient client;

	private String userLabelDefault = "";
	private String pLabelDefault = "";

	private int refreshCount;

	/**
	 * Attempt to login with given username and password (retrieved from username and password fields) If login failure
	 * (due to no account) attempt registration with same details. On success, initiate client and user to global
	 * context.
	 */
	@FXML
	void loginButton() {
		SoundCache.play("click");
		resetUserLabel();
		String username = userField.getText().trim();
		String password = passwordField.getText().trim();
		logger.info("USERNAME:'" + username + "'");
		logger.info("PASSWORD:'" + password + "'");

		try {
			this.client = new CoasterClient();
		} catch (ProcessingException e) {
			// Try to refresh 6 times
			if (this.refreshCount++ < 6) {
				loginButton();
			} else {
				logger.error("Failure to register CoasterClient \n" + e.getStackTrace());
			}
		}

		// Handle empty field
		if ("".equals(username)) {
			emptyUserLabel();
			return;
		}
		if ("".equals(password)) {
			emptyPasswordLabel();
			return;
		}

		login(username, password);
	}

	/**
	 * Log into singularity and store the User and Client instances into world. Failure to login, create user with given
	 * details.
	 *
	 * @param username Username to submit to singularity.
	 * @param password Password to submit to singularity.
	 */
	public void login(String username, String password) {
		try {
			// Attempt to login
			client.setupCredentials(username, password);
			User user = client.getUserInformationByUserName(username);
			World world = World.getInstance();
			world.setUser(user);
			world.setClient(client);
			Window.goToScreen("Rego Screen", "Start Screen");

		} catch (WebApplicationException e) {
			logger.debug("Setting user and pass failure", e);
			logger.info(Integer.toString(e.getResponse().getStatus()));

			// No user found, a register new user
			switch (e.getResponse().getStatus()) {
				case 404:
					logger.debug("fail");
					break;
				case 403:
					User user = new User(username, "Unknown", null, "Unknown", password);
					try {
						user = client.createUser(user);
						login(username, password);
						return;
					} catch (WebApplicationException i) {
						logger.debug("Error creating user", i);

						// Handle Web application exception create conflict (i.e. user name already exists)
						if ((i).getResponse().getStatus() == 409) {
							logger.info("Username already exists.");
							takenUserLabel();
						} else {
							// TODO Handle generic registration error. What this is shouldn't
							// matter as this will be user facing.
						}
					} catch (JsonProcessingException i) {
						logger.debug("Error processing json", i);

					} catch (Exception i) {
						logger.error("Other error logging in", i);
					}
					break;
				default:
					// TODO: Handle all other login errors. Again, these can be bundled as they will
					// have no meaning to user.
			}
		} catch (ProcessingException e) {
			// Handle Failed to connect exception
			// TODO 
		} catch (Exception e) {
			logger.error("Other error logging in", e);
		}
	}

	@FXML
	void passwordField() {
		passAction();
	}

	@FXML
	void userField() {
		userAction();
	}

	@FXML
	void backButtonAction() {
		SoundCache.play("back");
		Window.goToScreen("Rego Screen", "Start Screen");
	}

	private void userAction() {
		if (userField.getText().length() == 0) {
			emptyUserLabel();
		} else {
			resetUserLabel();
		}
	}

	private void passAction() {
		if (passwordField.getText().length() == 0) {
			emptyPasswordLabel();
		} else {
			resetPasswordLabel();
		}
	}

	public void stopImage() {
		backgroundImage.setDisable(true);
		backgroundImage.getImage().cancel();
		backgroundImage = null;
	}

	public String userLabelText() {
		return userLabel.getText();
	}

	public String pLabelText() {
		return passwordLabel.getText();
	}

	private void resetBothLabel() {
		resetUserLabel();
		resetPasswordLabel();
	}

	private void resetUserLabel() {
		userLabel.setText(userLabelDefault);
	}

	private void takenUserLabel() {
		userLabel.setText(userLabelDefault + "   USER NAME '" + userField.getText() + "' TAKEN");
	}

	private void emptyUserLabel() {
		userLabel.setText(userLabelDefault + "   USER NAME CAN NOT BE EMPTY");
	}

	private void emptyPasswordLabel() {
		passwordLabel.setText(pLabelDefault + "   PASSWORD CAN NOT BE EMPTY");
	}

	private void resetPasswordLabel() {
		passwordLabel.setText(pLabelDefault);
	}

	public void manualFieldTrigger() {
		userAction();
		passAction();
	}

	public void clear() {
		resetBothLabel();
	}

	public void dummyData() {
		userField.setText("test");
		passwordField.setText("test");
	}

	@FXML
		// This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert userLabel != null : "fx:id=\"userLabel\" was not injected: check your FXML file 'coasterRego.fxml'.";
		assert userField != null : "fx:id=\"userField\" was not injected: check your FXML file 'coasterRego.fxml'.";
		assert passwordLabel != null : "fx:id=\"passwordLabel\" was not injected: check your FXML file " +
				"'coasterRego.fxml'.";
		assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file " +
				"'coasterRego.fxml'.";
		assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'coasterRego.fxml'.";

		userLabelDefault = userLabel.getText();
		pLabelDefault = passwordLabel.getText();
		logger.debug("Attempting to register controller");
		FXMLControllerRegister.register(CoasterRegoController.class, this);

		this.refreshCount = 0;
	}
}
