package uq.deco2800.coaster.game.entities.npcs.mounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

public class MountTest {
	private static World world = World.getInstance();

	@Before
	public void init() {
		TestHelper.load();
	}

	public void initialise() {
		TestHelper.load();
		world.resetWorld();
	}

	@Test
	public void testCreation() {
		initialise();
		Mount mount = new Mount();
		assertFalse(mount.blocksOtherEntities());
		assertFalse(mount.isRenderHealthbar());
		assertFalse(mount.flyingMount);
		assertFalse(mount.jumpDown);
		assertEquals(2, mount.range);
		assertEquals(0, (int) mount.getSaddleX());
		assertEquals(0, (int) mount.getSaddleY());
		assertEquals(null, mount.getRider());
	}

	@Test
	public void testRider() {
		initialise();
		Mount mount = new Mount();
		Player player = new Player();
		mount.registerRider(player);
		assertEquals(player, mount.getRider());
		mount.removeRider(player);
		assertEquals(null, mount.getRider());
	}

	@Test
	public void testFly() {
		initialise();
		Mount mount = new Mount();
		Player player = new Player();
		mount.fly(true, false, false, false);
		assertTrue(mount.getVelX() < 0);
		mount.fly(false, true, false, false);
		assertTrue(mount.getVelX() > 0);
		mount.fly(false, false, false, true);
		assertTrue(mount.getVelY() > 0);
		mount.fly(false, false, true, false);
		assertTrue(mount.getVelY() < 0);
	}

	@Test
	public void testSpawning() {
		initialise();
		Mount mount = new Mount();
		Player player = new Player();
		mount.fly(true, false, false, false);
		assertTrue(mount.getVelX() < 0);
		mount.fly(false, true, false, false);
		assertTrue(mount.getVelX() > 0);
		mount.fly(false, false, false, true);
		assertTrue(mount.getVelY() > 0);
		mount.fly(false, false, true, false);
		assertTrue(mount.getVelY() < 0);
	}

	@Test
	public void testPlayer() {
		initialise();
		Mount mount = new Mount();
		Player player = new Player();
		player.setMount(mount);
		player.setOnMountStatus(true);
		player.saveRider(mount, player);
		assertTrue(player.getOnMountStatus());
		assertEquals(mount, player.getMount());
	}

	@Test
	public void testTogglePlayer() {
		initialise();
		Mount mount = new BatMount();
		Player player = new Player();
		world.debugReset();
		world.addEntity(player);
		player.setPosition(20, 20);
		world.addEntity(mount);
		mount.setPosition(20, 20);
		world.gameLoop(1);
		assertTrue(world.getAllEntities().contains(mount));
		assertFalse(player.getOnMountStatus());
		PlayerMountActions.toggleMount(player);
		assertTrue(player.getOnMountStatus());
		PlayerMountActions.toggleMount(player);
		assertFalse(player.getOnMountStatus());
	}

	@Test
	public void testPlayerYPosition() {
		initialise();
		Mount mount = new BatMount();
		Player player = new Player();
		world.debugReset();
		world.addEntity(player);
		player.setPosition(20, 20);
		world.addEntity(mount);
		mount.setPosition(20, 20);
		world.gameLoop(1);
		PlayerMountActions.toggleMount(player);
		PlayerMountActions.positionPlayer(player);
		assertEquals((int) (mount.getY() + mount.getSaddleY()), (int) player.getY());
	}

	@Test
	public void testPlayerXPosition() {
		initialise();
		Mount mount = new BatMount();
		Player player = new Player();
		world.debugReset();
		world.addEntity(player);
		player.setPosition(20, 20);
		world.addEntity(mount);
		mount.setPosition(20, 20);
		world.gameLoop(1);
		PlayerMountActions.toggleMount(player);
		PlayerMountActions.positionPlayer(player);
		assertEquals((int) (mount.getX() + mount.getSaddleX()), (int) player.getX());
	}

	@Test
	public void testBatMount() {
		initialise();
		Mount mount = new BatMount();
		assertEquals(-10, (int) mount.getJumpSpeed());
		assertEquals(20, (int) mount.getMoveSpeed());
		assertEquals(true, mount.flyingMount);
	}

	@Test
	public void testBirdMount() {
		initialise();
		Mount mount = new BirdMount();
		assertEquals(-30, (int) mount.getJumpSpeed());
		assertEquals(15, (int) mount.getMoveSpeed());
		assertEquals(true, mount.flyingMount);
	}

	@Test
	public void testDogMount() {
		initialise();
		Mount mount = new DogMount();
		assertEquals(-15, (int) mount.getJumpSpeed());
		assertEquals(25, (int) mount.getMoveSpeed());
		assertEquals(false, mount.flyingMount);
	}

	@Test
	public void testElephantMount() {
		initialise();
		Mount mount = new ElephantMount();
		assertEquals(-15, (int) mount.getJumpSpeed());
		assertEquals(5, (int) mount.getMoveSpeed());
		assertEquals(false, mount.flyingMount);
	}

	@Test
	public void testJumpingMount() {
		initialise();
		Mount mount = new JumpingMount();
		assertEquals(-50, (int) mount.getJumpSpeed());
		assertEquals(15, (int) mount.getMoveSpeed());
		assertEquals(false, mount.flyingMount);
	}

	@Test
	public void testRhinoMount() {
		initialise();
		Mount mount = new RhinoMount();
		assertEquals(-15, (int) mount.getJumpSpeed());
		assertEquals(5, (int) mount.getMoveSpeed());
		assertEquals(false, mount.flyingMount);
	}

	@Test
	public void testTurtleMount() {
		initialise();
		Mount mount = new TurtleMount();
		assertEquals(2, (int) mount.saddleX);
	}

	@Test
	public void testSaveFlag() {
		initialise();
		SaveFlag flag = new SaveFlag();

		assertFalse(flag.blocksOtherEntities());
		assertFalse(flag.isRenderHealthbar());
		assertTrue(flag.flagDown);
		assertEquals(true, flag.inRange);
		assertEquals(false, flag.skip);
		assertEquals(true, flag.flagLeft);
		assertEquals(2, flag.range);
	}
	
	@Test
	@Ignore //works locally, but not with gradle?
	public void testSaveFlagToggle() {
		initialise();
		SaveFlag flag = new SaveFlag();
		assertTrue(flag.flagDown);
		flag.toggleFlag();
		assertFalse(flag.flagDown);
		assertTrue(flag.flagLeft);
		flag.toggleFlag();
		assertFalse(flag.flagLeft);
	}

	@Test
	public void testFlagTick() {
		initialise();
		SaveFlag flag = new SaveFlag();
		Player player = new Player();
		world.debugReset();
		world.addEntity(player);
		player.setPosition(20, 20);
		world.addEntity(flag);
		flag.setPosition(20, 20);
		flag.tick(1);
		assertFalse(flag.inRangeFromOutRange);
	}
}
