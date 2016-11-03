package uq.deco2800.coaster.graphics.screens;

import javafx.scene.Node;

public abstract class Screen {
	Node rootNode;
	// When getVisibility is true, this screen will be displayed. Otherwise render will not be called.
	private boolean isVisible = false;
	double screenWidth;
	double screenHeight;

	protected Screen(Node root) {
		rootNode = root;
		rootNode.setVisible(false);
	}

	protected Screen() {
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		isVisible = visible;
		rootNode.setVisible(visible);
		setDisable(!visible);
	}

	public void setDisable(boolean disabled) {
		rootNode.setDisable(disabled);
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setSize(double width, double height) {
		screenWidth = width;
		screenHeight = height;
	}

	/**
	 * Centers the Screen in the X direction according to the specified width.
	 *
	 * @param width The max width to center to
	 */
	public void centerScreenX(int width) {
		double translate = (width - screenWidth) / 2;
		if (translate < 0) {
			rootNode.setLayoutX(0);
		} else {
			rootNode.setLayoutX(translate);
		}
	}

	/**
	 * Centers the Screen in the Y direction according to the specified height.
	 *
	 * @param height The max height to center to
	 */
	public void centerScreenY(int height) {
		double translate = (height - screenHeight) / 2;
		if (translate < 0) {
			rootNode.setLayoutY(0);
		} else {
			rootNode.setLayoutY(translate);
		}
	}

	public abstract void setWidth(int newWidth);

	public abstract void setHeight(int newHeight);

	// This method gets called every engine tick.
	public abstract void render(long ms, boolean renderBackGround);
}
