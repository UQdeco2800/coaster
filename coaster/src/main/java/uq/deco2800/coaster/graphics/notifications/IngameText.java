package uq.deco2800.coaster.graphics.notifications;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uq.deco2800.coaster.graphics.Viewport;

/**
 * Draws dynamic text inside the world
 * Static ingametext will generate on fixed position(above player,
 * new text will be overwritten, recommended for player status update)
 * dynamic ingametext will generate on the position declared
 */
public class IngameText {

	/**
	 * In game texts can be either static or dynamic.
	 */
	public enum textType {
		STATIC,
		DYNAMIC
	}

	private textType type; // in game text type
	private float posX; // x position of text
	private float posY; // y position of text
	private String text; // string of text to be displayed
	private int duration; // duration remaining to be displayed
	private double R; // red value
	private double G; // green value
	private double B; // blue value
	private double A; // alpha value

	/**
	 * Constructor stores the x and y position of the text to be displayed, as well as its colour and whether it is
	 * dynamic or static type. Start time is used to determine the texts
	 */
	public IngameText(String text, float posX, float posY, int duration, textType type, double R, double G, double B, double A) {
		this.type = type;
		this.text = text;
		this.posX = posX;
		this.posY = posY;
		this.duration = duration;
		this.R = R;
		this.G = G;
		this.B = B;
		this.A = A;
	}

	/**
	 * Renders the text in the game
	 */
	public void render(GraphicsContext gc, Viewport viewport, long ms) {
		if (duration <= 0) {
			return;
		}
		if (type == textType.DYNAMIC) {
			float tileSize = viewport.getTileSideLength();
			int leftBorder = viewport.getLeftBorder();
			int topBorder = viewport.getTopBorder();

			int left = (int) Math.floor(viewport.getLeft());
			int top = (int) Math.floor(viewport.getTop());

			float subTileShiftX = (viewport.getLeft() - left) * tileSize;
			float subTileShiftY = (viewport.getTop() - top) * tileSize;

			float x = (posX - left) * tileSize + leftBorder - subTileShiftX;
			float y = (posY - top) * tileSize + topBorder - subTileShiftY;
			if (duration > 1000) {
				posY -= 0.02;
			}

			gc.setFill(new Color(R, G, B, A));
			gc.fillText(text, x, y);
		} else {
			int height = viewport.getResHeight();
			int width = viewport.getResWidth();

			if (duration > 1000) {
				posY -= 0.02;
			}

			gc.setFill(new Color(R, G, B, A));
			gc.fillText(text, width/2 - 30, height/2 - 20 - posY);
		}
		duration -= ms;
	}

	/**
	 * Returns the duration remaining for the in game text.
	 */
	public int getDuration() {
		return duration;
	}
}