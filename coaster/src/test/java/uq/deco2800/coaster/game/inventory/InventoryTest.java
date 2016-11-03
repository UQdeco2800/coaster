package uq.deco2800.coaster.game.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.items.ArmourType;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.ItemType;

public class InventoryTest {
	private static final String activeArray = Inventory.ACTIVE_ARRAY;
	private static final String passiveArray = Inventory.PASSIVE_ARRAY;
	private static final String generalArray = Inventory.GENERAL_ARRAY;
	private static final int activeSize = Inventory.ACTIVE_SIZE;
	private static final int passiveSize = Inventory.PASSIVE_SIZE;
	private static final int generalSize = Inventory.GENERAL_SIZE;
	// The number of items initially in the active array
	private static final int numItemsActive = 9;
	// The number of items initially in the passive array
	private static final int numItemsPasisve = 2;
	// The number of items initially in the general array
	private static final int numItemsGeneral = 5;

	/**
	 * Test the initialization of the inventory
	 */
	@Test
	public void checkInit() {
		TestHelper.load();
		Inventory inv = new Inventory();

		assertEquals(activeSize, inv.sizeOfActiveArray());
		assertEquals(passiveSize, inv.sizeOfPassiveArray());
		assertEquals(generalSize, inv.sizeOfGeneralArray());

		// Active array tests
		assertTrue(inv.getItemID(activeArray, 0).equals("Gun1"));
		assertTrue(inv.getItemID(activeArray, 1).equals("Melee1"));
		// Passive array tests
		assertTrue(inv.getItemID(passiveArray, 0).equals("helmet"));
		assertTrue(inv.getItemID(passiveArray, 1).equals("chestplate"));
		// General array tests
		assertTrue(inv.getItemID(generalArray, 0).equals("potion"));
		assertTrue(inv.getItemID(generalArray, 1).equals("tears"));
		assertTrue(inv.getItemID(generalArray, 2).equals("blooddrop"));
		assertTrue(inv.getItemID(generalArray, 3).equals("ammo"));

		for (int i = numItemsActive; i < inv.sizeOfActiveArray(); i++) {
			assertTrue(inv.getItemID(activeArray, i).equals("emptyslot"));
			assertTrue(inv.getItemQuantity(activeArray, i) == 0);
		}

		for (int i = numItemsPasisve; i < inv.sizeOfPassiveArray(); i++) {
			assertTrue(inv.getItemID(passiveArray, i).equals("emptyslot"));
			assertTrue(inv.getItemQuantity(passiveArray, i) == 0);
		}

		for (int i = numItemsGeneral; i < inv.sizeOfGeneralArray(); i++) {
			assertTrue(inv.getItemID(generalArray, i).equals("emptyslot"));
			assertTrue(inv.getItemQuantity(generalArray, i) == 0);
		}

		assertTrue("Empty Slot".equals(Inventory.getItemInfo("emptyslot", "Description")));
		assertTrue("Empty Slot".equals(Inventory.getItemInfo("emptyslot", "Name")));
		assertTrue("".equals(Inventory.getItemInfo("emptyslot", "idk")));
		assertTrue(Inventory.getItemType("emptyslot") == ItemType.NOTHING);
		assertTrue(inv.getSprite("emptyslot") == ItemRegistry.getItem("emptyslot").getSprite().getFrame());

	}

	/**
	 * Tests the insertion function
	 */
	@Test
	public void checkplaceItem() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(inv.getItemID(activeArray, 0).equals("Gun1"));
		// Checking to see if the item is successfully inserted
		assertTrue(inv.placeItem(activeArray, 0, "Gun7", 5));
		assertTrue(inv.getItemID(activeArray, 0).equals("Gun7"));
		assertTrue(inv.getItemQuantity(activeArray, 0) == 5);
		// Checking to see if another item is successfully inserted
		assertTrue(inv.placeItem(activeArray, 0, "potion1", 20));
		assertTrue(inv.getItemID(activeArray, 0).equals("potion1"));
		assertTrue(inv.getItemQuantity(activeArray, 0) == 20);

