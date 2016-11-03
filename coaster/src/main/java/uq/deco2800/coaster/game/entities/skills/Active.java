package uq.deco2800.coaster.game.entities.skills;


/**
 * Active skills are skills that are activated manually by the player. They have cooldowns and mana costs.
 */
public class Active extends Skill {

	private int manaCost;
	private int cooldown;
	private String type;
	private Spells.BaseSpell spell;
	private int skillPointsCost;

	public Active(Spells.BaseSpell spell, String name, String description, SkillClass category, int prerequisiteXP,
				  int skillPointsCost, int manaCost, int cooldown) {
		super(name, description, category, prerequisiteXP, skillPointsCost);
		this.spell = spell;
		this.manaCost = manaCost;
		this.cooldown = cooldown;
		this.type = "Active";
		this.skillPointsCost = skillPointsCost;
	}

	public int getManaCost() {
		return manaCost;
	}
	
	public int getSkillPointsCost() {
		return this.skillPointsCost;
	}

	public int getCooldown() {
		return cooldown;
	}

	public Spells.BaseSpell getSpell() { return spell; }
	
	public String getType() {
		return type;
	}
}
