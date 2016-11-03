package uq.deco2800.coaster.game.entities;

import javafx.scene.canvas.GraphicsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.game.entities.npcs.AttackableNPC;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.puzzle.TerrainEntity;
import uq.deco2800.coaster.game.entities.weapons.Projectile;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.tiles.Tile;
import uq.deco2800.coaster.game.tiles.TileInfo;
import uq.deco2800.coaster.game.world.Chunk;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Viewport;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;
import uq.deco2800.coaster.graphics.sprites.SpriteRelation;

import java.util.*;
import java.util.function.Predicate;

public abstract class Entity {
	private static final Logger logger = LoggerFactory.getLogger(Entity.class);

	public static final float GRAVITY = 50f;
	public static final float terminalVelocity = 50f; // Max falling speed
	public static final float UNDER_WATER_GRAVITY_MULTIPLIER = 0.7f;

	protected World world;
	protected Sprite sprite = null;

	// For now, coordinate system is top-left just so we don't have to flip it
	// when rendering. If this turns out

	// Map of extra sprites that are not bound to the entity's hitbox. They are
	// described by SpriteRelations, which
	// contain the sprite and how they are connected with the owner entity.
	protected Map<BodyPart, SpriteRelation> additionalSprites = new HashMap<>();
	// When true, facing = last movement direction. When false, facing is not
	// updated in this fashion.
	protected boolean strafeActive = false;

	protected int renderFacing = 1;

	// For now, coordinate system is top-left just so we don't have to flip it
	// when rendering. If this turns out
	// horrible then I'll consider changing it to bottom-left.
	protected AABB bounds;
	protected List<AABB> hitboxes = new ArrayList<>();

	protected float prevX; // For projectile collision detection
	protected float prevY; // For projectile collision detection
	protected float posX;
	protected float posY;
	protected float prevVelX;
	protected float prevVelY;
	protected float velX;
	protected float velY;
	// Entity states regarding physics to avoid recalculation
	protected boolean onGround;
	protected boolean isUnderLiquid;
	protected int onWall = 0; // -1 for wall on left, 1 for right, 0 for not on wall
	protected boolean onCeiling;
	// Some properties all entities should have
	protected boolean enableGravity = true; // Affected by gravity?
	protected boolean hurtByProjectiles = true; // Collides with projectiles?
	private boolean blocksOtherEntities = true;
	protected boolean renderFlag = true; // Does the sprite get rendered? Other effects still rendered
	protected int facing = 1; // -1 for left, 1 for right.\
	protected Predicate<Entity> collisionFilter;
	Viewport viewport;
	boolean firstTick = true;

	protected long latestTick = 5000;


	private List<AABB> checkedTiles = new LinkedList<AABB>();

	public Entity() {
		this.world = World.getInstance();
		setCollisionFilter(e -> true);
	}

	/**
	 * Will intialise some of the properties of an entity
	 *
	 * @param sprite              the type of sprite to use
	 * @param blocksOtherEntities whether or not this entity blocks other entities
	 * @param collisionFilter     a predicate which filters collisions
	 */
	protected void initEntity(SpriteList sprite, boolean blocksOtherEntities, Predicate<Entity> collisionFilter) {
		setSprite(new Sprite(sprite));
		setBlocksOtherEntities(blocksOtherEntities);
		bounds = new AABB(posX, posY, this.sprite.getWidth() / 32, this.sprite.getHeight() / 32);
		setCollisionFilter(collisionFilter);
	}

	/**
	 * Sets this entity's sprite to the specified sprite instance
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * Getter for this entity's sprite
	 *
	 * @return the current Sprite instance
	 */
	public Sprite getSprite() {
		return this.sprite;
	}

	/**
	 * Sets the additional sprites, which are rendered over the top of this entity and are not hitbox-aligned.
	 *
	 * @param additionalSprites a map of <SpritePart, SpriteRelation>
	 */
	public void setAdditionalSprites(Map<BodyPart, SpriteRelation> additionalSprites) {
		this.additionalSprites = additionalSprites;
	}

	/**
	 * Returns the ID of the current Sprite
	 *
	 * @return the ID of the current Sprite
	 */
	public SpriteList getSpriteID() {
		return sprite.getSpriteId();
	}

	/**
	 * Getter for the entity's bounds This is an axis-aligned bounding box (AABB), which is essentially the rectangle
	 * that defines the entity's size and location This can also be thought of as a hitbox.
	 *
	 * @return the entity's hitbox
	 */
	public AABB getBounds() {
		return bounds;
	}

	/**
	 * Sets the size of the AABB (and therefore the entity) <p> Warning: This should NOT be used for collision-sensitive
	 * entities (e.g Player, NPCs) as it can cause all kinds of funky physics issues. <p> For those entities,
	 * changeBounds is the preferred method.
	 *
	 * @param width  new width in tiles
	 * @param height new height in tiles
	 */
	public void setSize(float width, float height) {
		this.bounds.setAABB(posX, posY, width, height);
	}

	/**
	 * Sets the direction that the entity is facing in. Note that this is distinct to renderFacing due to the difference
	 * between movement and rendering
	 *
	 * @require facing == 1, to make the entity face right OR facing == -1 to make the entity face left
	 */
	public void setFacing(int facing) {
		this.facing = facing;
	}

	/**
	 * Returns the direction the entity is facing (in terms of movement)
	 *
	 * @return -1 if the entity is facing to the left, 1 if to the right
	 */
	public int getFacing() {
		return facing;
	}

	/**
	 * Returns the direction the entity is rendered as facing
	 *
	 * @return -1 if the entity is rendered to the left, 1 if to the right
	 */
	public int getRenderFacing() {
		return renderFacing;
	}

	/**
	 * Sets the direction that the entity is rendered as facing in. Note that this is distinct to facing due to the
	 * difference between movement and rendering
	 *
	 * @require renderFacing == 1, to make the entity face right OR renderFacing == -1 to make the entity face left
	 */
	public void setRenderFacing(int renderFacing) {
		this.renderFacing = renderFacing;
	}


