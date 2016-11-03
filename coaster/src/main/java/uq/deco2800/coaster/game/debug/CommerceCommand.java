package uq.deco2800.coaster.game.debug;

import uq.deco2800.coaster.game.commerce.PlayerCommerce;
import uq.deco2800.coaster.game.world.World;

/**
 * A simple command to allow you to add and remove money from both
 * the Player and the Players Bank.
 *
 * Created by draganmarjanovic on 21/10/2016.
 */
public class CommerceCommand extends Command {

	/**
	 * Construct and initialise settings for the
	 * CommerceCommand
	 */
	public CommerceCommand() {
		commandName = "commerce";
		argumentCount = 3;
		help = "DESCRIPTION:\n" +
				"Add or remove gold from different targets.\n" +
				"commerce <target> <operation> <quantity>\n" +
				"\t <target> - player, bank\n" +
				"\t <operation> - add, remove\n" +
				"\t <quantity> - integer\n" +
				"Example:\n" +
				"\tcommerce player add 100";
	}

	/**
	 * Processes the arguments, ensuring they are valid and then handles it
	 * appropriately.
	 *
	 * @param arguments a list of arguments which are passed to the function.
	 * @return returns an error message (in the case of invalid arguments/format) or
	 * returns a success message.
	 */
	@Override
	String execute(Object[] arguments) {
		String target = ((String) arguments[0]).trim();
		String operation = ((String) arguments[1]).trim();
		Integer amount;

		// Ensure operation is "add" or "remove"
		if (!"add".equals(operation) && !"remove".equals(operation)) {
			return "Invalid operation.";
		}

		// Check to ensure the amount was an Integer
		try {
			amount = Integer.parseInt((String) arguments[2]);
		} catch (NumberFormatException e) {
			return "Specified amount is not a number.";
		}

		// Dispatch to appropriate handler
		String result = "Invalid target specified.";
		if ("player".equals(target)) {
			result = player(operation, amount);
		} else if ("bank".equals(target)) {
			result = bank(operation, amount);
		}
		return result;
	}

	/**
	 * Handles an operation intended for the bank.
	 *
	 * @param operation the type of operation (add/remove)
	 * @param amount the amount to add/remove.
	 * @return the success or error string to be printed to the console.
	 */
	private String bank(String operation, Integer amount) {
		PlayerCommerce commerce = World.getInstance().getFirstPlayer().getCommerce();
		String result = "Gold before:" + (commerce.getNetValue() - commerce.getGold());
		if ("add".equals(operation)) {
			commerce.getBank().addGold(amount);
		} else {
			commerce.getBank().withdrawGold(amount);
		}
		result += "\nGold after:" + (commerce.getNetValue() - commerce.getGold());
		return result;
	}

	/**
	 * Handles an operation intended for the player.
	 *
	 * @param operation the type of operation (add/remove).
	 * @param amount the amount to add/remove.
	 * @return the success or error string to be printed to the console.
	 */
	private String player(String operation, Integer amount) {
		PlayerCommerce commerce = World.getInstance().getFirstPlayer().getCommerce();
		String result = "Gold before:" + commerce.getGold();
		if ("add".equals(operation)) {
			commerce.addGold(amount);
		} else {
			commerce.reduceGold(amount);
		}
		result += "\nGold after:" + commerce.getGold();
		return result;
	}

}

