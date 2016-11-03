package uq.deco2800.coaster.game.entities.skills;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.MetaTest;
import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.TestingNPC;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpellTest {
	public World testWorld;
	public Player testPlayer;
	public TestingNPC testingNPC;

	//initialise logger
	private static final Logger logger = LoggerFactory.getLogger(MetaTest.class);

	//initialise player
	public void initialise() {
		TestHelper.load();
		testWorld = World.getInstance();
		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testPlayer.setPosition(3, 3);
	}

	public void initialiseTestingNPC() {
		testingNPC = new TestingNPC();
		testWorld.addEntity(testingNPC);
		testingNPC.setPosition(2, 2);
		testingNPC.setCurrentHealth(100);
		logger.error(Integer.toString(testPlayer.getNearbyEntities(100).size()));
	}

	@Test
	public void teleportTest() {
		initialise();
		Spells.teleport(testPlayer, 1, 1);
		Assert.assertEquals(1, (int) testPlayer.getX());
		Assert.assertEquals(1, (int) testPlayer.getY());
	}

	@Test
	public void dashUnlockTest() {
		TestHelper.load();
		testWorld = World.getInstance();
		InputManager.clearAllValues();

		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1);
		testPlayer.setPosition(0, 0);
		testWorld.setTiles(TestHelper.getMobilityTestTiles());
		testWorld.gameLoop(1);
		testWorld.gameLoop(1);

		InputManager.setTestValue(GameAction.DASH, true);
		assertEquals(EntityState.STANDING, testPlayer.getCurrentState());
		InputManager.setTestValue(GameAction.DASH, false);

		//Add dash skill to test player 
		testPlayer.addMovementSkill(SkillList.DASH);
		// Test moving -> dashing
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		testWorld.gameLoop(1);
		testWorld.gameLoop(1);
		assertEquals(EntityState.MOVING, testPlayer.getCurrentState());
		InputManager.setTestValue(GameAction.DASH, true);
		testWorld.gameLoop(1);
		assertEquals(EntityState.DASHING, testPlayer.getCurrentState());
		assertEquals(testPlayer.getDashSpeed(), testPlayer.getVelX(), 0.0002);
	}

	@Test
	public void doubleJumpUnlockTest() {
		TestHelper.load();
		testWorld = World.getInstance();
		InputManager.clearAllValues();

		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1);
		testPlayer.setPosition(2, 29);
		testWorld.setTiles(TestHelper.getMobilityTestTiles());
		testWorld.gameLoop(1);
		testWorld.gameLoop(1);
		
		assertEquals(EntityState.STANDING, testPlayer.getCurrentState());
		
		//Add double jump skill to test player 
		testPlayer.addMovementSkill(SkillList.DOUBLE_JUMP);
		// STANDING and jump input => jumping
		InputManager.setTestValue(GameAction.JUMP, true);
		testWorld.gameLoop(1);
		assertEquals(EntityState.JUMPING, testPlayer.getCurrentState());
		assertEquals(testPlayer.getJumpSpeed(), testPlayer.getVelY(), 0.0002);

		// Wait until halfway down, then jump again
		InputManager.setTestValue(GameAction.JUMP, false);
		for (int i = 0; i <= (testPlayer.getJumpDuration() / 2); i++) {
			testWorld.gameLoop(1);
		}
		assertTrue(testPlayer.getVelY() > 0); // Because y is positive downwards
		assertEquals(EntityState.JUMPING, testPlayer.getCurrentState());

		InputManager.setTestValue(GameAction.JUMP, true);
		testWorld.gameLoop(1);
		testWorld.gameLoop(1);
		assertEquals(EntityState.JUMPING, testPlayer.getCurrentState());
		assertTrue(testPlayer.getVelY() < 0); // Because y is positive downwards
	}
	
	@Test
	public void healTest () {
		TestHelper.load();
		testWorld = World.getInstance();
		InputManager.clearAllValues();
		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1);
		int maxHealth = testPlayer.getMaxHealth();
		testWorld.gameLoop(1);
		
		assertEquals(maxHealth, testPlayer.getCurrentHealth());
		testPlayer.addHealth(-1);
		testWorld.gameLoop(1);
		maxHealth -= 1;
		assertEquals(maxHealth, testPlayer.getCurrentHealth());
		maxHealth += 1;
		testPlayer.addDefenseSkill(SkillList.HEALING);
		for (int i = 0; i < 51; i++){
			testWorld.gameLoop(1);
		}
		assertEquals(maxHealth, testPlayer.getCurrentHealth());
	}
	
	
/* Removed until NPCs can work properly
	@Test
	public void blinkStrikeTest() {
		initialise();
		initialiseTestingNPC();


		int startingHealth = testingNPC.getCurrentHealth();
		testWorld.gameLoop(1);
		Spells.blinkStrike(testingNPC, testWorld);
		Assert.assertTrue(startingHealth > testingNPC.getCurrentHealth());
	}
*/
}
