package uq.deco2800.coaster.game.entities.skills;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.PlayerStats;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;

public class playerStatTest {
	public Player testPlayer;
	public PlayerStats stats;
	private World testWorld = World.getInstance();
	
	public void initialise() {
		TestHelper.load();
		testPlayer = new Player();
		stats = testPlayer.getPlayerStatsClass();
		testWorld.debugReset();
		testWorld.addEntity(testPlayer);
		TestHelper.load();
		testWorld.debugReset();
	}
	
	@Test
	public void levelUpTest() {
		initialise();
		TestHelper.load();
		InputManager.clearAllValues();
		int level = 1;
		assertEquals(level, stats.getPlayerLevel());
		testPlayer.addExperiencePoint(20);
		level++;
		assertEquals(level, testPlayer.getPlayerLevel());
	}
	
	@Test
	public void levelUpTwiceTest() {
		initialise();
		TestHelper.load();
		InputManager.clearAllValues();
		int level = 1;
		assertEquals(level, stats.getPlayerLevel());
		testPlayer.addExperiencePoint(200);
		testWorld.gameLoop(1);
		testWorld.gameLoop(1);
		testWorld.gameLoop(1);
		int experiencePoints = 200;
		while (experiencePoints > 20 * level) {
			experiencePoints =- (20 * level); 
			level++;
		}
		assertEquals(level, testPlayer.getPlayerLevel());
	}
}
