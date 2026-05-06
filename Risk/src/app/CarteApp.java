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
        Scanner sc = new Scanner(System.in);
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
            int choix = sc.nextInt();
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
}