package uq.deco2800.coaster.game.items;

import uq.deco2800.coaster.game.items.enchantments.EmptyEnchant;
import uq.deco2800.coaster.game.items.enchantments.EnchantmentDecorator;
import uq.deco2800.coaster.graphics.sprites.Sprite;

/**
 * Created by Hayley on 21/10/2016. The armour class is implemented via creating hitboxes. The hitboxes apply a
 * multiplier to the damage recieved. The tiers of armour and their damage multiplier can be found in BodyPart.java.
 * The armour class uses the builder pattern.
 */
public class Armour extends Item {
    private int rank;
    private float damageMulti;
    private float critDamageMulti;;
    private int additionalHealth;
    private int additionalMana;
    private ArmourType type;
    private EnchantmentDecorator enchantment = new EmptyEnchant();

    /**
     * Getter for the additional Health variable of the armour.
     * This will be used in player stats to modify a player's health
     *
     * @return int representing the amount of health to be added to the player
     */
    public int getAdditionalHealth() {
        return additionalHealth + enchantment.getAdditionalHealth();
    }


    /**
     * Getter for the additional Mana variable of the armour.
     * This will be used in player stats to modify a player's mana
     *
     * @return int representing the amount of mana to be added to the player
     */
    public int getAdditionalMana() {
        return additionalMana + enchantment.getAdditionalMana();
    }

    /**
     * Getter for the rank of the armour. Used to determine which hitbox to create in player.java
     * and in tests
     *
     * @return an int representing the armour's rank (1 -3 )
     */
    public int getRank() {
        return rank;
    }


    /**
     * Getter for the base damage multiplier of the armour. This is used in Player.java
     * and in tests
     *
     * @return float representing the multiplier to the base damage of the player.
     */
    public float getDamageMulti() {
        return damageMulti + enchantment.getDamageMulti();
    }

    /**
     * Getter for the base crit damage multiplier of the armour. This is used in Player.java
     * and in tests.
     *
     * @return float representing the multiplier to the crit damage of the player.
     */
    public float getCritDamageMulti() {
        return critDamageMulti + enchantment.getCritDamageMulti();
    }

    /**
     * Getter for the type of armour the item is, e.g. helmet
     *
     * @return ArmourType enum stating what type of armour this is
     */
    public ArmourType getArmourType() {
        return type;
    }

    /**
     * Set the armour's current enchantment to an enchantment instance
     *
     * @param enchantment the enchantment to be placed on the armour
     */
    public void setEnchantment(EnchantmentDecorator enchantment) {
        this.enchantment = enchantment;
    }

    /**
     * Returns the current enchantment
     *
     * @return enchantment
     */
    public EnchantmentDecorator getEnchantment() {
        return enchantment;
    }

    public String getDescription() {
        return super.getDescription() + enchantment.getDescription();
    }
    
    /**
     * The builder used for the armour item.
     */
    protected static class ArmourBuilder extends Item.Builder {

        private int rank;
        private float damageMulti;
        private float critDamageMulti;
        private int additionalHealth = 0;
        private int additionalMana = 0;
        private ArmourType type;

        public ArmourBuilder(String id, String name, Sprite sprite, String description, ItemType type) {
            super(id, name, sprite, description, type);
        }

        public ArmourBuilder rank(int rank) {
            this.rank = rank;
            return this;
        }

        public ArmourBuilder damageMulti(float damageMulti) {
            this.damageMulti = damageMulti;
            return this;
        }


        public ArmourBuilder critDamageMulti(float critDamageMulti) {
            this.critDamageMulti = critDamageMulti;
            return this;
        }

        public ArmourBuilder additionalHealth(int additionalHealth) {
            this.additionalHealth = additionalHealth;
            return this;
        }

        public ArmourBuilder additionalMana(int additionalMana) {
            this.additionalMana = additionalMana;
            return this;
        }

        public ArmourBuilder armourType(ArmourType type) {
            this.type = type;
            return this;
        }

        public Armour build() {
            return new Armour(this);
        }
    }

    public Armour(ArmourBuilder builder) {
        super(builder);
        rank = builder.rank;
        damageMulti = builder.damageMulti;
        critDamageMulti = builder.critDamageMulti;
        additionalHealth = builder.additionalHealth;
        additionalMana = builder.additionalMana;
        type = builder.type;
    }

}
