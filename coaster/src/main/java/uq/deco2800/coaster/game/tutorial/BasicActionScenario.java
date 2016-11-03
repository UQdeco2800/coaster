package uq.deco2800.coaster.game.tutorial;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;

/**
 * A basic scenario that tells the player the button for an action and moves on
 * once it is pressed.
 */
public class BasicActionScenario extends Scenario {
	// the game action to test
	private GameAction action;

	/**
	 * Initialises the action and prompt of the scenario.
	 * 
	 * @param action
	 *            the game action to task the player with.
	 */
	public BasicActionScenario(GameAction action) {
		super(null);
		this.action = action;
		this.prompt = "\n\n" + reward() + " Now press the \"" + ControlsKeyMap.getStyledKeyCode(action) + "\" key to "
				+ ControlsKeyMap.getStyledGameAction(action);
	}

	/**
	 * Checks if the correct key was pressed for the action.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		return InputManager.getActionState(action);
	}

	/**
	 * The set up for this scenario.
	 */
	@Override
	public void setUpScenario() {
		/* no setup required */
	}

	/**
	 * Returns a random phrase of encouragement
	 * 
	 * @return a random phrase of encouragement
	 */
	public String reward() {
		Random random = new Random();
		int index = random.nextInt(100);
		List<String> phrases = Arrays.asList("Excellent!", "Very impressive!", "Great work!", "You're doing well!", " ",
				" ", " ", " ");
		return phrases.get(index % phrases.size());
	}

	/**
	 * Returns the basic action
	 * 
	 * @return the action
	 */
	public GameAction getAction() {
		return action;
	}
}
