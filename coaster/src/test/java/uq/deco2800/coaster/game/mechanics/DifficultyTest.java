package uq.deco2800.coaster.game.mechanics;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.world.World;

public class DifficultyTest {

	@Test
	public void testDifficultyEnumValues() {
		TestHelper.load();
		Assert.assertEquals("[EASY, MEDIUM, HARD, INSANE]",
				Arrays.toString(Difficulty.values()));
	}

	@Test
	public void testSettingDifficulty() {
		TestHelper.load();
		World world = World.getInstance();
		Assert.assertTrue(world.getDifficulty() == 1.0); // Default value

		world.setDifficulty(Difficulty.EASY);
		Assert.assertTrue(world.getDifficulty() == 0.5);

		world.setDifficulty(Difficulty.MEDIUM);
		Assert.assertTrue(world.getDifficulty() == 1.0);

		world.setDifficulty(Difficulty.HARD);
		Assert.assertTrue(world.getDifficulty() == 1.5);

		world.setDifficulty(Difficulty.INSANE);
		Assert.assertTrue(world.getDifficulty() == 2.0);
	}
}
