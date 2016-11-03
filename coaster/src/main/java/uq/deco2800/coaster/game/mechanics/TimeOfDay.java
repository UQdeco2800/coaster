package uq.deco2800.coaster.game.mechanics;

/**
 *  An enum containg some information used to run the day night cycle
 * 
 * i.e. phases of the day
 * 
 * @author Cieran
 *
 */
public enum TimeOfDay {
	DAWN,
	MORNING,
	DAY,
	AFTERNOON,
	DUSK,
	EVENING,
	NIGHT,
	PRE_DAWN;
	
	// time 0 is 6am;
	public final static long DAY_LENGTH = 180_000L;
	public final static long HOUR_LENGTH = DAY_LENGTH / 24;
	
	public final static long SUN_RISE = HOUR_LENGTH * 4;
	public final static long SUN_SET = HOUR_LENGTH * 9;
	
	public final static long MOON_RISE = HOUR_LENGTH * 13;
	public final static long MOON_SET = HOUR_LENGTH * 22;

	/** 
	 * returns the next phase in the sequence. Loops around to the start when finished
	 */
	public TimeOfDay next() {
		return values()[(this.ordinal() + 1) % values().length];
	}
	
	/**
	 * 	Returns the duration for this particular phase in ms
	 *  full day is 600 000 ms = 10 minutes
	 * 
	 */
	public long getDuration() {
		switch (this) {
			case DAWN:
				return HOUR_LENGTH * 2; 
			case MORNING:
				return HOUR_LENGTH * 2;  
			case DAY:
				return HOUR_LENGTH * 4;
			case AFTERNOON:
				return HOUR_LENGTH * 3;
			case DUSK:
				return HOUR_LENGTH * 2;
			case EVENING:
				return HOUR_LENGTH * 2;
			case NIGHT:
				return HOUR_LENGTH * 7;
			case PRE_DAWN:
				return HOUR_LENGTH * 2;
			default:
				return HOUR_LENGTH;
		}
	}
	
	/**
	 * Returns the default light level at this phase
	 */
	public int getLightLevel() {
		switch (this) {
			case DAWN:
				return 25;
			case MORNING:
				return 20;
			case DAY:
				return 20;
			case AFTERNOON:
				return 20;
			case DUSK:
				return 30;
			case EVENING:
				return 35;
			case NIGHT:
				return 40;
			case PRE_DAWN:
				return 30;
			default:
				return 0;
		}
	}
	
	/** 
	 * Takes a double between 0 and 1, representing the fraction of the phase that has passed
	 * Returns the light level for that point in that phase
	 */
	public int getLightGradient(double fraction) {
		switch (this) {
			case DAWN:
				return (int) (-5 * fraction + 25);
			case MORNING:
				return 20;
			case DAY:
				return 20;
			case AFTERNOON:
				return (int) (10 * fraction + 20);
			case DUSK:
				return (int) (5 * fraction + 30);
			case EVENING:
				return (int) (5 * fraction + 35);
			case NIGHT:
				return (int) (-10 * fraction + 40);
			case PRE_DAWN:
				return (int) (-5 * fraction + 30);
			default:
				return 20;
		}
	}

}

