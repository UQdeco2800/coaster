package uq.deco2800.coaster.graphics.screens;

import java.util.Date;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.graphics.Viewport;


public class ToasterScreen extends Screen {
	private Viewport viewport;

	private Canvas canvas;
	private GraphicsContext gc;

	public ToasterScreen(Viewport viewport, Canvas canvas) {
		super(canvas);
		this.viewport = viewport;
		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();
		this.setVisible(true);
	}


	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		canvas.setVisible(visible);
	}

	//TODO RyanCarrier set auto width by 20%

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
		//Match debug
		//Styling
		int rightEdge = viewport.getResWidth();
		int fontSize = 20; //bigger than debug cause we better fam; plus we all know size matters
		Color fontCol = Color.WHITE;
		Color shadowCol = Color.BLACK;

		gc.setFont(new Font(fontSize));
		canvas.setStyle("-fx-stroke: black;-fx-stroke-width: 2px;");

		gc.setTextAlign(TextAlignment.RIGHT);
		String text = Toaster.getToast(new Date());

		gc.setFill(shadowCol);
		gc.fillText(text, rightEdge - 20 + 1, canvas.getHeight() / 2 + 1);
		gc.setFill(fontCol);
		gc.fillText(text, rightEdge - 20, canvas.getHeight() / 2);


	}
}
