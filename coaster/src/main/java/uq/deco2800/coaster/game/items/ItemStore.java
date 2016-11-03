package uq.deco2800.coaster.game.items;

import javafx.scene.image.ImageView;

/**
 * This class is used to contain metadata about a stored Item. It allows you to pass in an Item type and provides
 * functionality to store the quantity of that Item available.
 *
 * Example Usage/Scenarios: An example usage of this class could be to store different Items in an Inventory, Store or
 * any similar implementation. As each Item is by default immutable, this allows you to bundle other properties with the
 * Item and furthermore can be easily added to a JavaFX TableView.
 *
 * Created by draganmarjanovic on 17/09/2016.
 */
public class ItemStore {

	private Integer quantity;
	private Item item;
	/**
	 * The Constructor for the ItemStore.
	 *
	 * @param item     the item to stored in this entry.
	 * @param quantity the amount stored.
	 */
	public ItemStore(Item item, Integer quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	/**
	 * Returns the available Quantity of the specified Item Entry.
	 *
	 * @return amount of given Item.
	 */
	public Integer getStock() {
		return quantity;
	}

	/**
	 * Returns the item associated with the store entry.
	 *
	 * @return Item in the store entry.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Adds the given amount of items to the Item Store.
	 *
	 * @param amount integer amount to add
	 * @return amount after the addition.
	 */
	public Integer increaseStock(Integer amount) {
		quantity += amount;
		return quantity;
	}

	/**
	 * Removes the specified amount of the Item from the item store.
	 *
	 * @param amount amount to remove from the store.
	 * @return amount available after removal.
	 */
	public Integer decreaseStock(Integer amount) {
		quantity -= amount;
		return quantity;
	}

	// Functions for TableView

	/**
	 * Returns an ImageView to be used within a TableView.
	 */
	public ImageView getSprite() {
		ImageView imageview = new ImageView();
		imageview.setFitHeight(50);
		imageview.setFitWidth(50);
		imageview.setImage(this.item.getSprite().getFrame());
		return imageview;
	}

	/**
	 * @return the amount of items in the store.
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @return the item name.
	 */
	public String getName() {
		return item.getName();
	}

	/**
	 * @return returns the item description
	 */
	public String getDescription() {
		return item.getDescription();
	}

	/**
	 * @return the Items monetary value.
	 */
	public Integer getValue() {
		return item.getValue();
	}

	/**
	 * @return the ItemStore's Item Type.
	 */
	public ItemType getType() {
		return item.getType();
	}

}
