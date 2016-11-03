package uq.deco2800.coaster.core.input; /**
 * The class which contains Junit tests for the InputManager class.
 */

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputManagerTest {
	private static World world = World.getInstance();

	public void initialise() {
		TestHelper.load();
		world.debugReset();
	}

	@Test
	public void flagToSwap() {
		initialise();
		assertFalse(InputManager.getSwappingState());
		InputManager.flagToSwap();
		assertTrue(InputManager.getSwappingState());
		assertFalse(InputManager.getSwapKey1State());
	}

	@Test
	public void flagToReMap() {
		initialise();
		assertFalse(InputManager.getMappingState());
		InputManager.flagToReMap(GameAction.JUMP);
		assertTrue(InputManager.getMappingState());
		InputManager.breakFromReMap();
		assertFalse(InputManager.getMappingState());
	}

	@Test
	public void flagToQuery() {
		initialise();
		assertFalse(InputManager.getQueryState());
		InputManager.queryKey();
		assertTrue(InputManager.getQueryState());

	}
}
