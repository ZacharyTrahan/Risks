package app;
//Code complet assisté par Gemini

import reseau.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;

/**
 * Version exécutable de l'affichage du jeu Risk.
 * Cette classe contient maintenant son propre main pour lancer la fenêtre.
 */
public class CarteApp extends JPanel {

    private final Carte carte;
    private final Map<Territoire, Point> positions;
    private Territoire territoireSelectionne = null;

    public CarteApp(Carte carte) {
        this.carte = carte;
        this.positions = new HashMap<>();
        this.setPreferredSize(new Dimension(900, 700));
        this.setBackground(new Color(25, 25, 25)); // Look "War Room"

        genererPositionsEnCercle();
        setupMouseListener();
    }

    private void genererPositionsEnCercle() {
        for (Territoire t : carte.getConnexions().keySet()) {
            Point pGeo = null;

            switch (t.getNom()) {
                case "Canada":      pGeo = new Point(54, -100); break;
                case "U.S.A":       pGeo = new Point(39, -97);  break;
                case "Royaume-Uni": pGeo = new Point(54, -50);   break;
                case "Espagne":     pGeo = new Point(39, -3);   break;
                case "France":      pGeo = new Point(46, 2);    break;
                case "Suisse":      pGeo = new Point(47, 8);    break;
                case "Allemagne":   pGeo = new Point(51, 10);   break;
                case "Italie":      pGeo = new Point(42, 13);   break;
                case "Pologne":     pGeo = new Point(52, 19);   break;
                case "Chine":       pGeo = new Point(34, 103);  break;
                default:            pGeo = new Point(45, 0);    break; // Centre par défaut
            }

            // --- NOUVELLE FORMULE D'AJUSTEMENT ---

            // 1. Longitude (X) : On passe de [-102, 103] à [50, 850] pixels
            // (pGeo.y + 102) donne une plage de 0 à 205.
            // 900 (largeur) / 205 (amplitude) = env 4.3
            int x = (int)((pGeo.y + 102) * 4.5) + 30;

            // 2. Latitude (Y) : On passe de [34, 58] à [100, 600] pixels
            // Attention : en Java, Y=0 est en haut. Donc on inverse (58 - latitude).
            // (58 - pGeo.x) donne une plage de 0 à 24.
            // 700 (hauteur) / 24 = env 29
            int y = (int)((58 - pGeo.x) * 23.0) + 80;

            positions.put(t, new Point(x, y));
        }
    }

    private void setupMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean trouve = false;
                for (Map.Entry<Territoire, Point> entry : positions.entrySet()) {
                    if (entry.getValue().distance(e.getPoint()) < 35) {
                        territoireSelectionne = entry.getKey();
                        trouve = true;

                        // (À toi de remplir : logique de sélection ou d'attaque)
                        System.out.println("Sélection : " + territoireSelectionne.getNom());
                        break;
                    }
                }
                if (!trouve) territoireSelectionne = null;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // ... réglages habituels ...

        // 1. Dessiner les frontières
        for (var entree : carte.getConnexions().entrySet()) {
            Point p1 = positions.get(entree.getKey());

            if (p1 != null) { // <--- CETTE VÉRIFICATION EMPÊCHE LE CRASH
                for (Territoire voisin : entree.getValue()) {
                    Point p2 = positions.get(voisin);
                    if (p2 != null) {
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
        }
        // 2. Dessiner les territoires (Nœuds)
        for (Territoire t : carte.getConnexions().keySet()) {
            Point p = positions.get(t);

            // Choix de la couleur selon l'état
            Color couleurCercle;
            if (t.getEtat() == Etat.JOUEUR1) {
                couleurCercle = new Color(52, 152, 219); // Bleu
            } else if (t.getEtat() == Etat.JOUEUR2) {
                couleurCercle = new Color(231, 76, 60);  // Rouge
            } else if (t.getEtat() == Etat.JOUEUR3) {
                couleurCercle = new Color(158, 253, 56); // Citron vert
            } else if (t.getEtat() == Etat.JOUEUR4) {
                couleurCercle = new Color(108, 0, 255); // Violet
            } else if (t.getEtat() == Etat.JOUEUR5) {
                couleurCercle = new Color(255, 0, 212);  // Rose
            } else if (t.getEtat() == Etat.JOUEUR6) {
                couleurCercle = new Color(255, 230, 0);  // Jaune
            } else {
                couleurCercle = Color.DARK_GRAY;         // Neutre
            }

            // Surlignage si sélectionné
            if (t.equals(territoireSelectionne)) {
                g2d.setColor(Color.WHITE);
                g2d.fillOval(p.x - 40, p.y - 40, 80, 80);
            }

            // Dessin du territoire
            g2d.setColor(couleurCercle);
            g2d.fillOval(p.x - 35, p.y - 35, 70, 70);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(p.x - 35, p.y - 35, 70, 70);

            // Nom du territoire
            g2d.setColor(Color.WHITE);
            String txt = t.getNom();
            int largeurTxt = g2d.getFontMetrics().stringWidth(txt);
            g2d.drawString(txt, p.x - (largeurTxt / 2), p.y + 5);
        }

        // 3. Interface HUD
        dessinerHUD(g2d);
    }

    private void dessinerHUD(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(20, 20, 300, 100);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(20, 20, 300, 100);

        g2d.drawString("COMMANDEMENT RISK", 35, 45);

        if (territoireSelectionne != null) {
            g2d.drawString("Territoire : " + territoireSelectionne.getNom(), 35, 75);
            g2d.drawString("État : " + territoireSelectionne.getEtat(), 35, 95);
        } else {
            g2d.drawString("Sélectionnez un territoire...", 35, 75);
        }
    }

    // --- LE MAIN POUR LANCER L'APPLICATION ---
    public static void main(String[] args) {

    }
}