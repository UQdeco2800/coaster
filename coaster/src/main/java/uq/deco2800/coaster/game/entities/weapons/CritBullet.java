package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class CritBullet extends Projectile {

	public CritBullet(Entity owner, float velX, float velY, int speed, int damage) {
		super(owner, velX, velY, speed, damage);
		if (owner instanceof Player) {
			SoundCache.play("critical");
		}
		bounds = new AABB(posX, posY, 0.25f, 0.0625f); //Default bullet size
		setAngledSprite(SpriteList.LAZOR);
		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initFiringPosition();
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			BodyPart location = hitLocations.get(i);
			if (handleDuckKing(entity, location)) {
				return;
			}
			if (location == BodyPart.HEAD) {
				Toaster.lightToast("Headshot");
				createHeadshotParticles();
			}
			float multiplier = location.getMultiplier();
			if (entity instanceof BaseNPC) {
				((BaseNPC) entity).receiveDamage((int) (damage * multiplier), this.owner);
			}
		}
		this.kill(null);
	}
}
