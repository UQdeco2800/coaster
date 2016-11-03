package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Jump;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * RangedEnemyNPC is the base class for Enemy NPCs classed as Ranged attackers.
 * RangedEnemyNPC extends from BaseNPC and includes its associated methods. It
 * implements AttackableNPC in order for it to target players. It provides
 * fields for NPC attributes and implements an defined attacking structure. It
 * provides public methods to retrieve such information about the NPC as well as
 * those to manipulate NPC and its attributes. Additionally it contains default
 * methods for implementing behavior which is similar across multiple NPCs, and
 * helper methods to determine such behavior.
 *
 * NPC attacking behaviour is based on a targeting system that looks for the
 * closest entities to engage with projectiles.
 */
public class RangedEnemyNPC extends BaseNPC implements AttackableNPC {
	private long firingRateTracker; // ticks between firing
	private int genericBulletDamage;
	private int bulletSpeed;

	/**
	 * Constructor for the RangedEnemyNPC
	 */
	public RangedEnemyNPC() {
		/* Define basic properties */
		myType = NPCType.ENEMY;
		maxHealth = 100;
		currentHealth = maxHealth;

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.SPIDER_WALKING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.SPIDER_WALKING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.SPIDER_WALKING));
		setSprite(sprites.get(EntityState.MOVING));

		/* Define proportions and attributes */
		bounds = new AABB(posX, posY, 1.5f, 1.2f);

		moveSpeedHor = (int) 2f;
		visionRangeHor = 4;
		visionRangeVer = 4;
		jumpSpeed = -15;
		firingRateTracker = 40;
		bulletSpeed = (int) (30 * difficultyScale);
		genericBulletDamage = (int) (4 * difficultyScale);

		availableActions.add(new DefaultMove(this));
		availableActions.add(new Jump(this));
		determineIdleDirection();
		soundType = NPCSound.HIGH;
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
	 * This method calculates the RangedEnemyNPC's movements based on player
	 * proximity and the NPC's location within the game
	 */
	public void determineNextAction() {
		/* Target the player */
		target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);

		/* Check if within acceptable positioning surrounding the player */
		if (this.onGround) {
		}
		boolean withinRange = distanceFromPlayer < Math
				.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer);

		determineFacingDirection(target, withinRange);

		/* Fire at player if within range */
		if (!(distanceFromPlayer > 2 * Math.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer))
				&& !isBlockedVision()) {
			setFiringFactors(target);
			if (this.firingRateTracker == 0) {
				/* Generate buller */
				GenericBullet bullet = new GenericBullet(this, xFactor, yFactor, this.bulletSpeed,
						this.genericBulletDamage, SpriteList.SPIDER_WEB);
				world.addEntity(bullet);
				firingRateTracker = 20;
			} else {
				firingRateTracker--;
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
	 * Calls the original onDeath() method in BaseNPC. Cause of death could be
	 * used in particular scenarios.
	 * 
	 * @param cause Cause of this NPCs death.
	 */
	protected void onDeath(Entity cause) {
		super.onDeath();
		if (isBoss) {
			((Player) cause).addBossKill("SPIDER BOSS");
		}
	}

	@Override
	/**
	 * Top-level method to run every game tick.
	 */
	protected void tick(long ms) {
		stateUpdate(ms);
	}

	@Override
	/**
	 * Adjusts the NPC's facing direction based on its range to the player If in
	 * range of player, it turns to face away from the player. If out of range
	 * of player, it turns to face the player.
	 *
	 * @param target The targeted player
	 * @param withinRange boolean that states if the player is within firing
	 *            range
	 */
	public void determineFacingDirection(Player target, boolean withinRange) {
		if (withinRange) { // retreating
			if (!target.getInvisible()) {
				if (this.posX < target.getX()) {
					setFacing(-1); // left
				} else {
					setFacing(1); // right
				}
			}
		} else { // attacking
			if (this.posX < target.getX()) {
				setFacing(1); // right
			} else {
				setFacing(-1); // left
			}
		}
	}
}
