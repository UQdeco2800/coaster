package uq.deco2800.coaster.game.items;

import java.util.Random;

import uq.deco2800.coaster.game.entities.CoinDrop;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.PowerUp;
import uq.deco2800.coaster.game.entities.QuantityDrop;
import uq.deco2800.coaster.game.entities.WeaponDrop;
import uq.deco2800.coaster.game.world.World;

/**
 * This class provides functions to assist with dropping
 * items into the world randomly.
 *
 * Created by draganmarjanovic on 22/10/2016.
 */
public class ItemDrop {

	private static World world;
	private static Random random;

	static {
		world = World.getInstance();
		random = new Random();
	}

	public static void randomDrop(float xPos, float yPos) {
		Item dropItem = ItemRegistry.randomItem();
		drop(dropItem, xPos, yPos);
	}

	public static void drop(Item drop, float xPos, float yPos) {
		switch (drop.getType()) {
			case POWERUP:
				powerupDrop(drop, xPos, yPos);
				break;
			case COIN:
				dropCoin(drop, xPos, yPos);
				break;
			case WEAPON:
				dropWeapon(drop, xPos, yPos);
				break;
			case AMMO:
//				dropAmmo(drop, xPos, yPos);
				dropQuantityItem(drop, xPos, yPos);
				break;
			default:
				ItemEntity entityDrop = new ItemEntity(drop, 1f, 1f);
				entityDrop.setPosition(xPos, yPos);
				world.addEntity(entityDrop);
		}
	}

	// Different handlers and initialisers
	private static void powerupDrop(Item drop, float xPos, float yPos) {
		PowerUp newdrop;

		if (drop.getID().contains("Speed")) {
			newdrop = new PowerUp(drop, "speed", 1.5f, 10000, 10000);
		} else if (drop.getID().contains("Mana")) {
			newdrop = new PowerUp(drop, "mana", 50f, -1, 10000);
		} else if (drop.getID().contains("Shield")) {
			newdrop = new PowerUp(drop, "shield", 1f, 10000, 10000);
		} else if (drop.getID().contains("Weapon")) {
			newdrop = new PowerUp(drop, "weapon", 2f, 15000, 10000);
		} else {
			newdrop = new PowerUp(drop, "health", 50f, -1, 10000); //default
		}

		newdrop.setPosition(xPos, yPos);
		world.addEntity(newdrop);

	}

	private static void dropCoin(Item drop, float xPos, float yPos) {
		CoinDrop coinDrop = new CoinDrop(drop, 1);
		coinDrop.setPosition(xPos, yPos);
		world.addEntity(coinDrop);
	}


	private static void dropWeapon(Item drop, float xPos, float yPos) {
		Integer gunID = random.nextInt(6) + 1;
		String idString = gunID.toString();
		Item weapon = ItemRegistry.getItem("Gun" + idString);
		WeaponDrop weaponDrop = new WeaponDrop(weapon);
		weaponDrop.setPosition(xPos, yPos);
		world.addEntity(weaponDrop);
	}



	private static void dropQuantityItem(Item drop, float xPos, float yPos) {
		int ammoCount = random.nextInt(5) + 1;
		QuantityDrop quantityDrop = new QuantityDrop(drop, ammoCount);
		quantityDrop.setPosition(xPos, yPos);
		world.addEntity(quantityDrop);
	}



}
