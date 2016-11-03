package uq.deco2800.coaster.game.entities;

import java.util.List;

import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.inventory.Inventory;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.world.World;

/**
 * QuantityDrop allows an Item to be dropped with a specific quantity.
 * Useful for things such as Coins, Ammo and other Items in which only
 * receiving one would not be enough.
 *
 * Created by draganmarjanovic on 23/10/2016.
 */
public class QuantityDrop extends ItemEntity {

	private int dropAmount;
	private Inventory inv = null;

	public QuantityDrop(Item item, Integer amount) {
		super(item, 1f, 1f);
		this.dropAmount = amount;
		this.setVelocity(0, -20);
	}

	public QuantityDrop(Item item, float width, float height) {
		super(item, width, height);
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (inv == null) {
			inv = World.getInstance().getFirstPlayer().getInventory();
		}
		for (Entity entity : entities) {
			if (entity instanceof Player) {
				inv.addItem(this.dropAmount,this.getItem().getID());
				this.delete();
				Toaster.toast("You picked up " + this.dropAmount + " " + this.getItem().getName());
			}

		}

	}
}
