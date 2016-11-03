package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;

/**
 * This scenario ends the tutorial
 */
public class TutorialConclusion extends Scenario {

	public TutorialConclusion(Player player) {
		super(player);
		this.prompt = "\n\nAmazing! You now know just enough to begin your adventure."
				+ "\nThere is much to discover! Go forth! If you need any reminders, \n"
				+ "check out the controls menu or come visit me again. Press \n" + "\""
				+ ControlsKeyMap.getStyledKeyCode(GameAction.SKIP_TUTORIAL) + "\" to quit. I'll be waiting.";
	}

	/**
	 * No set up is required.
	 */
	@Override
	public void setUpScenario() {
		/* No set up */
	}

	/**
	 * This scenario is complete once the player presses the skip tutorial
	 * button.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		return InputManager.getActionState(GameAction.SKIP_TUTORIAL);
	}
}
