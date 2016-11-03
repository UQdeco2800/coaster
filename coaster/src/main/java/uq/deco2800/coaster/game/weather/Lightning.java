package uq.deco2800.coaster.game.weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Declan on 20/10/2016.
 */
public class Lightning {
	//Segment positions of the lightning strike. Includes the initial and target positions.
	private float[] xPositions;
	private float[] yPositions;

	public Lightning(float posX, float posY, float targetX, float targetY) {
		generateLightningPositions(posX, posY, targetX, targetY);
	}

	/**
	 * @return A float[] containing the x-positions of the segments of the strike.
	 */
	public float[] getXPositions() {
		return xPositions;
	}

	/**
	 * @return A float[] containing the y-positions of the segments of the strike.
	 */
	public float[] getYPositions() {
		return yPositions;
	}

	/**
	 * (this is why I hate java generics)
	 * Converts a List\<Float\> to a float[].
	 * @param input Input list of Float objects.
	 * @return An array of float primitives.
	 */
	private float[] toRealFloatArray(List<Float> input) {
		float[] retval = new float[input.size()];
		for (int i = 0; i < input.size(); i++) {
			retval[i] = input.get(i);
		}

		return retval;
	}

	/**
	 * Generates the positions of each "segment" down the lightning strike.
	 * @param posX The initial starting x-coordinate of the lightning strike.
	 * @param posY The initial starting y-coordinate of the lightning strike.
	 */
	private void generateLightningPositions(float posX, float posY, float targetX, float targetY) {
		//Positions are "segment" positions down the strike
		//We generate a new direction pair every ~5 tiles
		List<Float> tempX = new ArrayList<Float>();
		List<Float> tempY = new ArrayList<Float>();
		tempX.add(posX);
		tempY.add(posY);

		Random random = new Random();
		float lastX = posX;
		float lastY = posY;
		//Loop until we reach the target
		while (true) {
			//If we're within 5 vertical tiles of the target, finish
			if (Math.abs(targetY - lastY) < 5) {
				break;
			}

			boolean tendDirection = lastX > targetX; //False is left, true is right

			//We tend towards the target
			boolean nextDirection = random.nextFloat() > 0.7 == tendDirection;

			//x direction varies within 2 tiles
			float nextX = random.nextFloat() * 2;
			if (!nextDirection) {
				nextX *= -1;
			}
			nextX += tempX.get(tempX.size() - 1);

			//y direction is between 3-5 tiles
			float nextY = random.nextFloat() * 2 + 3;
			nextY += tempY.get(tempY.size() - 1);

			lastX = nextX;
			lastY = nextY;

			tempX.add(nextX);
			tempY.add(nextY);
		}

		tempX.add(targetX);
		tempY.add(targetY);

		xPositions = toRealFloatArray(tempX);
		yPositions = toRealFloatArray(tempY);
	}
}
