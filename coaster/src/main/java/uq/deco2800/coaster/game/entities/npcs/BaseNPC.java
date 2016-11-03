package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.StateEntity;
import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.graphics.notifications.IngameText;
import uq.deco2800.coaster.graphics.Viewport;

/**
 * BaseNPC is the base class for which all NPCs will extend from. BaseNPC itself
 * extends from StateEntity and hence Entity. It provides the fields for NPC
 * attributes and characteristics which are used in the game. It provides public
 * methods to retrieve such information about the NPC as well as those to
 * manipulate NPC and its attributes. Additionally it contains default methods
 * for implementing behavior which is similar across multiple NPCs, and helper
 * methods to determine such behavior.
 *
 * NPC behavior is structured around determining next actions for the NPC based
 * on the current game situation, and recalculated each tick. However NPCs are
 * able to store future actions to be executed on following ticks if desired.
 */
public abstract class BaseNPC extends StateEntity {

	/* Fields for the attributes and characteristics of a general NPC */
	protected NPCType myType;
	protected boolean isBoss = false;
	protected int maxHealth;
	protected int currentHealth;
	protected int distanceFromPlayer;
	protected float fallModifier = 1f;
	protected float terminalVelocityModifier = 1f;
	protected int moveSpeedVer;
	protected int moveSpeedHor;
	protected float attackRangeHor;
	protected float attackRangeVer;
	protected int visionRangeHor;
	protected int visionRangeVer;
	protected int patrolStep;
	protected int jumpSpeed;
	protected int baseDamage;
	protected float yDistance;
	protected float xFactor;
	protected float yFactor;
	protected double difficultyScale = (world.getDifficulty() >= 1) ? world.getDifficulty() : 1;
	List<NPCAction> actionQueue = new ArrayList<>();
	protected List<ActionExecution> availableActions = new LinkedList<>();
	protected int stateDuration;
	protected NPCSound soundType;
	Player target;
	/* Future potential fields to implement */
	// Weapon currentWeapon; //Change to list of weapons if npc can equip or
	// hold multiple
	// List<Item> inventory; //Current inventory for npc

	/**
	 * Constructor for BaseNPC
	 */
	public BaseNPC() {

	}

	@Override
	/**
	 * The top level method for each game tick.
	 *
	 * @paramm ms
	 */
	protected abstract void tick(long ms);

	/**
	 * Called each tick to determine the next action of the NPC
	 *
	 * @param ms
	 */
	@Override
	protected void stateUpdate(long ms) {
	}

