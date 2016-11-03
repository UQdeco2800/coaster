package uq.deco2800.coaster.game.inventory;

import java.util.Arrays;
import java.util.List;

import javafx.scene.image.Image;
import uq.deco2800.coaster.game.items.Armour;
import uq.deco2800.coaster.game.items.ArmourType;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.ItemType;


public class Inventory {
	// Some #defines
	private static final String EMPTY_SLOT = "emptyslot";
	public static final String ACTIVE_ARRAY = "Active Inventory";
	public static final int ACTIVE_SIZE = 10;
	private InventoryPair[] activeInventory = new InventoryPair[ACTIVE_SIZE];

	public static final String PASSIVE_ARRAY = "Passive Inventory";
	public static final int PASSIVE_SIZE = 4;
	private InventoryPair[] passiveInventory = new InventoryPair[PASSIVE_SIZE];

	public static final String GENERAL_ARRAY = "General Inventory";
	public static final int GENERAL_SIZE = 29;

	private InventoryPair[] generalInventory = new InventoryPair[GENERAL_SIZE];


	/**
	 * Creates a new inventory and loads it with the starting items.
	 */
	public Inventory() {

		// The empty slot
		InventoryPair emptyPair = new InventoryPair(0, ItemRegistry.getItem(EMPTY_SLOT));
		// If you would like to ADD ITEMS HERE, please see our wikipage:
		// https://github.com/UQdeco2800/deco2800-2016-coaster/wiki/Inventory-Screen

		// Filling up the active Inventory
		for (int i = 0; i < activeInventory.length; i++) {
			this.activeInventory[i] = emptyPair;
		}

		// ADD ITEMS HERE for the active inventory
		this.activeInventory[0] = new InventoryPair(1, ItemRegistry.getItem("Gun1"));
		this.activeInventory[1] = new InventoryPair(1,  ItemRegistry.getItem("Melee1"));
//		this.activeInventory[2] = new InventoryPair(1,  ItemRegistry.getItem("Gun2"));
//		this.activeInventory[3] = new InventoryPair(1,  ItemRegistry.getItem("Gun3"));
//		this.activeInventory[4] = new InventoryPair(1,  ItemRegistry.getItem("Gun4"));
//		this.activeInventory[5] = new InventoryPair(1,  ItemRegistry.getItem("Gun5"));
//		this.activeInventory[6] = new InventoryPair(1,  ItemRegistry.getItem("Gun6"));
//		this.activeInventory[7] = new InventoryPair(1,  ItemRegistry.getItem("Gun7"));
//		this.activeInventory[8] = new InventoryPair(1,  ItemRegistry.getItem("Gun8"));

		// Filling up the passive inventory
		for (int i = 0; i < passiveInventory.length; i++) {
			this.passiveInventory[i] = emptyPair;
		}
		// ADD ITEMS HERE for passive inventory
		this.passiveInventory[0] = new InventoryPair(1, ItemRegistry.getItem("helmet"));
		this.passiveInventory[1] = new InventoryPair(1, ItemRegistry.getItem("chestplate"));

		// Filling up the general Inventory

		for (int i = 0; i < generalInventory.length; i++) {
			this.generalInventory[i] = emptyPair;
		}

		// ADD ITEMS HERE for general inventory
		this.generalInventory[0] = new InventoryPair(2, ItemRegistry.getItem("potion"));
		this.generalInventory[1] = new InventoryPair(1, ItemRegistry.getItem("tears"));
		this.generalInventory[2] = new InventoryPair(1, ItemRegistry.getItem("blooddrop"));
		this.generalInventory[3] = new InventoryPair(10000, ItemRegistry.getItem("ammo"));
		this.generalInventory[4] = new InventoryPair(50, ItemRegistry.getItem("ex_ammo"));
	}

	/**
	 * Returns the size of the active inventory
	 *
	 * @return the size of the active inventory
	 */
	public int sizeOfActiveArray() {
		return ACTIVE_SIZE;
	}

	/**
	 * Returns the size of the passive inventory
	 *
	 * @return the size of the passive inventory
	 */
	public int sizeOfPassiveArray() {
		return PASSIVE_SIZE;
	}

	/**
	 * Returns the size of the item inventory
	 *
	 * @return the size of the general inventory
	 */
	public int sizeOfGeneralArray() {
		return GENERAL_SIZE;
	}


	/**
	 * This method will indicate if an empty slot exists in a particular array
	 *
	 * If a corrupt array is passed in, it will return true
	 *
	 * @param itemArray the name of the array you would like to check
	 * @return true if full, false if not full
	 */
	public boolean isFull(String itemArray) {
		final InventoryPair[] arrayToCheck;

		arrayToCheck = getInventory(itemArray);

		for (int i = 0; i < arrayToCheck.length; i++) {
			if ((EMPTY_SLOT).equals(arrayToCheck[i].getItem().getID())) {
				return false;
			}
		}

		return true;
	}


