package uq.deco2800.coaster.game.entities;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.world.World;

public class PowerUpTest {

	private World world = World.getInstance();

	@Test
	public void testAddPowerups() {
		TestHelper.load();
		world.debugReset();
		Item healthItem = ItemRegistry.getItem("Health1");
		Item manaItem = ItemRegistry.getItem("Mana1");
		Item shieldItem = ItemRegistry.getItem("Shield1");
		Item speedItem = ItemRegistry.getItem("Speed1");
		Item weaponItem = ItemRegistry.getItem("Weapon1");

		// Add Powerups to the world
		PowerUp p1 = new PowerUp(healthItem, "health", 25, 15, 10000);
		PowerUp p2 = new PowerUp(manaItem, "mana", 50, 15, 10000);
		PowerUp p3 = new PowerUp(shieldItem, "shield", 1f, 15, 10000);
		PowerUp p4 = new PowerUp(speedItem, "speed", 1.5f, 1000, 10000);
		PowerUp p5 = new PowerUp(weaponItem, "weapon", 2, 1500, 10000);

		world.addEntity(p1);
		world.addEntity(p2);
		world.addEntity(p3);
		world.addEntity(p4);
		world.addEntity(p5);

		world.gameLoop(1);

		// Check that there are 5 entities in the world
		assert (world.getAllEntities().size() == 5);
		// Check the world for the entities added
		assert (world.getAllEntities().contains(p1));
		assert (world.getAllEntities().contains(p2));
		assert (world.getAllEntities().contains(p3));
		assert (world.getAllEntities().contains(p4));
		assert (world.getAllEntities().contains(p5));

		world.gameLoop(15000);
		assert (!world.getAllEntities().contains(p1));
		assert (!world.getAllEntities().contains(p2));
		assert (!world.getAllEntities().contains(p3));
		assert (!world.getAllEntities().contains(p4));
		assert (!world.getAllEntities().contains(p5));

	}

	@Test
	public void testHealthPowerUp() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		Item healthItem = ItemRegistry.getItem("Health1");
		PowerUp h1 = new PowerUp(healthItem, "health", 20, 15, 10000);

		world.addEntity(player);
		world.addEntity(h1);

		world.gameLoop(1);

		player.setPosition(0, 50); // Place player on ground
		player.addHealth(-25); // Remove 25 HP from player

		h1.setPosition(0, 50); // Place h1 at player

		assert (world.getAllEntities().contains(h1)); // h1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes h1, gets 20 HP
		world.gameLoop(1); // Move time so player consumes h1, gets 20 HP

		player.getMaxHealth();
		player.getCurrentHealth();

