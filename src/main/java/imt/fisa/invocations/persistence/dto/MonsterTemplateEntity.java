package imt.fisa.invocations.persistence.dto;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "monster_templates")
public class MonsterTemplateEntity {
    @MongoId
    private String id;

    private String name;

    private String element;

    private Integer hp;

    private Integer atk;

    private Integer def;

    private Integer vit;

    private SkillTemplate[] skills;

    private Integer level;

    private Integer exp;

    private Double lootRate;

    public MonsterTemplateEntity(String id, String name, String element, Integer hp, Integer atk, Integer def, Integer vit, SkillTemplate[] skills, Integer level, Integer exp, Double lootRate) {
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
        this.lootRate = lootRate;
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

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public Integer getVit() {
        return vit;
    }

    public void setVit(Integer vit) {
        this.vit = vit;
    }

    public SkillTemplate[] getSkills() {
        return skills;
    }

    public void setSkills(SkillTemplate[] skills) {
        this.skills = skills;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Double getLootRate() {
        return lootRate;
    }

    public void setLootRate(Double lootRate) {
        this.lootRate = lootRate;
    }
}