	/**
	 * This method will only be adding to the general inventory. This method is for external use,
	 * such as item store, crafting and enchantment. It will insert the item in the first empty slot or the
	 * first valid stack of items it comes across
	 * <p>
	 * To use this method, call it using the item you want to add and how many of that particular item you
	 * would like to add.
	 *
	 * @param quantity the amount of items to be added
	 * @param itemID   the string name of the item to be added
	 * @return true if item added successfully, false if not
	 */
	public boolean addItem(int quantity, String itemID) {

		InventoryPair emptyPair = new InventoryPair(0, ItemRegistry.getItem(EMPTY_SLOT));
		
		InventoryPair newItemPair = new InventoryPair(quantity, ItemRegistry.getItem(itemID));

		for (int i = 0; i < generalInventory.length; i++) {
			if ((generalInventory[i].getItem().getID().equals(itemID)) &&
							generalInventory[i].getItem().isStackable() &&
							generalInventory[i].addQuantity(quantity)) {
				return true;
			}
		}
		for (int i = 0; i < generalInventory.length; i++) {
			if (generalInventory[i].equalPair(emptyPair)) {
				generalInventory[i] = newItemPair;
				return true;
			}
		}
		return false;
	}

	/**
	 * This function allows for the insertion of any item into any array along with specifying which item to insert and
	 * how much
	 *
	 * @param itemArray  - The array for insertion
	 * @param itemID     - The item you would like to insert
	 * @param quantity   - the quantity of the item you would like to insert
	 * @param slotNumber - The place you would like to insert the item
	 * @return boolean - true for success, false for failure
	 */
	public boolean placeItem(String itemArray, int slotNumber, String itemID, int quantity) {
		final InventoryPair[] arrayToPlace;

		arrayToPlace = getInventory(itemArray);

		if (slotNumber + 1 > arrayToPlace.length || slotNumber < 0) {
			return false;
		}
		
		InventoryPair addition = new InventoryPair(quantity, ItemRegistry.getItem(itemID));
		arrayToPlace[slotNumber] = addition;

		return true;
	}


