package uq.deco2800.coaster.game.entities.npcs.mounts;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This entity is a flag that saves the players progress and can be used to load
 * from on death.
 */
public class SaveFlag extends BasicMovingEntity {
	private static Logger logger = LoggerFactory.getLogger(SaveFlag.class);
	/* The interaction boundary distance */
	protected static final int range = 2;
	/* Whether the player is out of range */
	protected boolean outOfRange = false;
	/* Whether the player has just changed from out of range to in range */
	protected boolean inRangeFromOutRange = false;
	/* Whether the player is in range */
	protected boolean inRange = true;
	/* Whether the */
	protected boolean skip = false;
	/* Whether the flag is down */
	protected boolean flagDown = true;
	/* Whether the flag is facing left */
	protected boolean flagLeft = true;

	public SaveFlag() {
		super();
		setBlocksOtherEntities(false);
		disableHealthBar();
		bounds = new AABB(posX, posY, 1f, 3f);

		sprites.put(EntityState.DEFAULT, new Sprite(SpriteList.FLAG_DOWN_LEFT));
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.FLAG_UP_LEFT));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.FLAG_UP_RIGHT));
		this.setState(EntityState.DEFAULT);
	}

	/**
	 * The tick method is called many times per second automatically
	 * 
	 * @param ms
	 *            the time since last cycle
	 */
	@Override
	protected void tick(long ms) {
		// 3 states
		// player out of range
		// player in range but out of range last time
		// player in range but in range last time
		skip = false;

		for (Entity entity : this.getNearbyEntities(this.range)) {
			if (entity instanceof Player) {

				// In range now
				if (outOfRange) {
					// Player was last out of range and is now in range
					inRangeFromOutRange = true;
					toggleFlag();
					if (((Player) entity).isCheckPointsEnabled()) {
						Window.getEngine().save("tmp/flagSave.json");
					}
					((Player) entity).setCheckPointReached(true);
				} else {
					inRangeFromOutRange = false;
				}
				inRange = true;
				outOfRange = false;
				skip = true;
			}
		}
		if (!skip) {
			outOfRange = true;
			inRangeFromOutRange = false;
			inRange = false;
		}
	}

	/**
	 * Toggles the state of the flag from (down, left) to (up, left) to (up, right)
	 * and then cycles between (up, left) and (up, right)
	 */
	public void toggleFlag() {
		logger.info("Checkpoint flag reached");
		if(!((Player) World.getInstance().getFirstPlayer()).isCheckPointsEnabled()){
			Toaster.ejectAllToast();
			Toaster.toast("To enable loading from checkpoints on death, press the \""
					+ ControlsKeyMap.getStyledKeyCode(GameAction.ENABLE_CHECKPOINTS)
					+ "\" key.");
		}
		if (flagDown) {
			flagDown = false;
			flagLeft = true;
			this.setState(EntityState.JUMPING);
		} else if (flagLeft) {
			flagLeft = false;
			this.setState(EntityState.STANDING);
		} else {
			flagLeft = true;
			this.setState(EntityState.JUMPING);
		}
	}

	/**
	 * Updates the state of the entity.
	 */
	@Override
	protected void stateUpdate(long ms) {
		/* Do nothing */
	}

	/**
	 * Handles collisions of entities
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		/* Do nothing */
	}

	/**
	 * Handles collision with terrain.
	 */
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		/* Do nothing */
	}

	/**
	 * Actions to be performed on mount's death.
	 */
	@Override
	protected void onDeath(Entity cause) {
		/* Do nothing */
	}
}
