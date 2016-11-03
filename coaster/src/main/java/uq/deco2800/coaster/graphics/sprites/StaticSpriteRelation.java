package uq.deco2800.coaster.graphics.sprites;

import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.coaster.game.entities.Entity;

public class StaticSpriteRelation extends SpriteRelation {

	/**
	 * Implementation of SpriteRelation that renders the sprite on an angle. <p> Renders sprites without rotation
	 *
	 * @param sprite the actual sprite instance that gets rendered
	 * @param owner  the entity this SpriteRelation is tied to
	 * @param mountX the x-offset of this sprite from owner, in tiles
	 * @param mountY the y-offset of this sprite from owner, in tiles
	 * @param sizeX  the horizontal size of this sprite, in tiles
	 * @param sizeY  the vertical size of this sprite, in tiles
	 */
	public StaticSpriteRelation(Sprite sprite, Entity owner, float mountX,
								float mountY, float sizeX, float sizeY) {
		super(sprite, owner, mountX, mountY, sizeX, sizeY);
	}

	/**
	 * Renders the sprite with no rotation
	 *
	 * @param gc       the graphics context to be rendered on
	 * @param tileSize the size of a tile
	 * @param ownerX   the X coordinate of the entity in pixels
	 * @param ownerY   the Y coordinate of the entity in pixels
	 */
	public void renderSprite(GraphicsContext gc, float tileSize, float ownerX, float ownerY) {
		if (owner.getRenderFacing() > 0) {
			gc.drawImage(sprite.getFrame(), ownerX, ownerY, sizeX * tileSize, sizeY * tileSize);
		} else {
			gc.drawImage(sprite.getFrame(), ownerX + sizeX * tileSize, ownerY, -sizeX * tileSize,
					sizeY * tileSize);
		}
	}

	/**
	 * This is here because abstract classes mean it has to be i.e. I can call SpriteRelation.setTarget, rather than
	 * having to do a class check and cast to AngledSpriteRelation
	 */
	public void setTarget(double targetX, double targetY) {
		return; //Do nothing
	}
}
