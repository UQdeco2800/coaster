package uq.deco2800.coaster.game.entities;

import java.util.List;

import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.inventory.Inventory;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.notifications.IngameText;

/**
 * Class that is responsbile for weapons dropping from mobs
 *
 * @author Rohan
 */
public class WeaponDrop extends ItemEntity {
	private Item setItem;

	private Inventory inv = World.getInstance().getFirstPlayer().getInventory();

	/**
	 * Sets the weapon when specified an ID
	 *
	 * @param item
	 *            The Item that holds the information for the weapon
	 */
	public WeaponDrop(Item item) {
		super(item, 1f, 1f);
		this.setItem = item;
		this.setVelocity(0, -20);
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity instanceof Player || entity instanceof CompanionNPC) {
				if (inv.addItem(1, setItem.getID())) {
					this.delete();
					IngameText ingameText = new IngameText("Picked up " + this.getItem().getName(), 0,
							0, 2000, IngameText.textType.STATIC, .255, .161, .20, 1);
					world.addIngameText(ingameText);
				}

			}

		}

	}

}