package se233.chapter1.model.character;

import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.Weapon;

public class BasedCharacter {
    protected String name, imgpath;
    protected DamageType type;
    protected Integer fullHp, basedPow, basedDef, basedRes;
    protected Integer hp, power, defense, resistance;
    protected Weapon weapon;
    protected Armor armor;

    public String getName() {
        return name;
    }

    public String getImagepath() {
        return imgpath;
    }

    public Integer getFullHp() {
        return fullHp;
    }

    public Integer getHp() {
        return hp;
    }

    public Integer getPower() {
        return power;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getResistance() {
        return resistance;
    }

    public void equipWeapon(Weapon weapon) {
        if (weapon != null && !canEquipWeapon(weapon)) {
            throw new IllegalArgumentException("Character cannot equip weapon of type " + weapon.getDamageType());
        }
        this.weapon = weapon;
        if (weapon == null) {
            this.power = this.basedPow;
        } else {
            this.power = this.basedPow + weapon.getPower();
        }
    }

    public void equipArmor(Armor armor) {
        if (armor != null && !canEquipArmor(armor)) {
            throw new IllegalArgumentException("Character cannot equip armor");
        }
        this.armor = armor;
        if (armor == null) {
            this.defense = this.basedDef;
            this.resistance = this.basedRes;
        } else {
            this.defense = this.basedDef + armor.getDefense();
            this.resistance = this.basedRes + armor.getResistance();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public DamageType getType() {
        return type;
    }

    /**
     * Check if the character can equip a weapon.
     * By default, a character can only equip weapons of their own DamageType.
     * @param weapon The weapon to check
     * @return true if the character can equip the weapon
     */
    public boolean canEquipWeapon(Weapon weapon) {
        return weapon != null && weapon.getDamageType() == this.type;
    }

    /**
     * Check if the character can equip armor.
     * By default, all characters can equip armor.
     * @param armor The armor to check
     * @return true if the character can equip the armor
     */
    public boolean canEquipArmor(Armor armor) {
        return true;
    }

}
