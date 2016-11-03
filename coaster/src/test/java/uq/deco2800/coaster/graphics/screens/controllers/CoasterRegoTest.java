package uq.deco2800.coaster.graphics.screens.controllers;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;

/**
 * Created by ryanj on 18/09/2016.
 */
public class CoasterRegoTest {

	@Test
	public void testPasswordLabel() {
		TestHelper.loadWithFXML();
		CoasterRegoController controller = (CoasterRegoController) FXMLControllerRegister.get(CoasterRegoController.class);
		String uDefault = controller.userLabelText();
		String pDefault = controller.pLabelText();

		assert controller.userLabelText() == uDefault;
		assert controller.pLabelText() == pDefault;

		controller.manualFieldTrigger();

		assert controller.userLabelText() != uDefault;
		assert controller.pLabelText() != pDefault;

		assert controller.userLabelText().toLowerCase().contains("empty");
		assert controller.pLabelText().toLowerCase().contains("empty");

		controller.dummyData();

		controller.manualFieldTrigger();

		assert controller.userLabelText() == uDefault;
		assert controller.pLabelText() == pDefault;
	}
}
