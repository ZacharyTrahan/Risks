//Code complet assisté par Gemini

import app.CarteApp;
import reseau.*;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

import System.SystemeRisk;

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


            SystemeRisk system = new SystemeRisk();
            system.gameOn(maCarte,appInstance);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}