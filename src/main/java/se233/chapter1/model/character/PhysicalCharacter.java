package se233.chapter1.model.character;

public class PhysicalCharacter extends BasedCharacter {
    public PhysicalCharacter(String name, String imgpath, int basedDef, int basedRes) {
        this.name = name;
        this.type = DamageType.physical;
        this.imgpath = imgpath;
        this.fullHp = 50;
        this.basedPow = 30;
        this.basedDef = basedDef;
        this.basedRes = basedRes;
        this.hp = this.fullHp;
        this.power = this.basedPow;
        this.defense = this.basedDef;
        this.resistance = this.basedRes;
    }
}
