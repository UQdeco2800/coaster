package uq.deco2800.coaster.game.entities;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.RhinoNPC;
import uq.deco2800.coaster.game.entities.skills.SkillList;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;

public class MobilityTest {
	private static float EPSILON = 0.0002f;
	
	private World world = World.getInstance();
	private static Logger logger = LoggerFactory.getLogger(MobilityTest.class);
	private Player p;
	private static WorldTiles standardTiles;
	private static WorldTiles waterTiles;

	@Before
	public void init() {
		TestHelper.load();
		if (standardTiles == null) {
			logger.info("tiles are null");
			standardTiles = TestHelper.getMobilityTestTiles();
		}
		if (waterTiles == null) {
			logger.info("tiles are null");
			waterTiles = TestHelper.getWaterTiles();
		}
	}
	
	@AfterClass
	public static void cleanUp() {
		InputManager.clearAllValues();
	}

	/**
	 * Place player in midair. Test that they are in the jumping state
	 */
	@Test
	public void testNotOnGround() {
		loadWorldWithPlayer(2, 0);

		assertEquals(EntityState.JUMPING, p.getCurrentState());

	}

	/**
	 * Place the player on the ground. Test that they are in the standing state.
	 */
	@Test
	public void testStanding() {
		loadWorldWithPlayer(2, 29);
		// Test that onGround and no inputs => standing
		world.gameLoop(1);
		// Test that onGround and no inputs => standing
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertEquals(0, p.getVelY(), EPSILON);
	}

	/**
	 * Place the player on the ground and test that they can move left and right.
	 */
	@Test
	public void testMovementBothDirections() {
		loadWorldWithPlayer(2, 29);
		// STANDING and left input => moving left
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);

