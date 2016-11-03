package uq.deco2800.coaster;

import uq.deco2800.coaster.graphics.Window;

public class Launcher {
	private static Window window;


	private Launcher() {
	}

	/**
	 * Call this method to launch into play
	 */
	public static void main(String[] args) {
		window = new Window();
		window.begin();
	}
}
