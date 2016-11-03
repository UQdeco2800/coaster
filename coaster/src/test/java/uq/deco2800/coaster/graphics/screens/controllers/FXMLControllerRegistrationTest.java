package uq.deco2800.coaster.graphics.screens.controllers;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.TestHelper;

/**
 * Created by ryanj on 18/09/2016.
 */
public class FXMLControllerRegistrationTest {
	private static final Logger logger = LoggerFactory.getLogger(FXMLControllerRegistrationTest.class);

	@Before
	public void load() {
		TestHelper.load();
	}

	// @After
	// public void clean() {
	// FXMLControllerRegister.clear();
	// }

	@Test
	public void testRegister() {
		// FXMLControllerRegister.clear();
		// assert 0 == FXMLControllerRegister.getReferences().size();
		// uq.deco2800.coaster.TestHelper.loadWithFXML();
		assert 0 < FXMLControllerRegister.getReferences().size();
		logger.info(FXMLControllerRegister.getReferences().toString());
		assert null != FXMLControllerRegister.get(CoasterRegoController.class);
		assert null != FXMLControllerRegister.get(StartScreenController.class);
		assert null != FXMLControllerRegister.get(AboutScreenController.class);
		assert null != FXMLControllerRegister.get(DifficultyController.class);
		assert null != FXMLControllerRegister.get(PauseMenuController.class);
		assert null != FXMLControllerRegister.get(OptionsMenuController.class);
		assert null != FXMLControllerRegister.get(AchievementsController.class);
		assert null != FXMLControllerRegister.get(AchievementToolTipController.class);
		assert null != FXMLControllerRegister.get(SkillTreeController.class);
		assert null != FXMLControllerRegister.get(PassiveInfoPanelController.class);
		assert null != FXMLControllerRegister.get(BankController.class);
//		assert null != FXMLControllerRegister.get(StoreController.class);
		assert null != FXMLControllerRegister.get(PopUpController.class);
	}
}
