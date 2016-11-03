package uq.deco2800.coaster.graphics.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ryanj on 28/08/2016.
 */
public class Toaster {

	private static final Logger logger = LoggerFactory.getLogger(Toaster.class);
	public static final int DURATION_DEFAULT = 2000;
	public static final int FADE_DURATION_DEFAULT = 500;
	/*
	Toaster is used for making toast notifications!
	The notification can be prematurely ejected from the 'toaster' if needed.
	 */
	private static final ArrayList<Bread> loaf = new ArrayList<>();

	private Toaster() {
	}

	/**
	 * getToast cleans the old dirty toast out of the toaster, returning the current bread that is still in the
	 * toaster.
	 *
	 * @param now The current time to test against, this is not generated in the function due to testing reasons; if the
	 *            current time was used during testing, race conditions will exist.
	 * @return the current bread inside the toaster.
	 */
	public static String getToast(Date now) {
		//Could just pass new date in each time, but it would be slower
		StringBuilder sb = new StringBuilder();
		//loop reverse to remove while looping
		for (int i = loaf.size() - 1; i >= 0; i--) {
			if (loaf.get(i).status(now) == 2) {
				loaf.remove(i);
			} else {
				sb.append(loaf.get(i).getText() + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	public static void ejectAllToast() {
		for (int i = loaf.size() - 1; i >= 0; i--) {
			loaf.remove(i);
		}
	}

	/**
	 * Toast that lasts for a standard amount of time
	 *
	 * @param bread the text to display
	 * @return hashcode reference of the Bread created
	 */
	public static int toast(String bread) {
		return manualToast(new Date(), bread, DURATION_DEFAULT, FADE_DURATION_DEFAULT);
	}

	/**
	 * eject toast prematurely, if you want to eject toast (early) save it's hashcode when you create it.
	 *
	 * @param hashcode hashcode of the toast you want to eject
	 */
	public static void ejectToast(int hashcode) {
		for (int i = loaf.size() - 1; i >= 0; i--) {
			if (loaf.get(i).hashCode() == hashcode) {
				logger.debug("Toast ejected; " + loaf.get(i));
				loaf.remove(i);
				break;
			}
		}
		logger.debug("Toast not found; " + Integer.toString(hashcode));
	}

	/**
	 * Manually set your toasts parameters because you are the master of your own toast.
	 *
	 * @param bread          the text to display
	 * @param durationMs     duration of the notification
	 * @param fadeDurationMs fade out duration
	 * @return hashcode reference of the Bread created
	 */
	public static int manualToast(Date now, String bread, int durationMs, int fadeDurationMs) {
		Bread b = new Bread(now, bread, durationMs, fadeDurationMs);
		loaf.add(b);
		return b.hashCode();
	}

	/**
	 * Toast that lasts for longer than usual
	 *
	 * @param bread the text to display
	 * @return hashcode reference of the Bread created
	 */
	public static int darkToast(String bread) {
		return manualToast(new Date(), bread, DURATION_DEFAULT * 2, FADE_DURATION_DEFAULT);
	}

	/**
	 * Toast that lasts for less time than usual
	 *
	 * @param bread the text to display
	 * @return hashcode reference of the Bread created
	 */
	public static int lightToast(String bread) {
		return manualToast(new Date(), bread, DURATION_DEFAULT / 2, FADE_DURATION_DEFAULT);
	}


	/**
	 * Bread is a hidden class which is used to aid in keeping track of the duration of each notification. The entire
	 * class should never be accessed elsewhere
	 */
	static class Bread {
		private String text;
		private Date expireAt;
		private Date fadeAt;
		private int fadeDurationMs;


		protected Bread(Date now, String text, int durationMs, int fadeDurationMs) {
			this.text = text;
			this.fadeDurationMs = fadeDurationMs;
			expireAt = new Date();
			expireAt.setTime(now.getTime() + durationMs + fadeDurationMs);
			fadeAt = new Date();
			fadeAt.setTime(now.getTime() + durationMs);
		}

		/**
		 * gets the status of the bread
		 *
		 * @param now the time to compare to
		 * @return 0, still displaying; 1, fading out; 2, expired
		 */
		protected int status(Date now) {
			if (fadeAt.after(now)) {
				return 0;
			}
			if (expireAt.after(now)) {
				return 1;
			}
			return 2;
		}

		/**
		 * getter
		 *
		 * @return get text of the notification
		 */
		protected String getText() {
			return text;
		}

		/**
		 * how much the notification should be faded at the moment
		 *
		 * @param now current time to compare with
		 * @return percent the text should be faded
		 */
		protected int fadeAmt(Date now) {
			//TODO RyanCarrier
			//Fade percent
			return (int) ((expireAt.getTime() - now.getTime()) / fadeDurationMs);
		}
	}
}