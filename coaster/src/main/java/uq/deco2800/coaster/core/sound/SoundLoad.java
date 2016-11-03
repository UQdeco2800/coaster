package uq.deco2800.coaster.core.sound;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

/**
 * This is a utility class to load sounds into the SoundCache. Put all sound loads into the loadSound method.
 */
public class SoundLoad {

	private SoundLoad() {
	}

	/**
	 * Loads all the sounds used in the game.
	 *
	 * @throws IOException When a file doesn't exist in the sound resource folder.
	 */
	public static void loadSound() throws IOException, InvalidMidiDataException {
		SoundCache.getInstance().loadSound("jump", "effects/jump.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("attack", "effects/BasicAttack.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("duckDead", "effects/duck1.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("duckHurt", "effects/duck2.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("coin", "effects/Coin.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("critical", "effects/CriticalAttack.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("damaged", "effects/DamageTaken.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("highDead", "effects/HighDead.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("highHit", "effects/HighHit.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("lowDead", "effects/LowDead.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("lowHit", "effects/LowHit.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("PortalAttack", "effects/PortalAttack.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("secretCode", "effects/secret.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("PowerUp", "effects/Powerup.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("click", "effects/GUIClick.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("back", "effects/GUIBack.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("lightning", "effects/lightning.wav", SoundType.EFFECT);
		SoundCache.getInstance().loadSound("title", "midi/title.mid", SoundType.MIDI);
		SoundCache.getInstance().loadSound("game", "midi/game.mid", SoundType.MIDI);
	}
}
