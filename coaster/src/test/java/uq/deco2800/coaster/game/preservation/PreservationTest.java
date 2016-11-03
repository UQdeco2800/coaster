package uq.deco2800.coaster.game.preservation;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Decoration;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.TestingNPC;
import uq.deco2800.coaster.game.entities.particles.ParticleSource;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by rcarrier on 13/08/2016.
 */
public class PreservationTest {
	private static Logger logger = LoggerFactory.getLogger(PreservationTest.class);
	private static final Random random = new Random();
	private String testSaveLocation = "tmp/test/";
	String saveFileLocation = testSaveLocation + "save.json";
	private World world = World.getInstance();

	@Test
	public void testDecoration() {
		init();
		Decoration d = new Decoration(new Sprite(SpriteList.TREE_PALM));//tree
		world.addEntity(d);
		world.gameLoop(1);
		ExportableWorld got = saveLoad();
		assertEqual(got.entities.get(0), d);
	}

	@Test
	public void testItems() {
		init();
		//rcarrier, todo: a better job
		ItemRegistry.itemList().forEach(s ->testItem(ItemRegistry.getItem(s)));
		cleanup();
	}

	private void testItem(Item item) {
		init();
		ItemEntity ie = new ItemEntity(item);
		ie.setPosition(random.nextFloat(), random.nextFloat());
		ie.setRemoveDelay(random.nextLong());
		world.addEntity(ie);
		world.gameLoop(1);
		ExportableWorld got = saveLoad();
		assertEqual(got.entities.get(0), ie);
	}

	@Test
	public void testParticle() {
		init();
		ParticleSource.addParticleSource(0f, 0f);
		world.gameLoop(1);
		ExportableWorld got = saveLoad();
		//-1 for player
		assert got.entities.size() < world.getAllEntities().size() - 1;
		world.loadEntities(got.entities);
		assert got.entities.size() == world.getAllEntities().size();
	}

	@Test
	public void testEntity() {
		init();
		TestingNPC e = new TestingNPC();
		e.maxHealth();
		e.receiveDamageForce(1);
		e.setPosition(10f, 10f);
		world.addEntity(e);
		world.gameLoop(1);
		ExportableWorld got = saveLoad();
		equalOrFail(got.entities.size(), 1);
		assertEqual(got.entities.get(0), e);
	}

//	@Test
//	public void testMixedAmounts() {
//		init();
//		Player p = new Player();
//		world.addEntity(p);
//		world.gameLoop(1);
//
//		TestingNPC e1 = new TestingNPC();
//		TestingNPC e2 = new TestingNPC();
//		TestingNPC e3 = new TestingNPC();
//
//		Decoration d = new Decoration(new Sprite(SpriteList.TALL_TREE));//tree
//
//		e2.maxHealth();
//		e2.receiveDamageForce(1);
//		e2.setPosition(10f, 10f);
//		world.addEntity(e1);
//		world.addEntity(e2);
//		world.addEntity(e3);
//		world.addEntity(d);
//		//add the entities with a single game loop
//		world.gameLoop(1);
//		ExportableWorld got = saveLoad();
//		int totalDecorations = 0;
//		for (ExportableEntity ee : got.entities) {
//			if (ee.decoration) {
//				totalDecorations++;
//			}
//		}
//		equalOrFail(totalDecorations, 1);
//		equalOrFail(got.playerEntities.size(), 1);
//		equalOrFail(got.entities.size(), 4);
//	}

	@Test
	public void testPlayer() {
		init();
		Player p = new Player();
		world.addEntity(p);
		world.gameLoop(1);
		ExportableWorld got = saveLoad();
		assert got.entities.size() == 0;
		assert got.playerEntities.size() == 1;
		assertEqual(got.playerEntities.get(0), p);
	}

	@Test
	public void testEmpty() {
		init();
		ExportableWorld got = saveLoad();
		assert got.entities.size() == 0 && got.playerEntities.size() == 0;
	}

	@Test
	public void testPlayerNotLoaded() {
		init();
		try {
			world.loadPlayer(new ExportablePlayer());
			logger.debug("Player not loaded, exception should be thrown");
			assert false;
		} catch (IllegalStateException e) {
			logger.debug("Exception caught correctly");
			assert true;//useless I know but pretty
		}
	}

	@Test
	public void testNewFile() {
		init();
		File file = new File(saveFileLocation);
		assert !file.exists();
		Preservation.save(saveFileLocation);
		assert file.exists();
	}

