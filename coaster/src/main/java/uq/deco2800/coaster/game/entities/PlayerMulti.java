package uq.deco2800.coaster.game.entities;

import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.GameInput;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.entities.particles.Particle;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.Weapon;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.Viewport;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.sprites.AngledSpriteRelation;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;
import uq.deco2800.singularity.common.representations.coaster.state.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static uq.deco2800.coaster.core.input.InputManager.justPressed;

public class PlayerMulti extends Player {

	GameInput playerInput = new GameInput();
	PlayerStats stats = new PlayerStats();
	protected ArrayList<String> activeWeapons = new ArrayList<>();

	protected boolean host;
	// mobs for the boss

	protected int prevHealth;

	protected boolean hasFired;


	/**
	 * The Player class is the entity controlled by the user.
	 */
	public PlayerMulti(boolean host, String name) {
		if (name.equals("")) {
			throw new IllegalArgumentException("'String name' cannot be empty.");
		}
		this.host = host;
		this.name = name;
		setCollisionFilter(e -> this.knockBackTimer < 0);

		sprites.put(EntityState.STANDING, new Sprite(SpriteList.KNIGHT_STANDING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.KNIGHT_JUMPING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.KNIGHT_WALKING));
		sprites.put(EntityState.WALL_SLIDING, new Sprite(SpriteList.KNIGHT_WALLSLIDE));
		sprites.put(EntityState.DASHING, new Sprite(SpriteList.KNIGHT_DASH));
		sprites.put(EntityState.AIR_DASHING, new Sprite(SpriteList.KNIGHT_DASH));
		sprites.put(EntityState.SLIDING, new Sprite(SpriteList.KNIGHT_SLIDE));
		sprites.put(EntityState.CROUCHING, new Sprite(SpriteList.KNIGHT_CROUCH));
		sprites.put(EntityState.AIR_CROUCHING, new Sprite(SpriteList.KNIGHT_CROUCH));
		sprites.put(EntityState.SPRINTING, new Sprite(SpriteList.KNIGHT_SPRINT));
		sprites.put(EntityState.DEAD, new Sprite(SpriteList.KNIGHT_SLIDE));
		sprites.put(EntityState.KNOCK_BACK, new Sprite(SpriteList.KNIGHT_KNOCK_BACK));

		this.enableManaBar();
		this.addMana(100);
		this.healing = 1;

		setSprite(sprites.get(EntityState.JUMPING));
		bounds = new AABB(posX, posY, BASE_WIDTH, BASE_HEIGHT); // Size is 1x2 for now

		// dud implementation of the obtaining weapons
		// Subject to change with inventory and weapon drop progression

		activeWeapons.add(ItemRegistry.getItem("Gun1").getID());
		activeWeapons.add(ItemRegistry.getItem("Melee1").getID());

		// Represents the currently equipped weapon.
		equippedWeapon = (Weapon) ItemRegistry.getItem("Gun1");

		// adjust firing Rate - currently average of player stats and native gun
		this.firingRateTracker = (stats.getFiringRate() + equippedWeapon.getFiringRate()) / 2;
		this.moveSpeed = BASE_MOVE_SPEED;
		this.jumpSpeed = BASE_JUMP_SPEED;

		// Add sprites for arm and head
		commonSpriteSet.put(BodyPart.HEAD, new AngledSpriteRelation(new Sprite(SpriteList.KNIGHT_HEAD), this,
				headRenderAngle, 0.0f, 0.05f, 0.8f, 0.7f, 0.5f, 0.5f));
		commonSpriteSet.put(BodyPart.ARM, new AngledSpriteRelation(new Sprite(SpriteList.KNIGHT_ARM), this,
				armRenderAngle, 0.25f, 0.7f, 0.5f, 0.5f, 0.1f, 0.075f));

		sprintSpriteSet.put(BodyPart.HEAD, new AngledSpriteRelation(new Sprite(SpriteList.KNIGHT_HEAD), this,
				headRenderAngle, 0.0f, 0.05f, 0.8f, 0.7f, 0.5f, 0.5f));

		// Add this for standing, jumping and moving
		this.additionalSpritesCache.put(EntityState.STANDING, commonSpriteSet);
		this.additionalSpritesCache.put(EntityState.JUMPING, commonSpriteSet);
		this.additionalSpritesCache.put(EntityState.MOVING, commonSpriteSet);
		this.additionalSpritesCache.put(EntityState.SPRINTING, sprintSpriteSet);

		commonHitboxes.add(defineHitbox(BodyPart.HEAD, 0.8f, 0.7f, 0.0f, 0.05f));

		this.hitboxesCache.put(EntityState.STANDING, commonHitboxes);
		this.hitboxesCache.put(EntityState.JUMPING, commonHitboxes);
		this.hitboxesCache.put(EntityState.MOVING, commonHitboxes);
		this.hitboxesCache.put(EntityState.WALL_SLIDING, commonHitboxes);
		this.hitboxesCache.put(EntityState.DASHING, commonHitboxes);
		this.hitboxesCache.put(EntityState.AIR_DASHING, commonHitboxes);
		this.hitboxesCache.put(EntityState.SPRINTING, commonHitboxes);
		this.hitboxesCache.put(EntityState.DEAD, commonHitboxes);
		this.hitboxesCache.put(EntityState.KNOCK_BACK, commonHitboxes);

		this.hitboxesCache.put(EntityState.SLIDING, slidingHitboxes);

		this.hitboxesCache.put(EntityState.CROUCHING, crouchingHitboxes);
		this.hitboxesCache.put(EntityState.AIR_CROUCHING, crouchingHitboxes);

		setState(EntityState.JUMPING);


		this.strafeActive = true;

		this.firingRateTracker = stats.getFiringRate();
		this.bulletSpeed = 60;
		this.genericBulletDamage = stats.getDamage();
		this.accuracy = equippedWeapon.getAccuracy() + 5;

	}


