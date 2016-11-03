package uq.deco2800.coaster.game.commerce;

import uq.deco2800.coaster.game.entities.npcs.CommerceNPC;

/**
 * A class to contain all Commerce related properties and functions which are to be available/attached to the player.
 *
 * Motivation: Reduce code clutter in the main player class and improve maintainability.
 *
 * Created by draganmarjanovic on 18/09/2016.
 */
public class PlayerCommerce {

	private int gold;
	private Store nearestStore;
	private Bank bank;

	/**
	 * Constructor for PlayerCommerce.
	 */
	public PlayerCommerce() {
		this.gold = 0;
		this.nearestStore = null;
		this.bank = new Bank();
	}

	/**
	 * Get player's current gold
	 *
	 * @return player's gold count.
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * Retrieves the stored closest store to the player
	 *
	 * @return whichever store has been stored as the closest to the player
	 */
	public Store getNearestStore() {
		return nearestStore;
	}

	/**
	 * Returns the players Bank.
	 *
	 * @return the players bank
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * Returns the players net value which is the sum of the
	 * banked gold and that on their person.
	 *
	 * @return players netvalue
	 */
	public int getNetValue() {
		int netValue = 0;
		netValue += this.gold;
		netValue += bank.getGold();
		return netValue;
	}

	/**
	 * Updates the players nearest store.
	 *
	 * @param nearestStore the nearest store.
	 */
	public void setNearestStore(Store nearestStore) {
		this.nearestStore = nearestStore;
	}

	/**
	 * Updates the players nearest store.
	 *
	 * @param storeNPC the nearest store NPC.
	 */
	public void setNearestStore(CommerceNPC storeNPC) {
		setNearestStore(storeNPC.getStore());
	}

	/**
	 * Adds to the players gold count
	 *
	 * @param extraGold The amount of gold to add.
	 */
	public void addGold(int extraGold) {
		this.gold += extraGold;
	}


	/**
	 * Reduces the player's gold count
	 *
	 * @param deduction How much gold to deduct from the player.
	 * @return How much gold was actually deducted
	 */
	public Integer reduceGold(int deduction) {
		Integer deductedAmount;
		if (deduction <= this.gold) {
			this.gold -= deduction;
			deductedAmount = deduction;
		} else {
			// More deducted than available - deduct all
			deductedAmount = this.gold;
			this.gold = 0;
		}
		return deductedAmount;
	}

}