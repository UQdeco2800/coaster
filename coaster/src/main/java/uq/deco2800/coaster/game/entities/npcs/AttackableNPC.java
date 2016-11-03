package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.Entity;

/**
 * Prescription of methods that an NPC which is attackable, either by other entities, or the player, will need to
 * implement to maintain consistency for NPC decision making across multiple unique NPCs.
 */
public interface AttackableNPC {
	// Logic for determining what the next action or set of actions for the NPC should be
	void determineNextAction();

	// For now will almost always be Player, but needed for when allies/depth of NPCs are introduced
	Entity determinePriorityTarget();

}
