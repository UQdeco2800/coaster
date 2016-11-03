package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.game.entities.Player;

public class CompanionScenario extends Scenario {

	public CompanionScenario(Player player) {
		super(player);
		this.prompt = "\n\n Your pet has come with you on this journey. \nYou can summon him by pressing \"P\"\n.";
	}

	/**
	 * Set up for this scenario is not required.
	 */
	@Override
	public void setUpScenario() {
		// nothing
	}

	/**
	 * Checks if the player has activated the companion.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		if (player.getCompanionStatus()) {
			return true;
		} else {
			return false;
		}
	}
}
