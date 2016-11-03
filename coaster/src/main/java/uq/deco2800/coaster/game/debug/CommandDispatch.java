package uq.deco2800.coaster.game.debug;

import java.util.ArrayList;

/**
 *
 * This class holds all supported console commands, does some pre-processing and
 * then attempts to run a commandName based off the user input.
 *
 * Pre-processing / Meta Functions:
 *  - provides a list of all possible commands
 *  - splits commandName and arguments
 *  - matches specified commandName to an available commandName
 *    returning error message on failure
 *
 * Created by draganmarjanovic on 15/10/2016.
 */
public class CommandDispatch {


	private ArrayList<Command> commands;


	/**
	 * Initalises the commandName dispatcher. Commands which
	 * are to be available must be added here.
	 */
	public CommandDispatch() {
		commands = new ArrayList<Command>();
		// Commands
		commands.add(new QuitCommands(QuitCommands.CommandType.CLOSE));
		commands.add(new HealthCommands(HealthCommands.CommandType.HEAL));
		commands.add(new HealthCommands(HealthCommands.CommandType.HEALTH));
		commands.add(new MoveCommand());
		commands.add(new ScottyCommand());
		commands.add(new CommerceCommand());
		commands.add(new ItemCommand());
		commands.add(new QuitCommands(QuitCommands.CommandType.EXIT));
		commands.add(new QuitCommands(QuitCommands.CommandType.QUIT));
	}

	/**
	 * Matches the commandName text to the proper commandName. Removes
	 * the commandName text from the string and splits the rest into
	 * an object array to be passed to the commandName executor.
	 *
	 * @param input the user entered input.
	 * @return output of the executed commandName.
	 */
	public String processCommand(String input) {
		String[] split = input.split(" ", 2);
		// For easier readability
		String command = split[0];
		String arguments = "";
		if (split.length == 2) {
			arguments = split[1];
		}
		// Dispatch to handler
		String result = "Invalid command name.";
		if ("help".equals(command)) {
			result = help();
		} else {
			for (Command potentialCommand : commands) {
				if (command.equals(potentialCommand.getCommandName())) {
					result = potentialCommand.execute(arguments);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Creates a string that lists all available commands.
	 *
	 * @return the names of all the available commands.
	 */
	private String help() {
		String result = "The following is a list of available commands:\n";
		for (Command command1 : commands) {
			result += "\t" + command1.getCommandName() + "\n";
		}
		result += "For more information type\t<command_name> help";
		return result;
	}

}
