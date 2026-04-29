package reseau;

import java.io.Serializable;

public class Territoire implements Serializable {

    private Etat etat;
    private String nom;
    private final static Etat ETAT_BASE = Etat.NEUTRE;
    private static final long serialVersionUID=1L;

    public Territoire(String nom){
        this(nom,ETAT_BASE);
    }
    public Territoire(String nom, Etat etatBase){
        setEtat(etat);
        this.nom = nom;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}
