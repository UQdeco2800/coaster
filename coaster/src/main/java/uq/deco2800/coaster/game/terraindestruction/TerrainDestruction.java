package uq.deco2800.coaster.game.terraindestruction;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.mounts.Mount;
import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.tiles.Tile;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.tiles.Tiles;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;

/**
 * TerrainDestruction provides a number of method to remove and add blocks to
 * the terrain.
 */
public class TerrainDestruction {

	private static Logger logger = LoggerFactory.getLogger(TerrainDestruction.class);

	/**
	 * Reduces the hit points of the block located at the coordinates specified
	 * by the amount specified
	 *
	 * @param blockXPosition The x coordinate of the block to damage
	 * @param blockYPosition The y coordinate of the block to damage
	 * @param damage The hit points to subtract from the block
	 * @param checkDestructible check the block is destructible or not
	 */
	public static void damageBlock(int blockXPosition, int blockYPosition, int damage, boolean checkDestructible,
			boolean noItems) {
		World world = World.getInstance();
		if (!world.getTerrainDestructionMode()) {
			return;
		}
		int blockDamage = damage / 149 + 1;
		try {
			int blockX = blockXPosition;
			int blockY = blockYPosition;
			WorldTiles tiles = world.getTiles();
			Tile tile = tiles.get(blockX, blockY);

			if (!checkDestructible || tile.getTileType().isDestructible()) {
				tile.decreaseHitPoints(blockDamage);
				TileInfo damagedTile = tile.getTileType().getDamagedTile();
				if (damagedTile != null) {
					world.getTiles().set(blockX, blockY, damagedTile);
				}

				if (tile.isDestroyed()) {
					world.getTiles().set(blockX, blockY, tile.getTileType().getBackgroundTile());
					Random random = new Random();

					if (!noItems && random.nextInt(100) > 90) {
						ItemDrop.randomDrop(blockX, blockY);
					}

					world.setDestructionShadowUpdate(true);

					for (int x = -8; x < 9; x++) {
						for (int y = -8; y < 9; y++) {
							if (!tiles.test(blockX + x, blockY + y)) {
								continue;
							}
							updateDestructionFog(blockX + x, blockY + y, x, y, tiles);
						}
					}
					world.checkDecorations();
				}
			}
		} catch (IndexOutOfBoundsException exception) {
			// attempting to destroy blocks outside of valid boundaries is an
			// acceptable consequence of radial block destruction from
			// explosive weapons
			logger.error("Search for block to destroy exceeded valid boundaries", exception);
		}
	}

	/**
	 * Reduces the hit points of the block located at the coordinates specified
	 * by the amount specified
	 *
	 * @param blockXPosition The x coordinate of the block to damage
	 * @param blockYPosition The y coordinate of the block to damage
	 * @param damage The hit points to subtract from the block
	 */
	public static void damageBlock(int blockXPosition, int blockYPosition, int damage) {
		damageBlock(blockXPosition, blockYPosition, damage, true, false);
	}

	/**
	 * Updates the destruction fog of the specified tile.
	 *
	 * @param xPosition the x coordinate of the tile
	 * @param yPosition the y coordinate of the tile
	 * @param distX the horizontal distance from the tile
	 * @param distY the vertical distance from the tile
	 * @param tiles the world tiles
	 */
	private static void updateDestructionFog(int xPosition, int yPosition, int distX, int distY, WorldTiles tiles) {
		// get new light value
		int fogRevealLevel = Math.max(Math.abs(distX), Math.abs(distY)) * 13;
		if (tiles.get(xPosition, yPosition).getFog() > fogRevealLevel) {
			tiles.get(xPosition, yPosition).setBlockFog(fogRevealLevel);
		}
		tiles.get(xPosition, yPosition).setFogCheck(0);
	}

	/**
	 * Damages all blocks in a circle.
	 *
	 * @param xPosition the x coordinate of the top left corner of the square
	 *            that encloses the circle
	 * @param yPosition the y coordinate of the top left corner of the square
	 *            that encloses the circle
	 * @param radius the explosive circle radius
	 * @param damage the amount of damage to apply to blocks
	 */
	public static void damageCircle(int xPosition, int yPosition, int radius, int damage) {
		for (int x = xPosition; x < xPosition + 2 * radius; x++) {
			for (int y = yPosition; y < yPosition + 2 * radius; y++) {
				double radial = Math.pow(Math.pow(((double) (x - radius)) - xPosition, 2)
						+ Math.pow(((double) (y - radius)) - yPosition, 2), 0.5);
				if (radial <= radius - .5) {
					TerrainDestruction.damageBlock(x, y, damage);
				} else if (radial <= radius) {
					TerrainDestruction.damageBlock(x, y, 1);
				}
			}
		}
	}

