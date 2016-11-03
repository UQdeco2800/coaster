package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * EyeballNPC is a flying NPC implementation. It spawns above players and shoots
 * laser projectiles.
 */
public class EyeballNPC extends FlyingNPC {
	private long firingRateTracker; //ticks between firing
	private int laserDamage;
	private int laserSpeed;

	public EyeballNPC() {
		super();

		firingRateTracker = 0;
		laserDamage = 1;
		laserSpeed = 30;
		soundType = NPCSound.HIGH;

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.EYE_IDLE));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.EYE_NEAR));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.EYE_SHOOTING));
		setSprite(sprites.get(EntityState.MOVING));
	}

	@Override
	/**
	 * Unique logic for this NPC. Called every tick. This method calculates the
	 * Eyeballs's direction, hovering, and fires its bullets at the Player.
	 */
	public void determineNextAction() {
		chooseFacing();
		/* Fire at player if within range */
		if (!(distanceFromPlayer > 2 * Math.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer))) {
			setFiringFactors(target);
			if (this.firingRateTracker == 0) {
				/* Generate Bullets */
				setSprite(sprites.get(EntityState.JUMPING));
				// Can't fire lasers because no y velocity, TODO: refactor LazorParticle
				// LazorParticle laser = new LazorParticle(world, this, laserSpeed, )
				GenericBullet bullet = new GenericBullet(this, xFactor, yFactor, this.laserSpeed + 1, this.laserDamage,
						SpriteList.BAT_BULLET);
				world.addEntity(bullet);
				firingRateTracker = 60;
				setSprite(sprites.get(EntityState.STANDING));
			} else {
				firingRateTracker--;
			}
		}
		hover();
	}

	@Override
	public void onDeath(Entity cause) {
		super.onDeath(cause);
		if (isBoss) {
			((Player) cause).addBossKill("EYEBALL BOSS");
		}
		ItemDrop.drop(ItemRegistry.getItem("tears"), this.getX(),  this.getY());
		ItemDrop.drop(ItemRegistry.getItem("glass"), this.getX(),  this.getY());
		ItemDrop.drop(ItemRegistry.getItem("crystal"), this.getX(),  this.getY());
	}
}
