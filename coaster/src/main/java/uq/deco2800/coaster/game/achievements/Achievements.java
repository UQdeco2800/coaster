package uq.deco2800.coaster.game.achievements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.graphics.notifications.PopUps;

/**
 * Manages all the achievements the player can unlock in-game and tracks which
 * have been unlocked and which haven't.
 *
 * @author Steven
 */
public class Achievements {
	private static final Logger logger = LoggerFactory.getLogger(Achievements.class);
	private static final HashMap<AchievementType, ArrayList<Achievement>> allAchievementsMap = new HashMap<>();
	private final HashMap<AchievementType, ArrayList<Achievement>> achievementsMap;
	private final TreeSet<Achievement> unlocked;
	private static final String ERROR = "%s at line: %d. Achievement skipped";
	private static final String INT_REGEX = "^-?\\d+$";

	/**
	 * Loads the Achievements.
	 *
	 * @param filePath the CSV file to load from
	 * @throws IOException if the filePath is invalid
	 */
	public static void initialize(String filePath) throws IOException {
		if (allAchievementsMap.isEmpty()) {
			for (AchievementType type : AchievementType.values()) {
				allAchievementsMap.put(type, new ArrayList<Achievement>());
			}
			loadByCSV(filePath);
		}
	}

	/**
	 * Constructs the Achievements
	 */
	public Achievements() {
		achievementsMap = new HashMap<>();
		unlocked = new TreeSet<>();
		if (allAchievementsMap.isEmpty()) {
			throw new IllegalStateException("Achivements have not been loaded.");
		}

		for (Map.Entry<AchievementType, ArrayList<Achievement>> entry : allAchievementsMap.entrySet()) {
			ArrayList<Achievement> achievementList = new ArrayList<>();
			for (Achievement achievement : entry.getValue()) {
				// Clone Achievement to not affect global achievements.
				Achievement newAchievement = new Achievement(achievement);
				newAchievement.lock();
				achievementList.add(newAchievement);
			}
			Collections.sort(achievementList);
			achievementsMap.put(entry.getKey(), achievementList);
		}
	}

	/**
	 *
	 * @param filePath the CSV file to load from
	 * @throws IOException if the filePath is invalid
	 */
	private static void loadByCSV(String filePath) throws IOException {
		if (filePath.trim().isEmpty()) {
			throw new IOException("Failed to load file: '" + filePath + "'");
		}
		BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(Achievements.class.getClassLoader().getResourceAsStream(filePath)));

		String line;
		final String delimiter = ",";
		int lineNumber = 0;

		// Read through file
		while ((line = fileReader.readLine()) != null) {
			lineNumber++;
			if (lineNumber == 1) {
				continue;
			}
			String[] tokens = line.split(delimiter);
			loadFromArray(tokens, lineNumber);
		}
		fileReader.close();

