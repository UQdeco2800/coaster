package uq.deco2800.coaster.game.entities.puzzle;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TerrainEntityTest {
		private static float EPSILON = 0.0002f;
		
		private World world = World.getInstance();
		private static Logger logger = LoggerFactory.getLogger(TerrainEntity.class);
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

		
		@Test
		public void testPushCrateRight() {
			loadWorldWithPlayer(2, 30);
			Crate crate = new Crate();
			world.addEntity(crate);
			world.gameLoop(1);
			crate.setPosition(3, 29);
			world.gameLoop(1);

			assertEquals(2, p.getX(), EPSILON);
			assertEquals(3, crate.getX(), EPSILON);
			
			InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
			
			for (int i = 0; i < 5; i++) {
				world.gameLoop(1);
			}
			assertEquals(true, p.getX() > 2);
			assertEquals(true, crate.getX() > 3);
			assertEquals(p.getBounds().right(), crate.getX(), 0.05);

		}
		
		@Test
		public void testPushCrateLeft() {
			loadWorldWithPlayer(4, 29);
			Crate crate = new Crate();
			world.addEntity(crate);
			world.gameLoop(1);
			crate.setPosition(3, 30);
			world.gameLoop(1);

			assertEquals(4, p.getX(), EPSILON);
			assertEquals(3, crate.getX(), EPSILON);
			
			InputManager.setTestValue(GameAction.MOVE_LEFT, true);
			
			for (int i = 0; i < 5; i++) {
				world.gameLoop(1);
			}
			assertEquals(true, p.getX() < 4);
			assertEquals(true, crate.getX() < 3);
			assertEquals(p.getX(), crate.getBounds().right(), 0.05);
		}
		
		@Test
		public void testStandOnCrate() {
			loadWorldWithPlayer(3, 27.9f);

			Crate crate = new Crate();
			world.addEntity(crate);
			world.gameLoop(1);
			crate.setPosition(3, 30);
			world.gameLoop(1);

			assertEquals(EntityState.JUMPING, p.getCurrentState());
			
			for (int i = 0; i < 60; i++) {
				assertEquals(EntityState.JUMPING, p.getCurrentState());
				world.gameLoop(1);
			}
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(p.getBounds().bottom(), crate.getY(), 0.05);
		}

		@Test
		public void testDropThroughPlatformHorizontal() {
			DropThroughPlatform platform = new DropThroughPlatform(1, 1);
			world.addEntity(platform);
			world.gameLoop(1);
			platform.setPosition(3, 29);
			
			loadWorldWithPlayer(2, 29);
			
			InputManager.setTestValue(GameAction.MOVE_RIGHT, true);
			for (int i = 0; i < 100; i++) {
				assertEquals(29, p.getY(), EPSILON);
				assertEquals(0, p.getOnWall());
				assertEquals(true, p.isOnGround());
				world.gameLoop(1);
			}
		}

		@Test
		public void testDropThroughPlatformVertical() {
			loadWorldWithPlayer(3, 29);

			DropThroughPlatform platform = new DropThroughPlatform(1, 1);
			world.addEntity(platform);
			world.gameLoop(1);
			platform.setPosition(3, 29);

			
			
			InputManager.setTestValue(GameAction.JUMP, true);
			world.gameLoop(1);
			InputManager.setTestValue(GameAction.JUMP, false);
			
			assertEquals(29, p.getY(), EPSILON);
			
			for (int i = 0; i < p.getJumpDuration() / 2; i++) {
				assertEquals(EntityState.JUMPING, p.getCurrentState());
				world.gameLoop(1);
			}
			
			assertTrue(p.getBounds().bottom() < platform.getBounds().top());
			
			while (p.getY() < 27) {
				assertEquals(EntityState.JUMPING, p.getCurrentState());
				world.gameLoop(1);
			}
			
			assertEquals(27, p.getY(), EPSILON);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			
			world.gameLoop(1);
			
			InputManager.setTestValue(GameAction.MOVE_DOWN, true);
			world.gameLoop(1);
			assertEquals(EntityState.CROUCHING, p.getCurrentState());
			
			world.gameLoop(1);
			world.gameLoop(1);
			world.gameLoop(1);
			assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
			assertTrue(p.getY() > 27);
		}
		
		@Test
		public void testDropThroughPlatformFalling() {
			DropThroughPlatform platform = new DropThroughPlatform(1, 1);
			world.addEntity(platform);
			world.gameLoop(1);
			platform.setPosition(3, 29);

			loadWorldWithPlayer(3, 26.9f);
			InputManager.setTestValue(GameAction.MOVE_DOWN, true);
			world.gameLoop(1);
			
			while (p.getY() < 27.2f) {
				assertEquals(EntityState.AIR_CROUCHING, p.getCurrentState());
				assertEquals(false, p.isOnGround());
				world.gameLoop(1);
			}
		}
		
		@Test
		public void testMovingPlatformHorizontal() {
			loadWorldWithPlayer(2, 28);
			
			MovingPlatform platform = new MovingPlatform(true);
			platform.setPath(2, 7, 30, 30, 100);
			
			world.addEntity(platform);
			world.gameLoop(1);
			
			platform.setPosition(2, 30);
			p.setPosition(2, 28);
			world.gameLoop(1);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(2, p.getX(), EPSILON);
			
			world.gameLoop(50);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(4.5, p.getX(), EPSILON);
		}
		
		@Test
		public void testMovingPlatformVertical() {
			loadWorldWithPlayer(2, 28);
			
			MovingPlatform platform = new MovingPlatform(true);
			platform.setPath(2, 2, 30, 28, 100);
			
			world.addEntity(platform);
			world.gameLoop(1);
			
			platform.setPosition(2, 30);
			p.setPosition(2, 28);
			world.gameLoop(1);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(true, p.isOnGround());
			assertEquals(28, p.getY(), 0.1);
			
			world.gameLoop(50);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(true, p.isOnGround());
			assertEquals(27, p.getY(), 0.1);
			
			world.gameLoop(50);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(true, p.isOnGround());
			assertEquals(26, p.getY(), 0.1);

			world.gameLoop(50);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(true, p.isOnGround());
			assertEquals(27, p.getY(), 0.1);

			world.gameLoop(50);
			assertEquals(EntityState.STANDING, p.getCurrentState());
			assertEquals(true, p.isOnGround());
			assertEquals(28, p.getY(), 0.1);
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
}
