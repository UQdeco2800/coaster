package uq.deco2800.coaster.game.debug;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

/**
 * Allows easy movement to a specified x,y coordinate.
 *
 * Created by draganmarjanovic on 15/10/2016.
 */
public class MoveCommand extends Command {

	private Player player;

	public MoveCommand() {
		commandName = "move";
		argumentCount = 2;
		help = "move x y\n\tmoves the player to the specified x,y coordinates";
	}

	/**
	 * Processes the arguments and executes the movement command.
	 *
	 * @param arguments a list of arguments which are passed to the function.
	 * @return
	 */
	@Override
	String execute(Object[] arguments) {
		player = World.getInstance().getFirstPlayer();
		try {
			float x = Float.parseFloat((String) arguments[0]);
			float y = Float.parseFloat((String) arguments[1]);
			return movePlayer(x, y);
		} catch (NumberFormatException e) {
			return "Invalid coordinate/s.";
		}

	}

	/**
	 * Moves the player to the specified x,y coordinates.
	 *
	 * @param x the x coordinate to move the player to.
	 * @param y the y coordinate to move the player to.
	 * @return the players coordinates from before the command and after.
	 */
	private String movePlayer(float x, float y) {
		String result = "Before:(" + (int) player.getX() + "," + (int) player.getY() + ")\n";
		player.setPosition(x, y);
		result += "After:(" + (int) player.getX() + "," + (int) player.getY() + ")";
		return result;
	}
}
