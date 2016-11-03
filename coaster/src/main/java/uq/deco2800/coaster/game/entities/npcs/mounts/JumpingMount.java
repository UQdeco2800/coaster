package uq.deco2800.coaster.game.entities.npcs.mounts;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This mount is jumpy animal, currently a frog.
 */
public class JumpingMount extends Mount {
	/* The acceleration of the mounts movement */
	private static final int ACCELERATION = 20;

	public JumpingMount() {
		super();
		bounds = new AABB(posX, posY, 3f, 3f);

		sprites.put(EntityState.DEFAULT, new Sprite(SpriteList.FROG_STANDING));
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.FROG_STANDING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.FROG_WALKING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.FROG_WALKING));

		this.setState(EntityState.DEFAULT);

		this.saddleX = 1;
		this.saddleY = (int) getHeight() / 5 - 1;
		this.setMoveSpeed(15);
		this.jumpSpeed = -50;
	}

	/**
	 * A method for moving the mount, customised to allow a unique jumping
	 * movement.
	 * 
	 * @param left
	 *            reflects whether the entity is moving left
	 * @param right
	 *            reflects whether the entity is moving right
	 * @param jump
	 *            reflects whether the entity is jumping
	 */
	@Override
	protected void entityMove(boolean left, boolean right, boolean jump) {
		if (left == right) {
			setState(EntityState.STANDING);
			return;
		}
		if ((!jump) && currentState != EntityState.JUMPING) {
			if (left) { // ground speed
				velX = -moveSpeed;
			} else {
				velX = moveSpeed;
			}
		}

		if (jump && ableToJump()) {
			setState(EntityState.JUMPING);
			velY = (float) .8 * jumpSpeed;
			velX = (float) (1 * getFacing() * -1 * jumpSpeed);
		} else if (!onGround) {
			setState(EntityState.JUMPING);
		}

	}

	/**
	 * A method for moving the jumping mount, customised to create a unique
	 * feel, launching the frog forward in a low arc when moving sideways, or
	 * jumping high in the air when not.
	 * 
	 * @param left
	 *            reflects whether the entity is moving left
	 * @param right
	 *            reflects whether the entity is moving right
	 */
	@Override
	protected void entityJump(boolean left, boolean right) {
		if (playerInput.getDownPressed() && playerInput.getJumpPressed()) {
			velY = (float) (.8 * -jumpSpeed);
			return;
		}
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
			if (left && velX > -moveSpeed) {
				velX -= ACCELERATION; // air horizontal speed
			} else if (right && velX < moveSpeed) {
				velX += ACCELERATION;
			}
		}
		if (onCeiling) {
			velY = 0;
		}

		if (playerInput.getDownPressed()) {
			if (velY > jumpSpeed) {
				velY += .1 * ACCELERATION;
			}
		}
	}

	/**
	 * Handles collision with terrain. On landing the terrain is damaged.
	 * 
	 * @param tileX
	 *            the x coordinate of the collided tile
	 * @param tileY
	 *            the y coordinate of the collided tile
	 * @param side
	 *            the side of the tile collided with
	 */
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		if (velY > -.9 * jumpSpeed) {
			int radius = (int) Math.abs(velY / 15);
			TerrainDestruction.damageCircle(tileX - radius, tileY - (int) (radius * 1.5), radius, 100);
			velX = 0;
			velY = 0;
			setState(EntityState.STANDING);
		}
	}

	/**
	 * The instructions to be shown to the player when they mount this animal
	 * are included here, in reverse order.
	 */
	@Override
	protected void toastInstructions() {
		Toaster.darkToast("Press " + ControlsKeyMap.getStyledKeyCode(GameAction.MOVE_DOWN) + " to speed up its fall.");
		Toaster.darkToast("Jump while pressing " + ControlsKeyMap.getStyledKeyCode(GameAction.MOVE_LEFT) + " or "
				+ ControlsKeyMap.getStyledKeyCode(GameAction.MOVE_RIGHT) + " to launch forward.");
		Toaster.darkToast("This frog loves to jump.");

	}
}
