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
 * The way to get into a room from the main world. You need to press the ENTER_ROOM key to activate the totem.
 */
public class Totem extends Entity {
	public Totem() {
		initEntity(SpriteList.TOTEM, false, e -> true);
	}

	@Override
	protected void tick(long ms) {
		boolean playerNear = false;
		for (Entity entity : this.getNearbyEntities(2)) {
			if (entity instanceof Player) {
				playerNear = true;
			}
		}
		if (playerNear) {
			Toaster.ejectAllToast();
			Toaster.toast("Press " + ControlsKeyMap.getStyledKeyCode(GameAction.ENTER_ROOM) + " to activate totem");
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
