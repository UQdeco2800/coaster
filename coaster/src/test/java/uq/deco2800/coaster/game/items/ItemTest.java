package uq.deco2800.coaster.game.items;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ItemTest {

	/**
	 * Tests to see that an item from the REGISTRY is being initialised and properties are being returned properly.
	 */
	@Test
	public void testGetItem() {
		//Test getting a weapon
		TestHelper.load();

		assertEquals(ItemRegistry.getItem("Gun1").getID(), "Gun1");
		assertEquals(ItemRegistry.getItem("Gun1").getName(), "Starting Gun");
		assertEquals(ItemRegistry.getItem("Gun1").getDescription(), "Don't you know a gun when you see one?");
		assertEquals(ItemRegistry.getItem("Gun1").getType(), ItemType.WEAPON);
		Weapon gun1 = (Weapon) ItemRegistry.getItem("Gun1");
		assertEquals(gun1.getDamage(), 50);
		assertEquals(gun1.getFiringRate(), 50);
	}

	/**
	 * Tests to see if the registry returns null when an item doesn't exist.
	 */
	@Test
	public void testNonExistentItem() {
		TestHelper.load();

		assertEquals(ItemRegistry.getItem("memes"), null);
	}

	/**
	 * Checks to see if the randomItem() returns a non null item.
	 */
	@Test
	public void testRandomItem() {
		TestHelper.load();
		assertNotEquals(ItemRegistry.randomItem(), null);
	}

	@Test
	public void testItemList() {
		TestHelper.load();
		assert ItemRegistry.itemList().size() > 0;
	}

	@Test
	public void testAllLookups() {
		TestHelper.load();
		ItemRegistry.itemList().forEach(this::testLookup);

	}

	private void testLookup(String item) {
		assertEquals(ItemRegistry.reverseLookup(ItemRegistry.getItem(item)), item);
	}

	@Test
	public void testEquals() {
		TestHelper.load();
		ItemRegistry.itemList().forEach(item -> {
			assert ItemRegistry.getItem(item).equals(ItemRegistry.getItem(item));
		});
	}
}
