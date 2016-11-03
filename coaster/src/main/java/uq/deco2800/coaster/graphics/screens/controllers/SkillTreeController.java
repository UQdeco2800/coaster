package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uq.deco2800.coaster.core.Engine;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.PlayerStats;
import uq.deco2800.coaster.game.entities.skills.*;
import uq.deco2800.coaster.game.entities.skills.Spells.Omnislash;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Renderer;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;


public class SkillTreeController {
	Logger logger = LoggerFactory.getLogger(SkillTreeController.class);

	@FXML
	Button activeLevelUp1;
	@FXML
	Button passiveLevelUp1;
	@FXML
	Button damageBoost;
	@FXML
	Button healing;
	@FXML
	Button skillLevelUp;
	@FXML
	Label deathBlossomLevel;
	@FXML
	Label omnislashLevel;
	@FXML
	Label timeLockLevel;
	@FXML
	Label splitShotLevel;
	@FXML
	Label nukeLevel;
	@FXML
	public Label skillPointLabel;
	@FXML 
	Button closeButton;
	@FXML
	ImageView skillIcon;
	@FXML
	Label skillDescription;
	@FXML
	Label dashState;
	@FXML
	Label doubleJumpState;
	@FXML 
	Label skillType;
	@FXML
	Button keybind1;
	@FXML
	Button keybind2;
	@FXML
	Button keybind3;
	@FXML
	Button keybind4;
	private int omnislashLevelCounter = 0;
	private int timeLockLevelCounter = 0;
	private int deathBlossomLevelCounter = 0;
	private int splitShotLevelCounter = 0;
	private int overChargeLevelCounter = 0;
	private int invincibleLevelCounter = 0;
	private int fieldLevelCounter = 0;
	private int nukeLevelCounter = 0;
	private int highnoonLevelCounter = 0;
	private int sniperShotLevelCounter = 0;
	private int damageBoostLevel = 0;
	private int healingLevel = 0;
	private int indexNumber = 0;
	private int deathBlossomIndex = 0;
	private int invincibleIndex = 0;
	
	private String selectedSkill = null;

	Engine engine = Window.getEngine();

	Renderer renderer = engine.getRenderer();
	
	private String rateOfFireDescription = "Player gains increased rate of fire.";
	private String dashDescription = "Player gains gains the ability to dash through the air.";
	private String doubleJumpDescription = "Jumpy McJumpFace. Player gains the ability to double jump.";
	private String sprintDescription = "I HAVE A NEED, A NEED FOR SPEED. Player gains the ability to sprint.";
	private String damageBoostDescription = "Player gains increased damage.";
	private String healingDescription = "Player gains the ability to passively heal over time.";
	private String deathBlossomDescription = "Fire a burst of bullets in both direction. die... die... DIE!!";
	private String omnislashDescription = "Jumps onto nearby enemies with a sword, slashing the enemy " +
			"and dealing damage at each jump.";
	private String timeLockDescription = "Freezes time and all enemies around the player, making them unable " +
			"to move or attack. With my freeze ray I will stop the world.";
	private String highNoonDescription = "It's high noon. Locks onto nearby enemies and finish them quick and clear.";
	private String splitShotDescription = "Fire a powerful bullet that splits into three bullets. ";
	private String nukeDescription = "Drops a devastating nuclear bomb killing everything in sight except you " +
			"because you are the player and you cheat. Shame on you";
	private String maxHealthDescription = "WANNA GET MORE HEALTH?! CAUSE THIS IS HOW YOU GET MORE HEALTH!!";
	private String maxManaDescription = "yer a wizard harry.";
	private String critChanceDescription = "Increases your chances with dealing a critical hit.";
	private String critDamageDescription = "Increases your damage with dealing a critical hit.";
	private String overChargeDescription = "WANNA GO FAST?!";
	private String invincibleDescription = "Invincible. Impossible. Inconceivable. Invincible to all damage, " +
			"enemies get so scared they die upon walking into you.";
	private String fieldDescription = "Gain the ability to create a death field around you.";
	private String sniperShotDescription = "360 no scope.";
	Image deathBlossomIcon = new Image("/sprites/skills/deathBlossom128.png"); 
	Image nukeIcon = new Image("/sprites/skills/nuke128.png");
	Image timeLockIcon = new Image("/sprites/skills/timeLock128.png");
	Image omnislashIcon = new Image("/sprites/skills/omnislash128.png");
	Image highNoonIcon = new Image("/sprites/skills/highNoon128.png");
	Image doubleJumpIcon = new Image("/sprites/skills/doubleJump128.png");
	Image dashIcon = new Image("/sprites/skills/dash128.png");
	Image healIcon = new Image("/sprites/skills/heal128.png");
	Image healthIcon = new Image("/sprites/skills/health128.png");
	Image splitShotIcon = new Image("/sprites/skills/splitShot128.png");
	Image fireRateIcon = new Image("/sprites/skills/fireRate128.png");
	Image damageBoostIcon = new Image ("/sprites/skills/damageBoost128.png");
	Image critChanceIcon = new Image ("/sprites/skills/splitShot128.png");
	Image critDamageIcon = new Image ("/sprites/skills/headshot128.png");
	Image overChargeIcon = new Image("/sprites/skills/dash128.png");
	Image invincibleIcon = new Image("/sprites/skills/heal128.png");
	Image fieldIcon = new Image("/sprites/skills/shield128.png");
	Image sniperShotIcon = new Image("/sprites/skills/headshot128.png");
	
