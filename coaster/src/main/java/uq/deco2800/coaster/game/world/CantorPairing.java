package uq.deco2800.coaster.game.world;

/**
 * Cantor Pairing helper class. Cantor pairing functions combine two natural numbers to create a unique natural number
 */
public class CantorPairing {

	private CantorPairing() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Applies Cantor's pairing function to 2D coordinates.
	 *
	 * @param k1 X-coordinatec
	 * @param k2 Y-coordinate
	 * @return Unique 1D value
	 */
	public static long pair(final int k1, final long k2) {
		return (k1 + k2) * (k1 + k2 + 1) / 2 + k2;
	}

	/**
	 * Inverse of the Cantor Pairing function.
	 *
	 * @param z Cantor 1D value
	 * @return Original 2D coordinate pair
	 */
	public static long[] depair(long z) {
		long t = (int) Math.floor((Math.sqrt(8 * z + 1) - 1) / 2);
		long x = t * (t + 3) / 2 - z;
		long y = z - t * (t + 1) / 2;

		return new long[]{x, y};
	}
}
