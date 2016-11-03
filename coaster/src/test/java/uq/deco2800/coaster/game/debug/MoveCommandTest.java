package uq.deco2800.coaster.game.debug;

import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the MoveCommand.
 *
 * Created by draganmarjanovic on 22/10/2016.
 */
public class MoveCommandTest {

	private World testWorld;
	private MoveCommand command;
	Player testPlayer;

	@Before
	public void setup() {
		TestHelper.load();
		command = new MoveCommand();
		testWorld = World.getInstance();
		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testPlayer.setPosition(3,3);
		testWorld.gameLoop(1);
	}

	// Getter setter tests
	@Test
	public void testHelpMessage() {
		TestHelper.load();
		String helpMessage = "move x y\n\tmoves the player to the specified x,y coordinates";
		assertEquals(helpMessage, command.execute("help"));
	}

	@Test
	public void testCommandName() {
		TestHelper.load();
		assertEquals("move", command.getCommandName());

	}

	@Test
	public void testArgumentCount() {
		TestHelper.load();
		assertEquals((Integer) 2, command.getArgumentCount());
	}

	// Valid data tests
	@Test
	public void testInvalidCoordinates() {
		TestHelper.load();
		assertEquals("Invalid coordinate/s.", command.execute("A 5"));
		assertEquals("Invalid coordinate/s.", command.execute("5 B"));
	}

	@Test
	public void testMovement() {
		TestHelper.load();
		testPlayer.setPosition(0, 0);
		// Test positive movement in both directions
		command.execute("5 5");
		assertEquals((int) testPlayer.getX(), 5);
		assertEquals((int) testPlayer.getY(), 5);
		// Test negative movement in both directions
		command.execute("-5 -5");
		assertEquals((int) testPlayer.getX(), -5);
		assertEquals((int) testPlayer.getY(), -5);
		command.execute("0 5");
		assertEquals((int) testPlayer.getX(), 0);
		assertEquals((int) testPlayer.getY(), 5);
		command.execute("5 5");
		assertEquals((int) testPlayer.getX(), 5);
		assertEquals((int) testPlayer.getY(), 5);
	}

	@Test
	public void testMovementMessage() {
		testPlayer.setPosition(0,0);
		String expected1 = "Before:(0,0)\nAfter:(0,0)";
		assertEquals(expected1, command.execute("0 0"));

		String expected2 = "Before:(0,0)\nAfter:(5,5)";
		assertEquals(expected2, command.execute("5 5"));

		String expected3 = "Before:(5,5)\nAfter:(-5,0)";
		assertEquals(expected3, command.execute("-5 0"));

		String expected4 = "Before:(-5,0)\nAfter:(-5,-5)";
		assertEquals(expected4, command.execute("-5 -5"));
	}


}
