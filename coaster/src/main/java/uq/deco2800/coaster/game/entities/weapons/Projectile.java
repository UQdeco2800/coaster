package uq.deco2800.coaster.game.entities.weapons;

import org.apache.commons.lang3.mutable.MutableDouble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.DuckKingNPC;
import uq.deco2800.coaster.game.entities.npcs.mounts.Mount;
import uq.deco2800.coaster.game.entities.particles.ParticleSource;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.AngledSpriteRelation;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;
import uq.deco2800.coaster.graphics.sprites.SpriteRelation;


public abstract class Projectile extends Entity {
	protected Entity owner;
	protected double damage;
	protected double decay;
	private int speed;
	private float initPosX;//initial position of projectile -> calculate distance and decrease damage over time
	private float initPosY;

	public Projectile(Entity owner, float velX, float velY, int speed, int damage) {
		enableGravity = false;
		this.damage = damage;
		this.decay = this.damage * 1 / 100;
		this.speed = speed;
		this.velX = velX * speed;	// + 1000*owner.getMeasuredVelX(); buggy
		this.velY = velY * speed;	// + 1000*owner.getMeasuredVelY();
		this.owner = owner;
		hurtByProjectiles = false;
		setBlocksOtherEntities(false);
		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		this.facing = (int) Math.signum(velX);
		this.renderFacing = facing;
	}
	

	protected void initFiringPosition() {
		/*Use velocity of projectile to place the bullet in a radius around the center of the owner
			velX/speed is the vector of the angle ie x is between -1 and 1.
			likewise for the initPosY;
			since this is using tile the radius will be equal to one tile width
		*/
		initPosX = owner.getBounds().posX() + (velX / speed) - this.bounds.getHalfWidth() + owner.getMeasuredVelX()*owner.getTick();
		initPosY = owner.getBounds().posY() + (velY / speed) - this.bounds.getHalfHeight() + owner.getMeasuredVelY()*owner.getTick();
		
		if((owner.getClass()).equals(Player.class)){
			if(((Player) owner).getOnMountStatus()){
				Mount mount =((Player)owner).getMount();
				initPosX = owner.getBounds().posX() + (velX / speed) - this.bounds.getHalfWidth() + mount.getMeasuredVelX()*owner.getTick();
				initPosY = owner.getBounds().posY() + (velY / speed) - this.bounds.getHalfHeight() + mount.getMeasuredVelY()*owner.getTick();;
			}

		}
		setPosition(initPosX, initPosY);
	}

	/* Particle effect on headshots */
	protected void createHeadshotParticles() {
		ParticleSource.addParticleSource(posX, posY, 297, 20, 5, true, false);
	}

	@Override
	protected void tick(long ms) {
		if (this.damage > 0) {
			this.damage -= this.decay;
		} else {
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
			if (location == BodyPart.HEAD) {
				Toaster.lightToast("Headshot");
			}
			float multiplier = location.getMultiplier();
			if (entity instanceof BaseNPC) {
				((BaseNPC) entity).receiveDamage((int) (damage * multiplier), this.owner);
			}
		}
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		if (side == Side.VOID) {
			this.kill(null);
			return;
		}
		if (owner instanceof Player) {
			if (((Player) owner).getEquippedWeapon().getSoilerAdder()) {
				TerrainDestruction.placeBlock(owner, tileX, tileY, side);
			} else {
				TerrainDestruction.damageBlock(tileX, tileY, (int)damage);
			}
		} else {
			TerrainDestruction.damageBlock(tileX, tileY, (int)damage);
		}
		this.kill(null);
	}

	@Override
	protected void onDeath(Entity cause) {
		this.delete();
	}

	protected void setAngledSprite(SpriteList sprite) {
		MutableDouble angle = new MutableDouble(Math.toDegrees(Math.atan2(velY, velX)));

		Map<BodyPart, SpriteRelation> angledSprite = new HashMap<BodyPart, SpriteRelation>();
		angledSprite.put(BodyPart.MAIN, new AngledSpriteRelation(new Sprite(sprite), this, angle, 0, 0,
				this.bounds.getWidth(), this.bounds.getHeight(), this.bounds.posX(), this.bounds.posY()));

		setAdditionalSprites(angledSprite);
	}
	

	/** 
	 * creates the list of htiboxes used for genericBullet and customBullet collisions
	 */
	protected void createHitboxes() {
		double angle = Math.atan2(velY, velX);
		
		float realWidth =(float) Math.abs(bounds.getWidth() * Math.cos(angle));
		float realHeight = (float) Math.abs(bounds.getWidth() * Math.sin(angle));

		float boxWidth = realWidth / 5f;
		float boxHeight = realHeight / 5f;
		
		int yDir = (int) Math.signum(velY);
		yDir = yDir == 0 ? 1 : yDir;
		
		float firstOffsetX = bounds.getHalfWidth() - boxWidth * 2 - (boxWidth / 2);
		float firstOffsetY = -yDir * 2 * boxHeight;
				
		for (int i = 0; i < 5; i++) {
			float offsetX = firstOffsetX + boxWidth * i;
			float offsetY = firstOffsetY + yDir * boxHeight * i;
			AABB hitbox = new AABB(posX, posY, boxWidth, boxHeight, offsetX, offsetY);
			hitbox.makeChild(true);
			hitbox.setPos(this.bounds, this.facing);
			hitboxes.add(hitbox);
		}		
	}

	/**
	 * Check if the target is the duck king, if so reflect the damage
	 * @param entity the target being hit
	 * @param hitLocation the location hit
	 * @return if the target should not take damage
	 */
	boolean handleDuckKing(Entity entity, BodyPart hitLocation) {
		if (entity instanceof DuckKingNPC) {
			if (hitLocation == BodyPart.SHIELD) {
				((DuckKingNPC) entity).hitDuckKingWithProjectile();
			} else {
				this.setVelocity(-1 * velX, -1 * velY);
				this.setPosition(posX + velX * 0.1F, posY + velY * 0.1F);
			}
			return true;
		}
		return false;
	}
}
