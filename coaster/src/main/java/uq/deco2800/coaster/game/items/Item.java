package uq.deco2800.coaster.game.items;

import uq.deco2800.coaster.graphics.sprites.Sprite;

/**
 * A class that aims to serve as the base for all items (things that can be in an inventory) for the game. It is
 * intended that the class be extended with additional functionality as required per item type. <p> In addition to
 * acting as a base Class for all items, this class also keeps a registry of all possible items - this way only one
 * instance of each item is available. Some general methods are also provided including the ability to get Item
 * information based on the Items id and additionally functions to randomly return an item. <p> <p> <h2>Adding Items to
 * the registry</h2> As long as an a game item is an instance of Item (whether that be directly) or through a class than
 * extends it - it can be added to the registry. To add a new item to the registry simply add another entry to the
 * generateItems() function. <p> <p> A demonstration of creating a new item and adding it to the registry is given
 * below. <p> <p> addRegistryItem(new Item.Builder("Health1", "Health", new Sprite(SpriteList.HEALTH), "Heals Player",
 * ItemType.POWERUP).rarity(5) .tradeable(false).stackable(false).build()); <p> <p> } </pre> Misc. Notes: <ul> <li>This
 * class uses the Builder Pattern</li> </ul>
 *
 * @see ItemType
 */

public class Item {

	// General properties that are available to all items
	// Mandatory properties
	private String id;
	private String name;
	private Sprite sprite;
	private String description;
	private ItemType type;
	// Optional Properties
	private int rarity;
	private boolean tradeable;
	private boolean stackable;
	private boolean consumable;
	private boolean degradable;

	protected Item(Builder builder) {
		id = builder.id;
		name = builder.name;
		sprite = builder.sprite;
		description = builder.description;
		type = builder.type;
		rarity = builder.rarity;
		tradeable = builder.tradeable;
		stackable = builder.stackable;
		consumable = builder.consumable;
		degradable = builder.degradable;
	}

	public boolean equals(Item item) {
		return null == item || id.equals(item.id) &&
				name.equals(item.name) &&
				sprite.getSpriteId() == item.sprite.getSpriteId() &&
				description.equals(item.description) &&
				type.name().equals(item.type.name()) &&
				rarity == item.rarity &&
				tradeable == item.tradeable &&
				consumable == item.consumable &&
				degradable == item.degradable;
	}

	// Getters
	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public String getDescription() {
		return description;
	}

	public ItemType getType() {
		return type;
	}

	public int getValue() {
		return rarity;
	}

	public double getRarity() {
		return 1.0 / rarity;
	}

	public boolean isTradeable() {
		return tradeable;
	}

	public boolean isStackable() {
		return stackable;
	}

	public boolean isConsumable() {
		return consumable;
	}

	public boolean isDegradable() {
		return degradable;
	}

	protected static class Builder {
		// Mandatory properties
		private String id;
		private String name;
		private Sprite sprite;
		private String description;
		private ItemType type;
		// Optional Properties
		private int rarity = 0;
		private boolean tradeable = true;
		private boolean stackable = true;
		private boolean consumable = false;
		private boolean degradable = false;

		/**
		 * The main builder for the Item class - all parameters and invocation is mandatory to create a new item.
		 *
		 * @param string      the items unique id.
		 * @param name        the name of the item.
		 * @param sprite      the sprite for the item.
		 * @param description a description for the item.
		 * @param type        the items type.
		 */
		public Builder(String string, String name, Sprite sprite, String description, ItemType type) {
			this.id = string;
			this.name = name;
			this.sprite = sprite;
			this.description = description;
			this.type = type;
		}

		/**
		 * Set the rarity of an item.
		 *
		 * @param value int - higher value denotes rarer item the actual rarity if 1/rarity so for a 1 in 10000 chance
		 *              of spawning, passed in value should be 10,000
		 */
		public Builder rarity(int value) {
			rarity = value;
			return this;
		}

		/**
		 * Sets whether the item is tradeable.
		 *
		 * @param value boolean
		 */
		public Builder tradeable(boolean value) {
			tradeable = value;
			return this;
		}

		/**
		 * Sets whether the object can be stacked in the user inventory.
		 *
		 * @param value - true or false
		 */
		public Builder stackable(boolean value) {
			stackable = value;
			return this;
		}

		/**
		 * Set whether the object can be consumed in some way.
		 *
		 * @param value boolean
		 */
		public Builder consumable(boolean value) {
			consumable = value;
			return this;
		}

		/**
		 * Set whether the item degrades over time
		 *
		 * @param value boolean
		 */
		public Builder degradable(boolean value) {
			degradable = value;
			return this;
		}

		/**
		 * Completes the building of the item.
		 *
		 * @return Item - returns the generated item.
		 */
		public Item build() {
			return new Item(this);
		}

	}
}
