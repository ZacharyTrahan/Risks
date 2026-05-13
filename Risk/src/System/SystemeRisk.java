package System;

import app.CarteApp;
import reseau.Carte;
import reseau.Etat;
import reseau.Territoire;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SystemeRisk {
    public static final Etat[] etats = Etat.values();
    public static final Scanner scanner = new Scanner(System.in);
    private double nbJoueur;

    public SystemeRisk() {
    }

    //1.1 Création des joueurs
    public void creationJoueur(Carte maCarte, CarteApp appInstance) {

        boolean serrure = true;

        do {

            System.out.print("Combien de joueurs êtes vous ? :");
            try {
                nbJoueur = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vous devez mettre un nombre.");
            }

            try {
                if (nbJoueur > (etats.length-1) || nbJoueur <= 0) {
                    throw new IllegalArgumentException("Le nombre de maximum de joueurs est de " + (etats.length-1) + ".");

                } else {


                    for (int i = 0; i < nbJoueur; i++) {
                        boolean serrure2 = true;

                        do {

                            System.out.println("Dans quel pays voulez vous commencer ? : ");
                            afficherListe(maCarte);
                            int choix = -1;
                            try {
                                choix = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Vous devez mettre un nombre.");
                            }
                            Territoire reponse = null; 
                            try {
                                 reponse = choixPays(choix, maCarte);
                            } catch (NullPointerException e) {
                                System.out.println("Vous devez mettre un nombre valable");;
                            }
                            try {
                                if (reponse != null) {
                                    
                                
                                if (reponse.getEtat().equals(Etat.NEUTRE)) {
                                    reponse.setEtat(etats[i]);
                                    appInstance.repaint();
                                    serrure2 = false;
                                } else {
                                    throw new IllegalArgumentException("Le territoire ne doit pas être pris par quelqu'un d'autres");
                                }
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
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

    public void afficherCarte() throws IOException {
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

    public void afficherListe(Carte maCarte) {
        int nombre = 1;
        for (Map.Entry<Territoire, TreeSet<Territoire>> entry : maCarte.getConnexions().entrySet()) {
            System.out.print(nombre++ + " : ");
            System.out.println(entry.getKey().getNom());
        }

    }

    public Territoire choixPays(int choix, Carte maCarte) {
        List<Territoire> territoires = new ArrayList<>(maCarte.getConnexions().keySet());

        if (choix < 1 || choix > territoires.size()) {
            System.out.println("Choix invalide.");
            return null;
        }

        Territoire territoireChoisi = territoires.get(choix - 1);

        System.out.println("Vous avez choisi " + territoireChoisi.getNom() + ".");

        return territoireChoisi;
    }

    public void afficheActionJoueur(int j) {


        System.out.println("En attente de l'action du joueur..." + etats[j]);

        System.out.println("[1] Attaquer");
        System.out.println("[2] Transfère troupe");
        System.out.println("[3] Passer son tour");
        System.out.println();
        System.out.println("[4] Sauvegarder");


    }

    public void gameOn(Carte maCarte, CarteApp appInstance) throws IOException, InterruptedException {
        logiqueJeu();
        creationJoueur(maCarte, appInstance);


        do {
            for (int i = 0; i < nbJoueur; i++) {
                afficheActionJoueur(i);
                int choix = -1;
                try {
                    choix = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Vous devez mettre un nombre.");
                }

                try {
                    if (choix > 4 || choix <= 0) {
                        throw new IllegalArgumentException("Vous devez choisir un des choix.");

                    } else {
                        actionJoueur(choix);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }

            }


        } while (!victoire());
    }

    public void logiqueJeu() throws InterruptedException {
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
    }

    public boolean victoire() {

        return false;
    }

    public void actionJoueur(int choix) {
        switch (choix) {
            case 1:
                System.out.println("Vous avez choisi : L'Attaque");


            case 2:
                System.out.println("Vous avez choisi : Le transfère");


            case 3:
                System.out.println("Vous avez choisi : Passer son tour");


            case 4:
                System.out.println("Vous avez choisi Sauvegarder");
                break;

            default:
                System.out.println("Choix invalide.");

        }


    }


}
