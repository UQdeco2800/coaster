package uq.deco2800.coaster.game.tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.notifications.Toaster;


/**
 * An abstract representation of the scenarios a player will play through in the
 * tutorial
 */
public abstract class Scenario {
	private static Logger logger = LoggerFactory.getLogger(Scenario.class);

	String prompt;
	// the world
	protected World world;
	// the player
	protected Player player;

	/**
	 * Instantiates the scenario, using the player specified.
	 *
	 * @param player the player in the tutorial
	 */
	public Scenario(Player player) {
		world = World.getInstance();
		logger.info("setting tutorial world");
		world.setTiles(world.getTutorialWorld());
		this.player = player;
	}

	/**
	 * The check made to determine whether a scenario has been completed
	 *
	 * @return returns true if the scenario has been completed
	 */
	public abstract boolean checkScenarioCompleted();

	/**
	 * Sets up the scenario in game.
	 */
	public abstract void setUpScenario();


	/*
	 * Prompts the player with the scenario prompt.
	 */

	public void getPrompt() {
		Toaster.ejectAllToast();
		Toaster.toast(prompt);
	}

	/**
	 * Defines the scenario prompt
	 *
	 * @param prompt the message to prompt the player with for the tutorial
	 */

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
