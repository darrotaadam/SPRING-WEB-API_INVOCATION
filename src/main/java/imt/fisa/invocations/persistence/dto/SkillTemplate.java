package imt.fisa.invocations.persistence.dto;

public class SkillTemplate {

    private int num;
    private int dmg;
    private String ratio_stat;
    private int ratio_percent;
    private int cooldown;
    private float lootrate;

    public SkillTemplate(int num, int dmg, String ratio_stat, int ratio_percent, int cooldown, float lootrate) {
        this.num = num;
        this.dmg = dmg;
        this.ratio_stat = ratio_stat;
        this.ratio_percent = ratio_percent;
        this.cooldown = cooldown;
        this.lootrate = lootrate;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public String getRatio_stat() {
        return ratio_stat;
    }

    public void setRatio_stat(String ratio_stat) {
        this.ratio_stat = ratio_stat;
    }

    public int getRatio_percent() {
        return ratio_percent;
    }

    public void setRatio_percent(int ratio_percent) {
        this.ratio_percent = ratio_percent;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public float getLootrate() {
        return lootrate;
    }

    public void setLootrate(float lootrate) {
        this.lootrate = lootrate;
    }
}
