package uq.deco2800.coaster.game.achievements;

/**
 * Represents Achievements in-game for the Player to unlock when the conditions
 * for unlock are met.
 *
 * @author Steven
 */
public class Achievement implements Comparable<Achievement> {

	private AchievementType type;
	private String name;
	private String description;
	private String imagePath;
	private int value;
	private String valueID;
	private boolean unlocked;

	/**
	 * Constructs an Achievement
	 *
	 * @param type the Achievement type
	 * @param name the name to represent the Achievement
	 * @param description the description attached to the Achievement
	 * @param value the required unlock value
	 * @param imagePath the filename of an image to represent the Achievement
	 *            in-game
	 */
	public Achievement(AchievementType type, String name, String description, int value, String imagePath) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.value = value;
		this.valueID = null;
		this.unlocked = false;
	}

	/**
	 * Constructs an Achievement
	 *
	 * @param type the Achievement type
	 * @param name the name to represent the Achievement
	 * @param description the description attached to the Achievement
	 * @param valueID the unique id value
	 * @param imagePath the filename of an image to represent the Achievement
	 *            in-game
	 */
	public Achievement(AchievementType type, String name, String description, String valueID, String imagePath) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.valueID = valueID;
		this.unlocked = false;
	}

	/**
	 * Constructs an Achievement from another Achievement ( clone ).
	 *
	 * @param achievement
	 */
	public Achievement(Achievement achievement) {
		this.type = achievement.getType();
		this.name = achievement.getName();
		this.description = achievement.getDescription();
		this.imagePath = achievement.getImagePath();
		this.value = achievement.getValue();
		this.valueID = achievement.getValueID();
		this.unlocked = achievement.isUnlocked();
	}

	/**
	 * Takes an int value and sets the unlocked state to true if the value meets
	 * the requirements of being >= the required value.
	 *
	 * @param value the value to check against
	 */
	public void unlock(int value) {
		if (valueID == null && value >= this.value) {
			unlocked = true;
		}
	}

	/**
	 * Takes an int value and sets the unlocked state to true if the value meets
	 * the requirements of being >= the required value.
	 *
	 * @param value the value to check against
	 */
	public void unlock(String valueID) {
		if (this.valueID != null && this.valueID.equals(valueID)) {
			unlocked = true;
		}
	}

	/**
	 * Ignore the requirement for unlocking the Achievement and set it to
	 * unlocked.
	 */
	public void forceUnlock() {
		unlocked = true;
	}

	/**
	 * Locks the achievement regardless of its current unlocked value.
	 */
	public void lock() {
		unlocked = false;
	}

	/**
	 * @return true if the achievement is unlocked else false
	 */
	public boolean isUnlocked() {
		return unlocked;
	}

	/**
	 * @return the type of the Achievement.
	 */
	public AchievementType getType() {
		return type;
	}

	/**
	 * @return the name associated with the achievement
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description associated with the achievement
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the value required for the achievement to be unlocked
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the valueID required for the achievement to be unlocked
	 */
	public String getValueID() {
		return valueID;
	}

	/**
	 * @return the file path for the image associated with he achievement
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param compareAchievement the Achievement to compare
	 * @return the result of the comparison
	 */
	private int compareValues(Achievement compareAchievement) {
		int compareValue = compareAchievement.getValue();
		String compareValueID = compareAchievement.getValueID();
		// If both have a uniqueID
		if (valueID != null && compareValueID != null) {
			return valueID.compareTo(compareAchievement.getValueID());
			// If only this achievement has uniqueID
		} else if (valueID != null) {
			return valueID.compareTo(String.valueOf(compareValue));
			// If only other achievement has uniqueID
		} else if (compareValueID != null) {
			return String.valueOf(value).compareTo(compareValueID);
		}
		// Neither have uniqueID so compare values
		return value - compareValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type.toString().hashCode();
		result = prime * result + name.hashCode();
		result = prime * result + description.hashCode();
		if (valueID == null) {
			result = prime * result + value;
		} else {
			result = prime * result + valueID.hashCode();
		}
		result = prime * result + imagePath.hashCode();
		result += (unlocked ? 1 : 0);

		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Achievement)) {
			return false;
		}
		Achievement achievement = (Achievement) object;

		boolean result = type == achievement.getType() && name.equals(achievement.getName())
				&& description.equals(achievement.getDescription()) && imagePath.equals(achievement.getImagePath());

		return result && compareValues(achievement) == 0 && unlocked == achievement.isUnlocked();
	}

	@Override
	public int compareTo(Achievement compareAchievement) {
		// Allow sorting by AchievementType and required value in ascending
		// order for efficient checking.
		int compareTyp = type.compareTo(compareAchievement.getType());
		if (compareTyp != 0) {
			return compareTyp;
		}

		// If neither has uniqueID
		return compareValues(compareAchievement);
	}
}
