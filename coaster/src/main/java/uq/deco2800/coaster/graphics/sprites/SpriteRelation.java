package uq.deco2800.coaster.graphics.sprites;

import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.coaster.game.entities.Entity;

public abstract class SpriteRelation {
	protected Sprite sprite;
	protected Entity owner;
	protected float mountX; // Offset from owner entity
	protected float mountY;
	protected float sizeX; // Size of additional sprite
	protected float sizeY;
	// All dimensions in tiles

	/**
	 * An abstract class, implemented by AngledSpriteRelation and StaticSpriteRelation <p> This allows multiple sprites
	 * to be rendered for each entity, without relying on a hitbox to define them. All distances/offsets are measured in
	 * tiles and measured from top left corners The mount point is defined by the offset from owner to this sprite
	 *
	 * @param sprite the actual sprite instance that gets rendered
	 * @param owner  the entity this SpriteRelation is tied to
	 * @param mountX the x-offset of this sprite from owner, in tiles
	 * @param mountY the y-offset of this sprite from owner, in tiles
	 * @param sizeX  the horizontal size of this sprite, in tiles
	 * @param sizeY  the vertical size of this sprite, in tiles
	 */
	public SpriteRelation(Sprite sprite, Entity owner, float mountX, float mountY, float sizeX, float sizeY) {
		this.sprite = sprite;
		this.owner = owner;
		this.mountX = mountX;
		this.mountY = mountY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	/**
	 * Renders the spriteRelation
	 */
	public abstract void renderSprite(GraphicsContext gc, float tileSize, float ownerX, float ownerY);

	/**
	 * Makes angled spriteRelations point towards the target point
	 */
	public abstract void setTarget(double targetX, double targetY);

	/**
	 * Gets the sprite
	 *
	 * @return the actual sprite instance
	 */
	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * Returns the x-offset of the mounting point
	 *
	 * @return the x-offset of the mounting point
	 */
	public float getmountX() {
		return mountX;
	}

	/**
	 * Returns the y-offset of the mounting point
	 *
	 * @return the y-offset of the mounting point
	 */
	public float getmountY() {
		return mountY;
	}

	/**
	 * Returns the horizontal size of the sprite
	 *
	 * @return the horizontal size of the sprite
	 */
	public float getSizeX() {
		return sizeX;
	}

	/**
	 * Returns the vertical size of the sprite
	 *
	 * @return the horizontal size of the sprite
	 */
	public float getSizeY() {
		return sizeY;
	}

}
