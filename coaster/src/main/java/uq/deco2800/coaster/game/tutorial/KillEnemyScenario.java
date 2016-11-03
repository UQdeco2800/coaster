package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.BatNPC;

/**
 * The player must kill an NPC in this scenario.
 */
public class KillEnemyScenario extends Scenario {
	// the enemy to kill in this scenario
	BaseNPC testEnemy;

	public KillEnemyScenario(Player player) {
		super(player);
		this.prompt = "\n\nTo level up you need to kill enemies to gain XP."
				+ "\nUse the mouse to aim, and hold the left button to attack.";
	}

	/**
	 * The set up for this scenario adds a NPC to kill.
	 */
	@Override
	public void setUpScenario() {
		testEnemy = new BatNPC();
		testEnemy.setPosition(player.getX() + 4, 0);
		testEnemy.setCurrentHealth(100);
		world.addEntity(testEnemy);
	}

	/**
	 * The scenario is completed when the enemy is dead.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		return testEnemy.getCurrentHealth() <= 0;
	}
}
