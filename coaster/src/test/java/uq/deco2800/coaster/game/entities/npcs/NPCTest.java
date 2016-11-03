package uq.deco2800.coaster.game.entities.npcs;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.weapons.CustomBullet;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

import static org.junit.Assert.*;

public class NPCTest {
	private class TestNPC extends TestingNPC {
		public TestNPC() {
			super();
		}

		@Override
		protected void onDeath(Entity cause) {
			super.onDeath();
		}

		protected void forceKill() {
			Entity emptyEntity = new Entity() {
				@Override
				protected void tick(long ms) {

				}

				@Override
				protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {

				}

				@Override
				protected void onTerrainCollide(int tilex, int tiley, Side side) {

				}

				@Override
				protected void onDeath(Entity cause) {

				}
			};
			super.onDeath(emptyEntity);
		}

		protected void forceTick() {
			super.tick(1);
		}

		protected void onTerrainCollide(int tileX, int tileY, Side side) {
			super.onTerrainCollide(tileX, tileY, side);
		}

		@Override
		protected void tick(long ms) {
			stateUpdate(ms);
			checkStateUpdate();
			patrolArea();
			ableToJump();
		}
	}

	@Before
	public void init() {
		TestHelper.load();
		TestHelper.manualSoundLoad();
	}

	@Test
	public void testBaseNPC() {
		// Create World
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);
		testWorld.gameLoop(1); // Start the game

		// Create NPC
		TestNPC testNPC = new TestNPC();
		testWorld.addEntity(testNPC);

