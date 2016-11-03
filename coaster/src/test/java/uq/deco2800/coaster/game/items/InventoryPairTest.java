package uq.deco2800.coaster.game.items;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.inventory.InventoryPair;

import static org.junit.Assert.assertTrue;

public class InventoryPairTest {

	/**
	 * Tests the getQuantity function
	 */
	@Test
	public void testGetQuantity() {
		TestHelper.load();
		Item testItem = new Item.Builder("test", "TESTER", null, "Testing Item", ItemType.NOTHING).build();
		InventoryPair pair = new InventoryPair(5, testItem);
		assertTrue(pair.getQuantity() == 5);
	}

	/**
	 * Tests the getItem function
	 */
	@Test
	public void testGetItem() {
		TestHelper.load();
		Item testItem = new Item.Builder("test", "TESTER", null, "Testing Item", ItemType.NOTHING).build();
		InventoryPair pair = new InventoryPair(5, testItem);
		assertTrue(pair.getItem() == testItem);
	}

	/**
	 * Tests the addition and subtraction of quantity
	 */
	@Test
	public void testQuantityManiplation() {
		TestHelper.load();
		Item testItem = new Item.Builder("test", "TESTER", null, "Testing Item", ItemType.NOTHING).build();
		InventoryPair pair = new InventoryPair(5, testItem);
		assertTrue(pair.addQuantity(5));
		assertTrue(pair.getQuantity() == 10);
		assertTrue(pair.subtractQuantity(5));
		assertTrue(pair.getQuantity() == 5);
		assertTrue(pair.subtractQuantity(10));
		assertTrue(pair.getQuantity() == 0);
		assertTrue(!pair.addQuantity(20));

		Item unstackabeleItem = new Item.Builder("unstackable", "TESTER", null, "Testing Item", ItemType.NOTHING)
				.stackable(false).build();
		InventoryPair unstackablePair = new InventoryPair(5, unstackabeleItem);
		assertTrue(unstackablePair.getQuantity() == 5);
		assertTrue(!unstackablePair.addQuantity(6));
		assertTrue(unstackablePair.getQuantity() == 1);
		assertTrue(!unstackablePair.subtractQuantity(6));
		assertTrue(unstackablePair.getQuantity() == 1);

		// Testing the custom quantity limits for ammunition
		Item ammo = new Item.Builder("ammo", "TESTER", null, "Testing Item", ItemType.NOTHING).build();
		InventoryPair ammunition = new InventoryPair(5, ammo);
		assertTrue(ammunition.getQuantity() == 5);
		assertTrue(ammunition.addQuantity(500));
		assertTrue(ammunition.getQuantity() == 505);

		Item exammo = new Item.Builder("ex_ammo", "TESTER", null, "Testing Item", ItemType.NOTHING).build();
		InventoryPair explosiveAmmunition = new InventoryPair(5, exammo);
		assertTrue(explosiveAmmunition.getQuantity() == 5);
		assertTrue(!explosiveAmmunition.addQuantity(500));
		assertTrue(explosiveAmmunition.addQuantity(55));
		assertTrue(explosiveAmmunition.getQuantity() == 60);
		assertTrue(explosiveAmmunition.addQuantity(4));
		assertTrue(explosiveAmmunition.getQuantity() == 64);
	}

	/**
	 * Tests the compair pair function
	 */
	@Test
	public void testComapirPair() {
		TestHelper.load();
		Item testItem1 = new Item.Builder("test1", "TESTER", null, "Testing Item", ItemType.NOTHING).build();
		Item testItem2 = new Item.Builder("test2", "TESTER", null, "Testing Item", ItemType.NOTHING).build();
		InventoryPair compair = new InventoryPair(1, testItem1);
		InventoryPair testPair1 = new InventoryPair(1, testItem1);
		InventoryPair testPair2 = new InventoryPair(2, testItem1);
		InventoryPair testPair3 = new InventoryPair(1, testItem2);
		assertTrue(compair.equalPair(testPair1));
		assertTrue(!compair.equalPair(testPair2));
		assertTrue(!compair.equalPair(testPair3));
	}
}
