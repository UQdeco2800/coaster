package uq.deco2800.coaster.game.entities.npcs.npcactions;

import uq.deco2800.coaster.game.entities.npcs.ActionExecution;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;

public class DefaultMove implements ActionExecution {

	protected BaseNPC parent;

	public DefaultMove(BaseNPC parent) {
		this.parent = parent;
	}

	@Override
	public void execute() {
		defaultMove();
	}

	void defaultMove() {
		//Move the NPC in the world - if we go with separating out the renderer then this should
		//			just be determining the next position on the grid.
		/* Calculate next position based on move speed */
		/*float xPos = NPC.getX();
		
		xPos += NPC.getFacing() * NPC.getMoveSpeedVer() * 20; //Replace with appropriate figures e.g. bounds
		NPC.setPosition(xPos, NPC.getY());*/
		parent.setVelocity(parent.getMoveSpeedHor() * parent.getFacing(), parent.getVelY());

	}
}