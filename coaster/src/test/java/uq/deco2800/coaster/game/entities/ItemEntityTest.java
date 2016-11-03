package uq.deco2800.coaster.game.entities;

import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemEntityTest {

	private World world = World.getInstance();
//	Item testItem = new Item.Builder("test01", "TESTING", null, "Testing Item", ItemType.POWERUP).build();
	Item testItem = ItemRegistry.getItem("Health1");
	private Player p;
	private static WorldTiles tiles;

	@Before
	public void init() {
		TestHelper.load();
		if (tiles == null) {
			world.debugReset();
			tiles = TestHelper.getMobilityTestTiles();
		}
	}

	@Test
	public void isItemEntity() {
		ItemEntity testIE = new ItemEntity(testItem);
		assertTrue(testIE instanceof ItemEntity);
	}

	@Test
	public void hurtByProjectiles() {
		ItemEntity testIE = new ItemEntity(testItem);
		assertFalse(testIE.isHurtByProjectiles());
	}

	@Test
	public void correctSprite() {
		ItemEntity testIE = new ItemEntity(testItem);
		assertTrue(testIE.getSprite() == testItem.getSprite());
	}

	@Test
	public void correctBounds() {
		ItemEntity testIE = new ItemEntity(testItem);
		float posX = 1.0f;
		float posY = 1.0f;
		testIE.setPosition(posX, posY);
		AABB test = new AABB(posX, posY, 1.5f, 1.5F);
		assertEquals(test.ownerBottom(), testIE.getBounds().ownerBottom(), 0.00000001f);
		assertEquals(test.ownerTop(), testIE.getBounds().ownerTop(), 0.00000001f);
		assertEquals(test.ownerRight(), testIE.getBounds().ownerRight(), 0.00000001f);
		assertEquals(test.ownerLeft(), testIE.getBounds().ownerLeft(), 0.00000001f);
	}

//	@Test
//	public void OnCollision() {
//
//		ItemEntity testIE = new ItemEntity(testItem);
//		loadWorldWithPlayer(0, 8);
//		testIE.setPosition(0f, 8f);
//		world.addEntity(testIE);
//		assert (!world.getAllEntities().contains(testIE));
//
//	}

//	@Test
//	public void onTerrainCollide() {
//
//		ItemEntity testIE = new ItemEntity(testItem);
//
//		testIE.setPosition(35f, 0f);
//		world.addEntity(testIE);
//		world.gameLoop(1);
//		assert (world.getAllEntities().contains(testIE));
//		assert (testIE.getVelX() < 0.0001f);
//		assert (testIE.getVelY() < 0.0001f);
//	}

	@Test
	public void onTick() {
		ItemEntity testIE = new ItemEntity(testItem);
		ItemEntity aftertestIE = testIE;
		testIE.setPosition(0f, 0f);
		world.addEntity(testIE);
		world.gameLoop(1);
		assert (testIE == aftertestIE);

	}

	@Test
	public void selfRemove() {
		ItemEntity testIE = new ItemEntity(testItem);
		loadWorldWithPlayer(0, 0);
		testIE.setPosition(0f, 10f);
		world.gameLoop(12000);
		assert (!world.getAllEntities().contains(testItem));
	}

	@Test
	public void onDeath() {
		ItemEntity testIE = new ItemEntity(testItem);
		loadWorldWithPlayer(0, 0);
		testIE.setPosition(0f, 10f);
		world.addEntity(testIE);
		world.gameLoop(1);
		assert (world.getAllEntities().contains(testIE));
		testIE.kill(null);
		world.gameLoop(1);
		assert (!world.getAllEntities().contains(testIE));
	}

	@Test
	public void testPermanence() {
		ItemEntity testIE = new ItemEntity(testItem);
		loadWorldWithPlayer(0, 0);
		testIE.setPosition(0f, 10f);
		testIE.setRemoveDelay(10);
		testIE.setPermanent(true);
		world.addEntity(testIE);
		world.gameLoop(1);
		// Double check that the world goes contain the entity
		assert (world.getAllEntities().contains(testIE));
		testIE.tick(20);
		world.gameLoop(1);
		// Entity should not have been removed because it is permanent
		assert (world.getAllEntities().contains(testIE));
		testIE.setPermanent(false);
		testIE.tick(20);
		world.gameLoop(1);
		// Entity should have been removed now that it isn't permanent
		assert (!world.getAllEntities().contains(testIE));
	}

	private void loadWorldWithPlayer(float posX, float posY) {
		InputManager.clearAllValues();
		world.debugReset();
		if (p == null) {
			p = new Player();
		}
		world.addEntity(p);
		world.gameLoop(1);
		p.setPosition(posX + 70, posY + 32);
		world.setTiles(tiles);
		world.gameLoop(1);
		world.gameLoop(1);
	}

}
