package uq.deco2800.coaster.game.entities.skills;

/**
 * Passive skills are skills that do not require activation from the player. They provide multiplicative (unless
 * otherwise stated) bonuses to certain numerical values important int the game, e.g. move speed, attack damage etc.
 */
public class Passive extends Skill {

	private int bonus;
	private StatType modifiedStat;
	private String type;

	/**
	 *
	 * @param name
	 * @param description
	 * @param category
	 * @param prerequisiteXP
	 * @param skillPointsCost
	 * @param modifiedStat
	 * @param bonus
	 */
	public Passive(String name, String description, SkillClass category, int prerequisiteXP, int skillPointsCost, StatType modifiedStat, int bonus) {
		super(name, description, category, prerequisiteXP, skillPointsCost);
		this.modifiedStat = modifiedStat;
		this.bonus = bonus;
		this.type = "Passive";
	}

	public int getModifierBonus() {
		return this.bonus;
	}

	public void setModifierBonus(int setBonus) {
		this.bonus += setBonus;
	}

	public StatType getModifiedStat() {
		return this.modifiedStat;
	}
	
	public String getType() {
		return type;
	}
}
