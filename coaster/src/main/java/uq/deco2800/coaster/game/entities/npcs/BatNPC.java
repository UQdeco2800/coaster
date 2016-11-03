package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * BatNPC is a flying NPC implementation. It spawns above players and shoots
 * damaging projectiles.
 */

public class BatNPC extends FlyingNPC {
	private long firingRateTracker; //ticks between firing
	int bulletDamage;
	private int bulletSpeed;

	/**
	 * Constructor for the BatNPC
	 */
	public BatNPC() {
		super();
		firingRateTracker = 0;
		bulletSpeed = 30 * (int) difficultyScale;
		bulletDamage = 2 * (int) difficultyScale;
		soundType = NPCSound.HIGH;
	}

	@Override
	/**
	 * Unique logic for this NPC. Called every tick. This method calculates the
	 * Bat's direction, bobbing movements, and fires its bullets at the Player.
	 */
	public void determineNextAction() {
		chooseFacing();
		/* Target the player */
		target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);

		/* Fire at player if within range */
		if (!(distanceFromPlayer > 2 * Math.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer))
				&& !isBlockedVision()) {
			setFiringFactors(target);
			if (this.firingRateTracker == 0) {
				/* Generate Bullets */
				setSprite(sprites.get(EntityState.STANDING));
				GenericBullet bullet = new GenericBullet(this, xFactor, yFactor, this.bulletSpeed, this.bulletDamage,
						SpriteList.BAT_BULLET);
				world.addEntity(bullet);
				firingRateTracker = 60;
				setSprite(sprites.get(EntityState.STANDING));
			} else {
				firingRateTracker--;
			}
		}
		flap();
	}

	@Override
	public void setBoss() {
		super.setBoss();
		bulletDamage *= 5;
	}

	@Override
	public void onDeath(Entity cause) {
		super.onDeath(cause);
		ItemDrop.drop(ItemRegistry.getItem("batwing"), this.getX(),  this.getY());
		ItemDrop.drop(ItemRegistry.getItem("batteeth"), this.getX(),  this.getY());
		if (isBoss) {
			((Player) cause).addBossKill("BAT BOSS");
		}
	}
}