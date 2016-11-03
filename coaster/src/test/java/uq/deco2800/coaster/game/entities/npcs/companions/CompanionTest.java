package uq.deco2800.coaster.game.entities.npcs.companions;

import org.junit.Test;

import java.util.List;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.PowerUp;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.MeleeEnemyNPC;
import uq.deco2800.coaster.game.entities.npcs.SkeletonNPC;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertTrue;

public class CompanionTest {
	private World world = World.getInstance();

	@Test
	/***
	 * Test for testing if the companion can be intialized in game world
	 */
	public void testCompanionLoad() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();
		world.addEntity(player);
		player.setPosition(0, 50);
		world.addEntity(testCompanion);
		world.gameLoop(10);
		assertTrue(world.getAllEntities().contains(testCompanion));

	}

	@Test
	/***
	 * Test to determine in companion moves on game tick in addition to testing
	 * companions teleport when far away from player
	 */
	public void testCompanionMovement() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();
		world.addEntity(player);
		player.setPosition(0, 21);
		world.addEntity(testCompanion);
		testCompanion.setPosition(0, 20);
		float loadPositionX = testCompanion.getX();
		world.gameLoop(1);
		// Testing Companion is in world
		assertTrue(world.getAllEntities().contains(testCompanion));
		// Testing companion spawn
		assert (loadPositionX == testCompanion.getX());
		player.setPosition(50, 0);
		world.gameLoop(3);
		// Testing companion teleport if it moves to far away
		assert (loadPositionX != testCompanion.getX());
		float newPosition = testCompanion.getX();
		world.gameLoop(100);
		assert (newPosition != testCompanion.getX());

	}

	@Test
	/***
	 * Test to check if determinePriorityTarget works Checks to see if companion
	 * can find its owner
	 */
	public void testDeterminePriorityPlayerTarget() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();

		player.setPosition(0, 20);
		testCompanion.setPosition(0, 18);

		world.addEntity(player);
		world.addEntity(testCompanion);

		Player currentTarget = testCompanion.determinePriorityTarget();
		assert (currentTarget = player) != null;

	}

	@Test
	/***
	 * Test to determine NPCEnemyTarget Finds closest BaseNPC instance in vision
	 * range
	 * 
	 */
	public void testDetmineNPCEnemyTarget() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();

		MeleeEnemyNPC testNPC = new MeleeEnemyNPC();

		testNPC.setPosition(0, 16);
		player.setPosition(0, 20);
		testCompanion.setPosition(0, 18);

		world.addEntity(testNPC);
		world.addEntity(player);
		world.addEntity(testCompanion);

		assert (testCompanion.determineNPCEnemyTarget() instanceof BaseNPC);
		assert (testCompanion.determineNPCEnemyTarget().getX() == testNPC.getX());
		assert (testCompanion.determineNPCEnemyTarget().getY() - testNPC.getY() <= 2.1f);

		testNPC.delete();
		assert (testCompanion.determineNPCEnemyTarget() == testCompanion);

	}

	@Test
	/***
	 * Test to determine if companion finds the nearest itemEntity
	 */
	public void testDeterminItemEntityTarget() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();
		Item healthItem = ItemRegistry.getItem("Health1");
		ItemEntity p1 = new ItemEntity(healthItem);
		testCompanion.setPosition(3, 14);
		world.addEntity(testCompanion);
		world.addEntity(p1);
		p1.setPosition(0, 14);
		assert (testCompanion.determineNPCItemTarget() == null);
		world.gameLoop(4);
		assert (testCompanion.determineNPCItemTarget() == p1);

	}

	@Test
	/***
	 * Test to determine the correct CompanionStyle is working
	 */
	public void testDetermineCurrentAI() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();

		world.addEntity(testCompanion);
		world.addEntity(player);

		testCompanion.setPosition(0, 15);
		player.setPosition(3, 15);

		world.gameLoop(2);
		assert (testCompanion.getCompanionClass() == 0);
		testCompanion.setCompanionClass();
		assert (testCompanion.getCompanionClass() == 1);
		testCompanion.setCompanionClass();
		assert (testCompanion.getCompanionClass() == 2);
		testCompanion.setCompanionClass();
		assert (testCompanion.getCompanionClass() == 0);

	}

	@Test
	/***
	 * Tests the companions ever so rare death
	 */
	public void rareDeath() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();

		world.addEntity(testCompanion);
		world.addEntity(player);
		world.gameLoop(2);
		assert (world.getAllEntities().contains(testCompanion));
		testCompanion.kill(player);
		world.gameLoop(4);
		assert (!(world.getAllEntities().contains(testCompanion)));

	}

	@Test
	/***
	 * Testing getter method for the companions owner
	 */
	public void testGetOwner() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();

		assert (testCompanion.getOwner() == player);
	}

	@Test
	/***
	 * Testing the companion can be upgraded
	 */
	public void testUpgrade() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();

		assert (testCompanion.getNPCLevel() == 1);
		testCompanion.upgradeCompanion();
		assert (testCompanion.getNPCLevel() == 2);
		testCompanion.upgradeCompanion();
		assert (testCompanion.getNPCLevel() == 3);
		testCompanion.upgradeCompanion();
		assert (testCompanion.getNPCLevel() == 3);

	}

	@Test
	/***
	 * Testing the companion can generate powerups
	 */
	public void testGeneratePowerUp() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();

		world.addEntity(testCompanion);
		world.addEntity(player);
		world.gameLoop(2);
		assert (world.getAllEntities().contains(testCompanion));
		assert (world.getAllEntities().contains(player));
		world.gameLoop(2);
		testCompanion.generatePowerUp();
		world.gameLoop(2);
		boolean passflag = false;
		List<Entity> entityWorld = world.getAllEntities();
		for (Entity e : entityWorld) {
			if (e instanceof PowerUp) {
				passflag = true;
				break;
			}
		}
		assert (passflag);
	}

	@Test
	/***
	 * Testing that NPC Companion damages instances of BaseNPC in the right a
	 * mode
	 * 
	 */
	public void testDamage() {
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		TestHelper.load();
		world.debugReset();
		SkeletonNPC attackingNPC = new SkeletonNPC();
		world.addEntity(attackingNPC);
		world.addEntity(testCompanion);
		world.addEntity(player);
		world.gameLoop(2);
		player.setPosition(10, 0);
		testCompanion.setPosition(10, 1);
		attackingNPC.setPosition(10, 0);
		assert (testCompanion.getCompanionClass() == 0);
		testCompanion.stateUpdate(2);
		world.gameLoop(2);
		world.gameLoop(2);
		assert (attackingNPC.getCurrentHealth() < attackingNPC.getMaxHealth());

	}

}