	/**
	 * Updates the players state through user button presses.
	 *
	 * @param ms millisecond tick the player attack is being handled on
	 */
	@Override
	protected void stateUpdate(long ms) {
		// Attacks
		boolean basicAttackPressed = InputManager.getActionState(GameAction.BASIC_ATTACK);
		if (basicAttackPressed) {
			hasFired = true;
		}
		updateRenderAngle();
		playerInput.updateGameInput();
		moveStateEntity(ms);


		// change weapons
		weaponChange(Arrays.asList(justPressed(GameAction.WEAPON_ONE), justPressed(GameAction.WEAPON_TWO)));
		if (host) {
			playerAttack(ms, basicAttackPressed);
			//hasFired = true;
		}
	}

	public Update getPlayerUpdate() {
		return new PlayerUpdate(name, posX, posY, currentHealth);
	}

	public Update getFireUpdate() {
		if (hasFired) {
			hasFired = false;
			return new FireUpdate(name, InputManager.getMousePixelX(), InputManager.getMousePixelY());
			//if (basicAttackPressed) {
			//	Multiplayer.addUpdate(0, new FireUpdate(getName(), getProjectileVelocity()[0], getProjectileVelocity()[1]));
			//}
		}
		return new NilUpdate();
	}

	public Update getDamageUpdate() {
		if (prevHealth != currentHealth) {
			return new DamageUpdate(name, currentHealth);
		}
		return new NilUpdate();
	}

	public void setPosition(float x, float y) {
		prevX = posX;
		prevY = posY;
		posX = x;
		posY = y;
		bounds.setX(posX);
		bounds.setY(posY);

		updateHitboxes();
	}

/*
	@Override
	public void entityLoop(long ms) {
		this.latestTick = ms;
		// We do a special first tick, where we just place it into the world and
		// see if it collides with anything.
		// i.e. there is no movement on the first tick.

		if (onGround || onCeiling) {
			velY = 0;
		}
		tick(ms);

		if (this.isOnGround()) {
			applyTileEffects(ms);
		}
	}*/

	/**
	 * Tick handler for player
	 *
	 * @param ms millisecond tick the player attack is being handled on
	 */
	@Override
	protected void tick(long ms) {
		addMana(1);
		stateUpdate(ms);
		// Skill Controlling
		if (stunned) {
			return;
		}
		// particleFX
		/*
		if (currentState == EntityState.DASHING || currentState == EntityState.AIR_DASHING) {
			for (float y = getY(); y < getY() + getHeight(); y += 0.2) {
				Particle particle = new Particle(0, 0, getX() + (0.5f * getWidth()), y, 302, 10, true, false);
				world.addEntity(particle);
			}
		}*/
		tickDebug();
		updateTimers(ms);

	}

	/**
	 * Method to handle player's attack and to have a look at the attack type.
	 * <p>
	 * The type of player attack determined. When more types of attacks or
	 * different weapons is added to the game then this function could
	 * distinguish between the attacks. This method also decides if the attack
	 * was a critical hit or not.
	 *
	 * @param ms          millisecond tick the player attack is being handled on
	 * @param basicAttack true if the basic attack is selected by the player
	 */

