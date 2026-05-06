package reseau;

import java.io.Serializable;
import java.util.Objects;

public class Territoire implements Serializable, Comparable<Territoire> {

    private Etat etat;
    private String nom;
    private final static Etat ETAT_BASE = Etat.NEUTRE;
    private static final long serialVersionUID = 1L;
    private int nBInfanterie;
    private static final int INTERVALMIN = 3;
    private static final int INTERVALMAX = 5;




    public Territoire(String nom) {
        this(nom, ETAT_BASE);
    }

    public Territoire(String nom, Etat etat) {
        setEtat(etat);
        this.nom = nom;
        setnBInfanterie(((int) (Math.random() * (INTERVALMAX - INTERVALMIN + 1)) + INTERVALMIN));
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        isValid(etat);
        this.etat = etat;
    }

    public int getnBInfanterie() {
        return nBInfanterie;
    }

    public void setnBInfanterie(int nBInfanterie) {
        this.nBInfanterie = nBInfanterie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public int compareTo(Territoire autre) {
        if (autre == null) {
            throw new NullPointerException("Impossible de comparer avec un objet null");
        }
        // Compare les noms en ordre alphabétique
        return this.nom.compareTo(autre.nom);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Territoire)) return false;
        Territoire autre = (Territoire) obj;
        return this.nom == autre.nom;

    }

    @Override
    public String toString() {
        return "Agent{" +
                "etat=" + etat +
                ", nom=" + nom +
                ", serialVersionUID=" + serialVersionUID +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }


    public boolean isValid(Etat etat) {
        boolean option = true;
        if (etat == null) {
            throw new IllegalArgumentException("etat nulle");
        }
        return option;
    }


}
