package uq.deco2800.coaster.game.entities.npcs.npcactions;

import uq.deco2800.coaster.game.entities.npcs.ActionExecution;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;

/**
 * A component of the NPC action list and command pattern for NPCs.
 */
public class WormMove extends DefaultMove implements ActionExecution {

	/**
	 * Constructor for the WormMove()
	 * @param parent The entity (NPC) wanting to execute this action. Generally a WormNPC.
	 */
	public WormMove(BaseNPC parent) {
		super(parent);
	}

	@Override
	/**
	 * This implementation requires the use of 2 extra states in which the NPC could be facing. I.e. accounts for facing
	 * upwards and downwards rather than just left and right.
	 */
	void defaultMove() {
		
		if (parent.getFacing() == 0) {
			/* Move up */
			parent.setVelocity(0, -parent.getMoveSpeedVer());
		} else if (parent.getFacing() == 2) {
			/* Move down */
			parent.setVelocity(0, parent.getMoveSpeedVer());
		} else {
			/* Move sideways */
			parent.setVelocity(parent.getFacing() * parent.getMoveSpeedHor(), 0);
		}
	}
}