	protected void playerAttack(long ms, boolean basicAttack) {
		float armourDamageBonus = 1;
		this.firingRateTracker -= ms;
		if (currentState == EntityState.DASHING || currentState == EntityState.AIR_DASHING
				|| currentState == EntityState.STUNNED || currentState == EntityState.SLIDING) {
			return;
		}
		if (basicAttack) {
			SoundCache.getInstance().play("attack");
			if (this.firingRateTracker <= 0) {
				equippedWeapon.basicAttack(this, getProjectileVelocity()[0], getProjectileVelocity()[1], this.bulletSpeed,
						(int) armourDamageBonus * this.genericBulletDamage);
				this.firingRateTracker = (stats.getFiringRate() + equippedWeapon.getFiringRate()) / 2;
			}
		}
	}

	/**
	 * The ex-hueg state machine that governs how the player moves
	 *
	 * @param ms time since the last tick in ms
	 */
	protected void moveStateEntity(long ms) {
		if (currentState == EntityState.DEAD) {
			return;
		}
		inputDir = playerInput.getInputDirection();

		boolean left = playerInput.getLeftPressed();
		boolean right = playerInput.getRightPressed();
		boolean jump = playerInput.getJumpPressed();
		boolean up = playerInput.getUpPressed();
		boolean down = playerInput.getDownPressed();

		jumpAvailable = ableToJump();

		switch (currentState) {
			case DEAD:
				return;
			case KNOCK_BACK:
				entityKnockBack();
				break;
			case STANDING:
				entityStand(left, right, jump);
				break;
			case MOVING:
				entityMove(left, right, jump);
				break;
			case JUMPING:
				entityJump(left, right, up, down, jump);
				break;
			case WALL_SLIDING:
				entityWallSlide(left, right, jump);
				break;
			case DASHING:
				entityDash(jump);
				break;
			case SLIDING:
				entitySlide();
				break;
			case AIR_DASHING:
				entityAirDash();
				break;
			default:
				break;
		}
	}


	/**
	 * Handles transitions from the standing state
	 * <p>
	 * -> moving: user input to either side -> jumping: either user inputs jump,
	 * or the player walks off a ledge -> dashing: user inputs dash (and there
	 * is enough mana and the skill is unlocked) -> sliding: user inputs dash
	 * (and there is enough mana and this doesn't cause collision problems)
	 */
	protected void entityStand(boolean left, boolean right, boolean jump) {
		setVelocity(0, 0);
		if (!onGround) {
			setState(EntityState.JUMPING);
			return;
		}
		if (left != right) {
			setState(EntityState.MOVING);
		} else if (jump && jumpAvailable) {
			SoundCache.getInstance().play("jump");
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
			return;
		}
	}

	/**
	 * Handles transitions from the moving state
	 * <p>
	 * -> standing: no user input to either side -> sprinting (sub-state of
	 * moving): user double taps the left/right button -> jumping: either user
	 * inputs jump, or the player walks off a ledge -> dashing: user inputs dash
	 * (and there is enough mana and the skill is unlocked) -> sliding: user
	 * inputs dash (and there is enough mana and this doesn't cause collision
	 * problems)
	 */
	protected void entityMove(boolean left, boolean right, boolean jump) {
		if (left == right) {
			setState(EntityState.STANDING);
			return;
		} else {
			velX = inputDir * moveSpeed;
		}
		if (inputDir == 1 && InputManager.getDoublePressed(GameAction.MOVE_RIGHT)) {
			velX = inputDir * (moveSpeed * 2);
		}
		if (inputDir == -1 && InputManager.getDoublePressed(GameAction.MOVE_LEFT)) {
			velX = inputDir * (moveSpeed * 2);
		}
		if (jump && jumpAvailable) {
			SoundCache.getInstance().play("jump");
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
		} else if (!onGround) {
			setState(EntityState.JUMPING);
		}
	}