		// Checking to see if the item is successfully inserted
		assertTrue(inv.placeItem(passiveArray, 0, "helmet", 5));
		assertTrue(inv.getItemID(passiveArray, 0).equals("helmet"));
		assertTrue(inv.getItemQuantity(passiveArray, 0) == 5);
		// Checking to see if another item is successfully inserted
		assertTrue(inv.placeItem(passiveArray, 0, "chestplate", 20));
		assertTrue(inv.getItemID(passiveArray, 0).equals("chestplate"));
		assertTrue(inv.getItemQuantity(activeArray, 0) == 20);

		// Checking to see if the item is successfully inserted
		assertTrue(inv.placeItem(generalArray, 27, "helmet", 40));
		assertTrue(inv.getItemID(generalArray, 27).equals("helmet"));
		assertTrue(inv.getItemQuantity(generalArray, 27) == 40);
		// Checking to see if another item is successfully inserted
		assertTrue(inv.placeItem(generalArray, 17, "chestplate", 20));
		assertTrue(inv.getItemID(generalArray, 17).equals("chestplate"));
		assertTrue(inv.getItemQuantity(generalArray, 17) == 20);

		// Testing failed inserts
		assertTrue(!inv.placeItem(activeArray, 12, "Gun1", 0));
		assertTrue(!inv.placeItem(passiveArray, 40, "Gun1", 0));
		assertTrue(!inv.placeItem(generalArray, 80, "Gun1", 0));
	}

	/**
	 * Tests the clear function
	 */
	@Test
	public void checkClearItem() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(inv.getItemID(activeArray, 0).equals("Gun1"));
		// Checking to see if the item is successfully cleared
		assertTrue(inv.clearInvSlot(activeArray, 0));
		assertTrue(inv.getItemID(activeArray, 0).equals("emptyslot"));
		assertTrue(inv.getItemQuantity(activeArray, 0) == 0);
		// Checking to see if another item is successfully cleared
		assertTrue(inv.clearInvSlot(activeArray, 0));
		assertTrue(inv.getItemID(activeArray, 0).equals("emptyslot"));
		assertTrue(inv.getItemQuantity(activeArray, 0) == 0);

		// Checking to see if the item is successfully cleared
		assertTrue(inv.clearInvSlot(passiveArray, 0));
		assertTrue(inv.getItemID(passiveArray, 0).equals("emptyslot"));
		assertTrue(inv.getItemQuantity(passiveArray, 0) == 0);
		// Checking to see if another item is successfully cleared
		assertTrue(inv.clearInvSlot(passiveArray, 0));
		assertTrue(inv.getItemID(passiveArray, 0).equals("emptyslot"));
		assertTrue(inv.getItemQuantity(activeArray, 0) == 0);

		// Checking to see if the item is successfully cleared
		assertTrue(inv.clearInvSlot(generalArray, 26));
		assertTrue(inv.getItemID(generalArray, 26).equals("emptyslot"));
		assertTrue(inv.getItemQuantity(generalArray, 26) == 0);
		// Checking to see if another item is successfully cleared
		assertTrue(inv.clearInvSlot(generalArray, 17));
		assertTrue(inv.getItemID(generalArray, 17).equals("emptyslot"));
		assertTrue(inv.getItemQuantity(generalArray, 17) == 0);

		// Testing failed clears
		assertTrue(!inv.clearInvSlot(activeArray, 12));
		assertTrue(!inv.clearInvSlot(passiveArray, 40));
		assertTrue(!inv.clearInvSlot(generalArray, 80));
		assertTrue(!inv.clearInvSlot(generalArray, -80));
	}

	/**
	 * Checking to make sure when Is full says its full its actually full
	 */
	@Test
	public void checkIsFull() {
		TestHelper.load();
		Inventory inv = new Inventory();

		// check the initialization values
		assertTrue(!inv.isFull(activeArray));
		assertTrue(!inv.isFull(passiveArray));
		assertTrue(!inv.isFull(generalArray));

		// Test abnormal behavior
		assertTrue(inv.isFull("jibberish"));

		// Fill all of the arrays with items
		for (int i = 0; i < inv.sizeOfActiveArray(); i++) {
			inv.placeItem(activeArray, i, "Gun7", 5);
		}

		for (int i = 0; i < inv.sizeOfPassiveArray(); i++) {
			inv.placeItem(passiveArray, i, "helmet", 1);
		}
		for (int i = 0; i < inv.sizeOfGeneralArray(); i++) {
			inv.placeItem(generalArray, i, "potion1", 1);
		}

		// now confirm all of them are full
		assertTrue(inv.isFull(activeArray));
		assertTrue(inv.isFull(passiveArray));
		assertTrue(inv.isFull(generalArray));

		// remove only 1 element
		inv.placeItem(activeArray, 3, "emptyslot", 1);
		inv.placeItem(passiveArray, 2, "emptyslot", 1);
		inv.placeItem(generalArray, 20, "emptyslot", 1);

		// now confirm that they are not full
		assertTrue(!inv.isFull(activeArray));
		assertTrue(!inv.isFull(passiveArray));
		assertTrue(!inv.isFull(generalArray));

		// reinsert  1 element
		inv.placeItem(activeArray, 3, "helmet", 1);
		inv.placeItem(passiveArray, 2, "helmet", 1);
		inv.placeItem(generalArray, 20, "helmet", 1);

		// now confirm all of them are full
		assertTrue(inv.isFull(activeArray));
		assertTrue(inv.isFull(passiveArray));
		assertTrue(inv.isFull(generalArray));

		// Empty all of the arrays with items
		for (int i = 0; i < inv.sizeOfActiveArray(); i++) {
			inv.placeItem(activeArray, i, "emptyslot", 1);
		}
		for (int i = 0; i < inv.sizeOfPassiveArray(); i++) {
			inv.placeItem(passiveArray, i, "emptyslot", 1);
		}
		for (int i = 0; i < inv.sizeOfGeneralArray(); i++) {
			inv.placeItem(generalArray, i, "emptyslot", 1);
		}

		// now confirm all of them are empty
		assertTrue(!inv.isFull(activeArray));
		assertTrue(!inv.isFull(passiveArray));
		assertTrue(!inv.isFull(generalArray));

		// Check for negative bounds
		assertTrue(!inv.placeItem(activeArray, -3, "helmet", 1));
		assertTrue(!inv.placeItem(passiveArray, -2, "helmet", 1));
		assertTrue(!inv.placeItem(generalArray, -20, "helmet", 1));
	}

	/**
	 * Testing get quantity
	 */
	@Test
	public void checkQuantityGetter() {
		TestHelper.load();
		Inventory inv = new Inventory();
		for (int i = 0; i < 2; i++) {
			assertTrue(inv.getItemQuantity(activeArray, i) == 1);
		}

		assertTrue(inv.getItemQuantity(passiveArray, 0) == 1);
		assertTrue(inv.getItemQuantity(generalArray, 0) == 2);

		assertTrue(inv.getItemQuantity(activeArray, 20) == 0);
		assertTrue(inv.getItemQuantity(passiveArray, 15) == 0);
		assertTrue(inv.getItemQuantity(generalArray, 100) == 0);
		assertTrue(inv.getItemQuantity("jibberish", 20) == 0);
		assertTrue(inv.getItemQuantity(generalArray, -10) == 0);
	}

	/**
	 * Testing add Item
	 */
	@Test
	public void testAddItem() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(inv.addItem(5, "tester"));

		assertTrue(inv.getItemID(generalArray, numItemsGeneral).equals("tester"));
		assertTrue(inv.getItemQuantity(generalArray, numItemsGeneral) == 5);
		assertTrue(inv.addItem(5, "tester"));
		assertTrue(inv.getItemID(generalArray, numItemsGeneral).equals("tester"));
		assertTrue(inv.getItemQuantity(generalArray, numItemsGeneral) == 10);

		// Now fill up the array
		for (int i = 0; i < inv.sizeOfGeneralArray(); i++) {
			inv.placeItem(generalArray, i, "potion1", 1);
		}
		// confirm it's full
		assertTrue(inv.isFull(generalArray));
		assertTrue(!inv.addItem(5, "tester"));
		inv.clearInvSlot(generalArray, 0);
		inv.clearInvSlot(generalArray, 1);
		inv.placeItem(generalArray, 0, "tester", 14);
		assertTrue(inv.addItem(1, "tester"));
		assertTrue(inv.getItemID(generalArray, 0).equals("tester"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 15);
		assertTrue(inv.addItem(3, "tester"));
		assertTrue(inv.getItemID(generalArray, 0).equals("tester"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 15);
		assertTrue(inv.getItemID(generalArray, 1).equals("tester"));
		assertTrue(inv.getItemQuantity(generalArray, 1) == 3);
		inv.clearInvSlot(generalArray, 1);
		inv.placeItem(generalArray, 0, "tester", 16);
		inv.placeItem(generalArray, 2, "tester", 14);
		assertTrue(inv.getItemID(generalArray, 0).equals("tester"));
		assertTrue(inv.getItemID(generalArray, 1).equals("emptyslot"));
		assertTrue(inv.getItemID(generalArray, 2).equals("tester"));
		assertTrue(inv.getItemQuantity(generalArray, 2) == 14);
		assertTrue(inv.addItem(1, "tester"));
		assertTrue(inv.getItemQuantity(generalArray, 2) == 15);
		inv.placeItem(generalArray, 1, "Gun1", 1);
		assertTrue(inv.getItemID(generalArray, 1).equals("Gun1"));
		inv.addItem(1, "Gun1");
		assertTrue(inv.getItemQuantity(generalArray, 1) == 1);
	}

	/**
	 * Testing the remove item Function
	 */
	@Test
	public void testRemoveItem() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(!inv.removeItem(1, "chestplate"));
		assertTrue(inv.removeItem(1, "potion"));
		assertTrue(inv.getItemID(generalArray, 0).equals("potion"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 1);
		assertTrue(inv.removeItem(1, "potion"));
		assertTrue(inv.getItemID(generalArray, 0).equals("emptyslot"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 0);

		assertTrue(inv.getItemID(generalArray, 1).equals("tears"));
		assertTrue(inv.removeItem(1, "tears"));
		assertTrue(inv.getItemQuantity(generalArray, 1) == 0);

		// Testing the removability of unstackable items
		inv.placeItem(generalArray, 0, "Gun1", 1);
		assertTrue(!inv.removeItem(4, "Gun1"));
		assertTrue(inv.removeItem(1, "Gun1"));

		// Testing remove item's version for controller
		assertTrue(inv.checkItem("empty"));
		inv.placeItem(generalArray, 0, "potion", 1);
		assertTrue(inv.checkItem("potion"));
		assertTrue(!inv.checkItem("potion"));
	}

	/**
	 * Testing item exists function
	 */
	@Test
	public void testItemExists() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(inv.itemExists("Gun1"));
		assertTrue(inv.itemExists("helmet"));
		assertTrue(inv.itemExists("potion"));
		assertTrue(!inv.itemExists("tester"));
	}

	/**
	 * Testing get Item ID function
	 */
	@Test
	public void testGetItemID() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue("Invalid parameters".equals(inv.getItemID("poop", 1)));
		assertTrue("Invalid parameters".equals(inv.getItemID(activeArray, -1)));
	}

	/**
	 * Testing get Item Info function
	 */
	@Test
	public void testGetItemInfo() {
		TestHelper.load();
		assertTrue(Inventory.getItemInfo("potion1", "Description").equals("It'll knock you off your chops"));
		assertTrue(Inventory.getItemInfo("potion1", "Name").equals("Potion"));
		assertTrue(Inventory.getItemInfo("potion1", "XXXXX").equals(""));
	}

	/**
	 * Testing item amount function
	 */
	@Test
	public void testGetAmount() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(inv.getAmount("Gun1") == 1);
		assertTrue(inv.getAmount("potion") == 2);
	}

	/**
	 * Test the swapping of the inventory items
	 */
	@Test
	public void checkSwapping() {
		TestHelper.load();
		Inventory inv = new Inventory();

		assertTrue(inv.getItemID(activeArray, 0).equals("Gun1"));
		assertTrue(inv.getItemID(activeArray, 1).equals("Melee1"));
		assertTrue(inv.getItemID(passiveArray, 0).equals("helmet"));
		assertTrue(inv.getItemID(passiveArray, 1).equals("chestplate"));
		inv.placeItem(generalArray, 0, "potion1", 1);
		inv.placeItem(generalArray, 1, "potion1", 1);
		// ensuring rapid swapping maintains integrity
		inv.swapItem(activeArray, 0, generalArray, 0, true);
		inv.swapItem(generalArray, 0, activeArray, 0, true);

		inv.swapItem(activeArray, 0, activeArray, 1, true);
		assertTrue(inv.getItemID(activeArray, 1).equals("Gun1"));
		assertTrue(inv.getItemID(activeArray, 0).equals("Melee1"));

		assertTrue(inv.getItemQuantity(generalArray, 0) == 1);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 1);
		assertTrue(inv.addItem(2, "potion1"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 3);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 1);
		assertTrue(inv.getItemID(generalArray, 0).equals("potion1"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion1"));

		// Should not stack
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 1, false));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 1);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 3);
		assertTrue(inv.getItemID(generalArray, 0).equals("potion1"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion1"));
		// Swap it back
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 1, false));

		assertTrue(inv.getItemQuantity(generalArray, 0) == 3);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 1);
		assertTrue(inv.getItemID(generalArray, 0).equals("potion1"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion1"));

		// swapping 2 of the same item (i.e. will stack)
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 1, true));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 0);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 4);
		assertTrue(inv.getItemID(generalArray, 0).equals("emptyslot"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion1"));

		// Swapping 2 different items (i.e. will not stack)
		assertTrue(inv.placeItem(generalArray, 0, "potion1", 1));
		assertTrue(inv.placeItem(generalArray, 1, "potion2", 1));
		assertTrue(inv.getItemID(generalArray, 0).equals("potion1"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion2"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 1);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 1);
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 1, true));
		assertTrue(inv.getItemID(generalArray, 0).equals("potion2"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion1"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 1);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 1);

		// Swap the same item (i.e. should not delete)
		assertTrue(inv.placeItem(generalArray, 0, "potion2", 1));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 1);
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 1, true));
		assertTrue(inv.placeItem(generalArray, 0, "potion2", 1));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 1);

		//Swap between arrays
		assertTrue(inv.swapItem(generalArray, 0, activeArray, 1, false));
		assertTrue(inv.swapItem(generalArray, 0, activeArray, 1, false));
		assertTrue(inv.swapItem(generalArray, 0, activeArray, 1, true));
		assertTrue(inv.getItemID(generalArray, 0).equals("Gun1"));
		assertTrue(inv.getItemID(activeArray, 1).equals("potion2"));

		// Try to stack 2 stacks that will result in a stack bigger than max stack
		assertTrue(inv.placeItem(generalArray, 0, "potion", 14));
		assertTrue(inv.placeItem(generalArray, 1, "potion", 10));
		assertTrue(inv.getItemID(generalArray, 0).equals("potion"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 14);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 10);
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 1, true));
		assertTrue(inv.getItemID(generalArray, 0).equals("potion"));
		assertTrue(inv.getItemID(generalArray, 1).equals("potion"));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 10);
		assertTrue(inv.getItemQuantity(generalArray, 1) == 14);

		// Swap an item with itself
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 0, true));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 10);
		assertTrue(inv.swapItem(generalArray, 0, generalArray, 0, false));
		assertTrue(inv.getItemQuantity(generalArray, 0) == 10);

		// Testing out of bounds
		assertTrue(!inv.swapItem(activeArray, -9, generalArray, 1, true));
		assertTrue(!inv.swapItem(activeArray, 12, generalArray, 1, true));
		assertTrue(!inv.swapItem(activeArray, 1, generalArray, -30, true));
		assertTrue(!inv.swapItem(activeArray, 1, generalArray, 30, true));
	}

	/**
	 * Testing the Active array restrictions
	 */
	@Ignore
	@Test
	public void testCheckActiveAmount() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(inv.checkActiveAmount(0, false));
		assertTrue(inv.checkActiveAmount(1, false));
		assertTrue(inv.checkActiveAmount(2, false));
		// Trying to equip a third weapon before level 7
		assertTrue(!inv.checkActiveAmount(3, true));
		assertTrue(inv.checkActiveAmount(7, true));
		// 3 guns in inventory
		inv.placeItem(activeArray, 2, "Gun1", 1);
		assertTrue(inv.checkActiveAmount(7, false));
		assertTrue(inv.checkActiveAmount(11, false));
		assertTrue(!inv.checkActiveAmount(7, true));
		assertTrue(!inv.checkActiveAmount(14, true));
		assertTrue(inv.checkActiveAmount(16, true));
		// 4 guns in inventory
		inv.placeItem(activeArray, 3, "Gun1", 1);
		assertTrue(inv.checkActiveAmount(16, false));
		assertTrue(inv.checkActiveAmount(23, false));
		assertTrue(!inv.checkActiveAmount(16, true));
		assertTrue(!inv.checkActiveAmount(23, true));
		assertTrue(inv.checkActiveAmount(28, true));
		// 5 guns in inventory
		inv.placeItem(activeArray, 4, "Gun1", 1);
		assertTrue(!inv.checkActiveAmount(16, false));
		assertTrue(inv.checkActiveAmount(29, false));
		assertTrue(!inv.checkActiveAmount(16, true));
		assertTrue(!inv.checkActiveAmount(23, true));
		assertTrue(!inv.checkActiveAmount(28, true));
	}

	/**
	 * Testing check level restrictions active function
	 */
	@Test
	public void testCheckLevelRestrictionsActive() {
		TestHelper.load();
		assertTrue(Inventory.checkLevelRestrictionsActive(1, 0));
		assertTrue(Inventory.checkLevelRestrictionsActive(1, 1));
		assertTrue(Inventory.checkLevelRestrictionsActive(1, 2));
		assertTrue(Inventory.checkLevelRestrictionsActive(8, 3));
		assertTrue(!Inventory.checkLevelRestrictionsActive(3, 3));
		assertTrue(Inventory.checkLevelRestrictionsActive(17, 4));
		assertTrue(!Inventory.checkLevelRestrictionsActive(3, 4));
		assertTrue(Inventory.checkLevelRestrictionsActive(28, 5));
		assertTrue(!Inventory.checkLevelRestrictionsActive(3, 5));
		assertTrue(!Inventory.checkLevelRestrictionsActive(3, 6));
	}

	/**
	 * Tests that it reduces the currently equipped item properly
	 */
	@Test
	public void testReduceActiveConsumable() {
		TestHelper.load();
		Inventory inv = new Inventory();
		assertTrue(!inv.reduceActiveConsumable(0));
		assertTrue(!inv.reduceActiveConsumable(1));
		assertTrue(!inv.reduceActiveConsumable(2));
		assertTrue(!inv.reduceActiveConsumable(activeSize));
		assertTrue(inv.placeItem(activeArray, 4, "potion", 4));
		assertTrue(inv.reduceActiveConsumable(4));
		assertTrue(inv.getItemQuantity(activeArray, 4) == 3);
	}

	/**
	 * Tests that get armour type is working properly
	 */
	@Test
	public void testGetArmourType() {
		TestHelper.load();
		// Invalid Item
		assertTrue(Inventory.getArmourType("bababobo") == ArmourType.INVALID);
		// Valid item, but not of armour instance
		assertTrue(Inventory.getArmourType("potion") == ArmourType.INVALID);
		// Actual armour
		assertTrue(Inventory.getArmourType("helmet") == ArmourType.HEAD);
	}
}
