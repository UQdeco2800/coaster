package uq.deco2800.coaster.graphics.sprites;

import javafx.scene.image.Image;

//A wrapper for Image[] so we don't accidentally duplicate it between instances, as well as some helper methods.
public class FrameCollection {
	private Image[] frames;

	/**
	 * idk what this does but #documentation
	 */
	public FrameCollection(Image[] frames) {
		this.frames = frames;
	}

	/**
	 * idk what this does but #documentation
	 */
	public Image getFrame(int index) {
		return frames[index];
	}

	/**
	 * idk what this does but #documentation
	 */
	public int getFrameCount() {
		return frames.length;
	}
}