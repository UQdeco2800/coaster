package uq.deco2800.coaster.game.preservation;

import uq.deco2800.coaster.game.entities.Player;

/**
 * Created by ryanj on 13/08/2016.
 */
public class ExportablePlayer extends ExportableMovingEntity {


	//Needed for reasons stated in the variable
	//This will be gone eventually.

	//public String specialVariable = "Json deserializer doesn't know the difference between the two if they aren't different...";

	public int currentGold;

	/**
	 * ExportablePlayer default constructor is needed for importing from json
	 */

	public ExportablePlayer() {
	}

	/**
	 * Converts Player to ExportablePlayer
	 */
	public ExportablePlayer(Player player) {
		super(player);
		currentGold = player.getCommerce().getGold();
	}

}
