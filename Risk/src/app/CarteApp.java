package app;

import reseau.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        int centerX = 450, centerY = 350, radius = 250;
        Set<Territoire> territoires = carte.getConnexions().keySet();
        int total = territoires.size();
        int index = 0;

        for (Territoire t : territoires) {
            double angle = 2 * Math.PI * index / total;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            positions.put(t, new Point(x, y));
            index++;
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Dessiner les frontières (Arêtes)
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(70, 70, 70));
        for (var entree : carte.getConnexions().entrySet()) {
            Point p1 = positions.get(entree.getKey());
            for (Territoire voisin : entree.getValue()) {
                Point p2 = positions.get(voisin);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // 2. Dessiner les territoires (Nœuds)
        for (Territoire t : carte.getConnexions().keySet()) {
            Point p = positions.get(t);

            // Choix de la couleur selon l'état
            Color couleurCercle;
            if (t.getEtat() == Etat.ALLIE) {
                couleurCercle = new Color(52, 152, 219); // Bleu
            } else if (t.getEtat() == Etat.AXE) {
                couleurCercle = new Color(231, 76, 60);  // Rouge
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
        // 1. Création d'une carte de test
        Carte riskMap = new Carte();

        Territoire t1 = new Territoire("France", Etat.ALLIE);
        Territoire t2 = new Territoire("Espagne", Etat.ALLIE);
        Territoire t3 = new Territoire("Allemagne", Etat.AXE);
        Territoire t4 = new Territoire("Italie", Etat.NEUTRE);
        Territoire t5 = new Territoire("UK", Etat.NEUTRE);

        riskMap.ajouterTerritoire(t1);
        riskMap.ajouterTerritoire(t2);
        riskMap.ajouterTerritoire(t3);
        riskMap.ajouterTerritoire(t4);
        riskMap.ajouterTerritoire(t5);

        try {
            riskMap.lierTerritoire(t1, t2);
            riskMap.lierTerritoire(t1, t3);
            riskMap.lierTerritoire(t1, t4);
            riskMap.lierTerritoire(t3, t4);
            riskMap.lierTerritoire(t1, t5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Lancement de la fenêtre
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Risk - Visualiseur de Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new CarteApp(riskMap));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("Application démarrée ! (À toi de remplir la suite)");
        });
    }
}