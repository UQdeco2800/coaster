package uq.deco2800.coaster.game.world;

import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
/**
 * An Array Deque is a re-sizable array that implements Deque functionality. It's main selling points are its
 * O(1) insertion to the head and tail as well as O(1) retrieval.
 */
public class ArrayDeque<E> {

	private E[] elements; // elements being stored in the ArrayDeque
	private int head; // head pointer
	private int tail; // tail pointer

	private static final int MIN_INITIAL_CAPACITY = 16; // minimum initial capacity of the array deque

	/**
	 * Empty constructor, creates an array deque of minimum initial capacity
	 */
	public ArrayDeque() {
		this(MIN_INITIAL_CAPACITY);
	}

	/**
	 * "2D" compatible constructor, creates an array deque with the capacity to store width * height values
	 */
	public ArrayDeque(int width, int height) {
		this(width * height);
	}


	/**
	 * Default constructor, creates an array deque large enough to store the specified number of elements
	 */
	public ArrayDeque(int numElements) {
		allocateElements(numElements);
	}


	/**
	 * Allocates an array large enough to hold the specified number of elements, rounded to the nearest power of 2
	 */
	private void allocateElements(int numElements) {
		int initialCapacity = MIN_INITIAL_CAPACITY;

		// if greater than minimum capacity, round to nearest power of 2.
		if (numElements > initialCapacity) {
			initialCapacity = (int) Math.pow(2, Math.ceil(Math.log(numElements)/Math.log(2)));
		}

		elements = (E[]) new Object[initialCapacity];
	}

	/**
	 * When the array has reached its capacity, this method will double its capacity. It performs this by copying the
	 * array into a new array of double the capacity, re-ordering the elements to fill the first half of the array
	 * in the process
	 */
	private void doubleCapacity() {
		assert head == tail;

		int start = head;
		int capacity = elements.length;
		int right = capacity - start; // number of elements to the right of the head

		int newCapacity = capacity << 1;
		if (newCapacity < 0) {
			throw new IllegalStateException("Overflow detected, Deque too big.");
		}

		Object[] newElements = new Object[newCapacity];
		System.arraycopy(elements, start, newElements, 0, right);
		System.arraycopy(elements, 0, newElements, right, start);
		elements = (E[]) newElements;
		head = 0;
		tail = capacity;
	}

	/**
	 * Copies the elements of the array deque into the provided array in non-circular format
	 */
	private <T> T[] copyElements(T[] array) {
		if (head < tail) {
			System.arraycopy(elements, head, array, 0, size());
		} else {
			int headPortionLen = elements.length - head;
			System.arraycopy(elements, head, array, 0, headPortionLen);
			System.arraycopy(elements, 0, array, headPortionLen, tail);
		}
		return array;
	}

	/**
	 * Adds an element to the head of the array deque
	 */
	public void addFirst(E element) {
		if (element == null) {
			throw new NullPointerException();
		}

		head = (head - 1) & (elements.length - 1);
		elements[head] = element;
		if (head == tail) {
			doubleCapacity();
		}
	 }

	/**
	 * Adds an element to the tail of the array deque
	 */
	public void addLast(E element) {
		if (element == null) {
			throw new NullPointerException();
		}

		elements[tail] = element;
		tail = (tail + 1) & (elements.length - 1);
		if (tail == head) {
			doubleCapacity();
		}
	}

	/**
	 * Gets the first element from the head of the array deque
	 */
	public E getFirst() {
		E element = elements[head];
		if (element == null) {
			throw new NoSuchElementException("No element at " + ((tail - 1) & (elements.length - 1)));
		}
		return element;
	}

	/**
	 * Gets the last element from the head of the array deque (i.e. gets the tail element)
	 */
	public E getLast() {
		E element = elements[(tail - 1) & (elements.length - 1)];
		if (element == null) {
			throw new NoSuchElementException("No element at " + ((tail - 1) & (elements.length - 1)));
		}
		return element;
	}

	/**
	 * Gets the element at the specified index from the array deque
	 */
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("Attempted to get " + index + " but size " + size());
		}
		return elements[(head + index) % elements.length];
	}

	/**
	 * Gets the first element from the head of the array deque and sets its element to the provided one
	 */
	public void set(int index, E element) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("Attempted to set " + index + " but size " + size());
		}
		elements[(head + index) % elements.length] = element;
	}

	/**
	 * Returns the array deque's capacity
	 */
	public int capacity() {
		return elements.length;
	}

	/**
	 * Returns the array deque's size, i.e. the number of elements in the array
	 */
	public int size() {
		return (tail - head) & (elements.length - 1);
	}

	/**
	 * Returns a copy array representation of the array deque in non-circular format
	 */
	public Object[] toArray() {
		return copyElements(new Object[size()]);
	}
}
