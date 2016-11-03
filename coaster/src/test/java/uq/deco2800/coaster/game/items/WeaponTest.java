package uq.deco2800.coaster.game.items;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.weapons.ProjectileType;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.weapons.CustomBullet;
import uq.deco2800.coaster.game.entities.weapons.GenericBlade;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.game.entities.weapons.GrenadeBullet;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Hayley on 28/08/2016.
 */


public class WeaponTest {

	private World world = World.getInstance();

	/**
	 * Tests that the weapon builder and setters preform properly for a simple gun
	 */
	@Test
	public void testWeaponGunGetters() {
		init();

		Weapon testWeapon = (Weapon) new Weapon.WeaponBuilder("Gun1", "Gun", null, "Bang Bang", ItemType.WEAPON)
				.damage(50).projectileType(ProjectileType.BULLET).rate(30).bulletSpeed(20)
				.rarity(2).consumable(false).stackable(false).degradable(true).tradeable(true).build();

		assertEquals(testWeapon.getID(), "Gun1");
		assertEquals(testWeapon.getName(), "Gun");
		assertEquals(testWeapon.getSprite(), null);
		assertEquals(testWeapon.getDescription(), "Bang Bang");
		assertEquals(testWeapon.getType(), ItemType.WEAPON);
		assertEquals(testWeapon.getValue(), 2);
		assertEquals(testWeapon.isTradeable(), true);
		assertEquals(testWeapon.isStackable(), false);
		assertEquals(testWeapon.isConsumable(), false);
		assertEquals(testWeapon.isDegradable(), true);
		assertEquals(testWeapon.getDamage(), 50);
		assertEquals(testWeapon.getFiringRate(), 30);
		assertEquals(testWeapon.getProjectileType(), ProjectileType.BULLET);
		assertEquals(testWeapon.getBulletSpeed(), 20);
	}

	/**
	 * Tests that the weapon builder and setters preform properly for a simple melee
	 */
	@Test
	public void testWeaponMeleeGetters() {
		init();

		Weapon testWeapon = (Weapon) new Weapon.WeaponBuilder("Melee1", "Sword", null, "Slice Slice", ItemType.WEAPON)
				.damage(50).projectileType(ProjectileType.MELEE).actionSprite(SpriteList.SHOE).swingTime(50).build();

		assertEquals(testWeapon.getID(), "Melee1");
		assertEquals(testWeapon.getName(), "Sword");
		assertEquals(testWeapon.getSprite(), null);
		assertEquals(testWeapon.getDescription(), "Slice Slice");
		assertEquals(testWeapon.getType(), ItemType.WEAPON);
		assertEquals(testWeapon.getDamage(), 50);
		assertEquals(testWeapon.getActionSprite(), SpriteList.SHOE);
		assertEquals(testWeapon.getProjectileType(), ProjectileType.MELEE);
		assertEquals(testWeapon.getSwingTime(), 50);
	}

	/**
	 * Tests that the weapon builder and setters preform properly for a simple grenade weapon
	 */
	@Test
	public void testWeaponGrenadeGetters() {
		init();

		Weapon testWeapon = (Weapon) new Weapon.WeaponBuilder("Nade1", "Launcher", null, "Bomb Bomb", ItemType.WEAPON)
				.damage(50).projectileType(ProjectileType.GRENADE).grenadeTime(4).radius(2).build();

		assertEquals(testWeapon.getID(), "Nade1");
		assertEquals(testWeapon.getName(), "Launcher");
		assertEquals(testWeapon.getSprite(), null);
		assertEquals(testWeapon.getDescription(), "Bomb Bomb");
		assertEquals(testWeapon.getType(), ItemType.WEAPON);
		assertEquals(testWeapon.getDamage(), 50);
		assertEquals(testWeapon.getGrenadeRadius(), 2);
		assertEquals(testWeapon.getGrenadeTime(), 4);
		assertEquals(testWeapon.getProjectileType(), ProjectileType.GRENADE);
	}


