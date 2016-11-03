package uq.deco2800.coaster.game.entities.npcs.mounts;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This mount is a flying bat mount, like the enemy, but black.
 */
public class BatMount extends Mount {
	public BatMount() {
		super();
		bounds = new AABB(posX, posY, 2f, 2f);

		sprites.put(EntityState.DEFAULT, new Sprite(SpriteList.BAT_MOUNT_STATIC));
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.BAT_MOUNT_FLAPPING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.BAT_MOUNT_FLAPPING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.BAT_MOUNT_FLAPPING));

		this.setState(EntityState.DEFAULT);
		this.saddleX = getWidth() / 2 - 1 / 2;
		this.saddleY = -1;
		this.setMoveSpeed(20);
		this.jumpSpeed = -10;
		this.flyingMount = true;
	}

	/**
	 * The instructions to be shown to the player when they mount this animal
	 * are included here.
	 */
	@Override
	protected void toastInstructions() {
		Toaster.darkToast("Press " + ControlsKeyMap.getStyledKeyCode(GameAction.MOVE_DOWN) + " to fly lower.");
		Toaster.darkToast("Press " + ControlsKeyMap.getStyledKeyCode(GameAction.MOVE_UP) + " to fly higher.");
		Toaster.darkToast("This bat loves to fly.");
	}
}
