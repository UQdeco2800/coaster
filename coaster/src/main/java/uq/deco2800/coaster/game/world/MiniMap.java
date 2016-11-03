package uq.deco2800.coaster.game.world;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * MiniMap class that shows the player where on the map they have visited.
 */
public class MiniMap {
	public static final int MAP_PADDING = 10; // padding around mini map
	private static boolean visible = false; // off by default
	
	/**
	 * Explicit private constructor to hide implicit one
	 */
	private MiniMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * If the Minimap is visible, set it invisible. If it's invisible, set it visible.
	 */
	public static void toggleVisibility() {
		visible = !visible;
	}

	/**
	 * Set MiniMap visibility to a given boolean value
	 */
	public static void setVisibility(boolean visibility) {
		visible = visibility;
	}

	/**
	 * @return if MiniMap is visible or not
	 */
	public static boolean getVisibility() {
		return visible;
	}
	
	/**
	 * Takes a list of entities (usually all the entities in a world) and returns a filtered list containing
	 * only entities that will be visible on the MiniMap (Players, BaseNPC's (includes mobs), and ItemEntities)
	 */
	public static List<Entity> getMapEntities(List<Entity> allEntities) {
		List<Entity> entities = new ArrayList<>();
		for (Entity entity : allEntities) {
			if(entity instanceof BaseNPC || 
					entity instanceof ItemEntity || 
					entity instanceof Player) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	/**
	 * Sets the chunk surrounding the player to visited
	 */
	public static void visitChunk() {
		Player player = World.getInstance().getFirstPlayer();
		
		int top = 0;
		int left = (int) Math.floor(player.getX()) - (Chunk.CHUNK_WIDTH / 2);
		int right = (int) Math.floor(player.getX()) + (Chunk.CHUNK_WIDTH / 2);
		int bottom = Chunk.CHUNK_HEIGHT;

		World world = World.getInstance();
		WorldTiles tiles = world.getTiles();
		
		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				if (!tiles.test(x,y)) {
					continue;
				}
				tiles.setVisited(x, y, true);
			}
		}
	}
	
	/**
	 * Set tiles in a radius around the given player to visited
	 */
	public static void updateVisited(Player player) {
		if (player == null) {
			return;
		}

		int top = (int) Math.floor(player.getY() - Chunk.CHUNK_HEIGHT / 2);
		int left = (int) Math.floor(player.getX() - Chunk.CHUNK_WIDTH / 2);
		int right = (int) Math.floor(player.getX() + Chunk.CHUNK_WIDTH / 2);
		int bottom = (int) Math.floor(player.getY() + Chunk.CHUNK_HEIGHT / 2);
		
		WorldTiles tiles = World.getInstance().getTiles();
		
		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				if (!tiles.test(x,y)) {
					continue;
				}

				// Sets tile to visited if within player's radius of view
				if (isPointWithinRadius(x, y, (int)player.getX(), (int)player.getY(), player.getViewDistance())) {
					tiles.setVisited(x, y, true);
				}
			}
		}
	}

	/**
	 * Tests and returns if a given point is within a circle of given radius
	 */
	private static boolean isPointWithinRadius(int pointX, int pointY, int centreX, int centreY, int radius) {
		return pow(abs(pow((double) pointX - centreX, 2)) + abs(pow((double) pointY - centreY, 2)), 2) < pow(radius, 2);
	}
}
