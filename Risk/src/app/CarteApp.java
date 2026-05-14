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
 * Composant Swing chargé d'afficher la carte du jeu.
 *
 * <p>La vue dessine les territoires, les frontières et une petite interface
 * HUD pour montrer le territoire sélectionné.</p>
 */

/**
 * Version exécutable de l'affichage du jeu Risk.
 * Cette classe contient maintenant son propre main pour lancer la fenêtre.
 */
public class CarteApp extends JPanel {

    private final Carte carte;
    private final Map<Territoire, Point> positions;
    private Territoire territoireSelectionne = null;

    /**
     * Construit la vue de la carte.
     *
     * @param carte carte à afficher
     */

    public CarteApp(Carte carte) {
        this.carte = carte;
        this.positions = new HashMap<>();
        this.setPreferredSize(new Dimension(900, 700));
        this.setBackground(new Color(25, 25, 25)); // Look "War Room"

        genererPositionsEnCercle();
        setupMouseListener();
    }

    /**
     * Calcule les positions d'affichage des territoires.
     *
     * <p>Les positions sont générées à partir du nom des territoires afin d'obtenir
     * un placement stable à l'écran.</p>
     */

    private void genererPositionsEnCercle() {
        for (Territoire t : carte.getConnexions().keySet()) {
            Point pGeo = null;

            switch (t.getNom()) {
                case "Canada":      pGeo = new Point(54, -100); break;
                case "U.S.A":       pGeo = new Point(39, -97);  break;
                case "Royaume-Uni": pGeo = new Point(54, -50);  break;
                case "Espagne":     pGeo = new Point(39, -30);   break;
                case "France":      pGeo = new Point(50, -37);    break;
                case "Suisse":      pGeo = new Point(47, 8);    break;
                case "Allemagne":   pGeo = new Point(51, 0);   break;
                case "Italie":      pGeo = new Point(42, 13);   break;
                case "Pologne":     pGeo = new Point(52, 19);   break;
                case "Chine":       pGeo = new Point(34, 103);  break;
                default:            pGeo = new Point(45, -10);    break;
            }

            // --- AJUSTEMENT POUR RÉSOLUTION 1920 x 1080 ---

            // 1. Longitude (X) : Plage d'entrée approx. [-100, 103] => Amplitude de 203
            // On veut étaler ça sur environ 1600 pixels (pour laisser des marges)
            // Ratio : 1600 / 203 ≈ 7.8
            int x = (int)((pGeo.y + 100) * 7.8) + 160;

            // 2. Latitude (Y) : Plage d'entrée approx. [34, 54] => Amplitude de 20
            // On veut étaler ça sur environ 800 pixels
            // Ratio : 800 / 20 = 40
            // Inversion Java : (MaxLatitude - pGeo.x)
            int y = (int)((54 - pGeo.x) * 40.0) + 140;

            positions.put(t, new Point(x, y));
        }
    }

    /**
     * Installe l'écouteur de souris pour sélectionner un territoire.
     */

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

    /**
     * Dessine la carte, les frontières, les territoires et l'HUD.
     *
     * @param g contexte graphique Swing
     */

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
                g2d.fillOval(p.x - 40, p.y - 40, 110, 110);
            }

            // Dessin du territoire
            g2d.setColor(couleurCercle);
            g2d.fillOval(p.x - 35, p.y - 35, 100, 100);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(p.x - 35, p.y - 35, 100, 100);

            // Nom du territoire
            g2d.setColor(Color.WHITE);
            String txt = t.getNom();
            int largeurTxt = g2d.getFontMetrics().stringWidth(txt);
            g2d.drawString(txt, p.x - (largeurTxt /4), p.y + 20);
        }

        // 3. Interface HUD
        dessinerHUD(g2d);
    }

    /**
     * Dessine le panneau d'information en haut à gauche.
     *
     * @param g2d contexte graphique 2D
     */

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

    /**
     * Point d'entrée utilitaire de la vue.
     *
     * <p>Charge une carte sérialisée depuis le dossier des données et affiche
     * la fenêtre Swing correspondante.</p>
     *
     * @param args arguments de ligne de commande
     */

    // --- LE MAIN POUR LANCER L'APPLICATION ---
    public static void main(String[] args) {

    }
}