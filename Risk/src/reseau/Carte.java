package reseau;

import java.io.*;
import java.util.*;

/**
 * Représente la carte du jeu sous forme de graphe.
 *
 * <p>Chaque territoire est un nœud et chaque frontière est un lien non orienté
 * entre deux territoires voisins.</p>
 */

public class Carte implements java.io.Serializable {

    private final Map<Territoire, TreeSet<Territoire>> connexions;
    private static final int MINIMAL_INFANTERIE = 2;

    /**
     * Crée une carte vide sans territoire.
     */

    public Carte() {
        this.connexions = new HashMap<>();
    }

    /**
     * Retourne une vue non modifiable des connexions de la carte.
     *
     * @return carte des territoires et de leurs voisins
     */

    public Map<Territoire, TreeSet<Territoire>> getConnexions() {
        return Collections.unmodifiableMap(connexions);
    }

    /**
     * Ajoute un territoire à la carte.
     *
     * @param territoire territoire à ajouter
     * @throws IllegalArgumentException si le territoire est nul
     */

    public void ajouterTerritoire(Territoire territoire) {
        if (territoire == null) {
            throw new IllegalArgumentException("Impossible d'ajouter un Agent null!");
        }
        this.connexions.put(territoire, new TreeSet<Territoire>());
    }

    /**
     * Crée un lien bidirectionnel entre deux territoires.
     *
     * @param a premier territoire
     * @param b second territoire
     * @throws NoSuchElementException si un des territoires n'existe pas dans la carte
     * @throws IllegalArgumentException si un des paramètres est nul ou si les deux territoires sont identiques
     */

    public void lierTerritoire(Territoire a, Territoire b) throws NoSuchElementException, IllegalArgumentException {
        // les territoires doivent exister!
        if (a == null || b == null || a.equals(b)) {
            throw new IllegalArgumentException("Les agents doivent être différent et non null");
        }
        // condition qu'il doit y avoir un lien avec la map.
        if (!connexions.containsKey(a) || !connexions.containsKey(b)) {
            throw new NoSuchElementException("Un agent n'a pas de lien au réseau!");
        }
        //on ajoute les connexions.
        connexions.get(a).add(b);
        connexions.get(b).add(a);
    }

    /**
     * Supprime le lien bidirectionnel entre deux territoires.
     *
     * @param a premier territoire
     * @param b second territoire
     * @throws NoSuchElementException si un des territoires n'existe pas dans la carte
     */

    public void rompreLien(Territoire a, Territoire b) {
        //condition qu'il doit y exister une clé.
        if (!connexions.containsKey(a) || !connexions.containsKey(b)) {
            throw new NoSuchElementException("On ne sait pas de quelle lien il est question, on ne le trouve pas!");
        }
        // on retire les liens.
        connexions.get(a).remove(b);
        connexions.get(b).remove(a);
    }

    /**
     * Retire un territoire de la carte et supprime toutes ses connexions.
     *
     * @param cible territoire à retirer
     * @throws NoSuchElementException si le territoire n'existe pas
     */

    public void retirerTerritoire(Territoire cible) {
        if (!connexions.containsKey(cible)) {
            throw new NoSuchElementException("Le territoire cible n'existe pas!");
        }
        //on retire la clé
        connexions.remove(cible);
        // on retire l'Agent de tous les treeSet des autres territoire. celui à gauche c'est l'action et à droite c'est la précision de quoi faire avec le treeSet qu'on a trouvé.
        connexions.values().forEach(treeSet -> treeSet.remove(cible));
    }

    /**
     * Tente une attaque entre deux territoires.
     *
     * <p>Un territoire attaquant ne peut pas attaquer son propre pays, doit être adjacent
     * au défenseur et doit disposer d'au moins deux unités d'infanterie.</p>
     *
     * @param attaquant territoire qui attaque
     * @param defendant territoire qui défend
     * @throws IllegalArgumentException si l'attaque est invalide
     */

    public void attaquer(Territoire attaquant, Territoire defendant) {
        if (connexions.containsKey(attaquant) && connexions.containsKey(defendant)) {
            if (!attaquant.getEtat().equals(defendant.getEtat())) {
                if ((!connexions.get(attaquant).contains(defendant))) {
                    throw new IllegalArgumentException("L'attaquant :" + attaquant.getNom() + " doit avoir un lien avec le défenseur  :" + defendant.getNom());
                } else {
                    if (attaquant.getnBInfanterie() <= 1) {
                        throw new IllegalArgumentException("L'attaquant :" + attaquant.getNom() + "doit avoir au minimum : " + MINIMAL_INFANTERIE);
                    } else {
                        if (attaquant.getnBInfanterie() > defendant.getnBInfanterie()) {
                            defendant.setnBInfanterie(((attaquant.getnBInfanterie() - defendant.getnBInfanterie()) - 1));
                            attaquant.setnBInfanterie(1);
                            defendant.setEtat(attaquant.getEtat());
                        }
                    }


                }

            } else {
                throw new IllegalArgumentException("Tu ne peux attaquer ton propre pays!");
            }


        }
    }

    /**
     * Sauvegarde la carte dans un fichier sérialisé.
     *
     * @param nomFichier chemin du fichier de sortie
     * @throws IOException si l'écriture échoue
     * @throws IllegalArgumentException si le nom de fichier est vide ou nul
     */


    public void sauvegarder(String nomFichier) throws IOException {
        if (nomFichier == null || nomFichier.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du fichier doit exister, là il n'y a rien!");
        }
        // même logique que la création du fichier qu'on est habituer mais j'ai rechercher la logique du ObjectOutputStream.
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            // on lui demande d'écrire l'objet qu'on fait dans le fichier donc c'est une sauvegarde au moment qu'on appelle la méthode.
            objectOutputStream.writeObject(this);
        }
    }

    /**
     * Charge une carte depuis un fichier sérialisé.
     *
     * @param nomFichier chemin du fichier à lire
     * @return carte chargée
     * @throws IOException si la lecture échoue
     * @throws ClassNotFoundException si le fichier ne contient pas une carte valide
     * @throws IllegalArgumentException si le nom de fichier est vide ou nul
     */

    //logique de méthode pour trouver la range nécessaire pour attaquer.

    public static Carte charger(String nomFichier) throws IOException, ClassNotFoundException {
        if (nomFichier == null || nomFichier.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du fichier doit exister, là il n'y a rien!");
        }
        // même logique que tout à l'heure mais à la place d'écrire on lit et bien entendu c'est mieux de préciser que c'est l'objet de cette classe ici.
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(nomFichier))) {
            return (Carte) objectInputStream.readObject();
        }
    }
}