	/**
	 * Handles collision with entities
	 *
	 * @param entities
	 * @param hitLocations
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
	}

	/**
	 * Handles collision with terrain
	 *
	 * @param tileX
	 * @param tileY
	 * @param side The direction the collision occurs
	 */
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
	}

	/**
	 * To be called to gracefully handle an NPCs death. In most instances to
	 * allow for death animations, increasing player experience, dropping items
	 * and other consequential interactions with the game.
	 */
	protected void onDeath() {

		// Delete entity
		this.delete();
		/* Increase player experience and npc kill count */

		Player player = this.world.getPlayerEntities().get(0);
		int xpGained = this.maxHealth / 10;
		player.addExperiencePoint(xpGained);
		IngameText ingameText = new IngameText("+" + xpGained + " XP", player.getX(), player.getY(), 2000,
				IngameText.textType.DYNAMIC, 1, 0, 1, 1);
		world.addIngameText(ingameText);
		player.addKillCount(1);

		ItemDrop.randomDrop(this.getX(), this.getY());
	}

	/**
	 * To receive information about this NPCs health.
	 *
	 * @return The current health of the NPC.
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * @return The maximum health of the NPC.
	 */

	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Sets the current health of this NPC.
	 *
	 * @param health the health which it will be set to
	 */
	public void setCurrentHealth(int health) {
		currentHealth = health;
	}

	/**
	 * To receive information about this NPCs jumping speed.
	 *
	 * @return The current jumping speed of the NPC.
	 */
	public int getJumpSpeed() {
		return jumpSpeed;
	}

	/**
	 * Remove health from the NPC. In most circumstances use receiveDamage() in
	 * order to allow the defined behavior/characteristics of the NPC to
	 * function properly.
	 *
	 * @param damage The amount of health that will be removed from the NPC.
	 */
	public void receiveDamageForce(int damage) {

		currentHealth -= damage;

		if (currentHealth < 0) {
			onDeath();
		} else {
		}
	}

	/**
	 * Attempt to remove health from the NPC. This will take into account unique
	 * properties of NPCs and any stats or attributes that could increase or
	 * decrease the amount of damage taken by the NPC. For example armour
	 * resistances.
	 *
	 * @return Damage taken by the NPC.
	 */
	public int receiveDamage(int damage, Entity cause) {
		IngameText ingameText = new IngameText("-" + Integer.toString(damage), getX(), getY(), 500,
				IngameText.textType.DYNAMIC, 1, 0, 0, 1);
		world.addIngameText(ingameText);
		/*
		 * Default implementation. NPCs will generally override this to account
		 * for armor, shields etc.
		 */
		currentHealth -= damage;
		try {
			if (soundType == NPCSound.LOW) {
				SoundCache.play("lowHit");
			} else if (soundType == NPCSound.HIGH) {
				SoundCache.play("highHit");
			}
			return damage;
		} finally {
			if (currentHealth < 0) {
				/*
				 * NPC should die. Play sounds/animations and remove from the
				 * world
				 */
				if (soundType == NPCSound.LOW) {
					SoundCache.play("lowDead");
				} else if (soundType == NPCSound.HIGH) {
					SoundCache.play("highDead");
				}
				onDeath(cause);
			} else {
				/* NPC still alive Play sounds/animation and continue */
			}
		}
	}

	/**
	 *
	 * Sets the state of the entity to the desired EntityState (for example
	 * STUNNED), and for the desired time in seconds. Damage an NPC due to
	 * damage-on-tick events, used to stop endless items from spawning (and
	 * endless sound effects)
	 */
	public int receiveDOTDamage(int damage) {
		/*
		 * Default implementation. NPCs will generally override this to account
		 * for armor, shields etc.
		 */
		currentHealth -= damage;
		try {
			return damage;
		} finally {
			if (currentHealth < 0) {
				/*
				 * NPC should die. Play sounds/animations and remove from the
				 * world
				 */
				onDeathDOT();
			}
		}
	}

	/**
	 * Kill an NPC as a result of a DOT effect (player receives no experience
	 * for now - needs to be reworked)
	 */
	protected void onDeathDOT() {
		/* Remove the NPC from the world */
		this.delete();
	}

	/**
	 * Sets the state of the entity to the desired EntityState (for example
	 * STUNNED), and for the desired time in seconds.
	 *
	 * @param state The desired state for the NPC to be in.
	 * @param duration The desired time for the NPC to be in the aforementioned
	 *            state, in seconds.
	 */
	public void setState(EntityState state, int duration) {

		int ticks = duration * 60; // 60 ticks per second.
		int i;

		if (state == EntityState.STUNNED) {
			/*
			 * Reset the action queue and stopEffect the npc for allotted time
			 */
			actionQueue.clear();
			moveSpeedHor = 0;
			for (i = 0; i < ticks; i++) {
				actionQueue.add(NPCAction.NOTHING);
			}
		}
		/* Update the state */
		this.setState(state);
		/* Timer on when this temporary state should reset */
		stateDuration = ticks;

	}

	/**
	 * Helper method called each tick to check whether we should return from a
	 * temporarily set state.
	 */
	protected void checkStateUpdate() {

		if (stateDuration > 0) {
			/* We still need to be in the current state */
			--stateDuration;
		}
		if (stateDuration == 0) {
			/* State needs to be reset. Generally to the NPCs default state. */
			this.setState(EntityState.DEFAULT);
		}
	}

	/**
	 * Receive information about the NPCs current movement speed in the
	 * horizontal axis.
	 *
	 * @return The NPCs current movement speed in the horizontal axis.
	 */
	public int getMoveSpeedVer() {
		return moveSpeedVer;
	}

	/**
	 * Receive information about the NPCs current movement spede in the vertical
	 * axis.
	 *
	 * @return The NPCs current movement speed in the vertical axis.
	 */
	public int getMoveSpeedHor() {
		return moveSpeedHor;
	}

	/**
	 * Starts or continues the actions for the NPC to patrol the area around it.
	 * Updates the current patrol variable, switches direction if needed and
	 * executes a single movement to complete the patrol step in the game.
	 */
	protected void patrolArea() {
		/* We start or continue the patrol */
		++patrolStep;
		patrolStep %= 500;
		if (patrolStep == 0) {
			/* Toggle direction */
			setFacing(-1 * getFacing());
			setRenderFacing(-1 * getRenderFacing());
		}
		/* Complete the patrol step by moving in the game */
		availableActions.get(0).execute();
	}

	/**
	 * Determines if the NPC is able to jump based on the tiles and terrain
	 * surrounding it.
	 *
	 * @return True if the NPC is able to jump from its current position.
	 */
	public boolean ableToJump() { // Needs to include a check for entities

		/* Check for terrain collision */
		// Array of tile x-coordinates taken up by the player
		int[] xVals = new int[(int) Math.ceil(posX + getWidth()) - (int) posX];
		for (int i = 0; i < xVals.length; i++) {
			xVals[i] = ((int) posX) + i;
		}

		int rowAbove = (int) Math.ceil(getY() - 1);
		for (int i = 0; i < xVals.length; i++) {
			if (!world.getTiles().test(xVals[i], rowAbove)) {
				continue;
			}
			TileInfo tileType = world.getTiles().get(xVals[i], rowAbove).getTileType();
			if (tileType.isObstacle()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Receive information about the current target the NPC is focused on.
	 *
	 * @return The NPCs current target in the game. If null then no current
	 *         target.
	 */
	public Entity getCurrentTarget() {
		return target;
	}

	/**
	 * Receive information about the current NPCs base damage. This damage is
	 * for the NPCs basic attack.
	 *
	 * @return The current base damage used for the NPCs base attack.
	 */
	public int getBaseDamage() {
		return baseDamage;
	}

	/**
	 * @param target The target entity to determine distance between it and this
	 *            NPC
	 * @return The current distance (radius) between this NPC and the target
	 *         entity.
	 */
	public int getDistanceFromPlayer(Player target) {

		boolean leftOfTarget = this.posX < target.getX();
		boolean aboveTarget = (this.posY + this.getHeight() / 2) - (target.getY() + target.getHeight() / 2) < 0;

		float xDistance;

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

		return (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
	}

	public int getDistanceFromTarget(BaseNPC target) {
		boolean leftOfTarget = this.posX < target.getX();
		boolean aboveTarget = (this.posY + this.getHeight() / 2) - (target.getY() + target.getHeight() / 2) < 0;

		float xDistance;

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

		return (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);

	}

	/**
	 * To assist NPCs that wish to decide their next direction randomly.
	 *
	 * @return Random value which indicates a direction/
	 */
	public boolean determineIdleDirection() {
		return Math.random() <= 0.5;
	}

	/**
	 * To assist an NPC in determining if it should jump as its next action in
	 * the world. This implementation is for if there is a tile blocking the
	 * NPCs path and could jump to pass it.
	 *
	 * @param direction The direction the NPC is currently facing, -1 for left,
	 *            1 for right.
	 * @return True if the NPC will jump on the next action, false otherwise.
	 */
	public boolean shouldJump(int direction) {
		if (direction > 0) {
			int testX = (int) (Math.ceil(getX() + getWidth() / 2) + 1);
			int testY = (int) (Math.floor(getY() + getHeight() / 2));
			Boolean tileExists = world.getTiles().test(testX, testY);
			if (tileExists) {
				return world.getTiles().get(testX, testY).getTileType().isObstacle();
			}
		} else {
			int testX = (int) (Math.floor(getX()) - (getWidth() / 2) - 1);
			int testY = (int) (Math.floor(getY() + getHeight() / 2));
			Boolean tileExists = world.getTiles().test(testX, testY);
			if (tileExists) {
				return world.getTiles().get(testX, testY).getTileType().isObstacle();
			}
		}
		return false;
	}

	public boolean isBlockedVision() {
		float left = (getX() <= target.getX()) ? getX() : target.getX();
		float right = (left == getX()) ? target.getX() : getX();
		float lower = (getY() <= target.getY()) ? getY() : target.getY();
		float upper = (lower == getY()) ? target.getY() : getY();
		float x;
		float y;

		x = left;

		while (x < right) {
			y = (((x - left) / (right - left)) * (upper - lower)) + lower;
			Boolean tileExists = world.getTiles().test((int) x, (int) y);
			if (tileExists && world.getTiles().get((int) x, (int) y).getTileType().isObstacle()) {
				return true;
			}
			x += 0.1f;

		}
		return false;
	}

	/**
	 * To update the direction in which an NPC should be facing, relative to the
	 * NPCs current target, and if the caller wishes to invert the decision.
	 *
	 * @param target The current target of the NPC
	 * @param invert Indicator of whether to flip the decision.
	 */
	public void determineFacingDirection(Player target, boolean invert) {
		if (!target.getInvisible()) {
			if (this.posX < target.getX()) {
				if (invert) {
					setFacing(-1); // left
				} else {
					setFacing(1); // right
				}
			} else {
				if (invert) {
					setFacing(1); // right
				} else {
					setFacing(-1); // left
				}
			}
		}
	}

	/***
	 * Function that determines the direction of the target NPC relative to
	 * BaseNPC calling it
	 *
	 * @param target BaseNPC which you want to find the direction of
	 * @param invert Determination of whether to inverse the boolean.
	 */
	public void determineFacingDirectionNPC(BaseNPC target, boolean invert) {
		if (this.posX < target.getX()) {
			if (invert) {
				setFacing(-1); // left
			} else {
				setFacing(1); // right
			}
		} else {
			if (invert) {
				setFacing(1); // right
			} else {
				setFacing(-1); // left
			}
		}
	}

	/***
	 * Function that determines the direction of the target ItemEntity relative
	 * to BaseNPC calling it
	 *
	 * @param target ItemEntity which you want to find the direction of
	 * @param invert Determination of whether to inverse the boolean.
	 */
	public void determineFacingDirectionItemEntity(ItemEntity target, boolean invert) {
		if (this.posX < target.getX()) {
			if (invert) {
				setFacing(-1); // left
			} else {
				setFacing(1); // right
			}
		} else {
			if (invert) {
				setFacing(1); // right
			} else {
				setFacing(-1); // left
			}
		}
	}

	/**
	 * Sets up the firing factors in order to utilize ranged attacks on a target
	 * entity.
	 *
	 * @param target The current target of the NPC.
	 */
	public void setFiringFactors(Player target) {
		// Look the maths here was causing all sorts of physics problems when
		// diffX < 0
		float diffX = target.getX() - posX;
		float diffY = target.getY() - posY;
		float magnitude = (float) Math.hypot(diffX, diffY);
		xFactor = diffX / magnitude;
		yFactor = diffY / magnitude;
	}

	/**
	 * Renders the NPC and its healthbar
	 *
	 * @param gc The grapics context of the entity
	 * @param viewport The viewport of the entity
	 * @param ms The number of milliseconds the game has run
	 */
	@Override
	public void render(GraphicsContext gc, Viewport viewport, long ms) {
		super.render(gc, viewport, ms);
		float tileSize = viewport.getTileSideLength();
		int leftBorder = viewport.getLeftBorder();
		int topBorder = viewport.getTopBorder();

		int left = (int) Math.floor(viewport.getLeft());
		int top = (int) Math.floor(viewport.getTop());

		float subTileShiftX = (viewport.getLeft() - left) * tileSize;
		float subTileShiftY = (viewport.getTop() - top) * tileSize;

		float x = (posX - left) * tileSize + leftBorder - subTileShiftX;
		float y = (posY - top) * tileSize + topBorder - subTileShiftY;

		float healthPercent = currentHealth / (float) maxHealth;
		// Green bit
		gc.setFill(Color.web("#FDF514"));
		gc.fillRect(x, y - (tileSize / 2), bounds.getWidth() * tileSize * healthPercent, tileSize / 10);

		// Empty bit
		gc.setFill(new Color(0.3, 0.3, 0.3, 1));
		gc.fillRect(x + bounds.getWidth() * tileSize * healthPercent, y - (tileSize / 2),
				bounds.getWidth() * tileSize * (1 - healthPercent), tileSize / 10);
	}

	public boolean isBoss() {
		return isBoss;
	}

	public void setBoss() {
		// only set boss once
		if (!isBoss) {
			isBoss = true;
			/* Define basic properties */
			maxHealth *= 50 * difficultyScale;
			currentHealth = maxHealth;

			/* Define proportions and attributes */
			bounds = new AABB(posX, posY, bounds.getWidth() * 5, bounds.getHeight() * 5);
		}
	}

	/**
	 * Enable changing the velocity
	 * @param ms time since the last tick in milliseconds.
	 */
	@Override
	protected void updateVelYFromGravity(float ms) {
		float seconds = ms / (float) 1000;
		velY += GRAVITY * seconds * fallModifier * (isUnderLiquid ? UNDER_WATER_GRAVITY_MULTIPLIER : 1f); // Top left coordinate system, so gravity is positive.
		if (velY > (terminalVelocity * terminalVelocityModifier)) {
			velY = terminalVelocity * terminalVelocityModifier;
		}
	}
}