package uq.deco2800.coaster.graphics.notifications;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.TestHelper;

/**
 * Created by rcarrier on 13/08/2016.
 */
public class ToasterTest {
	private static Logger logger = LoggerFactory.getLogger(ToasterTest.class);
	private static final String ls = System.lineSeparator();

	/**
	 * testAdd tests; adding expired removing adding another (displaying 2 at
	 * same time) ejecting (remove by reference (forced removing of
	 * notification)) preservation of order (and that eject is by reference not
	 * by time added)
	 */
	@Test
	public void testAdd() {
		TestHelper.load();

		Toaster.ejectAllToast();
		//TODO add order of notifications, top to bot, ? etc
		//TODO test fade when implemented
		//as in check if they are returning in the correct order

		String test1 = "test";
		Date now = new Date();
		//Test adding
		int ToasterReference = Toaster.manualToast(new Date(), test1, 10000000, 0);
		logAssert(test1 + ls, Toaster.getToast(now));
		//Test removing
		Toaster.manualToast(new Date(), test1, -100000, 0);
		logAssert(test1 + ls, Toaster.getToast(now));
		//Test adding another
		Toaster.manualToast(new Date(), test1 + "2", 10000000, 0);
		logAssert(test1 + "2" + ls + test1 + ls, Toaster.getToast(now));
		//Test eject
		Toaster.ejectToast(ToasterReference);
		logAssert(test1 + "2" + ls, Toaster.getToast(now));
		//Prove it works the other way too
		ToasterReference = Toaster.manualToast(new Date(), test1, 10000000, 0);
		logAssert(test1 + ls + test1 + "2" + ls, Toaster.getToast(now));
		Toaster.ejectToast(ToasterReference);
		logAssert(test1 + "2" + ls, Toaster.getToast(now));
		Toaster.ejectAllToast();
		logAssert("", Toaster.getToast(now));
	}

	@Test
	public void testColors() {
		TestHelper.load();

		Toaster.ejectAllToast();
		Date now = new Date();
		Toaster.manualToast(now, "d", Toaster.DURATION_DEFAULT * 2, Toaster.FADE_DURATION_DEFAULT);
		Toaster.manualToast(now, "r", Toaster.DURATION_DEFAULT, Toaster.FADE_DURATION_DEFAULT);
		Toaster.manualToast(now, "l", Toaster.DURATION_DEFAULT / 2, Toaster.FADE_DURATION_DEFAULT);

		Toaster.darkToast("d2");
		Toaster.toast("r2");
		Toaster.lightToast("l2");

		logAssert("l2" + ls + "r2" + ls + "d2" + ls + "l" + ls + "r" + ls + "d" + ls, Toaster.getToast(now));
		//Increment a bit to compensate for testing geneneration differences (100ms is heaps) 4GB is more than enough
		now.setTime(now.getTime() + 100 + Toaster.FADE_DURATION_DEFAULT);
		now.setTime(now.getTime() + Toaster.DURATION_DEFAULT / 2);
		logAssert("r2" + ls + "d2" + ls + "r" + ls + "d" + ls, Toaster.getToast(now));
		now.setTime(now.getTime() + Toaster.DURATION_DEFAULT / 2);
		//We are now at just over duration default extra
		logAssert("d2" + ls + "d" + ls, Toaster.getToast(now));
		now.setTime(now.getTime() + Toaster.DURATION_DEFAULT / 2);
		logAssert("d2" + ls + "d" + ls, Toaster.getToast(now));
		now.setTime(now.getTime() + Toaster.DURATION_DEFAULT / 2);
		logAssert("", Toaster.getToast(now));

	}

	private void logAssert(String a, String b) {
		if (!a.trim().equals(b.trim())) {
			logger.debug("'" + a.trim() + "' != '" + b.trim() + "'");
			assert false;
		}
	}
}
