/**
 * Created by Connor on 17/09/2016.
 */
package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.singularity.clients.coaster.CoasterClient;
import uq.deco2800.singularity.common.representations.User;
import uq.deco2800.singularity.common.representations.coaster.Score;
import uq.deco2800.singularity.common.representations.coaster.ScoreType;

public class LeaderboardController {
	private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);

	@FXML // fx:id="leaderboard"
	private StackPane leaderboard;

	@FXML // fx:id="backbutton"
	public Button backButton;

	@FXML // fx:id="rank"
	public Label rank;

	@FXML // fx:id="username"
	public Label username;

	@FXML // fx:id="experience"
	public Label experience;

	@FXML // fx:id="timeSurvived"
	public Label timeSurvived;

	@FXML // fx:id="experienceGrid"
	public GridPane experienceGrid;

	@FXML // fx:id="killsGrid"
	public GridPane killsGrid;
	
	@FXML // fx:id="networthGrid"
	public GridPane networthGrid;
	
	@FXML // fx:id="timeGrid"
	public GridPane timeGrid;
	
	@FXML // fx:id="tabs"
	public TabPane tabs;
	
	@FXML // fx:id="errorPane"
	public VBox errorPane;
	
	@FXML // fx:id="kills"
	public Label kills;
	
	@FXML // fx:id="bosses"
	public Label bosses;
	
	@FXML // fx:id="networth"
	public Label networth;
	
	@FXML // fx:id="killsUsername"
	public Label killsUsername;
	
	@FXML // fx:id="killsRank"
	public Label killsRank;
	
	@FXML // fx:id="worthUsername"
	public Label worthUsername;
	
	@FXML // fx:id="worthRank"
	public Label worthRank;
	
	@FXML // fx:id="worthUsername"
	public Label timeUsername;
	
	@FXML // fx:id="worthRank"
	public Label timeRank;
	
	@FXML // fx:id="timePlayed"
	public Label timePlayed;
	
	@FXML // fx:id="container"
	public VBox container;

	@FXML // fx:id="registrationButton"
	public Button registrationButton;

	@FXML // fx:id "leaderboardContainer"
	private AnchorPane leaderboardContainer;

	private int changeCount;

	@FXML
	void registrationbuttonaction(ActionEvent event) {
		Window.goToScreen("Leaderboard", "Rego Screen");
	}

	@FXML
	void backbuttonaction(ActionEvent event) {
		Window.goToScreen("Leaderboard", "Start Screen");	}

	private List<Score> experienceScores = null;
	private List<Score> killsScores = null;
	private List<Score> networthScores = null;
	private List<Score> timeScores = null; 
		
	public AnchorPane getLeaderboardContainer() {
		return leaderboardContainer;
	}


	/**
	 * Intialise leader board data content.
	 */
	@FXML
	void initialize() {
		assert leaderboard != null : "fx:id=\"helpScreen\" was not injected: check your FXML file 'leaderboard.fxml'.";
		assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'leaderboard.fxml'.";
		assert leaderboardContainer != null: "fx:id=\"leaderboardContainer\" was not injected: check your FXML file 'leaderboard.fxml'.";
		this.changeCount = 0;

		leaderboardContainer.visibleProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// Check if view has been loaded after initial load.
				if ((newValue) && (++changeCount > 0)) {
					onScreenLoad();
				}
			}
		});

		FXMLControllerRegister.register(LeaderboardController.class, this);
	}

	/**
	 * Initilise leaderboard screen at time of set visible.
	 */
	private void onScreenLoad() {
		logger.info("Loading leaderboard view.");

		World world = World.getInstance();
		List<Player> players = world.getPlayerEntities();
		CoasterClient client = world.getClient();;
		User user = world.getUser();

		if (players.size() < 1) {
			logger.info("Player not set, load from world.");
			
			// No player generated yet, check rego for values
			if ((user == null) || (client == null)) {
				userRegistrationWarning();
				return;
			}
		} else {
			logger.info("Loading connection data from player.");
//			players.get(0); FIXME 
			// TODO Add details to player  
		}
		
		// Add the tabbed pane back and hide the error pane 
		errorPane.setVisible(false);
		errorPane.setManaged(false);
		tabs.setVisible(true);
		tabs.setManaged(true);

		// Update with initial grid if removed 
		String experienceGridId = experienceGrid.getId();
		String killsGridId = killsGrid.getId();
		String networthGridId = networthGrid.getId();
		String timeGridId = timeGrid.getId();
		
		// Remove any residual elements. 
		errorPane.getChildren().removeAll(errorPane.getChildren());

		// Get Data - Initialise with HIGH SCORE by XP
		List<Score> updatedExperienceScores = client.getHighestScores(ScoreType.EXPERIENCE);
		List<Score> updatedKillsScores = client.getHighestScores(ScoreType.KILLS);
		List<Score> updatedNetworthScores = client.getHighestScores(ScoreType.WORTH);
		List<Score> updatedTimeScores = client.getHighestScores(ScoreType.TIME);

		if (updatedExperienceScores.isEmpty() && updatedKillsScores.isEmpty() 
				&& updatedNetworthScores.isEmpty() && updatedTimeScores.isEmpty()) {
			// No scores in db
			noDataWarning();
			return; 
		}

		List<User> users = new ArrayList<User>();

		// Initialise scores list for first run
		if (experienceScores == null) {
			experienceScores = new ArrayList<Score>();
		}
		if (killsScores == null) {
			killsScores = new ArrayList<Score>();
		}
		if (networthScores == null) {
			networthScores = new ArrayList<Score>();
		}
		if (timeScores == null) {
			timeScores = new ArrayList<Score>();
		}
		
		// Check if new data exists 
		if (!updatedExperienceScores.equals(this.experienceScores)) {
			// Update experience scores 
			this.experienceScores = updatedExperienceScores;
			
			// Clear previous pane scores
			int currentIndex = 0;
			for (Node node : experienceGrid.getChildren()) {
				if (node instanceof GridPane) {
					GridPane pane = (GridPane) node;
					if (GridPane.getRowIndex(rank) != 0) {
						experienceGrid.getChildren().remove(node);
					}
				}
			}
			if (!updatePane(experienceScores, ScoreType.EXPERIENCE, users, client)) {
				// TODO Failure to update scores 
			}
			
		} else {
			logger.info("No new Experience score, pane not updated.");
		}
		
		// Update kills scores pane 
		if (!updatedKillsScores.equals(this.killsScores)) {
			// Update experience scores 
			this.killsScores = updatedKillsScores;
			
			// Clear previous pane scores
			int currentIndex = 0;
			for (Node node : killsGrid.getChildren()) {
				if (node instanceof GridPane) {
					GridPane pane = (GridPane) node;
					if (GridPane.getRowIndex(rank) != 0) {
						killsGrid.getChildren().remove(node);
					}
				}
			}
			if (!updatePane(killsScores, ScoreType.KILLS, users, client)) {
				// TODO Failure to update scores 
			}
		} else {
			logger.info("No new kill scores, pane not updated.");
		}

		// Update networth scores pane 
		if (!updatedNetworthScores.equals(this.networthScores)) {
			// Update experience scores 
			this.networthScores = updatedNetworthScores;
			
			// Clear previous pane scores
			int currentIndex = 0;
			for (Node node : networthGrid.getChildren()) {
				if (node instanceof GridPane) {
					GridPane pane = (GridPane) node;
					if (GridPane.getRowIndex(rank) != 0) {
						networthGrid.getChildren().remove(node);
					}
				}
			}
			if (!updatePane(networthScores, ScoreType.WORTH, users, client)) {
				// TODO Failure to update scores 
			}
		} else {
			logger.info("No new networth scores, pane not updated.");
		}
		
		// Update timeplayed scores 
		if (!updatedTimeScores.equals(this.timeScores)) {
			// Update experience scores 
			this.timeScores = updatedTimeScores;
			
			// Clear previous pane scores
			int currentIndex = 0;
			for (Node node : timeGrid.getChildren()) {
				if (node instanceof GridPane) {
					GridPane pane = (GridPane) node;
					if (GridPane.getRowIndex(rank) != 0) {
						timeGrid.getChildren().remove(node);
					}
				}
			}
			if (!updatePane(timeScores, ScoreType.TIME, users, client)) {
				// TODO Failure to update scores 
			}
		} else {
			logger.info("No new time scores, pane not updated.");
		}
	}
	
	/**
	 * Update the pane with score data 
	 * @param scores	
	 * 			Score data to update the pane with 
	 * @param scoreType
	 * 			Pane to update 
	 * @param users
	 *			Users data to complement score data (ie. ids in scores can be found as full user objects in this param)
	 * @param client
	 * 			Singularity client instance to use. 
	 * @return
	 */
	private boolean updatePane (List<Score> scores, ScoreType scoreType, List<User> users, CoasterClient client) {
		logger.info("Updating pane with new score data.");
		// Add the experience scores. 
		int scoreRank = 1;
		int currentRow = 0;

		for (Score score : scores) {
			String userId = score.getScoreId();
			Stream<User> result = users.stream().filter(userSearch -> Objects.equals(userSearch.getUserId(), userId));

			// Only retrieve user information if hasn't been done already.
			User scoreUser;
			if (result.count() == 0) {
				scoreUser = client.getUserInformationById(score.getUserId());
				users.add(scoreUser);
			} else {
				scoreUser = result.findFirst().get();
			}

			List<Label> labels = new ArrayList<Label>();
			labels.add(new Label(Integer.toString(scoreRank)));
			labels.add(new Label(scoreUser.getUsername().toUpperCase()));
			
			switch (scoreType) {
				case EXPERIENCE:
					labels.add(new Label(score.getExperience()));
					break;
				case KILLS:
					labels.add(new Label(score.getKills()));
					labels.add(new Label(score.getBossKills()));
					break;
				case WORTH:
					labels.add(new Label(score.getNetWorth()));
					break;
				case TIME:
					labels.add(new Label(score.getPlayTime()));
					break;
				default: 
					// Should never reach here. 
					break;
			}
			
			
			// Label layouts 
			for (Label label : labels) {
				label.setId("scoreRecord");
				label.setTextAlignment(TextAlignment.CENTER);
				label.setPadding(new Insets(0, 10, 0, 10));
			}

			switch (scoreType) {
				case EXPERIENCE:
					experienceGrid.add(labels.get(0), GridPane.getColumnIndex(rank), ++currentRow, 1, 1);
					experienceGrid.add(labels.get(1), GridPane.getColumnIndex(username), currentRow, 1, 1);
					experienceGrid.add(labels.get(2), GridPane.getColumnIndex(experience), currentRow, 1, 1);
					break;
				case KILLS:
					killsGrid.add(labels.get(0), 0, ++currentRow, 1, 1);
					killsGrid.add(labels.get(1), GridPane.getColumnIndex(killsUsername), currentRow, 1, 1);
					killsGrid.add(labels.get(2), GridPane.getColumnIndex(kills), currentRow, 1, 1);
					killsGrid.add(labels.get(3), GridPane.getColumnIndex(bosses), currentRow, 1, 1);
					break;
				case WORTH:
					networthGrid.add(labels.get(0), 0, ++currentRow, 1, 1);
					networthGrid.add(labels.get(1), GridPane.getColumnIndex(worthUsername), currentRow, 1, 1);
					networthGrid.add(labels.get(2), GridPane.getColumnIndex(networth), currentRow, 1, 1);
					break;
				case TIME:
					timeGrid.add(labels.get(0), 0, ++currentRow, 1, 1);
					timeGrid.add(labels.get(1), GridPane.getColumnIndex(timeUsername), currentRow, 1, 1);
					timeGrid.add(labels.get(2), GridPane.getColumnIndex(timePlayed),  currentRow,  1, 1);
					labels.add(new Label(score.getPlayTime()));
					break;
				default: 
					// Should never reach here. 
			}
			++scoreRank;
		}
		
		return true;
	}

	/**
	 * Change display of leader board to show user no data exists. s
	 */
	private void noDataWarning() {
		logger.info("No data for given request.");
		// Gather our new objects to make available 
		String messageId = "noData";
		Label noData = new Label("NO RECORDS EXIST.");
		noData.setId(messageId);
	
		// Hide our tabs 
		tabs.setVisible(false);
		tabs.setManaged(false);
		
		// Show our error pane 
		errorPane.setVisible(true);
		
		// Add message to our pane only if it isn't already there 
		boolean addMessage = true;
		
		// Check if it is already on the screen
		if (errorPane.getChildren().size() != 0){
			for (Node node : errorPane.getChildren()) {
				if (node.getId() == messageId) {
					addMessage = false;
				} 
			}
		}
		
		if (addMessage) {
			errorPane.getChildren().add(noData);
		}
	}

	/**
	 * Change display of learderboard to show warning and prompt for registration/login
	 */
	void userRegistrationWarning() {
		logger.info("User not registered, access forbidden.");
		
		// Gather our new objects to make available 
		String messageId = "notLoggedInMessage";
		String buttonId = "registrationButton";
		Label notLoggedIn = new Label("NOT ALLOWED.\nPLEASE LOG IN OR REGISTER AND TRY AGAIN.");
		notLoggedIn.setId(messageId);
		Button registration = new Button("Register/Login");
		registration.setId(buttonId);
		
		registration.setOnAction(new EventHandler<ActionEvent>() {
			@Override
					public void handle(ActionEvent event) {
						registrationbuttonaction(event);
					}
			});
		
		// Hide our tabs 
		tabs.setVisible(false);
		tabs.setManaged(false);
		
		// Show our error pane 
		errorPane.setVisible(true);
		
		// Add message to our pane only if it isn't already there 
		boolean addButton = true;
		boolean addMessage = true;
		
		// Check if it is already on the screen
		if (errorPane.getChildren().size() != 0){
			for (Node node : errorPane.getChildren()) {
				if (node.getId() == messageId) {
					addMessage = false;
				} 
				if (node.getId() == buttonId){
					addButton = false;
				}
			}
		}
		
		if (addMessage) {
			errorPane.getChildren().add(notLoggedIn);
		}
		if (addButton) {
			errorPane.getChildren().add(registration);
		}
	}
}