	/**
	 * Damages a rectangle of blocks, of the same height as the player.
	 *
	 * @param xPosition the x coordinate of the top left tile of the rectangle
	 *            to be damaged
	 * @param yPosition the y coordinate of the top left tile of the rectangle
	 *            to be damaged
	 * @param length the length of the rectangle
	 * @param damage the damage to apply to the blocks
	 * @return returns true if the blocks being damaged are indestructible
	 */
	public static boolean damageRectangle(int xPosition, int yPosition, int length, int damage) {
		boolean retract = false;
		for (int x = xPosition; x < xPosition + length; x++) {
			for (int y = yPosition; y < yPosition + 2; y++) {
				Tile tile = World.getInstance().getTiles().get(x, y);
				if (!tile.getTileType().isDestructible()) {
					retract = true;
				}
				TerrainDestruction.damageBlock(x, y, damage);
			}
		}
		return retract;
	}

	/**
	 * Damages a column of blocks.
	 *
	 * @param xPosition the x coordinate of the bottom tile of the column to be
	 *            damaged
	 * @param yPosition the y coordinate of the bottom tile of the column to be
	 *            damaged
	 * @param damage the damage to apply to the blocks
	 * @param height the height of the column
	 */
	public static void destroyColumn(int xPosition, int yPosition, int damage, int height) {
		for (int y = yPosition; y > yPosition - height; y--) {
			TerrainDestruction.damageBlock(xPosition, y, damage);
		}
	}

	/**
	 * Places a block adjacent to the face of the block that was shot by the
	 * player.
	 *
	 * @param owner the entity who shot the block
	 * @param blockedByTileX the x coordinate of the tile that blocked the shot
	 * @param blockedByTileY the y coordinate of the tile that blocked the shot
	 * @param side the side of the block shot
	 */
	public static void placeBlock(Entity owner, int blockedByTileX, int blockedByTileY, Side side) {
		int newTileX = blockedByTileX;
		int newTileY = blockedByTileY;

		TileInfo tileType = TileInfo.get(Tiles.DIRT);
		final int safeheight = 2; // don't build too high or game breaks
		if (newTileY < safeheight) {
			return;
		}
		switch (side) {
			case TOP:
				newTileY++;
				break;
			case BOTTOM:
				newTileY--;
				break;
			case LEFT:
				newTileX--;
				break;
			case RIGHT:
				newTileX++;
				break;
			default:
				break;
		}
		/* stops the blocks from being produced on the player */
		float minPlayerX = owner.getX() - 1;
		float maxPlayerX = owner.getX() + owner.getWidth();
		float minPlayerY = owner.getY() - 1;
		float maxPlayerY = owner.getY() + owner.getHeight();
		Player player = (Player) owner;
		/* stops the blocks from being produced on the player's mount */
		if (player.getOnMountStatus()) {
			Mount mount = player.getMount();
			minPlayerX = mount.getX();
			maxPlayerX = mount.getX() + mount.getWidth();
			minPlayerY = mount.getY() - 1;
			maxPlayerY = mount.getY() + mount.getHeight();
			if (mount.getSaddleY() < 0) {
				minPlayerX += mount.getSaddleY();
			}
		}
		World world = World.getInstance();
		if (newTileX < minPlayerX || newTileX > maxPlayerX) {
			world.getTiles().set(newTileX, newTileY, tileType);
		} else if (newTileY < minPlayerY || newTileY > maxPlayerY) {
			world.getTiles().set(newTileX, newTileY, tileType);
		}
	}

	/**
	 * Places a block in front of an enemy, towards the player.
	 *
	 * @param owner the entity who shot the block
	 * @param enemyX the x coordinate of the enemy that blocked the shot
	 * @param enemyY the y coordinate of the enemy that blocked the shot
	 * @param xVelocity the x velocity of the projectile shot at the enemy
	 * @param yVelocity the y velocity of the projectile shot at the enemy
	 */
	public static void placeEnemyBlock(Entity owner, int enemyX, int enemyY, float xVelocity, float yVelocity) {
		int newTileX = enemyX;
		int newTileY = enemyY;
		double hypotenuse = Math.pow(Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2), 0.5);
		newTileX -= (int) (xVelocity * 2 / hypotenuse);
		newTileY -= (int) (yVelocity * 2 / hypotenuse);
		if (newTileY < 1) {
			return;
		}
		TileInfo tileType = TileInfo.get(Tiles.DIRT);
		World world = World.getInstance();
		if (newTileX < (owner.getX() - 1) || newTileX > (owner.getX() + owner.getWidth() + 1)) {
			world.getTiles().set(newTileX, newTileY, tileType);
		} else if (newTileY < (owner.getY() - 3) || newTileY > (owner.getY() + owner.getHeight())) {
			world.getTiles().set(newTileX, newTileY, tileType);
		}
	}
}
