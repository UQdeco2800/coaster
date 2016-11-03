package uq.deco2800.coaster.game.items.enchantments;


/**
 * Created by Jeevan on 23/10/16.
 * Will be used to declare enchantments on a piece of armour
 */
public interface EnchantmentDecorator {


    /**
     * Getter for the base damage multiplier of the armour. This is used in Player.java
     * and in tests
     *
     * @return float representing the multiplier to the base damage of the player.
     */
    float getDamageMulti();

    /**
     * Getter for the base crit damage multiplier of the armour. This is used in Player.java
     * and in tests.
     *
     * @return float representing the multiplier to the crit damage of the player.
     */
    float getCritDamageMulti();


    /**
     * Getter for the additional Health variable of the armour.
     * This will be used in player stats to modify a player's health
     *
     * @return int representing the amount of health to be added to the player
     */
    int getAdditionalHealth();

    /**
     * Getter for the additional Mana variable of the armour.
     * This will be used in player stats to modify a player's mana
     *
     * @return int representing the amount of mana to be added to the player
     */
    int getAdditionalMana();

    /**
     * Returns a description modifier based on the current enchantment
     *
     * @return string with enchantment lore
     */
    String getDescription();
}
