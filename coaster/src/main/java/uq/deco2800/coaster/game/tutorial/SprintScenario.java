package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
/**
 * The player must sprint in this scenario
 */
public class SprintScenario extends Scenario {

	public SprintScenario(Player player) {
		super(player);
		this.prompt = "\n\nMove around faster by sprinting. Double tap the \""
				+ ControlsKeyMap.getStyledKeyCode(GameAction.MOVE_LEFT) + "\" key" + "\nor the \""
				+ ControlsKeyMap.getStyledKeyCode(GameAction.MOVE_RIGHT) + "\" key to try it.";
	}
	
	/**
	 * No set up is required for this scenario.
	 */
	@Override
	public void setUpScenario() {
		// no set up
	}

	/**
	 * Once the player is sprinting this scenario is complete.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		return player.getCurrentState().equals(EntityState.SPRINTING);
	}
}