	/**
	 * Creates and returns a child hitbox offset from this entity's bounds
	 *
	 * @param bodyPart the bodyPart assigned to this
	 * @param sizeX    the hitbox's width in tiles
	 * @param sizeY    hitbox's height
	 * @param offsetX  offset from the entity's left edge to this hitbox's left edge when facing right
	 * @param offsetY  offset from the entity's top edge to this hitbox's top edge
	 * @return a child hitbox described by the input information
	 */

	public AABB defineHitbox(BodyPart bodyPart, float sizeX, float sizeY, float offsetX, float offsetY) {
		AABB hitbox = new AABB(posX, posY, sizeX, sizeY, offsetX, offsetY);
		hitbox.makeChild(true);
		hitbox.setBodyPart(bodyPart);
		return hitbox;
	}

	/**
	 * Removes all hitboxes described by the input body part from the list of hitboxes
	 *
	 * @param armourPart
	 */
	public void removeHitbox(BodyPart armourPart) {
		for (int i = 0; i < hitboxes.size(); i++) {
			if (hitboxes.get(i).getBodyPart() == armourPart) {
				hitboxes.remove(i);
			}
		}
	}


	/**
	 * Sets the entity's position (top-left corner) to the specified point <br>
	 * Note that movement commands are queued so we can process collision a bit easier
	 *
	 * @param x the new x coordinate in tiles
	 * @param y the new y coordinate in tiles
	 */
	public void setPosition(float x, float y) {
		prevX = posX;
		prevY = posY;
		posX = x;
		posY = y;
		bounds.setX(posX);
		bounds.setY(posY);

		updateHitboxes();

		World tempWorld = World.getInstance();
		if (this instanceof Player) {
			int offset = -getNearestChunkX();
			tempWorld.resetTiles();
			tempWorld.getTiles().setOffset(offset);
			tempWorld.loadAroundPlayer((Player) this);
		}
	}

	/**
	 * Sets the entity's velocity to the specified vector
	 *
	 * @param x the new x velocity in tiles/second
	 * @param y the new y velocity in tiles/second
	 */
	public void setVelocity(float x, float y) {
		velX = x;
		velY = y;
	}

	/**
	 * Returns the starting x coordinate of the chunk the player is standing on.
	 */
	public int getNearestChunkX() {
		return Chunk.CHUNK_WIDTH * Math.round(posX / Chunk.CHUNK_WIDTH);
	}

	/**
	 * Gets the x-coordinate of the entity's top-left corner
	 *
	 * @return x-coordinate of the entity's top-left corner in tiles
	 */
	public float getX() {
		return posX;
	}

	/**
	 * Gets the y-coordinate of the entity's top-left corner
	 *
	 * @return y-coordinate of the entity's top-left corner in tiles
	 */
	public float getY() {
		return posY;
	}

	/**
	 * sets the x-coordinate of the entity's top-left corner
	 */
	public void setX(float xPos) {
		this.posX = xPos;
		bounds.setX(xPos);
		updateHitboxes();
	}

	/**
	 * sets the y-coordinate of the entity's top-left corner
	 */
	public void setY(float yPos) {
		this.posY = yPos;
		bounds.setY(yPos);
		updateHitboxes();
	}

	/**
	 * Gets the x velocity
	 *
	 * @return x-velocity in tiles/second
	 */
	public float getVelX() {
		return velX;
	}

	/**
	 * Gets the y velocity
	 *
	 * @return y-velocity in tiles/second
	 */
	public float getVelY() {
		return velY;
	}

	/**
	 * Get latest change in time in ms
	 */
	public long getTick() {
		return latestTick;
	}

	/**
	 * Gets the actual observed x velocity
	 *
	 * @return x-velocity in tiles/millisecond
	 */

	public float getMeasuredVelX() {
		return (getX() - getPrevX()) / getTick();

	}

	/**
	 * Gets the actual observed y velocity
	 *
	 * @return y-velocity in tiles/millisecond
	 */

	public float getMeasuredVelY() {
		return (getY() - getPrevY()) / getTick();

	}

	/**
	 * returns the x-coordinate of the player at the previous tick
	 *
	 * @return previous x-coordinate in tiles
	 */
	public float getPrevX() {
		return prevX;
	}

	/**
	 * returns the y-coordinate of the player at the previous tick
	 *
	 * @return previous y-coordinate in tiles
	 */
	public float getPrevY() {
		return prevY;
	}

	/**
	 * returns the width of this entity's hitbox
	 *
	 * @return the hitbox width in tiles
	 */
	public float getWidth() {
		return bounds.getWidth();
	}

	/**
	 * returns the height of this entity's hitbox
	 *
	 * @return the hitbox height in tiles
	 */
	public float getHeight() {
		return bounds.getHeight();
	}

	/**
	 * returns whether or not this entity is hurt by projectiles
	 *
	 * @return true if projectiles hurt this entity, otherwise false.
	 */
	public boolean isHurtByProjectiles() {
		return hurtByProjectiles;
	}

	/**
	 * returns whether or not this entity blocks other entities i.e. returns true this entity is a physical object in
	 * game, and false if it is permeable
	 *
	 * @return true if this entity blocks other entities, otherwise false.
	 */
	public boolean blocksOtherEntities() {
		return doesItBlockOtherEntities();
	}

	/**
	 * returns if gravity causes this entity to accelerate downwards when not onGround
	 *
	 * @return true if gravity affects this entity, otherwise false
	 */
	public boolean isEnableGravity() {
		return enableGravity;
	}

	/**
	 * toggles gravity
	 */
	public void toggleGravity() {
		enableGravity = !enableGravity;
	}

	/**
	 * returns if the entity is currently on the ground i.e. if the entity is unable to move downwards due to a
	 * collision Standing on top of terrain or other entities both cause this to be true
	 *
	 * @return true if the entity is on the ground
	 */
	public boolean isOnGround() {
		return onGround;
	}


