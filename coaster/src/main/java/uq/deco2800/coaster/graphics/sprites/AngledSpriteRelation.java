package uq.deco2800.coaster.graphics.sprites;

import org.apache.commons.lang3.mutable.MutableDouble;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;
import uq.deco2800.coaster.game.entities.Entity;

public class AngledSpriteRelation extends SpriteRelation {
	protected float pivotX; // pivot point offset in tiles
	protected float pivotY; // pivot point offset in tiles

	protected MutableDouble angle;

	/**
	 * Implementation of SpriteRelation that renders the sprite on an angle. <p> Renders sprites at the angle specified
	 * in this.angle
	 *
	 * @param sprite the actual sprite instance that gets rendered
	 * @param owner  the entity this SpriteRelation is tied to
	 * @param angle  a Mutable Double holding the angle, in degrees, that this is rendered at. The purpose of a
	 *               MutableDOuble is to allow this to be modified both within this class and within the owner. Should
	 *               be between -90 and +90, with 0 being horizontal. Sprites are flipped when facing left so this
	 *               should not be accounted for outside of this class.
	 * @param mountX the x-offset of this sprite from owner, in tiles
	 * @param mountY the y-offset of this sprite from owner, in tiles
	 * @param sizeX  the horizontal size of this sprite, in tiles
	 * @param sizeY  the vertical size of this sprite, in tiles
	 * @param pivotX the x-offset of the pivot point from the mounting point, in tiles
	 * @param pivotY the x-offset of the pivot point from the mounting point, in tiles
	 */
	public AngledSpriteRelation(Sprite sprite, Entity owner, MutableDouble angle, float mountX, float mountY,
								float sizeX, float sizeY, float pivotX, float pivotY) {
		super(sprite, owner, mountX, mountY, sizeX, sizeY);
		this.angle = angle;
		this.pivotX = pivotX;
		this.pivotY = pivotY;
	}

	/**
	 * Renders the sprite at an angle
	 *
	 * @param gc       the graphics context to be rendered on
	 * @param tileSize the size of a tile
	 * @param ownerX   the X coordinate of the entity in pixels
	 * @param ownerY   the Y coordinate of the entity in pixels
	 */
	public void renderSprite(GraphicsContext gc, float tileSize, float ownerX, float ownerY) {
		gc.save();
		if (owner.getRenderFacing() > 0) {
			Rotate r = new Rotate(angle.getValue(), ownerX + mountX * tileSize + pivotX * tileSize,
					ownerY + mountY * tileSize + pivotY * tileSize);
			gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
			gc.drawImage(getSprite().getFrame(), ownerX + mountX * tileSize, ownerY + mountY * tileSize,
					sizeX * tileSize, sizeY * tileSize);
		} else {
			Rotate r = new Rotate(angle.getValue(),
					ownerX + owner.getBounds().getWidth() * tileSize - mountX * tileSize - pivotX * tileSize,
					ownerY + mountY * tileSize + pivotY * tileSize);
			gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
			gc.drawImage(getSprite().getFrame(),
					ownerX + owner.getBounds().getWidth() * tileSize - mountX * tileSize - 2 * pivotX * tileSize,
					ownerY + mountY * tileSize + 2 * pivotY * tileSize, sizeX * tileSize, -sizeY * tileSize);
		}
		gc.restore();
	}

	/**
	 * Adjusts the angle to point directly from the pivot point to the input point. All inputs in tiles.
	 */
	public void setTarget(double targetX, double targetY) {
		if (owner.getRenderFacing() > 0) {
			angle.setValue(Math.toDegrees(Math.atan2(targetY - (owner.getY() + pivotY + mountY),
					targetX - (owner.getX() + pivotX + mountX))));
		} else {
			angle.setValue(Math.toDegrees(Math.atan2(targetY - (owner.getY() + pivotY + mountY),
					targetX - (owner.getX() + owner.getBounds().getWidth() - pivotX - mountX))));
		}
	}

	/**
	 * Gets the x-offset of the pivot point. Will be useful for rendering held weapons
	 *
	 * @return the x-offset of the pivot point
	 */
	public float getPivotX() {
		return pivotX;
	}

	/**
	 * Gets the y-offset of the pivot point. Will be useful for rendering held weapons
	 *
	 * @return the y-offset of the pivot point
	 */
	public float getPivotY() {
		return pivotY;
	}

}
