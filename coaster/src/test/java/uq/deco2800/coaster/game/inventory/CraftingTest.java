package uq.deco2800.coaster.game.inventory;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertTrue;


public class CraftingTest {
	
	/**
	 * Test the initialization of the crafting
	 */
	@Test
	public void checkGetCraftingProduct() {
		TestHelper.load();
		Crafting craft = new Crafting();
		assertTrue(craft.getCraftingProduct("glass","crystal","empty","empty").equals("potion"));
		assertTrue(craft.getCraftingProduct("crystal","glass","empty","empty").equals("invalid recipe"));
		assertTrue(craft.getCraftingProduct("crystal","empty","empty","glass").equals("invalid recipe"));
		assertTrue(craft.getCraftingProduct("potion1","firestone","empty","empty").equals("invalid recipe"));
	}

}
