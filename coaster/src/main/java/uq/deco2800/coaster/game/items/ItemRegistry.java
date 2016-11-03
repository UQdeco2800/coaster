package uq.deco2800.coaster.game.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import uq.deco2800.coaster.game.entities.weapons.ProjectileType;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This class contains instances of each possible item.
 *
 * Created by draganmarjanovic on 19/10/2016.
 */
public class ItemRegistry {

	protected static final Map<String, Item> REGISTRY = new HashMap<>();

	private static Random random;

	/**
	 * Generates an instance of all possible items. <p> Note: If you would like to add an item to the game, you will
	 * need to add it to the below list.
	 */
	private static void ItemRegistry() {
		ItemRegistry.random = new Random(World.getMapSeed());
		// Weapons in Registry

		//AKs
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun3", "SpaceK-47", new Sprite(SpriteList.AK_RED), "Avtomat Kalashnikova", ItemType.WEAPON)
				.damage(250).rate(150).bulletSpeed(50).projectileType(ProjectileType.CUSTOM_BULLET)
				.accuracy(1)
				.travelSprite(SpriteList.BULLET_REDTAIL).travelHeight(0.3f).travelWidth(1.4f)
				.actionSprite(SpriteList.EXPLOSION).actionWidth(2f).actionHeight(2f).ammoDeduction(2)
				.rarity(60).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun10", "Ocean AK-47", new Sprite(SpriteList.AK_BLUE), "Must be infused with the power of the sea!", ItemType.WEAPON)
				.damage(120).rate(1).bulletSpeed(50).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_BLUE)
				.ammoDeduction(1).accuracy(1)
				.rarity(60).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun11", "Rusty AK-47", new Sprite(SpriteList.AK_RUSTY), "Might want to wear gloves with this one", ItemType.WEAPON)
				.damage(40).rate(1).bulletSpeed(50).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_REDTAIL)
				.ammoDeduction(1).accuracy(3)
				.rarity(70).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun13", "AK-47", new Sprite(SpriteList.AK), "Bog standard Avtomat Kalashnikova", ItemType.WEAPON)
				.damage(603).rate(100).bulletSpeed(50).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET)
				.ammoDeduction(1).accuracy(2)
				.rarity(100).consumable(false).stackable(false).degradable(true).tradeable(true).build());


		//SMGs
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun2", "Speedy Gun", new Sprite(SpriteList.SMG_NORMAL), "Gotta go fast", ItemType.WEAPON)
				.damage(20).rate(1).bulletSpeed(60).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_SILVER)
				.ammoDeduction(5)
				.rarity(2).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun1", "Starting Gun", new Sprite(SpriteList.HANDGUN), "Don't you know a gun when you see one?", ItemType.WEAPON)
				.damage(50).rate(50).bulletSpeed(30).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET)
				.ammoDeduction(5)
				.rarity(2).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun12", "Diamond SMG ", new Sprite(SpriteList.SMG_D), "Gun made with diamond", ItemType.WEAPON)
				.damage(60).rate(10).bulletSpeed(80).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_BLUE)
				.ammoDeduction(10)
				.rarity(5).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun4", "Grenade Launcher", new Sprite(SpriteList.GRENADELAUNCHER), "Don't hurt yourself!", ItemType.WEAPON)
				.damage(150).projectileType(ProjectileType.GRENADE).radius(5).grenadeTime(100).bulletSpeed(1).travelSprite(SpriteList.GRENADE)
				.rate(1000).rarity(140).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun5", "Rocket Launcher", new Sprite(SpriteList.ROCKETLAUNCHER), "Objects appear closer than they are!", ItemType.WEAPON)
				.damage(300).projectileType(ProjectileType.ROCKET).radius(10).grenadeTime(0).bulletSpeed(1).travelSprite(SpriteList.ROCKET)
				.rate(1000).rarity(150).consumable(false).stackable(false).degradable(false).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun6", "Lazor Gun", new Sprite(SpriteList.SPACEGUN), "Keep calm and lazor tag!", ItemType.WEAPON)
				.damage(200).rate(500).bulletSpeed(25).projectileType(ProjectileType.LAZOR).actionSprite(SpriteList.LAZOR_BULLET) 
				.rarity(120).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Melee1", "Starting Sword", new Sprite(SpriteList.SWORD), "Really just a kitchen knife", ItemType.WEAPON)
				.damage(100).projectileType(ProjectileType.MELEE).swingTime(20).actionSprite(SpriteList.SWORD)
				.ammoDeduction(0)
				.rarity(900).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun7", "Sawed Off Shotgun", new Sprite(SpriteList.SHOTGUN), "A proper permit is required to own me", ItemType.WEAPON)
				.damage(100).rate(60).projectileType(ProjectileType.CUSTOM_BULLET).multishot().shots(5).shotArc(10).bulletSpeed(5).range(10)
				.travelSprite(SpriteList.BULLET_PELLET).travelHeight(0.1f).travelWidth(0.2f).ammoDeduction(1)
				.rarity(140).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun8", "Soil Gun", new Sprite(SpriteList.SOILGUN), "Places blocks!", ItemType.WEAPON)
				.damage(0).rate(-500).bulletSpeed(50).accuracy(-5).soilAdder(true).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET)
				.rarity(190).consumable(false).stackable(false).degradable(false).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun9", "Portal Gun", new Sprite(SpriteList.PORTALGUN), "The cake is a lie", ItemType.WEAPON)
				.damage(0).rate(100).bulletSpeed(20).accuracy(1).projectileType(ProjectileType.PORTAL)
				.rarity(150).consumable(false).stackable(false).degradable(false).tradeable(true).build());
		addRegistryItem(
				new Item.Builder("Landmine1", "Landmine", new Sprite(SpriteList.LANDMINE), "explode when player steps on it", 
						ItemType.WEAPON)
						.build()
		);
		addRegistryItem(
				new Item.Builder("Chest1", "Chest", new Sprite(SpriteList.CHEST), "generates item when player collide", 
						ItemType.NOTHING)
						.build()
		);
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun10", "AK-47 BLUE ", new Sprite(SpriteList.AK_BLUE), "Places blocks!", ItemType.WEAPON)
				.damage(20).rate(1).bulletSpeed(60).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_SILVER)
				.ammoDeduction(1)
				.rarity(190).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun11", "AK-47 RUSTY ", new Sprite(SpriteList.AK_RUSTY), "RUSTY!", ItemType.WEAPON)
				.damage(10).rate(1).bulletSpeed(30).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_REDTAIL)
				.ammoDeduction(1)
				.rarity(150).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun14", "RED SMG ", new Sprite(SpriteList.SMG_RED), "Just a red SMG", ItemType.WEAPON)
				.damage(80).rate(50).bulletSpeed(70).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_REDTAIL)
				.ammoDeduction(1)
				.rarity(100).consumable(false).stackable(false).degradable(true).tradeable(true).build());
		addRegistryItem(new Weapon.WeaponBuilder
				("Gun15", "RUSTY SMG ", new Sprite(SpriteList.SMG_RUSTY), "Very old Gun", ItemType.WEAPON)
				.damage(2).rate(2).bulletSpeed(2).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.BULLET_SILVER)
				.ammoDeduction(1)
				.rarity(200).consumable(false).stackable(false).degradable(true).tradeable(true).build());
        addRegistryItem(new Weapon.WeaponBuilder
                ("Gun16", "SPACEGUN ", new Sprite(SpriteList.SPACEGUN), "Tons of damage", ItemType.WEAPON)
                .damage(120).rate(2).bulletSpeed(60).projectileType(ProjectileType.BULLET).travelSprite(SpriteList.LAZOR_BULLET)
                .ammoDeduction(1)
                .rarity(200).consumable(false).stackable(false).degradable(true).tradeable(true).build());
        addRegistryItem(
				new Item.Builder("Landtrap1", "Land trap", new Sprite(SpriteList.TRAP), "constricts player movement when player steps on it", 
						ItemType.WEAPON)
						.build()
		);
                

        // Items in Registry
		addRegistryItem(new Item.Builder("Health1", "Health", new Sprite(SpriteList.HEALTH), "Heals Player", ItemType.POWERUP).rarity(5)
				.tradeable(false).stackable(false).build());
		addRegistryItem(new Item.Builder("Speed1", "Speed", new Sprite(SpriteList.SHOE), "Increases Speed", ItemType.POWERUP)
				.rarity(3).tradeable(false).stackable(false).build());
		addRegistryItem(new Item.Builder("Mana1", "Mana", new Sprite(SpriteList.MANA_UP), "Fully Restores Mana", ItemType.POWERUP)
				.rarity(3).tradeable(false).stackable(false).build());
		addRegistryItem(
				new Item.Builder("Shield1", "Shield", new Sprite(SpriteList.SHIELD), "Create Protective Shield", ItemType.POWERUP)
						.rarity(5).tradeable(false).stackable(false).build());
		addRegistryItem(
				new Item.Builder("Weapon1", "Weapon", new Sprite(SpriteList.WEAPON_UP), "Doubles Player BaseDamage + Increases accuracy",
						ItemType.POWERUP).rarity(5).tradeable(false).stackable(false).build());
		addRegistryItem(
				new Item.Builder("Map1", "Map", new Sprite(SpriteList.MAP), "Reveals surrounding area on minimap",
						ItemType.MAP).rarity(5).tradeable(false).stackable(false).build());

		// Commerce
		addRegistryItem(
				new Item.Builder("Coin1", "GoldCoin", new Sprite(SpriteList.COIN), "Coin Pickup", ItemType.COIN)
						.rarity(2).tradeable(false).stackable(false).build()
		);
		
		// Inventory
		addRegistryItem(
				new Item.Builder("emptyslot", "Empty Slot", new Sprite(SpriteList.EMPTY), "Empty Slot", ItemType.NOTHING).stackable(false)
						.build()
		);

		addRegistryItem(
				new Item.Builder("tester", "For tests", new Sprite(SpriteList.EMPTY), "Used for tests", ItemType
						.NOTHING).rarity(1).build()
		);
		
		addRegistryItem(
				new Item.Builder("potion", "Empty Potion", new Sprite(SpriteList.EMPTY_POTION), "No good without something in it", ItemType.MISC)
				.build());

		addRegistryItem(
				new Item.Builder("potion1", "Potion", new Sprite(SpriteList.POTION), "It'll knock you off your chops", ItemType.POWERUP)
						.build()
		);

		addRegistryItem(
				new Item.Builder("potion2", "Red Potion", new Sprite(SpriteList.POTION2), "It's good for your cholesterol", ItemType.POWERUP)
						.build()
		);
		//Enchant Stones
		addRegistryItem(
				new Item.Builder("firestone", "Fire Stone", new Sprite(SpriteList.FIRE_STONE), "Hot hot hot!", ItemType.STONE)
				.build()
		);

		addRegistryItem(
				new Item.Builder("grassstone", "Grass Stone", new Sprite(SpriteList.GRASS_STONE), "One with nature", ItemType.STONE)
				.build()
		);

		addRegistryItem(
				new Item.Builder("waterstone", "Water Stone", new Sprite(SpriteList.WATER_STONE), "Somewhere... beyond the sea", ItemType.STONE)
				.build()
		);

		addRegistryItem(
				new Item.Builder("sunstone", "Sun Stone", new Sprite(SpriteList.SUN_STONE), "Gaze into the light", ItemType.STONE)
				.build()
		);

		addRegistryItem(
				new Item.Builder("moonstone", "Moon Stone", new Sprite(SpriteList.MOON_STONE), "A little piece of the cosmos", ItemType.STONE)
				.build()
		);
		
		addRegistryItem(
				new Item.Builder("mountainstone", "Mountain Stone", new Sprite(SpriteList.MOUNTAIN_STONE), "Powered by the ground you stand on", ItemType.STONE)
				.build()
		);

		// Armour
		//Helms
		addRegistryItem(new Armour.ArmourBuilder
				("helmet", "Rusty Helmet", new Sprite(SpriteList.HELMETRUST), "Used to protect noggins", ItemType.ARMOUR)
				.rank(1).damageMulti(11).critDamageMulti(1f).armourType(ArmourType.HEAD).rarity(6).build()
		);

		addRegistryItem(new Armour.ArmourBuilder
				("helmet2", "Standard Helmet", new Sprite(SpriteList.HELMETBLK), "Used to protect noggins", ItemType.ARMOUR)
				.rank(2).damageMulti(1.3f).critDamageMulti(1.5f).armourType(ArmourType.HEAD).rarity(10).build()
		);

		addRegistryItem(new Armour.ArmourBuilder
				("helmet3", "Blue Helmet", new Sprite(SpriteList.HELMETBLU), "Infused with the power of blue", ItemType.ARMOUR)
				.rank(3).damageMulti(1.6f).critDamageMulti(1.7f).additionalMana(10).armourType(ArmourType.HEAD).rarity(15).build()
		);

		addRegistryItem(new Armour.ArmourBuilder
				("helmet4", "Rock Helmet", new Sprite(SpriteList.HELMETRED), "Infused with red energy..huh", ItemType.ARMOUR)
				.rank(3).damageMulti(1.8f).critDamageMulti(1.9f).additionalHealth(10).armourType(ArmourType.HEAD).
						rarity(20).build()
		);

		//chests
		addRegistryItem(new Armour.ArmourBuilder
				("chestplate", "Rusty Chestplate", new Sprite(SpriteList.CHESTRUST), "Rusty piece of junk", ItemType.ARMOUR)
				.rank(1).damageMulti(1f).critDamageMulti(1f).armourType(ArmourType.CHEST)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(6).build());

		addRegistryItem(new Armour.ArmourBuilder
				("chestplate2", "Chestplate", new Sprite(SpriteList.CHESTBLK), "Used to protect tummies", ItemType.ARMOUR)
				.rank(2).damageMulti(1.3f).critDamageMulti(1.5f).armourType(ArmourType.CHEST)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(10).build());
		
		//This chest piece should be craftable only, and not a drop from NPCs
		addRegistryItem(new Armour.ArmourBuilder
				("rhinochestplate", "Rhino Chestplate", new Sprite(SpriteList.CHESTRHINO), "Forged from the remains of rhinos", ItemType.ARMOUR)
				.rank(2).damageMulti(1.3f).critDamageMulti(1.3f).armourType(ArmourType.CHEST)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(13).build());
		
		//This chest piece should be craftable only, and not a drop from NPCs
		addRegistryItem(new Armour.ArmourBuilder
				("batchestplate", "Bat Chestplate", new Sprite(SpriteList.CHESTBAT), "Forged from the remains of rhinos, given flight through wings", ItemType.ARMOUR)
				.rank(2).damageMulti(1.3f).critDamageMulti(1.6f).armourType(ArmourType.CHEST)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(13).build());

		addRegistryItem(new Armour.ArmourBuilder
				("chestplate3", "Blue Chestplate", new Sprite(SpriteList.CHESTBLU), "A more fashionable chestplate in blue", ItemType.ARMOUR)
				.rank(3).damageMulti(1.6f).critDamageMulti(1.7f).armourType(ArmourType.CHEST)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(15).build());

		addRegistryItem(new Armour.ArmourBuilder
				("chestplate4", "Red Chestplate", new Sprite(SpriteList.CHESTRED), "The most fashionable chestplate on the market", ItemType.ARMOUR)
				.rank(3).damageMulti(1.8f).critDamageMulti(1.9f).armourType(ArmourType.CHEST)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(20).build());
		
		//bottoms
		addRegistryItem(new Armour.ArmourBuilder
				("pants", "Rusty Pants", new Sprite(SpriteList.PANTSRUST), "To protect the butt!", ItemType.ARMOUR)
				.rank(1).damageMulti(1f).critDamageMulti(1f).armourType(ArmourType.PANTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(6).build());
		
		addRegistryItem(new Armour.ArmourBuilder
				("pants2", "Emo Pants", new Sprite(SpriteList.PANTSBLK), "Them skinny jeans!", ItemType.ARMOUR)
				.rank(2).damageMulti(1.3f).critDamageMulti(1.5f).armourType(ArmourType.PANTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(10).build());
		
		addRegistryItem(new Armour.ArmourBuilder
				("rhinopants", "Rhino Pants", new Sprite(SpriteList.PANTSRHINO), "The feel of rhino on your butt", ItemType.ARMOUR)
				.rank(2).damageMulti(1.5f).critDamageMulti(1.6f).armourType(ArmourType.PANTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(10).build());

		addRegistryItem(new Armour.ArmourBuilder
				("pants3", "Blue Pants", new Sprite(SpriteList.PANTSBLU), "Shining bright like a diamond~", ItemType.ARMOUR)
				.rank(3).damageMulti(1.6f).critDamageMulti(1.7f).armourType(ArmourType.PANTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(15).build());

		addRegistryItem(new Armour.ArmourBuilder
				("pants4", "Red Pants", new Sprite(SpriteList.PANTSRED), "The most fashionable pants on the market", ItemType.ARMOUR)
				.rank(3).damageMulti(1.8f).critDamageMulti(1.9f).armourType(ArmourType.PANTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(20).build());
		
		//boots
		addRegistryItem(new Armour.ArmourBuilder
				("boots", "Rusty Boots", new Sprite(SpriteList.BOOTRUST), "It's corroded, you ain't gonna last long out there!", ItemType.ARMOUR)
				.rank(1).damageMulti(1f).critDamageMulti(1f).armourType(ArmourType.BOOTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(6).build());
		
		addRegistryItem(new Armour.ArmourBuilder
				("boots2", "Black Boots", new Sprite(SpriteList.BOOTBLK), "It's Ugg boots, it ain't Crocs", ItemType.ARMOUR)
				.rank(2).damageMulti(1.3f).critDamageMulti(1.5f).armourType(ArmourType.BOOTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(10).build());
		
		addRegistryItem(new Armour.ArmourBuilder
				("swagshoes", "Swag Shoes", new Sprite(SpriteList.BOOTSWAG), "+50 Swag Stat", ItemType.ARMOUR)
				.rank(2).damageMulti(1.5f).critDamageMulti(1.6f).armourType(ArmourType.BOOTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(10).build());

		addRegistryItem(new Armour.ArmourBuilder
				("boots3", "Blue Boots", new Sprite(SpriteList.BOOTBLU), "Cindarellies Glass Boots", ItemType.ARMOUR)
				.rank(3).damageMulti(1.6f).critDamageMulti(1.7f).armourType(ArmourType.BOOTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(15).build());

		addRegistryItem(new Armour.ArmourBuilder
				("boots4", "Red Boots", new Sprite(SpriteList.BOOTRED), "One must always own at least one red boot", ItemType.ARMOUR)
				.rank(3).damageMulti(1.8f).critDamageMulti(1.9f).armourType(ArmourType.BOOTS)
				.rarity(6).consumable(false).stackable(false).degradable(true).tradeable(true).rarity(20).build());
		
		//Crafting Items
		
		addRegistryItem(
				new Item.Builder("wood", "Wood", new Sprite(SpriteList.WOOD), "Get it in the morning", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("crystal", "Crystal Shard", new Sprite(SpriteList.CRYSTAL), "It's a purple rock", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("bone", "Bone", new Sprite(SpriteList.BONE), "sp00ky", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("horn", "Rhino Horn", new Sprite(SpriteList.RHINO_HORN), "Looks deadly", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("batteeth", "Bat Teeth", new Sprite(SpriteList.BAT_TEETH), "A deadly poisonous tooth", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("batwing", "Bat Wing", new Sprite(SpriteList.BAT_WING), "Take to the skies", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("blooddrop", "Blood Drop", new Sprite(SpriteList.BLOOD_DROP), "Be glad it's not yours", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("tears", "Tears", new Sprite(SpriteList.TEARS), "Don't cry Isaac, mother is here", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("slimesoul", "Slime Soul", new Sprite(SpriteList.SLIME_SOUL), "The screams are eternal", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("slime", "Slime", new Sprite(SpriteList.SLIME_DROP), "Sticky and Icky", ItemType.MISC)
				.build());
		
		addRegistryItem(
				new Item.Builder("glass", "Glass", new Sprite(SpriteList.GLASS), "Fragile", ItemType.MISC)
				.build());

		//Ammunition
		addRegistryItem(
				new Item.Builder("ammo", "Normal Ammunition", new Sprite(SpriteList.AMMO), "Used in all non explosive weapons", ItemType.AMMO)
						.rarity(3).tradeable(false).stackable(true).build()
		);
		addRegistryItem(
				new Item.Builder("ex_ammo", "Explosive Ammunition", new Sprite(SpriteList.EX_AMMO), "Used all explosive weapons", ItemType.AMMO)
						.rarity(5).tradeable(false).stackable(true).build()
		);

		// Rooms
		addRegistryItem(new Item.Builder("puzzle_key", "Key", new Sprite(SpriteList.KEY), "Can unlock a door only " +
				"once!", ItemType.PUZZLE_ITEM).tradeable(false).stackable(true).build());
		addRegistryItem(new Item.Builder("duck_king_key", "Duck King Key", new Sprite(SpriteList.DUCK_KING_KEY),
				"Can take you to the Duck King!", ItemType.PUZZLE_ITEM).tradeable(false).stackable(true).build());
		
	}
		

	public static Set<String> itemList() {
		return REGISTRY.keySet();
	}

	public static String reverseLookup(Item i) {
		for (String k : REGISTRY.keySet()) {
			if (REGISTRY.get(k).getName().equals(i.getName())) {
				return k;
			}
		}
		return "";
	}

	/**
	 * Adds the item to the REGISTRY. Designed as a helper function to reduce need for writing an ID twice.
	 *
	 * @param item the item to be added to the registry.
	 */
	private static void addRegistryItem(Item item) {
		REGISTRY.put(item.getID(), item);
	}

	/**
	 * Initialises the item registry if it hasn't already been initialised.
	 */
	private static void checkInitialised() {
		if (REGISTRY.size() == 0) {
			ItemRegistry();
		}
	}

	/**
	 * Returns a requested item from the item registry or initialises the registry if it has not been initialised.
	 *
	 * @param id the id of the item to return.
	 * @return the item referenced by the specified id - null if it does not exist.
	 */
	public static Item getItem(String id) {
		checkInitialised();
		return REGISTRY.get(id);
	}

	/**
	 * Returns a completely random item.
	 *
	 * @return returns a random item
	 */
	public static Item randomItem() {
		checkInitialised();
		ArrayList<Item> possibleItems = new ArrayList<>();
		while (possibleItems.size() < 1) {
			possibleItems = adjacentProbability(random.nextDouble());
		}
		return possibleItems.get(random.nextInt(possibleItems.size()));

	}

	/**
	 * Returns a random item that is of a given type - null if it does not exist.
	 *
	 * @param type the type of the item to be chosen from.
	 * @return id of a random item
	 */
	public static Item randomItem(ItemType type) {
		checkInitialised();

		ArrayList<ItemType> types = new ArrayList<>();
		types.add(type);

		ArrayList<Item> possibleItems = new ArrayList<>();
		while (possibleItems.size() < 1) {
			possibleItems = adjacentProbability(random.nextDouble(), types);
		}
		return possibleItems.get(random.nextInt(possibleItems.size()));
	}



	static ArrayList<Item> adjacentProbability(double probability) {
		ArrayList<Item> itemReturns = new ArrayList<>();

		for (Map.Entry<String, Item> itemEntry : REGISTRY.entrySet()) {
			double entryRarity = itemEntry.getValue().getRarity();

			if (entryRarity > probability) {

				if (itemReturns.isEmpty()) {
					itemReturns.add(itemEntry.getValue());
				} else if (entryRarity < itemReturns.get(0).getRarity()) {
					itemReturns.clear();
					itemReturns.add(itemEntry.getValue());
				} else if (entryRarity == itemReturns.get(0).getRarity()) {
					itemReturns.add(itemEntry.getValue());
				}

			}
		}

		return itemReturns;
	}

	/**
	 * Returns the item with closest drop probability to the given probability. If multiple items
	 * of the same drop probability exist it will return all of them.
	 *
	 * @param probability the probability to check for.
	 * @param types the allowed drop types.
	 * @return a list of items with drop probability closest to the specified probability.
	 */
	static ArrayList<Item> adjacentProbability(double probability, List types) {
		ArrayList<Item> itemReturns = new ArrayList<>();

		for (Map.Entry<String, Item> itemEntry : REGISTRY.entrySet()) {
			double entryRarity = itemEntry.getValue().getRarity();
			if (entryRarity > probability && types.contains(itemEntry.getValue().getType())) {
				if (itemReturns.isEmpty()) {
					itemReturns.add(itemEntry.getValue());
				} else if (entryRarity < itemReturns.get(0).getRarity()) {
					itemReturns.clear();
					itemReturns.add(itemEntry.getValue());
				} else if (entryRarity == itemReturns.get(0).getRarity()) {
					itemReturns.add(itemEntry.getValue());
				}
			}
		}

		return itemReturns;
	}


}