	@FXML
	protected void skillLevelUp(ActionEvent e) {
		SoundCache.play("PowerUp");
		Player player = World.getInstance().getFirstPlayer();
		PlayerStats stats = player.getPlayerStatsClass();
		List<Passive> allSkills = player.getActivateSkillClass().getAllAvailableSkills();
	    int playerSkillPoints;
	    playerSkillPoints = player.getPlayerStatsClass().getSkillPoints();
		if (selectedSkill== null){
			return;
		} else if (selectedSkill == "deathBlossom") {
				if (deathBlossomLevelCounter == 0) {
					if (playerSkillPoints < 20) {
				        String infoString = "Death Blossom will cost you 20 skill points to unlock.";
				        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
					} else {
						player.getPlayerStatsClass().addSkillPoints(-20);
						skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
						deathBlossomLevelCounter++;
						deathBlossomLevel.setText(Integer.toString(deathBlossomLevelCounter));
						player.setUpdateHud(true);
						Sprite deathBlossom = new Sprite(SpriteList.DEATH_BLOSSOM);
						player.setDrawSKill(deathBlossom);
						if (indexNumber < 4){
							player.setSpellKey(""+indexNumber);
							player.addSpell(indexNumber, new Active(new Spells.DeathBlossom(player, indexNumber,
									deathBlossomLevelCounter), "Death Blossom", deathBlossomDescription
									, SkillClass.ATTACK, 0, 20, 70, 6));
							deathBlossomIndex = indexNumber;
							indexNumber ++;
						} else {
							player.setSpellKey("0");
							player.addSpell(0, new Active(new Spells.DeathBlossom(player, 0, deathBlossomLevelCounter),
									"Death Blossom",
									deathBlossomDescription
									, SkillClass.ATTACK, 0, 10, 70, 6));
							deathBlossomIndex = 0;
							logger.debug("death bloosom lvl 1");
						}
					}
				} else if (deathBlossomLevelCounter == 1) {
					if (playerSkillPoints < 30) {
				        String infoString = "Death Blossom LVL 2 will cost you 30 skill points to unlock.";
				        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
					} else {
						player.getPlayerStatsClass().addSkillPoints(-30);
						skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
						deathBlossomLevelCounter++;
						deathBlossomLevel.setText(Integer.toString(deathBlossomLevelCounter));
						player.getActiveSkill().get(deathBlossomIndex ).getSpell().setSpellLevel(deathBlossomLevelCounter);
						logger.debug("death bloosom lvl 2");
					}
				} else if (deathBlossomLevelCounter == 2) {
					infoBox("Go kill things with it.", "That's it. 2 levels.", "ARE YOU NOT ENTERTAINED?!");
				}
		} else if (selectedSkill == "nuke") {
			if (playerSkillPoints < 20) { 
				String infoString = "Nuke will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				nukeLevelCounter++;
				nukeLevel.setText(Integer.toString(nukeLevelCounter));
				if (nukeLevelCounter < 2){
					player.setUpdateHud(true);
					Sprite nuke = new Sprite(SpriteList.NUKE);
					player.setDrawSKill(nuke);
					if (indexNumber < 4){
						player.setSpellKey(""+indexNumber);
						player.addSpell(indexNumber, new Active(new Spells.Nuke(player, indexNumber), "Nuke",
								nukeDescription, SkillClass.ATTACK, 0, 10, 70, 6));
						indexNumber ++;
					}
					else {
						player.setSpellKey("0");
						player.addSpell(0, new Active(new Spells.Nuke(player, 0), "Nuke",
								nukeDescription
								, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				}
				logger.debug("nuke up");
			}
		} else if (selectedSkill == "timeLock") {
			if (playerSkillPoints < 20) { 
				String infoString = "Time Lock will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				timeLockLevelCounter++;
				timeLockLevel.setText(Integer.toString(timeLockLevelCounter));
				if (timeLockLevelCounter < 2){
					player.setUpdateHud(true);
					Sprite timeLock = new Sprite(SpriteList.TIME_LOCK);
					player.setDrawSKill(timeLock);
					if (indexNumber < 4){
						player.setSpellKey(""+indexNumber);
						player.addSpell(indexNumber, new Active(new Spells.TimeLock(player, indexNumber), "Time Lock",
								timeLockDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
						indexNumber ++;
					}
					else {
						player.setSpellKey("0");
						player.addSpell(0, new Active(new Spells.TimeLock(player, 0), "Time Lock",
								timeLockDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
					}
				}
				logger.debug("timeLockUp");
			}
		} else if (selectedSkill == "omnislash") {
			if (playerSkillPoints < 20) { 
				String infoString = "Omnislash will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				omnislashLevelCounter++;
				omnislashLevel.setText(Integer.toString(omnislashLevelCounter));
				if (omnislashLevelCounter < 2){
					player.setUpdateHud(true);
					Sprite omnislash = new Sprite(SpriteList.OMNISLASH);
					player.setDrawSKill(omnislash);
					if (indexNumber < 4){
						player.setSpellKey(""+indexNumber);
						player.addSpell(indexNumber, new Active(new Omnislash(player, omnislashLevelCounter, indexNumber),
								"Omnislash", omnislashDescription, SkillClass.ATTACK, 0, 10, 70, 6));
						indexNumber ++;
					}
					else {
						player.setSpellKey("0");
						player.addSpell(0, new Active(new Spells.Omnislash(player, omnislashLevelCounter, 0), "Omnislash",
								omnislashDescription, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				}
				logger.debug("omnislash");	
			}
		}  else if (selectedSkill == "highNoon") {
			if (playerSkillPoints < 20) { 
				String infoString = "High Noon will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				highnoonLevelCounter++;
				// Don't know which label is for high noon 
				//variable.setText(Integer.toString(highnoonLevelCounter));
				if (highnoonLevelCounter < 2){
					player.setUpdateHud(true);
					Sprite highnoon = new Sprite(SpriteList.HIGH_NOON);
					player.setDrawSKill(highnoon);
					if (indexNumber < 4){
						player.setSpellKey(""+indexNumber);
						player.addSpell(indexNumber, new Active(new Spells.HighNoon(player, indexNumber), "High Noon",
								highNoonDescription, SkillClass.ATTACK, 0, 10, 70, 6));
						indexNumber ++;
					}
					else {
						player.setSpellKey("0");
						player.addSpell(0, new Active(new Spells.HighNoon(player, 0), "High Noon",
								highNoonDescription, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				}
				logger.debug("high noon up");
			}
		} else if (selectedSkill == "splitShot") {
			if (playerSkillPoints < 20) { 
				String infoString = "Split Shot will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				splitShotLevelCounter++;
				splitShotLevel.setText(Integer.toString(splitShotLevelCounter));
				if (splitShotLevelCounter < 2){
					player.setUpdateHud(true);
					Sprite splitshot = new Sprite(SpriteList.SPLIT_SHOT);
					player.setDrawSKill(splitshot);
					if (indexNumber < 4){
						player.setSpellKey(""+indexNumber);
						player.addSpell(indexNumber, new Active(new Spells.SplitShot(player, indexNumber), "Split Shot",
								splitShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
						indexNumber ++;
					}
					else {
						player.setSpellKey("0");
						player.addSpell(0, new Active(new Spells.SplitShot(player, 0), "Split Shot",
								splitShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				}
				logger.debug("SplitShotUP");
			}
		} else if (selectedSkill == "overCharge") {
			if (playerSkillPoints < 20) { 
				String infoString = "Overcharge will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				overChargeLevelCounter++;
				if (overChargeLevelCounter < 2) {
					player.setUpdateHud(true);
					Sprite overCharge = new Sprite(SpriteList.KNIGHT_SPRINT);
					player.setDrawSKill(overCharge);
					if (indexNumber < 4){
						player.setSpellKey(""+indexNumber);
						player.addSpell(indexNumber, new Active(new Spells.Overcharge(player, indexNumber),
								"Overcharge", overChargeDescription, SkillClass.MOVEMENT, 0, 10, 70, 6));
						indexNumber ++;
					}
					else {
						player.setSpellKey("0");
						player.addSpell(indexNumber, new Active(new Spells.Overcharge(player, indexNumber),
								"Overcharge", overChargeDescription, SkillClass.MOVEMENT, 0, 10, 70, 6));
					}
				}
			}
		} else if (selectedSkill == "invincible") {
            if (invincibleLevelCounter == 0) {
                if (playerSkillPoints < 20) {
                    String infoString = "Invincibility will cost you 20 skill points to unlock.";
                    infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
                } else {
                    player.getPlayerStatsClass().addSkillPoints(-20);
                    skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
                    invincibleLevelCounter++;
                    player.setUpdateHud(true);
                    Sprite invincible = new Sprite(SpriteList.KNIGHT_HEAD);
                    player.setDrawSKill(invincible);
                    if (indexNumber < 4){
                        player.setSpellKey(""+indexNumber);
                        player.addSpell(indexNumber, new Active(new Spells.Invincible(player, indexNumber, invincibleLevelCounter),
                                "Invincible", invincibleDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
                        invincibleIndex = indexNumber;
                        indexNumber ++;
                    } else {
                        player.setSpellKey("0");
                        player.addSpell(indexNumber, new Active(new Spells.Invincible(player, indexNumber, invincibleLevelCounter),
                                "Invincible", invincibleDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
                        invincibleIndex = 0;
                        logger.debug("Invincibility lvl 1");
                    }
                }
            } else if (invincibleLevelCounter == 1) {
                if (playerSkillPoints < 30) {
                    String infoString = "Invinciblility LVL 2 will cost you 30 skill points to unlock.";
                    infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
                } else {
                    player.getPlayerStatsClass().addSkillPoints(-30);
                    skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
                    invincibleLevelCounter++;
                    player.getActiveSkill().get(invincibleIndex).getSpell().setSpellLevel(invincibleLevelCounter);
                    logger.debug("Invinciblility lvl 2");
                }
            } else if (invincibleLevelCounter == 2) {
                infoBox("Have fun with it.", "That's it. 2 levels.", "MAX LEVEL REACHED");
            }
		} else if (selectedSkill == "ATField") {
			if (playerSkillPoints < 20) { 
				String infoString = "ATField will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				player.setUpdateHud(true);
				Sprite field = new Sprite(SpriteList.SHIELD);
				player.setDrawSKill(field);
				if (indexNumber < 4){
					player.setSpellKey(""+indexNumber);
					player.addSpell(indexNumber, new Active(new Spells.ATField(player, indexNumber),
							"ATField", fieldDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
					indexNumber ++;
				}
				else {
					player.setSpellKey("0");
					player.addSpell(indexNumber, new Active(new Spells.ATField(player, indexNumber),
							"ATField", fieldDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
				}
			}
		} else if (selectedSkill == "Sniper Shot") {
			if (playerSkillPoints < 20) { 
				String infoString = "Sniper Shot will cost you 20 skill points to unlock.";
		        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
			} else {
				player.setUpdateHud(true);
				Sprite sniper = new Sprite(SpriteList.CROSSHAIR);
				player.setDrawSKill(sniper);
				if (indexNumber < 4){
					player.setSpellKey(""+indexNumber);
					player.addSpell(indexNumber, new Active(new Spells.SniperShot(player, indexNumber),
							"Sniper Shot", sniperShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
					indexNumber ++;
				}
				else {
					player.setSpellKey("0");
					player.addSpell(indexNumber, new Active(new Spells.SniperShot(player, indexNumber),
							"Sniper Shot", sniperShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
				}
			}
		} else if (selectedSkill == "Damage boost") {
			damageBoostLevel++;
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Damage boost")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addAttackSkills(SkillList.DAMAGE_BOOST);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
			logger.debug("damage boost called");
		} else if (selectedSkill == "Double jump") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Double jump")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addMovementSkill(SkillList.DOUBLE_JUMP);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Firing rate boost") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Firing rate boost")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addAttackSkills(SkillList.FIRING_RATE);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Dash") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Dash")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addMovementSkill(SkillList.DASH);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Healing") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Healing")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addDefenseSkill(SkillList.HEALING);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Mana boost") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Mana boost")) {
					skillPointCost = p.getSkillPointsCost();
					System.out.println("before: " + stats.getMana());
					stats.addMaxMana(p.getModifierBonus());
					System.out.println("after: " + stats.getMana());
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addDefenseSkill(SkillList.MANA_BOOST);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Health boost") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Health boost")) {
					skillPointCost = p.getSkillPointsCost();
					stats.addMaxHealth(p.getModifierBonus());
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addDefenseSkill(SkillList.HEALTH_BOOST);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Sprint") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Sprint")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addMovementSkill(SkillList.SPRINT);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Critical hit chance boost") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Critical hit chance boost")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addAttackSkills(SkillList.CRIT_CHANCE);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		} else if (selectedSkill == "Critical hit damage boost") {
			int skillPointCost = 0;
			for (Passive p: allSkills) {
				if (p.getName().equals("Critical hit damage boost")) {
					skillPointCost = p.getSkillPointsCost();
				}
			}
			player.getPlayerStatsClass().addSkillPoints(-skillPointCost);
			player.addAttackSkills(SkillList.CRIT_DAMAGE);
			skillPointLabel.setText(Integer.toString(player.getPlayerStatsClass().getSkillPoints()));
		}
	}
	
	public boolean updateHud = false;
	
	@FXML
	protected void deathBlossomSkillClick(ActionEvent e) {
		activeSkillHandler("deathBlossom", deathBlossomIcon, deathBlossomDescription, deathBlossomLevelCounter);
	}
	
	@FXML
	protected void overChargeSkillClick(ActionEvent e) {
		activeSkillHandler("overCharge", overChargeIcon, overChargeDescription, overChargeLevelCounter);
	}

	@FXML
	protected void omnislashSkillClick(ActionEvent e) {
		activeSkillHandler("omnislash", omnislashIcon, omnislashDescription, omnislashLevelCounter);
	}

	@FXML
	protected void timeLockSkillClick(ActionEvent e) {
		activeSkillHandler("timeLock", timeLockIcon, timeLockDescription, timeLockLevelCounter);
	}

	@FXML
	protected void highNoonSkillClick(ActionEvent e) {
		activeSkillHandler("highNoon", highNoonIcon, highNoonDescription, highnoonLevelCounter);
	}
	
	@FXML
	protected void splitShotSkillClick(ActionEvent e) {
		activeSkillHandler("splitShot", splitShotIcon, splitShotDescription, splitShotLevelCounter);
	}
	
	@FXML
	protected void nukeSkillClick(ActionEvent e) {
		activeSkillHandler("nuke", nukeIcon, nukeDescription, nukeLevelCounter);
	}

	@FXML
	protected void invincibleSkillClick(ActionEvent e) {
		activeSkillHandler("invincible", invincibleIcon, invincibleDescription, invincibleLevelCounter);
	}	
	
	@FXML
	protected void fieldSkillClick(ActionEvent e) {
		activeSkillHandler("ATField", fieldIcon, fieldDescription, fieldLevelCounter);
	}	
	
	@FXML
	protected void sniperShotSkillClick(ActionEvent e) {
		activeSkillHandler("Sniper Shot", sniperShotIcon, sniperShotDescription, sniperShotLevelCounter);
	}	
	
	
	@FXML
	protected void rateOfFireSkillClick(ActionEvent e) {
		passiveSkillHandler ("Firing rate boost", "Firing rate boost is already unlocked.", 
				fireRateIcon, rateOfFireDescription);
	}

	@FXML
	protected void dashSkillClick(ActionEvent e) {
		passiveSkillHandler ("Dash", "Dash is already unlocked."
				+ " Why don't you give that SHIFT key on your keyboard a press.", 
				dashIcon, dashDescription);
	}

	@FXML
	protected void doubleJumpSkillClick(ActionEvent e) {
		passiveSkillHandler ("Double jump", "Double jump is already unlocked."
				+ "Maybe if you try pressing the SPACE BAR while you're in the air you'll realise.", 
				doubleJumpIcon, doubleJumpDescription);
	}
	
	@FXML
	protected void sprintSkillClick(ActionEvent e) {
		passiveSkillHandler ("Sprint", "Sprint is already unlocked.", 
				doubleJumpIcon, sprintDescription);
	}

	@FXML
	protected void damageBoostInfoPanel(ActionEvent e) {
		passiveSkillHandler ("Damage boost", "Damage boost is already unlocked.", 
				damageBoostIcon, damageBoostDescription);
	}
	
	@FXML
	protected void healingSkillClick(ActionEvent e) {
		passiveSkillHandler("Healing", "Passive healing is already unlocked.", healIcon, healingDescription);
	}
	
	@FXML
	protected void healthSkillClick(ActionEvent e) {
		passiveSkillHandler("Health boost", "Health boost is already unlocked.", healthIcon, maxHealthDescription);
	}
	
	@FXML
	protected void manaSkillClick(ActionEvent e) {
		passiveSkillHandler("Mana boost", "Mana boost is already unlocked.", healthIcon, maxManaDescription);
	}
	
	@FXML
	protected void 	critChanceSkillClick(ActionEvent e) {
		passiveSkillHandler("Critical hit chance boost", "Critical hit chance boost is already unlocked.", critChanceIcon, critChanceDescription);
	}
	
	@FXML
	protected void 	critDamageSkillClick(ActionEvent e) {
		passiveSkillHandler("Critical hit damage boost", "Critical hit damage boost is already unlocked.", critDamageIcon, critDamageDescription);
	}
	
	@FXML
	protected void keyBind1(ActionEvent e) {
		changeBinds(0);
	}
	
	@FXML
	protected void keyBind2(ActionEvent e) {
		changeBinds(1);
	}
	
	@FXML
	protected void keyBind3(ActionEvent e) {
		changeBinds(2);
	}
	
	@FXML
	protected void keyBind4(ActionEvent e) {
		changeBinds(3);
	}
	
	@FXML
	protected void closePanel(ActionEvent e) {
		renderer.toggleScreen("Skill Tree");
		engine.toggleInMenu();
		skillIcon.setImage(null);
		skillDescription.setText(" ");
		selectedSkill = null;
		skillType.setText(" ");
	}
	
	@FXML
	void initialize() {
		FXMLControllerRegister.register(SkillTreeController.class, this);
		engine.setSkillTreeController(this);
		keybind1.setVisible(true);
		keybind2.setVisible(true);
		keybind3.setVisible(true);
		keybind4.setVisible(true);
		keybind1.setVisible(false);
		keybind2.setVisible(false);
		keybind3.setVisible(false);
		keybind4.setVisible(false);
	}

	private void passiveSkillHandler(String skillName, String infoBoxSass, Image icon, String description) {
	    keybind1.setVisible(false);
	    keybind2.setVisible(false);
	    keybind3.setVisible(false);
	    keybind4.setVisible(false);
	    boolean activated = false;
	    Player player = World.getInstance().getFirstPlayer();
	    List<Passive> allSkills = player.getActivateSkillClass().getAllAvailableSkills();
	    int skillPointsCost = 0;
	    int playerSkillPoints;
	    playerSkillPoints = player.getPlayerStatsClass().getSkillPoints();
	    for (Passive p: allSkills) {
	        if (skillName.equals(p.getName())) {
	            skillPointsCost = p.getSkillPointsCost();
	        }
	    }
	    if (skillPointsCost == 0) {
	        logger.debug(skillName + " skill not found.");
	    }
	    if (playerSkillPoints >= skillPointsCost) {
	        selectedSkill = skillName;
	        List<Passive> skills = player.getAllPassiveSkills();
	        for (Passive p: skills) {
	            if (skillName.equals(p.getName())) {
	                activated = true;
	            }
	        }
	        if (activated) {
	            infoBox(infoBoxSass, "This skill has already been unlocked", "Maybe spend your skill pointz elsewhere hey");
	        } else {
	            logger.debug(skillName);
	            skillIcon.setImage(icon);
	            skillDescription.setText(description);
	            skillType.setText("Passive");
	        }
	    } else {
	        String infoString = skillName;
	        infoString += " will cost you ";
	        infoString += Integer.toString(skillPointsCost);
	        infoString  += " skill points to unlock.";
	        infoBox(infoString, "NOT ENOUGH SKILL POINTS", "Go kill more things!");
	    }
	}
	
	private void activeSkillHandler(String skillName, Image icon, String description, int level) {
		keybind1.setVisible(true);
		keybind2.setVisible(true);
		keybind3.setVisible(true);
		keybind4.setVisible(true);
		selectedSkill = skillName;
		skillIcon.setImage(icon);
		skillDescription.setText(description);
		skillType.setText("Active");
		logger.debug("skillName click");
	}
	
	public static void infoBox(String infoMessage, String titleBar, String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}
	
	private void changeBinds(int indexNum){
		Player player = World.getInstance().getFirstPlayer();
		boolean nextCheck = true;
		if(selectedSkill== null){
			return;
		} else if (selectedSkill == "deathBlossom") {
			if (deathBlossomLevelCounter > 0){
				try {
					for (int i = 0; i<player.getActiveSkill().size(); i++){
						if ("Death Blossom" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
							} else{
								player.setUpdateHud(true);
								Sprite deathBlossom = new Sprite(SpriteList.DEATH_BLOSSOM);
								player.setDrawSKill(deathBlossom);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNum, new Active(new Spells.DeathBlossom(player, indexNum,
										deathBlossomLevelCounter), "Death Blossom", deathBlossomDescription
										, SkillClass.ATTACK, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite deathBlossom = new Sprite(SpriteList.DEATH_BLOSSOM);
						player.setDrawSKill(deathBlossom);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNum, new Active(new Spells.DeathBlossom(player, indexNum,
								deathBlossomLevelCounter), "Death Blossom", deathBlossomDescription
								, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		} else if (selectedSkill == "nuke") {
			if (nukeLevelCounter > 0){
				try {
					for (int i = 0; i<player.getActiveSkill().size(); i++){
						if ("Nuke" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite nuke = new Sprite(SpriteList.NUKE);
								player.setDrawSKill(nuke);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNum, new Active(new Spells.Nuke(player, indexNum), "Nuke",
										nukeDescription
										, SkillClass.ATTACK, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite nuke = new Sprite(SpriteList.NUKE);
						player.setDrawSKill(nuke);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNum, new Active(new Spells.Nuke(player, indexNum), "Nuke",
								nukeDescription
								, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
				
			}
		} else if (selectedSkill == "timeLock") {
			if (timeLockLevelCounter > 0){
				try {
					for (int i = 0; i<player.getActiveSkill().size(); i++){
						if ("Time Lock" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite timeLock = new Sprite(SpriteList.TIME_LOCK);
								player.setDrawSKill(timeLock);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNum, new Active(new Spells.TimeLock(player, indexNum), "Time Lock",
										timeLockDescription, SkillClass.DEFENSE, 0, 10, 70, 6));;
								nextCheck = false;		
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite timeLock = new Sprite(SpriteList.TIME_LOCK);
						player.setDrawSKill(timeLock);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNum, new Active(new Spells.TimeLock(player, indexNum), "Time Lock",
								timeLockDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		} else if (selectedSkill == "omnislash") {
			if (omnislashLevelCounter > 0){
				try {
					for (int i = 0; i<player.getActiveSkill().size(); i++){
						if ("Omnislash" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite omnislash = new Sprite(SpriteList.OMNISLASH);
								player.setDrawSKill(omnislash);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNum, new Active(new Spells.Omnislash(player, omnislashLevelCounter,
										indexNum), "Omnislash", omnislashDescription, SkillClass.ATTACK,
										0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					}  if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite omnislash = new Sprite(SpriteList.OMNISLASH);
						player.setDrawSKill(omnislash);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNum, new Active(new Spells.Omnislash(player, omnislashLevelCounter,
								indexNum), "Omnislash", omnislashDescription,
								SkillClass.ATTACK, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		}  else if (selectedSkill == "highNoon") {
			if (highnoonLevelCounter > 0){
				try {
					for (int i = 0; i<player.getActiveSkill().size(); i++){
						if ("High Noon" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite highNoon = new Sprite(SpriteList.HIGH_NOON);
								player.setDrawSKill(highNoon);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNum, new Active(new Spells.HighNoon(player, indexNum), "High Noon",
										highNoonDescription, SkillClass.ATTACK, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite highNoon = new Sprite(SpriteList.HIGH_NOON);
						player.setDrawSKill(highNoon);
						player.setSpellKey(""+indexNum);
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		}	else if (selectedSkill == "splitShot") {
			if (splitShotLevelCounter > 0){
				try {
					for (int i = 0; i<player.getActiveSkill().size(); i++){
						if ("Split Shot" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite splitShot = new Sprite(SpriteList.SPLIT_SHOT);
								player.setDrawSKill(splitShot);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNum, new Active(new Spells.SplitShot(player, indexNum),
										"Split Shot", splitShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite splitShot = new Sprite(SpriteList.SPLIT_SHOT);
						player.setDrawSKill(splitShot);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNum, new Active(new Spells.SplitShot(player, indexNum), "Split Shot",
								splitShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		} else if (selectedSkill == "overCharge") {
			if (overChargeLevelCounter > 0){
				try {
					for (int i = 0; i < player.getActiveSkill().size(); i++) {
						if ("Overcharge" == (player.getActiveSkill().get(i).getName())) {
							if (player.getActiveSkill().get(indexNum) == null) {
								
							} else{
								player.setUpdateHud(true);
								Sprite splitShot = new Sprite(SpriteList.KNIGHT_SPRINT);
								player.setDrawSKill(splitShot);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNum, new Active(new Spells.Overcharge(player, indexNum),
										"Overcharge", overChargeDescription, SkillClass.MOVEMENT, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite overCharge = new Sprite(SpriteList.KNIGHT_SPRINT);
						player.setDrawSKill(overCharge);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNum, new Active(new Spells.Overcharge(player, indexNum),
								"Overcharge", overChargeDescription, SkillClass.MOVEMENT, 0, 10, 70, 6));
					}
				} catch (NullPointerException error) {
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		} else if (selectedSkill == "invincible") {
			if (invincibleLevelCounter > 0) {
				try {
					for (int i = 0; i < player.getActiveSkill().size(); i++){
						if ("Invincible" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite invincible = new Sprite(SpriteList.KNIGHT_HEAD);
								player.setDrawSKill(invincible);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNumber, new Active(new Spells.Invincible(player, indexNumber, invincibleLevelCounter),
										"Invincible", invincibleDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite invincible = new Sprite(SpriteList.KNIGHT_HEAD);
						player.setDrawSKill(invincible);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNumber, new Active(new Spells.Invincible(player, indexNumber, invincibleLevelCounter),
								"Invincible", invincibleDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		} else if (selectedSkill == "ATField") {
			if (fieldLevelCounter > 0){
				try {
					for (int i = 0; i < player.getActiveSkill().size(); i++){
						if ("ATField" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite field = new Sprite(SpriteList.SHIELD);
								player.setDrawSKill(field);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNumber, new Active(new Spells.ATField(player, indexNumber),
										"ATField", fieldDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite field = new Sprite(SpriteList.SHIELD);
						player.setDrawSKill(field);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNumber, new Active(new Spells.ATField(player, indexNumber),
								"ATField", fieldDescription, SkillClass.DEFENSE, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		}else if (selectedSkill == "Sniper Shot") {
			if (sniperShotLevelCounter > 0){
				try {
					for (int i = 0; i < player.getActiveSkill().size(); i++){
						if ("Sniper Shot" == (player.getActiveSkill().get(i).getName())){
							if (player.getActiveSkill().get(indexNum) == null){
								
							} else{
								player.setUpdateHud(true);
								Sprite sniperShot = new Sprite(SpriteList.CROSSHAIR);
								player.setDrawSKill(sniperShot);
								player.setSpellKey(""+indexNum);
								player.setSkillSwap(true);
								player.setDrawSKill2(player.getSkillSprites().get(indexNum));
								player.setSpellKey2(""+i);
								player.addSpell(i, player.getActiveSkill().get(indexNum));
								player.addSpell(indexNumber, new Active(new Spells.SniperShot(player, indexNumber),
										"Sniper Shot", sniperShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
								nextCheck = false;
								break;
							}
						}
					} if (player.getUpdateHud() == false && nextCheck){
						player.setUpdateHud(true);
						Sprite sniperShot = new Sprite(SpriteList.CROSSHAIR);
						player.setDrawSKill(sniperShot);
						player.setSpellKey(""+indexNum);
						player.addSpell(indexNumber, new Active(new Spells.SniperShot(player, indexNumber),
								"Sniper Shot", sniperShotDescription, SkillClass.ATTACK, 0, 10, 70, 6));
					}
				} catch (NullPointerException error){
					//This happens when the player hasn't unlocked 4 skills yet
				}
			}
		}			
	}
}

