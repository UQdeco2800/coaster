package uq.deco2800.coaster.game.entities.npcs.mounts;

import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This mount is a slow elephant.
 */
public class ElephantMount extends Mount {
	public ElephantMount() {
		super();
		bounds = new AABB(posX, posY, 6f, 5f);

		sprites.put(EntityState.DEFAULT, new Sprite(SpriteList.ELEPHANT_STANDING));
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.ELEPHANT_STANDING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.ELEPHANT_WALKING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.ELEPHANT_WALKING));
		this.setState(EntityState.DEFAULT);
		this.saddleX = 1;
		this.saddleY = 0;
		this.setMoveSpeed(5);
		this.jumpSpeed = -15;
		this.jumpDown = true;
	}

	/**
	 * Handles collision with terrain. On horizontal collision the terrain is
	 * destroyed.
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
		if (1000 * getMeasuredVelY() > 10) {
			this.setVelocity(getVelX(), 0);
			for (int i = (int) this.getX() - 1; i < ((int) this.getX() + this.getWidth() + 1); i++) {
				TerrainDestruction.damageBlock(i, tileY, 100);
				TerrainDestruction.damageBlock(i, tileY + 1, 25);
			}
		}
		if (1000 * getMeasuredVelY() < -1) {
			for (int i = (int) this.getX() - 1; i < ((int) this.getX() + this.getWidth()); i++) {
				TerrainDestruction.damageBlock(i, tileY, 100);
				TerrainDestruction.damageBlock(i, tileY + 1, 25);
			}
		}

		if (tileY >= this.getY() && tileY < this.getY() + (int) this.getHeight()) {
			TerrainDestruction.destroyColumn(tileX, (int) this.getY() + (int) this.getHeight() - 1, 100,
					(int) this.getHeight());
		}
	}

	/**
	 * The instructions to be shown to the player when they mount this animal
	 * are included here, in reverse order.
	 */
	@Override
	protected void toastInstructions() {
		Toaster.darkToast("It will destroy any blocks in its way.");
		Toaster.darkToast("This elephant is unstoppable.");
	}
}