	/**
	 * Handles transitions from the jumping state
	 * <p>
	 * -> standing: user lands with no horizontal input -> moving: user lands
	 * with horizontal input -> double jumping (sub-state of jumping): user
	 * inputs jump (and their velocity is downwards and the skill is unlocked)
	 * -> wall sliding: player is against a wall and holding the input of that
	 * direction -> air dashing: user inputs dash (and there is enough mana and
	 * the skill is unlocked)
	 */
	protected void entityJump(boolean left, boolean right, boolean up, boolean down, boolean jump) {
		if (wallJumpTimer < 0) {
			if (velX <= moveSpeed && velX >= -moveSpeed) {
				velX = inputDir * moveSpeed;
			} else if (facing != inputDir && inputDir != 0) {
				velX = -velX;
			}
			if (inputDir == 0) {
				velX *= 0.9f;
			}
		}
		if (jump && ableToDoubleJump() && getSkillUnlocked("Double jump")) {
			SoundCache.getInstance().play("jump");
			setState(EntityState.JUMPING);
			// particleFX
			for (float x = getX(); x < getX() + getWidth(); x += 0.2) {
				Particle particle = new Particle(0, 0, x, getY() + (getHeight()), 301, 10, true, false);
				world.addEntity(particle);
			}
			velY = jumpSpeed;
			doubleJumpAvailable = false;
		}
		if ((inputDir == onWall) && (velY > 0) && (inputDir != 0)) {
			setState(EntityState.WALL_SLIDING);
			strafeActive = false;
			setFallModifier(wallSlidingFallModifier);
			setTerminalVelModifier(wallSlidingTerminalModifier);
		}
		if (onGround) { // We hit the ground
			transitionOnLanding();
		}
		if (onCeiling) {
			velY = 0f;
		}
	}

	/**
	 * Handles transitions from the wall sliding state
	 * <p>
	 * -> standing: user lands with no horizontal input -> moving: user lands
	 * with horizontal input -> jumping: player is no longer against a wall and
	 * holding the input of that direction -> wall jumping (sub-state of
	 * jumping): player inputs jump
	 */
	protected void entityWallSlide(boolean left, boolean right, boolean jump) {
		if ((inputDir != onWall) || (velY < 0)) {
			setState(EntityState.JUMPING);
			setFallModifier(1f);
			setTerminalVelModifier(1f);
			strafeActive = true;
		}
		if (jump && jumpAvailable) {
			setFallModifier(1f);
			setTerminalVelModifier(1f);
			setState(EntityState.JUMPING);
			velY = jumpSpeed * fallModifier;
			velX = -wallJumpSpeedModifier * facing * moveSpeed;
			wallJumpTimer = wallJumpDuration;
			strafeActive = true;
		}
		if (onGround) { // We hit the ground
			transitionOnLanding();
		}
	}

	/**
	 * Handles transitions from the dashing state
	 * <p>
	 * -> standing: user runs into a wall, or the timer runs out with no
	 * horizontal input -> moving: the timer runs out with horizontal input ->
	 * jumping: user jumps or falls off a ledge -> wall jumping (sub-state of
	 * jumping): player inputs jump
	 */
	protected void entityDash(boolean jump) {
		if (!onGround) {
			strafeActive = true;
			setState(EntityState.JUMPING);
		}
		if (onWall != 0) {
			strafeActive = true;
			setState(EntityState.STANDING);
		}
		if (jump && jumpAvailable) {
			strafeActive = true;
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
		}
		if (actionTimer < 0) {
			transitionToOnGround();
		}
	}

	/**
	 * Handles transitions from the sliding state
	 * <p>
	 * -> standing: user runs into a wall, or the timer runs out with no
	 * horizontal input -> moving: the timer runs out with horizontal input ->
	 * jumping: user jumps or falls off a ledge -> wall jumping (sub-state of
	 * jumping): player inputs jump
	 */
	protected void entitySlide() {
		if (!onGround && jumpAvailable) {
			strafeActive = true;
			setState(EntityState.JUMPING);
			changeBounds(1f, 2f);
			// No need to check if transition is possible because of
			// jumpAvailable (in theory)
		}
		if (onWall != 0 && jumpAvailable) {
			strafeActive = true;
			setState(EntityState.STANDING);
			changeBounds(1f, 2f);
		} else if (onWall != 0) {
			velX *= -1;
			actionTimer = -1L;
		}
		if (actionTimer < 0 && jumpAvailable) {
			changeBounds(1f, 2f);
			transitionToOnGround();
		}
	}