		for (ArrayList<Achievement> achievementList : allAchievementsMap.values()) {
			Collections.sort(achievementList);
		}
		logger.info("CSV Achievments loaded");
	}

	/**
	 * Used in conjunction with Achievements.loadCSV which handles parsing the
	 * line data that was split into an array. If the value isn't an integer or
	 * the type isn't a value of AchievementType the line is skipped.
	 *
	 * @param array an array of String used to construct an Achievement from
	 * @param lineNumber the line from the file the array was read at
	 */
	private static void loadFromArray(String[] array, int line) {
		if (array.length != 5) {
			logger.error(String.format(ERROR, "Missing fields", line));
			return;
		}

		AchievementType type;
		try {
			type = AchievementType.valueOf(array[4]);
		} catch (IllegalArgumentException e) {
			logger.error(String.format(ERROR, "Invalid TYPE", line), e);
			return;
		}

		Achievement achievement;
		if (array[2].matches(INT_REGEX)) {
			achievement = new Achievement(type, array[0], array[1], Integer.parseInt(array[2]), array[3]);
		} else {
			achievement = new Achievement(type, array[0], array[1], array[2], array[3]);
		}
		achievement.forceUnlock();
		allAchievementsMap.get(type).add(achievement);
	}

	/**
	 * Unlocks all achievements of a given AchievementType for the value
	 * provided. Any achievements that are unlocked are added to unlocked
	 * arrayList and any achievements unlocked when the method is called are
	 * returned in an arrayList.
	 *
	 * @param type the AchievementType to check for
	 * @param value the value to check against
	 * @return an arrayList of any achievements unlocked for a given method call
	 */
	public List<Achievement> unlock(AchievementType type, int value) {
		// Get the achievementList for the specified achievementType
		ArrayList<Achievement> achievementList = achievementsMap.get(type);
		ArrayList<Achievement> newUnlocks = new ArrayList<>();

		while (!achievementList.isEmpty()) {
			// Get the first achievement from that list
			Achievement achievement = achievementList.get(0);
			// Attempt to unlock it
			achievement.unlock(value);
			if (achievement.isUnlocked()) {
				// If achievement is unlocked it is removed from the
				// achievementList and added to the unlocked list
				unlocked.add(achievementList.remove(0));
				newUnlocks.add(achievement);
			} else {
				// Order is guaranteed so no further check required as all the
				// following achievements will not unlock as well
				break;
			}
		}
		return newUnlocks;
	}

	/**
	 * Unlocks all achievements of a given AchievementType for the valueID
	 * provided. Any achievements that are unlocked are added to unlocked
	 * arrayList and any achievements unlocked when the method is called are
	 * returned in an arrayList.
	 *
	 * @param type the AchievementType to check for
	 * @param value the value to check against
	 * @return an arrayList of any achievements unlocked for a given method call
	 */
	public List<Achievement> unlock(AchievementType type, String valueID) {
		ArrayList<Achievement> achievementList = achievementsMap.get(type);
		ArrayList<Achievement> newUnlocks = new ArrayList<>();
		for (int i = achievementList.size() - 1; i >= 0; i--) {
			// Get the first achievement from that list
			Achievement achievement = achievementList.get(i);
			// Attempt to unlock it
			achievement.unlock(valueID);
			if (achievement.isUnlocked()) {
				// If achievement is unlocked it is removed from the
				// achievementList and added to the unlocked list
				unlocked.add(achievementList.remove(i));
				newUnlocks.add(achievement);
			}
		}
		return newUnlocks;
	}

	/**
	 * Same as Achievements.unlock(AchievementType, int) with the addition of
	 * any new achievements unlocked will will have the achievement name added
	 * to PopUps which will in turn display a message on screen to notify the
	 * user an achievement as been unlocked.
	 *
	 * @param type the AchievementType to check for
	 * @param value the value to check against
	 */
	public void unlockWithPopUp(AchievementType type, int value) {
		for (Achievement achievement : unlock(type, value)) {
			PopUps.add("Achievement Unlocked: " + achievement.getName());
		}
	}

	/**
	 * Same as Achievements.unlock(AchievementType, String) with the addition of
	 * any new achievements unlocked will will have the achievement name added
	 * to PopUps which will in turn display a message on screen to notify the
	 * user an achievement as been unlocked.
	 *
	 * @param type the AchievementType to check for
	 * @param valueID the valueID to check against
	 */
	public void unlockWithPopUp(AchievementType type, String valueID) {
		for (Achievement achievement : unlock(type, valueID)) {
			PopUps.add("Achievement Unlocked: " + achievement.getName());
		}
	}

	/**
	 * Unlock all the Achievements.
	 */
	public void forceUnlockAll() {
		for (AchievementType type : achievementsMap.keySet()) {
			List<Achievement> achievementList = getAchievements(type);
			while (!achievementList.isEmpty()) {
				Achievement achievement = achievementList.remove(0);
				achievement.forceUnlock();
				unlocked.add(achievement);
			}
		}
	}

	/**
	 * @return the Set of currently unlocked achievements
	 */
	public List<Achievement> getUnlocked() {
		return new ArrayList<Achievement>(unlocked);
	}

	/**
	 * @param type the AchievementType specified
	 * @return the Set of locked achievements matching the type
	 */
	public List<Achievement> getAchievements(AchievementType type) {
		return achievementsMap.get(type);
	}

	/**
	 * @param type the AchievementType specified
	 * @return the Set of locked achievements matching the type
	 */
	public static List<Achievement> getGloablAchievements(AchievementType type) {
		return allAchievementsMap.get(type);
	}

	/**
	 * Helper method to add all values from a selected achievementsMap to a
	 * sorted List.
	 *
	 * @param map the selected achievementsMap
	 * @return sorted List of all Achievement in map
	 */
	private static List<Achievement> toList(HashMap<AchievementType, ArrayList<Achievement>> map) {
		ArrayList<Achievement> allAchievements = new ArrayList<>();
		for (ArrayList<Achievement> achievementList : map.values()) {
			allAchievements.addAll(achievementList);
		}
		Collections.sort(allAchievements);
		return allAchievements;
	}

	/**
	 * @return a sorted List of all locked Achievements.
	 */
	public List<Achievement> getAllAchievements() {
		return toList(achievementsMap);
	}

	/**
	 * @return a sorted List of all Achievements in allAchievementsMap.
	 */
	public static List<Achievement> getAllGlobalAchievements() {
		return toList(allAchievementsMap);
	}

	/**
	 * Wrapper method to clear loaded Achievements.
	 */
	public static void clear() {
		allAchievementsMap.clear();
	}
}
