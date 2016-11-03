package uq.deco2800.coaster.game.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.Multiplayer;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.singularity.common.representations.coaster.state.*;

import java.util.List;

public class PlayerMultiDummy extends PlayerMulti {
	private static final Logger logger = LoggerFactory.getLogger(PlayerMultiDummy.class);
	private boolean useless = true;

	/**
	 * The Player class is the entity controlled by the user.
	 */
	public PlayerMultiDummy(String name) {
		super(false, name);
	}


	/**
	 * Updates the players state through user button presses.
	 *
	 * @param ms millisecond tick the player attack is being handled on
	 */
	@Override
	protected void stateUpdate(long ms) {
		//updateRenderAngle();
		//playerInput.updateGameInput();
		if (name.equals("")) {
			throw new IllegalStateException("PlayerMultiDummy has no name");
		}

		tick(ms);
	}

	protected void updates(List<Update> us) {
		for (Update u : us) {
			if (null == u || u instanceof NilUpdate) {
				continue;
			}
			update(u);
		}
	}

	protected void update(Update u) {
		//logger.info("updating " + name + " with " + u.getClass().toGenericString());
		if (u instanceof DisconnectedPlayer) {
			world.deleteEntity(this);
		} else if (u instanceof PlayerUpdate) {
			update((PlayerUpdate) u);
		} else if (u instanceof FireUpdate) {
			update((FireUpdate) u);
		} else if (u instanceof DamageUpdate) {
			update((DamageUpdate) u);
		} else {
			logger.error("Update not of useable type; " + u.getClass().getName());
		}
	}

	protected void update(PlayerUpdate u) {
		logger.info("" + u.getName() + " " + u.getPosX() + u.getPosY());
		currentHealth = u.getHealth();
		posX = u.getPosX();
		posY = u.getPosY();
	}

	protected void update(FireUpdate u) {

	}

	protected void update(DamageUpdate u) {

	}

	@Override
	public void entityLoop(long ms) {
		//logger.info("dummy loop");
		tick(ms);
	}

	/**
	 * Tick handler for player
	 *
	 * @param ms millisecond tick the player attack is being handled on
	 */
	@Override
	public void tick(long ms) {
		// Skill Controlling
		//logger.info("dummy tick");
		updates(Multiplayer.getUpdates(name));
		//logger.info("dummy should have updates");
		if (stunned) {
			return;
		}
		updateTimers(ms);
	}

	/**
	 * Method to handle player's attack and to have a look at the attack type.
	 * <p>
	 * The type of player attack determined. When more types of attacks or
	 * different weapons is added to the game then this function could
	 * distinguish between the attacks. This method also decides if the attack
	 * was a critical hit or not.
	 *
	 * @param ms          millisecond tick the player attack is being handled on
	 * @param basicAttack true if the basic attack is selected by the player
	 */

	protected void playerAttack(long ms, boolean basicAttack) {
		if (currentState == EntityState.DASHING || currentState == EntityState.AIR_DASHING
				|| currentState == EntityState.STUNNED || currentState == EntityState.SLIDING) {
			return;
		}
		if (basicAttack) {
			SoundCache.getInstance().play("attack");
			equippedWeapon.basicAttack(this, getProjectileVelocity()[0], getProjectileVelocity()[1], this.bulletSpeed,
					this.genericBulletDamage);

		}
	}

	/**
	 * Updates renderFacing to face towards the cursor Updates the arm and head
	 * additional sprites to point to the cursor.
	 */

	private void updateRenderAngle() {
		//TODO RyanCarrier
	}

	/**
	 * returns the x and y velocity for a projectile that is aimed at the mouse
	 *
	 * @return float[0] = xvel, float[1] = yvel
	 */
	private float[] getProjectileVelocity() {
		// Barney Whiteman
		/*
		 * Angle between player and mouse with accuracy built in
		 *
		 * aim is a random number between -accuracy and +accuracy and then
		 * converted into radians
		 */
		float prjXVel = 0f;
		float prjYVel = 0f;
		if (viewport != null) {
			double aim = (Math.PI / 180) * ((Math.random() * accuracy * 2) - accuracy);
			aimAngle = (Math.atan2(InputManager.getDiffY(posY + this.getHeight() / 2),
					InputManager.getDiffX(posX + this.getWidth() / 2))) + aim;
			prjXVel = (float) (Math.cos(aimAngle));
			prjYVel = (float) (Math.sin(aimAngle));
		}
		return new float[]{prjXVel, prjYVel};
	}

	/**
	 * Entity collision event handler
	 * <p>
	 * We deal knockback only with the first entity for convenience
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (entities.isEmpty()) {
			return;
		}
		for (Entity entity : entities) {
			if (entity instanceof CompanionNPC) {
				return;
			} else if (entity instanceof BaseNPC) {
				int knockBackDir = (int) Math.signum(entity.getVelX());
				transitionToKnockBack(knockBackDir);
				return;
			}
		}
	}


	/**
	 * On death event handler;
	 *
	 * @param cause entity that caused the death
	 */
	@Override
	protected void onDeath(Entity cause) {
		velX = 0;
		setState(EntityState.DEAD);
	}

	/**
	 * Adjusts the BME's health by the input quantity, up to the maximum value
	 */
	@Override
	public void addHealth(int health) {
		if (currentState == EntityState.KNOCK_BACK || knockBackTimer > 0) {
			return;
		}
		if (health < 0 && shielded) {
			// Don't alter current health
		} else {
			currentHealth += health;
		}
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}
/*
TODO weapon change
 */

}