	/**
	 * This method will only be removing from the general inventory. This method is for external use,
	 * such as item store, crafting and enchantment. It will encounter the first stack, and if the stack
	 * is smaller than the quantity required to remove, it will skip it and try to search for a bigger stack
	 * <p>
	 * To use this method, call it using the item you want to add and how many of that particular item you
	 * would like to remove.
	 *
	 * @param quantity the amount of items to be removed
	 * @param itemID   the string name of the item to be removed
	 * @return true if item removed successfully, false if not
	 */
	public boolean removeItem(int quantity, String itemID) {
		for (int i = 0; i < generalInventory.length; i++) {
			if (generalInventory[i].getItem().getID().equals(itemID) &&
					generalInventory[i].subtractQuantity(quantity)) {
				if (generalInventory[i].getQuantity() == 0) {
					clearInvSlot(GENERAL_ARRAY, i);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * This will clear a requested slot from a stated array.
	 * If an unknown array is passed in, a false will be returned
	 *
	 * @param itemArray the name of the array you would like to clear a slot from
	 * @param slotNumber the position in the array that you would like to clear
	 * @return true if successful, false if failed
	 */
	public boolean clearInvSlot(String itemArray, int slotNumber) {
		final InventoryPair[] arrayToChange;


		arrayToChange = getInventory(itemArray);

		if (slotNumber > arrayToChange.length || slotNumber < 0) {
			return false;
		}

		arrayToChange[slotNumber] = new InventoryPair(0, ItemRegistry.getItem(EMPTY_SLOT));
		return true;
	}

	/**
	 * NOTICE
	 * This method are using to test the item is exist or not, if the item exist then we can delete the item, the method
	 * will check all the type in the big data, itemlist, item active, weaponactive and also the armor active part, and
	 * check which item object will pass the boolean
	 *
	 * @param itemID the item you would like to see if it exists
	 * @return boolean value
	 */
	public boolean itemExists(String itemID) {
		Item item = ItemRegistry.getItem(itemID);
		return itemExistsSingular(activeInventory, item) ||
				itemExistsSingular(passiveInventory, item) ||
				itemExistsSingular(generalInventory, item);
	}

	/**
	 * This method is a boolean function to test the item is exist or not in the inventorypair list
	 *
	 * @param ipl the inventory pair list you would like to check
	 * @param item the item you are searching for in an inventory pair
	 * @return true if item exists
	 */
	private boolean itemExistsSingular(InventoryPair[] ipl, Item item) {
		return getInventoryPair(ipl, item) != null;
	}

	/**
	 * This method is using the find the item that match with the item in the list of the inventorypair
	 *
	 * @param ipl the inventory pair list you would like to check
	 * @param item the item you are searching for in an inventory pair
	 * @return InventoryPair associated with Item
	 */
	private InventoryPair getInventoryPair(InventoryPair[] ipl, Item item) {
		for (InventoryPair ip : ipl) {
			if (item == ip.getItem()) {
				return ip;
			}
		}
		return null;
	}


	/**
	 * This method will take the inventorypair and set it to be a array list the use for it will be group all the data
	 * in the same list, and easy for implementation later of other function, and it will get each pair of the item with
	 * their item object and also the qunatity of each item
	 *
	 * @return the list of the data with itemlist, itemactive, weaponacitve and the armoractive
	 */
	private List<InventoryPair[]> getInventoryPairs() {
		return Arrays.asList(activeInventory, passiveInventory, generalInventory);
	}


	/**
	 * @param itemID the name of the item
	 * @return the quantity of the item that focus on, if exist, return 0
	 */
	public int getAmount(String itemID) {
		Item item = ItemRegistry.getItem(itemID);
		int total = 0;
		InventoryPair got;
		for (InventoryPair[] list : getInventoryPairs()) {
			if ((got = getInventoryPair(list, item)) != null) {
				total += got.getQuantity();
			}
		}
		return total;
	}

	/**
	 * Returns the id number that we want to find out, each of the item type have different id for it, then we are using
	 * different case for the function to defind the item and also return its id number
	 *
	 * @param itemArray  array you would like to check
	 * @param slotNumber the slot of the array you would like to check
	 * @return the id number of the specified item that focus on
	 */
	public String getItemID(String itemArray, int slotNumber) {
		final InventoryPair[] arrayToCheck;


		arrayToCheck = getInventory(itemArray);

		if (slotNumber > arrayToCheck.length || slotNumber < 0) {
			return "Invalid parameters";
		}
		return arrayToCheck[slotNumber].getItem().getID();
	}

	/**
	 * Returns the quantity of a particular element in a Particular element
	 *
	 * @param itemArray  array you would like to check
	 * @param slotNumber the slot of the array you would like to check
	 * @return int quantity
	 */
	public int getItemQuantity(String itemArray, int slotNumber) {
		final InventoryPair[] arrayToCheck;
		arrayToCheck = getInventory(itemArray);

		if (slotNumber > arrayToCheck.length || slotNumber < 0) {
			return 0;
		}

		return arrayToCheck[slotNumber].getQuantity();
	}

	/**
	 * To cut down on duplicated code, call with the array's string ID.
	 *
	 * @param array - The string ID of the array you would like
	 * @return InventoryPair array
	 */
	private InventoryPair[] getInventory(String array) {
		if (ACTIVE_ARRAY.equals(array)) {
			return activeInventory;
		} else if (PASSIVE_ARRAY.equals(array)) {
			return passiveInventory;
		} else if (GENERAL_ARRAY.equals(array)) {
			return generalInventory;
		} else {
			return new InventoryPair[0];
		}
	}

	/**
	 * Returns an detail information for the item that base on the id that they had, and also base on the string request
	 * will return different string one will be the description and the other one will be the name of the item sprite
	 *
	 * @return the string of the name for the specified item
	 */
	public static String getItemInfo(String id, String request) {
		if ("Description".equals(request)) {
			return ItemRegistry.getItem(id).getDescription();
		} else if ("Name".equals(request)) {
			return ItemRegistry.getItem(id).getName();
		} else {
			return "";
		}
	}
	
	/** Returns the type of an item given an item id
	 * 
	 * @param id
	 * @return
	 */
	public static ItemType getItemType(String id){
		Item item = ItemRegistry.getItem(id);
		return item.getType();
	}

	/**
	 * Returns the armour type of an item if the item is armour
	 *
	 * @param id The item's string ID you would like information on
	 * @return ArmourType of the item. If invalid or not of Armour type, ArmourType.INVALID is returned
	 */
	public static ArmourType getArmourType(String id){
		if (ItemRegistry.getItem(id) instanceof Armour) {
			return ((Armour) ItemRegistry.getItem(id)).getArmourType();
		}
		return ArmourType.INVALID;

	}

	/**
	 * Returns an Image object that can then be painted on inventory screen. this method will take one parameter id from
	 * the input string, and base on the string id, will get the sprite of the item that equal to the same id
	 *
	 * @param id an absolute String giving for the specified sprite
	 * @return the image at the specified id
	 * @see Image
	 */
	public Image getSprite(String id) { return ItemRegistry.getItem(id).getSprite().getFrame(); }

	// Dam you sonar and your Cyclomatic Complexity
	// http://deco2800-sonar.uqcloud.net/issues/search#issues=AVfcngM9uv8Cj2ilNSiA
	/**
	 * This method allows swapping of items, given 2 arrays and the corresponding slot numbers
	 *
	 * @param firstArray  name of the first array
	 * @param firstSlot   position of the first item
	 * @param secondArray name of the second array
	 * @param secondSlot position of the second item
	 * @param stack Set true if items are to be stacked
	 * @return true if successful, false if unsuccessful
	 */
	public boolean swapItem(String firstArray, int firstSlot, String secondArray, int secondSlot, boolean stack) {
		final InventoryPair[] firstInventory;
		final InventoryPair[] secondInventory;

		firstInventory = getInventory(firstArray);
		secondInventory = getInventory(secondArray);
		if (!checkArrayInBounds(firstInventory, firstSlot) || !checkArrayInBounds(secondInventory, secondSlot)) {
			return false;
		}

		// Checks to see if it is in the same array and in the same slot
		// Checks to see that the items are the same item
		boolean sameItem = !(firstArray.equals(secondArray) && firstSlot == secondSlot) &&
				firstInventory[firstSlot].getItem().getID().equals(secondInventory[secondSlot].getItem().getID());

		if (sameItem && (stack && secondInventory[secondSlot].addQuantity(firstInventory[firstSlot].getQuantity()))) {
				clearInvSlot(firstArray, firstSlot);
				return true;
		}


		InventoryPair temp = new InventoryPair(firstInventory[firstSlot].getQuantity(), firstInventory[firstSlot].getItem());
		firstInventory[firstSlot] = new InventoryPair(secondInventory[secondSlot].getQuantity(),
				secondInventory[secondSlot].getItem());
		secondInventory[secondSlot] = temp;
		return true;

	}

	/**
	 * Takes in an inventory pair array and checks to see if the slot number is not out of bounds
	 *
	 * @param array The array you would like to check
	 * @param slotToCheck The slot number you would like to check
	 * @return true if in bounds, false if not in bounds
	 */
	public boolean checkArrayInBounds(InventoryPair[]  array, int slotToCheck) {
		if (slotToCheck > array.length || slotToCheck < 0) {
			return false;
		}
		return true;
	}

	/**
	 * This method is provided for reducing consumables when in the active slot of the user
	 * Note: This method will not work on un stackable items
	 *
	 * @param slotNumber The slot
	 * @return true if item was sucessfuly decremented, false if could not ddecrement due to unstackable item or empty slot
	 */
	public boolean reduceActiveConsumable(int slotNumber) {
		if (slotNumber > ACTIVE_SIZE - 1) {
			return false;
		}
		if (activeInventory[slotNumber].getItem().isStackable()) {
			activeInventory[slotNumber].subtractQuantity(1);
			return true;
		}
		return false;
	}

	/**
	 * Checks the amount of actives that are currently in the active array
	 * 
	 * @param playerLevel
	 * @param equip
	 * @return
	 */
	public boolean checkActiveAmount(int playerLevel, boolean equip){
		int activeCounter = 0;
		for (int i = 0; i < ACTIVE_SIZE; i++){
			if (getItemType(getItemID(ACTIVE_ARRAY, i)) == ItemType.WEAPON){
				activeCounter += 1;
			}
		}

		if (equip){
			activeCounter += 1;
		}

		return checkLevelRestrictionsActive(playerLevel, activeCounter);
	}

	/** Returns a boolean depending on the number of actives in the active array
	 *  and they player's level.
	 * 
	 * @param playerLevel
	 * @param activeAmount
	 * @return
	 */
	public static boolean checkLevelRestrictionsActive(int playerLevel, int activeAmount){
		switch (activeAmount){
			case 0:
			case 1:
			case 2:
				return true;
			case 3:
			case 4:
			case 5:
				return playerLevel >= activeAmount * activeAmount - (5 - activeAmount);
			default:
				return false;
		}
	}


	/**
	 * Checks if an Item is in the general array, and if so, removes 1 quantity of it.
	 * If the ID passed in is "", it will return true. To be used with complete crafting
	 * Only for the controller, pls no use if ur not crafting controller
	 *
	 * @param id Item ID you would like to remove
	 * @return true if item removed successfully or if string is "empty"
	 */
	public boolean checkItem(String id) {
		// My attempt at looking pro ;)
		return "empty".equals(id) || removeItem(1, id);
	}
}