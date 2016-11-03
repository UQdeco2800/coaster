package uq.deco2800.coaster.game.debug;

public class DebugChannel {
	private String debugString;
	private boolean enabled;


	public DebugChannel() {
		debugString = "";
		enabled = true;
	}

	/**
	 * Return channel's debugString, if enabled. Else return empty string.
	 */
	public String get() {
		if (enabled) {
			return debugString;
		} else {
			return "";
		}
	}

	/**
	 * Add to channel's debugString
	 */
	public void add(String newString) {
		debugString += newString + "\n";
	}

	/**
	 * Toggle channel
	 */
	public void toggle() {
		enabled = !enabled;
	}

	/**
	 * Clear channel
	 */
	public void clear() {
		debugString = "";
	}

}
