package app;

import reseau.Carte;
import reseau.Territoire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;

public class CarteApp extends JPanel {

    private final Carte carte;
    private final Map<Territoire, Point> positions;
    private Territoire territoireSelectionne = null;

    public CarteApp(Carte carte) {
        this.carte = carte;
        this.positions = new HashMap<>();
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(new Color(45, 45, 45)); // Fond sombre pour le style Risk

        genererPositionsEnCercle();
        setupMouseListener();
    }

    private void genererPositionsEnCercle() {
        int centerX = 400, centerY = 300, radius = 200;
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
                for (Map.Entry<Territoire, Point> entry : positions.entrySet()) {
                    if (entry.getValue().distance(e.getPoint()) < 30) {
                        territoireSelectionne = entry.getKey();

                        // (À toi de remplir : Ajouter ici l'ouverture d'un menu d'attaque ou de déplacement)

                        repaint();
                        return;
                    }
                }
                territoireSelectionne = null;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Dessiner les frontières
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        for (var entree : carte.getConnexions().entrySet()) {
            Point p1 = positions.get(entree.getKey());
            for (Territoire voisin : entree.getValue()) {
                Point p2 = positions.get(voisin);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // 2. Dessiner les territoires
        for (Territoire t : carte.getConnexions().keySet()) {
            Point p = positions.get(t);

            // Couleur par défaut (peut changer selon le joueur proprio)
            g2d.setColor(Color.LIGHT_GRAY);

            if (t.equals(territoireSelectionne)) {
                g2d.setColor(Color.YELLOW); // Surlignage sélection
                g2d.fillOval(p.x - 35, p.y - 35, 70, 70);
            }

            g2d.setColor(new Color(100, 150, 255)); // Couleur du pays
            g2d.fillOval(p.x - 30, p.y - 30, 60, 60);

            g2d.setColor(Color.WHITE);
            g2d.drawString(t.getNom(), p.x - 20, p.y + 5);
        }

        // 3. HUD (Affichage des infos)
        g2d.setColor(Color.WHITE);
        g2d.drawString("Carte du monde Risk - Tour actuel", 20, 30);
        if (territoireSelectionne != null) {
            g2d.drawString("Territoire : " + territoireSelectionne.getNom(), 20, 550);
            g2d.drawString("État : " + territoireSelectionne.getEtat(), 20, 570);
        }
    }

    public static void main(String[] args) {
        String cheminFichier = "src/donnees/map_europe.ser";

        try {
            Carte carteChargee = Carte.charger(cheminFichier);

            JFrame frame = new JFrame("Risk Game - Visualisation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new CarteApp(carteChargee));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (Exception e) {
            System.err.println("Erreur : Impossible de charger la carte. Lancez d'abord Main!");
        }
    }
}