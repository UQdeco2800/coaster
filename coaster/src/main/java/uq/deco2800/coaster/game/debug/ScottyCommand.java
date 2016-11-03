package uq.deco2800.coaster.game.debug;

import java.awt.*;
import java.net.URL;

/**
 * A bit of a laff.
 *
 * Aside from being here for laughs, this command is also
 * useful for testing.
 *
 * Created by draganmarjanovic on 15/10/2016.
 */
public class ScottyCommand extends Command {

	/**
	 * Command metadata.
	 */
	public ScottyCommand() {
		commandName = "meme_me_up_scotty";
		argumentCount = 1;
		help = "memes you up";
	}

	/**
	 * Memes you up.
	 *
	 * @param arguments a list of arguments which are passed to the function.
	 * @return result.
	 */
	@Override
	String execute(Object[] arguments) {
		try {
			Desktop.getDesktop().browse(new URL("https://www.youtube.com/watch?v=dQw4w9WgXcQ").toURI());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return "memed";
	}

}
