package tests;

import reseau.Etat;
import reseau.Territoire;

import static org.junit.jupiter.api.Assertions.*;

class TerritoireTest {

    @org.junit.jupiter.api.Test
    void getEtat() {
        Territoire territoire = new Territoire("France");
        assertEquals(Etat.NEUTRE, territoire.getEtat(), "L'état initial doit être NEUTRE");
        territoire.setEtat(Etat.JOUEUR2);
        assertEquals(Etat.JOUEUR2, territoire.getEtat(), "L'état doit être mis à jour correctement après l'appel à setEtat");
    }

    @org.junit.jupiter.api.Test
    void setEtat() {
        Territoire territoire = new Territoire("Canada");
        assertEquals(Etat.NEUTRE, territoire.getEtat(), "L'état initial doit être NEUTRE");
        territoire.setEtat(Etat.JOUEUR1);
        assertEquals(Etat.JOUEUR1, territoire.getEtat(), "L'état doit être mis à jour correctement après l'appel à setEtat");
    }

    @org.junit.jupiter.api.Test
    void getnBInfanterie() {
        Territoire territoire = new Territoire("France");
        territoire.setnBInfanterie(10);
        assertEquals(10, territoire.getnBInfanterie());
    }

    @org.junit.jupiter.api.Test
    void setnBInfanterie() {
        Territoire territoire = new Territoire("France");
        territoire.setnBInfanterie(10);
        assertEquals(10, territoire.getnBInfanterie());
    }

    @org.junit.jupiter.api.Test
    void getNom() {
        Territoire territoire = new Territoire("Canada");
        territoire.setNom("Canada");
        assertEquals("Canada", territoire.getNom());
    }

    @org.junit.jupiter.api.Test
    void setNom() {
        Territoire territoire = new Territoire("Canada");
        territoire.setNom("Canada");
        assertEquals("Canada", territoire.getNom());
    }

    @org.junit.jupiter.api.Test
    void compareTo() {
        Territoire t1 = new Territoire("Canada");
        Territoire t2 = new Territoire("Bresil");
        Territoire t3 = new Territoire("Canada");
        assertTrue(t2.compareTo(t1) < 0);
        assertTrue(t1.compareTo(t2) > 0);
        // Noms identiques
        assertEquals(0, t1.compareTo(t3));
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        Territoire t1 = new Territoire("U.S.A");
        Territoire t2 = new Territoire("U.S.A");
        Territoire t3 = new Territoire("Canada");

        assertEquals(t1, t1, "Un objet doit être égal à lui-même");
        assertEquals(t1, t2, "Deux territoires avec le même nom (référence) doivent être égaux");
        assertNotEquals(t1, t3, "Deux territoires avec des noms différents ne doivent pas être égaux");
        assertNotEquals(t1, null, "Un objet n'est jamais égal à null");
        assertNotEquals(t1, new Object(), "Un objet n'est pas égal à une instance d'une autre classe");
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        Territoire territoire = new Territoire("Allemagne", Etat.NEUTRE);
        String resultat = territoire.toString();
        assertTrue(resultat.contains("Allemagne"), "Le toString doit contenir le nom du territoire");
        assertTrue(resultat.contains("NEUTRE"), "Le toString doit contenir l'état du territoire");
    }

    @org.junit.jupiter.api.Test
    void testHashCode() {
        String nomCommun = "Canada";
        Territoire t1 = new Territoire(nomCommun);
        Territoire t2 = new Territoire(nomCommun);

        assertEquals(t1.hashCode(), t2.hashCode(),
                "Deux objets égaux doivent avoir le même hashCode");
    }

    @org.junit.jupiter.api.Test
    void isValid() {
        Territoire territoire = new Territoire("Canada");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            territoire.setEtat(null);
        });

        assertEquals("etat nulle", exception.getMessage());
    }
}