	/**
	 * Returns the onWall status
	 * i.e. -1 if the entity is colliding with a wall on the left, 1 for a wall on the right
	 * returns 0 for no horizontal collision
	 *
	 * @return
	 */
	public int getOnWall() {
		return onWall;
	}

	/**
	 * returns if the entity is currently in the Water
	 *
	 * @return true if the entity is in the water
	 */
	public boolean isUnderLiquid() {
		return isUnderLiquid;
	}

	/**
	 * Sets the collision filter to the input predicate
	 *
	 * @param predicate a boolean used to determine whether or not a collision will occur with the input entity
	 */
	public void setCollisionFilter(Predicate<Entity> predicate) {
		collisionFilter = predicate;
	}

	/**
	 * Updates the entity's y velocity to reflect gravity. <br>
	 * Note that an ifEnableGravity check should be made first. <p>
	 * <p>
	 * Downwards velocity due to gravity is capped at terminalVelocity. <br>
	 * The rate of acceleration is slowed down when underwater
	 *
	 * @param ms time since the last tick in milliseconds.
	 */
	protected void updateVelYFromGravity(float ms) {
		float seconds = ms / (float) 1000;

		velY += GRAVITY * seconds * (isUnderLiquid ? UNDER_WATER_GRAVITY_MULTIPLIER : 1f); // Top left coordinate system, so gravity is positive.
		if (velY > terminalVelocity) {
			velY = terminalVelocity;

		}
	}

	/**
	 * Get a list of nearby entities of a given property, excluding this entity
	 *
	 * @param radius    the radius of the search
	 * @param classType the type of class to select
	 * @return A list of entities
	 */

	public List<Entity> getNearbyEntities(int radius, Class<?> classType) {
		ArrayList<Entity> nearbyEntities = new ArrayList<>();
		for (Entity entity : world.getAllEntities()) {

			float distX = bounds.posX() - entity.getBounds().posX();
			float distY = bounds.posY() - entity.getBounds().posY();
			double distSquared = distX * distX + distY * distY;
			if ((distSquared < (radius * radius)) && entity != this && classType.isInstance(entity)) {
				nearbyEntities.add(entity);
			}
		}
		return nearbyEntities;
	}

	/**
	 * Get a list of nearby entities, excluding this entity
	 *
	 * @param radius the radius of the search
	 * @return A list of entities
	 */
	public List<Entity> getNearbyEntities(int radius) {
		return getNearbyEntities(radius, Entity.class);
	}

	/**
	 * Get a list of nearby enemy NPCs
	 *
	 * @param radius the radius of the search
	 * @return A list of BaseNPCs
	 */
	public List<BaseNPC> getNearbyAttackableNPCs(int radius) {
		ArrayList<BaseNPC> nearbyEnemyNPCs = new ArrayList<>();
		for (Entity entity : getNearbyEntities(radius, AttackableNPC.class)) {
			nearbyEnemyNPCs.add((BaseNPC) entity);
		}
		return nearbyEnemyNPCs;
	}

	/**
	 * Returns the straight-line distance between this entity's top-left and that of the target
	 *
	 * @param e an entity to which the distance is calculated
	 * @return the straight line distance in tiles
	 */
	public float distanceFrom(Entity e) {
		return Math.abs((this.posX + this.getWidth() / 2) - (e.posX + e.getWidth() / 2));
	}

	/**
	 * Returns the closest entity out of the input list.
	 *
	 * @param entities a list of entities
	 * @return the closest of these entities
	 */
	public Entity getClosest(List<Entity> entities) {
		float dist;
		float closestDistance = Float.MAX_VALUE;
		Entity closest = null;
		for (Entity entity : entities) {
			// Check if this player is closer
			dist = this.distanceFrom(entity);
			if (dist < closestDistance) {
				closest = entity;
				closestDistance = dist;
			}
		}
		return closest;
	}

	/*
	 * ------------------------------------------------------------------------
	 */
	/*
	 * ------------------------------------------------------------------------
	 */
	/*
	 * Below here is lots of physics, state, framework and rendering code
	 */
	/*
	 * ------------------------------------------------------------------------
	 */
	/*
	 * ------------------------------------------------------------------------
	 */


	/**
	 * Updates the position of all hitboxes relative to bounds (the main hitbox)
	 */
	protected void updateHitboxes() {
		if (hitboxes != null) {
			for (AABB hitbox : hitboxes) {
				hitbox.setPos(bounds, renderFacing);
			}
		}
	}

