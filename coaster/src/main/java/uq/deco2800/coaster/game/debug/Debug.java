package uq.deco2800.coaster.game.debug;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// A controller for DebugScreen. Holds a string that is displayed and then
// cleared every tick.
public class Debug {
	private static final String DEFAULT_CHANNEL = "debug";
	private Map<String, DebugChannel> channels;

	public Debug() {
		channels = new TreeMap<>();
		channels.put(DEFAULT_CHANNEL, new DebugChannel());
	}

	/**
	 * Load given channel. If doesn't exist - create.
	 */
	private DebugChannel load(String channel) {
		DebugChannel debug = channels.get(channel);
		if (debug == null) {
			debug = new DebugChannel();
		}
		return debug;
	}

	/**
	 * Add text to be displayed on the next render() call, followed by a \n
	 */
	public void addToDebugString(String channel, String newString) {
		DebugChannel debug = load(channel);
		debug.add(newString);
		channels.put(channel, debug);
	}

	/**
	 * Toggles visibility from true to false, or false to true;
	 */
	public void toggleDebug(String channel) {
		DebugChannel debug = load(channel);
		debug.toggle();
		channels.put(channel, debug);
	}

	/**
	 * If enabled/visible, return debug string to be displayed
	 */
	public String getDebugString(String channel) {
		DebugChannel debug = load(channel);
		return debug.get();
	}

	/**
	 * Return a string containing the debugString from all channels
	 */
	public String getAllDebugStrings() {
		String string = "";
		Set<String> channelList = this.channels.keySet();
		DebugChannel debug;
		for (String channel : channelList) {
			debug = channels.get(channel);
			string += debug.get();
		}
		return string;
	}

	/**
	 * Set the string of all channels to empty
	 */
	public void clearAllDebugStrings() {
		Set<String> channelList = this.channels.keySet();
		DebugChannel debug;
		for (String channel : channelList) {
			debug = channels.get(channel);
			debug.clear();
			channels.put(channel, debug);
		}
	}

	/**
	 * Set debug string to empty
	 */
	public void clearDebugString(String channel) {
		DebugChannel debug = load(channel);
		debug.clear();
		channels.put(channel, debug);
	}

	/**
	 * Clears default channel
	 */
	public void clearDebugString() {
		clearDebugString(DEFAULT_CHANNEL);
	}

	/**
	 * Returns default channel's debugString
	 */
	public String getDebugString() {
		return getDebugString(DEFAULT_CHANNEL);
	}

	/**
	 * Toggles default channel
	 */
	public void toggleDebug() {
		toggleDebug(DEFAULT_CHANNEL);
	}

	/**
	 * Adds given string to default channel's debugString
	 */
	public void addToDebugString(String newString) {
		addToDebugString(DEFAULT_CHANNEL, newString);
	}
}
