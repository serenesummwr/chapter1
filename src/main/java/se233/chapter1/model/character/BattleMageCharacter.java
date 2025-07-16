package se233.chapter1.model.character;

import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.Weapon;

public class BattleMageCharacter extends BasedCharacter{
    public BattleMageCharacter(String name, String imgpath, int basedDef, int basedRes) {
        this.name = name;
        this.type = DamageType.magical;
        this.imgpath = imgpath;
        this.fullHp = 40;
        this.basedPow = 40;
        this.basedDef = basedDef;
        this.basedRes = basedRes;
        this.hp = this.fullHp;
        this.power = this.basedPow;
        this.defense = this.basedDef;
        this.resistance = this.basedRes;
    }

    /**
     * BattleMage can equip weapons of any DamageType.
     * @param weapon The weapon to check
     * @return true if the weapon is not null
     */
    @Override
    public boolean canEquipWeapon(Weapon weapon) {
        return weapon != null;
    }

    /**
     * BattleMage cannot equip any armor.
     * @param armor The armor to check
     * @return false (BattleMage cannot equip armor)
     */
    @Override
    public boolean canEquipArmor(Armor armor) {
        return false;
    }
}
