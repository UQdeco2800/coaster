package uq.deco2800.coaster.game.entities.weapons;

import org.junit.Test;

import java.util.ArrayList;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

import static org.junit.Assert.assertEquals;

public class ProjectileTest {

	private World world = World.getInstance();

	@Test
	public void testAddProjectiles() {
		TestHelper.load();
		world.debugReset();
		//Add player so projectiles have an owner
		Player player = new Player();
		world.addEntity(player);
		// Add a projectiles to the world
		SuperBullet b1 = new SuperBullet(player, 0, 1, 0, 0, SpriteList.BULLET);
		GenericBullet b2 = new GenericBullet(player, 1, 0, 0, 0, SpriteList.BULLET);
		Projectile b3 = new GenericBullet(player, 0, -1, 0, 0, SpriteList.BULLET);
		CritBullet b4 = new CritBullet(player, 0, -1, 0, 0);
		GrenadeBullet b5 = new GrenadeBullet(player, 0, 1, 0, 100, 0, 0, SpriteList.GRENADE, false);
		PortalBullet b6 = new PortalBullet(player, 0, 1, 100, true);
		//Add the three types of projectiles to the world
		world.addEntity(b1);
		world.addEntity(b2);
		world.addEntity(b3);
		world.addEntity(b4);
		world.addEntity(b5);
		world.addEntity(b6);
		world.gameLoop(1);
		// Check that there 6 entities in the world and that they're all the projectiles and the player
		assert (world.getAllEntities().size() == 7);
		assert (world.getAllEntities().contains(player));
		assert (world.getAllEntities().contains(b1));
		assert (world.getAllEntities().contains(b2));
		assert (world.getAllEntities().contains(b3));
		assert (world.getAllEntities().contains(b4));
		assert (world.getAllEntities().contains(b5));
		assert (world.getAllEntities().contains(b6));
	}

	@Test
	public void testBulletDecay() {
		TestHelper.load();
		world.debugReset();
		//Add player so projectiles have an owner
		Player player = new Player();
		//make projectiles with sprites
		GenericBullet b1 = new GenericBullet(player, 1, 0, 0, 1, SpriteList.BULLET);
		GenericBullet b2 = new GenericBullet(player, 1, 0, 0, 200, SpriteList.BULLET);
		world.addEntity(b1);
		world.addEntity(b2);
		world.gameLoop(1);
		assert (world.getAllEntities().size() == 2);
		assert (world.getAllEntities().contains(b1));
		assert (world.getAllEntities().contains(b2));
		for (int i = 0; i <= 100; i++) {//gameloop until the projectiles lifetime is over
			world.gameLoop(i + 2);
		}
		assert (world.getAllEntities().size() == 0);//Make sure both bullets are gone
		assert (!world.getAllEntities().contains(b1));
		assert (!world.getAllEntities().contains(b2));
	}

	@Test
	public void testBulletHit() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		player.setPosition(0, 50);//place them on the ground
		GenericBullet b1 = new GenericBullet(player, 0, 1, 10, 0, SpriteList.BULLET);
		world.addEntity(b1);//shoot at ground -> bullet should remove itself
		assert (world.getAllEntities().size() == 1);
		assert (world.getAllEntities().contains(player));
		assert (!world.getAllEntities().contains(b1));
	}

	/**
	 * Tests that the Custom Bullet defaults function properly when given null values when constructing.
	 */
	@Test
	public void testCustomBulletDefaults() {
		TestHelper.load();
		Player player = new Player();
		CustomBullet customBullet = new CustomBullet(player, 0, 0, 1, 1
				, 0, null, 0, 0, null, 0, 0);
		assertEquals(customBullet.getTravelSprite(), SpriteList.BULLET);
		assertEquals(customBullet.getImpactSprite(), null);
		ArrayList<Float> defaultDimesions = new ArrayList<Float>();
		defaultDimesions.add(0.75f);
		defaultDimesions.add(0.25f);
		defaultDimesions.add(1f);
		defaultDimesions.add(1f);
		assertEquals(customBullet.getSpriteDimensions(), defaultDimesions);
	}

	/**
	 * Tests that the Range counter in the custom bullet decreases as expected
	 */
	@Test
	public void testCustomBulletRange() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		player.setPosition(0, 50);
		CustomBullet customBullet = new CustomBullet(player, 0, 0, 1, 1
				, 2, null, 0, 0, null, 0, 0);
		world.addEntity(customBullet);//Add bullet into world with range of 2
		world.gameLoop(1);
		assertEquals(true, world.getAllEntities().contains(customBullet));
		assertEquals(customBullet.getRange(), 2);
		world.gameLoop(1); //range counter--
		assertEquals(customBullet.getRange(), 1);
		world.gameLoop(1); //range counter--
		assertEquals(customBullet.getRange(), 0);
		assertEquals(true, world.getAllEntities().contains(player));
		assertEquals(false, world.getAllEntities().contains(customBullet));
	}
	
	/**
	 * A test to see if the portals are behaving properly - checks their creation and most functionality.
	 */
	@Test
	public void testPortalBullet() {
		TestHelper.load();
		world.debugReset();
		Player player = new Player();
		world.addEntity(player);
		world.gameLoop(1);
		player.setPosition(0, 50);
		PortalBullet portal1 = new PortalBullet(player, 0, 0, 0, true);
		player.setPortal(portal1, true);
		
		world.addEntity(portal1);
		world.gameLoop(1);
		assert (world.getAllEntities().size() == 2);
		assertEquals(player.getPortal(true), portal1);
				
				
		//Collision checking
		assert(portal1.collides() == false);
		
		/* this isn't working yet...
		player.setPosition(20, 50);
		world.gameLoop(1);
		PortalBullet portal2 = new PortalBullet(player, 0, 0, 0, false);
		world.addEntity(portal2);
		player.setPortal(portal2, false);
		portal2.setHit(true);
		player.teleportPortal(false);
		world.gameLoop(1);
		assert(player.getX() == portal1.getX());
		assert(player.getY() == portal1.getY());*/
	}
}
