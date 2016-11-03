package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.core.input.ControlsKeyMap;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.world.PuzzleRoom;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * A flippable lever which has two states, up or down. Will later be used to trigger an event in a puzzle.
 * TODO: make it do something like taht.
 */
public class Lever extends Entity {
	// Keeps track of what state the lever is in
	private boolean isUp;

	public Lever(boolean up) {
		setUp(up);
		enableGravity = false;
	}

	/**
	 * @return Whether or not the lever is up or down
	 */
	public boolean isUp() {
		return isUp;
	}

	/**
	 * Will change the state of the lever.
	 * @param up Whether or not to change the lever to up or down
	 */
	private void setUp(boolean up) {
		isUp = up;
		initEntity(up ? SpriteList.LEVER_UP : SpriteList.LEVER_DOWN, false, e -> false);
		if (!up) {
			((PuzzleRoom) World.getInstance()).removeGate(1);
		}
	}

	/**
	 * Flips the lever so it is the opposite of it's current state
	 */
	public void flip() {
		setUp(!isUp);
	}

	@Override
	protected void tick(long ms) {
		for (Entity entity : this.getNearbyEntities(1)) {
			if (entity instanceof Player) {
				Toaster.ejectAllToast();
				Toaster.toast("Press " + ControlsKeyMap.getStyledKeyCode(GameAction.ENTER_ROOM) + " to use the lever.");
				break;
			}
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
