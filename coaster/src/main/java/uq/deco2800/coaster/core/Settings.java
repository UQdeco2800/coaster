package uq.deco2800.coaster.core;

/**
 * A settings class, where we can store settings/defaults for the game.
 */
public class Settings {

	private int resWidth;
	private int resHeight;

	/**
	 * Initialises the Settings class.
	 */
	public Settings() {
		loadDefaultSettings();
	}


	/**
	 * Fallback default settings
	 */
	private void loadDefaultSettings() {
		resWidth = 1280;
		resHeight = 720;
	}

	/**
	 * @return resWidth
	 */
	public int getResWidth() {
		return resWidth;
	}

	/**
	 * @return resHeight
	 */
	public int getResHeight() {
		return resHeight;
	}
}
