package reseau;

import java.io.*;
import java.util.*;

public class Carte implements java.io.Serializable {

    private final Map<Territoire, TreeSet<Territoire>> connexions;

    public Carte() {
        this.connexions = new HashMap<>();
    }

    public Map<Territoire, TreeSet<Territoire>> getConnexions() {
        return Collections.unmodifiableMap(connexions);
    }

    public void ajouterTerritoire(Territoire territoire) {
        if (territoire == null){
            throw new IllegalArgumentException("Impossible d'ajouter un Agent null!");
        }
        this.connexions.put(territoire, new TreeSet<Territoire>());
    }

    public void lierTerritoire(Territoire a, Territoire b) throws NoSuchElementException, IllegalArgumentException {
        // les territoires doivent exister!
        if (a == null || b == null || a.equals(b)){
            throw new IllegalArgumentException("Les agents doivent être différent et non null");
        }
        // condition qu'il doit y avoir un lien avec la map.
        if (!connexions.containsKey(a) || !connexions.containsKey(b)){
            throw new NoSuchElementException("Un agent n'a pas de lien au réseau!");
        }
        //on ajoute les connexions.
        connexions.get(a).add(b);
        connexions.get(b).add(a);
    }

    public void rompreLien(Territoire a, Territoire b) {
        //condition qu'il doit y exister une clé.
        if (!connexions.containsKey(a) || !connexions.containsKey(b)){
            throw new NoSuchElementException("On ne sait pas de quelle lien il est question, on ne le trouve pas!");
        }
        // on retire les liens.
        connexions.get(a).remove(b);
        connexions.get(b).remove(a);
    }

    public void retirerTerritoire(Territoire cible) {
        if (!connexions.containsKey(cible)){
            throw new NoSuchElementException("Le territoire cible n'existe pas!");
        }
        //on retire la clé
        connexions.remove(cible);
        // on retire l'Agent de tous les treeSet des autres territoire. celui à gauche c'est l'action et à droite c'est la précision de quoi faire avec le treeSet qu'on a trouvé.
        connexions.values().forEach(treeSet -> treeSet.remove(cible));
    }

    public List<Territoire> identifierSuperPropagateurs(int seuil) {
        if (seuil < 0){
            throw new IllegalArgumentException("Le seuil est négatif!");
        }
        //c'est la future liste de tout les territoires qui sont des superPropagateurs.
        List<Territoire> superPropagateurs = new ArrayList<>();
        // on définit l'objet qu'on manipule et pour parcourir une map on a besions de entrySet() et Entry. entree parce qu'on manipule l'Entry de connexions.
        for (Map.Entry<Territoire, TreeSet<Territoire>> entree : connexions.entrySet()){

            if (entree.getValue() != null && entree.getValue().size() >= seuil){
                superPropagateurs.add(entree.getKey());
            }
        }
        return superPropagateurs;
    }

    public void sauvegarder(String nomFichier) throws IOException {
        if (nomFichier == null || nomFichier.trim().isEmpty()){
            throw new IllegalArgumentException("Le nom du fichier doit exister, là il n'y a rien!");
        }
        // même logique que la création du fichier qu'on est habituer mais j'ai rechercher la logique du ObjectOutputStream.
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(nomFichier))){
            // on lui demande d'écrire l'objet qu'on fait dans le fichier donc c'est une sauvegarde au moment qu'on appelle la méthode.
            objectOutputStream.writeObject(this);
        }
    }

    public static Carte charger(String nomFichier) throws IOException, ClassNotFoundException {
        if (nomFichier == null || nomFichier.trim().isEmpty()){
            throw new IllegalArgumentException("Le nom du fichier doit exister, là il n'y a rien!");
        }
        // même logique que tout à l'heure mais à la place d'écrire on lit et bien entendu c'est mieux de préciser que c'est l'objet de cette classe ici.
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(nomFichier))){
            return (Carte) objectInputStream.readObject();
        }
    }
}
