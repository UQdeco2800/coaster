package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMeleeAttack;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class RhinoNPC extends BaseNPC implements AttackableNPC {

	private static final int CHARGE_SPEED_HORIZ = 10;
	private int attackDelay = 1;
	private int chargeDelay = 1;

	protected List<AABB> commonHitboxes = new ArrayList<>();

	/**
	 * Constructor for RHinoNPC
	 */
	public RhinoNPC() {
		/*
		 * Allocate the sprites for this NPC in relation to possible states the
		 * NPC could reside in
		 */
		sprites.put(EntityState.STUNNED, new Sprite(SpriteList.GREEN_RHINO_DAZED));
		sprites.put(EntityState.DEFAULT, new Sprite(SpriteList.GREEN_RHINO_WALKING));
		/* Initialise the sprite for the NPC and set the dimensions for it */
		bounds = new AABB(posX, posY, 2f, 2f);

		commonHitboxes.add(defineHitbox(BodyPart.SHIELD, 0.3f, 2f, 1.7f, 0f));
		commonHitboxes.add(defineHitbox(BodyPart.BACK, 1.7f, 2f, 0f, 0f));

		hitboxesCache.put(EntityState.STUNNED, commonHitboxes);
		hitboxesCache.put(EntityState.DEFAULT, commonHitboxes);

		setState(EntityState.DEFAULT);

		/* Initialisation of this NPC attributes and characteristics */
		myType = NPCType.ENEMY;
		maxHealth = 1000;
		currentHealth = maxHealth;
		moveSpeedHor = 1;
		moveSpeedVer = 0; // Rhino doesn't jump
		attackRangeHor = 0.5f; // Default melee attack range
		attackRangeVer = 0.5f;
		visionRangeHor = 8;
		visionRangeVer = 8;
		patrolStep = 0;
		baseDamage = (int) (40 * difficultyScale);
		soundType = NPCSound.LOW;

		/* The initial state for this NPC */
		this.setState(EntityState.DEFAULT);
		/*
		 * Add the actions available to this NPC. No jump is needed, and for now
		 * charge can be implement using defaultMove
		 */
		availableActions.add(new DefaultMove(this));
		availableActions.add(new DefaultMeleeAttack(this));

	}

	/**
	 * Top-level method to run every game tick.
	 */
	@Override
	protected void tick(long ms) {

		/* Check if we need to reset our state */
		checkStateUpdate();

		/* Determine our next action for this NPC */
		determineNextAction();

	}

	@Override
	/**
	 * Calls the original onDeath() method in BaseNPC. Cause of death could be
	 * used in particular scenarios.
	 * 
	 * @param cause Cause of this NPCs death.
	 */
	protected void onDeath(Entity cause) {
		super.onDeath();
		if (isBoss) {
			((Player) cause).addBossKill("RHINO BOSS");
		}
		ItemDrop.drop(ItemRegistry.getItem("horn"), this.getX(),  this.getY());
		ItemDrop.drop(ItemRegistry.getItem("blooddrop"), this.getX(),  this.getY());
	}

	@Override
	/**
	 * Unique logic for this NPC. Called every tick, this method determines how
	 * the NPC should behave right now. Whether that be from deriving a new
	 * action based on the current state, or completing the sequence of actions
	 * defined for this NPC in a previous call to this method. The priority is
	 * completing the actions already in the queue.
	 */
	public void determineNextAction() {

		/* Default section generally shared by most NPCs */
		NPCAction action;
		/* Check if we have actions queued */
		//Accounts for being stunned already due to setState(state, duration) adding 'NOTHING' to queue.
		if (!actionQueue.isEmpty()) {
			/* Call the action then pop it from queue */
			action = actionQueue.get(0);
			if (action.equals(NPCAction.DEFAULT_MOVE)) {
				availableActions.get(0).execute();
			} else if (action.equals(NPCAction.DEFAULT_ATTACK)) {
				availableActions.get(1).execute();
			}
			/*
			 * Remove the just completed action from the queue as it has been
			 * consumed
			 */
			actionQueue.remove(0);
			moveSpeedHor = 0; //We were stunned
			/* Actions for this tick have been completed */
			return;
		}
		/* No queued commands, generate new commandName/list */

		/* AI Logic for this NPC */

		/* Update counter variables for previously executed actions */
		if (attackDelay > 0) {
			--attackDelay;
		}
		if (chargeDelay > 0) {
			--chargeDelay;
		}
		if ((attackDelay > 100) || (chargeDelay > 100)) {
			/* Rhino pauses after completing an attack or charge */
			moveSpeedHor = 0;
			return;
		}
		/* Reset to default movement speed */
		moveSpeedHor = 1;
		/* Check if we need a new target */
		target = determinePriorityTarget();

		/* Determining relative positions between this NPC and its target */
		boolean leftOfTarget = this.posX < target.getX();
		boolean aboveTarget = (this.posY + this.getHeight() / 2) - (target.getY() + target.getHeight() / 2) < 0;

		float xDistance;
		float yDistance;

		if (leftOfTarget) {
			xDistance = target.getX() - (this.posX + this.getWidth());
		} else {
			xDistance = this.posX - (target.getX() + target.getWidth());
		}
		if (aboveTarget) {
			yDistance = target.getY() - (this.posY + this.getHeight());
		} else {
			yDistance = this.posY - (target.getY() + target.getHeight());
		}
		distanceFromPlayer = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);

		/*
		 * This NPC patrols until a player is in its vision range. In which case
		 * it charges the player
		 */
		if (distanceFromPlayer > visionRangeHor) {
			/* Patrol the area at default movement speed */
			patrolArea();
		} else {
			/* NPC has vision/knowledge of the player */
			if (chargeDelay > 0) {
				/*
				 * If we have charged recently, the rhino takes some time before
				 * doing anything else
				 */
				return;
			}

			/* Move to recognise the rhino has vision */
			int direction = leftOfTarget ? 1 : -1;
			setFacing(direction);
			setRenderFacing(direction);
			availableActions.get(0).execute();

			/* Attempt to attack */
			if ((attackDelay == 0) && (xDistance <= attackRangeHor) && (yDistance <= attackRangeVer)) {
				actionQueue.add(NPCAction.DEFAULT_ATTACK);
				attackDelay = 250;
			} else {
				/* Charge the player */
				chargeTarget();
				chargeDelay = 250;
			}

		}
		return;
	}

	@Override
	/**
	 * Based on this NPCs design, determines which target for the NPC is most
	 * fitting. Generally will return the sole Player, however each NPC could
	 * determine the player, their ally, or an enemy, based on distance,
	 * strategy or other factors.
	 * 
	 * @return The priority target for this NPC.
	 */

	public Player determinePriorityTarget() {
		return world.getFirstPlayer();
	}

	/**
	 * Defines the list of actions required to charge the target to get within
	 * attacking range. Although the charge may currently be stopped due to a
	 * stun (setState), the rhino does not determine new/other actions whilst
	 * the charge is in progress- hence use of the actionQueue.
	 */
	private void chargeTarget() {

		moveSpeedHor = CHARGE_SPEED_HORIZ;

		/*
		 * Determines the number of moves the rhino needs to get within
		 * attacking range of the player
		 */
		int moves = distanceFromPlayer / 10;

		/* Add the remaining moves required to the queue */
		for (int i = 0; i < moves - 1; i++) {
			actionQueue.add(NPCAction.DEFAULT_MOVE);
		}
		/* Execute the first move this tick */
		availableActions.get(0).execute();
	}

	@Override
	public void setBoss() {
		super.setBoss();
		baseDamage *= 5;
	}

