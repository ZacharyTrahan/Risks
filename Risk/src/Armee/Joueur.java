package Armee;

import reseau.Territoire;

public class Joueur {

    protected int id;
    protected int nbInfanterie;
    protected int nbSniper;
    protected int nbTanks;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbInfanterie() {
        return nbInfanterie;
    }

    public void setNbInfanterie(int nbInfanterie) {
        this.nbInfanterie = nbInfanterie;
    }

    public int getNbSniper() {
        return nbSniper;
    }

    public void setNbSniper(int nbSniper) {
        this.nbSniper = nbSniper;
    }

    public int getNbTanks() {
        return nbTanks;
    }

    public void setNbTanks(int nbTanks) {
        this.nbTanks = nbTanks;
    }

    public int compareTo(Joueur j) {
        return Integer.compare(this.id, j.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Joueur)) return false;
        Joueur autre = (Joueur) obj;
        return this.id == autre.id;
    }
}
