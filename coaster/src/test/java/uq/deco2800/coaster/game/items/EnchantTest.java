package uq.deco2800.coaster.game.items;

import org.junit.Test;
import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.items.enchantments.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jeevan on 23/10/16.
 *
 */
public class EnchantTest {

	final static float PRECISION_LEVEL = 0.0001f;

	/**
	 * Test that specific enchants return the correct value
	 */
	@Test
	public void checkEnchants() {
		TestHelper.load();
		
		//Test Empty
		EmptyEnchant emptyE = new EmptyEnchant();
		assertTrue(emptyE.getDamageMulti() == 0f);
		assertTrue(emptyE.getCritDamageMulti() == 0f);
		assertTrue(emptyE.getAdditionalHealth() == 0);
		assertTrue(emptyE.getAdditionalMana() == 0);
		assertTrue(emptyE.getDescription().equals(""));
		
		//Test Fire
		Fire fire = new Fire();
		assertTrue(fire.getDamageMulti() == 0f);
		assertTrue(fire.getCritDamageMulti() == 1f);
		assertTrue(fire.getAdditionalHealth() == 0);
		assertTrue(fire.getAdditionalMana() == 0);
		assertTrue(fire.getDescription().equals(", now hot"));
		
		//Test Grass
		Grass grass = new Grass();
		assertTrue(grass.getDamageMulti() == 0f);
		assertTrue(grass.getCritDamageMulti() == 0f);
		assertTrue(grass.getAdditionalHealth() == 10);
		assertTrue(grass.getAdditionalMana() == 0);
		assertTrue(grass.getDescription().equals(", now environmentally friendly"));
		
		//Test Moon
		Moon moon = new Moon();
		assertTrue(moon.getDamageMulti() == 0f);
		assertTrue(moon.getCritDamageMulti() == 5f);
		assertTrue(moon.getAdditionalHealth() == 0);
		assertTrue(moon.getAdditionalMana() == 20);
		assertTrue(moon.getDescription().equals(", now celestially COOL"));
		
		//Test Stone
		Stone stone = new Stone();
		assertTrue(stone.getDamageMulti() == 1f);
		assertTrue(stone.getCritDamageMulti() == 0f);
		assertTrue(stone.getAdditionalHealth() == 0);
		assertTrue(stone.getAdditionalMana() == 0);
		assertTrue(stone.getDescription().equals(", now rock hard"));
		
		//Test Sun
		Sun sun = new Sun();
		assertTrue(sun.getDamageMulti() == 5f);
		assertTrue(sun.getCritDamageMulti() == 0f);
		assertTrue(sun.getAdditionalHealth() == 20);
		assertTrue(sun.getAdditionalMana() == 0);
		assertTrue(sun.getDescription().equals(", now celestially HOT"));
		
		//Test Water
		Water water = new Water();
		assertTrue(water.getDamageMulti() == 0f);
		assertTrue(water.getCritDamageMulti() == 0f);
		assertTrue(water.getAdditionalHealth() == 0);
		assertTrue(water.getAdditionalMana() == 10);
		assertTrue(water.getDescription().equals(", now cool to the touch"));
	}

	/**
	 * Test that specific enchants return the correct value
	 */
	@Test
	public void checkEnhanting() {
		TestHelper.load();
		Armour testArmour = (Armour) new Armour.ArmourBuilder("armour1", "armour", null, "armourr", ItemType.ARMOUR)
				.damageMulti(1.4f).critDamageMulti(1.8f).rank(3)
				.rarity(2).consumable(false).stackable(false).degradable(true).tradeable(true).build();
		assertTrue("armourr".equals(testArmour.getDescription()));
		assertEquals(testArmour.getDamageMulti(), 1.4f, PRECISION_LEVEL);
		assertEquals(testArmour.getCritDamageMulti(), 1.8f, PRECISION_LEVEL);
		assertEquals(testArmour.getAdditionalHealth() == 0, true);
		assertEquals(testArmour.getAdditionalMana() == 0, true);

		// Enable Enchant
		testArmour.setEnchantment(new Sun());
		assertTrue("armourr, now celestially HOT".equals(testArmour.getDescription()));
		assertEquals(testArmour.getDamageMulti(), 1.4f + 5f, PRECISION_LEVEL);
		assertEquals(testArmour.getCritDamageMulti(), 1.8f, PRECISION_LEVEL);
		assertEquals(testArmour.getAdditionalHealth() == 20, true);
		assertEquals(testArmour.getAdditionalMana() == 0, true);

		// Enable different Enchant
		testArmour.setEnchantment(new Water());
		assertTrue("armourr, now cool to the touch".equals(testArmour.getDescription()));
		assertEquals(testArmour.getDamageMulti(), 1.4f, PRECISION_LEVEL);
		assertEquals(testArmour.getCritDamageMulti(), 1.8f, PRECISION_LEVEL);
		assertEquals(testArmour.getAdditionalHealth() == 0, true);
		assertEquals(testArmour.getAdditionalMana() == 10, true);

		// Remove Enchants
		testArmour.setEnchantment(new EmptyEnchant());
		assertTrue("armourr".equals(testArmour.getDescription()));
		assertEquals(testArmour.getDamageMulti(), 1.4f, PRECISION_LEVEL);
		assertEquals(testArmour.getCritDamageMulti(), 1.8f, PRECISION_LEVEL);
		assertEquals(testArmour.getAdditionalHealth() == 0, true);
		assertEquals(testArmour.getAdditionalMana() == 0, true);

		// Add a new enchant
		testArmour.setEnchantment(new Moon());
		assertTrue("armourr, now celestially COOL".equals(testArmour.getDescription()));
		assertEquals(testArmour.getDamageMulti(), 1.4f, PRECISION_LEVEL);
		assertEquals(testArmour.getCritDamageMulti(), 1.8f + 5f, PRECISION_LEVEL);
		assertEquals(testArmour.getAdditionalHealth() == 0, true);
		assertEquals(testArmour.getAdditionalMana() == 20, true);
	}
}
