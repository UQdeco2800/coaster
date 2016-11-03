package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Doors are the way to go from a room back to the parent world. They can be locked, if they are locked you must use
 * a key to unlock it.
 */
public class RoomDoor extends Entity {
	// whether or not this door is locked
	private boolean isLocked;

	public RoomDoor(boolean isLocked) {
		setLocked(isLocked);
	}

	/**
	 * @return whether or not this door is locked
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * Lock or unlock the door
	 * @param locked true if the door is to be locked, false for unlocked
	 */
	public void setLocked(boolean locked) {
		isLocked = locked;
		initEntity(isLocked ? SpriteList.LOCKED_DOOR : SpriteList.UNLOCKED_DOOR, false, e -> false);
	}

	@Override
	protected void tick(long ms) {
		boolean playerNear = false;
		List<Entity> nearbyEntities = this.getNearbyEntities(2);
		for (Entity entity : nearbyEntities) {
			if (entity instanceof Player) {
				playerNear = true;
				break;
			}
		}
		if (playerNear) {
			Toaster.ejectAllToast();
			Toaster.toast(!isLocked() ? "Press " + ControlsKeyMap.getStyledKeyCode(GameAction.ENTER_ROOM) +
					" to enter door." : "You need a key to unlock this door!");
		}
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		// Nothing needs to be done
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// Nothing needs to be done
	}

	@Override
	protected void onDeath(Entity cause) {
		// Nothing needs to be done
	}
}
