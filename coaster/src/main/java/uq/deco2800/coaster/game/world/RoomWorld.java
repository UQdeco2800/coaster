package uq.deco2800.coaster.game.world;

import uq.deco2800.coaster.game.entities.Player;

/**
 * A sub-world for a small room.
 */
public abstract class RoomWorld extends World {
	// The tiles of this room
	public WorldTiles tiles = null;
	// Stores the x and y coords that the player will go back to when they leave the room
	private float tempPlayerX = 0;
	private float tempPlayerY = 0;
	// The main world
	private World parentWorld = null;

	@Override
	public WorldTiles getTiles() {
		return tiles;
	}

	@Override
	public void loadAroundPlayer(Player player) {
		// Do nothing instead
	}

	@Override
	public boolean isDecoGenEnabled() {
		return false;
	}

	@Override
	public boolean isBuildingGenEnabled() {
		return false;
	}

	@Override
	public boolean isTotemGenEnabled() {
		return false;
	}

	@Override
	public boolean isNpcGenEnabled() {
		return false;
	}

	/**
	 * @return the x coordinate which the player will return to when leaving the room
	 */
	float getTempPlayerX() {
		return tempPlayerX;
	}

	/**
	 * Will set the x coordinate which the player will return to when leaving the room
	 * @param tempPlayerX the x coordinate which the player will return to when leaving the room
	 */
	void setTempPlayerX(float tempPlayerX) {
		this.tempPlayerX = tempPlayerX;
	}

	/**
	 * @return the y coordinate which the player will return to when leaving the room
	 */
	float getTempPlayerY() {
		return tempPlayerY;
	}

	/**
	 * Will set the x coordinate which the player will return to when leaving the room
	 * @param tempPlayerY the x coordinate which the player will return to when leaving the room
	 */
	void setTempPlayerY(float tempPlayerY) {
		this.tempPlayerY = tempPlayerY;
	}

	/**
	 * @return the instance of the parent world
	 */
	World getParentWorld() {
		return parentWorld;
	}

	/**
	 * Sets the instance of the parent world
	 * @param parentWorld the instance of the parent world
	 */
	void setParentWorld(World parentWorld) {
		this.parentWorld = parentWorld;
	}

	/**
	 * @return returns the x coordinate of the starting position in this room
	 */
	abstract float getStartingX();

	/**
	 * @return the y coordinate of the starting position of this room
	 */
	abstract float getStartingY();

	/**
	 * This will populate the world with the tiles and entities it needs
	 */
	abstract void populateRoom();
}
