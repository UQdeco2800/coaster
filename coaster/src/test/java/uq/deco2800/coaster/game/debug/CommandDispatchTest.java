package uq.deco2800.coaster.game.debug;


import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * A test for the CommandDispatch class.
 *
 * Created by draganmarjanovic on 21/10/2016.
 */
public class CommandDispatchTest {

	CommandDispatch dispatch;

	/**
	 * Initialise CommandDispatch and any other
	 * required items.
	 */
	@Before
	public void setup() {
		dispatch = new CommandDispatch();
	}

	/**
	 * Check to ensure that a message indicating an invalid command was entered
	 * is returned.
	 */
	@Test
	public void testInvalidCommandName() {
		TestHelper.load();
		assertEquals("Invalid command name.", dispatch.processCommand("gibberish"));
		assertNotEquals("Invalid command name.", dispatch.processCommand("commerce"));
	}


	/**
	 * Test to ensure that the "help" command returns
	 * a list of available commands.
	 */
	@Test
	public void testHelpCommand() {
		TestHelper.load();
		String helpMessage = dispatch.processCommand("help");
		assertTrue(helpMessage.contains("The following is a list of available commands:\n"));
		assertTrue(helpMessage.contains("For more information type\t<command_name> help"));
	}

	/**
	 * Ensures that a valid command input gets matched to
	 * the command.
	 */
	@Test
	public void testCommandMatching() {
		TestHelper.load();
		assertEquals("memes you up", dispatch.processCommand("meme_me_up_scotty help"));
	}

}
