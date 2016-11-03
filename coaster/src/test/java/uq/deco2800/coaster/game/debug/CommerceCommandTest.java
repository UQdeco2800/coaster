package uq.deco2800.coaster.game.debug;

import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.commerce.PlayerCommerce;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;


/**
 * A test for the Commerce command.
 *
 * Created by draganmarjanovic on 21/10/2016.
 */
public class CommerceCommandTest {

	private World testWorld;
	private CommerceCommand command;
	private PlayerCommerce commerce;

	@Before
	public void setup() {
		TestHelper.load();
		command = new CommerceCommand();
		testWorld = World.getInstance();
		testWorld.debugReset();
		Player testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testPlayer.setPosition(3,3);
		testWorld.gameLoop(1);
		commerce = testWorld.getFirstPlayer().getCommerce();
	}

	// Data Tests
	@Test
	public void testHelpMessage() {
		TestHelper.load();
		String helpMessage = "DESCRIPTION:\n" +
				"Add or remove gold from different targets.\n" +
				"commerce <target> <operation> <quantity>\n" +
				"\t <target> - player, bank\n" +
				"\t <operation> - add, remove\n" +
				"\t <quantity> - integer\n" +
				"Example:\n" +
				"\tcommerce player add 100";

		assertEquals(helpMessage, command.execute("help"));
	}

	@Test
	public void testCommandName() {
		TestHelper.load();
		assertEquals("commerce", command.getCommandName());
	}

	@Test
	public void testArgumentCount() {
		TestHelper.load();
		assertEquals((Integer) 3, command.getArgumentCount());
	}

	// Argument Checking Tests
	@Test
	public void testInvalidTarget() {
		TestHelper.load();
		assertEquals("Invalid target specified.", command.execute("invalid add 5"));
	}

	@Test
	public void testInvalidOperation() {
		TestHelper.load();
		assertEquals("Invalid operation.", command.execute("bank invalid 5"));
	}

	@Test
	public void testInvalidNumber() {
		TestHelper.load();
		assertEquals("Specified amount is not a number.", command.execute("bank add invalid"));
	}

	// Key Functionality Tests
	@Test
	public void testBankAdd() {
		command.execute("bank add 5");
		assertEquals((Integer) 5, commerce.getBank().getGold());
	}

	@Test
	public void testBankRemoval() {
		commerce.getBank().addGold(5);
		command.execute("bank remove 5");
		assertEquals((Integer) 0, commerce.getBank().getGold());
	}

	@Test
	public void testPlayerAdd() {
		command.execute("player add 7");
		assertEquals(7, commerce.getGold());
	}

	@Test
	public void testPlayerRemoval() {
		commerce.addGold(9);
		command.execute("player remove 9");
		assertEquals(0, commerce.getGold());
	}

}
