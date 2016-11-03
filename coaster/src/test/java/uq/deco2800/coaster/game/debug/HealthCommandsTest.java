package uq.deco2800.coaster.game.debug;

import org.junit.Before;
import org.junit.Test;
import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;

public class HealthCommandsTest{
	private World testWorld;
	private HealthCommands commandHeal;
	private HealthCommands commandHealth;
	Player testPlayer;
	
	@Before
	public void setup() {
		TestHelper.load();
		commandHeal = new HealthCommands(HealthCommands.CommandType.HEAL);
		commandHealth = new HealthCommands(HealthCommands.CommandType.HEALTH);
		testWorld = World.getInstance();
		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testPlayer.setPosition(3,3);
		testWorld.gameLoop(1);
	}

	@Test
	public void testCommandHealName() {
		TestHelper.load();
		assertEquals("heal", commandHeal.getCommandName());
	}
	
	@Test
	public void testCommandHealthName() {
		TestHelper.load();
		assertEquals("health", commandHealth.getCommandName());
	}
	
	@Test
	public void testCommandHealArgCount() {
		TestHelper.load();
		assertEquals((Integer) 1, commandHeal.getArgumentCount());
	}
	
	@Test
	public void testCommandHealthArgCount() {
		TestHelper.load();
		assertEquals((Integer) 1, commandHealth.getArgumentCount());
	}
	
	@Test
	public void testHealExecute() {
		testPlayer.addHealth(-5);
		commandHeal.execute("");
		assertEquals(testPlayer.getMaxHealth(), testPlayer.getCurrentHealth());
	}

	@Test
	public void testHealthExecute() {
		commandHealth.execute("10000");
		assertEquals(testPlayer.getCurrentHealth(), testPlayer.getMaxHealth());
		assertEquals(testPlayer.getMaxHealth(), 10000);
	}
}