package uq.deco2800.coaster.game.entities.npcs.mounts;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.GameInput;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;

/**
 * The mount is a parent class to entities that can be ridden by the player.
 */
public class Mount extends BasicMovingEntity {
	private static Logger logger = LoggerFactory.getLogger(Mount.class);
	// the distance the player can be and still get on mount
	protected final int range = 2;
	// the X position of the player relative to the mount
	protected float saddleX;
	// the Y position of the player relative to the mount
	protected float saddleY;
	// the mount rider
	protected Player rider;
	// the game input
	protected GameInput playerInput = new GameInput();
	// whether or not the mount should fly
	protected boolean flyingMount = false;
	// whether or not the mount can jump down quickly
	protected boolean jumpDown = false;

	/**
	 * Creates a mount with no health bar that doesn't block other entities
	 */
	public Mount() {
		super();
		setBlocksOtherEntities(false);
		disableHealthBar();
	}

	/**
	 * The tick method is called many times per second automatically
	 * 
	 * @param ms
	 *            the time since last cycle
	 */
	@Override
	protected void tick(long ms) {
		if (!(this.rider == null)) {
			playerInput.updateGameInput();
			if (flyingMount) {
				fly(playerInput.getLeftPressed(), playerInput.getRightPressed(), playerInput.getUpPressed(),
						playerInput.getDownPressed());
			} else {
				moveStateEntity(ms, playerInput.getLeftPressed(), playerInput.getRightPressed(),
						playerInput.getJumpPressed());
				if (playerInput.getDownPressed() && jumpDown && playerInput.getJumpPressed()) {
					/* Do nothing */
				} else if (playerInput.getDownPressed() && jumpDown) {
					if (1000 * getMeasuredVelY() < -1.5 * jumpSpeed) {
						velY -= jumpSpeed;
					}
				}
			}
			PlayerMountActions.positionPlayer(rider);
		} else {
			boolean playerNear = false;
			for (Entity entity : this.getNearbyEntities(this.range)) {
				if (entity instanceof Player) {
					playerNear = true;
				}
			}
			if (playerNear) {
				Toaster.ejectAllToast();
				Toaster.toast("Press " + ControlsKeyMap.getStyledKeyCode(GameAction.ACTIVATE_MOUNT) + " to mount");
			}
		}
	}

	/**
	 * Register the rider of the mount as the player specified.
	 * 
	 * @param rider
	 *            the rider of the mount
	 */
	public void registerRider(Player rider) {
		this.rider = rider;
		this.rider.toggleGravity();
		this.setState(EntityState.STANDING);
		Toaster.ejectAllToast();
		toastInstructions();
		logger.info("Player mounted");
	}

	/**
	 * Returns the ride of the mount.
	 * 
	 * @return Returns the ride of the mount.
	 */
	public Player getRider() {
		return this.rider;
	}

	/**
	 * Removes the rider from the mount
	 * 
	 * @param player
	 *            the rider to remove
	 */
	public void removeRider(Player player) {
		this.rider.toggleGravity();
		this.rider = null;
		this.setState(EntityState.DEFAULT);
		velX = 0;
		velY = 0;
	}

	/**
	 * Handles the movement for mounts who fly
	 * 
	 * @param left
	 *            indicates left movement when true
	 * @param right
	 *            indicates right movement when true
	 * @param up
	 *            indicates up movement when true
	 * @param down
	 *            indicates down movement when true
	 */
	public void fly(boolean left, boolean right, boolean up, boolean down) {
		if (left) {
			velX = -moveSpeed;
		} else if (right) {
			velX = moveSpeed;
		} else {
			velX = 0;
		}

		if (up) {
			velY = jumpSpeed;
		} else if (down) {
			velY = -2 * jumpSpeed;
		} else {
			velY = 0;
		}
	}

	/**
	 * Returns the x offset of the player relative to the mount
	 * 
	 * @return the x offset of the player relative to the mount
	 */
	public float getSaddleX() {
		return saddleX;
	}

	/**
	 * Returns the y offset of the player relative to the mount
	 * 
	 * @return the y offset of the player relative to the mount
	 */
	public float getSaddleY() {
		return saddleY;
	}

	/**
	 * Toaster notifications to show once the player has mounted
	 */
	protected void toastInstructions() {
		/* Do nothing */
	}

	/**
	 * Updates the state of the entity.
	 */
	@Override
	protected void stateUpdate(long ms) {
		/* Do nothing */
	}

	/**
	 * Handles collisions of entities
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		/* Do nothing */
	}

	/**
	 * Handles collision with terrain.
	 */
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		/* Do nothing */
	}

	/**
	 * Actions to be performed on mount's death.
	 */
	@Override
	protected void onDeath(Entity cause) {
		/* Do nothing */
	}
}
