package uq.deco2800.coaster.game.world;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertEquals;

public class CantorPairingTest {

	@Test(expected = UnsupportedOperationException.class)
	public void testConstructor() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
		TestHelper.load();
		final Constructor<CantorPairing> constructor = CantorPairing.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		try {
			constructor.newInstance();
		} catch (InvocationTargetException e) {
			throw (UnsupportedOperationException) e.getTargetException();
		}
	}

	@Test
	public void pairTest() {
		TestHelper.load();

		long val;

		// test 0 0
		val = CantorPairing.pair(0, 0);
		Assert.assertEquals(val, 0);

		// test 10 0
		val = CantorPairing.pair(0, 10);
		Assert.assertEquals(val, 65);

		// test 0 10
		val = CantorPairing.pair(10, 0);
		Assert.assertEquals(val, 55);

		// test 10 10
		val = CantorPairing.pair(10, 10);
		Assert.assertEquals(val, 220);
	}

	@Test
	public void depairTest() {
		TestHelper.load();

		int x = 10;
		int seed = 10;

		long pair = CantorPairing.pair(x, seed);
		long[] depair = CantorPairing.depair(pair);

		assertEquals(x, depair[0]);
		assertEquals(seed, depair[1]);
	}

}
