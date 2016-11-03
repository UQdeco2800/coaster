package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class FireProjectile extends Projectile{
	
	private int time = (int)(Math.random() * 200) + 100;
	private boolean hit = false;
	
	public FireProjectile(Entity owner, float velX, float velY, int speed, int damage) {
		super(owner, velX, velY, speed, damage);
		this.setSprite(new Sprite(SpriteList.FLAME));
		this.enableGravity = true;
		bounds = new AABB(posX, posY, 1f, 0.5f);
		setCollisionFilter(e -> e.isHurtByProjectiles());
		initFiringPosition();
	}
	
	protected void tick(long ms) {
		if(this.hit) {
			this.time--;
		}
		if(time <= 0) {
			this.kill(null);
		}
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
			if (entity instanceof BaseNPC) {
				((BaseNPC) entity).receiveDamage((int) (damage * multiplier), this.owner);
			}
		}
		this.hit = true;
	}
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		//Stop the portal once it has hit the ground
		this.setVelocity(0, 0);
		this.setX(tileX);
		this.setY(tileY-2);
		hit = true;
		//Set its size and sprite
		this.setSize(1f, 2f);
		this.setSprite(new Sprite(SpriteList.FLAME));
	}
}
