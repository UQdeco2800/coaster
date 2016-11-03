package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.SpriteList;


public class GenericBlade extends Melee {

	public GenericBlade(Entity owner, int damage, SpriteList spriteID, int swingTime) {
		super(owner, damage, spriteID, swingTime);
		bounds = new AABB(posX, posY, 4f, 1f); //Default blade size

		enableGravity = false;

		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initSwingPosition();
	}

	/**
	 * Reduces health of entity on entity collide
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity instanceof BaseNPC && !(this.owner instanceof BaseNPC)) {
				if (((Player) owner).getEquippedWeapon().getSoilerAdder()) {
					TerrainDestruction.placeEnemyBlock(owner, (int) this.getX(), (int) this.getY(),
							this.getVelX(), this.getVelY());
				} else {
					((BaseNPC) entity).receiveDamage((int) damage, this.owner);
				}
			} else if (entity instanceof Player) {
				((Player) entity).addHealth(-(int) damage, this.owner);
			}
		}
		this.kill(null);
	}
	
	/**
	 * Removes upon death
	 */
	@Override
	protected void onDeath(Entity cause) {
	}

}
