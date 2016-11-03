package uq.deco2800.coaster.graphics.screens;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uq.deco2800.coaster.graphics.Viewport;

//A UI overlay for the game. Doesn't do anything at the moment.
//Adding a little bit of background colour for Skill tree - W
public class UIScreen extends Screen {
	private Canvas canvas;
	private GraphicsContext gc;

	public UIScreen(Viewport viewport, Canvas canvas) {
		super(canvas);

		this.setVisible(true);

		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();
	}

	public void render(long ms, boolean renderBackGround) {
		gc.setFill(Color.SILVER);

		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setStroke(Color.CHOCOLATE);
		gc.setFont(new Font(350));
		gc.strokeText("SKILLZ ", 0, 400);
	}

	@Override
	public void setWidth(int newWidth) {
		canvas.setWidth(newWidth);
	}

	@Override
	public void setHeight(int newHeight) {
		canvas.setHeight(newHeight);
	}
}
