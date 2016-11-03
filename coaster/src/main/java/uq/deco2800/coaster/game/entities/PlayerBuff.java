package uq.deco2800.coaster.game.entities;

public class PlayerBuff {
	private String stat;
	private float modifier;
	private long time;
	private Boolean applied;

	/**
	 * Class that is parsed when Buff/Debuff is created. Thic class is parsed to the player where player methods deal
	 * with the activation of effect and removal of effect based on the modifier and time parsed in. <p> For certain
	 * buffs such as Health and Mana buffs the time value parsed in is discarded however a valid long must be input. <p>
	 * The modifier effects each buff dirently. <p> stat = health Float = health change time = irrelvent <p> stat = Mana
	 * Float = mana change time = irrelevant <p> Shield stat = shield Float = irrelevant time = time for shield in ms
	 * <p> Weapon stat = weapon Float = irrelvent (always doubles base damage) time = time for powerup to last in ms <p>
	 * Speed stat = speed float = modifier factor (old * modifier) time = time for powerup in ms
	 *
	 * @require a valid instance of each variable type in every constructor
	 */
	public PlayerBuff(String stat, float modifier, long time) {
		this.stat = stat;
		this.modifier = modifier;
		this.time = time;
		this.applied = false;

	}

	public String getStat() {
		return this.stat;
	}

	public float getModifier() {
		return this.modifier;
	}

	public long getTime() {
		return this.time;
	}

	public void countDown(long ms) {
		this.time -= ms;
	}

	public boolean getApplied() {
		return this.applied;
	}

	public void setApplied() {
		this.applied = true;
	}

	public void setNotApplied() {
		this.applied = false;
	}
}