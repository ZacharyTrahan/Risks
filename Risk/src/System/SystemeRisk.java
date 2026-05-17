package System;

import app.CarteApp;
import reseau.Carte;
import reseau.Etat;
import reseau.Territoire;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Contient la logique principale du jeu Risk.
 *
 * <p>Cette classe gère la création des joueurs, la sélection des territoires,
 * l'affichage des actions possibles et le déroulement global de la partie.</p>
 */

public class SystemeRisk {

    /**
     * Tableau des états disponibles pour les joueurs.
     */

    public static final Etat[] etats = Etat.values();

    /**
     * Lit les entrées utilisateur.
     */

    public static final Scanner scanner = new Scanner(System.in);

    /**
     * Nombre de joueurs choisis pour la partie.
     */

    private double nbJoueur;

    /**
     * Construit le moteur de jeu.
     */

    public SystemeRisk() {
    }

    /**
     * Demande à l'utilisateur de créer les joueurs et d'attribuer un territoire de départ à chacun.
     *
     * @param maCarte     carte du jeu
     * @param appInstance interface Swing à rafraîchir après les choix
     */

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
                if (nbJoueur > (etats.length - 1) || nbJoueur <= 0) {
                    throw new IllegalArgumentException("Le nombre de maximum de joueurs est de " + (etats.length - 1) + ".");

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
                                System.out.println("Vous devez mettre un nombre valable");
                                ;
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

    /**
     * Affiche la liste des cartes sauvegardées et ouvre la carte choisie.
     *
     * @throws IOException si la lecture du fichier échoue
     */

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

            System.out.println("=== Fichiers de réseaux disponibles ===");
            for (int i = 0; i < fichiers.length; i++) {
                System.out.println((i + 1) + ". " + fichiers[i].getName());
            }

            // 2. Demander le choix à l'utilisateur
            System.out.print("\nQuel fichier voulez-vous charger ? (entrez le numéro) : ");
            int choix = scanner.nextInt();
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

    /**
     * Affiche les territoires disponibles dans la carte.
     *
     * @param maCarte carte à lister
     */

    public void afficherListe(Carte maCarte) {
        int nombre = 1;
        for (Map.Entry<Territoire, TreeSet<Territoire>> entry : maCarte.getConnexions().entrySet()) {
            System.out.print(nombre++ + " : ");
            System.out.println(entry.getKey().getNom());
        }

    }

    /**
     * Retourne le territoire correspondant au choix utilisateur.
     *
     * @param choix   numéro choisi par l'utilisateur
     * @param maCarte carte contenant les territoires
     * @return territoire sélectionné, ou {@code null} si le choix est invalide
     */

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

    /**
     * Affiche le menu d'actions d'un joueur.
     *
     * @param j index du joueur dans le tableau des états
     */

    public void afficheActionJoueur(int j) {


        System.out.println("En attente de l'action du joueur..." + etats[j]);

        System.out.println("[1] Attaquer");
        System.out.println("[2] Transfère troupe");
        System.out.println("[3] Passer son tour");
        System.out.println();
        System.out.println("[4] Sauvegarder");


    }

    public void choixAction(int action, Carte maCarte, int joueur) throws IOException {

        boolean serrure3 = true;

        do {

            if (action == 1) {


                afficherListe(maCarte);
                System.out.println("Quel pays voulez-vous utiliser pour attaquer ?");
                int choix = -1;
                try {
                    choix = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Vous devez mettre un nombre.");
                }
                Territoire attaquant = null;
                try {
                    attaquant = choixPays(choix, maCarte);
                    if (!etats[joueur].equals(attaquant.getEtat())) {
                        System.out.println(("Tu ne peux pas utilisé un pays que tu ne contrôle pas ."));
                        attaquant = null;
                    }
                } catch (NullPointerException e) {
                    System.out.println("Vous devez mettre un nombre valable");

                }
                if (attaquant != null) {


                    System.out.println("Quel pays voulez-vous  attaquer ? ?");
                    try {
                        choix = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Vous devez mettre un nombre.");
                    }
                    Territoire defendant = null;
                    try {
                        defendant = choixPays(choix, maCarte);
                    } catch (NullPointerException e) {
                        System.out.println("Vous devez mettre un nombre valable");

                    }
                    try {
                        if (defendant != null && attaquant != null) {
                            int maxAttaque = attaquant.getnBInfanterie() - 1;
                            if (maxAttaque < 1) {
                                System.out.println("Impossible d'attaquer : il faut au moins 2 troupes sur le pays.");
                            } else {
                                int nb = demanderNombreTroupes("Combien de troupes pour l'attaque ?", 1, maxAttaque);
                                maCarte.attaquer(attaquant, defendant, nb);
                                serrure3 = false;
                            }
                        } else {
                            throw new IllegalArgumentException("L'attaque a échoué, veuillez réessayer.");
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }


            }
            if (action == 2) {

                afficherListe(maCarte);
                System.out.println("Quel pays voulez-vous utiliser pour receveur ?");
                int choix = -1;
                try {
                    choix = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Vous devez mettre un nombre.");
                }
                Territoire receveur = null;
                try {
                    receveur = choixPays(choix, maCarte);
                    if (!etats[joueur].equals(receveur.getEtat())) {
                        System.out.println(("Tu ne peux pas utilisé un pays que tu ne contrôle pas ."));
                        receveur = null;
                    }
                } catch (NullPointerException e) {
                    System.out.println("Vous devez mettre un nombre valable");

                }
                if (receveur != null) {


                    System.out.println("Quel pays voulez-vous  pour envoyer ? ?");
                    try {
                        choix = Integer.parseInt(scanner.nextLine().trim());

                    } catch (NumberFormatException e) {
                        System.out.println("Vous devez mettre un nombre.");
                    }
                    Territoire envoyeur = null;
                    try {
                        envoyeur = choixPays(choix, maCarte);
                        if (!etats[joueur].equals(envoyeur.getEtat())) {
                            System.out.println(("Tu ne peux pas utilisé un pays que tu ne contrôle pas ."));
                            envoyeur = null;
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Vous devez mettre un nombre valable");

                    }
                    try {
                        if (envoyeur != null && receveur != null) {
                            int maxTransfert = envoyeur.getnBInfanterie() - 1;
                            if (maxTransfert < 1) {
                                System.out.println("Pas assez de troupes pour un transfert.");
                            } else {
                                int nb = demanderNombreTroupes("Combien de troupes transférer ?", 1, maxTransfert);
                                maCarte.transferer(envoyeur, receveur, nb);
                                serrure3 = false;
                            }
                        } else {
                            throw new IllegalArgumentException("Le transfer a échoué, veuillez réessayer.");
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }

                }


            }
            if (action == 3) {
                System.out.println("Le tour est passé...");
                serrure3 = false;
            }
            if (action == 4) {
                System.out.println("Quel nom voulez vous ?");
                String nom = scanner.nextLine().trim();
                maCarte.sauvegarder("Risk/src/donnees/map_europe_" + nom.trim() + ".ser");
                System.exit(0);
            }
        } while (serrure3);
    }


    /**
     * Lance la boucle principale du jeu.
     *
     * @param maCarte     carte du jeu
     * @param appInstance interface Swing à actualiser
     * @throws IOException          si un chargement ou une sauvegarde échoue
     * @throws InterruptedException si l'affichage de l'introduction est interrompu
     */

    public void gameOn(Carte maCarte, CarteApp appInstance) throws IOException, InterruptedException {
        logiqueJeu();
        creationJoueur(maCarte, appInstance);


        do {
            boolean serrure1 = true;


            for (int i = 0; i < nbJoueur; i++) {
                renfortAleatoire(maCarte, i);

                do {


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
                            choixAction(choix, maCarte, i);
                            serrure1 = false;
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } while (serrure1);

            }


        } while (!victoire(maCarte));


        System.out.println("Fin de partie");
    }

    /**
     * Affiche le texte d'introduction et le résumé des règles.
     *
     * @throws InterruptedException si l'attente entre les messages est interrompue
     */

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

    /**
     * Indique si la partie est gagnée.
     *
     * @return {@code true} si une condition de victoire est remplie, sinon {@code false}
     */

    public boolean victoire() {

        return false;
    }

    /**
     * Exécute l'action choisie par le joueur.
     *
     * @param choix numéro de l'action choisie
     */

    public void actionJoueur(int choix) {
        switch (choix) {
            case 1:
                System.out.println("Vous avez choisi : L'Attaque");
                break;

            case 2:
                System.out.println("Vous avez choisi : Le transfère");
                break;

            case 3:
                System.out.println("Vous avez choisi : Passer son tour");
                break;

            case 4:
                System.out.println("Vous avez choisi Sauvegarder");
                break;

            default:
                System.out.println("Choix invalide.");

        }


    }

    //IA

    public int demanderNombreTroupes(String message, int min, int max) {
        int troupes = -1;
        while (troupes < min || troupes > max) {
            System.out.println(message + " (Entre " + min + " et " + max + ") :");
            try {
                troupes = Integer.parseInt(scanner.nextLine().trim());

                if (troupes < min) {
                    System.out.println("Erreur : Le minimum est de " + min + ".");
                } else if (troupes > max) {
                    System.out.println("Erreur : Vous n'avez que " + max + " troupes disponibles.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez entrer un nombre entier (pas de lettres).");
            }
        }
        return troupes;
    }
    //IA

    public void renfortAleatoire(Carte maCarte, int indexJoueur) {
        Etat nomJoueur = etats[indexJoueur];
        List<Territoire> sesPays = new ArrayList<>();
        for (Territoire t : maCarte.getConnexions().keySet()) {
            if (t.getEtat().equals(nomJoueur)) {
                sesPays.add(t);
            }
        }

        if (!sesPays.isEmpty()) {
            //IA
            // Calcul du bonus (ex: nombre de pays / 3, minimum 3)

            int bonus = Math.max(3, sesPays.size() / 3);

            System.out.println("\n>>> Phase de renfort pour " + nomJoueur + " !");
            System.out.println(">>> " + bonus + " troupes distribuées aléatoirement.");

            for (int i = 0; i < bonus; i++) {
                // Choix d'un index au hasard entre 0 et la taille de la liste - 1
                int indexHasard = (int) (Math.random() * sesPays.size());
                Territoire t = sesPays.get(indexHasard);

                // On ajoute 1 troupe au pays tiré au sort
                t.setnBInfanterie(t.getnBInfanterie() + 1);
            }
            //IA
        }
    }




    public boolean victoire(Carte maCarte) {
        if (maCarte.getConnexions().isEmpty()) return false;

        //IA
        Etat joueur = maCarte.getConnexions().keySet().iterator().next().getEtat();
        //IA

        for (Territoire t : maCarte.getConnexions().keySet()) {
            if (!t.getEtat().equals(joueur)) {
                return false;
            }
        }

        System.out.println("\n*********************************");
        System.out.println("LE JOUEUR " + joueur + " A GAGNÉ !");
        System.out.println("*********************************\n");
        return true;


    }

}
