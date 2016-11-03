package uq.deco2800.coaster.core.sound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.SysexMessage;

import javafx.scene.media.AudioClip;

/**
 * SoundCache Where all the sound loaded into the game is stored. This is a
 * singleton class so only instance of it exists. It also exists globally for
 * any class to access.
 */
public class SoundCache {

	private static final Logger logger = LoggerFactory.getLogger(SoundCache.class);

	//Start the class singleton instance.
	private static SoundCache ourInstance = new SoundCache();

	private static class DelayedSound {
		public final String identifier;
		private long remainingMs;

		public DelayedSound(String identifier, long delayMs) {
			this.identifier = identifier;
			this.remainingMs = delayMs;
		}

		public long getRemainingMs() {
			return remainingMs;
		}

		public void setRemainingMs(long newRemainingMs) {
			this.remainingMs = newRemainingMs;
		}
	}

	private static List<DelayedSound> delayedSounds = new ArrayList<DelayedSound>();

	private static long midiDelay = -1;

	/**
	 * Returns the Singleton object instance.
	 *
	 * @return The single SoundCache instance.
	 */
	public static SoundCache getInstance() {
		return ourInstance;
	}

	public SoundCache() {
		try {
			midiSequencer = MidiSystem.getSequencer(false);
			midiReceiver = MidiSystem.getReceiver();
			midiAvailable = true;
			logger.info("MIDI is available on this system.");
		} catch (MidiUnavailableException error) {
			logger.warn("MIDI is unsupported on this system.", error);
			midiAvailable = false;
		}
		setVolume(0.5);
	}

	/**
	 * A hashmap that uniquely stores all AudioClips.
	 */
	private static HashMap<String, AudioClip> effectsCollection = new HashMap<>();

	/**
	 * A hashmap that uniquely stores all MIDI Sequences.
	 */
	private static HashMap<String, Sequence> midiCollection = new HashMap<>();

	private static double volume;

	private static boolean midiAvailable;

	private static Sequencer midiSequencer;

	private static Receiver midiReceiver;

	private static long midiTickPosition = 0;

	private static boolean paused = false;

	private static boolean muted = false;

	public static final double VOLUME_STEP = 0.1;

	/**
	 * Mute all sounds.
	 */
	public static void mute() {
		muted = true;
		stopMidi();
	}

	/**
	 * Unmute all sounds.
	 */
	public static void unmute() {
		muted = false;
		resumeMidi();
	}

	/**
	 * Change the volume of all sounds.
	 *
	 * @param volumeValue A double to set the volume of all sounds between 0.0
	 *            and 1.0. If the parameter is higher or lower than this range,
	 *            then the volume is clamped to the end of the ranges.
	 */
	public static void setVolume(double volumeValue) {
		if (volumeValue < 0.0) {
			volume = 0.0;
		} else if (volumeValue > 1.0) {
			volume = 1.0;
		} else {
			volume = volumeValue;
		}
		if (midiAvailable) {
			/*
			 * Construct a system exclusive MIDI message to send a real time
			 * message to request a volume change.
			 */
			boolean midiNeedsToBeClosed = false;
			if (!midiSequencer.isOpen()) {
				openMidi();
				midiNeedsToBeClosed = true;
			}

			Double volumeDouble = volumeValue * 127.0;
			byte volumeByte = (byte) volumeDouble.intValue();
			try {
				MidiMessage volumeMessage = new SysexMessage(SysexMessage.SYSTEM_EXCLUSIVE,
						new byte[] { 0x7F, 0x7F, 0x04, 0x01, 0x00, volumeByte }, 6);
				/*
				 * REFERENCE: This MIDI message: "7F 7F 04 01 LL MM" is used
				 * from this Stack Overflow question:
				 * http://stackoverflow.com/questions/23170223/how-to-change-the
				 * -master-volume-of-a-synthesizer-sequencer The answer here
				 * describes the appropriate message needed to be sent to MIDI
				 * receiver and not an implementation as how to send such
				 * message.
				 */
				midiReceiver.send(volumeMessage, -1);
			} catch (InvalidMidiDataException exception) {
				throw new RuntimeException(exception.getMessage());
			} finally {
				if (midiNeedsToBeClosed) {
					midiSequencer.close();
				}
			}
		}
	}

