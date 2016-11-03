package uq.deco2800.coaster.game.entities.skills;

public class Skill {
	private String name;
	private String description;
	private SkillClass category;
	private int prerequisiteXP;
	private int skillPointsCost;

	public Skill(String name, String description, SkillClass category, int prerequisiteXP, int skillPointsCost) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.prerequisiteXP = prerequisiteXP;
		this.skillPointsCost = skillPointsCost;
	}

	public boolean canBeUnlocked(int xp) {
		return this.prerequisiteXP <= xp;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public SkillClass getCategory() {
		return this.category;
	}

	public int getPrerequisiteXP() {
		return this.prerequisiteXP;
	}

	public int getSkillPointsCost() {
		return this.skillPointsCost;
	}

	@Override
	public String toString() {
		String string = "";
		string += "Name: ";
		string += this.name;
		string += "\nDescription: ";
		string += this.description;
		string += "\nCategory: ";
		string += this.category;
		string += "\nPrerequisite XP: ";
		string += Integer.toString(this.prerequisiteXP);
		return string;
	}
}