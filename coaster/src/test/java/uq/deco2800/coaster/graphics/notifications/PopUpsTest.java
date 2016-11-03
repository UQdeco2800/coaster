package uq.deco2800.coaster.graphics.notifications;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.graphics.notifications.PopUps.PopUp;

public class PopUpsTest {

	@Before
	public void load() {
		TestHelper.load();
	}

	@Test
	public void popUpTest() {
		// Check clearAll empties
		PopUps.clearAll();
		Assert.assertFalse(PopUps.hasPopUps());

		// Check adding a Popup adds
		PopUps.add("Hello World");
		Assert.assertTrue(PopUps.hasPopUps());

		// Check getting PopUp removes it and Contents
		PopUp popUp = PopUps.getPopUp();
		Assert.assertFalse(PopUps.hasPopUps());
		Assert.assertEquals(popUp.getText(), "Hello World");
		Assert.assertEquals(popUp.getDelay(), 2000);

		// Check delay reduces
		popUp.reduceDelay(1000);
		Assert.assertEquals(popUp.getDelay(), 1000);

		// Check Popup with custom delay
		PopUps.addCustom("World, Hello", 1);
		popUp = PopUps.getPopUp();
		Assert.assertEquals(popUp.getText(), "World, Hello");
		Assert.assertEquals(popUp.getDelay(), 1);
	}
}