	@Test
	public void testNullSprites() {
		//Test not done, can't figure out how to make null sprite
		init();

		Player p = new Player();
		TestingNPC e = new TestingNPC();
		Decoration d = new Decoration(new Sprite(SpriteList.TREE_DEAD));//tree

		world.addEntity(p);
		world.addEntity(e);
		world.addEntity(d);
		//add the entities with a single game loop
		world.gameLoop(1);

		equalOrFail(world.getAllEntities().size(), 3);
		ExportableWorld got = saveLoad();
		//+1 for player
		equalOrFail(world.getAllEntities().size(), got.entities.size() + 1);
		world.debugReset();
		//d = new Decoration(new Sprite(SpriteList.NULL));//tree
		world.addEntity(p);
		world.addEntity(e);
		world.addEntity(d);
		//add the entities with a single game loop
		world.gameLoop(1);

		equalOrFail(world.getAllEntities().size(), 3);
		got = saveLoad();
		//+1 for player
		//-1 for Decoration null sprite
		//equalOrFail(world.getAllEntities().size() - 1, got.entities.size() + 1);
	}


	private ExportableWorld saveLoad() {
		Preservation.save(saveFileLocation);
		ExportableWorld got = Preservation.load(saveFileLocation);
		cleanup();
		return got;
	}

	/**
	 * Interface should be used in current state, but in future entity and player will have different things to compare
	 *
	 * @param ee the loaded entity
	 * @param e  the saved entity
	 */
	private void assertEqual(ExportableEntity ee, Entity e) {
		if (ee == null) {
			logger.debug("ExportableEntity null");
			assert false;
		}
		if (e == null) {
			logger.debug("Entity null");
			assert false;
		}
		equalOrFail(ee.posX, e.getX());
		equalOrFail(ee.posY, e.getY());
		equalOrFail(ee.decoration, e instanceof Decoration);
		if (e instanceof ItemEntity) {
			assertEqualItem((ExportableItem) ee, (ItemEntity) e);
		} else if (e instanceof BasicMovingEntity) {
			if (e instanceof Player) {
				assertEqualPlayer((ExportablePlayer) ee, (Player) e);
			} else {
				assertEqualMovingEntity((ExportableMovingEntity) ee, (BasicMovingEntity) e);
			}
		}

	}

	/**
	 * Compares loaded and saved moving entities
	 *
	 * @param eme the loaded moving entity
	 * @param me  the saved moving entity
	 */
	private void assertEqualMovingEntity(ExportableMovingEntity eme, BasicMovingEntity me) {
		equalOrFail(eme.currentHealth, me.getCurrentHealth());
		equalOrFail(eme.moveSpeed, me.getMoveSpeed());
		equalOrFail(eme.jumpSpeed, me.getJumpSpeed());

	}

	private void assertEqualItem(ExportableItem ee, ItemEntity e) {
		equalOrFail(ee.removeDelay, e.getRemoveDelay());
		equalOrFail(ee.itemReference, ItemRegistry.reverseLookup(e.getItem()));
	}

	/**
	 * Compares loaded and saved players
	 *
	 * @param ep the loaded player
	 * @param p  the saved player
	 */
	private void assertEqualPlayer(ExportablePlayer ep, Player p) {
		equalOrFail(ep.currentHealth, p.getCurrentHealth());
		equalOrFail(ep.moveSpeed, p.getMoveSpeed());
		equalOrFail(ep.jumpSpeed, p.getJumpSpeed());
		equalOrFail(ep.currentGold, p.getCommerce().getGold());
	}

	/**
	 * Checks if the two Objects are equal value, if not fails assertion.
	 *
	 * @param a object one
	 * @param b object two
	 */
	private void equalOrFail(Object a, Object b) {
		if (a instanceof Float && b instanceof Float) {
			assert (Float) a - (Float) b < 0.00001f;
		}
		if (a != b && !a.toString().equals(b.toString())) {
			String logString = "'";
			logString += a.toString();
			logString += "' != '";
			logString += b.toString();
			logger.debug(logString + "'");
			logger.info(logString + "'");
			assert false;
		}
	}

	/**
	 * Loads sprites, so they can be accessed by tiles to create a world.
	 */
	private void init() {
		cleanup();
		TestHelper.load();
		TestHelper.manualSoundLoad();
		world.debugReset();
	}


	/**
	 * cleans up the mess made by the tests
	 */
	private void cleanup() {
		File folder = new File(testSaveLocation);
		if (folder.exists()) {
			for (String file : folder.list()) {
				(new File(folder.getPath(), file)).delete();
			}
			folder.delete();
		}
	}
}
