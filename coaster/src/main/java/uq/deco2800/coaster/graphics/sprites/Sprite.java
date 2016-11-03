package uq.deco2800.coaster.graphics.sprites;

import javafx.scene.image.Image;
import uq.deco2800.coaster.game.tiles.TileInfo;

//An instance of a SpriteInfo. This contains a reference to the SpriteInfo (and therefore the frames), but keeps its
//own information about the current state of the animation.
public class Sprite {
	private SpriteInfo si;
	private int frameDuration;
	private long lastFrameTime;
	private int currentFrame;

	private int width;
	private int height;

	private SpriteList spriteId;

	/**
	 * Constructor for entity sprites
	 */
	public Sprite(SpriteList spriteId) {
		this(SpriteCache.getSpriteInfo(spriteId));
		this.spriteId = spriteId;
	}

	/**
	 * Constructor for tile sprites
	 */
	public Sprite(TileInfo tileType, String variant) {
		this(SpriteCache.getSpriteInfo(tileType, variant));
	}

	//Private to ensure nobody loads sprites that aren't cached
	private Sprite(SpriteInfo si) {
		this.si = si;
		frameDuration = si.frameDuration;
		lastFrameTime = System.currentTimeMillis();
		currentFrame = 0;

		width = (int) si.frames.getFrame(0).getWidth();
		height = (int) si.frames.getFrame(0).getHeight();
	}

	/**
	 * Gets the ID of the sprite
	 *
	 * @return the spriteID
	 */
	public SpriteList getSpriteId() {
		return spriteId;
	}

	/**
	 * Sets the frame duration to allow dynamic changes in animation speed
	 */
	public void setFrameDuration(int newDuration) {
		frameDuration = newDuration;
	}

	/**
	 * Resets the frame duration to the value stored in the cache
	 */
	public void resetFrameDuration() {
		frameDuration = si.frameDuration;
	}

	/**
	 * Gets the next frame of the animation sequence. If the time elapsed since the last getFrame call is greater than
	 * the frameDuration, then the animation will progress to the next frame in the sequence.
	 */
	public Image getFrame() {
		Image frame = si.frames.getFrame(currentFrame);

		if (frameDuration != -1) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastFrameTime > frameDuration) {
				currentFrame = (currentFrame + 1) % si.frames.getFrameCount();
				lastFrameTime = currentTime;
			}
		}
		return frame;
	}

	public void delete() {
		si = null;
	}

	/**
	 * Returns the width of the sprite.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the sprite.
	 */
	public int getHeight() {
		return height;
	}

}