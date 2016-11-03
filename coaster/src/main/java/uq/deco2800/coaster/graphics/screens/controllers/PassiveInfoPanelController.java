package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import uq.deco2800.coaster.core.Engine;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.skills.Passive;
import uq.deco2800.coaster.game.entities.skills.SkillList;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Renderer;
import uq.deco2800.coaster.graphics.Window;

public class PassiveInfoPanelController {
	Logger logger = LoggerFactory.getLogger(SkillTreeController.class);

	@FXML
	Button closeButton;
	@FXML
	public Label skillName;
	@FXML
	public TextArea skillDescription;
	@FXML
	public ImageView skillIcon;
	@FXML
	public Button skillUp;
	@FXML
	public Label skillLevel;
	@FXML
	public Label levelLabel;

	Engine engine = Window.getEngine();

	Renderer renderer = engine.getRenderer();

	@FXML
	public void initialize() {
		engine.setPassiveController(this);
		FXMLControllerRegister.register(PassiveInfoPanelController.class, this);
	}

	@FXML
	protected void closePanel(ActionEvent e) {
		engine.togglePassiveStatus();
		renderer.toggleScreen("Passive");
	}

	@FXML
	protected void passiveSkillClick(ActionEvent e) {
		Player player = World.getInstance().getFirstPlayer();
		List<Passive> allSkills = player.getActivateSkillClass().getAllAvailableSkills();
		if (skillName.toString().contains("Dash")) {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Dash")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			levelLabel.setText("");
			skillLevel.setText("Unlocked!");
			player.addMovementSkill(SkillList.DASH);
			logger.debug("dashing");
		} else if (skillName.toString().contains("Double jump")) {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Double jump")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			levelLabel.setText("");
			skillLevel.setText("Unlocked!");
			player.addMovementSkill(SkillList.DOUBLE_JUMP);
			logger.debug("doubling");
		} else if (skillName.toString().contains("Rate of Fire")) {
			player.addAttackSkills(SkillList.FIRING_RATE);
			logger.debug("firing");
		} else if (skillName.toString().contains("Damage boost")) {
			player.addAttackSkills(SkillList.DAMAGE_BOOST);
			logger.debug("boosting damage");
		} else if (skillName.toString().contains("Healing")) {
			player.addDefenseSkill(SkillList.HEALING);
			logger.debug("healing");
		}
	}
}
