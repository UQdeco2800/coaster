package uq.deco2800.coaster.game;

import org.junit.Test;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.game.debug.Debug;
import uq.deco2800.coaster.game.world.World;

import static org.junit.Assert.assertEquals;

/**
 * Created by WilliamHayward on 21-8-16
 */
public class DebugTest {
	private World world = World.getInstance();
	private Debug debug;

	/**
	 * testHideShow tests that a debug class is hiding and showing as intended
	 */
	@Test
	public void testHideShow() {
		init();
		debug.addToDebugString("Visible");
		assertEquals(debug.getDebugString(), "Visible\n");
		debug.toggleDebug(); //Hide debug
		assertEquals(debug.getDebugString(), "");
		debug.toggleDebug(); //Show debug
		assertEquals(debug.getDebugString(), "Visible\n");
		debug.toggleDebug(); //Hide debug
		assertEquals(debug.getDebugString(), "");
		debug.toggleDebug(); //Show debug again
		assertEquals(debug.getDebugString(), "Visible\n"); //Should not have changed
	}

	/**
	 * testAddSingle tests if a single piece of text can be properly added
	 */
	@Test
	public void testAddSingle() {
		init();
		debug.addToDebugString("Line 1");
		assertEquals(debug.getDebugString(), "Line 1\n");
	}

	/**
	 * testAddMultiple tests if multiple pieces of text can be properly added
	 */
	@Test
	public void testAddMultiple() {
		init();
		debug.addToDebugString("Line 1");
		debug.addToDebugString("Line 2");
		assertEquals(debug.getDebugString(), "Line 1\nLine 2\n");
	}

	/**
	 * testClear tests if a single piece of text can be properly added
	 */
	@Test
	public void testClear() {
		init();
		debug.addToDebugString("Line 1");
		debug.clearDebugString();
		assertEquals(debug.getDebugString(), "");
	}

	/**
	 * testAddSingle tests if a single piece of text can be properly added
	 */
	@Test
	public void testClearAndAdd() {
		init();
		debug.addToDebugString("Line 1");
		debug.clearDebugString();
		debug.addToDebugString("Line 2");
		assertEquals(debug.getDebugString(), "Line 2\n");
	}

	/**
	 * testHideShow tests that a debug class is hiding and showing as intended
	 */
	@Test
	public void testHideShowChannel() {
		init();
		debug.addToDebugString("test", "Visible");
		assertEquals(debug.getDebugString("test"), "Visible\n");
		debug.toggleDebug("test"); //Hide debug
		assertEquals(debug.getDebugString("test"), "");
		debug.toggleDebug("test"); //Show debug
		assertEquals(debug.getDebugString("test"), "Visible\n");
		debug.toggleDebug("test"); //Hide debug
		assertEquals(debug.getDebugString("test"), "");
		debug.toggleDebug("test"); //Show debug again
		assertEquals(debug.getDebugString("test"), "Visible\n"); //Should not have changed
	}

	/**
	 * testAddSingle tests if a single piece of text can be properly added
	 */
	@Test
	public void testAddSingleToChannel() {
		init();
		debug.addToDebugString("test", "Line 1");
		assertEquals(debug.getDebugString("test"), "Line 1\n");
	}

	/**
	 * testAddMultiple tests if multiple pieces of text can be properly added
	 */
	@Test
	public void testAddMultipleToChannel() {
		init();
		debug.addToDebugString("test", "Line 1");
		debug.addToDebugString("test", "Line 2");
		assertEquals(debug.getDebugString("test"), "Line 1\nLine 2\n");
	}

	/**
	 * testAddMultiple tests if multiple pieces of text can be properly added
	 */
	@Test
	public void testAddToMultipleChannels() {
		init();
		debug.addToDebugString("test1", "First Channel");
		debug.addToDebugString("test2", "Second Channel");
		assertEquals(debug.getDebugString("test1"), "First Channel\n");
		assertEquals(debug.getDebugString("test2"), "Second Channel\n");
	}

	/**
	 * testClear tests if a single piece of text can be properly added
	 */
	@Test
	public void testClearChannel() {
		init();
		debug.addToDebugString("test", "Line 1");
		debug.clearDebugString("test");
		assertEquals(debug.getDebugString("test"), "");
	}

	/**
	 * testAddSingle tests if a single piece of text can be properly added
	 */
	@Test
	public void testClearChannelAndAdd() {
		init();
		debug.addToDebugString("test", "Line 1");
		debug.clearDebugString("test");
		debug.addToDebugString("test", "Line 2");
		assertEquals(debug.getDebugString("test"), "Line 2\n");
	}

	/**
	 * testGetAll tests if text can be retrieved from all channels
	 */
	@Test
	public void testGetAllChannels() {
		init();
		debug.addToDebugString("test1", "First Channel");
		debug.addToDebugString("test2", "Second Channel");
		assertEquals(debug.getAllDebugStrings(),
				"First Channel\nSecond Channel\n");
	}

	/**
	 * testGetAll tests if text can be retrieved from all channels
	 */
	@Test
	public void testClearAllChannels() {
		init();
		debug.addToDebugString("test1", "First Channel");
		debug.addToDebugString("test2", "Second Channel");
		debug.clearAllDebugStrings();
		assertEquals(debug.getAllDebugStrings(), "");
	}


	//TODO: Add tests for toggling individual channels

	private void init() {
		TestHelper.load();
		world.debugReset();
		debug = world.getDebug();
	}
}
