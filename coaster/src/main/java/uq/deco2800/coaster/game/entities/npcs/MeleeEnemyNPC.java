package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Jump;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * MeleeEnemyNPC is the base class for Enemy NPCs classed as Melee attackers.
 * MeleeEnemyNPC extends from BaseNPC and includes its associated methods. It
 * implements AttackableNPC in order for it to target players. It provides
 * fields for NPC attributes and implements an defined attacking structure. It
 * provides public methods to retrieve such information about the NPC as well as
 * those to manipulate NPC and its attributes. Additionally it contains default
 * methods for implementing behavior which is similar across multiple NPCs, and
 * helper methods to determine such behavior.
 * 
 * NPC attacking behaviour is based on a targeting system that looks for the
 * closest entities to engage, and engages them through physical contact.
 */

public class MeleeEnemyNPC extends BaseNPC implements AttackableNPC {
	private long directionChangeTracker = 300; //ticks before changing direction
	private boolean idleMoveRight; //true if right, false if left

	protected List<AABB> commonHitboxes = new ArrayList<>();

	private int delayAttack = 1;

	/**
	 * Constructor for the MeleeEnemyNPC
	 */
	public MeleeEnemyNPC() {
		/* Define basic properties */
		myType = NPCType.ENEMY;
		maxHealth = 100;
		currentHealth = maxHealth;

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.SLIME));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.SLIME));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.SLIME));

		/* Define proportions and attributes */
		bounds = new AABB(posX, posY, 2f, 2f);
		bounds.setActive(false);

		commonHitboxes.add(defineHitbox(BodyPart.MAIN, 2f, 1.4f, 0f, 0.6f));

		hitboxesCache.put(EntityState.STANDING, commonHitboxes);
		hitboxesCache.put(EntityState.MOVING, commonHitboxes);
		hitboxesCache.put(EntityState.JUMPING, commonHitboxes);

		setState(EntityState.MOVING);

		moveSpeedHor = (int) 2f;
		visionRangeHor = 8;
		visionRangeVer = 8;
		attackRangeHor = 1;
		attackRangeVer = 1;
		jumpSpeed = -15;

		baseDamage = (int) (10 * difficultyScale);

		availableActions.add(new DefaultMove(this));
		availableActions.add(new Jump(this));
		this.idleMoveRight = determineIdleDirection();
		soundType = NPCSound.LOW;
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
	 * Unique logic for this NPC. Called every tick, this method determines how
	 * the NPC should behave right now. Whether that be from deriving a new
	 * action based on the current state, or completing the sequence of actions
	 * defined for this NPC in a previous call to this method. The priority is
	 * completing the actions already in the queue.
	 *
	 * This method calculates the MeleeEnemyNPC's movememnts based on player
	 * proximity and the NPC's location within the game
	 */
	public void determineNextAction() {
		/* Target the player */
		Player target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);

		/* Check range of player */
		boolean withinRange = distanceFromPlayer < Math
				.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer);
		boolean withinAttackRange = (distanceFromPlayer <= attackRangeHor) && (yDistance <= attackRangeVer);

		/* Reduce delay counter */
		if (delayAttack > 0) {
			--delayAttack;
		}

		/* Engage player if within range */
		if (withinRange) {
			if (withinAttackRange && delayAttack == 0) {
				target.addHealth(-(int) baseDamage, this);
				delayAttack = 100;
			}
			determineFacingDirection(target, false);
		} else { //idle
			if (!this.onGround) { //do nothing while jumping
				directionChangeTracker--;
				return;
			} else if (directionChangeTracker <= 0) {
				this.idleMoveRight = !this.idleMoveRight;
				setFacing(this.getFacing() * -1);
				this.directionChangeTracker = 300;
			} else {
				this.directionChangeTracker--;
			}
		}

		/* Animate NPC */
		if (shouldJump(this.getFacing())) {
			availableActions.get(1).execute();
		} else {
			availableActions.get(0).execute();
		}
	}

	/**
	 * Returns the target which this MeleeEnemyNPC will target first.
	 *
	 * @return the closest Player if one exists
	 */
	@Override
	public Player determinePriorityTarget() {
		return (Player) this.getClosest(new ArrayList<>(world.getPlayerEntities()));
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
		if (isBoss && cause instanceof Player) {
			((Player) cause).addBossKill("SANDSLIME BOSS");
		}
	}

	@Override
	/**
	 * Top-level method to run every game tick.
	 */
	protected void tick(long ms) {
		stateUpdate(ms);
	}
}
