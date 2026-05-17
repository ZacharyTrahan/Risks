package reseau;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représente un territoire de la carte Risk.
 *
 * <p>Un territoire possède un nom, un état (joueur propriétaire ou neutre)
 * et un nombre d'infanterie associé.</p>
 */

public class Territoire implements Serializable, Comparable<Territoire> {

    private Etat etat;
    private String nom;
    private final static Etat ETAT_BASE = Etat.NEUTRE;
    private static final long serialVersionUID = 1L;
    private int nBInfanterie;
    private static final int INTERVALMIN = 300;
    private static final int INTERVALMAX = 5000;



    /**
     * Crée un territoire neutre avec un nom donné.
     *
     * @param nom nom du territoire
     */

    public Territoire(String nom) {
        this(nom, ETAT_BASE);
    }

    /**
     * Crée un territoire avec un nom et un état initial.
     *
     * @param nom nom du territoire
     * @param etat état initial du territoire
     */

    public Territoire(String nom, Etat etat) {
        setEtat(etat);
        this.nom = nom;
        setnBInfanterie(((int) (Math.random() * (INTERVALMAX - INTERVALMIN + 1)) + INTERVALMIN));
    }

    /**
     * Retourne l'état actuel du territoire.
     *
     * @return état du territoire
     */

    public Etat getEtat() {
        return etat;
    }

    /**
     * Modifie l'état du territoire.
     *
     * @param etat nouvel état à affecter
     * @throws IllegalArgumentException si l'état est nul
     */

    public void setEtat(Etat etat) {
        isValid(etat);
        this.etat = etat;
    }

    /**
     * Retourne le nombre d'infanterie du territoire.
     *
     * @return nombre d'infanterie
     */

    public int getnBInfanterie() {
        return nBInfanterie;
    }

    /**
     * Définit le nombre d'infanterie du territoire.
     *
     * @param nBInfanterie nouveau nombre d'infanterie
     */

    public void setnBInfanterie(int nBInfanterie) {
        this.nBInfanterie = nBInfanterie;
    }

    /**
     * Retourne le nom du territoire.
     *
     * @return nom du territoire
     */

    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom du territoire.
     *
     * @param nom nouveau nom du territoire
     */

    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Compare deux territoires selon leur nom.
     *
     * @param autre territoire à comparer
     * @return résultat de la comparaison lexicographique
     * @throws NullPointerException si le territoire à comparer est nul
     */

    public int compareTo(Territoire autre) {
        if (autre == null) {
            throw new NullPointerException("Impossible de comparer avec un objet null");
        }
        // Compare les noms en ordre alphabétique
        return this.nom.compareTo(autre.nom);
    }

    /**
     * Indique si ce territoire est égal à un autre objet.
     *
     * <p>L'égalité repose sur le nom du territoire.</p>
     *
     * @param obj objet à comparer
     * @return {@code true} si les deux objets représentent le même territoire
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Territoire)) return false;
        Territoire autre = (Territoire) obj;
        return this.nom == autre.nom;

    }

    /**
     * Retourne une représentation textuelle du territoire.
     *
     * @return texte décrivant le territoire
     */

    @Override
    public String toString() {
        return "Agent{" +
                "etat=" + etat +
                ", nom=" + nom +
                ", serialVersionUID=" + serialVersionUID +
                '}';
    }

    /**
     * Retourne le code de hachage basé sur le nom du territoire.
     *
     * @return hash code du territoire
     */

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

    /**
     * Valide l'état fourni.
     *
     * @param etat état à valider
     * @return toujours {@code true} si l'état n'est pas nul
     * @throws IllegalArgumentException si l'état est nul
     */

    public boolean isValid(Etat etat) {
        boolean option = true;
        if (etat == null) {
            throw new IllegalArgumentException("etat nulle");
        }
        return option;
    }


}