	/**
	 * Checks if a change of bounds would cause collision issues
	 * <p>
	 * This does not actually change the bounds, only checks for collisions at all locations within the potential new bounds
	 *
	 * @param posX   the potential new x position in tiles
	 * @param posY   the potential new y position in tiles
	 * @param width  the potential new width in tiles
	 * @param height the potential new height in tiles
	 * @return false if changing to these bounds would cause a collision, otherwise true
	 */
	protected boolean checkChangeOfBounds(float posX, float posY, float width, float height) {
		AABB testAABB = new AABB(posX, posY, width, height);

		int flooredLeft = (int) Math.floor(testAABB.left());
		int ceilRight = (int) Math.ceil(testAABB.right());
		int flooredTop = (int) Math.floor(testAABB.top());
		int ceilBottom = (int) Math.ceil(testAABB.bottom());
		for (int x = flooredLeft; x < ceilRight; x++) {
			for (int y = flooredTop; y < ceilBottom; y++) {
				if (!world.getTiles().test(x, y)) {
					continue;
				}
				TileInfo tile = world.getTiles().get(x, y).getTileType();
				AABB tileAABB = new AABB(x, y, 1, 1);
				if (tile.isObstacle() && testAABB.collides(tileAABB)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Change the entity's bounds/size.
	 * <p>
	 * Used when changing state e.g. moving -> sliding. <br>
	 * Adjusts the position to ensure fluidity
	 */
	protected boolean changeBounds(float width, float height) {
		float oldPosX = posX;
		float oldPosY = posY;
		if (onGround) {
			posY += getHeight() - height;
		}
		if (facing == -1 && getWidth() < width) {
			posX += getWidth() - width;
		}
		if (checkChangeOfBounds(posX, posY, width, height)) {
			setSize(width, height);
			return true;
		} else {
			posX = oldPosX;
			posY = oldPosY;
			return false;
		}
	}

	/**
	 * Returns the number of steps per collision detection.
	 * <p>
	 * This should ensure that an entity never moves horizontally more than half their width per step
	 * And also that an entity never moves vertically more that half their height per step
	 * Minimum return value is 4
	 *
	 * @return the number of steps to take.
	 */
	private int getCollisionScale(long ms) {
		float seconds = ms / (float) 1000;
		float distX = Math.abs(velX * seconds);
		float distY = Math.abs(velY * seconds);

		int stepsX = (int) Math.ceil(distX / bounds.getHalfWidth());
		int stepsY = (int) Math.ceil(distY / bounds.getHalfHeight());

		return Math.max(Math.max(stepsX, stepsY), 4);
	}

	/**
	 * The physics engine
	 * <p>
	 * Initialises/clears fields used to detect/process/react to collisions <br>
	 * then calls delegate methods to actually process this<br>
	 * <p>
	 * We process collisions in steps, to keep the distance between checks small <br>
	 * This way we gain a lot more precision than we would get from just doing this once per tick
	 *
	 * @param ms time since the last tick in milliseconds
	 * @WilliamHayward - I've made this public so that it can be overridden
	 * @ckent-78 aight
	 * @WilliamHayward - I've made this public so that it can be overridden
	 */
	public void updatePhysics(long ms) {
		float seconds = ms / (float) 1000;
		// apply gravity
		if (enableGravity) {
			updateVelYFromGravity(ms);
		}

		// Save previous states
		prevX = posX;
		prevY = posY;
		prevVelX = velX;
		prevVelY = velY;


		// initialise list of collided entites and BodyParts
		List<Entity> collidedEntities = new ArrayList<>(); // which entities have I collided with?
		List<BodyPart> hitLocations = new ArrayList<>(); // where did they hit me?

		// initialise list of collided tiles
		List<int[]> collidedTerrain = new ArrayList<>(); // coordinates of tile collisions
		List<Side> hitDirections = new ArrayList<>(); // which side of this entity is the tile on

		// Clearing collision flags used in various state machines

		onGround = false;
		onWall = 0;
		onCeiling = false;

		int collisionScale = getCollisionScale(ms);

		// distance travelled in one step
		float diffX = velX * seconds / collisionScale;
		float diffY = velY * seconds / collisionScale;

		// clear the list of tile hitboxes
		// this list is used for debugging terrain collisions
		// we render all of these hitboxes to show which ones were checked for collisions
		checkedTiles.clear();

		// saves the starting locatins of each hitbox

		bounds.saveCoords();
		if (hitboxes != null) {
			for (AABB hitbox : hitboxes) {
				hitbox.saveCoords();
			}

		}

		// This Reduces the speed of the Character when he is in Liquid

		if (isUnderLiquid) {
			diffX *= 0.7f;
			diffY *= 0.7f;
		}
		isUnderLiquid = false; // reset flag

		// process things one step at a time
		for (int step = 0; step < collisionScale; step++) {

			// increment position
			bounds.setX(bounds.left() + diffX);
			bounds.setY(bounds.top() + diffY);

			// process on bounds first
			// resolving any collisions with terrain
			processStep(bounds, collidedTerrain, hitDirections, collidedEntities, hitLocations);

			// then process all additional hitboxes, updating their's and bounds locations to maintain offsets
			// resolving any collisions with terrain

			if (hitboxes != null) {
				for (AABB hitbox : hitboxes) {
					hitbox.setPos(bounds, renderFacing);
					processStep(hitbox, collidedTerrain, hitDirections, collidedEntities, hitLocations);
					bounds.setPos(hitbox, renderFacing);
				}
			}

		}
		// End step

		detectWater();

		if (!strafeActive) {
			renderFacing = facing;
		}


		posX = bounds.left();
		posY = bounds.top();
		updateHitboxes();

		// This is kinda inefficient and hacky but it fixes some broken stuff
		// Note: not applicable to projectiles bc extra hitboxes
		// Basically if it all screwed up and you're about to warp through a wall
		// We snap you back to your previous location
		// Only a problem in small spaces like rooms
		if (this instanceof StateEntity && checkRemainingCollisions()) {
			posX = prevX;
			posY = prevY;
			bounds.setPos(posX, posY);
		}


		// React to collisions with terrain
		if (!collidedTerrain.isEmpty()) {
			collideWithTerrain(collidedTerrain, hitDirections);
		}
		if (!collidedEntities.isEmpty()) {
			onEntityCollide(collidedEntities, hitLocations);
		}
	}

	/**
	 * Scans along the top edge of the entity for liquid tiles <br>
	 * If a liquid tile is found, sets the isLiquid flag
	 */

	private void detectWater() {
		int topY = (int) Math.floor(bounds.top());
		int rightX = (int) Math.ceil(bounds.right() - 1);
		int leftX = (int) Math.floor(bounds.left());

		for (int testX = leftX; testX <= rightX; testX++) {

			if (!world.getTiles().test(testX, topY)) {
				continue;
			}
			TileInfo tile = world.getTiles().get(testX, topY).getTileType();
			AABB tileAABB = new AABB(testX, topY, 1, 1);
			if (tile.isLiquid() && bounds.collides(tileAABB)) {
				isUnderLiquid = true;
			}
		}
	}


	/**
	 * Detects if the input hitbox is currently colliding with anything and resolves the collision
	 * <p>
	 * Populates a list of entities and terrain with which we are colliding
	 * You can pass null into collidedTerrain and collidedEntities if you want to avoid onTerrainCollide and onEntityCollide form being called
	 *
	 * @param hitbox           the hitbox being processed
	 * @param collidedTerrain  a list of terrain locations that we've collided with in this tick
	 * @param hitDirections    which side of this entity did that collision occur on
	 * @param collidedEntities a list of entities that we've collided with in this tick
	 * @param hitLocations     which of our body parts collided with the entity
	 */

	private void processStep(AABB hitbox, List<int[]>
			collidedTerrain, List<Side> hitDirections, List<Entity> collidedEntities, List<BodyPart> hitLocations) {
		// the relevant row/column of tiles on each side
		// the -1 bc tiles are top left coords, so we want to look at the row that starts inside the player on the bottom/right edge
		int bottomY = (int) Math.ceil(hitbox.bottom() - 1);
		int topY = (int) Math.floor(hitbox.top());
		int rightX = (int) Math.ceil(hitbox.right() - 1);
		int leftX = (int) Math.floor(hitbox.left());


		if (velX < 0) {
			leftX += 1;
		} else if (velX > 0) {
			rightX -= 1;
		}

		if (velY > 0) { // moving down -> check the bottom edge for terrain/terrain entities
			for (int testX = leftX; testX <= rightX; testX++) {
				if (checkTileCollision(hitbox, collidedTerrain, hitDirections, testX, bottomY, Side.BOTTOM)) {
					break;
				}
			}
			TerrainEntity collidedTerrainEntity = checkTerrainEntities(bounds, Side.BOTTOM);
			if (collidedTerrainEntity != null) {
				insertInCollidedTerrain(collidedTerrain, hitDirections, collidedTerrainEntity.bounds);
			}
		} else if (velY < 0) { // moving up -> check the top edge for terrain/terrain entities
			for (int testX = leftX; testX <= rightX; testX++) {
				if (checkTileCollision(hitbox, collidedTerrain, hitDirections, testX, topY, Side.TOP)) {
					break;
				}
			}
			TerrainEntity collidedTerrainEntity = checkTerrainEntities(bounds, Side.TOP);
			if (collidedTerrainEntity != null) {
				insertInCollidedTerrain(collidedTerrain, hitDirections, collidedTerrainEntity.bounds);
			}
		}

		bottomY = (int) Math.ceil(hitbox.bottom() - 1);
		topY = (int) Math.floor(hitbox.top());
		rightX = (int) Math.ceil(hitbox.right() - 1);
		leftX = (int) Math.floor(hitbox.left());

		if (velX > 0) { // moving right -> check the right edge for terrain/terrain entities
			facing = 1;
			for (int testY = bottomY; testY >= topY; testY--) {
				if (checkTileCollision(hitbox, collidedTerrain, hitDirections, rightX, testY, Side.RIGHT)) {
					break;
				}
			}
			TerrainEntity collidedTerrainEntity = checkTerrainEntities(bounds, Side.RIGHT);
			if (collidedTerrainEntity != null) {
				insertInCollidedTerrain(collidedTerrain, hitDirections, collidedTerrainEntity.bounds);
			}
		} else if (velX < 0) {  // moving left -> check the left edge for terrain/terrain entities
			facing = -1;
			for (int testY = bottomY; testY >= topY; testY--) {
				// single | is deliberate, we don't want short circuiting
				if (checkTileCollision(hitbox, collidedTerrain, hitDirections, leftX, testY, Side.LEFT)) {
					break;
				}
			}
			TerrainEntity collidedTerrainEntity = checkTerrainEntities(bounds, Side.LEFT);
			if (collidedTerrainEntity != null) {
				insertInCollidedTerrain(collidedTerrain, hitDirections, collidedTerrainEntity.bounds);
			}
		}
		checkEntityCollisions(hitbox, collidedEntities, hitLocations);

	}

	/**
	 * Checks if the input hitbox collides with any terrain at the specified location
	 * <p>
	 * If a collision occurs, it is resolved (i.e. player is pushed out so they don't collided anymore)<br>
	 * And the coordinates of the collided tile are entered into the list
	 *
	 * @param hitbox          the hitbox for which we are checking collisions
	 * @param collidedTerrain a list of coordinates, storing which tiles we've collided with
	 * @param hitDirections   a list of Sides, storing on which side we've collided with a tile
	 * @param x               the x coordinate of the tile we're checking
	 * @param y               the y coordinate of the tile we're checking
	 * @param side            the side on which we're checking/use to resolve the collision
	 * @return true if a collision is found, otherwise false
	 */
	private boolean checkTileCollision(AABB hitbox, List<int[]> collidedTerrain, List<Side> hitDirections, int x,
	                                   int y, Side side) {
		if (!world.getTiles().test(x, y)) {
			return false;
		}
		TileInfo tile = world.getTiles().get(x, y).getTileType();
		AABB tileAABB = new AABB(x, y, 1, 1);
		if (world.renderHitboxes()) {
			checkedTiles.add(tileAABB);
		}
		if (tile.isObstacle() && hitbox.collides(tileAABB)) {
			warpGuard(hitbox, tileAABB, side);
			insertInCollidedTerrain(collidedTerrain, hitDirections, tileAABB);
			return true;
		}
		return false;
	}


	/**
	 * Sometimes just throwing a straight resolveCollision at things does not in fact resolve the collision
	 * Instead it sometimes warps you up or down even if that's not the idea
	 * What this does is instead check if that resolution makes sense before applying it.
	 * I.e. if resolving a left collision would move you further left, we push you up or down depending on velY
	 * And same goes for collisions on the right.
	 *
	 * @param hitbox      this entity's hitbox, that we want to move out of the other one
	 * @param otherHitbox the hitbox being compared against to fix collisions
	 * @param direction   the direction on which the collision occurs
	 */


	private void warpGuard(AABB hitbox, AABB otherHitbox, Side direction) {
		switch (direction) {
			case LEFT:
				if (otherHitbox.right() > hitbox.startLeft()) {
					if (velY > 0) {
						resolveCollision(hitbox, otherHitbox, Side.BOTTOM);
					} else if (velY < 0) {
						resolveCollision(hitbox, otherHitbox, Side.TOP);
					}
				} else {
					resolveCollision(hitbox, otherHitbox, Side.LEFT);
				}
				break;
			case RIGHT:
				if (otherHitbox.left() < hitbox.startRight()) {
					if (velY > 0) {
						resolveCollision(hitbox, otherHitbox, Side.BOTTOM);
					} else if (velY < 0) {
						resolveCollision(hitbox, otherHitbox, Side.TOP);
					}
				} else {
					resolveCollision(hitbox, otherHitbox, Side.RIGHT);
				}
				break;
			default:
				resolveCollision(hitbox, otherHitbox, direction);
				return;
		}
	}


	/**
	 * The most basic way of resolving a collision. When we detect overlapping hitboxes, we call this
	 * <p>
	 * What this does is set an edge of this hitbox to be equal to the opposite edge of the other hitbox <br>
	 * And so there is no more collision
	 * <p>
	 * i.e. set our left edge to equal their right edge
	 *
	 * @param ownHitbox   the hitbox being moved i.e. the one being processed in this step
	 * @param otherHitbox the hitbox eing compared against
	 * @param direction   the side of ownHitbox on which the collision occurred
	 */


	private void resolveCollision(AABB ownHitbox, AABB otherHitbox, Side direction) {
		switch (direction) {
			case BOTTOM:
				ownHitbox.setBottomY(otherHitbox.ownerTop());
				onGround = true;
				break;
			case TOP:
				ownHitbox.setY(otherHitbox.ownerBottom());
				onCeiling = true;
				break;
			case LEFT:
				ownHitbox.setX(otherHitbox.ownerRight());
				onWall = -1;
				break;
			case RIGHT:
				ownHitbox.setRightX(otherHitbox.ownerLeft());
				onWall = 1;
				break;
			case VOID:
			default:
				break;
		}
	}


	/**
	 * Checks for collisions with terrainEntities and resolves them as we would for regular ol tiles<br>
	 * Also informs the terrain entity about the collision, so that it can apply its affects on the next tick
	 *
	 * @param hitbox our hitbox that we're processing to check for collisions
	 * @param side   the side on which we're looking for collisions/resolve them
	 * @return the terrain entity with which we've collided, otherwise null
	 */


	private TerrainEntity checkTerrainEntities(AABB hitbox, Side side) {
		for (TerrainEntity platform : world.getTerrainEntities()) {
			if (platform.collidesWith(this, side) && hitbox.collides(platform) != BodyPart.VOID) {
				resolveCollision(hitbox, platform.bounds, side);
				platform.addToList(this);
				return platform;
			}
		}
		return null;
	}

	/**
	 * Checks if we've collided with any entities <br>
	 * If we have, records the entity and the BodyPart with which we collided in the lists
	 *
	 * @param hitbox           our hitbox that we're processing to check for collisions
	 * @param collidedEntities a list of entities with which we've collided this tick
	 * @param hitLocations     a list of BodyParts recording where we hit these enitities
	 */


	protected void checkEntityCollisions(AABB ownHitbox, List<Entity> collidedEntities, List<BodyPart> hitLocations) {
		if (collidedEntities == null || hitLocations == null) {
			return;
		}
		for (Entity entity : world.getAllEntities()) {
			if (entity == this || entity == null || entity instanceof Decoration) {
				continue;
			}
			if (entity.doesItBlockOtherEntities() && collisionFilter.test(entity)) {
				BodyPart location = ownHitbox.collides(entity);
				if (location != BodyPart.VOID) {
					insertInCollidedEntities(collidedEntities, hitLocations, entity, location);
				}
			}
		}
	}

	/**
	 * A very basic check that resolves any collisions along the right edge
	 * Used only in crates atm to stop them being pushed into walls
	 */
	public void basicRightEdgeCheck() {
		int bottomY = (int) Math.ceil(bounds.bottom() - 1);
		int topY = (int) Math.floor(bounds.top());
		int rightX = (int) Math.ceil(bounds.right() - 1);
		for (int testY = bottomY; testY >= topY; testY--) {
			if (checkTileCollision(bounds, null, null, rightX, testY, Side.RIGHT)) {
				break;
			}
		}
		checkTerrainEntities(bounds, Side.RIGHT);
	}


	/**
	 * A very basic check that resolves any collisions along the left edge
	 * Used only in crates atm to stop them being pushed into walls
	 */


	public void basicLeftEdgeCheck() {
		int bottomY = (int) Math.ceil(bounds.bottom() - 1);
		int topY = (int) Math.floor(bounds.top());
		int leftX = (int) Math.floor(bounds.left());

		for (int testY = bottomY; testY >= topY; testY--) {
			if (checkTileCollision(bounds, null, null, leftX, testY, Side.LEFT)) {
				break;
			}
		}
		checkTerrainEntities(bounds, Side.LEFT);
	}


	/**
	 * Entity specific logic that runs every tick. See StateEntity for an example (stateUpdate).
	 *
	 * @param ms millisecond tick the entity is being handled on
	 */
	protected abstract void tick(long ms);

	/**
	 * Entity collision event handler;
	 *
	 * @param hitLocations
	 */
	protected abstract void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations);

	/**
	 * Terrain collision event handler;
	 *
	 * @param side
	 */
	protected abstract void onTerrainCollide(int tileX, int tileY, Side side);

	/**
	 * On death event handler;
	 *
	 * @param cause entity that caused the death
	 */
	protected abstract void onDeath(Entity cause);

	/**
	 * Performs a special collision detection on the first tick an entity is added to the game<br>
	 * Otherwise calls updatePhysics
	 *
	 * @param ms The time since the last tick
	 */
	public void entityLoop(long ms) {
		this.latestTick = ms;
		// We do a special first tick, where we just place it into the world and
		// see if it collides with anything.
		// i.e. there is no movement on the first tick.
		if (firstTick) {
			if (this instanceof Projectile) {
				// if its a projectile, we check if it's colliding with terrain and let it kill itself if so
				firstTickCheckTerrainCollide();
			} else {
				// otherwise we push it upwards until its free (in theory)
				hardcoreCollisionResolution();
			}
			firstTick = false;
		} else {
			// don't apply physics to decorations. They don't deserve it
			if (!(this instanceof Decoration)) {
				updatePhysics(ms);
			}
		}
		if (onGround || onCeiling) {
			velY = 0;
		}
		tick(ms);

		if (this.isOnGround()) {
			applyTileEffects(ms);
		}
	}


	/**
	 * Applies effects from tiles that we're standing on
	 *
	 * @param ms time since last tick
	 */
	protected void applyTileEffects(long ms) {
		int centerX = (int) Math.floor(posX + getWidth() / 2);
		int bottomY = (int) Math.floor(posY + getHeight());
		if (world.getTiles().test(centerX, bottomY)) {
			Tile tile = world.getTiles().get(centerX, bottomY);
			tile.getTileType().onEntityOnTopOfBlock(this, tile, ms);
		}
	}

	/**
	 * Checks all tiles with which me might still be colliding
	 * <p>
	 * This is used to detect and prevent serious errors in the physics engine<br>
	 * e.g. warping right through walls in puzzle rooms
	 *
	 * @return true if we find a collision, otherwise false
	 */


	private boolean checkRemainingCollisions() {
		int flooredLeft = (int) Math.floor(this.bounds.ownerLeft());
		int ceilRight = (int) Math.ceil(this.bounds.ownerRight());
		int flooredTop = (int) Math.floor(this.bounds.ownerTop());
		int ceilBottom = (int) Math.ceil(this.bounds.ownerBottom());

		for (int x = flooredLeft; x < ceilRight; x++) {
			for (int y = flooredTop; y < ceilBottom; y++) {
				if (!world.getTiles().test(x, y)) {
					continue;
				}
				TileInfo tile = world.getTiles().get(x, y).getTileType();
				AABB tileAABB = new AABB(x, y, 1, 1);
				if (tile.isObstacle() && bounds.collides(tileAABB)) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Checks if the entity is intersecting with any obstacle tiles.
	 * <p> If so, moves
	 * the entity upwards until they are free. <br>
	 * Intended to resolve entities that get trapped in the ground on spawning
	 */
	private void hardcoreCollisionResolution() {
		boolean collidedTerrain = true;

		while (collidedTerrain) {
			int flooredLeft = (int) Math.floor(this.bounds.ownerLeft());
			int ceilRight = (int) Math.ceil(this.bounds.ownerRight());
			int flooredTop = (int) Math.floor(this.bounds.ownerTop());
			int ceilBottom = (int) Math.ceil(this.bounds.ownerBottom());

			collidedTerrain = false;
			for (int x = flooredLeft; x < ceilRight; x++) {
				for (int y = flooredTop; y < ceilBottom; y++) {
					if (checkTileCollision(bounds, null, null, x, y, Side.BOTTOM)) {
						collidedTerrain = true;
						break;
					}
				}
				if (collidedTerrain) {
					break;
				}
			}
		}

	}


	/**
	 * Inserts the input entity and BodyPart in the corresponding lists, to record the collision and deal with it at the
	 * end of the tick
	 * <p>
	 * If the a collision has already been recorded with the input entity, the BodyPart is overwritten if it has higher
	 * priority<br>
	 * Priority is determined by the order these elements occur in the BodyPart enum, with the lower down (higher index)
	 * elements having greater priority
	 *
	 * @param collidedEntities a list of which entities we've collided with this tick
	 * @param hitLocations     a list of locations with where on those entities we collided
	 * @param e                the entity with which we have collided, to add to the list
	 * @param bp               the location on e that the collision occurred, to add to the list
	 *                         >>>>>>> master
	 * @require both lists have the same size, so that for all entities in collidedEntities, the BodyPart with equal
	 * index denotes where the collision with that entity occurred
	 */
	private void insertInCollidedEntities(List<Entity> collidedEntities, List<BodyPart> hitLocations, Entity
			e, BodyPart bp) {
		if (collidedEntities == null || hitLocations == null) {
			return;
		}
		if (!collidedEntities.contains(e)) {
			collidedEntities.add(e);
			hitLocations.add(bp);
		} else {
			int index = collidedEntities.indexOf(e);
			BodyPart currentPart = hitLocations.get(index);
			if (bp.compareTo(currentPart) > 0) {
				hitLocations.set(index, bp);
			}
		}
	}

	/**
	 * Inserts the coordinates of the input bounds, and the side on which the collision occurred, into the lists. <br>
	 * This allows these tile collisions to be dealt with at the end of the tick
	 * <p>
	 * the side we insert is not necessarily the same as the one we use to resolve collisions<br>
	 * we determine the side based on the relative positions of the two center points.
	 * this is the side of our hitbox on which the collision occured, not the face of the tile.
	 *
	 * @param collidedTerrain list of coordintates, in which we store the coords of tiles we've collided with
	 * @param hitDirections   list of Sides, in which we store the side we struck those tiles
	 * @param tileBounds
	 * @require both lists have the same size, so that for all coords in collidedTerrain, the hitDirections with equal
	 * index denotes where the collision with that tile occurred
	 */
	private void insertInCollidedTerrain(List<int[]> collidedTerrain, List<Side> hitDirections, AABB tileBounds) {
		if (collidedTerrain == null || hitDirections == null) {
			return;
		}
		int[] thisPoint = {(int) tileBounds.left(), (int) tileBounds.top()};
		for (int[] otherPoint : collidedTerrain) {
			if (Arrays.equals(thisPoint, otherPoint)) {
				return;
			}
		}
		collidedTerrain.add(thisPoint);
		hitDirections.add(getDirection(this, tileBounds));
	}


	/**
	 * Returns the direction of the input AABB from the input entity
	 * <p>
	 * This is based on partitioning space into quadrants aligned on the diagonals.
	 * <p>
	 * The comparison is based on the centre points of the entity's bounds and the AABB
	 *
	 * @param entity the entity being processed
	 * @param other  the hitbox we want the direction of
	 * @return the direction in which the hitbox lies
	 */

	private Side getDirection(Entity entity, AABB other) {
		float diffX = entity.bounds.posX() - other.posX();
		float diffY = entity.bounds.posY() - other.posY();

		if (diffY < diffX && diffY < -diffX) {
			return Side.BOTTOM;
		} else if (diffY > diffX && diffY > -diffX) {
			return Side.TOP;
		} else if (diffY < diffX && diffY > -diffX) {
			return Side.RIGHT;
		} else if (diffY > diffX && diffY < -diffX) {
			return Side.LEFT;
		}
		return Side.VOID;
	}

	/**
	 * Checks all overlapping tiles/terrainEntities/Entities for collisions
	 * <p>
	 * Calls the relevant handle methods (onTerrainCollide and onEntityCollide)
	 * <p>
	 * To be used for projectiles so that they are still functional if they spawn inside stuff.
	 */

	private void firstTickCheckTerrainCollide() {
		List<Entity> collidedEntities = new ArrayList<>();
		List<BodyPart> hitLocations = new ArrayList<>();

		List<int[]> collidedTerrain = new ArrayList<>();
		List<Side> hitDirections = new ArrayList<>();


		bounds.setPos(posX, posY);
		processStep(bounds, collidedTerrain, hitDirections, collidedEntities, hitLocations);

		if (hitboxes != null) {
			for (AABB hitbox : hitboxes) {
				hitbox.setPos(bounds, renderFacing);
				processStep(hitbox, collidedTerrain, hitDirections, collidedEntities, hitLocations);
				bounds.setPos(hitbox, renderFacing);
			}
		}

		if (!collidedTerrain.isEmpty()) {
			collideWithTerrain(collidedTerrain, hitDirections);
		}

		if (!collidedEntities.isEmpty()) {
			onEntityCollide(collidedEntities, hitLocations);
		}
	}

	/**
	 * iterates through the list of tile collisions and hit directions <br>
	 * Applies onTerrainCollide to them
	 * <p>
	 * tbh we shoulda set this up the same was as onEntityCollide where it takes the list but oh well. Everyone else had
	 * their methods set up nice and pretty and cbf changing them
	 *
	 * @param collidedTerrain
	 * @param hitDirections
	 */

	public void collideWithTerrain(List<int[]> collidedTerrain, List<Side> hitDirections) {
		if (!collidedTerrain.isEmpty()) {
			for (int i = 0; i < collidedTerrain.size(); i++) {
				int x = collidedTerrain.get(i)[0];
				int y = collidedTerrain.get(i)[1];
				Side side = hitDirections.get(i);
				onTerrainCollide(x, y, side);
			}
		}
	}

	/**
	 * Gracefully calls the onDeath function for the entity to do any post-death animations before removing itself.
	 *
	 * @param cause the entity responsible for this entity's death
	 */
	public void kill(Entity cause) {
		onDeath(cause);
	}

	/**
	 * Deletes this entity from the world Does not call the onDeath callback
	 */
	public void delete() {
		world.deleteEntity(this);
	}

	/**
	 * Renders the entity's sprite and any additional sprites <p> Renders hitboxes as well if world.renderHitboxes is
	 * true
	 *
	 * @param gc       the graphics context on which the sprites are rendered
	 * @param viewport the viewport in which the sprites are rendered
	 * @param ms       the number of seconds since the last tick
	 */
	public void render(GraphicsContext gc, Viewport viewport, long ms) {
		if (!renderFlag) {
			return;
		}
		// do some maths
		this.viewport = viewport;
		float tileSize = viewport.getTileSideLength();
		int leftBorder = viewport.getLeftBorder();
		int topBorder = viewport.getTopBorder();

		int left = (int) Math.floor(viewport.getLeft());
		int top = (int) Math.floor(viewport.getTop());

		float subTileShiftX = (viewport.getLeft() - left) * tileSize;
		float subTileShiftY = (viewport.getTop() - top) * tileSize;

		float x = (posX - left) * tileSize + leftBorder - subTileShiftX;
		float y = (posY - top) * tileSize + topBorder - subTileShiftY;

		// Render the sprite
		if (sprite != null) {
			if (renderFacing > 0) {
				gc.drawImage(sprite.getFrame(), x, y, getWidth() * tileSize, getHeight() * tileSize);
			} else {
				gc.drawImage(sprite.getFrame(), x + getWidth() * tileSize, y, -getWidth() * tileSize,
						getHeight() * tileSize);
			}
		}

		// Render additional sprites
		if (additionalSprites != null && !additionalSprites.isEmpty()) {
			for (SpriteRelation sr : additionalSprites.values()) {
				if (sr != null) {
					sr.renderSprite(gc, tileSize, x, y);
				}
			}
		}

		// Render collision line hitboxes
		if (world.renderHitboxes()) {
			bounds.render(gc, viewport);
			for (AABB tile : checkedTiles) {
				tile.render(gc, viewport);
			}
			if (hitboxes != null) {
				for (AABB hitbox : hitboxes) {
					hitbox.render(gc, viewport);
				}
			}
		}
	}

	/**
	 * set the entity to belong to the input world
	 *
	 * @param world the new world
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * @return whether the entity blocks Other Entities
	 */
	public boolean doesItBlockOtherEntities() {
		return blocksOtherEntities;
	}

	/**
	 * @param blocksOtherEntities set blocksOtherEntities to true to block other entities
	 */
	public void setBlocksOtherEntities(boolean blocksOtherEntities) {
		this.blocksOtherEntities = blocksOtherEntities;
	}
}