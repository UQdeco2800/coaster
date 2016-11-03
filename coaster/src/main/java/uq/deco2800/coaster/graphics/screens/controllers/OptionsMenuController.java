package uq.deco2800.coaster.graphics.screens.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.Renderer;
import uq.deco2800.coaster.graphics.Window;

public class OptionsMenuController {
	@FXML
	private VBox mainContainer;

	@FXML
	private Slider masterSlider;

	@FXML
	private CheckBox muteCheckBox;

	@FXML
	private CheckBox lightingCheckBox;

	@FXML
	private Slider renderSlider;

	private World world = World.getInstance();
	private Renderer renderer = Window.getEngine().getRenderer();

	/**
	 * Getter method to access the mainContainer when resizing the screen
	 *
	 * @return
	 */
	public VBox getMainContainer() {
		return mainContainer;
	}

	/**
	 * Toggle the Controls menu
	 */
	public void onControlsButtonClick() {
		Window.toggleScreens("Options Menu", "Controls Screen");
	}

	/**
	 * Toggles the Options Menu
	 */
	@FXML
	protected void onBackButtonClick() {
		SoundCache.play("back");
		renderer.disableScreen("Options Menu");
		if (!renderer.isActiveScreen("Start Screen")) {
			renderer.enableScreen("Pause Menu");
		}
	}

	/**
	 * Sets the Listeners for the volume slider and mute checkbox
	 */
	private void setListeners() {
		masterSlider.adjustValue(SoundCache.getVolume() * 100.0);
		masterSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean finished, Boolean start) {
				if (finished) {
					SoundCache.play("click");
					SoundCache.setVolume(masterSlider.getValue() / 100.0);
				}
			}
		});
		muteCheckBox.setSelected(SoundCache.getMute());
		muteCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					Boolean newValue) {
				SoundCache.play("click");
				if (newValue) {
					SoundCache.mute();
				} else {
					SoundCache.unmute();
				}
			}
		});

		lightingCheckBox.setSelected(world.getLightingState());
		lightingCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					Boolean newValue) {
				SoundCache.play("click");
				world.setLighting(newValue);
			}
		});

		renderSlider.adjustValue(world.getEntityRenderDistance());
		renderSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean finished, Boolean start) {
				if (finished) {
					SoundCache.play("click");
					world.setEntityRenderDistance((int) renderSlider.getValue());
				}
			}
		});
	}

	@FXML
	void initialize() {
		assert mainContainer != null : "fx:id=\"mainContainer\" was not injected: check your FXML file " +
				"'OptionsMenu.fxml'.";
		assert masterSlider != null : "fx:id=\"masterSlider\" was not injected: check your FXML file " +
				"'OptionsMenu.fxml'.";
		assert muteCheckBox != null : "fx:id=\"muteCheckBox\" was not injected: check your FXML file " +
				"'OptionsMenu.fxml'.";
		assert lightingCheckBox != null : "fx:id=\"muteCheckBox\" was not injected: check your FXML file " +
				"'OptionsMenu.fxml'.";
		assert renderSlider != null : "fx:id=\"muteCheckBox\" was not injected: check your FXML file " +
				"'OptionsMenu.fxml'.";
		FXMLControllerRegister.register(OptionsMenuController.class, this);
		setListeners();
	}
}
