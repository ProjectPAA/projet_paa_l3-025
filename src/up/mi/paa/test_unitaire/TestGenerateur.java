package up.mi.paa.test_unitaire;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import up.mi.paa.pbl.Generateur;




class TestGenerateur {

    /**
     * Teste le constructeur et les accesseurs (Getters)
     * Vérifie que l'objet est bien initialisé.
     */
    @Test
    void testInitialisation() {
        Generateur gen = new Generateur("Gen1", 100);

        assertEquals("Gen1", gen.getNom(), "Le nom devrait être Gen1");
        // Attention : j'utilise getCapaciteMAx (avec le A majuscule) comme dans votre code source
        assertEquals(100, gen.getCapaciteMAx(), "La capacité devrait être 100");
    }

    /**
     * Teste le Setter.
     */
    @Test
    void testModificationCapacite() {
        Generateur gen = new Generateur("Gen1", 100);
        gen.setCapaciteMax(200);

        assertEquals(200, gen.getCapaciteMAx(), "La capacité devrait avoir été modifiée à 200");
    }

    /**
     * Teste la méthode equals().
     * Deux générateurs sont égaux si Nom ET Capacité sont identiques.
     */
    @Test
    void testEquals() {
        Generateur g1 = new Generateur("A", 50);
        Generateur g2 = new Generateur("A", 50); // Identique à g1
        Generateur g3 = new Generateur("B", 50); // Nom différent
        Generateur g4 = new Generateur("A", 100); // Capacité différente

        // Cas d'égalité
        assertEquals(g1, g2, "Deux générateurs avec mêmes attributs doivent être égaux");

        // Cas d'inégalité
        assertNotEquals(g1, g3, "Des noms différents doivent rendre l'égalité fausse");
        assertNotEquals(g1, g4, "Des capacités différentes doivent rendre l'égalité fausse");
        assertNotEquals(g1, null, "L'égalité avec null doit être fausse");
        assertNotEquals(g1, "Une String", "L'égalité avec un autre type doit être fausse");
    }

    /**
     * Teste hashCode().
     * Si deux objets sont equals(), ils DOIVENT avoir le même hashCode.
     */
    @Test
    void testHashCode() {
        Generateur g1 = new Generateur("A", 50);
        Generateur g2 = new Generateur("A", 50);

        assertEquals(g1.hashCode(), g2.hashCode(), "Le hashCode doit être identique pour deux objets égaux");
    }

    /**
     * Teste compareTo().
     * Votre implémentation ne compare QUE la capacité.
     */
    @Test
    void testCompareTo() {
        Generateur petit = new Generateur("Petit", 10);
        Generateur grand = new Generateur("Grand", 100);
        Generateur moyen = new Generateur("Moyen", 50);

        // Test < 0 (Petit est plus petit que Grand)
        assertTrue(petit.compareTo(grand) < 0, "10 comparé à 100 devrait être négatif");

        // Test > 0 (Grand est plus grand que Petit)
        assertTrue(grand.compareTo(petit) > 0, "100 comparé à 10 devrait être positif");

        // Test = 0 (Égalité de capacité)
        Generateur g1 = new Generateur("G1", 50);
        Generateur g2 = new Generateur("G1", 50);
        assertEquals(0, g1.compareTo(g2));
    }

    /**
     * Test critique : Incohérence entre Equals et CompareTo.
     * Votre code précise que c'est "inconsistent". Ce test prouve que ce comportement est voulu.
     */
    @Test
    void testIncoherenceEqualsEtCompareTo() {
        Generateur g1 = new Generateur("Alpha", 100);
        Generateur g2 = new Generateur("Beta", 100);

        // Générateur ont la même capacité, donc compareTo renvoie 0 (comme s'ils étaient égaux pour le tri)
        assertEquals(0, g1.compareTo(g2), "compareTo doit renvoyer 0 car capacités identiques");

        // Générateur ont des noms différents, donc equals renvoie false
        assertNotEquals(g1, g2, "equals doit renvoyer false car les noms sont différents");
    }
}