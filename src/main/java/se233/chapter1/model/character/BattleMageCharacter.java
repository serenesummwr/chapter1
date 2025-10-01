package se233.chapter1.model.character;

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
}
