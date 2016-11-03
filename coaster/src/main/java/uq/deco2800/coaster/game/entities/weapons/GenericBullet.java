package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class GenericBullet extends Projectile {

	public GenericBullet(Entity owner, float velX, float velY, int speed, int damage, SpriteList sprite) {
		super(owner, velX, velY, speed, damage);
		if (owner instanceof Player) {
			SoundCache.play("attack");
		}
		bounds = new AABB(posX, posY, 0.75f, 0.25f); // Default bullet size
		bounds.setActive(false);
		setAngledSprite(sprite);
		
		enableGravity = false;

		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initFiringPosition();
		
		createHitboxes();
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			BodyPart location = hitLocations.get(i);
			if (handleDuckKing(entity, location)) {
				return;
			}
			float multiplier = location.getMultiplier();
			if (location == BodyPart.HEAD) {
				Toaster.lightToast("Headshot");
				createHeadshotParticles();
			}			
			if (entity instanceof BaseNPC && !(this.owner instanceof BaseNPC)) {
				if (((Player) owner).getEquippedWeapon().getSoilerAdder()) {
					TerrainDestruction.placeEnemyBlock(owner, (int) this.getX(), (int) this.getY(),
							this.getVelX(), this.getVelY());
				} else {
					((BaseNPC) entity).receiveDamage((int) (damage * multiplier), this.owner);
				}
			} else if (entity instanceof Player) {
				((Player) entity).addHealth(-(int) (damage * multiplier), this.owner);
			}
		}
		this.kill(null);
	}
}