		// MOVING left transition to moving right
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		// Stop moving right => standing
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());

		// Turn to face left
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertEquals(0, p.getVelX(), EPSILON);
		assertEquals(-1, p.getFacing());

	}

	/**
	 * Place the player on the ground and test that they can jump, and that the jump lasts for the correct duration.
	 */
	@Test
	public void testJumpStill() {
		loadWorldWithPlayer(2, 29);

		// STANDING and jump input => jumping
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getJumpSpeed(), p.getVelY(), EPSILON);

		// Test jump time
		InputManager.setTestValue(GameAction.JUMP, false);
		for (int i = 0; i < p.getJumpDuration() - 1; i++) {
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			world.gameLoop(1);
		}

		// Test jump time i and jump -> standing
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertEquals(0, p.getVelY(), EPSILON);
	}

	/**
	 * Test that the player can jump while moving right
	 */
	@Test
	public void testJumpRight() {
		loadWorldWithPlayer(2, 29);
		// Begin moving right

		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		// STANDING and jump input => jumping
		InputManager.setTestValue(GameAction.JUMP, true);

		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getJumpSpeed(), p.getVelY(), EPSILON);
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		// Test jump time
		InputManager.setTestValue(GameAction.JUMP, false);
		for (int i = 0; i < p.getJumpDuration() - 1; i++) {
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);
			world.gameLoop(1);
		}

		// Test jump time == 1000 and jump -> standing
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can jump while moving left
	 */
	@Test
	public void testJumpLeft() {
		loadWorldWithPlayer(15, 29);
		// Begin moving right

		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);

		// STANDING and jump input => jumping
		InputManager.setTestValue(GameAction.JUMP, true);

		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getJumpSpeed(), p.getVelY(), EPSILON);
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);

		// Test jump time
		InputManager.setTestValue(GameAction.JUMP, false);
		for (int i = 0; i < p.getJumpDuration() - 1; i++) {
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);
			world.gameLoop(1);
		}

		// Test jump time == 1000 and jump -> standing
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can dash to the right
	 */
	@Test
	public void testDashRight() {
		loadWorldWithPlayer(2, 29);
		p.addMovementSkill(SkillList.DASH);
		// Test moving -> dashing
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		assertEquals(EntityState.DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);

		// Test dash duration -> standing
		InputManager.setTestValue(GameAction.DASH, false);
		for (int i = 0; i < (int) p.getDashDuration(); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.DASHING, p.getCurrentState());
			assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
		}
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		// Account for 1 frame delay in adjusting speed
		world.gameLoop(1);
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

	}
	
	@Test
	public void testDashOffLedge() {
		loadWorldWithPlayer(1, 22);
		p.addMovementSkill(SkillList.DASH);
		// Test moving -> dashing
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		assertEquals(EntityState.DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);

		while (p.getX() < 2) {
			assertEquals(EntityState.DASHING, p.getCurrentState());
			assertEquals(22, p.getY(), EPSILON);
			world.gameLoop(1);
		}
		assertEquals(EntityState.JUMPING, p.getCurrentState());
	}
	
	@Test
	public void testDashJump() {
		loadWorldWithPlayer(2, 29);
		p.addMovementSkill(SkillList.DASH);
		// Test moving -> dashing
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		assertEquals(EntityState.DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);

		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);;
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getJumpSpeed(), p.getVelY(), EPSILON);
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);		
	}
	
	/**
	 * Test that the player can dash to the right
	 */
	@Test
	public void testDashBothInput() {
		loadWorldWithPlayer(2, 29);
		p.addMovementSkill(SkillList.DASH);
		// Test moving -> dashing
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		assertEquals(EntityState.DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
	}


	/**
	 * Test that the player can dash to the left
	 */
	@Test
	public void testDashLeft() {
		loadWorldWithPlayer(15, 29);
		p.addMovementSkill(SkillList.DASH);
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);

		// Test standing -> dashing
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		assertEquals(EntityState.DASHING, p.getCurrentState());
		assertEquals(-p.getDashSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can air dash to the right
	 */
	@Test
	public void testAirDashRight() {
		loadWorldWithPlayer(2, 29);
		// Test jump -> air dash
		p.addMovementSkill(SkillList.DASH);
		jump();
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
		assertEquals(0, p.getVelY(), EPSILON);

		// Test AirDash duration -> jumping
		for (int i = 0; i < (int) p.getDashDuration(); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
			assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
		}
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());

		// Test that repeated air dashes fail
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.DASH, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());

		// wait until the player is on the ground again
		for (int i = 0; i < 500; i++) {
			world.gameLoop(1);
		}
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertEquals(0, p.getVelX(), EPSILON);
	}
	
	/**
	 * Test that the player can air dash to the right
	 */
	@Test
	public void testAirDashBothInput() {
		loadWorldWithPlayer(2, 20);
		// Test jump -> air dash
		p.addMovementSkill(SkillList.DASH);
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
		assertEquals(0, p.getVelY(), EPSILON);
		// no need to test the rest - already covered
	}
	
	/**
	 * Test that the player can air dash to the right
	 */
	@Test
	public void testAirDashNoInput() {
		loadWorldWithPlayer(2, 20);
		// Test jump -> air dash
		p.addMovementSkill(SkillList.DASH);
		airDash();

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
		assertEquals(0, p.getVelY(), EPSILON);
		// no need to test the rest - already covered
	}



	/**
	 * Test that the player can air dash to the left
	 */
	@Test
	public void testAirDashLeft() {
		loadWorldWithPlayer(15, 20);
		// Test jump -> air dash
		p.addMovementSkill(SkillList.DASH);
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(-p.getDashSpeed(), p.getVelX(), EPSILON);
		assertEquals(0, p.getVelY(), EPSILON);
	}

	/**
	 * Test that the player can air dash upwards
	 */
	@Test
	public void testAirDashUp() {
		loadWorldWithPlayer(15, 29);
		p.addMovementSkill(SkillList.DASH);
		// Test jump -> air dash
		jump();
		InputManager.setTestValue(GameAction.MOVE_UP, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_UP, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(-p.getDashSpeed(), p.getVelY(), EPSILON);
		assertEquals(0, p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can air dash downwards
	 */
	@Test
	public void testAirDashDown() {
		loadWorldWithPlayer(15, 29);
		p.addMovementSkill(SkillList.DASH);
		// Test jump -> air dash
		jump();
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_DOWN, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelY(), EPSILON);
		assertEquals(0, p.getVelX(), EPSILON);

		// Test AirDash into ground -> standing
		while (p.getY() < 29) {
			assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
			assertEquals(p.getDashSpeed(), p.getVelY(), EPSILON);
			assertEquals(0, p.getVelX(), EPSILON);
			world.gameLoop(1);
		}

		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
	}

	/**
	 * Test that the player can air dash diagonally up-left
	 */
	@Test
	public void testAirDashUpLeft() {
		loadWorldWithPlayer(15, 20);
		p.addMovementSkill(SkillList.DASH);
		// Test jump -> air dash
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		InputManager.setTestValue(GameAction.MOVE_UP, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		InputManager.setTestValue(GameAction.MOVE_UP, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(-p.getDashSpeed(), p.getVelY(), EPSILON);
		assertEquals(-p.getDashSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can air dash diagonally up-right
	 */
	@Test
	public void testAirDashUpRight() {
		loadWorldWithPlayer(2, 20);
		p.addMovementSkill(SkillList.DASH);
		// Test jump -> air dash
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		InputManager.setTestValue(GameAction.MOVE_UP, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);
		InputManager.setTestValue(GameAction.MOVE_UP, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(-p.getDashSpeed(), p.getVelY(), EPSILON);
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can air dash diagonally down-left
	 */
	@Test
	public void testAirDashDownLeft() {
		loadWorldWithPlayer(15, 20);
		p.addMovementSkill(SkillList.DASH);
		// Test jump -> air dash
		jump();
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_DOWN, false);
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelY(), EPSILON);
		assertEquals(-p.getDashSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can air dash diagonally down-right
	 */
	@Test
	public void testAirDashDownRight() {
		loadWorldWithPlayer(2, 20);
		p.addMovementSkill(SkillList.DASH);
		// Test jump -> air dash
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		airDash();
		InputManager.setTestValue(GameAction.MOVE_DOWN, false);
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);

		assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
		assertEquals(p.getDashSpeed(), p.getVelY(), EPSILON);
		assertEquals(p.getDashSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can slide to the right
	 */
	@Test
	public void testSlideRight() {
		loadWorldWithPlayer(2, 29);

		// Test moving -> SLIDING
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());

		InputManager.setTestValue(GameAction.SLIDE, true);
		world.gameLoop(1);
		assertEquals(EntityState.SLIDING, p.getCurrentState());
		assertEquals(p.getSlideSpeed(), p.getVelX(), EPSILON);

		// Test slide duration -> standing
		InputManager.setTestValue(GameAction.SLIDE, false);
		for (int i = 0; i < (int) p.getSlideDuration(); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.SLIDING, p.getCurrentState());
			assertEquals(p.getSlideSpeed(), p.getVelX(), EPSILON);
		}
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		// Account for 1 frame delay in becoming still
		world.gameLoop(1);
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can slide to the left
	 */
	@Test
	public void testSlideLeft() {
		loadWorldWithPlayer(15, 29);
		turnLeft();

		// Test standing -> SLIDING
		InputManager.setTestValue(GameAction.SLIDE, true);
		world.gameLoop(1);
		assertEquals(EntityState.SLIDING, p.getCurrentState());
		assertEquals(-p.getSlideSpeed(), p.getVelX(), EPSILON);

		// Test slide duration -> standing
		InputManager.setTestValue(GameAction.SLIDE, false);
		for (int i = 0; i < (int) p.getSlideDuration(); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.SLIDING, p.getCurrentState());
			assertEquals(-p.getSlideSpeed(), p.getVelX(), EPSILON);
		}
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		// Account for 1 frame delay in becoming still
		world.gameLoop(1);
		assertEquals(0, p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can double jump
	 */
	@Test
	public void testDoubleJumpStill() {
		loadWorldWithPlayer(2, 29);
		p.addMovementSkill(SkillList.DOUBLE_JUMP);
		// STANDING and jump input => jumping
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getJumpSpeed(), p.getVelY(), EPSILON);

		// Test that a jump on the way up fails
		InputManager.setTestValue(GameAction.JUMP, false);
		world.gameLoop(p.getJumpDuration() / 4);
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() < 0); // Because y is positive downwards

		// Wait until halfway down, then jump again
		InputManager.setTestValue(GameAction.JUMP, false);
		for (int i = 0; i < (p.getJumpDuration() / 2); i++) {
			world.gameLoop(1);
		}
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() > 0); // Because y is positive downwards

		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() < 0); // Because y is positive downwards

		// Test that another jump fails
		InputManager.setTestValue(GameAction.JUMP, false);
		for (int i = 0; i < (p.getJumpDuration() * 3 / 4); i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() > 0); // Because y is positive downwards
	}

	/**
	 * Test that the player will be unable to jump if there is a block directly over their head
	 */
	@Test
	public void testUnableToJump() {
		loadWorldWithPlayer(1, 29);

		assertEquals(EntityState.STANDING, p.getCurrentState());

		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertEquals(0, p.getVelY(), EPSILON);
	}

	/**
	 * Test that a jump will be cut short if the player hits a ceiling mid-jump.
	 */
	@Test
	public void testHitCeilingOnJump() {
		loadWorldWithPlayer(1, 26);
		assertEquals(EntityState.STANDING, p.getCurrentState());

		// STANDING and jump input => jumping
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getJumpSpeed(), p.getVelY(), EPSILON);

		// Test jump until ceiling
		InputManager.setTestValue(GameAction.JUMP, false);
		int jumpTimer = 0;
		while (p.getY() > 25) {
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertTrue(p.getVelY() < 0);
			world.gameLoop(1);
			jumpTimer++;
		}
		world.gameLoop(1);
		world.gameLoop(1);
		while (p.getY() < 26) {
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertTrue(p.getVelY() > 0);
			jumpTimer++;
			world.gameLoop(1);
		}

		// Test jump time < 1000 and jump -> standing
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertEquals(0, p.getVelY(), EPSILON);
		assertTrue(jumpTimer < p.getJumpDuration());
	}

	/**
	 * Tests wall sliding on the left had side
	 */
	@Test
	public void testWallSlideLeft() {
		loadWorldWithPlayer(1, 22);
		assertEquals(EntityState.STANDING, p.getCurrentState());

		InputManager.setTestValue(GameAction.MOVE_LEFT, true);

		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());

		int jumpTimer = 0;
		// wait until the player is mid jump
		for (int i = 0; i < (p.getJumpDuration() / 2 - 1); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertTrue(p.getVelY() < 0);
			jumpTimer++;
		}
		world.gameLoop(1);
		world.gameLoop(1);
		jumpTimer++;
		while (p.getY() < 22) {
			assertEquals(EntityState.WALL_SLIDING, p.getCurrentState());
			assertTrue(p.getVelY() > 0);
			jumpTimer++;
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertTrue(jumpTimer > p.getJumpDuration());
	}

	/**
	 * Tests exiting the wall sliding state when user input does not point into the wall
	 */
	@Test
	public void testWallSlideReleaseLeft() {
		loadWorldWithPlayer(1, 22);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());

		int jumpTimer = 0;
		// wait until the player is mid jump
		for (int i = 0; i < (p.getJumpDuration() / 2 - 1); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertTrue(p.getVelY() < 0);
			jumpTimer++;
		}
		world.gameLoop(1);
		jumpTimer++;
		for (int i = 0; i < 100; i++) {
			world.gameLoop(1);
			assertTrue(p.getVelY() > 0);
			assertEquals(EntityState.WALL_SLIDING, p.getCurrentState());
			jumpTimer++;
		}
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		world.gameLoop(1);
		while (p.getY() < 22) {
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertTrue(p.getVelY() > 0);
			jumpTimer++;
			world.gameLoop(1);
		}
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertTrue(jumpTimer > p.getJumpDuration());
		assertTrue(jumpTimer < 1000);
	}

	/**
	 * Tests wall jumping and returning to the wall, with the wall on the left hand side
	 */
	@Test
	public void testWallJumpLeft() {
		loadWorldWithPlayer(1, 22);
		assertEquals(EntityState.STANDING, p.getCurrentState());

		InputManager.setTestValue(GameAction.MOVE_LEFT, true);

		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());

		// wait until the player is mid jump
		for (int i = 0; i < (p.getJumpDuration() / 2 - 1); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertTrue(p.getVelY() < 0);
		}
		world.gameLoop(1);
		for (int i = 0; i < 100; i++) {
			world.gameLoop(1);
			assertEquals(EntityState.WALL_SLIDING, p.getCurrentState());
			assertTrue(p.getVelY() > 0);
		}

		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() < 0);
		assertEquals(2 * p.getMoveSpeed(), p.getVelX(), EPSILON);

		for (int i = 0; i < p.getWallJumpDuration() - 1; i++) {
			world.gameLoop(1);
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			assertTrue(p.getVelY() < 0);
			assertEquals(2 * p.getMoveSpeed(), p.getVelX(), EPSILON);
		}
		world.gameLoop(1);
		while (p.getVelY() < 0) {
			assertEquals(EntityState.JUMPING, p.getCurrentState());
			world.gameLoop(1);
		}
		world.gameLoop(1);
		assertEquals(EntityState.WALL_SLIDING, p.getCurrentState());
	}

	/**
	 * Test that the player exits the dashing state when they collide with a wall
	 */
	@Test
	public void testDashLeftIntoWall() {
		loadWorldWithPlayer(2, 29);
		p.addMovementSkill(SkillList.DASH);
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);

		// Test standing -> dashing
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		assertEquals(EntityState.DASHING, p.getCurrentState());
		assertEquals(-p.getDashSpeed(), p.getVelX(), EPSILON);

		// Test dash duration -> standing
		InputManager.setTestValue(GameAction.DASH, false);
		int dashTimer = 0;
		while (p.getX() > 1) {
			assertEquals(EntityState.DASHING, p.getCurrentState());
			assertEquals(-p.getDashSpeed(), p.getVelX(), EPSILON);
			dashTimer++;
			world.gameLoop(1);
		}
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertTrue(dashTimer < (int) Math.ceil(p.getDashDuration()));
		// Account for 1 frame delay in becoming still
		world.gameLoop(1);
		assertEquals(0, p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player exits the sliding state when they collide with a wall
	 */
	@Test
	public void testSlideLeftIntoWall() {
		loadWorldWithPlayer(3, 29);
		p.addMovementSkill(SkillList.DASH);
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);

		// Test standing -> dashing
		InputManager.setTestValue(GameAction.SLIDE, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		assertEquals(EntityState.SLIDING, p.getCurrentState());
		assertEquals(-p.getSlideSpeed(), p.getVelX(), EPSILON);

		// Test dash duration -> standing
		InputManager.setTestValue(GameAction.SLIDE, false);
		int slideTimer = 0;
		while (p.getX() > 1) {
			assertEquals(EntityState.SLIDING, p.getCurrentState());
			assertEquals(-p.getSlideSpeed(), p.getVelX(), EPSILON);
			slideTimer++;
			world.gameLoop(1);
		}
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertTrue(slideTimer < (int) Math.ceil(p.getSlideDuration()));
		// Account for 1 frame delay in becoming still
		world.gameLoop(1);
		assertEquals(0, p.getVelX(), EPSILON);
	}

	/**
	 * Test that the player can run left and run right
	 */
	@Test
	public void testRunBothDirections() {
		loadWorldWithPlayer(2, 29);
		p.addMovementSkill(SkillList.SPRINT);
		//STANDING and left input => move left
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);

		//Test that the running fails when left is pressed too quickly
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		world.gameLoop(1);
		for (int i = 0; i < 50; i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);

		//wait until the time interval has passed then move left again
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		for (int i = 0; i < 5; i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		InputManager.setDoubleKeyTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(true, InputManager.getDoublePressed(GameAction.MOVE_LEFT));
		assertEquals(EntityState.SPRINTING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed() * 2, p.getVelX(), EPSILON);

		//test that the running fails when left transitions to right
		InputManager.setDoubleKeyTestValue(GameAction.MOVE_LEFT, false);

		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		//test that the running fails when right is pressed too quickly
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);
		world.gameLoop(1);
		for (int i = 0; i < 50; i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		//wait until time interval have passed by and move right again
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);
		for (int i = 0; i < 5; i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		InputManager.setDoubleKeyTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(true, InputManager.getDoublePressed(GameAction.MOVE_RIGHT));
		assertEquals(EntityState.SPRINTING, p.getCurrentState());
		assertEquals(p.getMoveSpeed() * 2, p.getVelX(), EPSILON);
	}
	
	@Test
	public void testCrouch() {
		loadWorldWithPlayer(2, 29);
		
		//standing -> crouching
		assertEquals(EntityState.STANDING, p.getCurrentState());
		world.gameLoop(1);
		world.gameLoop(1);
		world.gameLoop(1);
		world.gameLoop(1);
		world.gameLoop(1);

		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		world.gameLoop(1);
		world.gameLoop(1);
		world.gameLoop(1);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
		
		InputManager.setTestValue(GameAction.MOVE_DOWN, false);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
	}
	
	@Test
	public void testCrouchMovingSpeedReduction() {
		loadWorldWithPlayer(2, 29);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
		assertEquals(p.getMoveSpeed() * 0.8f, p.getVelX(), EPSILON);
		
		for (int i = 0; i < 50; i++) {
			assertEquals(EntityState.CROUCHING, p.getCurrentState());
			world.gameLoop(1);
		}
		
		assertEquals(0f, p.getVelX(), EPSILON);
	}
	
	@Test
	public void testCrouchMovingAndBack() {
		loadWorldWithPlayer(2, 29);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
		assertEquals(p.getMoveSpeed() * 0.8f, p.getVelX(), EPSILON);
		
		for (int i = 0; i < 10; i++) {
			assertEquals(EntityState.CROUCHING, p.getCurrentState());
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_DOWN, false);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.MOVING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);
	}

	
	@Test
	public void testAirCrouch() {
		loadWorldWithPlayer(2, 10);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
		
		InputManager.setTestValue(GameAction.MOVE_DOWN, false);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
	}
	
	@Test
	public void testCrouchToAirCrouch() {
		loadWorldWithPlayer(2, 29);
		
		//standing -> crouching
		assertEquals(EntityState.STANDING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
		
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
		
		InputManager.setTestValue(GameAction.JUMP, false);
		for (int i = 0; i < p.getJumpDuration() - 1; i++) {
			assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
			world.gameLoop(1);
		}
		assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
	}
	
	@Test
	public void testCrouchToSlide() {
		loadWorldWithPlayer(2, 29);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
		
		InputManager.setTestValue(GameAction.SLIDE, true);
		world.gameLoop(1);
		assertEquals(EntityState.SLIDING, p.getCurrentState());

		for (int i = 0; i < (int) p.getSlideDuration(); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.SLIDING, p.getCurrentState());
			assertEquals(p.getSlideSpeed(), p.getVelX(), EPSILON);
		}
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
	}
	
	@Test
	public void testCrouchToSlideUsingDashButton() {
		loadWorldWithPlayer(2, 29);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
		
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		assertEquals(EntityState.SLIDING, p.getCurrentState());

		for (int i = 0; i < (int) p.getSlideDuration(); i++) {
			world.gameLoop(1);
			assertEquals(EntityState.SLIDING, p.getCurrentState());
			assertEquals(p.getSlideSpeed(), p.getVelX(), EPSILON);
		}
		world.gameLoop(1);
		assertEquals(EntityState.CROUCHING, p.getCurrentState());
	}

	@Test 
	public void testAirCrouchToDash() {
		loadWorldWithPlayer(2, 10);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		InputManager.setTestValue(GameAction.MOVE_DOWN, true);
		world.gameLoop(1);
		assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
		
		p.addMovementSkill(SkillList.DASH);
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.DASH, false);
		
		assertEquals(p.getDashSpeed(), p.getVelY(), EPSILON);
		assertEquals(0, p.getVelX(), EPSILON);
		world.gameLoop(1);

		// Test AirDash into ground -> standing
		for(int i = 0; i < p.getDashDuration(); i++) {
			assertEquals(EntityState.AIR_DASHING, p.getCurrentState());
			assertEquals(p.getDashSpeed(), p.getVelY(), EPSILON);
			assertEquals(0, p.getVelX(), EPSILON);
			world.gameLoop(1);
		}

		assertEquals(EntityState.JUMPING, p.getCurrentState());
		world.gameLoop(1);
		assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
	}
	
	@Test
	public void testKnockBack() {
		loadWorldWithPlayer(2, 29);
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);

		BaseNPC rhinoNPC = new RhinoNPC();
		world.addEntity(rhinoNPC);
		world.gameLoop(1);
		rhinoNPC.setPosition(4, 29);
		
		for (int i = 0; i < 52; i++) {
			assertEquals(EntityState.MOVING, p.getCurrentState());
			world.gameLoop(1);
		}
		
		assertEquals(EntityState.KNOCK_BACK, p.getCurrentState());
	
		for (int i = 0; i < 251; i++) {
			assertEquals(EntityState.KNOCK_BACK, p.getCurrentState());
			world.gameLoop(1);
		}
		
		assertEquals(EntityState.MOVING, p.getCurrentState());
	}
	
	@Test
	public void testTerminalVelocity() {
		loadWorldWithPlayer(100, 0);
		p.setVelocity(0, p.terminalVelocity - 0.5f);
		for (int i = 0; i < 15; i++) {
			assertEquals(true, p.getVelY() < p.terminalVelocity + EPSILON);
			world.gameLoop(1);
		}
	}
	
	/**
	 * Test to check if the player is currently in a world full of water
	 */
	@Test
	public void underWaterTest() {
		loadWaterWorld(10, 10);
		world.gameLoop(1);
		assertEquals(true, p.isUnderLiquid());
	}
	
	/**
	 * Test to check if the player has dropped into the water after free falling from air to water.
	 */
	@Test
	public void isUnderWaterAfterFreeFallingFromAir(){
		loadWaterWorld(10, -3);
		world.gameLoop(1);
		assertEquals(false, p.isUnderLiquid());
		for (int i = 0; i < 500; i++) {
			world.gameLoop(1);
		}
		assertEquals(true, p.isUnderLiquid());
	}
	
	/**
	 * Test if the Entity is able to jump and Double dump in the water
	 */
	@Test
	public void testPlayerMovementUnderWaterJumping(){
		loadWaterWorld(10, 50);
		//Test that the Character is Jumping
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() < 0);
		//Test if the Character is dropping down
		for (int i = 0; i < p.getJumpDuration(); i++) {
			world.gameLoop(1);
		}
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() > 0);
		//Test that the character is able to jump again
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertTrue(p.getVelY() < 0);
	}
	
	/**
	 * Test if the Entity is able to move Left and Right in the water.
	 */
	@Test
	public void testPlayerMovementUnderWaterRightAndLeft() {
		loadWaterWorld(10,30);
		//STANDING and left input => move left
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);

		//Test that the running fails when left is pressed too quickly
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		world.gameLoop(1);
		for (int i = 0; i < 50; i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(-p.getMoveSpeed(), p.getVelX(), EPSILON);

		//wait until the time interval has passed then move left again
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		for (int i = 0; i < 5; i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);

		//test that the running fails when left transitions to right
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);

		//test that the running fails when right is pressed too quickly
		InputManager.setTestValue(GameAction.MOVE_RIGHT, false);
		world.gameLoop(1);
		for (int i = 0; i < 50; i++) {
			world.gameLoop(1);
		}
		InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		assertEquals(p.getMoveSpeed(), p.getVelX(), EPSILON);
	}
	
	/**
	 * Initialises a test environment with a world and a player inserted at the input position
	 */
	private void loadWorldWithPlayer(float posX, float posY) {
		InputManager.clearAllValues();
		world.debugReset();
		if (p == null) {
			p = new Player();
		}
		world.addEntity(p);
		world.gameLoop(1);
		p.setPosition(posX, posY);
		world.setTiles(standardTiles);
		world.gameLoop(1);
		world.gameLoop(1);
	}
	
	/**
	 * Initialises a test environment with a world filled with water and a player inserted at the input position
	 */
	private void loadWaterWorld(float posX, float posY) {
		InputManager.clearAllValues();
		world.debugReset();
		if (p == null) {
			p = new Player();
		}
		world.addEntity(p);
		world.gameLoop(1);
		p.setPosition(posX, posY);
		world.setTiles(waterTiles);

		world.gameLoop(1);
		world.gameLoop(1);
	}


	/**
	 * Turns the player p to face left and tests that this is successful.
	 */
	private void turnLeft() {
		// Turn to face left
		InputManager.setTestValue(GameAction.MOVE_LEFT, true);
		world.gameLoop(1);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.MOVE_LEFT, false);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(EntityState.STANDING, p.getCurrentState());
		assertEquals(0, p.getVelX(), EPSILON);
		assertEquals(-1, p.getFacing());
	}

	private void jump() {
		InputManager.setTestValue(GameAction.JUMP, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.JUMP, false);
		assertEquals(EntityState.JUMPING, p.getCurrentState());

		// wait until the player is mid jump
		for (int i = 0; i < p.getJumpDuration() / 2; i++) {
			world.gameLoop(1);
		}
	}

	private void airDash() {
		p.addMovementSkill(SkillList.DASH);
		assertEquals(EntityState.JUMPING, p.getCurrentState());
		InputManager.setTestValue(GameAction.DASH, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.DASH, false);
	}
}