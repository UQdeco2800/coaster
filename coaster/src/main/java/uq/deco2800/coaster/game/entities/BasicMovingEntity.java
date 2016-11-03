package uq.deco2800.coaster.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uq.deco2800.coaster.game.entities.particles.Particle;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.graphics.Viewport;

public abstract class BasicMovingEntity extends StateEntity {
	// Modifies acceleration due to gravity. 1.0 is normal speed, 0.5 is half
	// speed.
	// Modifies terminal velocity by the same ratio. Could be split to separate
	// function later
	protected float fallModifier = 1f;
	protected float terminalVelocityModifier = 1f;
	// Some entity properties (in tiles/s)
	protected float moveSpeed = 5;
	protected float jumpSpeed = -15;
	protected boolean renderHealthbar = true;
	protected int maxHealth = 100;
	protected int currentHealth = 100;
	protected boolean renderManabar = false;
	protected int maxMana = 100;
	protected int currentMana = 0;
	boolean stunned = false;
	boolean marked = false;
	boolean gliding = false;
	boolean invisible = false;
	boolean shielded = false;
	boolean atField = false;
	private float tempFallModifier;

	public BasicMovingEntity() {
		// We load the entity in as "jumping" to evaluate freefall
		currentState = EntityState.JUMPING;
	}

	/**
	 * Getter for the BME's max health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Sets the maximum health.
	 * 
	 * @param newHealth Integer greater than 0
	 */
	public void setMaxHealth(int newHealth) {
		this.maxHealth = newHealth;
	}

	/**
	 * Getter for the BME's current health
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * Getter for the BME's max mana
	 */
	public int getMaxMana() {
		return maxMana;
	}

	/**
	 * Turns on rendering of the BME's mana bar
	 */
	public void enableManaBar() {
		renderManabar = true;
	}
	
	/**
	 * Turns off rendering of the BME's health bar
	 */
	public void disableHealthBar() {
		renderHealthbar = false;
	}

	/**
	 * Getter for the BME's current mana
	 */
	public int getCurrentMana() {
		return currentMana;
	}

	/**
	 * Returns true if the BME's healthbar is currently rendered
	 */
	public boolean isRenderHealthbar() {
		return renderHealthbar;
	}
	
	/**
	 * Adjusts the BME's health by the input quantity, up to the maximum value
	 */
	public void addHealth(int health) {
		if (health < 0 && shielded) {
			//Don't alter current health
		} else {
			currentHealth += health;
		}
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}

	/**
	 * Adjusts the BME's health by the input quantity. If this causes health to go below 0, the input entity is listed
	 * as the cuase for the death
	 *
	 * @param health the amount of health to be added
	 * @param cause  the entity responsible for this damage/health
	 */
	public void addHealth(int health, Entity cause) {
		addHealth(health);
		if (currentHealth <= 0) {
			this.kill(cause);
		}
	}

	/**
	 * Adjusts the BME's mana by the input amount
	 */
	public void addMana(int mana) {
		currentMana += mana;
		if (currentMana > maxMana) {
			currentMana = maxMana;
		}
		if (currentMana < 0) {
			currentMana = 0;
		}
	}

	/**
	 * Adjusts vertical position with respect to gravity and the fallModifier.
	 *
	 * @param ms the time since the last tick. Should be the same as the input param to Enitity.update_physics
	 */
	@Override
	protected void updateVelYFromGravity(float ms) {
		float seconds = ms / (float) 1000;
		velY += GRAVITY * seconds * fallModifier * (isUnderLiquid ? UNDER_WATER_GRAVITY_MULTIPLIER : 1f); // Top left coordinate system, so gravity is positive.
		if (velY > (terminalVelocity * terminalVelocityModifier)) {
			velY = terminalVelocity * terminalVelocityModifier;
		}
	}

