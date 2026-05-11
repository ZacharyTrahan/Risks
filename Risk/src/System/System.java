package System;

import app.CarteApp;
import reseau.Carte;
import reseau.Etat;
import reseau.Territoire;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class System {
    public static final Etat[] etats = Etat.values();
    public static final Scanner scanner = new Scanner(java.lang.System.in);
    private double nbJoueur;

    public System() {
    }

    //1.1 Création des joueurs
    public void creationJoueur(Carte maCarte, DefaultListCellRenderer appInstance) {

        boolean serrure = true;

        do {

            java.lang.System.out.print("Combien de joueurs êtes vous ? :");
            try {
                nbJoueur = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                java.lang.System.out.println("Vous devez mettre un nombre.");
            }

            try {
                if (nbJoueur > 6) {
                    throw new IllegalArgumentException("Le nombre de maximum de joueurs est de 6.");

                } else {


                    for (int i = 0; i < nbJoueur; i++) {
                        boolean serrure2 = true;

                        do {

                            java.lang.System.out.println("Dans quel pays voulez vous commencer ? : ");
                            afficherListe(maCarte);
                            int choix = -1;
                            try {
                                choix = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                java.lang.System.out.println("Vous devez mettre un nombre.");
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
                                            java.lang.System.out.println(e.getMessage());
                                        }

                                    }

                                }
                            }
                        } while (serrure2);


                    }

                    serrure = false;
                }
            } catch (IllegalArgumentException e) {
                java.lang.System.out.println(e.getMessage());
            }


        } while (serrure);
    }

    public void afficherCarte() throws IOException {
        String dossier = "Risk/src/donnees/";
        try {
            // 1. Lister les fichiers disponibles
            File dir = new File(dossier);
            File[] fichiers = dir.listFiles((d, name) -> name.endsWith(".ser"));

            if (fichiers == null || fichiers.length == 0) {
                java.lang.System.out.println("Aucun fichier de réseau (.ser) trouvé dans " + dossier);
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
            java.lang.System.err.println("Erreur : Assurez-vous d'entrer un numéro valide.");
            e.printStackTrace();
        } finally {
            // sc.close(); // Optionnel selon ton flux de travail
        }
    }

    public void afficherListe(Carte maCarte) {
        int nombre = 1;
        for (Map.Entry<Territoire, TreeSet<Territoire>> entry : maCarte.getConnexions().entrySet()) {
            java.lang.System.out.print(nombre++ + " : ");
            java.lang.System.out.println(entry.getKey().getNom());
        }

    }

    public String choixPays(int choix) {
        switch (choix) {
            case 1:
                java.lang.System.out.println("Vous avez choisi l'Allemagne.");
                return "Allemagne";

            case 2:
                java.lang.System.out.println("Vous avez choisi le Canada.");
                return "Canada";


            case 3:
                java.lang.System.out.println("Vous avez choisi l'Italie.");
                return "Italie";

            case 4:
                java.lang.System.out.println("Vous avez choisi le Royaume-Uni.");
                return "Royaume-Uni";

            case 5:
                java.lang.System.out.println("Vous avez choisi la Pologne.");
                return "Pologne";

            case 6:
                java.lang.System.out.println("Vous avez choisi la Suisse.");
                return "Suisse";

            case 7:
                java.lang.System.out.println("Vous avez choisi la Russie.");
                return "Russie";

            case 8:
                java.lang.System.out.println("Vous avez choisi la Serbie.");
                return "Serbie";

            case 9:
                java.lang.System.out.println("Vous avez choisi la Chine.");
                return "Chine";

            case 10:
                java.lang.System.out.println("Vous avez choisi la France.");
                return "France";

            case 11:
                java.lang.System.out.println("Vous avez choisi l'Espagne.");
                return "Espagne";

            case 12:
                java.lang.System.out.println("Vous avez choisi les U.S.A.");
                return "U.S.A";

            default:
                java.lang.System.out.println("Choix invalide.");
                return null;
        }
    }

    public void afficheActionJoueur() {


        java.lang.System.out.println("En attente du action joueur...");

        java.lang.System.out.println("[1] Attaquer");
        java.lang.System.out.println("[2] Transfère troupe");
        java.lang.System.out.println("[3] Passer son tour");
        java.lang.System.out.println();
        java.lang.System.out.println("[4] Sauvegarder");


    }

    public void gameOn() {

        Etat[] etats = Etat.values();


        afficheActionJoueur();


    }


}
