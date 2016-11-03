package uq.deco2800.coaster.game.entities.skills;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.PlayerStats;

/**
 * initialize all skills that could be made available to the player. A handler to check and stores which skill/spells
 * are activated by the player. A communication bridge between the skill tree GUI and the back hand code.
 *
 * @author Wil-DOG
 */
public class ActivateSkill {

	private List<Passive> movementSkills = new ArrayList<>();
	private List<Passive> defenseSkills = new ArrayList<>();
	private List<Passive> attackSkills = new ArrayList<>();
	private List<Passive> activeAbilities = new ArrayList<>();
	private List<Passive> allAvailableSkills = new ArrayList<>();
	private List<Active> allAvailableSpells = new ArrayList<>();

	private Passive damageBoost;
	private Passive firingRateBoost;
	private Passive critChanceBoost;
	private Passive critDamageBoost;

	private boolean deathBlossom;
	private boolean omniSlash;
	private boolean timeLock;
	private boolean overCharge;
	private boolean invincible;
	private boolean field;
	private boolean sniper;

	// Defence
	private Passive baseArmour;
	private Passive healthBoost;
	private Passive manaBoost;
	private Passive healing;

	// Movement
	private Passive doubleJump;
	private Passive speedBoost;
	private Passive dash;
	private Passive sprint;

	/**
	 * Initialize every skill available to a player
	 *
	 * @param stats The PlayerStats class for the individual player
	 */
	public ActivateSkill(PlayerStats stats) {
		//initialize skills available to the player
		this.damageBoost = new Passive("Damage boost", "Increase player base damage", SkillClass.ATTACK, 100, 2, StatType.DAMAGE, 10);
		this.allAvailableSkills.add(damageBoost);
		this.firingRateBoost = new Passive("Firing rate boost", "Increase player firing rate", SkillClass.ATTACK, 100, 2, StatType.ATTACKSPEED, (int) stats.getFiringRate());
		this.allAvailableSkills.add(firingRateBoost);
		this.critChanceBoost = new Passive("Critical hit chance boost", "Increase player critical hit chance", SkillClass.ATTACK, 100, 2, StatType.CRITCHANCE, 2);
		this.allAvailableSkills.add(critChanceBoost);
		this.critDamageBoost = new Passive("Critical hit damage boost", "Increase player critical damage dealt", SkillClass.ATTACK, 100, 2, StatType.CRITDAMAGE, 2);
		this.allAvailableSkills.add(critDamageBoost);
		this.speedBoost = new Passive("Movement speed increase", "Increase player movement speed", SkillClass.MOVEMENT, 100, 2, StatType.MOVESPEED, 10);
		this.allAvailableSkills.add(speedBoost);
		this.doubleJump = new Passive("Double jump", "Allow the player to double jump", SkillClass.MOVEMENT, 100, 2, StatType.MOVESPEED, 1);
		this.allAvailableSkills.add(doubleJump);
		this.dash = new Passive("Dash", "Allow the player to dash", SkillClass.MOVEMENT, 100, 2, StatType.MOVESPEED, 1);
		this.allAvailableSkills.add(dash);
		this.healthBoost = new Passive("Health boost", "Increase player base health", SkillClass.DEFENSE, 100, 2, StatType.HEALTH, 25);
		this.allAvailableSkills.add(healthBoost);
		this.manaBoost = new Passive("Mana boost", "Increase player base mana", SkillClass.DEFENSE, 100, 2, StatType.MANA, 25);
		this.allAvailableSkills.add(manaBoost);
		this.baseArmour = new Passive("Armour", "Increase the amour the player has", SkillClass.DEFENSE, 100, 2, StatType.ARMOUR, 10);
		this.allAvailableSkills.add(baseArmour);
		this.healing = new Passive("Healing", "playing heals over time", SkillClass.DEFENSE, 100, 2, StatType.HEALTH, 1);
		this.allAvailableSkills.add(healing);
		this.sprint = new Passive("Sprint", "playing can sprint", SkillClass.MOVEMENT, 100, 2, StatType.MOVESPEED, 1);
		this.allAvailableSkills.add(sprint);
		this.movementSkills = new ArrayList<>();
		this.defenseSkills = new ArrayList<>();
		this.attackSkills = new ArrayList<>();
		this.deathBlossom = false;
		this.omniSlash = false;
		this.timeLock = false;
		this.overCharge = false;
		this.invincible = false;
		this.sniper = false;
		this.allAvailableSpells = new ArrayList<>();
	}

