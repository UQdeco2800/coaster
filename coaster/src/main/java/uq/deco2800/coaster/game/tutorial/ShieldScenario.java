package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.PowerUp;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
/**
 * Adds a shield which the player must grab before continuing.
 */
public class ShieldScenario extends Scenario {

	public ShieldScenario(Player player) {
		super(player);
		this.prompt = "\n\nThe world is a tough place. \nYou should keep an eye out for powerups to help you out. "
				+ "\nTry grabbing this shield, it'll keep you safe for now.";
	}

	/**
	 * This scenario is set up by adding a shield.
	 */
	@Override
	public void setUpScenario() {
		Item testItem = ItemRegistry.getItem("Shield1");
		PowerUp shield = new PowerUp(testItem, "shield", 1f, 1000000, 10000000);
		shield.setPosition(player.getX() - 2, 0);
		world.addEntity(shield);

	}

	/**
	 * This scenario is complete once the shield is equipped. 
	 */
	@Override
	public boolean checkScenarioCompleted() {
		if (player.getShielded()) {
			player.setBlocksOtherEntities(false);
		}
		return player.getShielded();
	}
}
