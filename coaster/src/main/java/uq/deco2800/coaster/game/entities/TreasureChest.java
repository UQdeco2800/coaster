package uq.deco2800.coaster.game.entities;
 
import java.util.List;
import java.util.Random;

import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.mechanics.BodyPart;

/**
.
 */
public class TreasureChest extends ItemEntity {
	
	/**
	 * Constructor for the TreasureChest
	 */
	public TreasureChest(Item item) {
		/* Define proportions and attributes */
		super(item, 1f, 1f);
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity instanceof Player) {
				Random rn = new Random();
				int random = rn.nextInt(100);
				// generate only weapon
				if (random <= 50){
					int ammoCount = rn.nextInt(20)+10;
					Item ammo = ItemRegistry.getItem("ammo");
					QuantityDrop ammoDrop = new QuantityDrop(ammo, ammoCount);
					ammoDrop.setPosition(this.getX(), this.getY() - 5);
					world.addEntity(ammoDrop);
				} else {
					Item item = ItemRegistry.getItem("health1");
					 PowerUp health = new PowerUp(item, "health", 50f, -1, 10000);
					 health.setPosition(this.getX(), this.getY() - 5);
					world.addEntity(health);
				}
			}
		}
		this.delete();
	}
}
