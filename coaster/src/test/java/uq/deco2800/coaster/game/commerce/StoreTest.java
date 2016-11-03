package uq.deco2800.coaster.game.commerce;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.items.ItemStore;
import uq.deco2800.coaster.game.world.World;

/**
 * A set of tests for the Store class. <p> Created by draganmarjanovic on 31/08/2016.
 */
public class StoreTest {

	public World testWorld;
	public Player testPlayer;
	Store testStore;
	List<ItemStore> storeStock;

	/**
	 * Returns the first item in the store.
	 *
	 * @return the ID of a store item.
	 */
	private ItemStore getStoreItem(Integer itemIndex) {
		return storeStock.get(itemIndex);
	}

	@Ignore
	@Before
	public void initialise() {
		TestHelper.load();
		testWorld = World.getInstance();
		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testPlayer.setPosition(3, 3);
		testWorld.gameLoop(1);
		testStore = new Store(StoreType.GENERAL);
		storeStock = testStore.getItems();
	}

	/**
	 * Tests purchase FROM a store.
	 */
	@Ignore
	@Test
	public void testPurchase() {
//		Integer firstItem = 0;
//		getStoreItem(firstItem);
//		Integer itemStock = getStoreItem(firstItem).getStock();
//		assertEquals(testStore.buyItem(firstItem, itemStock), itemStock);
//		itemStock = storeStock.get(firstItem).getStock();
//		assertEquals(itemStock, (Integer) 0);
		//  TODO: Rewrite this test as a store might not have items in it due to rand()
	}

}
