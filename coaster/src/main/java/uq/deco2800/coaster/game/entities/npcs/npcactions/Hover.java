package uq.deco2800.coaster.game.entities.npcs.npcactions;

import uq.deco2800.coaster.game.entities.npcs.ActionExecution;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;

public class Hover implements ActionExecution {

	private BaseNPC parent;

	public Hover(BaseNPC parent) {
		this.parent = parent;
	}

	@Override
	public void execute() {
		defaultHover();
	}

	void defaultHover() {
		//Move the NPC in the world - if we go with separating out the renderer then this should
		//			just be determining the next position on the grid.
		/* Calculate next position based on move speed */
		/*float yPos = NPC.getY();
		
		yPos -= NPC.getMoveSpeedHor() * 20; //Replace with appropriate figures e.g. bounds
		NPC.setPosition(NPC.getX(), yPos);*/

		parent.setVelocity((parent.getCurrentTarget().getX() - parent.getX())/3, (parent.getCurrentTarget().getY() - parent.getY())/3);
	}
}

