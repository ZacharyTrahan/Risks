//Code complet assisté par Gemini
import reseau.*;

import java.io.IOException;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        // Création du dossier de données s'il n'existe pas
        File dir = new File("Risk/src/donnees");
        if (!dir.exists()) dir.mkdirs();

        Carte maCarte = new Carte();

        // 1. Création de quelques territoires
        Territoire france = new Territoire("France",Etat.AXE);
        Territoire espagne = new Territoire("Espagne",Etat.ALLIE);
        Territoire allemagne = new Territoire("Allemagne");
        Territoire italie = new Territoire("Italie");

        // 2. Ajout au réseau
        maCarte.ajouterTerritoire(france);
        maCarte.ajouterTerritoire(espagne);
        maCarte.ajouterTerritoire(allemagne);
        maCarte.ajouterTerritoire(italie);

        // 3. Création des frontières (liens)
        try {
            maCarte.lierTerritoire(france, espagne);
            maCarte.lierTerritoire(france, allemagne);
            maCarte.lierTerritoire(france, italie);
            maCarte.lierTerritoire(allemagne, italie);

            // Sauvegarde de la carte initiale
            maCarte.sauvegarder("Risk/src/donnees/map_europe.ser");
            System.out.println("Carte sauvegardée avec succès !");

            System.out.println("=== LOGIQUE DE JEU ===");

            System.out.println("Le monde est en guerre.\n" +
                    "Chacun pour soi, aucune pitié.\n" +
                    "\n" +
                    "Prenez vos territoires, attaquez sans hésiter…\n" +
                    "et dominez la carte.\n" +
                    "\n" +
                    "Que le meilleur stratège gagne.");
            Thread.sleep(1000);
            System.out.println("avant de ce lancer dans la parti, voici un résumer du déroulement:\n" +
                    "\n-01)Objectif du jeu : Chaque joueur doit réaliser son objectif" +
                    " \nsecret ou conquérir le monde entier selon la variante choisie. " +
                    "-\n2)Mécanique principale : Les joueurs choisissent une couleur, prennent leurs " +
                    "\narmées et doivent remplir leur mission secrète ou éliminer tous leurs adversaires." +
                    "\n-3)Renforts : Les joueurs peuvent obtenir des renforts en fonction de la taille de leurs territoires et des cartes de renforts." +
                    "\n-4)Combats : Les joueurs lancent des dés pour attaquer et " +
                    "\ndéfendre leurs territoires, avec des combats qui peuvent" +
                    " \nmener à la conquête de territoires. ");

            // (À toi de remplir : Logique de tour, choix des joueurs, etc.)
            System.out.println("En attente des actions joueurs...");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}