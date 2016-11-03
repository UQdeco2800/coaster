package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.particles.ParticleSource;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class PortalBullet extends Projectile {
	
	private boolean collides = false;
	private boolean justTeleportedTo = false;
	private boolean hit = false;
	private boolean portalType;
	private int range = 300; //100 default so offscreen
	private Entity owner;
	private int count;

	/**
	 * Created by Barney
	 * 
	 */
	public PortalBullet(Entity owner, float velX, float velY, int speed, boolean portalType) {

		super(owner, velX, velY, speed, 0);
		//set defaults if not specificed
		/**
		 * More information about what the defaults are on available on the wiki
		 */
		if (owner instanceof Player) {
			SoundCache.play("PortalAttack");
		}
		bounds = new AABB(posX, posY, 0.5f, 0.5f);
		this.portalType = portalType;
		this.owner = owner;
		if(portalType) {
			setSprite(new Sprite(SpriteList.PORTAL_BULLET_BLUE));
		} else {
			setSprite(new Sprite(SpriteList.PORTAL_BULLET_ORANGE));
		}
		enableGravity = false;

		setCollisionFilter(e -> e.isHurtByProjectiles());
		initFiringPosition();
	}

	@Override
	protected void tick(long ms) {
		if(!hit) {
			range--;
		}
		if (range == 0) {
			this.kill(null);
		}
		if(this.collides) {
			count++;
		}
		//If it's been more than 1 tick since last collision detection with player
		if(count > 1) {
			this.collides = false;
			this.justTeleportedTo = false;
		}
	}
	
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (this.bounds.collides(this.owner) != BodyPart.VOID) {
			this.collides = true;
			this.count = 0;
			if(!this.collides && this.hit){
				Toaster.toast("Press B to enter the portal!");
			}
		this.collides = true;
		this.count = 0;
		}
	}
//		for (Entity entity : entities) {
//			if (entity.equals(this.owner)) {
//				if(!this.collides && this.hit){
//					Toaster.toast("Press B to enter the portal!");
//				}
//				this.collides = true;
//				this.count = 0;
//			}
//		}
//	}
	
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		if (hit) {
			return;
		}
		//Stop the portal once it has hit the ground
		
		int newX = tileX;
		int newY = tileY;
		switch (side) {
			case VOID:
			case LEFT:
				newX = tileX - 1;
				newY = tileY - 1;
				break;
			case RIGHT:
				newX = tileX + 1;
				newY = tileY - 1;
				break;
			case TOP:
				newX = tileX;
				newY = tileY + 1;
				break;
			case BOTTOM:
				newX = tileX;
				newY = tileY - 2;
				break;
		}
		
		setPosition(newX, newY);
		setSize(1f, 2f);

		setPosition(bounds.left(), bounds.top());
		
		// if things are still colliding we say fuck it and nuke the terrain
		TerrainDestruction.damageRectangle((int) posX, (int) posY, 1, 100);
		
		hit = true;
		//Set its size and sprite
		if(portalType) {
			this.setSprite(new Sprite(SpriteList.PORTAL_BLUE));
		} else {
			this.setSprite(new Sprite(SpriteList.PORTAL_ORANGE));
		}
		this.setVelocity(0, 0);
	}
	
	/***
	 * Takes a portal bullet object and saves it to use to teleport players
	 * @param PortalBullet portal, the portal bullet corresponding to the other portal
	 */
	
	
	public void teleport(boolean teleportedTo) {
		this.justTeleportedTo = teleportedTo;
		if(this.portalType) {
			ParticleSource.addParticleSource(posX + 0.5f, posY + 1f, 298, 20, 5, true, false);
		} else {
			ParticleSource.addParticleSource(posX + 0.5f, posY + 1f, 299, 20, 5, true, false);
		}
		
	}
	
	/*** 
	 * sets whether or not the portal has hit the ground - used for testing
	 * @param hit, describes wheter the portal has hit the ground
	 */
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
	public float[] getPos() {
		return new float[] { this.posX, this.posY };
	}
	
	public boolean collides() {
		return this.collides && !this.justTeleportedTo && this.hit;
	}
	
	public boolean isHit() {
		return this.hit;
	}
}