		assert (!world.getAllEntities().contains(h1)); // h1 should be consumed
		// assert (hpDiff == 5); // player health difference should be 5
		assert (h1.getModifier() == 20); // Get Health Change

	}

	@Test
	public void testHealthPowerUpCompanion() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		Item healthItem = ItemRegistry.getItem("Health1");
		PowerUp h1 = new PowerUp(healthItem, "health", 20, 15, 10000);
		world.addEntity(player);
		world.addEntity(testCompanion);
		world.addEntity(h1);

		world.gameLoop(1);
		player.setPosition(0, 45);
		testCompanion.setPosition(0, 50); // Place player on ground
		player.addHealth(-25); // Remove 25 HP from player

		h1.setPosition(0, 50); // Place h1 at companion

		assert (world.getAllEntities().contains(h1)); // h1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes h1, gets 20 HP
		world.gameLoop(1); // Move time so player consumes h1, gets 20 HP

		player.getMaxHealth();
		player.getCurrentHealth();

		assert (!world.getAllEntities().contains(h1)); // h1 should be consumed
		// assert (hpDiff == 5); // player health difference should be 5
		assert (h1.getModifier() == 20); // Get Health Change

	}

	@Test
	public void testManaPowerUp() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		Item manaItem = ItemRegistry.getItem("Mana1");
		PowerUp m1 = new PowerUp(manaItem, "mana", 20, 15, 10000);

		world.addEntity(player);
		world.addEntity(m1);

		world.gameLoop(1);

		player.setPosition(0, 50); // Place player on ground
		player.addMana(-25); // Remove 25 MP from player

		m1.setPosition(0, 50); // Place m1 at player

		assert (world.getAllEntities().contains(m1)); // m1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes m1, gets 20 MP
		world.gameLoop(1); // Move time so player consumes m1, gets 20 MP

		player.getMaxMana();
		player.getCurrentMana();

		assert (!world.getAllEntities().contains(m1)); // m1 should be consumed

		assert (player.getCurrentMana() > 20);

	}

	@Test
	public void testManaPowerUpCompanion() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		Item manaItem = ItemRegistry.getItem("Mana1");
		PowerUp m1 = new PowerUp(manaItem, "mana", 20, 15, 10000);
		world.addEntity(testCompanion);
		world.addEntity(player);
		world.addEntity(m1);

		world.gameLoop(1);

		player.setPosition(0, 40); // Place player on ground
		testCompanion.setPosition(0, 50);
		player.addMana(-25); // Remove 25 MP from player

		m1.setPosition(0, 50); // Place m1 at player

		assert (world.getAllEntities().contains(m1)); // m1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes m1, gets 20 MP
		world.gameLoop(1); // Move time so player consumes m1, gets 20 MP

		player.getMaxMana();
		player.getCurrentMana();

		assert (!world.getAllEntities().contains(m1)); // m1 should be consumed

		assert (player.getCurrentMana() > 20);

	}

	@Test
	public void testShieldPowerUp() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		Item shieldItem = ItemRegistry.getItem("Shield1");
		PowerUp s1 = new PowerUp(shieldItem, "shield", 1f, 100, 10000);

		world.addEntity(player);
		world.addEntity(s1);

		world.gameLoop(1);

		player.setPosition(0, 50); // Place player on ground
		s1.setPosition(0, 50); // Place s1 at player

		assert (world.getAllEntities().contains(s1)); // s1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes s1, gets shielded
		world.gameLoop(1); // Move time so player consumes s1, gets shielded

		assert (!world.getAllEntities().contains(s1)); // s1 should be consumed
		// Test that player has shielded status
		world.gameLoop(40);
		assert (player.getShielded());
		world.gameLoop(60);

		assert (!player.getShielded()); // player should now have no shield

	}

	@Test
	public void testShieldPowerUpCompanion() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		Item shieldItem = ItemRegistry.getItem("Shield1");
		PowerUp s1 = new PowerUp(shieldItem, "shield", 1f, 100, 10000);
		world.addEntity(testCompanion);
		world.addEntity(player);
		world.addEntity(s1);

		world.gameLoop(1);
		testCompanion.setPosition(0, 55);
		player.setPosition(0, 50); // Place player on ground
		s1.setPosition(0, 55); // Place s1 at player

		assert (world.getAllEntities().contains(s1)); // s1 should be in the
		// world

		world.gameLoop(2); // Move time so player consumes s1, gets shielded
		world.gameLoop(2); // Move time so player consumes s1, gets shielded

		assert (!world.getAllEntities().contains(s1)); // s1 should be consumed
		// Test that player has shielded status
		world.gameLoop(40);
		assert (player.getShielded());
		world.gameLoop(60);

		assert (!player.getShielded()); // player should now have no shield

	}

	@Test
	public void testSpeedPowerUp() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		Item speedItem = ItemRegistry.getItem("Speed1");
		PowerUp s1 = new PowerUp(speedItem, "speed", 2, 250, 10000);

		float startingSpeed = player.getMoveSpeed();

		world.addEntity(player);
		world.addEntity(s1);

		world.gameLoop(1);

		player.setPosition(0, 50); // Place player on ground
		s1.setPosition(0, 50); // Place s1 at player

		assert (world.getAllEntities().contains(s1)); // s1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes s1, gains Speed
		world.gameLoop(1); // Move time so player consumes s1, gains Speed

		assert (!world.getAllEntities().contains(s1)); // s1 should be consumed
		// Test that player has greater speed than before
		world.gameLoop(10);
		// allow speedup to happen
		assert (startingSpeed < player.getMoveSpeed());

		world.gameLoop(260);
		// player current speed = starting speed
		assert (startingSpeed - player.getMoveSpeed() < 0.001f);

	}

	@Test
	public void testSpeedPowerUpCompanion() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		CompanionNPC testCompanion = new CompanionNPC(player);
		Item speedItem = ItemRegistry.getItem("Speed1");
		PowerUp s1 = new PowerUp(speedItem, "speed", 2, 250, 10000);

		float startingSpeed = player.getMoveSpeed();
		world.addEntity(testCompanion);
		world.addEntity(player);
		world.addEntity(s1);

		world.gameLoop(1);
		testCompanion.setPosition(0, 55);
		player.setPosition(0, 50); // Place player on ground
		s1.setPosition(0, 55); // Place s1 at player

		assert (world.getAllEntities().contains(s1)); // s1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes s1, gains Speed
		world.gameLoop(1); // Move time so player consumes s1, gains Speed

		assert (!world.getAllEntities().contains(s1)); // s1 should be consumed
		// Test that player has greater speed than before
		world.gameLoop(10);
		// allow speedup to happen
		assert (startingSpeed < player.getMoveSpeed());

		world.gameLoop(260);
		// player current speed = starting speed
		assert (startingSpeed - player.getMoveSpeed() < 0.001f);

	}

	@Test
	public void testWeaponPowerUp() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		new PlayerStats();
		Item weaponItem = ItemRegistry.getItem("Weapon1");
		PowerUp w1 = new PowerUp(weaponItem, "weapon", 2, 250, 10000);

		int startingDamage = player.getBaseDamage();

		world.addEntity(player);
		world.addEntity(w1);

		world.gameLoop(1);

		player.setPosition(0, 50); // Place player on ground
		w1.setPosition(0, 50); // Place s1 at player

		assert (world.getAllEntities().contains(w1)); // w1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes w1, gets powerup
		world.gameLoop(1); // Move time so player consumes w1, gets powerup

		assert (!world.getAllEntities().contains(w1)); // s1 should be consumed
		world.gameLoop(100);
		assert (player.getBaseDamage() > startingDamage);
		// TODO Test that after time has past stats are reverted
		world.gameLoop(300);
		assert (player.getBaseDamage() == startingDamage);

	}

	@Test
	public void testWeaponPowerUpCompanion() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		new PlayerStats();
		Item weaponItem = ItemRegistry.getItem("Weapon1");
		CompanionNPC testCompanion = new CompanionNPC(player);
		PowerUp w1 = new PowerUp(weaponItem, "weapon", 2, 250, 10000);

		int startingDamage = player.getBaseDamage();
		world.addEntity(testCompanion);
		world.addEntity(player);
		world.addEntity(w1);

		world.gameLoop(1);
		testCompanion.setPosition(0, 55);
		player.setPosition(0, 50); // Place player on ground
		w1.setPosition(0, 55); // Place s1 at player

		assert (world.getAllEntities().contains(w1)); // w1 should be in the
		// world

		world.gameLoop(1); // Move time so player consumes w1, gets powerup
		world.gameLoop(1); // Move time so player consumes w1, gets powerup

		assert (!world.getAllEntities().contains(w1)); // s1 should be consumed
		world.gameLoop(100);
		assert (player.getBaseDamage() > startingDamage);
		// TODO Test that after time has past stats are reverted
		world.gameLoop(300);
		assert (player.getBaseDamage() == startingDamage);

	}
}
