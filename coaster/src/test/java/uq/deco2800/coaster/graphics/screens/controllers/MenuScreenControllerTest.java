package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.scene.control.ScrollPane;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.Engine;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Renderer;
import uq.deco2800.coaster.graphics.Window;

/**
 * Created by lukeabel on 23/10/2016.
 */
public class MenuScreenControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(FXMLControllerRegistrationTest.class);
	private Engine engine = Window.getEngine();
	private World world = World.getInstance();
	private Renderer r = engine.getRenderer();


	@Before
	public void load() {
		TestHelper.loadWithFXML();
	}

	@Test
	public void testStartScreenElements() {
		StartScreenController controller = (StartScreenController)
				FXMLControllerRegister.get(StartScreenController.class);

		//Controller tests
		assert controller != null;
		Assert.assertEquals(controller.getClass(), StartScreenController.class);

		//Null element tests
		assert controller.loadButton != null;
		assert controller.achievementsButton != null;
		assert controller.leaderboardButton != null;
		assert controller.optionsButton != null;
		assert controller.quitButton != null;
		assert controller.startButton != null;

	}

	@Test
	public void testAboutScreenElements() {
		AboutScreenController controller = (AboutScreenController) FXMLControllerRegister.get(AboutScreenController.class);
		String blurbText = controller.getBlurb();

		//Controller tests
		assert controller != null;
		Assert.assertEquals(controller.getClass(), AboutScreenController.class);

		//Null element tests
		assert controller.startContainer != null;
		assert controller.backButton != null;
		assert controller.helpScreen != null;
		assert controller.blurb != null;
		assert controller.blurbScroll != null;

		//Text tests
		assert controller.blurb.getText() != null;
		assert controller.blurb.getText().equals(blurbText);

		//Scrollbar tests
		Assert.assertEquals(controller.blurbScroll.getHbarPolicy(), ScrollPane.ScrollBarPolicy.NEVER);
		Assert.assertEquals(controller.blurbScroll.getVbarPolicy(), ScrollPane.ScrollBarPolicy.AS_NEEDED);
	}

	@Test
	public void testDifficultyScreenElements() {
		DifficultyController controller = (DifficultyController) FXMLControllerRegister.get(DifficultyController.class);

		//Controller tests
		assert controller != null;
		Assert.assertEquals(controller.getClass(), DifficultyController.class);

		//Null element tests
		assert controller.difficultyContainer != null;
		assert controller.tutorial != null;
		assert controller.easy != null;
		assert controller.medium != null;
		assert controller.hard != null;
		assert controller.insane != null;
		assert controller.backButton != null;
		assert controller.multiplayer != null;
		assert controller.background != null;

	}
}
