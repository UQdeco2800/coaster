package uq.deco2800.coaster.game.debug;

import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertEquals;


/**
 * A set of tests for the Command class.
 *
 * Created by draganmarjanovic on 21/10/2016.
 */
public class CommandTest {

	private TestCommand testCommand;

	/**
	 * A command solely for testing.
	 */
	private class TestCommand extends Command {

		/**
		 * Initialise the Command properties / data.
		 */
		TestCommand() {
			commandName = "test";
			argumentCount = 2;
			help = "the help text";
		}

		/**
		 * Split the arguments separating them with a newline.
		 * @param arguments a list of arguments which are passed to the function.
		 * @return arguments printed separated by a newline character.
		 */
		@Override
		String execute(Object[] arguments) {
			return arguments[0] + "\n" + arguments[1];
		}
	}

	/**
	 * Initialise any helpers and classes needed for the tests.
	 */
	@Before
	public void setup() {
		testCommand = new TestCommand();
	}

	/**
	 * Check to see if the help text is returned given
	 * "help" as an argument to the command.
	 */
	@Test
	public void testCommandHelp () {
		TestHelper.load();
		assertEquals("the help text", testCommand.execute("help"));
	}

	/**
	 * Ensure that the Command returns an error if an incorrect
	 * number of arguments is passed.
	 */
	@Test
	public void testInvalidArgumentCount() {
		TestHelper.load();
		assertEquals("Invalid argument count.\n", testCommand.execute("memes dreams deco2800"));
	}

	/**
	 * Ensure that the command name is returned with the getter.
	 */
	@Test
	public void testGetCommandName() {
		TestHelper.load();
		assertEquals("test", testCommand.getCommandName());
	}

	/**
	 * Ensure that the splitting functionality is working as
	 * expected in the base Command class.
	 */
	@Test
	public void testArgumentSplitting() {
		TestHelper.load();
		assertEquals("first\nsecond", testCommand.execute("first second"));
	}

}
