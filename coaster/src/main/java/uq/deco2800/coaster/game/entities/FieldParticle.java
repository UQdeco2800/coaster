package uq.deco2800.coaster.game.entities;

import uq.deco2800.coaster.game.entities.weapons.SuperBullet;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.SpriteList;


public class FieldParticle extends SuperBullet {
	protected Entity owner;
	protected double damage;
	int position;
	float initX;
	float initY;

	public FieldParticle(Entity owner, float initX, float initY, int damage, int position) {
		super(owner, 0, 0, 0, 0, SpriteList.LAZOR);

		enableGravity = false;

		this.damage = damage;
		this.owner = owner;
		this.posX = initX;
		this.posY = initY;
		this.position = position;
		hurtByProjectiles = false;
		setBlocksOtherEntities(false);
		this.velX = 0;
		this.velY = 0;
		setPosition(posX, posY);
		bounds = new AABB(posX, posY, 0.05f, 0.05f);

		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
	}

	public int getPosition() {
		return position;
	}

	@Override
	protected void tick(long ms) {
		float xPos = (float) (owner.getX() + 0.5 * owner.getWidth() + 2 * Math.cos(Math.toRadians(position)));
		float yPos = (float) (owner.getY() + 0.5 * owner.getWidth() + 2 * Math.sin(Math.toRadians(position)));
		setPosition(xPos, yPos);
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		return;
	}

	@Override
	protected void onDeath(Entity cause) {
		this.delete();
	}
}
