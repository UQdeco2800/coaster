package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public abstract class Melee extends Entity {
	protected Entity owner;
	protected double damage;
	protected double time;
	private float initPosX;//initial position of projectile
	private float initPosY;
	private int weaponReach = 4; //default
	
	public Melee(Entity owner, int damage, SpriteList spriteID, int swingTime) {
		setSprite(new Sprite(spriteID));
		enableGravity = false;

		this.damage = damage;
		this.time = swingTime;
		this.owner = owner;
		hurtByProjectiles = false;
		setBlocksOtherEntities(false);

		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
	}

	/**
	 * Sets the initial position for the sword.
	 */
	protected void initSwingPosition() {
		if (owner.getFacing() == 1) {
			initPosX = owner.getBounds().posX();
			initPosY = owner.getBounds().posY() - bounds.getHeight() / 2;
			setPosition(initPosX, initPosY);
		} else {
			initPosX = owner.getBounds().posX() - bounds.getWidth();
			initPosY = owner.getBounds().posY() - bounds.getHeight() / 2;
			setPosition(initPosX, initPosY);
		}
		this.renderFacing = owner.getRenderFacing();
	}

	/**
	 * Updates remaining time, sword position and direction on each tick
	 */
	@Override
	protected void tick(long ms) {
		if (this.time > 0) {
			this.time--;
			//facing right
			if (owner.getRenderFacing() == 1) {
				this.renderFacing = 1;
				initPosX = owner.getBounds().posX();
				initPosY = owner.getBounds().posY() - bounds.getHeight() / 2;
				setPosition(initPosX, initPosY);
			}
			//facing left
			else {
				this.renderFacing = -1;
				initPosX = owner.getBounds().posX() - bounds.getWidth();
				initPosY = owner.getBounds().posY() - bounds.getHeight() / 2;
				setPosition(initPosX, initPosY);
			}
			TerrainDestruction.damageRectangle((int) initPosX, (int) initPosY, weaponReach, (int) damage);
		} else {
			this.delete();
		}

	}

	/**
	 * Reduces health of entity on entity collide
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (this.time > 0) {
			for (Entity entity : entities) {
				if (entity instanceof BaseNPC) {
					((BaseNPC) entity).receiveDamage((int) damage, this.owner);
				}
			}
		}
	}

	/**
	 * Removes on terrain collide
	 */
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
	}

	/**
	 * Removes upon death
	 */
	@Override
	protected void onDeath(Entity cause) {
		this.delete();
	}

}