package uq.deco2800.coaster.core.sound; /**
 * This is to test if all sound resources that are used in the game, can be loaded into the game.
 */

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import uq.deco2800.coaster.TestHelper;

public class SoundLoadCheckTest {
	public SoundCache soundCache = new SoundCache();

	@Before
	public void load() {
		TestHelper.load();
	}

	@Test
	public void CheckAllSounds() throws IOException, InvalidMidiDataException {
		SoundLoad.loadSound();
	}
}
