package uq.deco2800.coaster.game.entities;

import java.util.List;

import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.notifications.IngameText;

/**
 * The entity class for the coin that enemies can drop.
 *
 * @author James
 */
public class CoinDrop extends ItemEntity {
	private int coinValue;

	/**
	 * Sets the value and initial velocity of the coin.
	 *
	 * @param item The Item that holds the basic information for the coin
	 * @param value How much the coin should be worth
	 */
	public CoinDrop(Item item, int value) {
		super(item, 1f, 1f);
		this.coinValue = value;
		this.setVelocity(0, -20);
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity instanceof Player || entity instanceof CompanionNPC) {
				SoundCache.play("coin");
				if (entity instanceof Player) {
					((Player) entity).getCommerce().addGold(coinValue);
					((Player) entity).addCoinCount(coinValue);
				} else {
					((CompanionNPC) entity).getOwner().getCommerce().addGold(coinValue);
					((CompanionNPC) entity).getOwner().addCoinCount(coinValue);
				}
				IngameText ingameText = new IngameText("+" + coinValue + " G", this.getX(), this.getY(), 2000,
						IngameText.textType.DYNAMIC, 0.7, 0.7, 0, 1);
				world.addIngameText(ingameText);
				this.delete();
			}
		}
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// When it collides with the ground make velocity zero
	}
}