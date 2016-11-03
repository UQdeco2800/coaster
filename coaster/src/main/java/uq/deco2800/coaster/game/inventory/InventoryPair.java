package uq.deco2800.coaster.game.inventory;

import uq.deco2800.coaster.game.items.Item;

public class InventoryPair {
	private int quantity;
	private Item item;
	private static int maxStack = 16;
	private static int maxStackExplosives = 64;

	/**
	 * Returns an Inventory pair that with the quantity value and item object. the method make sure the value for this
	 * method call will be the same as the value in the class InventoryPair. This method always return the value with
	 * the quantity and the item from the InventoryPair class, to make sure the the value in here will be the same
	 * reference to the current object.
	 *
	 * @param item the item you want to add
	 * @param quantity how many items  on the stack
	 */
	public InventoryPair(int quantity, Item item) {
		this.quantity = quantity;
		this.item = item;
	}

	/**
	 * Returns the value of the quantity that with the specified number. The quantity will be the integer value for the
	 * items in used This method returns is used to return the value for the quantity.
	 *
	 * @return the quantity at the specified items
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Returns the specified item from the list. This method can use to get the item from the whole set of the data with
	 * entering the own name of the item that you want to call and in use.
	 *
	 * @return the object at the specified item
	 * @see Item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Increases the quantity of an item, but ensures that it doesn't go past the maxStack variable and doesn't stack
	 * unstackable items.
	 * 
	 * @param size amount of items needed to be added
	 * @return if the addition was successful, true is returned
	 */
	public boolean addQuantity(int size) {
		if (!this.item.isStackable()) {
			this.quantity = 1;
			return false;
		}
		if ("ammo".equals(this.item.getID())) {
			// continue
		}
		else if ("ex_ammo".equals(this.item.getID())) {
			if (this.quantity + size > maxStackExplosives) {
				return false;
			}
			// continue
		}
		else if (this.quantity + size > maxStack) {
			return false;
		}
		this.quantity += size;
		return true;
	}

	/**
	 * This method is used to decrease the quantity of a certain pair. If the quantity will be negative due to the
	 * subtraction, it will instead be set to zero
	 * 
	 * @param size the amount of items needed to be subtracted
	 * @return returns true if subtraction was successful
	 */
	public boolean subtractQuantity(int size) {
		if (!this.item.isStackable()) {
			if (size == 1) {
				this.quantity = 0;
				return true;
			} else {
				this.quantity = 1;
				return false;
			}
		}
		if (this.quantity - size > 0) {
			this.quantity -= size;
			return true;
		} else {
			this.quantity = 0;
			return true;
		}
	}

	/**
	 * This method is used to compair the value
	 * that from the getQuantity() and getItem() is equal with the compair value or not
	 * 
	 * @param compair The inventory pair you would like to compair the current inventory pair to
	 * @return true if the same, false if not the same
	 */
	public boolean equalPair(InventoryPair compair) {
		if (this.getQuantity() != compair.getQuantity()) {
			return false;
		}
		if (this.getItem() != compair.getItem()) {
			return false;
		}
		return true;
	}
}