//	@Override
//	/**
//	 * We now use hitboxes to handle this. Any projectiles hitting the front hitbox, denoted SHIELD, will do no damage
//	 * Other projectiles will do the normal amount of damage. This is handled in Projectile.onEntityCollide() and
//	 * derived methods. Grenades do full damage regardless of facing, as per comments on slack. As a result this
//	 * function is deprecated but is left in case we decide to revert
//	 *
//	 * Attempt to deal damage to this rhino. If the rhino is facing the projectile/attack, it has its shield up, and
//	 * will hence not receive any damage.
//	 *
//	 * @return The damage received by the rhino from the attack. A value of 0 typically indicates it hit the shield.
//	 */
//	public int receiveDamage(int damage, Entity cause) {
//
//		if (cause instanceof GrenadeBullet) {
//			if (((getFacing() == 1) && (cause.getPrevX() > posX)) ||
//					((getFacing() == -1) && (cause.getPrevX() < posX))) {
//				/* Receive no damage */
//				return 0;
//			}
//		} else if (((target.getX() > posX) && (getFacing() == 1)) ||
//				((target.getX() < posX) && (getFacing() == -1))) {
//			/* Receive no damage */
//			return 0;
//		}
//		/* Not facing the attack and no other damage resistance implemented. Receive as normal*/
//		return super.receiveDamage(damage, cause);
//	}
}