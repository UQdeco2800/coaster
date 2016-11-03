package uq.deco2800.coaster.graphics;

import uq.deco2800.coaster.game.world.Chunk;

//This is the class that controls the "camera" of the game. You can move the camera, position it onto a certain tile,
//and much more.
public class Viewport {
	private static final int VIEWPORT_WIDTH = 64; //Remember to keep this aspect ratio 16:9 for maximum compatibility
	private static final int VIEWPORT_HEIGHT = 36;
	//These units are in tiles
	//The viewport render will "snap" to tiles if you try to pan in a map smaller than the screen res, but that's fine
	//(you shouldn't be able to pan in those maps anyway)
	private float top;
	private float left;
	//These are in pixels
	private int resWidth;
	private int resHeight;
	private int borderLeft;
	private int borderTop;

	private float tileSideLength;

	public Viewport(int resWidth, int resHeight) {
		initViewport(resWidth, resHeight);
	}

	public void initViewport(int resWidth, int resHeight) {
		this.resWidth = resWidth;
		this.resHeight = resHeight;

		//Use resolution dimensions to determine aspect ratio and borders
		//The viewport is 16:9, and we box it into the resolution dimensions
		float viewportAR = VIEWPORT_WIDTH / (float) VIEWPORT_HEIGHT;
		float screenAR = resWidth / (float) resHeight;
		if (screenAR <= viewportAR) {
			tileSideLength = resHeight / (float) VIEWPORT_HEIGHT;
		} else {
			tileSideLength = resWidth / (float) VIEWPORT_WIDTH;
		}
	}

	public void calculateBorders(int mapWidth, int mapHeight) {
		int minWidth = Math.min(mapWidth, VIEWPORT_WIDTH);
		int minHeight = Math.min(mapHeight, VIEWPORT_HEIGHT);

		borderLeft = (int) ((resWidth - (minWidth * tileSideLength)) / 2);
		borderTop = (int) ((resHeight - (minHeight * tileSideLength)) / 2);
		if (borderLeft < 0) {
			borderLeft = 0;
		}
		if (borderTop < 0) {
			borderTop = 0;
		}
	}

	//Takes a tile coordinate input and returns the pixel coordinate.
	public int getPixelCoordX(float x) {
		float pixel = (x - left) * tileSideLength;
		pixel += borderLeft;
		return (int) pixel;
	}

	public int getPixelCoordY(float y) {
		float pixel = (y - top) * tileSideLength;
		pixel += borderTop;
		return (int) pixel;
	}

	//Takes a pixel coordinate and converts it to tile coordinates.
	public float getTileCoordX(int x) {
		int adjustedX = x - borderLeft;
		return (adjustedX / tileSideLength) + left;
	}

	public float getTileCoordY(int y) {
		int adjustedY = y - borderTop;
		return (adjustedY / tileSideLength) + top;

	}

	/**
	 * Moves the viewport the specified number of tiles.
	 */
	public void move(float dx, float dy) {
		top += dy;
		left += dx;
	}

	/*
	 * Returns the pixel length of a tile's side.
	 */
	public float getTileSideLength() {
		return tileSideLength;
	}

	/**
	 * Centers the viewport on the specified tile coordinate.
	 */
	public void centerOn(float x, float y) {
		centerOnX(x);
		centerOnY(y);
	}

	public void centerOnX(float x) {
		left = x - (VIEWPORT_WIDTH / 2.0f);
	}

	public void centerOnY(float y) {
		top = y - (VIEWPORT_HEIGHT / 2.0f);

		if (top + VIEWPORT_HEIGHT > Chunk.CHUNK_HEIGHT) {
			top = Chunk.CHUNK_HEIGHT - VIEWPORT_HEIGHT - 1;
		}
	}

	public float getTop() {
		return top;
	}

	public float getLeft() {
		return left;
	}

	public float getRight() {
		return left + VIEWPORT_WIDTH;
	}

	public float getBottom() {
		return top + VIEWPORT_HEIGHT;
	}

	public int getWidth() {
		return VIEWPORT_WIDTH;
	}

	public int getHeight() {
		return VIEWPORT_HEIGHT;
	}

	public int getLeftBorder() {
		return borderLeft;
	}

	public int getTopBorder() {
		return borderTop;
	}

	public int getResWidth() {
		return resWidth;
	}

	public int getResHeight() {
		return resHeight;
	}
}
