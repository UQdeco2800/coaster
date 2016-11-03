package uq.deco2800.coaster.core.sound;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

import javax.sound.midi.InvalidMidiDataException;

import javafx.scene.media.MediaException;
import uq.deco2800.coaster.TestHelper;

/**
 * Test the SoundCache class for the implementation of sound effects.
 */
public class SoundEffectsTest {


	@Before
	public void load() {
		TestHelper.load();
	}

	@Test
	public void testLoadSoundExist1() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance().loadSound("laser", "effects/BasicAttack.wav", SoundType.EFFECT);
		Assert.assertFalse(SoundCache.getInstance().loadSound("laser", "effects/BasicAttack.wav", SoundType.EFFECT));
	}

	@Test
	public void testLoadSoundExists2() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance().loadSound("laser", "effects/BasicAttack.wav", SoundType.EFFECT);
		Assert.assertFalse(SoundCache.getInstance().loadSound("laser", "effects/jump.wav", SoundType.SAMPLED));

	}

	@Test
	public void testLoadSound() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance();
		SoundCache.reset();
		Assert.assertTrue(SoundCache.getInstance().loadSound("jump", "effects/jump.wav", SoundType.EFFECT));
	}

	@Test(expected = FileNotFoundException.class)
	public void loadNonExistentSound() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance().loadSound("giggle", "jokes/giggle.wav", SoundType.EFFECT);
	}

	@Test
	public void playNoMixSound() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance();
		SoundCache.mute();
		SoundCache.getInstance().loadSound("laser", "effects/BasicAttack.wav", SoundType.EFFECT);
		SoundCache.play("laser");
	}

	@Test
	public void playMixSound() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance().loadSound("laser", "effects/BasicAttack.wav", SoundType.EFFECT);
		SoundCache.getInstance().play("laser", 0, 0, 0, 0, 0);
	}

	@Test
	/* Testing all changes in mute state.*/
	public void muteTest() {
		SoundCache.getInstance();
		SoundCache.mute();
		SoundCache.getInstance();
		Assert.assertTrue(SoundCache.getMute());
		SoundCache.getInstance();
		SoundCache.mute();
		SoundCache.getInstance();
		Assert.assertTrue(SoundCache.getMute());
		SoundCache.getInstance();
		SoundCache.unmute();
		SoundCache.getInstance();
		Assert.assertFalse(SoundCache.getMute());
		SoundCache.getInstance();
		SoundCache.unmute();
		SoundCache.getInstance();
		Assert.assertFalse(SoundCache.getMute());
		SoundCache.getInstance();
		SoundCache.mute();
		SoundCache.getInstance();
		Assert.assertTrue(SoundCache.getMute());
	}

	@Ignore
	@Test
	public void stopAllEmptyTest() {
		SoundCache.getInstance().stopAll();
	}

	@Test
	public void stopNotPlaying() {
		SoundCache.getInstance().stopEffect("laser");
	}

	@Ignore
	@Test
	public void stopAllNonEmptyTest() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance().loadSound("laser", "effects/BasicAttack.wav", SoundType.EFFECT);
		SoundCache.getInstance().stopAll();
	}

	@Test(expected = NoSuchElementException.class)
	public void playSoundNotLoaded1() throws InvalidMidiDataException {
		SoundCache.play("giggle");
	}

	@Test(expected = NoSuchElementException.class)
	public void playSoundNotLoaded2() {
		SoundCache.getInstance().play("giggle", 0.0, 0.0, 0.0, 0.0, 1);
	}

	@Test(expected = NoSuchElementException.class)
	public void stopSoundNotLoaded() {
		SoundCache.getInstance().stopEffect("giggle");
	}

	@Test(expected = MediaException.class)
	public void loadNotSound() throws IOException, InvalidMidiDataException{
		SoundCache.getInstance().loadSound("notEffect", "midi/title.mid", SoundType.EFFECT);
	}
	@Test
	public void volumeAboveRange(){
		SoundCache.setVolume(2.0);
		Assert.assertEquals(1.0, SoundCache.getVolume(),0.0);
	}
	@Test
	public void volumeBelowRange(){
		SoundCache.setVolume(-2.0);
		Assert.assertEquals(0.0, SoundCache.getVolume(), 0.0);
	}

	@Test
	public void volumeMiddle(){
		SoundCache.setVolume(0.5);
		Assert.assertEquals(0.5, SoundCache.getVolume(), 0.0);
	}

	@AfterClass
	public static void reloadSound() throws InvalidMidiDataException, IOException{
		SoundCache.getInstance();
		SoundCache.reset();
		SoundLoad.loadSound();
	}

}
