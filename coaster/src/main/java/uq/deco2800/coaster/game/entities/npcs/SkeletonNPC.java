package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Jump;
import uq.deco2800.coaster.game.entities.weapons.GenericSpear;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;
import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.items.ItemRegistry;

public class SkeletonNPC extends MeleeEnemyNPC implements AttackableNPC {
	private long directionChangeTracker = 300; //ticks before changing direction
	private boolean idleMoveRight; //true if right, false if left
	private long firingRateTracker; // ticks between firing
	private int delayAttack = 1;
	private int spearSpeed;
	int spearDamage;
	private int cooldown;

	/**
	 * Constructor for the SkeletonNPC
	 */
	public SkeletonNPC() {
		/* Define basic properties */

		myType = NPCType.ENEMY;
		maxHealth = 100;
		currentHealth = maxHealth;
		terminalVelocityModifier = 0.1f;

		/* Set sprites */
		// there is a skeleton attack movement SpriteList.SKELETON_ATTACK
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.SKELETON_ATTACK));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.SKELETON));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.SKELETON_FLY));

		/* Define proportions and attributes */
		bounds = new AABB(posX, posY, 1.5f, 2f);
		bounds.setActive(false);

		commonHitboxes.clear();
		commonHitboxes.add(defineHitbox(BodyPart.MAIN, 0.75f, 0.75f, 0.5f, 0.15f));
		commonHitboxes.add(defineHitbox(BodyPart.HEAD, 1f, 2f, 0.25f, 0f));
		setState(EntityState.JUMPING);

		hitboxesCache.clear();
		hitboxesCache.put(EntityState.STANDING, commonHitboxes);
		hitboxesCache.put(EntityState.JUMPING, commonHitboxes);

		moveSpeedHor = 2;
		visionRangeHor = 20;
		visionRangeVer = 20;
		attackRangeHor = 1;
		attackRangeVer = 1;
		firingRateTracker = 30;
		spearSpeed = 20 * (int) difficultyScale;
		spearDamage = 2 * (int) difficultyScale;
		cooldown = 200;
		availableActions.add(new DefaultMove(this));
		availableActions.add(new Jump(this));
		this.idleMoveRight = determineIdleDirection();
		soundType = NPCSound.LOW;
	}

	/**
	 * Called each tick to determine the behaviour of this SkeletonNPC
	 */
	@Override
	protected void stateUpdate(long ms) {
		determineNextAction();
	}

	/**
	 * Unique logic for this NPC. Called every tick. This method targets the
	 * plater, handles jumping and handles firing spears
	 */
	@Override
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

		// use hyper jump
		if (cooldown == 0) {
			setSprite(sprites.get(EntityState.JUMPING));
			jumpSpeed = -35;
			availableActions.get(1).execute();
			cooldown = 500;
		} else {
			jumpSpeed = -15;
			cooldown--;
		}

		// The skeleton only can throw the spear when it jump
		// when the spear hit the ground or player. The NPC will teleport to the spear
		if (!this.onGround) {
			setSize(2f, 2f);
			setState(EntityState.JUMPING);
			if (target.getY() - this.getY() > 7) {
				setFiringFactors(target);
				if (this.firingRateTracker == 0) {
					/* Generate spear */
					GenericSpear spear = new GenericSpear(this, xFactor, yFactor, spearSpeed, spearDamage,
							SpriteList.SPEAR);
					world.addEntity(spear);
					firingRateTracker = 30;
				} else {
					firingRateTracker--;
				}
			}
		}

		/* Engage player if within range */
		if (withinRange) {
			if (withinAttackRange && delayAttack == 0) {
				setSize(2.351f, 2f);
				setSprite(sprites.get(EntityState.STANDING));
				if (delayAttack == 0) {
					target.addHealth(-4, this);
					delayAttack = 50;
				}
			}
			determineFacingDirection(target, false);
		} else { //idle
			if (this.onGround) { //do nothing while jumping
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
	 * Returns the target which this SkeletonNPC will target first.
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
		super.onDeath(cause);
		if (isBoss) {
			((Player) cause).addBossKill("SKELETON BOSS");
		}
		ItemDrop.drop(ItemRegistry.getItem("bone"), this.getX(),  this.getY());
		ItemDrop.drop(ItemRegistry.getItem("wood"), this.getX(),  this.getY());
	}

	@Override
	/**
	 * Top-level method to run every game tick.
	 */
	protected void tick(long ms) {
		stateUpdate(ms);
	}

	@Override
	public void setBoss() {
		super.setBoss();
		spearDamage *= 5;
	}
}
