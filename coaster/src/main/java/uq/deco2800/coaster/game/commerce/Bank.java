package uq.deco2800.coaster.game.commerce;

/**
 * The backend to the Commerce banking system.
 *
 * Created by draganmarjanovic on 16/09/2016.
 */
public class Bank {

	// Stores the amount of gold held by the user.
	private Integer gold;

	public Bank() {
		gold = 0;
	}

	/**
	 * Returns the amount of gold in the bank.
	 *
	 * @return amount of gold in the bank.
	 */
	public Integer getGold() {
		return gold;
	}

	/**
	 * Adds the specified amount of gold to the bank.
	 *
	 * @param amount amount of gold to add.
	 * @return amount of gold in the bank after deposit.
	 */
	public Integer addGold(Integer amount) {
		gold += amount;
		return getGold();
	}

	/**
	 * Removes the specified amount of gold from the bank.
	 *
	 * @return amount of gold withdrawn.
	 */
	public Integer withdrawGold(Integer amount) {
		Integer withdrawalAmount;
		if (amount <= this.gold) {
			this.gold -= amount;
			withdrawalAmount = amount;
		} else {
			withdrawalAmount = this.gold;
			this.gold = 0;
		}
		return withdrawalAmount;
	}
}
