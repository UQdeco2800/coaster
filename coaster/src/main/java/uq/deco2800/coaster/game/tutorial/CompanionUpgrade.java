package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.game.entities.Player;

public class CompanionUpgrade extends Scenario {

	public CompanionUpgrade(Player player) {
		super(player);
		this.prompt = "\n\nI've given you some gold. Try upgrading your companion."
				+ "\nPress \"U\" to do so. He can be upgraded twice, try it!";
	}

	/**
	 * Adds enough gold for companion.
	 */
	@Override
	public void setUpScenario() {
		player.setCompanion(true);
		player.getCommerce().addGold(900);
	}

	/**
	 * Scenario is completed when the companions level is not 3.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		if (player.getCompanionNPC().getNPCLevel() != 3) {
			return false;
		} else {
			return true;
		}
	}
}
