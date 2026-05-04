package Armee;

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
}
