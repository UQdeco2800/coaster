package uq.deco2800.coaster.game;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import uq.deco2800.coaster.game.achievements.Achievement;
import uq.deco2800.coaster.game.achievements.AchievementType;

public class AchievementTest {

	@Test
	public void testAcheivementTypes() {
		Assert.assertEquals("[KILLS, LEVELS, COINS, PICKUPS, BOSS, MISC]", Arrays.toString(AchievementType.values()));
	}

	@Test
	public void testAchievement() {
		AchievementType type = AchievementType.KILLS;
		Achievement achievement = new Achievement(type, "Name", "Desc", 10, "Path");
		Assert.assertEquals(achievement.getType(), AchievementType.KILLS);
		Assert.assertEquals(achievement.getName(), "Name");
		Assert.assertEquals(achievement.getDescription(), "Desc");
		Assert.assertEquals(achievement.getValue(), 10);
		Assert.assertNull(achievement.getValueID());
		Assert.assertEquals(achievement.getImagePath(), "Path");

		Achievement achievementCopy = new Achievement(achievement);
		Assert.assertTrue(achievement.equals(achievementCopy));

		Assert.assertFalse(achievement.isUnlocked());
		achievement.unlock(0);
		Assert.assertFalse(achievement.isUnlocked());
		achievement.unlock("?");
		Assert.assertFalse(achievement.isUnlocked());
		achievement.unlock(10);
		Assert.assertTrue(achievement.isUnlocked());
		achievement.unlock(0);
		Assert.assertTrue(achievement.isUnlocked());

		achievement.lock();
		Assert.assertFalse(achievement.isUnlocked());
		achievement.forceUnlock();
		Assert.assertTrue(achievement.isUnlocked());
	}

	@Test
	public void testUniqueAchievement() {
		AchievementType type = AchievementType.KILLS;
		Achievement achievement = new Achievement(type, "Name", "Desc", "valueID", "Path");
		Assert.assertEquals(achievement.getType(), AchievementType.KILLS);
		Assert.assertEquals(achievement.getName(), "Name");
		Assert.assertEquals(achievement.getDescription(), "Desc");
		Assert.assertEquals(achievement.getValueID(), "valueID");
		Assert.assertEquals(achievement.getImagePath(), "Path");

		Achievement achievementCopy = new Achievement(achievement);
		Assert.assertTrue(achievement.equals(achievementCopy));

		Assert.assertFalse(achievement.isUnlocked());
		achievement.unlock(0);
		Assert.assertFalse(achievement.isUnlocked());
		achievement.unlock("?");
		Assert.assertFalse(achievement.isUnlocked());
		achievement.unlock("valueID");
		Assert.assertTrue(achievement.isUnlocked());
	}

	@Test
	public void testAchievementSort() {
		ArrayList<Achievement> testList = new ArrayList<>();
		AchievementType type = AchievementType.KILLS;
		Achievement achievement1 = new Achievement(type, "name", "desc", 20, "path");
		Achievement achievement2 = new Achievement(type, "name", "desc", 40, "path");
		Achievement achievement3 = new Achievement(type, "name", "desc", "valueID", "path");
		Achievement achievement4 = new Achievement(AchievementType.LEVELS, "name", "desc", 10, "path");
		Achievement achievement5 = new Achievement(AchievementType.LEVELS, "name", "desc", "VALUEID", "path");
		Achievement achievement6 = new Achievement(AchievementType.LEVELS, "name", "desc", "valueID", "path");

		testList.add(achievement1);
		testList.add(achievement3);
		testList.add(achievement2);
		testList.add(achievement5);
		testList.add(achievement4);
		testList.add(achievement6);

		Collections.sort(testList);

		Assert.assertEquals(testList.get(0), achievement1);
		Assert.assertEquals(testList.get(1), achievement2);
		Assert.assertEquals(testList.get(2), achievement3);
		Assert.assertEquals(testList.get(3), achievement4);
		Assert.assertEquals(testList.get(4), achievement5);
		Assert.assertEquals(testList.get(5), achievement6);
	}

	@Test
	public void testAchievementEquality() {
		AchievementType type = AchievementType.KILLS;
		Achievement achievement1 = new Achievement(type, "name", "desc", 20, "path");
		Achievement achievement2 = new Achievement(type, "name", "desc", 20, "path");
		Achievement achievement3 = new Achievement(type, "NAME", "desc", 20, "path");
		Achievement achievement4 = new Achievement(type, "name", "DESC", 20, "path");
		Achievement achievement5 = new Achievement(type, "name", "desc", 40, "path");
		Achievement achievement6 = new Achievement(type, "name", "desc", 20, "PATH");
		Achievement achievement7 = new Achievement(AchievementType.LEVELS, "name", "desc", 20, "path");
		Achievement achievement8 = new Achievement(type, "Name", "Desc", "valueID", "Path");

		Assert.assertFalse(achievement1.equals(0));

		Assert.assertTrue(achievement1.equals(achievement1));
		Assert.assertTrue(achievement1.hashCode() == achievement1.hashCode());

		Assert.assertTrue(achievement1.hashCode() == new Achievement(achievement1).hashCode());

		Assert.assertTrue(achievement1.equals(achievement2));
		Assert.assertTrue(achievement1.hashCode() == achievement2.hashCode());

		achievement2.unlock(20);

		Assert.assertFalse(achievement1.equals(achievement2));
		Assert.assertFalse(achievement1.hashCode() == achievement2.hashCode());

		Assert.assertFalse(achievement1.equals(achievement3));

		Assert.assertFalse(achievement1.hashCode() == achievement3.hashCode());

		Assert.assertFalse(achievement1.equals(achievement4));
		Assert.assertFalse(achievement1.hashCode() == achievement4.hashCode());

		Assert.assertFalse(achievement1.equals(achievement5));
		Assert.assertFalse(achievement1.hashCode() == achievement5.hashCode());

		Assert.assertFalse(achievement1.equals(achievement6));
		Assert.assertFalse(achievement1.hashCode() == achievement6.hashCode());

		Assert.assertFalse(achievement1.equals(achievement7));
		Assert.assertFalse(achievement1.hashCode() == achievement7.hashCode());

		Assert.assertFalse(achievement1.equals(achievement8));
		Assert.assertFalse(achievement1.hashCode() == achievement8.hashCode());
	}
}
