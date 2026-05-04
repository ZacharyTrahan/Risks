
import reseau.*;

import java.io.IOException;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        // Création du dossier de données s'il n'existe pas
        File dir = new File("src/donnees");
        if (!dir.exists()) dir.mkdirs();

        Carte maCarte = new Carte();

        // 1. Création de quelques territoires
        Territoire france = new Territoire("France");
        Territoire espagne = new Territoire("Espagne");
        Territoire allemagne = new Territoire("Allemagne");
        Territoire italie = new Territoire("Italie");

        // 2. Ajout au réseau
        maCarte.ajouterIndividu(france);
        maCarte.ajouterIndividu(espagne);
        maCarte.ajouterIndividu(allemagne);
        maCarte.ajouterIndividu(italie);

        // 3. Création des frontières (liens)
        try {
            maCarte.lierIndividus(france, espagne);
            maCarte.lierIndividus(france, allemagne);
            maCarte.lierIndividus(france, italie);
            maCarte.lierIndividus(allemagne, italie);

            // Sauvegarde de la carte initiale
            maCarte.sauvegarder("src/donnees/map_europe.ser");
            System.out.println("Carte sauvegardée avec succès !");

            System.out.println("=== LOGIQUE DE JEU ===");
            // (À toi de remplir : Logique de tour, choix des joueurs, etc.)
            System.out.println("En attente des actions joueurs...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}