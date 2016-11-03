package uq.deco2800.coaster.game.debug;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests the QuitCommand command.
 * 
 * Created by jamesthompson275 on 24/10/2016
 *
 */
public class QuitCommandTest {

	private QuitCommands commandQuit;
	private QuitCommands commandExit;
	private QuitCommands commandClose;
	Player testPlayer;

	@Before
	public void setup() {
		TestHelper.load();
		commandQuit = new QuitCommands(QuitCommands.CommandType.QUIT);
		commandExit = new QuitCommands(QuitCommands.CommandType.EXIT);
		commandClose = new QuitCommands(QuitCommands.CommandType.CLOSE);
	}
	
	@Test
	public void testCommandQuitName() {
		TestHelper.load();
		assertEquals("quit", commandQuit.getCommandName());
	}
	
	@Test
	public void testCommandCloseName() {
		TestHelper.load();
		assertEquals("close", commandClose.getCommandName());
	}
	
	@Test
	public void testCommandExitName() {
		TestHelper.load();
		assertEquals("exit", commandExit.getCommandName());
	}
	
	@Test
	public void testCommandQuitArgCount() {
		TestHelper.load();
		assertEquals((Integer) 1, commandQuit.getArgumentCount());
	}
	
	@Test
	public void testCommandCloseArgCount() {
		TestHelper.load();
		assertEquals((Integer) 1, commandClose.getArgumentCount());
	}
	
	@Test
	public void testCommandExitArgCount() {
		TestHelper.load();
		assertEquals((Integer) 1, commandExit.getArgumentCount());
	}
}