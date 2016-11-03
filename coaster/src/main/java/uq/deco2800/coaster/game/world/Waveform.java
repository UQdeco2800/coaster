package uq.deco2800.coaster.game.world;

/**
 * Simple class that holds the properties of a waveform
 */
public class Waveform {
	private int period;
	private int amplitude;

	/**
	 * Waveform constructor holding the period and amplitude properties
	 */
	public Waveform(int period, int amplitude) {
		this.period = period;
		this.amplitude = amplitude;
	}

	/**
	 * Returns the amplitude of the waveform
	 */
	int getAmplitude() {
		return amplitude;
	}

	/**
	 * Returns the period of the waveform
	 */
	int getPeriod() {
		return period;
	}
}
