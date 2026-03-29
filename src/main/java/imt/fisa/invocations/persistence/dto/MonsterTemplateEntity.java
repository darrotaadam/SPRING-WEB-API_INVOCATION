package imt.fisa.invocations.persistence.dto;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "monster_templates")
public class MonsterTemplateEntity {
    @MongoId
    private String id;

    private String name;

    private String element;

    private int hp;

    private int atk;

    private int def;

    private int vit;

    private SkillTemplate[] skills;

    private int level;

    private int exp;

    private Double dropRate;

    public MonsterTemplateEntity(String id, String name, String element, int hp, int atk, int def, int vit, SkillTemplate[] skills, int level, int exp, Double dropRate) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.vit = vit;
        this.skills = skills;
        this.level = level;
        this.exp = exp;
        this.dropRate = dropRate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getVit() {
        return vit;
    }

    public void setVit(int vit) {
        this.vit = vit;
    }

    public SkillTemplate[] getSkills() {
        return skills;
    }

    public void setSkills(SkillTemplate[] skills) {
        this.skills = skills;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public Double getDropRate() {
        return dropRate;
    }

    public void setDropRate(Double dropRate) {
        this.dropRate = dropRate;
    }
}

