package uq.deco2800.coaster.game.items;

import org.junit.Assert;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.items.enchantments.EmptyEnchant;
import uq.deco2800.coaster.game.items.enchantments.Sun;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;

/**
 * Created by Hayley on 22/10/2016.
 */
public class ArmourTest {

    private World world = World.getInstance();
    final static float PRECISION_LEVEL = 0.0001f;

    /**
     * Tests that the weapon builder and setters preform properly for a simple gun
     */
    @Test
    public void testArmourGetters() {
        init();

        Armour testArmour = (Armour) new Armour.ArmourBuilder("armour1", "armour", null, "armourr", ItemType.ARMOUR)
                .damageMulti(1.4f).critDamageMulti(1.8f).rank(3).additionalHealth(4).additionalMana(5)
                .rarity(2).consumable(false).stackable(false).degradable(true).tradeable(true).build();

        assertEquals(testArmour.getID(), "armour1");
        assertEquals(testArmour.getName(), "armour");
        assertEquals(testArmour.getSprite(), null);
        assertEquals(testArmour.getDescription(), "armourr");
        assertEquals(testArmour.getType(), ItemType.ARMOUR);
        assertEquals(testArmour.getRank(),3);
        assertEquals(testArmour.isTradeable(), true);
        assertEquals(testArmour.isStackable(), false);
        assertEquals(testArmour.isConsumable(), false);
        assertEquals(testArmour.isDegradable(), true);
        assertEquals(testArmour.getDamageMulti(), 1.4f, PRECISION_LEVEL);
        assertEquals(testArmour.getCritDamageMulti(), 1.8f, PRECISION_LEVEL);
        assertEquals(testArmour.getAdditionalHealth() == 4, true);
        assertEquals(testArmour.getAdditionalMana() == 5, true);
        Assert.assertTrue(testArmour.getEnchantment() instanceof EmptyEnchant);
    }

    /**
     * Tests that a player that is given armour in the correct slot in the inventory
     * will equip the armour.
     */
    @Test
    public void testDefaultChestArmour() {
        init();
        Player player = new Player();
        world.addEntity(player);
        player.setPosition(70,32);
        world.gameLoop(1);
        player.getInventory().placeItem("Passive Inventory",1,"chestplate",1);
        world.gameLoop(1);
        Armour armour = player.getEquippedChest();
        assertEquals(ItemRegistry.getItem("chestplate"),armour );
    }

    /**
     * Tests that the enchantments are being set properly
     */
    @Test
    public void testSetEnchant() {
        init();
        Armour testArmour = (Armour) new Armour.ArmourBuilder("armour1", "armour", null, "armourr", ItemType.ARMOUR)
                .damageMulti(1.4f).critDamageMulti(1.8f).rank(3).additionalHealth(4).additionalMana(5)
                .rarity(2).consumable(false).stackable(false).degradable(true).tradeable(true).build();
        Assert.assertTrue(testArmour.getEnchantment() instanceof EmptyEnchant);
        testArmour.setEnchantment(new Sun());
        Assert.assertTrue(testArmour.getEnchantment() instanceof Sun);
    }

    /**
     * Tests that a player that is given armour in the correct slot in the inventory
     * will equip the head armour.
     */
    @Test
    public void testDefaultHeadArmour() {
        init();
        Player player = new Player();
        world.addEntity(player);
        player.setPosition(70,32);
        world.gameLoop(1);
        player.getInventory().placeItem("Passive Inventory",0,"helmet",1);
        world.gameLoop(1);
        Armour armour = player.getEquippedHead();
        assertEquals(ItemRegistry.getItem("helmet"), armour);
    }

    /**
     * Tests that a player that has no chest armour in their inventory slot
     * will have no armour equipped.
     */
    @Test
    public void testNoArmour() {
        init();
        Player player = new Player();
        world.addEntity(player);
        player.setPosition(70,32);
        world.gameLoop(1);
        player.getInventory().placeItem("Passive Inventory",1,"chestplate",1);
        world.gameLoop(1);
        Armour armour = player.getEquippedChest();
        assertEquals(armour, ItemRegistry.getItem("chestplate"));
        player.getInventory().removeItem(1, "chestplate");
        player.getInventory().clearInvSlot("Passive Inventory", 1);
        world.gameLoop(1);
        world.gameLoop(1);
        armour = player.getEquippedChest();
        assertEquals(null, armour);
    }


    /**
     * Loads sprites, so they can be accessed by tiles to create a world.
     */
    private void init() {
        TestHelper.load();
        world.debugReset();
    }
}
