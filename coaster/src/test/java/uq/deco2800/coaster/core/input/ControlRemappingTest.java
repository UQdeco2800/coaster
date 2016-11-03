package uq.deco2800.coaster.core.input; /**
 * The class which contains Junit tests for the ControlsKeyMap and ControlSchemeManager classes.
 */

import org.junit.Test;

import java.io.File;
import java.util.EnumSet;

import javafx.scene.input.KeyCode;
import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.screens.controllers.ControlScreenController;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ControlRemappingTest {
	private static World world = World.getInstance();

	public void initialise() {
		TestHelper.load();
		world.debugReset();
		ControlScreenController controller = new ControlScreenController();
		controller.skipUI();
	}


	@Test
	public void testRegisterKey() {
		initialise();
		ControlsKeyMap.clearAllMappings();

		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);

		assertTrue("Verifying jump has been mapped", ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.JUMP));
		assertTrue("Verifying jump has been mapped", ControlsKeyMap.mappingContains(KeyCode.P, GameAction.JUMP));
		assertTrue("Verifying move left has been mapped",
				ControlsKeyMap.mappingContains(KeyCode.LEFT, GameAction.MOVE_LEFT));
		assertTrue("Verifying move right has been mapped",
				ControlsKeyMap.mappingContains(KeyCode.RIGHT, GameAction.MOVE_RIGHT));
	}

	@Test
	public void testgetGameAction() {
		initialise();
		ControlsKeyMap.clearAllMappings();

		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);

		assertTrue("Verifying jump has been mapped",
				ControlsKeyMap.getGameAction(KeyCode.SPACE).equals(GameAction.JUMP));
		assertTrue("Verifying jump has been mapped", ControlsKeyMap.getGameAction(KeyCode.P).equals(GameAction.JUMP));
		assertTrue("Verifying MOVE_LEFT has been mapped",
				ControlsKeyMap.getGameAction(KeyCode.LEFT).equals(GameAction.MOVE_LEFT));
		assertTrue("Verifying MOVE_RIGHT has been mapped",
				ControlsKeyMap.getGameAction(KeyCode.RIGHT).equals(GameAction.MOVE_RIGHT));
	}

	@Test
	public void testgetKeyCode() {
		initialise();
		ControlsKeyMap.clearAllMappings();

		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);

		assertTrue("Verifying jump has been mapped", ControlsKeyMap.getKeyCode(GameAction.JUMP).equals(KeyCode.SPACE)
				|| ControlsKeyMap.getKeyCode(GameAction.JUMP).equals(KeyCode.P));
		assertTrue("Verifying MOVE_LEFT has been mapped",
				ControlsKeyMap.getKeyCode(GameAction.MOVE_LEFT).equals(KeyCode.LEFT));
		assertTrue("Verifying MOVE_RIGHT has been mapped",
				ControlsKeyMap.getKeyCode(GameAction.MOVE_RIGHT).equals(KeyCode.RIGHT));
		assertTrue("Verifying action has not been mapped",
				ControlsKeyMap.getKeyCode(GameAction.BASIC_ATTACK).equals(KeyCode.UNDEFINED));
	}

	@Test
	public void testDeleteAllEntries() {
		initialise();
		ControlsKeyMap.clearAllMappings();

		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);
		ControlsKeyMap.clearAllMappings();

		assertFalse("Verifying jump map has been cleared",
				ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.JUMP));
		assertFalse("Verifying jump map has been cleared", ControlsKeyMap.mappingContains(KeyCode.P, GameAction.JUMP));
		assertFalse("Verifying move left map has been cleared",
				ControlsKeyMap.mappingContains(KeyCode.LEFT, GameAction.MOVE_LEFT));
		assertFalse("Verifying move right map has been cleared",
				ControlsKeyMap.mappingContains(KeyCode.RIGHT, GameAction.MOVE_RIGHT));
	}

	@Test
	public void testAddUniqueMapping() {
		initialise();
		ControlsKeyMap.clearAllMappings();

		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.L, GameAction.BASIC_ATTACK);
		ControlsKeyMap.registerKey(KeyCode.ESCAPE, GameAction.PAUSE);
		ControlsKeyMap.addUniqueMapping(GameAction.JUMP, KeyCode.L);

		assertFalse("Verifying space was removed as jump key",
				ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.JUMP));
		assertFalse("Verifying P was removed as jump key", ControlsKeyMap.mappingContains(KeyCode.P, GameAction.JUMP));
		assertTrue("Verifying jump was added to L key", ControlsKeyMap.mappingContains(KeyCode.L, GameAction.JUMP));

		assertTrue("Verifying previous L key action was mapped to jump key", ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.BASIC_ATTACK) || ControlsKeyMap.mappingContains(KeyCode.P, GameAction.BASIC_ATTACK));

	}

	@Test
	public void testAddUniqueMappingUndefined() {
		initialise();
		ControlsKeyMap.clearAllMappings();

		ControlsKeyMap.registerKey(KeyCode.L, GameAction.BASIC_ATTACK);
		ControlsKeyMap.addUniqueMapping(GameAction.JUMP, KeyCode.L);

		assertTrue("Verifying jump was added to L key", ControlsKeyMap.mappingContains(KeyCode.L, GameAction.JUMP));
		assertTrue("Verifying previous L key action was mapped to jump key (undefined)",
				ControlsKeyMap.mappingContains(KeyCode.UNDEFINED, GameAction.BASIC_ATTACK));
	}

	@Test
	public void testSwapTwoActionKeys() {
		initialise();

		ControlsKeyMap.clearAllMappings();
		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.BASIC_ATTACK);
		ControlsKeyMap.registerKey(KeyCode.Z, GameAction.BASIC_ATTACK);
		ControlsKeyMap.registerKey(KeyCode.ESCAPE, GameAction.PAUSE);
		ControlsKeyMap.swapKeys(KeyCode.SPACE, KeyCode.Z);

		assertNull("Verifying extra jump map has been cleared",
				ControlsKeyMap.getGameAction(KeyCode.P));
		assertTrue("Verifying jump has been mapped", ControlsKeyMap.mappingContains(KeyCode.Z, GameAction.JUMP));
		assertNull("Verifying extra Basic Attack map has been cleared",
				ControlsKeyMap.getGameAction(KeyCode.LEFT));
		assertTrue("Verifying Basic Attack has been mapped",
				ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.BASIC_ATTACK));
	}

	@Test
	public void testSwapActionKeyWithBlank() {
		initialise();

		ControlsKeyMap.clearAllMappings();
		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.Z, GameAction.BASIC_ATTACK);
		ControlsKeyMap.registerKey(KeyCode.ESCAPE, GameAction.PAUSE);
		ControlsKeyMap.swapKeys(KeyCode.SPACE, KeyCode.Q);

		assertFalse("Verifying jump map has been cleared",
				ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.JUMP));
		assertFalse("Verifying jump map has been cleared", ControlsKeyMap.mappingContains(KeyCode.P, GameAction.JUMP));
		assertTrue("Verifying jump has been mapped", ControlsKeyMap.mappingContains(KeyCode.Q, GameAction.JUMP));
		assertNull("Verifying original key has been cleared", ControlsKeyMap.getGameAction(KeyCode.SPACE));
	}

	@Test
	public void testSwapBlankwithActionKey() {
		initialise();

		ControlsKeyMap.clearAllMappings();
		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.Z, GameAction.BASIC_ATTACK);
		ControlsKeyMap.registerKey(KeyCode.ESCAPE, GameAction.PAUSE);
		ControlsKeyMap.swapKeys(KeyCode.Q, KeyCode.SPACE);

		assertFalse("Verifying jump map has been cleared",
				ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.JUMP));
		assertFalse("Verifying jump map has been cleared", ControlsKeyMap.mappingContains(KeyCode.P, GameAction.JUMP));
		assertTrue("Verifying jump has been mapped", ControlsKeyMap.mappingContains(KeyCode.Q, GameAction.JUMP));
		assertNull("Verifying original key has been cleared", ControlsKeyMap.getGameAction(KeyCode.SPACE));
	}

	@Test
	public void testSwapWithEscape() {
		initialise();

		ControlsKeyMap.clearAllMappings();
		ControlsKeyMap.registerKey(KeyCode.SPACE, GameAction.JUMP);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.RIGHT, GameAction.MOVE_RIGHT);
		ControlsKeyMap.registerKey(KeyCode.LEFT, GameAction.MOVE_LEFT);
		ControlsKeyMap.registerKey(KeyCode.Z, GameAction.BASIC_ATTACK);
		ControlsKeyMap.registerKey(KeyCode.ESCAPE, GameAction.PAUSE);
		ControlsKeyMap.swapKeys(KeyCode.SPACE, KeyCode.ESCAPE);

		assertTrue("Verifying jump map has remained",
				ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.JUMP));
		assertTrue("Verifying pause map has remained",
				ControlsKeyMap.mappingContains(KeyCode.ESCAPE, GameAction.PAUSE));
	}


	@Test
	public void testControlScheme() {
		initialise();

		EnumSet<ControlScheme> schemes = EnumSet.of(ControlScheme.DEFAULT, ControlScheme.ALTERNATE_1);

		assertTrue(schemes.contains(ControlScheme.valueOf("DEFAULT")));
		assertTrue(schemes.contains(ControlScheme.valueOf("ALTERNATE_1")));

		ControlsKeyMap.importControlScheme(ControlScheme.DEFAULT);
		assertTrue("Verifying MOVE_LEFT is A",
				ControlsKeyMap.mappingContains(KeyCode.A, GameAction.MOVE_LEFT));
		ControlsKeyMap.importControlScheme(ControlScheme.ALTERNATE_1);
		assertTrue("Verifying MOVE_LEFT is LEFT",
				ControlsKeyMap.mappingContains(KeyCode.LEFT, GameAction.MOVE_LEFT));
	}

	@Test
	public void testSaveLoadMapping() {
		initialise();
		String saveFileLocation = "tmp/mapsave.json";
		File f = new File(saveFileLocation);
		ControlsKeyMap.importControlScheme(ControlScheme.DEFAULT);

		assertTrue("Verifying JUMP is Space",
				ControlsKeyMap.mappingContains(KeyCode.SPACE, GameAction.JUMP));

		ControlsKeyMap.registerKey(KeyCode.P, GameAction.JUMP);
		assertTrue("Verifying JUMP is P",
				ControlsKeyMap.mappingContains(KeyCode.P, GameAction.JUMP));

		ControlsKeyMap.saveMapping(f);

		ControlsKeyMap.registerKey(KeyCode.O, GameAction.JUMP);
		assertTrue("Verifying JUMP is O",
				ControlsKeyMap.mappingContains(KeyCode.O, GameAction.JUMP));

		ControlsKeyMap.loadMapping(f);
		assertTrue("Verifying JUMP is P",
				ControlsKeyMap.mappingContains(KeyCode.P, GameAction.JUMP));


	}

}
