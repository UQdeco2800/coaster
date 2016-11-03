package uq.deco2800.coaster.game.debug;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.items.ItemType;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;

/**
 * Tests the ItemCommand command.
 *
 * Created by draganmarjanovic on 23/10/2016.
 */
public class ItemCommandTest {

	private World testWorld;
	private ItemCommand command;
	Player testPlayer;

	@Before
	public void setup() {
		TestHelper.load();
		command = new ItemCommand();
		testWorld = World.getInstance();
		testWorld.debugReset();
		testPlayer = new Player();
		testWorld.addEntity(testPlayer);
		testPlayer.setPosition(3,3);
		testWorld.gameLoop(1);
	}

	// Getter setter tests
	@Test
	public void testCommandName() {
		TestHelper.load();
		assertEquals("item", command.getCommandName());
	}

	@Test
	public void testHelpMessage() {
		TestHelper.load();
		String helpMessage = "item list\n\tlists all the items in the registry\n" +
				"item item_id quantity\n\tdrops quantity of item_id";
		assertEquals(helpMessage, command.getHelp());
	}

	@Test
	public void testVariableArgumentCount() {
		TestHelper.load();
		assertEquals("Invalid arguments.", command.execute("a b c d e f g h i j k"));
	}

	@Test
	public void testInvalidQuantity() {
		TestHelper.load();
		assertEquals("Invalid quantity.", command.execute("pants bad_number"));
	}

	@Test
	public void testInvalidItemId() {
		TestHelper.load();
		assertEquals("Invalid item id.", command.execute("memes 5"));

	}


	@Ignore
	@Test
	public void testItemDropped() {
		TestHelper.load();
		System.out.println(command.execute("Coin1 1"));
		boolean result = false;
		List<ItemEntity> entities = World.getInstance().getItemEntities();

		for (ItemEntity entity : entities) {
			if (entity.getItem().getType() == ItemType.COIN) {
				result = true;
				break;
			}
		}

		assert result;
	}



}
