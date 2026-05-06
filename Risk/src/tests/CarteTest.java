package tests;

import reseau.Carte;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class CarteTest {

    @Test
    public void constructionCarte_Defaut(){
        Carte carte = new Carte();
        assertEquals(0, carte.getConnexions().size());
    }

    @Test
    public void getConnexions(){
        
    }

    @Test
    public void ajouterTerritoire(){

    }

    @Test
    public void lierTerritoire(){

    }

    @Test
    public void rompreLien(){

    }

    @Test
    public void retirerTerritoire(){

    }

    @Test
    public void attaquer(){

    }

    @Test
    public void sauvegarderCharger(){

    }
}
