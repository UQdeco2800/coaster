package uq.deco2800.coaster.game.entities.weapons;

import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.DuckKingNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class GrenadeBullet extends Projectile {
	private static final float EPSILON = 0.00001f;

	private boolean landed = false;
	private int time; //time until explosion after initial collision
	private int radius; //radius of grenade explosion in tiles
	private long deathTime = 0; //time of death, used for animating the explosion
	private float deathPosX;//used to animate
	private float deathPosY;
	private int blockDamage;
	
	public GrenadeBullet(Entity owner, float velX, float velY, int speed, int time, int damage, int radius, SpriteList sprite, boolean grav) {
		super(owner, velX, velY, speed, damage);
		bounds = new AABB(posX, posY, 0.5f, 0.5f); //Default grenade size
		setAngledSprite(sprite);
		this.time = time;
		this.radius = radius;
		enableGravity = grav;
		blockDamage = damage;
		//Possible change collision so that the player can get hurt by it
		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initFiringPosition();
	}

	@Override
	protected void tick(long ms) {
		if (this.time == 0 && landed) {//explode
			setAdditionalSprites(null);
			if ((Math.abs(deathPosX) < EPSILON) && (Math.abs(deathPosY) < EPSILON)) {
				this.deathPosX = posX;//set death position for animation
				this.deathPosY = posY;
			}
			List<Entity> entities = this.getNearbyEntities(radius);
			for (Entity entity : entities) {//BOOM!
				if (entity instanceof DuckKingNPC) {
					// Do nothing because the duck king takes no grenade damage
				} else if (entity instanceof BaseNPC) {
					((BaseNPC) entity).receiveDamage((int) damage, this);
				} else if (entity instanceof Player) {
					((Player) entity).addHealth(-(int) damage, this.owner);
				}
			}
			this.preDeath();//animate the explosion
		} else if (landed) {
			this.time--;//count down to explosion
		}
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {//if collision with entity initiate the count down
		land(true);
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {//if collision with terrain initiate the count down
		land(false);
	}

	private void land(boolean grav) {
		this.enableGravity = grav;
		this.setVelocity(0, 0);
		this.landed = true;
	}

	private void preDeath() {
		++deathTime;
		this.facing = 1;
		this.setVelocity(0, 0);
		this.damage = 0;//make it only deal damage from initial explosion
		//increase radius to full size over time
		float r = 2 * this.radius * this.deathTime / 10;
		this.setPosition(deathPosX - (r / 2), deathPosY - (r / 2));
		//set size based on the time since exploding
		bounds.setAABB(posX, posY, r, r);
		setSprite(new Sprite(SpriteList.EXPLOSION));
		if (this.deathTime == 10) {//if the grenade has been dead for 10 ticks
			this.kill(null);
			TerrainDestruction.damageCircle((int) posX, (int) posY, radius, this.blockDamage);
		}

	}
}
