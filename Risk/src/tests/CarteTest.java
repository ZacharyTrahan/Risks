package tests;

import org.junit.jupiter.api.BeforeEach;
import reseau.Carte;

import org.junit.jupiter.api.Test;
import reseau.Etat;
import reseau.Territoire;


import static org.junit.jupiter.api.Assertions.*;

public class CarteTest {

    private Carte reseauCarte;
    private Territoire T1, T2, T3, T4;

    @BeforeEach
    void setUp(){
        reseauCarte = new Carte();
        T1 = new Territoire("Canada", Etat.NEUTRE);
        T2 = new Territoire("Allemagne", Etat.NEUTRE);
        T3 = new Territoire("France", Etat.NEUTRE);
        T4 = new Territoire("Russie", Etat.NEUTRE);
    }

    @Test
    public void constructionCarte_Defaut(){
        assertEquals(0, reseauCarte.getConnexions().size());
    }

    @Test
    public void getConnexions(){
        
    }

    @Test
    public void ajouterTerritoire(){
        reseauCarte.ajouterTerritoire(T1);
        assertTrue(reseauCarte.getConnexions().containsKey(T1));
        assertEquals(0, reseauCarte.getConnexions().size());
    }

    @Test
    public void lierTerritoire(){
        reseauCarte.ajouterTerritoire(T1);
        reseauCarte.ajouterTerritoire(T2);
        reseauCarte.lierTerritoire(T1, T2);

        assertTrue(reseauCarte.getConnexions().get(T1).contains(T2));
        assertTrue(reseauCarte.getConnexions().get(T2).contains(T1));
    }

    @Test
    public void rompreLien(){
        reseauCarte.ajouterTerritoire(T1);
        reseauCarte.ajouterTerritoire(T2);
        reseauCarte.lierTerritoire(T1, T2);
        reseauCarte.rompreLien(T1, T2);

        assertFalse(reseauCarte.getConnexions().get(T1).contains(T2));
        assertFalse(reseauCarte.getConnexions().get(T2).contains(T1));
    }

    @Test
    public void retirerTerritoire(){
        reseauCarte.ajouterTerritoire(T1);
        reseauCarte.ajouterTerritoire(T2);
        reseauCarte.lierTerritoire(T1, T2);
        reseauCarte.retirerTerritoire(T1);

        assertFalse(reseauCarte.getConnexions().containsKey(T1));
        assertFalse(reseauCarte.getConnexions().get(T2).contains(T1));
    }

    @Test
    public void attaquer(){

    }

    @Test
    public void sauvegarderCharger(){
        assertThrows(IllegalArgumentException.class, () -> reseauCarte.sauvegarder(""));
    }
}
