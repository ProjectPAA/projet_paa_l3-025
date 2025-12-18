package up.mi.paa.test_unitaire;

import up.mi.paa.pbl.Generateur;
import up.mi.paa.pbl.Maison;
import up.mi.paa.pbl.Reseau;
import up.mi.paa.pbl.TypeConsommation;
import up.mi.paa.solvers.SolverGreedyMaison;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestSolverGreedyMaison {

    /**
     * Cas simple : 2 générateurs et 2 maisons identiques.
     * La charge doit être parfaitement répartie (dispersion = 0).
     */
    @Test
    void testEquilibrageParfait() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);

        reseau.ajouterMaison("M1", TypeConsommation.BASSE); // 10 kWh
        reseau.ajouterMaison("M2", TypeConsommation.BASSE); // 10 kWh

        SolverGreedyMaison solver = new SolverGreedyMaison(reseau, 1.0);
        solver.solve(); // Lambda n'est pas utilisé dans cet algo

        reseau.updateTauxUtilisation(); // On met à jour avant de tester

        // On vérifie que c'est bien équilibré
        assertEquals(0.0, reseau.disp(), 0.001, "La dispersion doit être nulle");
        assertTrue(reseau.verifierConnexions(), "Tout le monde doit être connecté");
    }

    /**
     * Vérifie que l'algo choisit le générateur le plus puissant pour minimiser le taux d'utilisation.
     */
    @Test
    void testChoixDuMeilleurGenerateur() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G_Puissant", 1000); // G1
        reseau.ajouterGenerateur("G_Faible", 10);     // G2

        // Maison de 10 kWh
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);

        SolverGreedyMaison solver = new SolverGreedyMaison(reseau, 1.0);
        solver.solve();

        // On regarde où la maison a été connectée
        Maison m1 = reseau.getMaisons().get("M1");
        Generateur genChoisi = reseau.getConnexions().get(m1);

        assertNotNull(genChoisi);

        // Elle doit aller sur G_Puissant car 10/1000 (1%) < 10/10 (100%)
        assertEquals("G_Puissant", genChoisi.getNom(), "Devrait choisir le générateur puissant");
    }

    /**
     * On s'assure que l'algo supprime bien les anciennes connexions avant de commencer.
     */
    @Test
    void testNettoyageAnciennesConnexions() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);
        reseau.ajouterMaison("M1", TypeConsommation.FORTE);

        // On connecte mal la maison manuellement au début
        reseau.ajouterConnexion("M1", "G1");

        // Lancement de l'algo
        SolverGreedyMaison solver = new SolverGreedyMaison(reseau, 1.0);
        solver.solve();

        // Si la fonction verifierConnexions passe, c'est que l'algo a refait le travail correctement
        assertTrue(reseau.verifierConnexions());
    }

    /**
     * Test de l'heuristique (tri des maisons).
     * Les grosses maisons doivent être placées en premier pour éviter la surcharge.
     */
    @Test
    void testRespectCapacite() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 50);
        reseau.ajouterGenerateur("G2", 50);

        // 3 maisons moyennes (3 * 20 = 60 total) pour 100 de capacité totale
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M2", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M3", TypeConsommation.NORMAL);

        SolverGreedyMaison solver = new SolverGreedyMaison(reseau, 1.0);
        solver.solve();

        reseau.updateTauxUtilisation();

        // On vérifie qu'aucun générateur n'est en surcharge (> 1.0)
        double tauxG1 = reseau.getTauxUtilisation().get("G1");
        double tauxG2 = reseau.getTauxUtilisation().get("G2");

        assertTrue(tauxG1 <= 1.0, "G1 ne doit pas être surchargé");
        assertTrue(tauxG2 <= 1.0, "G2 ne doit pas être surchargé");
    }
}