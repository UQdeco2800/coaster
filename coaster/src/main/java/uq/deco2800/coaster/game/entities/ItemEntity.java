package uq.deco2800.coaster.game.entities;

import java.util.List;

import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.inventory.Inventory;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.LayerList;
import uq.deco2800.coaster.graphics.notifications.Toaster;

/**
 * This class is a base class created to allow the generation of any Items or
 * subclass to be able to be rendered as an interactive entity in the game
 * world. Any items with special functionality such as modifying player stats or
 * interacting with any other entity should extend this class.
 * <p>
 * <p>
 * This class takes the world and the item as a constructor. Sets the entity
 * sprite to equal what sprite was specified by the items in addition to
 * creating a AABB and setting the entity not to be hurt by projectiles
 * <p>
 * Example of use to render an item in the world.
 * <p>
 * <p>
 * ItemEntity newItem = ItemEntity(Item.getItem("Gun1"));
 * newItem.setPosition(Xposition,Yposition);
 * <p>
 * world.addentity(newItem);
 * <p>
 * <p>
 * Care should be taken to follow the standards set in the Item Class Items
 * should always be gotten from the Registry
 *
 * @author Blake
 * @version 1.0
 * @see Item
 * @see uq.deco2800.coaster.game.items.ItemType
 */

public class ItemEntity extends Entity {
	private boolean permanent = false; // If this is true, then the ItemEntity will never be removed after a timeout
	private long removeDelay = 10000;
	private Item item;
	private Inventory inv = null;

	public ItemEntity(Item item) {
		this(item, 1.5f, 1.5f);
	}

	public ItemEntity(Item item, float width, float height) {
		layer = LayerList.ITEMS;
		setSprite(item.getSprite());
		this.item = item;
		bounds = new AABB(posX, posY, width, height);
		hurtByProjectiles = false;
	}

	/**
	 * gets the remaining time
	 *
	 * @return long of milliseconds to keep around for
	 */
	public long getRemoveDelay() {
		return this.removeDelay;
	}

	/**
	 * @return the associated item
	 */
	public Item getItem() {
		return this.item;
	}

	/**
	 * Set the delay value
	 *
	 * @param d the value of the new removeDelay time
	 */
	public void setRemoveDelay(long d) {
		this.removeDelay = d;
	}

	/**
	 * Use this to toggle whether or not this entity is permanent
	 *
	 * @param permanent true if it is to be a permanent entity and false if not
	 */
	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	@Override
	protected void tick(long ms) {
		if (!permanent) {
			this.removeDelay = this.removeDelay - ms;
			if (this.removeDelay <= 0) {
				this.kill(null);
			}
		}
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (inv == null) {
			inv = World.getInstance().getFirstPlayer().getInventory();
		}
		for (Entity entity : entities) {
			if (entity instanceof Player || entity instanceof CompanionNPC) {
				if (inv.addItem(1, this.getItem().getID())) {
					String notification = "Picked up " + this.getItem().getName();
					this.delete();
					Toaster.toast(notification);
					World.getInstance().getFirstPlayer().getPlayerStatsClass().addPickUpCount();
				}
			}

		}

	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		this.setVelocity(0f, 0f);
	}

	@Override
	protected void onDeath(Entity cause) {

		this.delete();
	}

}
