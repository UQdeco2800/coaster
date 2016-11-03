package uq.deco2800.coaster.game.entities.npcs.mounts;

import java.util.Random;

import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This mount is a slow turtle.
 */
public class TurtleMount extends Mount {
	public TurtleMount() {
		super();
		bounds = new AABB(posX, posY, 4f, 3f);

		sprites.put(EntityState.DEFAULT, new Sprite(SpriteList.TURTLE_STANDING));
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.TURTLE_STANDING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.TURTLE_WALKING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.TURTLE_WALKING));

		this.setState(EntityState.DEFAULT);

		this.saddleX = 2;
		this.saddleY = 0;
		Random random = new Random();
		if (random.nextInt(100) > 75) {
			this.setMoveSpeed(60);
			this.jumpSpeed = -40;
		} else {
			this.setMoveSpeed(1);
			this.jumpSpeed = -1;
		}

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
		if (tileY >= this.getY() && tileY < this.getY() + (int) this.getHeight()) {
			TerrainDestruction.destroyColumn(tileX, (int) this.getY() + (int) this.getHeight() - 1, 300,
					(int) this.getHeight());
		}
	}

	/**
	 * The instructions to be shown to the player when they mount this animal
	 * are included here, in reverse order.
	 */
	@Override
	protected void toastInstructions() {
		Toaster.darkToast("Slow and steady");
	}
}
