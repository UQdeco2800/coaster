package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class GenericSpear extends Projectile {

	public GenericSpear(Entity owner, float velX, float velY, int speed, int damage, SpriteList sprite) {
		super(owner, velX, velY, speed, damage);
		bounds = new AABB(posX, posY, 1.3f, 0.6f); // Default bullet size
		setAngledSprite(sprite);
		enableGravity = false;
		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initFiringPosition();
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity instanceof Player) {
				spearBlink();
			}
		}
		this.kill(null);
	}
	public void spearBlink() {
		this.owner.setPosition(this.getX(), this.getY());;
	}

}
