package uq.deco2800.coaster.game.entities.npcs.mounts;

import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;

/**
 * A collection of methods relevant to the mount and player interaction.
 */
public class PlayerMountActions {

	/**
	 * Toggle the player on the nearest mount if not mounted. Otherwise it will
	 * toggle the player off the mount.
	 * 
	 * @param player
	 *            the player to toggle mount state
	 */
	public static void toggleMount(Player player) {
		if (!player.getOnMountStatus()) {
			List<Entity> mountsNearby = player.getNearbyEntities(5, Mount.class);
			if (!(mountsNearby.isEmpty())) {
				Mount mount = (Mount) player.getClosest(mountsNearby);
				player.setMount(mount);
				player.saveRider(mount, player);
				player.setOnMountStatus(true);
			}
		} else {
			player.getMount().removeRider(player);
			player.setMount(null);
			player.setOnMountStatus(false);
			player.setVelocity(0, -15);
		}
	}

	/**
	 * Positions the player according to the saddle parameters of the mount.
	 * 
	 * @param player
	 *            the player to position
	 */
	public static void positionPlayer(Player player) {
		Mount mount = player.getMount();
		if (mount.getFacing() > 0) {
			player.setX(mount.getX() + mount.getSaddleX());
		} else {
			player.setX(mount.getX() + mount.getWidth() - 1 - mount.getSaddleX());
		}
		player.setY(mount.getY() + mount.getSaddleY());
	}
}
