package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.CommerceNPC;

/**
 * This scenario gets the player to interact with a banker
 */
public class BankerScenario extends Scenario {
	// the banker to interact with
	CommerceNPC bankerNPC = new CommerceNPC(CommerceNPC.TraderType.BANK);

	public BankerScenario(Player player) {
		super(player);
		this.prompt = "\n\nYou can spend coins at vendors throughout the world and store coins at banks."
				+ "\nGo up to the banker and try it.";
	}

	/**
	 * The set up for this scenario, adds a banker.
	 */
	@Override
	public void setUpScenario() {
		bankerNPC.setPosition(player.getX() + 7, 50);
		world.addEntity(bankerNPC);
	}

	/**
	 * Checks if the player has talked to the banker.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		return player.getTalking();
	}
}
