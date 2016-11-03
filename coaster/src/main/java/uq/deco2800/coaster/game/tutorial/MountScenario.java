package uq.deco2800.coaster.game.tutorial;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.mounts.JumpingMount;
import uq.deco2800.coaster.game.entities.npcs.mounts.Mount;

/**
 * The player must ride a mount in this scenario.
 */
public class MountScenario extends Scenario {

	public MountScenario(Player player) {
		super(player);
		this.prompt = "\n\nGo up to the mount and press " + ControlsKeyMap.getStyledKeyCode(GameAction.ACTIVATE_MOUNT)
				+ " to jump on.";
	}
	
	/**
	 * This scenario is set up by adding a mount. 
	 */
	@Override
	public void setUpScenario() {
		Mount testMount = new JumpingMount();
		testMount.setPosition(player.getX() + 4, 0);
		world.addEntity(testMount);
	}

	/**
	 * This scenario is completed after player has a mount. 
	 */
	@Override
	public boolean checkScenarioCompleted() {
		return player.getOnMountStatus();
	}

}
