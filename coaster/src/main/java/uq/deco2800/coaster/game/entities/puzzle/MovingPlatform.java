package uq.deco2800.coaster.game.entities.puzzle;

import java.util.List;

import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * A platform that follows a path that a user can jump on. Is used for puzzles
 */
public class MovingPlatform extends TerrainEntity {
	private static float margin = 0.1f;
	
	// startX, endX, startY, and endY define the path that the platform follows
	private float startX;
	private float endX;
	private float startY;
	private float endY;
	// time in ms of travelling from one end to another
	private long time;
	// Which direction the platform is heading in
	private boolean headingToEnd;
	// Whether or not a path has been set yet
	private boolean hasPath;
	// The most recent change in x or y
	private float xDelta;
	private float yDelta;

	public MovingPlatform(boolean horizontal) {
		initEntity(horizontal ? SpriteList.PLATFORM_HORZ : SpriteList.PLATFORM_VERT, true, e -> true);
		enableGravity = false;
		headingToEnd = true;
		hasPath = false;
	}

	/**
	 * Will set the path which this platform follows
	 * 
	 * @param startX The starting x position of the path
	 * @param startY The starting y position of the path
	 * @param endX the ending x position of the path
	 * @param endY the ending y position of the path
	 * @param time the time in ms that it takes for the platform to go from start to end
	 */
	public void setPath(float startX, float endX, float startY, float endY, int time) {
		hasPath = true;
		headingToEnd = true;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.time = time;
		this.setPosition(startX, startY);
	}
	
	/** 
	 * Simplified physics such that this tracks the specified path
	 */
	@Override
	public void updatePhysics(long ms) {
		if (!hasPath) {
			return;
		}
		// Determine where we are moving to
		float destinationX = headingToEnd ? endX : startX;
		float destinationY = headingToEnd ? endY : startY;
		// Determine angle to move at and how far is left
		double angle = Math.atan2(destinationY - getY(), destinationX - getX());
		double distanceLeft = Math.hypot(destinationY - getY(), destinationX - getX());
		// Determine entire path length and use that to figure out how far we move this tick
		double pathLength = Math.hypot(endX - startX, + endY - startY);
		double movementLength = ((double)ms / time) * pathLength;
		// If we have reached the end, turn around
		if (distanceLeft <= movementLength) {
			headingToEnd = !headingToEnd;
		}
		// Determine the change in x and y
		xDelta = (float)(movementLength * Math.cos(angle));
		yDelta = (float)(movementLength * Math.sin(angle));
		// Move every entity on the block as well as this platform accordingly
		this.setPosition(this.getX() + xDelta, this.getY() + yDelta);
	}


	@Override
	public void entityLoop(long ms) {
		tick(ms);
	}

	/** 
	 * Moves entities standing on this platform, so that they stay on the platform as we go
	 */
	@Override
	public void applyAffect(Entity entity) {
		entity.setPosition(entity.getX() + xDelta, entity.getY() + yDelta);
	}
	
	/**
	 * We only collide with entities if they are on top of us and moving downwards
	 */
	@Override
	public boolean collidesWith(Entity e, Side side) {
		return (side == Side.BOTTOM) &&
				e.getBounds().bottom() < bounds.top() + margin;
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		// do nothing
	}

	
}
