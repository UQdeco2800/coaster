package uq.deco2800.coaster.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.stage.Stage;
import uq.deco2800.coaster.core.Engine;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;
import uq.deco2800.coaster.graphics.screens.Screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A wrapper class to assist with screen management.
 */
public class Renderer {
	private Stage stage;
	private Viewport viewport;
	//List of screens to be rendered, in first-to-last; that is, the end of the list is rendered topmost.
	//We're probably only ever going to have 3 screens ever; the game itself, a UI layer, and a "menu" layer
	private List<Screen> screens = new ArrayList<>();
	private HashMap<String, Screen> screenMapping = new HashMap<>();

	public Renderer(Stage stage, Viewport viewport, Engine engine) {
		this.stage = stage;
		this.viewport = viewport;
	}

	public void addScreen(String screenId, Screen screen) {
		screens.add(screen);
		screenMapping.put(screenId, screen);
		((Group) stage.getScene().getRoot()).getChildren()
				.add(screen.getRootNode());
	}

	public Screen getScreen(String screenId) {
		return screenMapping.get(screenId);
	}

	/*
	 * This game is completely single threaded, so we don't have to deal with
	 * state buffers or threading issues. This render call is called from the
	 * main game loop in the Engine class.
	 */
	public void render(long ms, boolean renderBackground) {
		if (World.getInstance().getPlayerEntities().size() > 0) {

			Entity playerEntity = World.getInstance().getFirstPlayer();
			if (((Player) playerEntity).getOnMountStatus()) {
				playerEntity = ((Player) playerEntity).getMount();
			}
			WorldTiles tiles = World.getInstance().getTiles();
			if (tiles.getHeight() > viewport.getHeight()) {
				viewport.centerOnY(playerEntity.getY() + playerEntity.getHeight() / 2);
			} else {
				viewport.centerOnY(tiles.getHeight() / 2);
			}
			if (tiles.getWidth() > viewport.getWidth()) {
				viewport.centerOnX(playerEntity.getX() + playerEntity.getWidth() / 2);
			} else {
				viewport.centerOnX(tiles.getWidth() / 2);
			}
		}
		for (Screen screen : screens) {
			if (screen.isVisible()) {
				screen.render(ms, renderBackground);
			}
		}
	}

	public void renderMulti(long ms) {
		if (World.getInstance().getPlayerEntities().size() > 0) {
			//todo this
//			Entity playerEntity = World.getInstance().getFirstPlayer();
			WorldTiles tiles = World.getInstance().getTiles();
			//Chunk.CHUNK_WIDTH
			//100
			viewport.centerOnY(110);//tiles.getHeight() / 2);
			viewport.centerOnX(21);//tiles.getWidth() / 2);
/*
			if (tiles.getHeight() > viewport.getHeight()) {
				viewport.centerOnY(playerEntity.getY() + playerEntity.getHeight() / 2);
			} else {
				viewport.centerOnY(tiles.getHeight() / 2);
			}
			if (tiles.getWidth() > viewport.getWidth()) {
				viewport.centerOnX(playerEntity.getX() + playerEntity.getWidth() / 2);
			} else {
				viewport.centerOnX(tiles.getWidth() / 2);
			}*/
		}
		for (Screen screen : screens) {
			if (screen.isVisible()) {
				screen.render(ms, true);
			}
		}
	}

	/**
	 * Shifts the specified Screen to the end of the screens array
	 *
	 * @param screen the Screen you want to give focus.
	 */
	public void sendToFront(Screen screen) {
		int screenIndex = screens.indexOf(screen);

		for (int i = screenIndex; i < screens.size() - 1; i++) {
			swapScreens(i, i + 1);
		}
	}

	/**
	 * @param screen to send to the back
	 */
	public void sendToBack(Screen screen) {
		int screenIndex = screens.indexOf(screen);

		for (int i = screenIndex; i > 0; i--) {
			swapScreens(i, i - 1);
		}
	}

	/**
	 * getter method to retrieve the Screen that currently has focus
	 *
	 * @return Screen at last index of screens array
	 */
	public Screen getActiveScreen() {
		return screens.get(screens.size() - 1);
	}

	/**
	 * Determines if the Screen specified by screenId is the active Screen
	 *
	 * @param screenId screen to check
	 * @return if the specified screen is active
	 */
	public boolean isActiveScreen(String screenId) {
		return getScreen(screenId).equals(getActiveScreen());
	}

	/**
	 * Helper method which toggles the display of the specified screen.
	 *
	 * @param screenId screen to toggleVisibility
	 */
	public void toggleScreen(String screenId) {
		Screen screen = getScreen(screenId);
		if (isActiveScreen(screenId)) {
			disableScreen(screen);
		} else {
			enableScreen(screen);
		}
	}

	/**
	 * Opens and displays the screen specified by the given string id.
	 *
	 * @param screenId the string ID of the screen to be enabled.
	 */
	public void enableScreen(String screenId) {
		Screen screen = getScreen(screenId);
		enableScreen(screen);
	}

	/**
	 * Opens and displays the specified screen.
	 *
	 * @param screen the screen to display
	 */
	private void enableScreen(Screen screen) {
		screen.setVisible(true);
		screen.setDisable(false);
		sendToFront(screen);
	}

	/**
	 * Closes and disables the screen specified by the given string id.
	 *
	 * @param screenId the string ID of the screen to be disabled.
	 */
	public void disableScreen(String screenId) {
		Screen screen = getScreen(screenId);
		disableScreen(screen);
	}

	/**
	 * Disables the specified screen.
	 *
	 * @param screen the screen which to disable.
	 */
	private void disableScreen(Screen screen) {
		screen.setVisible(false);
		screen.setDisable(true);
		sendToBack(screen);
	}

	/**
	 * @param mainScreen        current main screen
	 * @param desiredMainScreen the main screen to switch to
	 */
	public void swapScreens(int mainScreen, int desiredMainScreen) {
		Collections.swap(screens, mainScreen, desiredMainScreen);

		Group root = (Group) stage.getScene().getRoot();
		ObservableList<Node> tempChildren = FXCollections
				.observableArrayList(root.getChildren());
		Collections.swap(tempChildren, mainScreen, desiredMainScreen);
		root.getChildren().setAll(tempChildren);
	}

	public void setWidth(int newWidth) {
		for (Screen screen : screens) {
			screen.setWidth(newWidth);
		}
	}

	public void setHeight(int newHeight) {
		screens.forEach(screen -> screen.setHeight(newHeight));
	}

	public Viewport getViewport() {
		return viewport;
	}

	public void hideAllScreens() {
		for (Screen screen : screens) {
			screen.setVisible(false);
		}
	}

	/**
	 * Returns the root of the stage's scene. This is useful as you can
	 * add/remove children from the root.
	 */
	public Group getRoot() {
		return (Group) stage.getScene().getRoot();
	}

}
