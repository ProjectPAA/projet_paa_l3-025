package up.mi.paa.test_unitaire;

import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.TypeConsommation;
import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Map;

class TestReseau {

    /**
     * Teste l'ajout simple de maisons et générateurs.
     */
    @Test
    void testAjoutElements() {
        Reseau reseau = new Reseau();

        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);

        // Vérifie que les éléments sont bien dans les Maps
        assertTrue(reseau.getGenerateurs().containsKey("G1"), "Le générateur G1 devrait exister");
        assertTrue(reseau.getMaisons().containsKey("M1"), "La maison M1 devrait exister");

        // Vérifie que la mise à jour fonctionne (écraser un élément si elle existe)
        reseau.ajouterGenerateur("G1", 200);
        assertEquals(200, reseau.getGenerateurs().get("G1").getCapaciteMAx(), "La capacité de G1 aurait dû passer à 200");
    }

    /**
     * Teste la connexion entre une maison et un générateur.
     */
    @Test
    void testConnexions() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE); // 10 kWh

        // Ajoute une connexion
        reseau.ajouterConnexion("M1", "G1");

        Generateur genConnecte = reseau.getConnexions().get(reseau.getMaisons().get("M1"));
        assertNotNull(genConnecte, "La maison devrait être connectée");
        assertEquals("G1", genConnecte.getNom(), "La maison devrait être connectée à G1");

        // Vérifie que la connexion est valide
        assertTrue(reseau.verifierConnexions(), "Toutes les maisons sont connectées, ça devrait renvoyer TRUE");
    }

    /**
     * Teste le changement de connexion (déplacer une maison d'un générateur A vers B).
     */
    @Test
    void testModifierConnexion() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);

        reseau.ajouterConnexion("M1", "G1");

        // On bouge M1 de G1 vers G2
        reseau.modifierConnexion("M1", "G1", "G2");

        Generateur nouveauGen = reseau.getConnexions().get(reseau.getMaisons().get("M1"));
        assertEquals("G2", nouveauGen.getNom(), "La maison devrait maintenant être sur G2");
    }

    /**
     * Teste qu'on détecte bien les maisons sont non connectées.
     */
    @Test
    void testMaisonNonConnectee() {
        Reseau reseau = new Reseau();
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);
        // On n'ajoute PAS de connexion

        assertFalse(reseau.verifierConnexions(), "Ça doit renvoyer FALSE car M1 n'est pas connectée");
    }

    /**
     * Teste 1 : sur la Dispersion et Coût.
     * Scénario contrôlé :
     * - G1 (Capacité 100) avec M1 (10) -> Taux 0.1
     * - G2 (Capacité 100) avec M2 (10) -> Taux 0.1
     * -> Moyenne = 0.1
     * -> Dispersion = |0.1 - 0.1| + |0.1 - 0.1| = 0
     */
    @Test
    void testCalculsEquilibres() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G2");

        // Test dispersion (le résultat doit être 0 car c'est parfaitement équilibré)
        assertEquals(0.0, reseau.disp(), 0.0001, "La dispersion doit être 0 pour un réseau équilibré");
    }

    /**
     * Teste 2: Calcul avec un déséquilibre.
     * - G1 (Capacité 100) avec M1 (10) + M2 (10) -> Charge 20 -> Taux 0.2
     * - G2 (Capacité 100) avec personne -> Charge 0 -> Taux 0.0
     * -> Moyenne = (0.2 + 0.0) / 2 = 0.1
     * -> Disp = |0.2 - 0.1| + |0.0 - 0.1| = 0.1 + 0.1 = 0.2
     */
    @Test
    void testCalculsDesequilibres() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        // On met tout sur G1 (Déséquilibre !)
        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G1");

        // On force la mise à jour des calculs
        reseau.updateTauxUtilisation();

        // Vérification des taux individuels
        Map<String, Double> taux = reseau.getTauxUtilisation();
        assertEquals(0.2, taux.get("G1"), 0.001, "G1 devrait être à 20%");
        assertEquals(0.0, taux.get("G2"), 0.001, "G2 devrait être à 0%");

        // Vérification de la dispersion
        // Calcul attendu : 0.2
        assertEquals(0.2, reseau.disp(), 0.001, "La dispersion devrait être de 0.2");
    }
}