	/**
	 * Renders the BME's sprite and additional sprites using the Entity.render method Also renders effects indicating
	 * stunned, shielded and marked states, and the mana bar
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

		if (renderHealthbar) {
			float healthPercent = currentHealth / (float) maxHealth;
			// Green bit
			gc.setFill(new Color(0, 1, 0, 1));
			gc.fillRect(x, y - (tileSize / 2), bounds.getWidth() * tileSize * healthPercent, tileSize / 10);

			// Empty bit
			gc.setFill(new Color(0.3, 0.3, 0.3, 1));
			gc.fillRect(x + bounds.getWidth() * tileSize * healthPercent, y - (tileSize / 2),
					bounds.getWidth() * tileSize * (1 - healthPercent), tileSize / 10);
		}

		// stun indicator
		if (stunned) {
			gc.setFill(new Color(0, 0, 1, 0.5));
			gc.fillRect(x, y, bounds.getWidth() * tileSize, bounds.getHeight() * tileSize);
		}

		// shield indicator
		if (shielded) {
			gc.setFill(new Color(1, 1, 0, 0.3));
			gc.fillOval(x - bounds.getWidth() * tileSize / 2, y, bounds.getHeight() * tileSize,
					bounds.getHeight() * tileSize);
		}
		
		if (atField) {
			gc.setFill(new Color(1, 0, 0, 0.2));
			gc.fillOval(x - bounds.getWidth() * tileSize / 2, y, bounds.getHeight() * tileSize,
					bounds.getHeight() * tileSize);
		} 

		if (marked) {
			gc.setFill(new Color(1, 0, 0, 0.5));
			gc.fillRect(x, y, bounds.getWidth() * tileSize, bounds.getHeight() * tileSize);
		}

		if (renderManabar) {
			float manaPercent = currentMana / (float) maxMana;
			// blue bit
			gc.setFill(new Color(0, 0, 1, 1));
			gc.fillRect(x, y - 2 * (tileSize / 2), bounds.getWidth() * tileSize * manaPercent, 2 * (tileSize / 10));

			// Empty bit
			gc.setFill(new Color(0.3, 0.3, 0.3, 1));
			gc.fillRect(x + bounds.getWidth() * tileSize * manaPercent, y - 2 * (tileSize / 2),
					bounds.getWidth() * tileSize * (1 - manaPercent), 2 * (tileSize / 10));
		}
	}

	/**
	 * Returns true if the tile directly above the entity is not an obstacle and the entity is therefore able to jump
	 * Otherwise returns false Does not check if there is an entity directly above. This is assuming that protections
	 * against stacking will be implemented (e.g. knockback). This will be kinda buggy if the entity if the entity's
	 * height is not a whole number.
	 */
	protected boolean ableToJump() {
		// Needs to include a check for entities
		// xVals: an array of tile x-coordinates taken up by the player
		// Check for terrain collision
		int rightX = (int) Math.ceil(bounds.ownerRight() - 1);
		int leftX = (int) Math.floor(bounds.ownerLeft());

		int rowAbove = (int) Math.ceil(bounds.ownerTop() - 1);
		for (int testX = leftX; testX <= rightX; testX++) {
			if (!world.getTiles().test(testX, rowAbove)) {
				continue;
			}
			TileInfo tile = world.getTiles().get(testX, rowAbove).getTileType();
			if (tile.isObstacle()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if all the tiles to the input direction of the entity are obstacles, ignoring the bottom-most Else
	 * returns false <p> i.e. if every tile next to the entity is solid/a wall This is distinct from how onWall is
	 * determined. OnWall is set if a player is unable to move horizontally due to a terrain collision i.e. the physics
	 * AABB has a width of 0 after processing movement.
	 *
	 * @param direction is -1 to check left, 1 to check right
	 * @return true if the entity is wholly alongside a wall, otherwise false
	 */
	protected boolean completelyOnWall(int direction) {
		// Needs to include a check for entities
		// xVals: an array of tile x-coordinates taken up by the player
		// Check for terrain collision
		int[] yVals = new int[(int) Math.ceil(posY + getHeight()) - (int) posY];
		for (int i = 0; i < yVals.length; i++) {
			yVals[i] = ((int) posY) + i;
		}
		int columnAside = (int) Math.ceil(getX() + direction);
		
		for (int i = 0; i < yVals.length - 1; i++) {
			if (!world.getTiles().test(columnAside, yVals[i])) {
				continue;
			}
			TileInfo tile = world.getTiles().get(columnAside, yVals[i]).getTileType();
			if (!tile.isObstacle()) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Returns the duration of a standard jump by a basic entity, in number of milliseconds
	 */
	public int getJumpDuration() {
		return (int) Math.abs(Math.ceil(((2 * this.jumpSpeed) / GRAVITY) * 1000));
	}

	/**
	 * Returns the current moveSpeed of the BME Positive values are to the right, negative values to the left
	 *
	 * @return current moveSpeed in tiles/second
	 */
	public float getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * Sets the moveSpeed to the current value
	 *
	 * @param newvalue the new movespeed in tiles/second
	 */
	public void setMoveSpeed(float newvalue) {
		this.moveSpeed = newvalue;
	}

	/**
	 * Returns the initial jump speed of the bme
	 *
	 * @return initial jump speed
	 */
	public float getJumpSpeed() {
		return jumpSpeed;
	}

	/**
	 * Modifies the fall modifier to be the input value
	 *
	 * @param newModifier the new fallModifier
	 */
	protected void setFallModifier(float newModifier) {
		fallModifier = newModifier;
	}

	/**
	 * Returns true if the BME is stunned, else false
	 *
	 * @return true if stunned, else false
	 */
	public boolean getStunned() {
		return stunned;
	}

	/**
	 * Modifies the terminal velocity modifier to the input value
	 *
	 * @param newModifier the new terminalVelocityModifier
	 */
	protected void setTerminalVelModifier(float newModifier) {
		terminalVelocityModifier = newModifier;
	}

	/**
	 * Set the marked boolean to the input
	 */
	public void setMarked(boolean mark) {
		this.marked = mark;
	}

	/**
	 * Sets the stun boolean as input. If the player is stunned, gravity is disabled and the player's velocity is set to
	 * 0.
	 */
	public void setStunned(boolean stun) {
		this.stunned = stun;
		if (stun) {
			tempFallModifier = fallModifier;
			setFallModifier(0);
			setVelocity(0, 0);
		} else {
			fallModifier = tempFallModifier;
		}
	}

	/**
	 * Returns true if the player is gliding, otherwise false
	 *
	 * @return true if gliding, else false
	 */
	public boolean getGliding() {
		return gliding;
	}

	/**
	 * Sets the BME in/out of a gliding state as per input
	 */
	public void setGliding(boolean glide) {
		this.gliding = glide;
		if (glide) {
			tempFallModifier = fallModifier;
			setFallModifier(0.2f);
		} else {
			fallModifier = tempFallModifier;
		}
	}

	/**
	 * returns true if the BME is invisible, otherwise false
	 *
	 * @return true if invisible, else false
	 */
	public boolean getInvisible() {
		return invisible;
	}

	/**
	 * Setter for the invisible boolean
	 */
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	/**
	 * Returns true if the BME is shielded, otherwise false
	 *
	 * @return true if shielded, else false
	 */
	public boolean getShielded() {
		return shielded;
	}

	/**
	 * Sets shielded as the input value
	 */
	public void setShielded(boolean shield) {
		this.shielded = shield;
	}

	public void setATField(boolean field) {
		this.atField = field;
	}

	
	/**
	 * A simple state machine between standing still, moving, and jumping (freefall) based on the specified input.
	 * Essentially, based on the input and whether we're on the ground or not (determined by the physics engine), we
	 * choose which sprite to display and set our velocity accordingly.
	 *
	 * @param ms    time since the last tick in milliseconds
	 * @param left  causes the BME to move left if true
	 * @param right causes the BME to move right if true
	 * @param jump  causes the BME to jump if true
	 */
	protected void moveStateEntity(long ms, boolean left, boolean right, boolean jump) {
		if (currentState == EntityState.STANDING) {
			entityStand(left, right, jump);
		} else if (currentState == EntityState.MOVING) {
			entityMove(left, right, jump);
		} else if (currentState == EntityState.JUMPING) {
			entityJump(left, right);
		}
	}

	/**
	 * Handles the state machine for when the player is Standing.
	 *
	 * @param left  causes the BME to move left if true
	 * @param right causes the BME to move right if true
	 * @param jump  causes the BME to jump if true
	 */
	private void entityStand(boolean left, boolean right, boolean jump) {
		setVelocity(0, 0);
		if (!onGround) {
			setState(EntityState.JUMPING);
			return;
		}
		if (left != right) {
			setState(EntityState.MOVING);
		} else if (jump && ableToJump()) {
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
		}
	}

	/**
	 * Handles the state machine for when the player is Moving.
	 *
	 * @param left  causes the BME to move left if true
	 * @param right causes the BME to move right if true
	 * @param jump  causes the BME to jump if true
	 */
	protected void entityMove(boolean left, boolean right, boolean jump) {
		if (left == right) {
			setState(EntityState.STANDING);
			return;
		}
		if (left) {
			velX = -moveSpeed;
		} else {
			velX = moveSpeed;
		}
		if (jump && ableToJump()) {
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
		} else if (!onGround) {
			setState(EntityState.JUMPING);
		}

	}

	/**
	 * Handles the state machine for when the player is Jumping.
	 *
	 * @param left  causes the BME to move left if true
	 * @param right causes the BME to move right if true
	 * @param jump  causes the BME to jump if true
	 */
	protected void entityJump(boolean left, boolean right) {
		if (left == right) {
			velX = 0;
			if (onGround) {
				velY = 0;
				setState(EntityState.STANDING);
			}
		} else {
			if (onGround) {
				setState(EntityState.MOVING);
	        }
			if (left) {
				velX = -moveSpeed;
			} else {
				velX = moveSpeed;
			}
		}
		if (onCeiling) {
			velY = 0;
		}
	}
}