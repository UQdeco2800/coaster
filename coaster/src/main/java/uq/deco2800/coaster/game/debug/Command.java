package uq.deco2800.coaster.game.debug;

/**
 * A base class for console debug commands.
 *
 * What this class does for you:
 * - Allows easy matching in the CommandDispatch class
 * - Checks to ensure correct number of arguments
 * - Automatically handles "help" argument - printing relevant documentation
 *
 * What you need to do:
 * If you would like to add custom commands to the Debug Console,
 * extend this class, set the commandName string, number of arguments you expect,
 * and a help string. Once this is done, implement the desired functionality inside
 * an overridden execute(Object[]) function. Once complete, simply add an instance
 * inside the CommandDispatch class.
 *
 * Created by draganmarjanovic on 15/10/2016.
 */
public abstract class Command {

	protected String commandName; // The commandName name
	protected Integer argumentCount; // Number of args execute() expects
	protected String help; // The help string for the commandName

	/**
	 * The main function of the commandName.
	 *
	 * @param arguments a list of arguments which are passed to the function.
	 * @return the outcome of running the commandName.
	 */
	abstract String execute(Object[] arguments);

	/**
	 * This function takes a list of potential arguments as a string.
	 * If their is a single argument "help" the help information is returned
	 * for the commandName. If there is the correct number of arguments,
	 * they are split up and passed on to the main execute() function.
	 *
	 * Lastly, if the there is an incorrect number of arguments, an error
	 * message is returned.
	 *
	 * @param arguments the arguments passed to the function as a string
	 * @return the outcome of running the commandName or a processing error.
	 */
	public String execute(String arguments) {
		Object[] splitArguments = arguments.split(" ");
		if ("help".equals(arguments)) {
			return getHelp();
		} else if (splitArguments.length == argumentCount) {
			return execute(splitArguments);
		} else if (argumentCount == -1) {
			// In the case this command accepts any amount of args
			return execute(splitArguments);
		} else {
			return "Invalid argument count.\n";
		}
	}

	/**
	 * Returns the help string for the commandName.
	 *
	 * @return the commands help string/information
	 */
	public String getHelp() {
		return help;
	};

	/**
	 * The commandName name.
	 *
	 * @return the commandName name.
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * The number of arguments the commandName takes.
	 *
	 * @return the number of arguments the commandName takes.
	 */
	public Integer getArgumentCount() {
		return argumentCount;
	}
}
