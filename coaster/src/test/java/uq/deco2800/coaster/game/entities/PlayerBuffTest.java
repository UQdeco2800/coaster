package uq.deco2800.coaster.game.entities;

import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerBuffTest {
	String testStat = "health";
	float testmodifier = 2.0f;
	long testtime = 5000;
	private PlayerBuff test = new PlayerBuff(testStat, testmodifier, testtime);
	long countdown = 450;


	@Before
	public void load() {
		TestHelper.load();
	}

	@Test
	public void isPlayerBuff() {
		assertTrue(test instanceof PlayerBuff);

	}

	@Test
	public void statsCorrect() {
		assertEquals(testStat, test.getStat());
		assert (testmodifier == test.getModifier());
		assertEquals(testtime, test.getTime());
		assertFalse(test.getApplied());
		test.setApplied();
		assertTrue(test.getApplied());
		test.setNotApplied();
		assertFalse(test.getApplied());
		test.countDown(countdown);
		assert (test.getTime() + countdown == testtime);
	}

}