	/**
	 * Handles transitions from the air dashing state
	 * <p>
	 * -> standing: user lands with no horizontal input -> moving: the user
	 * lands with horizontal input -> jumping: the timer runs out
	 */
	protected void entityAirDash() {
		if (onGround) { // We hit the ground
			enableGravity = true;
			strafeActive = true;
			transitionOnLanding();
		}

		if (actionTimer < 0) {
			enableGravity = true;
			strafeActive = true;
			setState(EntityState.JUMPING);
			velY = 0;
			if (inputDir == 0) {
				velX = 0;
			} else {
				velX = facing * moveSpeed;
			}
		}
	}

	protected void updateTimers(float ms) {
		if (actionTimer >= 0) {
			actionTimer -= ms;
		}

		if (wallJumpTimer >= 0) {
			wallJumpTimer -= ms;
		}

		if (knockBackTimer >= 0) {
			knockBackTimer -= ms;
			knockBackRenderTimer += ms;
			if (knockBackRenderTimer > knockBackLastRenderTime + knockBackRenderGap) {
				renderFlag = !renderFlag;
				knockBackLastRenderTime = knockBackRenderTimer;
			}
		} else {
			renderFlag = true;
		}
	}

	/**
	 * Updates renderFacing to face towards the cursor Updates the arm and head
	 * additional sprites to point to the cursor.
	 */
	private void updateRenderAngle() {
		if (Window.getEngine() != null && Window.getEngine().getRenderer() != null
				&& Window.getEngine().getRenderer().getViewport() != null) {
			if (strafeActive) {
				if (InputManager.getDiffX(posX) > 0) {
					renderFacing = 1;
				} else {
					renderFacing = -1;
				}

				if (inputDir == 0) {
					facing = renderFacing;
				}
			}
			// For player, both the head and arm should be pointing at the
			// mouse.
			if (additionalSprites != null) {
				if (additionalSprites.containsKey(BodyPart.ARM)) {
					additionalSprites.get(BodyPart.ARM).setTarget(InputManager.getMouseTileX(),
							InputManager.getMouseTileY());
				}
				if (additionalSprites.containsKey(BodyPart.HEAD)) {
					additionalSprites.get(BodyPart.HEAD).setTarget(InputManager.getMouseTileX(),
							InputManager.getMouseTileY());
				}
				if (additionalSprites.containsKey(BodyPart.VOID)) {
					additionalSprites.get(BodyPart.VOID).setTarget(InputManager.getMouseTileX(),
							InputManager.getMouseTileY());
				}
			}
		}
	}

	public void render(GraphicsContext gc, Viewport viewport, long ms) {
		super.render(gc, viewport, ms);
	}

	/**
	 * returns the x and y velocity for a projectile that is aimed at the mouse
	 *
	 * @return float[0] = xvel, float[1] = yvel
	 */
	private float[] getProjectileVelocity() {
		// Barney Whiteman
		/*
		 * Angle between player and mouse with accuracy built in
		 *
		 * aim is a random number between -accuracy and +accuracy and then
		 * converted into radians
		 */
		float prjXVel = 0f;
		float prjYVel = 0f;
		if (viewport != null) {
			double aim = (Math.PI / 180) * ((Math.random() * accuracy * 2) - accuracy);
			aimAngle = (Math.atan2(InputManager.getDiffY(posY + this.getHeight() / 2),
					InputManager.getDiffX(posX + this.getWidth() / 2))) + aim;
			prjXVel = (float) (Math.cos(aimAngle));
			prjYVel = (float) (Math.sin(aimAngle));
		}
		return new float[]{prjXVel, prjYVel};
	}

	/**
	 * Entity collision event handler
	 * <p>
	 * We deal knockback only with the first entity for convenience
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (entities.isEmpty()) {
			return;
		}
		for (Entity entity : entities) {
			if (entity instanceof CompanionNPC) {
				return;
			} else if (entity instanceof BaseNPC) {
				int knockBackDir = (int) Math.signum(entity.getVelX());
				transitionToKnockBack(knockBackDir);
				return;
			}
		}
	}


	/**
	 * On death event handler;
	 *
	 * @param cause entity that caused the death
	 */
	@Override
	protected void onDeath(Entity cause) {
		velX = 0;
		setState(EntityState.DEAD);
	}

	/**
	 * Adjusts the BME's health by the input quantity, up to the maximum value
	 */
	@Override
	public void addHealth(int health) {
		if (currentState == EntityState.KNOCK_BACK || knockBackTimer > 0) {
			return;
		}
		prevHealth = health;
		if (health < 0 && shielded) {
			// Don't alter current health
		} else {
			currentHealth += health;
		}
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}

	/*
	 TODO weapon change
	  */

}