		// Add Player to test BaseNPC.onDeath() adding to player stats
		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1); // Load game
		assertEquals(false, testWorld.getPlayerEntities().isEmpty()); // Check
																		// player

		// Test basic attributes
		assertEquals(0, testNPC.getCurrentHealth());
		assertEquals(0, testNPC.getJumpSpeed());
		assertEquals(0, testNPC.getBaseDamage());
		assertNull(testNPC.getCurrentTarget());

		// Test health and damage
		testNPC.setCurrentHealth(10);
		assertEquals(1, testNPC.receiveDamage(1, testPlayer));
		assertEquals(9, testNPC.getCurrentHealth());
		testNPC.receiveDamageForce(1);
		assertEquals(8, testNPC.getCurrentHealth());
		testNPC.setCurrentHealth(0);
		testNPC.receiveDamageForce(1);
		assertEquals(-1, testNPC.getCurrentHealth());
		testNPC.setCurrentHealth(0);
		testNPC.receiveDamage(1, testPlayer);
		assertEquals(-1, testNPC.getCurrentHealth());

		// Test vision
		testNPC.target = (Player) testNPC.determinePriorityTarget();
		assertFalse(testNPC.isBlockedVision());

		// Test Stunned State
		testNPC.setState(EntityState.STUNNED, 3);
		assertEquals(0, testNPC.getMoveSpeedHor());
		testWorld.gameLoop(1);
		assertEquals(EntityState.STUNNED, testNPC.getCurrentState());

		// Test death from player
		GenericBullet testBullet = new GenericBullet(testPlayer, 0, 0, 0, 0, SpriteList.EMPTY);
		testNPC.onDeath(testBullet);
		assertEquals(false, testWorld.getNpcEntities().contains(testNPC)); // See
																			// if
																			// exists
																			// in
																			// world
	}

	@Test
	public void testTestingNPC() {
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		TestNPC testNPC = new TestNPC();
		testWorld.addEntity(testNPC);

		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1); // Load game

		// onDeath
		testWorld.addEntity(testPlayer);
		assertEquals(1, testWorld.getNpcEntities().size());
		testNPC.forceKill();
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());

		// Test protected methods
		testNPC.forceTick();
		testNPC.determineNextAction();
		testNPC.determinePriorityTarget();
		testNPC.maxHealth();
		testNPC.onTerrainCollide(0, 0, null);
	}

	@Test
	public void testBatNPC() {
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		BatNPC batNPC = new BatNPC();

		// Base stats
		assertEquals(100, batNPC.getCurrentHealth());
		assertEquals(2, batNPC.getMoveSpeedHor());
		assertEquals(0, batNPC.getMoveSpeedVer());
		assertEquals(null, batNPC.getCurrentState());

		// Test decision making
		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.addEntity(batNPC);

		testPlayer.setPosition(batNPC.getX(),batNPC.getY());
		testWorld.gameLoop(1);
		batNPC.determineNextAction();
		assertFalse(testWorld.getAllEntities().size() == 3);
		batNPC.determineNextAction();
		testPlayer.setPosition(batNPC.getX()+100,batNPC.getY()+100);
		testWorld.gameLoop(1);
		batNPC.determineNextAction();
		assertTrue(testWorld.getAllEntities().get(2) instanceof GenericBullet);

		// Test Boss
		batNPC.setBoss();
		assertTrue(batNPC.isBoss());

		// onDeath
		batNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}

	@Test
	public void testEyeballNPC(){
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		EyeballNPC eyeballNPC = new EyeballNPC();

		// Base stats
		assertEquals(100, eyeballNPC.getCurrentHealth());
		assertEquals(2, eyeballNPC.getMoveSpeedHor());
		assertEquals(0, eyeballNPC.getMoveSpeedVer());
		assertEquals(null, eyeballNPC.getCurrentState());

		// Test decision making
		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.addEntity(eyeballNPC);

		testPlayer.setPosition(eyeballNPC.getX(),eyeballNPC.getY());
		testWorld.gameLoop(1);
		eyeballNPC.determineNextAction();
		assertFalse(testWorld.getAllEntities().size() == 3);
		eyeballNPC.determineNextAction();
		testPlayer.setPosition(eyeballNPC.getX()+100,eyeballNPC.getY()+100);
		testWorld.gameLoop(1);
		eyeballNPC.determineNextAction();
		assertTrue(testWorld.getAllEntities().get(2) instanceof GenericBullet);

		// Test Boss
		eyeballNPC.setBoss();
		assertTrue(eyeballNPC.isBoss());

		// onDeath
		eyeballNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}

	@Test
	public void testIceSpiritNPC(){
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		IceSpiritNPC iceSpiritNPC = new IceSpiritNPC();

		// Base stats
		assertEquals(100, iceSpiritNPC.getCurrentHealth());
		assertEquals(2, iceSpiritNPC.getMoveSpeedHor());
		assertEquals(0, iceSpiritNPC.getMoveSpeedVer());
		assertEquals(null, iceSpiritNPC.getCurrentState());

		// Test decision making
		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.addEntity(iceSpiritNPC);

		testPlayer.setPosition(iceSpiritNPC.getX(), iceSpiritNPC.getY());
		testWorld.gameLoop(1);
		iceSpiritNPC.determineNextAction();
		assertFalse(testWorld.getAllEntities().size() == 3);
		iceSpiritNPC.determineNextAction();
		testPlayer.setPosition(iceSpiritNPC.getX()+100,iceSpiritNPC.getY()+100);
		testWorld.gameLoop(1);
		iceSpiritNPC.determineNextAction();
		assertTrue(testWorld.getAllEntities().get(2) instanceof CustomBullet);
		testPlayer.setPosition(iceSpiritNPC.getX(), iceSpiritNPC.getY());

		// onDeath
		iceSpiritNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());

		IceSpiritNPC iceSpiritNPCBoss = new IceSpiritNPC();
		iceSpiritNPCBoss.setBoss();
		testWorld.addEntity(iceSpiritNPCBoss);
		testWorld.gameLoop(1);
		iceSpiritNPCBoss.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}

	@Test
	public void testFlyingNPC(){
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		FlyingNPC flyingNPC = new FlyingNPC();
		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.addEntity(flyingNPC);
		flyingNPC.setPosition(testPlayer.getX()+1, testPlayer.getY()-1);
		testWorld.gameLoop(1);
		flyingNPC.target = flyingNPC.determinePriorityTarget();

		// Test flapping
		float oldVelX = flyingNPC.getVelX();
		flyingNPC.flap();
		testWorld.gameLoop(1);
		assertFalse(oldVelX == flyingNPC.getVelX());
		flyingNPC.hover();
		testWorld.gameLoop(1);
		assertFalse(oldVelX == flyingNPC.getVelX());
		oldVelX = flyingNPC.getVelX();
		flyingNPC.setPosition(flyingNPC.getX(), testPlayer.getY()+2);
		flyingNPC.hover();
		testWorld.gameLoop(1);
		assertTrue(oldVelX == flyingNPC.getVelX());

		// Facing direction
		testPlayer.setPosition(1,0);
		flyingNPC.setPosition(2,0);
		testWorld.gameLoop(1);
		flyingNPC.chooseFacing();
		testWorld.gameLoop(1);
		assertEquals(1, flyingNPC.getFacing());

		testPlayer.setPosition(3,0);
		flyingNPC.setPosition(2,0);
		testWorld.gameLoop(1);
		flyingNPC.chooseFacing();
		testWorld.gameLoop(1);
		assertEquals(-1, flyingNPC.getFacing());

		// onDeath
		flyingNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());

	}

	@Test
	public void testGolemNPC(){
		GolemNPC golemNPC = new GolemNPC();

		// Base stats
		assertEquals(1000, golemNPC.getCurrentHealth());
		assertEquals(2, golemNPC.getMoveSpeedHor());
		assertEquals(0, golemNPC.getMoveSpeedVer());
		assertEquals(25, golemNPC.getBaseDamage());
	}

	@Test
	public void testRangedEnemyNPC() {
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		RangedEnemyNPC rangedNPC = new RangedEnemyNPC();
		assertEquals(100, rangedNPC.getCurrentHealth());
		assertEquals(2, rangedNPC.getMoveSpeedHor());
		assertEquals(0, rangedNPC.getMoveSpeedVer());
		assertEquals(null, rangedNPC.getCurrentState());

		// Test firing logic
		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testWorld.addEntity(rangedNPC);
		for (int i = 0; i < 42; i++){
			testWorld.gameLoop(1);
		}
		assertTrue(testWorld.getAllEntities().get(2) instanceof GenericBullet);

		// Facing direction
		testPlayer.setPosition(1,0);
		rangedNPC.setPosition(2,0);
		rangedNPC.determineFacingDirection(testPlayer, true);
		assertEquals(1, rangedNPC.getFacing());
		rangedNPC.determineFacingDirection(testPlayer, false);
		assertEquals(-1, rangedNPC.getFacing());
		testPlayer.setPosition(3,0);
		rangedNPC.setPosition(2,0);
		rangedNPC.determineFacingDirection(testPlayer, true);
		assertEquals(-1, rangedNPC.getFacing());
		rangedNPC.determineFacingDirection(testPlayer, false);
		assertEquals(1, rangedNPC.getFacing());

		// Test Boss
		rangedNPC.setBoss();
		assertTrue(rangedNPC.isBoss());

		// onDeath
		rangedNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}

	@Test
	public void testTreasureNPC() {
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);
		assertEquals(0, testWorld.getAllEntities().size());

		TreasureNPC treasureNPC = new TreasureNPC();

		assertEquals(100, treasureNPC.getCurrentHealth());
		assertEquals(2, treasureNPC.getMoveSpeedHor());
		assertEquals(0, treasureNPC.getMoveSpeedVer());
		assertEquals(null, treasureNPC.getCurrentState());

		treasureNPC.selfDestruct();
		assertEquals(0, treasureNPC.getMoveSpeedHor());
		testWorld.gameLoop(30);
		assertEquals(false, testWorld.getAllEntities().contains(treasureNPC));
	}

	@Test
	public void testRhinoNPC() {
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		RhinoNPC rhinoNPC = new RhinoNPC();
		Player testPlayer = new Player();

		assertEquals(1000, rhinoNPC.getCurrentHealth());
		assertEquals(1, rhinoNPC.getMoveSpeedHor());
		assertEquals(0, rhinoNPC.getMoveSpeedVer());
		assertEquals(EntityState.DEFAULT, rhinoNPC.getCurrentState());

		testWorld.addEntity(rhinoNPC);
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1);
		rhinoNPC.tick(1);

		// Test Boss
		rhinoNPC.setBoss();
		assertTrue(rhinoNPC.isBoss());

		// onDeath
		rhinoNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}

	@Test
	public void testMeleeEnemyNPC() {
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		MeleeEnemyNPC meleeNPC = new MeleeEnemyNPC();
		Player testPlayer = new Player();

		assertEquals(100, meleeNPC.getCurrentHealth());
		assertEquals(2, meleeNPC.getMoveSpeedHor());
		assertEquals(0, meleeNPC.getMoveSpeedVer());

		// Test interactions
		testWorld.addEntity(meleeNPC);
		testWorld.addEntity(testPlayer);
		meleeNPC.setPosition(testPlayer.getX()+1,testPlayer.getY()-2);
		for (int i = 0; i < 2; i++){
			testWorld.gameLoop(1);
		}

		// onDeath
		meleeNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}

	@Test
	public void testExplosionBunnyNPC(){
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		ExplosionBunnyNPC explosionBunnyNPC = new ExplosionBunnyNPC();
		Player testPlayer = new Player();

		testWorld.addEntity(explosionBunnyNPC);
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1);
		explosionBunnyNPC.setPosition(testPlayer.getX()+1,testPlayer.getY()-2);
		testWorld.gameLoop(1);
		explosionBunnyNPC.determineNextAction();
	}

	@Test
	public void testSkeletonNPC() {
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		SkeletonNPC skeletonNPC = new SkeletonNPC();
		Player testPlayer = new Player();

		assertEquals(100, skeletonNPC.getCurrentHealth());
		assertEquals(2, skeletonNPC.getMoveSpeedHor());
		assertEquals(0, skeletonNPC.getMoveSpeedVer());

		// Test interactions
		testWorld.addEntity(skeletonNPC);
		testWorld.addEntity(testPlayer);
		skeletonNPC.setPosition(testPlayer.getX()+1,testPlayer.getY()-2);
		for (int i = 0; i < 2; i++){
			testWorld.gameLoop(1);
		}

		// Test Boss
		skeletonNPC.setBoss();
		assertTrue(skeletonNPC.isBoss());

		// onDeath
		skeletonNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}

	@Test
	public void testRatNPC(){
		World testWorld = World.getInstance();
		testWorld.debugReset();
		testWorld.setNpcGenEnabled(false);

		RatNPC ratNPC = new RatNPC();
		Player testPlayer = new Player();

		testWorld.addEntity(ratNPC);
		testWorld.addEntity(testPlayer);
		testWorld.gameLoop(1);

		ratNPC.determineNextAction();

		// Self destruct
		ratNPC.selfDestruct();
		ratNPC.onDeath(testPlayer);
		testWorld.gameLoop(1);
		assertEquals(0, testWorld.getNpcEntities().size());
	}
}
