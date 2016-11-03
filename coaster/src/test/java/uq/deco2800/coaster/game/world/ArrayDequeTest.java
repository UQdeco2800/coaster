package uq.deco2800.coaster.game.world;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class ArrayDequeTest {

	@Test
	public void testValidConstructors() {
		ArrayDeque arrayDeque1 = new ArrayDeque();
		ArrayDeque arrayDeque3 = new ArrayDeque(100);
		ArrayDeque arrayDeque2 = new ArrayDeque(10, 20);
		assertEquals(16, arrayDeque1.capacity());
		assertEquals(128, arrayDeque3.capacity());
		assertEquals(256, arrayDeque2.capacity());
	}

	@Test
	public void testAdding() {
		ArrayDeque arrayDeque = new ArrayDeque();
		assertEquals(16, arrayDeque.capacity());
		assertEquals(0, arrayDeque.size());
		for (int i = 0; i < 15; i++) {
			arrayDeque.addLast(i);
		}
		assertEquals(16, arrayDeque.capacity());
		assertEquals(15, arrayDeque.size());
		arrayDeque.addLast(15);
		assertEquals(32, arrayDeque.capacity());
		assertEquals(16, arrayDeque.size());
		for (int i = 0; i < 15; i++) {
			arrayDeque.addFirst(i);
		}
		assertEquals(32, arrayDeque.capacity());
		assertEquals(31, arrayDeque.size());
		arrayDeque.addFirst(15);
		assertEquals(64, arrayDeque.capacity());
		assertEquals(32, arrayDeque.size());
		arrayDeque.addFirst(16);
		List<Integer> expected = new ArrayList<>(Arrays.asList(16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0,
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
		assertEquals(expected, Arrays.asList(arrayDeque.toArray()));
	}

	@Test
	public void testGet() {
		ArrayDeque arrayDeque = new ArrayDeque();
		for (int i = 0; i < 15; i++) {
			arrayDeque.addLast(i);
		}
		assertEquals(0, arrayDeque.get(0));
		assertEquals(14, arrayDeque.get(14));
	}

	@Test
	public void testSet() {
		ArrayDeque arrayDeque = new ArrayDeque();
		arrayDeque.addFirst(1);
		assertEquals(1, arrayDeque.get(0));
		arrayDeque.set(0, 2);
		assertEquals(2, arrayDeque.get(0));
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidAddFirst() {
		ArrayDeque arrayDeque = new ArrayDeque();
		arrayDeque.addFirst(null);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidAddLast() {
		ArrayDeque arrayDeque = new ArrayDeque();
		arrayDeque.addLast(null);
	}

	@Test(expected = NoSuchElementException.class)
	public void testInvalidGetFirst() {
		ArrayDeque arrayDeque = new ArrayDeque();
		arrayDeque.getFirst();
	}

	@Test(expected = NoSuchElementException.class)
	public void testInvalidGetLast() {
		ArrayDeque arrayDeque = new ArrayDeque();
		arrayDeque.getLast();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidGet() {
		ArrayDeque arrayDeque = new ArrayDeque();
		arrayDeque.get(1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidSet() {
		ArrayDeque arrayDeque = new ArrayDeque();
		arrayDeque.set(1, 1);
	}
}
