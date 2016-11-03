package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.weapons.CustomBullet;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by adam on 10/22/16.
 */
public class IceSpiritNPC extends FlyingNPC {
	private long firingRateTracker; //ticks between firing
	int bulletDamage;
	private int bulletSpeed;

	public IceSpiritNPC() {
		super();

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.ICE_SPIRIT_IDLE));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.ICE_SPIRIT_IDLE));
		sprites.put(EntityState.AIR_DASHING, new Sprite(SpriteList.ICE_SPIRIT_ATTACKING));
		setSprite(sprites.get(EntityState.MOVING));

		bounds = new AABB(posX, posY, 2f, 2f);
		bulletSpeed = 10;
		bulletDamage = 2;
		firingRateTracker = 0;
	}

	@Override
	/**
	 * Unique logic for this NPC. Called every tick. This method calculates the
	 * Eyeballs's direction, hovering, and fires its bullets at the Player.
	 */
	public void determineNextAction() {
		chooseFacing();
		hover();
		if (getDistanceFromPlayer(target) < 20) {
			setSprite(sprites.get(EntityState.AIR_DASHING));
			setFiringFactors(target);
			if (firingRateTracker == 0) {
				CustomBullet iceWhirl = new CustomBullet(this, xFactor, yFactor, bulletSpeed, bulletDamage, 100,
						SpriteList.ICE_SPIRIT_WHIRL, 1.5f, 1.5f, SpriteList.EXPLOSION, 1.5f, 1.5f);
				world.addEntity(iceWhirl);
				firingRateTracker = 60;
			} else {
				firingRateTracker--;
			}
		}
	}

	@Override
	public void onDeath(Entity cause) {
		super.onDeath(cause);
		if (isBoss) {
			((Player) cause).addBossKill("ICESPIRIT BOSS");
		}
	}
}