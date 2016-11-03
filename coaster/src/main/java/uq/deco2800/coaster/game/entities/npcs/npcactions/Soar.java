package uq.deco2800.coaster.game.entities.npcs.npcactions;

import java.util.Random;

import uq.deco2800.coaster.game.entities.npcs.ActionExecution;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;

public class Soar implements ActionExecution {

	private BaseNPC parent;

	public Soar(BaseNPC parent) {
		this.parent = parent;
	}

	@Override
	public void execute() {
		defaultSoar();
	}

	void defaultSoar() {
		Random random = new Random();
		parent.setVelocity((0.5f - random.nextFloat())*2, -5);
	}
}

