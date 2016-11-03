package uq.deco2800.coaster.game.commerce;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.ItemStore;
import uq.deco2800.coaster.game.items.ItemType;
import uq.deco2800.coaster.game.world.World;

/**
 * This class provides the backend for all NPC stores.
 *
 * Created by draganmarjanovic on 31/08/2016.
 */
public class Store {

	private PlayerCommerce playerCommerce;
	private List<ItemStore> stock = new ArrayList<>();

	/**
	 * Initialises the class, given a unique
	 */
	public Store(StoreType type) {
		initialiseStock();
		playerCommerce = World.getInstance().getFirstPlayer().getCommerce();
	}

	/**
	 * @return returns a map of stored items and their quantity.
	 */
	public List<ItemStore> getItems() {
		return stock;
	}

	/**
	 * NOTE: This is buying an item FROM the store. <p> The return format has not been implemented as it hinges on other
	 * work to be completed by the inventory team.
	 */

	public Integer buyItem(Integer itemIndex, Integer quantity) {
		if (isAvailable(itemIndex, quantity)) {
			playerCommerce.reduceGold(stock.get(itemIndex).getValue() * quantity);
			stock.get(itemIndex).decreaseStock(quantity);
			return quantity;
		} else {
			// Desired quantity was not available
			return 0;
		}
	}

	/**
	 * Returns the value of the Item. <p> Note: This currently does not differ from the getValue() function available in
	 * the Item class however the idea is to have the value modified based on store properties and stock.
	 */
	public int valueItem(String itemId) {
		return ItemRegistry.getItem(itemId).getValue();
	}

	/**
	 * NOTE: This is selling an item TO the store. <p> //TODO(@jamesthompson275): This should return the proper value in
	 * Coins
	 *
	 * @param itemIndex the ID of the item being sold
	 * @param quantity  the quantity that should be sold to the store
	 */
	public int sellItem(Integer itemIndex, int quantity) {
		//decreaseStock(); //TODO (@jeevan): Access inventory decreaseStock Function
		stock.get(itemIndex).decreaseStock(quantity);
		return valueItem(stock.get(itemIndex).getName()) * quantity;
	}

	/**
	 * Checks to see whether the given quantity of the given item is available in the store.
	 *
	 * @param itemIndex the Id of the item to check availability for
	 * @param quantity  the quantity of items to check for
	 */
	private boolean isAvailable(Integer itemIndex, int quantity) {
		return stock.get(itemIndex).getStock() >= quantity;
	}

	/**
	 * Checks to ensure that at least one unit of the requested item is available in the stores stock.
	 *
	 * @return true if stock count for given item is >= 1
	 */
	public boolean isAvailable(Integer itemIndex) {
		return isAvailable(itemIndex, 1);
	}

	/**
	 * Initialises the store to have some basic stock including two of Food, Weapons, and Misc.
	 */
	private void initialiseStock() {
		int stockCount = 5;
		stock.add(new ItemStore(ItemRegistry.randomItem(ItemType.POWERUP), stockCount));
		stock.add(new ItemStore(ItemRegistry.randomItem(ItemType.POWERUP), stockCount));
		stock.add(new ItemStore(ItemRegistry.randomItem(ItemType.WEAPON), stockCount));
		stock.add(new ItemStore(ItemRegistry.randomItem(ItemType.WEAPON), stockCount));
	}

}
