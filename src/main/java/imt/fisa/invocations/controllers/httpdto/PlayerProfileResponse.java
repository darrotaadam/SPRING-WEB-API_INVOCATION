package imt.fisa.invocations.controllers.httpdto;

import imt.fisa.player.persistence.dto.Monstres;

import java.util.List;

public class PlayerProfileResponse {

    private String identifiant;
    private int level;
    private int experience;
    private List<Monstres> monstres;


    public PlayerProfileResponse(String identifiant, int level, int experience, List<Monstres> monstres) {
        this.identifiant = identifiant;
        this.level = level;
        this.experience = experience;
        this.monstres = monstres;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Monstres> getMonstres() {
        return monstres;
    }

    public void setMonstres(List<Monstres> monstres) {
        this.monstres = monstres;
    }
}
