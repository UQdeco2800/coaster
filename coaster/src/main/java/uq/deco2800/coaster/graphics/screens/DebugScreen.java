package uq.deco2800.coaster.graphics.screens;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import uq.deco2800.coaster.core.Engine;
import uq.deco2800.coaster.game.debug.Debug;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Viewport;


/**
 * A Debug overlay for developers. Displays text into the top right corner of
 * viewport, toggleable by hotkey J (Default).
 */
public class DebugScreen extends Screen {
	private Viewport viewport;
	private Canvas canvas;
	private GraphicsContext gc;

	public DebugScreen(Viewport viewport, Engine engine, Canvas canvas) {
		super(canvas);

		this.viewport = viewport;
		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		canvas.setVisible(visible);
	}

	@Override
	public void setWidth(int newWidth) {
		canvas.setWidth(newWidth);
	}

	@Override
	public void setHeight(int newHeight) {
		canvas.setHeight(newHeight);
	}

	public void render(long ms, boolean renderBackGround) {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		TextAlignment returnAlignment = gc.getTextAlign();

		//Styling
		int rightEdge = viewport.getResWidth();
		int fontSize = 14;
		Color fontCol = Color.WHITE;
		gc.setFill(fontCol);
		gc.setFont(new Font(fontSize));
		gc.setTextAlign(TextAlignment.RIGHT);

		//Display
		Debug debug = World.getInstance().getDebug();
		String debugString = debug.getDebugString();
		gc.fillText(debugString, rightEdge - 10, fontSize); // -10 and fontsize for margin
		//Revert
		gc.setTextAlign(returnAlignment);
		debug.clearAllDebugStrings(); // Prevent repetition
	}
}