	/**
	 * Get the volume currently set in the SoundCache.
	 *
	 * @return Return the volume level of the SoundCache.
	 */
	public static double getVolume() {
		return volume;
	}

	/**
	 * Resets the entire SoundCache class.
	 */
	public static void reset() {
		effectsCollection = new HashMap<>();
		midiCollection = new HashMap<>();
	}

	public static void tick(long ms) {
		//Iterate through the list of delayed sounds and play them if their time is now
		for (int i = delayedSounds.size() - 1; i >= 0; i--) {
			DelayedSound delayedSound = delayedSounds.get(i);
			long newTime = delayedSound.getRemainingMs() - ms;
			//Play them if their delay is up
			if (newTime <= 0) {
				play(delayedSound.identifier);
				delayedSounds.remove(i);
			} else {
				delayedSound.setRemainingMs(newTime);
			}
		}

		//Resume midi delay
		if (midiDelay != -1) {
			midiDelay -= ms;
			if (midiDelay <= 0) {
				resumeMidi();
				midiDelay = -1;
			}
		}
	}

	/**
	 * Load the sound into the cache.
	 *
	 * @param identifier The unique identifier to store the sound data. This is
	 *            unique across all sound types.
	 * @param source The path location relative to src/main/resources/sound/
	 * @param type The type of the sound to be store into the cache. This
	 *            determines to what object the sound is played by.
	 * @return Returns true if sound was successfully loaded into SoundCache.
	 *         False if a sound already exists with identifier.
	 * @throws IOException If there was a problem opening a file.
	 * @throws InvalidMidiDataException If the standard MIDI file is malformed.
	 */
	public boolean loadSound(String identifier, String source, SoundType type)
			throws InvalidMidiDataException, IOException {

		//If a sound is being loaded with an identifier already being used, return false.
		if (effectsCollection.containsKey(identifier) || midiCollection.containsKey(identifier)) {
			return false;
		}

		/*
		 * Attempt to create a file object and see if such a file exists.
		 * Otherwise throw a exception that it doesn't exist.
		 */
		Path soundPath = Paths.get("src", "main", "resources", "sounds", source);
		URI soundLocation = soundPath.toUri();
		File soundFile = new File(soundLocation);

		if (!soundFile.exists()) {
			throw new FileNotFoundException();
		}

		//If the sound is an Effect, create an AudioClip. If it is a MIDI create a sequence.
		if (type == SoundType.EFFECT) {
			effectsCollection.put(identifier, new AudioClip(soundLocation.toString()));
			return true;
		} else if (type == SoundType.MIDI) {
			Sequence sequence = MidiSystem.getSequence(soundFile);
			midiCollection.put(identifier, sequence);
			return true;
		} else {
			throw new IllegalArgumentException("Audio type does not exist.");
		}
	}

	/**
	 * Plays a sound that has been loaded into the game, after the specified
	 * amount of time.
	 *
	 * @param identifier Play the sound that has this unique identifier.
	 * @param delayMs The amount of time to wait before playing the sound, in
	 *            milliseconds.
	 */
	public void playDelayed(String identifier, long delayMs) {
		delayedSounds.add(new DelayedSound(identifier, delayMs));
	}

