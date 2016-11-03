package uq.deco2800.coaster.game;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.achievements.Achievement;
import uq.deco2800.coaster.game.achievements.AchievementType;
import uq.deco2800.coaster.game.achievements.Achievements;
import uq.deco2800.coaster.graphics.notifications.PopUps;

public class AchievementsTest {

	@BeforeClass
	public static void firstLoad() {
		TestHelper.load();
	}

	@Before
	public void reset() {
		Achievements.clear();
	}

	@AfterClass
	public static void cleanup() throws IOException {
		Achievements.initialize("achievements/AchievmentList.csv");
	}

	@Test
	public void testCSVAchievements() throws IOException {
		Achievements.initialize("achievements/AchievmentList.csv");
		Achievements achievements = new Achievements();
		Assert.assertTrue(achievements.getUnlocked().isEmpty());

		AchievementType testType = AchievementType.KILLS;
		Assert.assertNotEquals(achievements.getAchievements(testType), Achievements.getGloablAchievements(testType));
		List<Achievement> achievementSet = achievements.getAchievements(testType);
		int size = achievementSet.size();

		List<Achievement> unlocked = achievements.unlock(testType, 0);
		Assert.assertTrue(unlocked.isEmpty());

		unlocked = achievements.unlock(testType, achievementSet.get(0).getValue());

		Assert.assertTrue(achievements.getUnlocked().size() == 1);
		Assert.assertTrue(size - 1 == achievementSet.size());
		Assert.assertTrue(achievements.getUnlocked().contains(unlocked.get(0)));

		achievements.unlock(testType, achievementSet.get(achievementSet.size() - 1).getValue());
		Assert.assertTrue(achievements.unlock(testType, 0).isEmpty());
		Assert.assertTrue(size == achievements.getUnlocked().size());

		achievements.forceUnlockAll();
		Assert.assertTrue(achievements.getAllAchievements().isEmpty());
	}

	@Test
	public void testUniqueCSVAchievements() throws IOException {
		Achievements.initialize("achievements/AchievmentList.csv");
		Achievements achievements = new Achievements();
		Assert.assertTrue(achievements.getUnlocked().isEmpty());

		// Add more when added to CSV
		AchievementType type = AchievementType.KILLS;
		achievements.unlock(type, "???");
	}

	@Test
	public void testCSVAchievementsWithPopUp() throws IOException {
		Achievements.initialize("achievements/AchievmentList.csv");
		Achievements achievements = new Achievements();
		AchievementType testType = AchievementType.KILLS;
		List<Achievement> achievementList = achievements.getAchievements(testType);

		achievements.unlockWithPopUp(testType, achievementList.get(0).getValue());

		Assert.assertTrue(PopUps.hasPopUps());

		// Add more when added to CSV
		achievements.unlockWithPopUp(testType, "???");
	}

	@Test
	public void testDoubleLoad() throws IOException {
		Achievements.initialize("achievements/AchievmentList.csv");
		Achievements.getAllGlobalAchievements().get(0).forceUnlock();
		Achievements.initialize("achievements/AchievmentList.csv");
		Assert.assertTrue(Achievements.getAllGlobalAchievements().get(0).isUnlocked());
	}

	@Test(expected = IllegalStateException.class)
	public void testNonLoadedAchievements() {
		new Achievements();
	}

	@Test(expected = NullPointerException.class)
	public void testLoadCSVFail() throws IOException {
		Achievements.initialize("achievements/AList.csv");
	}

	@Test(expected = IOException.class)
	public void testLoadCSVFailEmptyPath() throws IOException {
		Achievements.initialize("");
	}
}