	/**
	 * This method adds the player's attack skills to the list of skills when a new skill is activated
	 *
	 * @param skill attack skill to be activated
	 */
	public void addAttackSkill(SkillList skill) {
		switch (skill) {
			case DAMAGE_BOOST:
				this.attackSkills.add(damageBoost);
				break;
			case FIRING_RATE:
				this.attackSkills.add(this.firingRateBoost);
				break;
			case CRIT_CHANCE:
				this.attackSkills.add(this.critChanceBoost);
				break;
			case CRIT_DAMAGE:
				this.attackSkills.add(this.critDamageBoost);
				break;
			default:
				break;
		}
	}

	/**
	 * Getter method for all passive attack skills activated by the player
	 *
	 * @return An List of all passive attack skills unlocked by the player
	 */
	public List<Passive> getAttackSkills() {
		return this.attackSkills;
	}

	/**
	 * This method adds the player's defense skills to the list of skills when a new skill is activated
	 *
	 * @param skill defense skill to be activated
	 */
	public void addDefenseSkill(SkillList skill) {
		switch (skill) {
			case HEALTH_BOOST:
				this.defenseSkills.add(this.healthBoost);
				break;
			case MANA_BOOST:
				this.defenseSkills.add(this.manaBoost);
				break;
			case ARMOUR_BOOST:
				this.defenseSkills.add(this.baseArmour);
				break;
			case HEALING:
				this.defenseSkills.add(this.healing);
				break;
			default:
				break;
		}
	}

	/**
	 * Getter method for all passive defense skills activated by the player
	 *
	 * @return An ArrayList of all passive defense skills unlocked by the player
	 */
	public List<Passive> getDefenseSkills() {
		return this.defenseSkills;
	}

	/**
	 * This method adds the player's movement skills to the list of skills when a new skill is activated
	 *
	 * @param skill movement skill to be activated
	 */
	public void addMovementSkill(SkillList skill) {
		switch (skill) {
			case SPEED_BOOST:
				this.movementSkills.add(this.speedBoost);
				break;
			case DASH:
				this.movementSkills.add(this.dash);
				break;
			case DOUBLE_JUMP:
				this.movementSkills.add(this.doubleJump);
				break;
			case SPRINT:
				this.movementSkills.add(this.sprint);
				break;
			default:
				break;
		}
	}

	/**
	 * Getter method for all passive movement skills activated by the player
	 *
	 * @return An List of all passive movement skills unlocked by the player
	 */
	public List<Passive> getMovementSkills() {
		return this.movementSkills;
	}

	/**
	 * Getter method for all passive skills activated by the player
	 *
	 * @return An List of all passive skills unlocked by the player
	 */
	public List<Passive> getAllActiveSkills() {
		this.activeAbilities.clear();
		this.activeAbilities.addAll(this.attackSkills);
		this.activeAbilities.addAll(this.defenseSkills);
		this.activeAbilities.addAll(this.movementSkills);
		return this.activeAbilities;
	}
	
	public List<Passive> getAllAvailableSkills() {
		return this.allAvailableSkills;
	}

	/**
	 * Unlock a specific active skills/spells
	 *
	 * @param spell The spell being unlocked
	 */
	public void enableSpell(SkillList spell) {
		switch (spell) {
			case DEATH_BLOSSOM:
				this.deathBlossom = true;
				break;
			case OMNISLASH:
				this.omniSlash = true;
				break;
			case SNIPER:
				this.sniper = true;
				break;
			case TIMELOCK:
				this.timeLock = true;
				break;
			case OVERCHARGE:
				this.overCharge = true;
				break;
			case INVINCIBLE:
				this.invincible = true;
				break;
			case ATFIELD:
				this.field= true;
				break;	
			default:
				break;
		}
	}

	/**
	 * Check to see if a specific spell has been unlocked by the player
	 *
	 * @param spell The spell to be checked
	 * @return True if the skill is unlocked and false if the skill is locked
	 */
	public boolean spellActivated(SkillList spell) {
		switch (spell) {
			case DEATH_BLOSSOM:
				return this.deathBlossom;
			case OMNISLASH:
				return this.omniSlash;
			case TIMELOCK:
				return this.timeLock;
			case OVERCHARGE:
				return this.overCharge;
			case INVINCIBLE:
				return this.invincible;
			case ATFIELD:
				return this.field;
			case SNIPER:
				return this.sniper;
			default:
				return false;
		}
	}
}
