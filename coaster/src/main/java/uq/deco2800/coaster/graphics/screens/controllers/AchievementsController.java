package uq.deco2800.coaster.graphics.screens.controllers;

import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.achievements.Achievement;
import uq.deco2800.coaster.game.achievements.AchievementType;
import uq.deco2800.coaster.game.achievements.Achievements;
import uq.deco2800.coaster.graphics.Renderer;
import uq.deco2800.coaster.graphics.Window;

/**
 * Controller for the Achievements Screen
 *
 * @author Steven
 */
public class AchievementsController {
	private HashMap<String, AchievementIcon> iconMap = new HashMap<>();
	private AchievementToolTipController toolTipController;
	private Renderer renderer;

	@FXML
	private ScrollPane mainContainer;

	@FXML
	private VBox achievementsContainer;

	/**
	 * Custom ImageView to link with Achievement
	 *
	 * @author Steven
	 */
	private class AchievementIcon extends ImageView {
		private Image[] images;

		/**
		 * Constructs the AchievementButton
		 *
		 * @param achievement
		 */
		public AchievementIcon(Achievement achievement) {
			super();
			images = new Image[2];
			images[0] = new Image("achievements/" + achievement.getImagePath());
			images[1] = new Image("achievements/locked-" + achievement.getImagePath());

			iconMap.put(achievement.getImagePath(), this);

			this.setFitHeight(80);
			this.setFitWidth(80);
			updateAchievementGraphic(achievement.isUnlocked());

			// Displays the ToolTip for the given achievement
			// Sets each value accordingly to match what is being moused over
			this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					renderer.enableScreen("Achievements Tool Tip Screen");
					toolTipController.setIcon(getIcon());
					toolTipController.setName(achievement.getName());
					toolTipController.setDescription(achievement.getDescription());
				}
			});

			// Hides the ToolTip when mouse exits the icon.
			this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					renderer.disableScreen("Achievements Tool Tip Screen");
				}
			});

			// Sets the position for the toolTip
			this.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					toolTipController.setPosition(e.getSceneX(), e.getSceneY());
				}
			});
		}

		/**
		 * Sets the graphic of the of the button using the achievement imagePath
		 * if the unlocked index 0 image is used - !unlocked then index 1 image.
		 *
		 * @param unlocked the unlocked state of the achievement
		 */
		public void updateAchievementGraphic(boolean unlocked) {
			this.setImage(images[unlocked ? 0 : 1]);
		}

		/**
		 * Wrapper to access the current Image.
		 *
		 * @return the current Image set to this
		 */
		public Image getIcon() {
			return this.getImage();
		}
	}

	/**
	 * Load all the Achievements into the Screen
	 */
	private void loadAchievements() {
		for (AchievementType type : AchievementType.values()) {
			achievementsContainer.getChildren().add(new Label(type.toString()));
			HBox hbox = new HBox();
			for (Achievement achievement : Achievements.getGloablAchievements(type)) {
				if (hbox.getChildren().size() == 5) {
					achievementsContainer.getChildren().add(hbox);
					hbox = new HBox();
				}
				Achievement newAchievement = new Achievement(achievement);
				newAchievement.forceUnlock();
				hbox.getChildren().add(new AchievementIcon(newAchievement));
			}
			achievementsContainer.getChildren().add(hbox);
		}
		Button backButton = new Button("BACK");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				SoundCache.play("back");
				renderer.disableScreen("Achievements Screen");
				if (!renderer.isActiveScreen("Start Screen")) {
					renderer.enableScreen("Pause Menu");
				}
			}
		});
		achievementsContainer.getChildren().add(backButton);
	}

	/**
	 * Getter method to access the mainContainer when resizing the screen
	 *
	 * @returnthe mainContainer for the loaded Screen
	 */
	public ScrollPane getMainContainer() {
		return mainContainer;
	}

	/**
	 * Getter method to access the achievementsContainer when resize the screen
	 *
	 * @return the achievementsContainer for the loaded Screen
	 */
	public VBox getAchievementsContainer() {
		return achievementsContainer;
	}

	/**
	 * Updates the AchievementButton for the given Achievement
	 *
	 * @param achievement the Achievement to update
	 */
	public void updateAchievementButton(Achievement achievement) {
		iconMap.get(achievement.getImagePath()).updateAchievementGraphic(achievement.isUnlocked());
	}

	/**
	 * updateAchievement for all Achievement in achievementsList
	 *
	 * @param achievementsList list of Achievments to update
	 */
	public void updateAllAchievementButtons(List<Achievement> achievementsList) {
		for (Achievement achievement : achievementsList) {
			updateAchievementButton(achievement);
		}
	}

	/**
	 * updateAchievement for all Global Achievements
	 */
	public void updateAllAchievementButtons() {
		updateAllAchievementButtons(Achievements.getAllGlobalAchievements());
	}

	/**
	 * Initial loading.
	 */
	@FXML
	void initialize() {
		assert mainContainer != null : "fx:id=\"mainContainer\" was not injected: check your FXML file "
				+ "'AchievementsScreen.fxml'.";
		assert achievementsContainer != null : "fx:id=\"achievementContainer\" was not injected: check your "
				+ "FXML file 'AchievementsScreen.fxml'.";
		FXMLControllerRegister.register(AchievementsController.class, this);
		toolTipController = (AchievementToolTipController) FXMLControllerRegister
				.get(AchievementToolTipController.class);
		renderer = Window.getEngine().getRenderer();
		loadAchievements();
	}
}
