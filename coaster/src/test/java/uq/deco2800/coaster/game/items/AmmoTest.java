package uq.deco2800.coaster.game.items;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.QuantityDrop;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

/**
 * Created by Hayley on 21/10/2016.
 */
public class AmmoTest {

    private World world;
    private static WorldTiles standardTiles = TestHelper.getMobilityTestTiles();

    /**
     * Tests that a player that intersects with an AmmoDrop entity will gain ammo in their
     * inventory.
     */
    @Test
    public void testPickUpAmmo() {
        init();
        Player player = new Player();
        world.addEntity(player);
        player.setPosition(70,32);
        world.gameLoop(1);
        int initialAmmoCount = player.getInventory().getAmount("ammo");
        Item ammo = ItemRegistry.getItem("ammo");
        QuantityDrop ammoDrop = new QuantityDrop(ammo, 10);
        world.addEntity(ammoDrop);
        ammoDrop.setPosition(70,32);
        world.gameLoop(1);
        world.gameLoop(1);
        world.gameLoop(1);
        world.gameLoop(1);
        //Let ammo fall onto player
        assertEquals(initialAmmoCount+10, player.getInventory().getAmount("ammo"));
    }

    /**
     * Tests that a player that intersects with an ExAmmoDrop entity will gain Exammo in their
     * inventory.
     */
    @Test
    public void testPickUpExAmmo() {
        init();
        Player player = new Player();
        world.addEntity(player);
        player.setPosition(70,32);
        world.gameLoop(1);
        int initialAmmoCount = player.getInventory().getAmount("ex_ammo");
        Item exammo = ItemRegistry.getItem("ex_ammo");
        QuantityDrop exAmmoDrop = new QuantityDrop(exammo, 5);
        world.addEntity(exAmmoDrop);
        exAmmoDrop.setPosition(70,32);
        world.gameLoop(1);
        world.gameLoop(1);
        world.gameLoop(1);
        world.gameLoop(1);
        //Let ammo fall onto player
        assertEquals(initialAmmoCount+5, player.getInventory().getAmount("ex_ammo"));
    }

    /**
     * Tests that a player that fires with no bullets will not create a projectile
     * entity.
	 */
	@Test
	public void testFiringWithNoAmmo() {
		init();
		Player p = new Player();
		world.addEntity(p);
		world.gameLoop(1);
		p.setPosition(70, 32);
		world.setTiles(standardTiles);
		world.gameLoop(1);
		int initialAmmoCount = p.getInventory().getAmount("ammo");
		InputManager.setTestValue(GameAction.BASIC_ATTACK, true);
		world.gameLoop(1);
		p.getInventory().removeItem(initialAmmoCount, "ammo");
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.BASIC_ATTACK, true);
		world.gameLoop(1);
		InputManager.setTestValue(GameAction.BASIC_ATTACK, false);
		world.gameLoop(1);
		world.gameLoop(1);
		assertEquals(1, world.getAllEntities().size());
		assertEquals(0, p.getInventory().getAmount("ammo"));
	}

    /**
     * Loads sprites, so they can be accessed by tiles to create a world.
     */
    private void init() {
        TestHelper.load();
		InputManager.clearAllValues();
		world = World.getInstance();
        world.debugReset();
    }


}
