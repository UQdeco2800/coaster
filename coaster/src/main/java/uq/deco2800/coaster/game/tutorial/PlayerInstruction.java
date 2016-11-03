package uq.deco2800.coaster.game.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.Difficulty;
import uq.deco2800.coaster.game.tutorial.BankerScenario;
import uq.deco2800.coaster.game.tutorial.BasicActionScenario;
import uq.deco2800.coaster.game.tutorial.CoinDropScenario;
import uq.deco2800.coaster.game.tutorial.CompanionScenario;
import uq.deco2800.coaster.game.tutorial.CompanionUpgrade;
import uq.deco2800.coaster.game.tutorial.KillEnemyScenario;
import uq.deco2800.coaster.game.tutorial.KillEnemySwarm;
import uq.deco2800.coaster.game.tutorial.MountScenario;
import uq.deco2800.coaster.game.tutorial.Scenario;
import uq.deco2800.coaster.game.tutorial.ShieldScenario;
import uq.deco2800.coaster.game.tutorial.TutorialConclusion;
import uq.deco2800.coaster.graphics.Window;

/**
 * A class for organising scenarios in the player tutorial.
 */
public class PlayerInstruction {
	// the active scenario index
	private int activeScenario = 0;
	// a list of scenarios
	List<Scenario> scenarios = new ArrayList<Scenario>();

	public PlayerInstruction(Player player) {
		// add in tutorial "scenarios" here
		BasicActionScenario skipTutorial = new BasicActionScenario(GameAction.SKIP_TUTORIAL);
		skipTutorial.setPrompt("\n\nHey there! Ready to learn how to play? \n Press the \""
				+ ControlsKeyMap.getStyledKeyCode(GameAction.SKIP_TUTORIAL)
				+ "\" key to skip a step in this tutorial. Try it now. I can wait.");
		scenarios.add(skipTutorial);

		List<GameAction> actions = Arrays.asList(GameAction.MOVE_LEFT, GameAction.MOVE_RIGHT);
		for (GameAction action : actions) {
			scenarios.add(new BasicActionScenario(action));
		}
		List<GameAction> actionsSecondary = Arrays.asList(GameAction.SLIDE, GameAction.JUMP, GameAction.WEAPON_TWO,
				GameAction.BASIC_ATTACK, GameAction.WEAPON_ONE, GameAction.BASIC_ATTACK, GameAction.SHOW_MAP,
				GameAction.INVENTORY, GameAction.SKILL_TREE_UI);
		for (GameAction action : actionsSecondary) {
			scenarios.add(new BasicActionScenario(action));
		}
		scenarios.add(new ShieldScenario(player));
		scenarios.add(new MountScenario(player));
		scenarios.add(new KillEnemyScenario(player));
		scenarios.add(new KillEnemySwarm(player));
		scenarios.add(new CoinDropScenario(player));
		scenarios.add(new BankerScenario(player));
		scenarios.add(new CompanionScenario(player));
		scenarios.add(new CompanionUpgrade(player));
		scenarios.add(new TutorialConclusion(player));
	}

	/**
	 * Returns whether the tutorial has passed.
	 * 
	 * @return whether the tutorial has passed
	 */
	public boolean tutorialPassed() {
		return scenarios.get(activeScenario).checkScenarioCompleted();
	}

	/**
	 * Moves the player onto the next scenario and checks for tutorial
	 * completion.
	 */
	public void nextCommand() {
		activeScenario++;
		if (checkCompletion()) {
			return;
		}
		scenarios.get(activeScenario).setUpScenario();
		scenarios.get(activeScenario).getPrompt();
	}

	/**
	 * Messages the player with the instruction.
	 */
	public void resendInstruction() {
		scenarios.get(activeScenario).getPrompt();
	}

	/**
	 * Checks whether the tutorial has completed and starts a game if so.
	 * 
	 * @return returns whether the tutorial has completed
	 */
	public boolean checkCompletion() {
		if (activeScenario == scenarios.size()) {
			Window.getEngine().stop();
			Window.getEngine().setTutorialMode(false);
			Window.getEngine().setDifficulty(Difficulty.EASY);
			Window.initGame();
			return true;
		}
		return false;
	}
}