	/**
	 * Play a sound that has been loaded into the game.
	 *
	 * @param identifier Play the sound that has this unique identifier.
	 */
	public static void play(String identifier) {
		if (effectsCollection.containsKey(identifier)) {
			if (muted) {
				effectsCollection.get(identifier).play(0.0);
			} else {
				effectsCollection.get(identifier).play(volume);
			}
		} else if (midiCollection.containsKey(identifier)) {
			if (muted || !midiAvailable) {
				return;
			}
			try {
				if (midiSequencer.isOpen()) {
					midiSequencer.stop();
					midiSequencer.close();
					midiTickPosition = 0;
				}
				midiSequencer.setSequence(midiCollection.get(identifier));
				midiSequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				openMidi();
				midiSequencer.open();
				midiSequencer.start();
			} catch (InvalidMidiDataException | MidiUnavailableException error) {
				throw new RuntimeException(error.getMessage());
			}
		} else {
			doesNotExist(identifier);
		}
	}

	/**
	 * Stops every sound that has been loaded into the game.
	 */
	public void stopAll() {
		for (String identifier : effectsCollection.keySet()) {

			this.stopEffect(identifier);
		}
	}

	/**
	 * Stops a particular Effects sound playing.
	 *
	 * @param identifier The identifier of the sound that is playing.
	 */
	public void stopEffect(String identifier) {
		if (effectsCollection.containsKey(identifier)) {
			if (effectsCollection.get(identifier).isPlaying()) {
				effectsCollection.get(identifier).stop();
			}
		} else {
			doesNotExist(identifier);
		}
	}

	/**
	 * Pause MIDI from playing. Used when the game is paused.
	 */
	public static void pauseMidi() {
		if (midiAvailable) {
			paused = true;
			midiTickPosition = midiSequencer.getTickPosition();
			midiSequencer.stop();
		}
	}

	/**
	 * Resume midi from being paused.
	 */
	public static void resumeMidi() {
		if (midiAvailable && midiSequencer.isOpen() && !midiSequencer.isRunning()) {
			if (paused) {
				paused = false;
				midiSequencer.setTickPosition(midiTickPosition);
				midiSequencer.start();
			} else {
				midiTickPosition = 0;
				midiSequencer.setTickPosition(0);
				midiSequencer.start();
			}
		}
	}

	/**
	 * Resume midi from being paused, after a delay.
	 *
	 * @param delayMs The amount of time to wait before resuming midi.
	 */
	public static void resumeMidiDelayed(long delayMs) {
		midiDelay = delayMs;
	}

	/**
	 * Stop midi from playing.
	 */
	public static void stopMidi() {
		if (midiAvailable && midiSequencer.isRunning()) {
			midiSequencer.stop();
		}
	}

	/**
	 * Close the midi sequencer.
	 */
	public static void closeMidi() {
		if (midiAvailable && midiSequencer.isOpen()) {
			stopMidi();
			midiSequencer.close();
		}
	}

	private static boolean openMidi() {
		try {
			midiSequencer.getTransmitter().setReceiver(midiReceiver);
			midiSequencer.open();
			return true;
		} catch (MidiUnavailableException exception) {
			logger.error("MidiUnavailable", exception);
			return false;
		}
	}

	private static void doesNotExist(String identifier) {
		throw new NoSuchElementException("Sound with identifier " + identifier + " does not exist");

	}

	/**
	 * Much more complex sound playing. Simplify wraps the AudioClip Super
	 *
	 * @param identifier A unique identifier to store the sound in the cache
	 * @param volume The volume to play the sound at
	 * @param balance The volume level of each sound
	 * @param rate The bitrate of playback.
	 * @param pan The rate at which the sound is balanced to.
	 * @param priority The priority ot the sound to be played.
	 */
	public void play(String identifier, double volume, double balance, double rate, double pan, int priority) {
		if (effectsCollection.containsKey(identifier)) {
			if (muted) {
				effectsCollection.get(identifier).play(0.0, balance, rate, pan, priority);
			} else {
				effectsCollection.get(identifier).play(volume, balance, rate, pan, priority);
			}
		} else {
			doesNotExist(identifier);
		}
	}

	/**
	 * Gets the mute state of all sounds.
	 *
	 * @return Returns if the state of all sounds being muted
	 */
	public static boolean getMute() {
		return muted;
	}

}
