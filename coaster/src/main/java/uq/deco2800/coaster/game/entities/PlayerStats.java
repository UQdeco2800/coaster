package uq.deco2800.coaster.game.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.game.achievements.AchievementType;
import uq.deco2800.coaster.game.achievements.Achievements;
import uq.deco2800.coaster.game.entities.skills.ActivateSkill;
import uq.deco2800.coaster.game.entities.skills.Passive;
import uq.deco2800.coaster.game.entities.skills.StatType;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.notifications.Toaster;

public class PlayerStats {
	private static final Logger logger = LoggerFactory.getLogger(PlayerStats.class);
	private List<Passive> defenseSkills = new ArrayList<Passive>();
	private List<Passive> attackSkills = new ArrayList<Passive>();
	private List<Passive> activeAbilities; // to be changed to Active
	private Achievements achievements;

	// subclass once it's done
	// Attack
	private int baseDamage;
	private int firingRate;
	private int firingAccuracy;
	private int baseCritChance;
	private int baseCritDamage;
	private int poisonCounter;

	// Defence
	private int baseArmour;

	// misc.
	private int baseMana;
	private int baseHealth;
	private int experiencePoints;
	private int playerLevel;
	private int killCount;
	private int bossKillCount;
	private HashSet<String> bossTypesKilled;
	private int pickupCount;
	private int coinCount;
	private int skillPoints;
	private ActivateSkill skills;

	public PlayerStats() {
		this.baseDamage = 20;
		this.baseArmour = 1;
		this.baseMana = 100;
		this.baseHealth = 1000;
		this.firingRate = 300; // Duration between shots in ms
		this.firingAccuracy = 15;// num of degrees deviation of bullet (will be
		// angle +/- random(accuracy)
		this.experiencePoints = 0;
		this.baseCritChance = 0;
		this.baseCritDamage = 0;
		this.poisonCounter = 0;
		this.killCount = 0;
		this.bossKillCount = 0;
		this.bossTypesKilled = new HashSet<>();
		this.pickupCount = 0;
		this.coinCount = 0;
		this.playerLevel = 1;
		this.skillPoints = 0;

		achievements = new Achievements();
	}

	/**
	 * Attempt to unlock all achievements using current Player Stat values.
	 * Typically used when loading Player Stats
	 */
	public void unlockAll() {
		achievements.unlock(AchievementType.KILLS, killCount);
		achievements.unlock(AchievementType.LEVELS, playerLevel);
		achievements.unlock(AchievementType.PICKUPS, pickupCount);
		achievements.unlock(AchievementType.COINS, coinCount);
		for (String bossID : bossTypesKilled) {
			achievements.unlock(AchievementType.BOSS, bossID);
		}
	}

	/**
	 * This method resets the maximum mana points the player has
	 *
	 * @param additionalMana the amount of additional mana points
	 */
	public void addMaxMana(int additionalMana) {
		this.baseMana += additionalMana;
	}

	/**
	 * Get player's mana points
	 *
	 * @return player's mana points
	 */
	public int getMana() {
		return this.baseMana;
	}

	/**
	 * This method resets the maximum health points the player has
	 *
	 * @param additionalHealth the amount of additional health points
	 */
	public void addMaxHealth(int additionalHealth) {
		this.baseHealth += additionalHealth;
	}

	/**
	 * Get player's maximum health points
	 *
	 * @return player's maximum health points
	 */
	public int getMaxHealth() {
		return this.baseHealth;
	}

	/**
	 * Set player's base damage
	 *
	 * @param newDamage player's damage points
	 */
	public void setBaseDamage(int newDamage) {
		this.baseDamage = newDamage;
	}

	/**
	 * Method to calculate player's damage points including any damage modifiers
	 * given by passive skills the player activated
	 *
	 * @return the amount of damage the player can deal
	 */
	public int calculateDamage() {
		int damageModifier = 0;
		this.attackSkills = skills.getAttackSkills();
		for (Passive attackSkill : attackSkills) {
			if (attackSkill.getModifiedStat().equals(StatType.DAMAGE)) {
				damageModifier += attackSkill.getModifierBonus();
			}
		}
		return damageModifier + baseDamage;
	}

	/**
	 * Get player's base damage
	 *
	 * @return player's base damage
	 */
	public int getDamage() {
		return this.baseDamage;
	}

