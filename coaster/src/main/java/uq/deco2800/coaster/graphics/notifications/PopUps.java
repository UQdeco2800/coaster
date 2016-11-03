package uq.deco2800.coaster.graphics.notifications;

import java.util.ArrayList;

/**
 * Class to handle in-game notifications that use the PopUps system.
 *
 * @author Steven
 */
public class PopUps {
	private static final ArrayList<PopUp> popUpQueue = new ArrayList<>();
	private static final int DELAY = 2000;

	private PopUps() {

	}

	/**
	 * Method to check if there are any PopUps in the popUpQueue to display
	 *
	 * @return True if popUpQueue is not empty else False
	 */
	public static boolean hasPopUps() {
		return !popUpQueue.isEmpty();
	}

	/**
	 * Gets the next PopUp in the queue
	 *
	 * @return the next PopUp in the queue
	 */
	public static PopUp getPopUp() {
		return popUpQueue.remove(0);
	}

	/**
	 * Adds a new PopUp to the queue with the default delay value
	 *
	 * @param text String value to appear in the PopUp
	 */
	public static void add(String text) {
		popUpQueue.add(new PopUp(text, DELAY));
	}

	/**
	 * Adds a new PopUp to the queue with a set delay value.
	 *
	 * @param text String value to appear in the PopUp
	 * @param delay int value for how long the PopUp is displayed for
	 */
	public static void addCustom(String text, int delay) {
		popUpQueue.add(new PopUp(text, delay));
	}

	/**
	 * Clears the PopUp queue
	 */
	public static void clearAll() {
		popUpQueue.clear();
	}

	/**
	 * Simple class to contain relevant information on PopUps
	 *
	 * @author Steven
	 */
	public static class PopUp {
		private String text;
		private int delay;

		public PopUp(String text, int delay) {
			this.text = text;
			this.delay = delay;
		}

		/**
		 * @return the String value of the PopUp
		 */
		public String getText() {
			return text;
		}

		/**
		 * @return the delay value of the PopUp
		 */
		public int getDelay() {
			return delay;
		}

		/**
		 * Reduces the delay value of the PopUp
		 *
		 * @param ms value of how much to reduce the delay in milliseconds.
		 */
		public void reduceDelay(long ms) {
			delay -= ms;
		}
	}
}
