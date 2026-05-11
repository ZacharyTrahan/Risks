//Code complet assisté par Gemini

import app.CarteApp;
import reseau.*;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    public static Carte maCarte = new Carte();
    public static JFrame frame;
    public static CarteApp appInstance;

    public static void main(String[] args) {

        // Création du dossier de données s'il n'existe pas
        File dir = new File("Risk/src/donnees");
        if (!dir.exists()) dir.mkdirs();


        // 1. Création de quelques territoires
        //si temps rendre la creation de pays en lecture de Fichier ou faire un tableau
        Territoire france = new Territoire("France");
        Territoire espagne = new Territoire("Espagne");
        Territoire allemagne = new Territoire("Allemagne");
        Territoire italie = new Territoire("Italie");
        Territoire royaumeUni = new Territoire("Royaume-Uni");
        Territoire pologne = new Territoire("Pologne");
        Territoire russie = new Territoire("Russie");
        Territoire serbie = new Territoire("Serbie");
        Territoire usa = new Territoire("U.S.A");
        Territoire canada = new Territoire("Canada");
        Territoire chine = new Territoire("Chine");
        Territoire suisse = new Territoire("Suisse");


        // 2. Ajout au réseau
        maCarte.ajouterTerritoire(france);
        maCarte.ajouterTerritoire(espagne);
        maCarte.ajouterTerritoire(allemagne);
        maCarte.ajouterTerritoire(italie);
        maCarte.ajouterTerritoire(royaumeUni);
        maCarte.ajouterTerritoire(pologne);
        maCarte.ajouterTerritoire(russie);
        maCarte.ajouterTerritoire(serbie);
        maCarte.ajouterTerritoire(usa);
        maCarte.ajouterTerritoire(canada);
        maCarte.ajouterTerritoire(chine);
        maCarte.ajouterTerritoire(suisse);


        // 3. Création des frontières (liens)
        try {
            maCarte.lierTerritoire(canada, france);
            maCarte.lierTerritoire(canada, usa);
            maCarte.lierTerritoire(usa, france);
            maCarte.lierTerritoire(usa, royaumeUni);
            maCarte.lierTerritoire(france, espagne);
            maCarte.lierTerritoire(royaumeUni, france);
            maCarte.lierTerritoire(royaumeUni, allemagne);
            maCarte.lierTerritoire(france, allemagne);
            maCarte.lierTerritoire(france, italie);
            maCarte.lierTerritoire(france, suisse);
            maCarte.lierTerritoire(allemagne, italie);
            maCarte.lierTerritoire(allemagne, suisse);
            maCarte.lierTerritoire(allemagne, pologne);
            maCarte.lierTerritoire(italie, suisse);
            maCarte.lierTerritoire(italie, serbie);
            maCarte.lierTerritoire(pologne, russie);
            maCarte.lierTerritoire(pologne, serbie);
            maCarte.lierTerritoire(russie, serbie);
            maCarte.lierTerritoire(russie, chine);


            // Sauvegarde de la carte initiale
            maCarte.sauvegarder("Risk/src/donnees/map_europe.ser");
            System.out.println("Carte sauvegardée avec succès !");
            //IA
            frame = new JFrame("Visualisation Risk");
            appInstance = new CarteApp(maCarte); // On passe l'objet maCarte directement
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(appInstance);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            //IA

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
            afficherCarte();
            creationJoueur();

            gameOn();



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //1.1 Création des joueurs
    public static void creationJoueur() {
        boolean serrure = true;

        Etat[] etats = Etat.values();
        do {
            int nbJoueur = 7;

            System.out.print("Combien de joueurs êtes vous ? :");
            try {
                nbJoueur = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vous devez mettre un nombre.");
            }

            try {
                if (nbJoueur > 6) {
                    throw new IllegalArgumentException("Le nombre de maximum de joueurs est de 6.");

                } else {


                    for (int i = 0; i < nbJoueur; i++) {
                        boolean serrure2 = true;

                        do {

                            System.out.println("Dans quel pays voulez vous commencer ? : ");
                            afficherListe();
                            int choix = -1;
                            try {
                                choix = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Vous devez mettre un nombre.");
                            }
                            if (choix != -1) {
                                String reponse = choixPays(choix);

                                for (Territoire t : maCarte.getConnexions().keySet()) {
                                    if (t.getNom().equals(reponse)) {
                                        try {
                                            if (t.getEtat().equals(Etat.NEUTRE)) {
                                                t.setEtat(etats[i]);
                                                appInstance.repaint();
                                                serrure2 = false;
                                            } else {
                                                throw new IllegalArgumentException("Le territoire ne doit pas être pris par quelqu'un d'autres");
                                            }
                                        } catch (IllegalArgumentException e) {
                                            System.out.println(e.getMessage());
                                        }

                                    }

                                }
                            }
                        } while (serrure2);


                    }

                    serrure = false;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }


        } while (serrure);
    }

    public static void afficherCarte() throws IOException {
        String dossier = "Risk/src/donnees/";
        try {
            // 1. Lister les fichiers disponibles
            File dir = new File(dossier);
            File[] fichiers = dir.listFiles((d, name) -> name.endsWith(".ser"));

            if (fichiers == null || fichiers.length == 0) {
                System.out.println("Aucun fichier de réseau (.ser) trouvé dans " + dossier);
                return;
            }

            int choix = 1;
            String nomFichierSelectionne = fichiers[choix - 1].getPath();

            // 3. Charger et afficher
            Carte reseauALire = Carte.charger(nomFichierSelectionne);

            JFrame frame = new JFrame("Visualisation de la carte de Jeu Risk - " + nomFichierSelectionne);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new CarteApp(reseauALire));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (Exception e) {
            System.err.println("Erreur : Assurez-vous d'entrer un numéro valide.");
            e.printStackTrace();
        } finally {
            // sc.close(); // Optionnel selon ton flux de travail
        }
    }

    public static void afficherListe() {
        int nombre = 1;
        for (Map.Entry<Territoire, TreeSet<Territoire>> entry : maCarte.getConnexions().entrySet()) {
            System.out.print(nombre++ + " : ");
            System.out.println(entry.getKey().getNom());
        }

    }

    public static String choixPays(int choix) {
        switch (choix) {
            case 1:
                System.out.println("Vous avez choisi l'Allemagne.");
                return "Allemagne";

            case 2:
                System.out.println("Vous avez choisi le Canada.");
                return "Canada";


            case 3:
                System.out.println("Vous avez choisi l'Italie.");
                return "Italie";

            case 4:
                System.out.println("Vous avez choisi le Royaume-Uni.");
                return "Royaume-Uni";

            case 5:
                System.out.println("Vous avez choisi la Pologne.");
                return "Pologne";

            case 6:
                System.out.println("Vous avez choisi la Suisse.");
                return "Suisse";

            case 7:
                System.out.println("Vous avez choisi la Russie.");
                return "Russie";

            case 8:
                System.out.println("Vous avez choisi la Serbie.");
                return "Serbie";

            case 9:
                System.out.println("Vous avez choisi la Chine.");
                return "Chine";

            case 10:
                System.out.println("Vous avez choisi la France.");
                return "France";

            case 11:
                System.out.println("Vous avez choisi l'Espagne.");
                return "Espagne";

            case 12:
                System.out.println("Vous avez choisi les U.S.A.");
                return "U.S.A";

            default:
                System.out.println("Choix invalide.");
                return null;
        }
    }

    public static void afficheActionJoueur() {


        System.out.println("En attente du action joueur...");

        System.out.println("[1] Attaquer");
        System.out.println("[2] Transfère troupe");
        System.out.println("[3] Passer son tour");
        System.out.println();
        System.out.println("[4] Sauvegarder");


    }

    public static void gameOn(){
        afficheActionJoueur();
    }


}