	/**
	 * Set player's base armour
	 *
	 * @param newArmour armour points the player has
	 */
	public void setBaseArmour(int newArmour) {
		this.baseArmour = newArmour;
	}

	/**
	 * Get player's base armour points with the addition of any armour points
	 * given by passive skills the player has unlocked
	 *
	 * @return the player's armour points
	 */
	public int getArmour() {
		defenseSkills = skills.getDefenseSkills();
		int armourModifier = 1;
		for (Passive defenseSkill : defenseSkills) {
			if (defenseSkill.getCategory().equals(StatType.ARMOUR)) {
				armourModifier += defenseSkill.getModifierBonus();
			}
		}
		return armourModifier + baseArmour;
	}

	/**
	 * Increase the player's firing speed.
	 *
	 * @param additionalFiringRate the speed of which the firing speed increases
	 *            by
	 */
	public void addFiringRate(int additionalFiringRate) {
		int firingRateBuffer = 0;
		for (Passive s : skills.getAttackSkills()) {
			if (s.getName() == "Firing rate boost") {
				s.setModifierBonus(additionalFiringRate + firingRate);
				firingRateBuffer = s.getModifierBonus();
			}
		}
		this.firingRate = firingRateBuffer;
	}

	/**
	 * Get the player's firing rate
	 *
	 * @return player's firing rate
	 */
	public long getFiringRate() {
		return this.firingRate;
	}

	public void addFiringAccuracy(int increaseAccuracy) {
		this.firingAccuracy = this.firingAccuracy - increaseAccuracy;
	}

	public int getFiringAccuracy() {
		return this.firingAccuracy;
	}

	/**
	 * This method adds player's experience points to the player's stats
	 *
	 * @param additionalExperiencePoints experience points gained by the player
	 */
	public void addExperiencePoints(int additionalExperiencePoints) {
		this.experiencePoints += additionalExperiencePoints;
	}

	/**
	 * A method to get the player's current experience points
	 *
	 * @return player's experience points
	 */
	public int getExperiencePoints() {
		return this.experiencePoints;
	}

	/**
	 * Set the player's chance of dealing critical damage when an attack is
	 * performed
	 *
	 * @param criticalHitChance The chance of the player dealing a critical hit
	 */
	public void setBaseCritChance(int criticalHitChance) {
		this.baseCritChance = criticalHitChance;
	}

	/**
	 * Get the player's chance of dealing a critical hit
	 *
	 * @return an int which represents the player's chance of dealing a critical
	 *         hit
	 */
	public int getCritChance() {
		attackSkills = skills.getAttackSkills();
		int critModifier = 0;
		for (Passive attackSkill : attackSkills) {
			if (attackSkill.getCategory().equals(StatType.CRITCHANCE)) {
				critModifier += attackSkill.getModifierBonus();
			}
		}
		return critModifier + baseCritChance;
	}

	/**
	 * Set the player's base critical damage points
	 *
	 * @param criticalHitDamage The player's damage points when a critical hit
	 *            is dealt
	 */
	public void setBaseCritDamage(int criticalHitDamage) {
		this.baseCritDamage = criticalHitDamage;
	}

	/**
	 * Get the player's base critical damage points
	 *
	 * @return the critical damage points
	 */
	public int getCritDamage() {
		attackSkills = skills.getAttackSkills();
		int critModifier = 1;
		for (Passive attackSkill : attackSkills) {
			if (attackSkill.getCategory().equals(StatType.CRITDAMAGE)) {
				critModifier *= attackSkill.getModifierBonus();
			}
		}
		return (critModifier + baseCritDamage) * baseDamage;
	}

	/**
	 * Set the player's poison damage
	 *
	 * @param poison The amount of points which the poison deals
	 */
	public void setBasePoison(int poison) {
		this.poisonCounter = poison;
	}

	/**
	 * Get the player's poison points
	 *
	 * @return the player's poison points
	 */
	public int getPoison() {
		attackSkills = skills.getAttackSkills();
		int poison = 1;
		for (Passive attackSkill : attackSkills) {
			if (attackSkill.getCategory().equals(StatType.POISON)) {
				poison *= attackSkill.getModifierBonus();
			}
		}
		return poison + poisonCounter;
	}

