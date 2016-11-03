package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.game.entities.CoinDrop;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
/**
 * The player must pick up coins in this scenario. 
 */
public class CoinDropScenario extends Scenario {

	public CoinDropScenario(Player player) {
		super(player);
		this.prompt = "\n\n You've probably got some coins already. \nHere's some more you can pick up, "
				+ "by walking over them.\n You'll have to dismount with  " 
				+ ControlsKeyMap.getStyledKeyCode(GameAction.ACTIVATE_MOUNT) + " though.";
	}

	/**
	 * Places coins for the player to pick up.
	 */
	@Override
	public void setUpScenario() {
		for (int i = 0; i < 40; i++) {
			Item coin = ItemRegistry.getItem("Coin1");
			CoinDrop coinDrop = new CoinDrop(coin, 5);
			coinDrop.setPosition(player.getX() + i, 0);
			coinDrop.setRemoveDelay(100000);
			world.addEntity(coinDrop);
		}
	}

	/**
	 * Checks if the player has picked up the coins.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		int commerce = player.getCommerce().getGold();
		if (commerce > 199) {
			return true;
		}
		return false;
	}
}
