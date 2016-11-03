package uq.deco2800.coaster.game.entities.npcs.npcactions;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.ActionExecution;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;

public class DefaultMeleeAttack implements ActionExecution {

	private BaseNPC parent;

	public DefaultMeleeAttack(BaseNPC parent) {
		this.parent = parent;
	}

	@Override
	public void execute() {
		defaultMeleeAttack();
	}

	void defaultMeleeAttack() {
		Player player = (Player) parent.getCurrentTarget();
		player.addHealth(-(parent.getBaseDamage()), parent);

	}
}