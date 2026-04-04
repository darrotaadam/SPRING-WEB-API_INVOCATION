package imt.fisa.invocations.persistence.dto;

public class SkillTemplate {

    private Integer num;
    private Integer dmg;
    private String ratio_stat;
    private Integer ratio_percent;
    private Integer cooldown;
    private Float lootrate;

    public SkillTemplate(Integer num, Integer dmg, String ratio_stat, Integer ratio_percent, Integer cooldown, Float lootrate) {
        this.num = num;
        this.dmg = dmg;
        this.ratio_stat = ratio_stat;
        this.ratio_percent = ratio_percent;
        this.cooldown = cooldown;
        this.lootrate = lootrate;
    }


    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getDmg() {
        return dmg;
    }

    public void setDmg(Integer dmg) {
        this.dmg = dmg;
    }

    public String getRatio_stat() {
        return ratio_stat;
    }

    public void setRatio_stat(String ratio_stat) {
        this.ratio_stat = ratio_stat;
    }

    public Integer getRatio_percent() {
        return ratio_percent;
    }

    public void setRatio_percent(Integer ratio_percent) {
        this.ratio_percent = ratio_percent;
    }

    public Integer getCooldown() {
        return cooldown;
    }

    public void setCooldown(Integer cooldown) {
        this.cooldown = cooldown;
    }

    public Float getLootrate() {
        return lootrate;
    }

    public void setLootrate(float lootrate) {
        this.lootrate = lootrate;
    }
}
