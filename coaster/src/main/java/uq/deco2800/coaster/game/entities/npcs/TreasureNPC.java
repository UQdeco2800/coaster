package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Jump;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * TreasureNPC is the base class for Enemy NPCs classed as Ranged attackers. TreadureNPC extends from BaseNPC and
 * includes its associated methods. It implements AttackableNPC in order for it to target players. It provides fields
 * for NPC attributes and implements an defined attacking structure. It provides public methods to retrieve such
 * information about the NPC as well as those to manipulate NPC and its attributes. Additionally it contains default
 * methods for implementing behavior which is similar across multiple NPCs, and helper methods to determine such
 * behavior.
 *
 * NPC attacking behaviour is based on a targeting system that looks for the closest entities to engage with
 * projectiles.
 */
public class TreasureNPC extends RangedEnemyNPC implements AttackableNPC {
	private int selfDestructTime = 500;
	private int explosionTime = 30;

	/**
	 * Constructor for the TreasureNPC
	 */
	public TreasureNPC() {
		/* Define basic properties */
		myType = NPCType.ENEMY;
		maxHealth = 100;
		currentHealth = maxHealth;

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.EXPLOSION));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.CHEST));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.CHEST));
		setSprite(sprites.get(EntityState.MOVING));

		/* Define proportions and attributes */
		bounds = new AABB(posX, posY, 1f, 1f);
		
		moveSpeedHor = (int) (2 * difficultyScale);
		visionRangeHor = 20;
		visionRangeVer = 20;
		jumpSpeed = -15;
		availableActions.add(new DefaultMove(this));
		availableActions.add(new Jump(this));

		/* Choose random travelling direction */
		if (Math.random() <= 0.5) {
		} else {
		}
	}

	/**
	 * Called each tick to determine the behaviour of this MeleeEnemyNPC
	 */
	@Override
	protected void stateUpdate(long ms) {
		determineNextAction();
	}

	@Override
	/**
	 * Unique logic for this NPC. Called every tick, this method determines how the NPC should behave right now.
	 * Whether that be from deriving a new action based on the current state, or completing the sequence of actions
	 * defined for this NPC in a previous call to this method. The priority is completing the actions already in the
	 * queue.
	 *
	 * This method calculates the RangedEnemyNPC's movements based on player proximity and attempts to flee the player
	 * before it can be killed
	 */
	public void determineNextAction() {
		/* Target the player */
		Player target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);

		/* Check range of player */
		boolean withinRange = distanceFromPlayer < Math.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer);
		if (withinRange && !target.getInvisible()) { //retreating and not invisible
			determineFacingDirection(target, true);
		}
		
		
		/* Destruction Logic */
		if (selfDestructTime == 0) {
			selfDestruct();
		} else {
			selfDestructTime--;
		}
		/* Animate NPC */
		if (shouldJump(this.getFacing())) {
			availableActions.get(1).execute();
		} else {
			availableActions.get(0).execute();
		}
	}

	/**
	 * Returns the target which this MeleeEnemyNPC will target first. Currently
	 *
	 * @return the closest Player if one exists
	 */
	@Override
	public Player determinePriorityTarget() {
		return (Player) this.getClosest(new ArrayList<>(world.getPlayerEntities()));
	}


	@Override
	/**
	 * Top-level method to run every game tick.
	 */
	protected void tick(long ms) {
		stateUpdate(ms);
	}


	/**
	 * Destroy the NPC if the explosion timer reaches 0. If not, decrement it.
	 */
	public void selfDestruct() {
		if (explosionTime == 0) {
			this.delete();
		} else {
			moveSpeedHor = (int) 0f;
			setSprite(sprites.get(EntityState.STANDING));
			explosionTime--;
		}
	}
}
