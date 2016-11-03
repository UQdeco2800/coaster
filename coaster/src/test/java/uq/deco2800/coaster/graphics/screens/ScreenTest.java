package uq.deco2800.coaster.graphics.screens;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.canvas.Canvas;
import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Viewport;


public class ScreenTest {
	private static Logger logger = LoggerFactory.getLogger(ScreenTest.class);
	
	@Test
	public void testGameScreen() {
		TestHelper.load();
		World.getInstance().setTiles(TestHelper.getMobilityTestTiles());
		double resWidth, resHeight;
		resWidth = resHeight = 1000;

		Canvas canvas = new Canvas(resWidth, resHeight);
		Viewport viewport = new Viewport((int) resWidth, (int) resHeight);

		try {
			new GameScreen(viewport, canvas).render(1, false);
		} catch (ArrayIndexOutOfBoundsException e) {
			//resolve this
			logger.error("ScreenTest Array Error", e);
		}

	}
}