	/**
	 * Get a list of the skills unlocked by the player
	 *
	 * @return a list of skills
	 */
	public List<Passive> getUnlockedSkills() {
		this.activeAbilities = skills.getAllActiveSkills();
		return this.activeAbilities;
	}

	/**
	 * @return the number of pickups the Player has picked up
	 */
	public int getPickupCount() {
		return pickupCount;
	}

	/**
	 * Increments the pickups count and attemps to unlock associated
	 * Achievements.
	 */
	public void addPickUpCount() {
		pickupCount++;
		achievements.unlockWithPopUp(AchievementType.PICKUPS, pickupCount);
	}

	/**
	 * Adds to the Player's running total of accumulated coins
	 *
	 * @param coinValue the value of the Coin
	 */
	public void addCoinCount(int coinValue) {
		coinCount += coinValue;
		achievements.unlockWithPopUp(AchievementType.COINS, coinCount);
	}

	/**
	 * Add the number of mobs the player has killed
	 *
	 * @param kills The number of mobs the player has killed
	 */
	public void addKillCount(int kills) {
		this.killCount += kills;
		achievements.unlockWithPopUp(AchievementType.KILLS, killCount);
	}

	/**
	 * Get the number of mobs the player has killed
	 *
	 * @return the number of mobs killed by the player
	 */
	public int getKillCount() {
		return killCount;
	}

	/**
	 * Increment the number of BossNPC the player has killed. And check for any
	 * unlocks
	 *
	 * @param bossID The ID for the boss
	 */
	public void addBossKill(String bossID) {
		bossKillCount++;
		bossTypesKilled.add(bossID);
		achievements.unlockWithPopUp(AchievementType.BOSS, bossID);
		unlockBossKillAchievement();
	}

	/**
	 * Get the number of BossNPC the player has killed
	 *
	 * @return the number of BossNPC killed by the player
	 */
	public int getBossKillCount() {
		return bossKillCount;
	}

	/**
	 * @return the Set containing each boss type killed
	 */
	public Set<String> getBossesKilled() {
		return bossTypesKilled;
	}

	private void unlockBossKillAchievement() {
		if (bossTypesKilled.size() == 8) {
			Player player = World.getInstance().getPlayerEntities().stream().filter(p -> p.stats == this).findFirst()
					.get();
			ItemEntity key = new ItemEntity(ItemRegistry.getItem("duck_king_key"));
			key.setPosition(player.getX(), player.getY() - 5);
			World.getInstance().addEntity(key);
		}
	}

	/**
	 * This method is called when the player has gained enough experience points
	 * to level up
	 */
	public void levelUp() {
		logger.info("level up");
		Toaster.ejectAllToast();
		Toaster.darkToast("LEVEL UP! Press T to see what new skills you can unlock!");
		playerLevel += 1;
		experiencePoints = 0;
		this.skillPoints += ((this.playerLevel / 5) + 3);
		achievements.unlockWithPopUp(AchievementType.LEVELS, playerLevel);
		levelBenefits(playerLevel);
	}

	/**
	 * This method outputs benefits the player recieves once they reach a
	 * certain level
	 *
	 * @param level
	 */
	public void levelBenefits(int level) {
		switch (level) {
			case 7:
				Toaster.darkToast("You can now equip 3 weapons at once!");
				break;
			case 15:
				Toaster.darkToast("You can now equip 4 weapons at once!");
				break;
			case 25:
				Toaster.darkToast("You can now equip 5 weapons at once!");
				break;
			default:
				break;
		}
	}

	/**
	 * Get player's current level
	 *
	 * @return player's current level
	 */
	public int getPlayerLevel() {
		return this.playerLevel;
	}

	public List<Passive> getPassiveSkills() {
		ArrayList<Passive> listOfSkills = new ArrayList<Passive>();
		listOfSkills.addAll(skills.getAttackSkills());
		listOfSkills.addAll(skills.getDefenseSkills());
		listOfSkills.addAll(skills.getMovementSkills());
		return listOfSkills;
	}

	public void addSkillPoints(int skillPoints) {
		this.skillPoints += skillPoints;
	}

	public int getSkillPoints() {
		return this.skillPoints;
	}

	public void setActivateSkill(ActivateSkill skills) {
		this.skills = skills;
	}

	public Achievements getAchievements() {
		return achievements;
	}
}
