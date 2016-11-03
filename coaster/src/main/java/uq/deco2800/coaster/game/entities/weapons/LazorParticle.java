package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class LazorParticle extends Projectile {

	public LazorParticle(Entity owner, int speed, int direction) {
		super(owner, direction, 0, speed, 100);
		this.setSprite(new Sprite(SpriteList.LAZOR));
		bounds = new AABB(posX, posY, 3f, 1f);
		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initFiringPosition();
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity instanceof BaseNPC) {
				((BaseNPC) entity).receiveDamage((int) damage, this.owner);
			}
		}
		this.kill(null);
	}
}
