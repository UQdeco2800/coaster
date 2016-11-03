package uq.deco2800.coaster.game.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.TestHelper;

public class AABBTest {

	@Before
	public void load() {
		TestHelper.load();
	}

	@Test
	public void testAdjacentNotCollisions() {
		AABB first = new AABB(0, 0, 1, 1);
		List<AABB> compares = new ArrayList<AABB>();
		compares.add(new AABB(0, 1, 1, 1));
		compares.add(new AABB(1, 0, 1, 1));
		compares.add(new AABB(1, 1, 1, 1));

		for (AABB aabb : compares) {
			assert !first.collides(aabb);
		}
	}

	@Test
	public void testAdjacentCollisions() {
		AABB first = new AABB(0, 0, 1, 1);
		List<AABB> compares = new ArrayList<AABB>();
		compares.add(new AABB(0, 0.99f, 1, 1));
		compares.add(new AABB(0.99f, 0, 1, 1));
		compares.add(new AABB(0.99f, 0.99f, 1, 1));

		for (AABB aabb : compares) {
			assert first.collides(aabb);
		}
	}

	@Test
	public void testIntersectingCollision() {
		AABB first = new AABB(0, 0, 1, 1);
		List<AABB> compares = new ArrayList<AABB>();
		compares.add(new AABB(0, 0.5f, 1, 1));
		compares.add(new AABB(0, 0, 1, 1));
		compares.add(new AABB(0.1f, 0.1f, 0.8f, 0.8f));

		for (AABB aabb : compares) {
			assert first.collides(aabb);
		}
	}

	@Test
	public void testOutside() {
		AABB first = new AABB(0, 0, 1, 1);
		List<AABB> compares = new ArrayList<AABB>();
		compares.add(new AABB(0, 10, 1, 1));
		compares.add(new AABB(10, 0, 1, 1));
		compares.add(new AABB(10, 10, 1, 1));

		for (AABB aabb : compares) {
			assert !first.collides(aabb);
		}
	}

	@Test
	public void testWideBox() {
		AABB first = new AABB(0, 0, 10, 1);
		List<AABB> compares = new ArrayList<AABB>();
		compares.add(new AABB(0, 0, 1, 1));
		compares.add(new AABB(9, 0, 1, 1));
		compares.add(new AABB(5, 0.5f, 1, 1));

		for (AABB aabb : compares) {
			assert first.collides(aabb);
		}
	}
}
