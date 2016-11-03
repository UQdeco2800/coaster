package uq.deco2800.coaster.game.debug;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.world.World;

/**
 * A debug command to handle item related functionality
 * such as showing possible items and allowing them to
 * be dropped.
 *
 * Created by draganmarjanovic on 23/10/2016.
 */
public class ItemCommand extends Command {

	public ItemCommand() {
		commandName = "item";
		argumentCount = -1;
		help = "item list\n\tlists all the items in the registry\n" +
				"item item_id quantity\n\tdrops quantity of item_id";
	}

	/**
	 * Process the arguments and handle them accordingly.
	 *
	 * @param arguments a list of arguments which are passed to the function.
	 * @return the corresponding response to the request.
	 */
	@Override
	String execute(Object[] arguments) {
		if (arguments.length == 1 && "list".equals(arguments[0])) {
			return listItems();
		} else if (arguments.length == 2) {
			// item id, quantity
			Integer quantity;
			try {
				quantity = Integer.parseInt((String) arguments[1]);
				return dropItem((String) arguments[0], quantity);
			} catch (NumberFormatException e) {
				return "Invalid quantity.";
			}
		} else {
			return "Invalid arguments.";
		}
	}


	/**
	 * Drops the given quantity of the given item.
	 *
	 * @param itemId the item to drop.
	 * @param quantity the quantity to drop.
	 * @return how much of what was dropped + drops the item.
	 */
	private String dropItem(String itemId, int quantity) {
		Player player = World.getInstance().getFirstPlayer();
		Item dropItem = ItemRegistry.getItem(itemId);

		if (dropItem == null) {
			return "Invalid item id.";
		}

		for (int i = 0; i < quantity; i++) {
			ItemDrop.drop(dropItem, player.getX()+2, player.getY()+2);
		}

		return "Dropped " + quantity + " " + dropItem.getName() + "(" + dropItem.getID() + ")";
	}

	/**
	 * Lists all the available items and their key info.
	 *
	 * @return a list of all items in the registry.
	 */
	private String listItems() {
		String result = "ID:\t\tRarity:\t\tType:\n";
		for (String key : ItemRegistry.itemList()) {
			Item item = ItemRegistry.getItem(key);
			result += item.getID() + "\t\t" + item.getValue() + "\t\t" + item.getType() + "\n";
		}
		return result;
	}

}
