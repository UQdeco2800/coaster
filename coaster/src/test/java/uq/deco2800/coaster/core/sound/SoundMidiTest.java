package uq.deco2800.coaster.core.sound;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uq.deco2800.coaster.TestHelper;

import javax.sound.midi.InvalidMidiDataException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Test the SoundCache class for the implementation of MIDI.
 */
public class SoundMidiTest {

	@Before
	public void load() {
		TestHelper.load();
	}

	@Test
	public void testLoadMidiExist1() throws IOException, InvalidMidiDataException {
		Assert.assertTrue(SoundCache.getInstance().loadSound("testLoadMidi1", "midi/title.mid", SoundType.MIDI));
		Assert.assertFalse(SoundCache.getInstance().loadSound("testLoadMidi1", "midi/title.mid", SoundType.MIDI));
	}

	@Test
	public void testLoadMIDI() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance();
		SoundCache.reset();
		Assert.assertTrue(SoundCache.getInstance().loadSound("game", "midi/game.mid", SoundType.MIDI));
	}

	@Test(expected = FileNotFoundException.class)
	public void loadNonExistentMidi() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance().loadSound("idonotexist", "music/jokes.mid", SoundType.MIDI);
	}

	@Test
	public void stopMidi() {
		SoundCache.getInstance();
		SoundCache.stopMidi();
	}

	@Test
	public void closeMidi() {
		SoundCache.getInstance();
		SoundCache.closeMidi();
	}

	@AfterClass
	public static void reloadSound() throws InvalidMidiDataException, IOException {
		SoundCache.getInstance();
		SoundCache.reset();
		SoundLoad.loadSound();
	}

}