	/**
	 * Tests the change weapon keys 1 - 5. Currently tests that the correct guns are equipped in according to first 5
	 * guns in the activeWeapons array.
	 */
	@Test
	public void testSwitchingWeapons() {
		init();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(0));
		InputManager.setTestValue(GameAction.WEAPON_TWO, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_TWO, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(1));
		InputManager.setTestValue(GameAction.WEAPON_THREE, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_THREE, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(2));
		InputManager.setTestValue(GameAction.WEAPON_FOUR, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_FOUR, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(3));
		InputManager.setTestValue(GameAction.WEAPON_FIVE, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_FIVE, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(4));
		InputManager.setTestValue(GameAction.WEAPON_ONE, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_ONE, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(0));
	}

	/**
	 * Tests that upon calling a weapon that uses a genericBullet projectile will produce appropriate projectile entity
	 * into the world when called to attack
	 */
	@Test
	public void testGenericBulletAttack() {
		init();
		world.debugReset();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		player.getEquippedWeapon().basicAttack(player, 1, 1, 1, 1);
		world.gameLoop(1);
		assertEquals(world.getAllEntities().size(), 2);
		assertTrue(world.getAllEntities().get(0).getClass() == Player.class);
		assertTrue(world.getAllEntities().get(1).getClass() == GenericBullet.class);
	}

	/**
	 * Tests that upon calling a weapon that uses a Grenade projectile will produce appropriate projectile entity into
	 * the world when called to attack
	 */
	@Test
	public void testGrenadeAttack() {
		init();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_FIVE, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_FIVE, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(4));
		player.getEquippedWeapon().basicAttack(player, 1, 1, 1, 1);
		world.gameLoop(1);
		assertEquals(2, world.getAllEntities().size()); 
		assertEquals(Player.class, world.getAllEntities().get(0).getClass());
		assertEquals(GrenadeBullet.class, world.getAllEntities().get(1).getClass());
	}

	/**
	 * Tests that upon calling a weapon that uses a genericBlade projectile will produce appropiate projectile entity
	 * into the world when called to attack
	 */
	@Test
	public void testGenericBladeAttack() {
		init();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_TWO, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_TWO, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(1));
		player.getEquippedWeapon().basicAttack(player, 1, 1, 1, 1);
		world.gameLoop(1);
		assertEquals(world.getAllEntities().size(), 2);
		assertTrue(world.getAllEntities().get(0).getClass() == Player.class);
		assertEquals(world.getAllEntities().get(1).getClass(), GenericBlade.class);
	}

	/**
	 * Tests that upon calling a weapon that uses a customBullet attack that the appropiate projectile class is inserted
	 * in the world
	 */
	@Test
	public void testCustomBulletAttack() {
		init();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_FOUR, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.WEAPON_FOUR, false);
		world.gameLoop(1);
		assertEquals(player.getEquippedWeapon().getID(), player.getActiveWeapons().get(3));
		player.getEquippedWeapon().basicAttack(player, 1, 1, 1, 1);
		world.gameLoop(1);
		assertEquals(world.getAllEntities().size(), 2);
		assertTrue(world.getAllEntities().get(0).getClass() == Player.class);
		assertEquals(world.getAllEntities().get(1).getClass(), CustomBullet.class);
	}

	/**
	 * Tests that upon calling a weapon that uses multishot that the correct number of shots are produced
	 */
	@Test
	public void testMultishotAttack() {
		init();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		Weapon testWeapon = (Weapon) ItemRegistry.getItem("Gun7");
		testWeapon.basicAttack(player, 1, 1, 1, 1);
		world.gameLoop(1);
		assertEquals(world.getAllEntities().size(), testWeapon.getShots() + 1);
		assertTrue(world.getAllEntities().get(0).getClass() == Player.class);
		assertEquals(world.getAllEntities().get(1).getClass(), CustomBullet.class);
	}

	/**
	 * Loads sprites, so they can be accessed by tiles to create a world.
	 */
	private void init() {
		TestHelper.load();
		InputManager.clearAllValues();
		world.debugReset();
	}
}

