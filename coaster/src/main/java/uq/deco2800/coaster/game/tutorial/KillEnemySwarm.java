package uq.deco2800.coaster.game.tutorial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.BatNPC;

/**
 * The player must kill a swarm in this scenario.
 */
public class KillEnemySwarm extends Scenario {
	// swarm to kill
	List<BaseNPC> enemies = new ArrayList<BaseNPC>();

	public KillEnemySwarm(Player player) {
		super(player);
		this.prompt = "\n\n Oh look, here come its friends. Kill them. Kill them all.";
	}

	/**
	 * This set up adds a swarm of enemies.
	 */
	@Override
	public void setUpScenario() {
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			BaseNPC testEnemy;
			testEnemy = new BatNPC();
			testEnemy.setPosition(player.getX() + i + random.nextInt(10), 0);
			testEnemy.setCurrentHealth(100);
			enemies.add(testEnemy);
			world.addEntity(testEnemy);
		}
	}

	/**
	 * This checks whether all enemies are dead.
	 */
	@Override
	public boolean checkScenarioCompleted() {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getCurrentHealth() > 0) {
				return false;
			}
		}
		player.setBlocksOtherEntities(true);
		return true;
	}
}
