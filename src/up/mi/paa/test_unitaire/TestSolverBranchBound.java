package up.mi.paa.test_unitaire;

import up.mi.paa.solvers.SolverBranchBound;
import up.mi.paa.solvers.SolverGreedyMaison;
import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.TypeConsommation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class TestSolverBranchBound {

    // Variables permettant de rediriger et capturer la sortie standard (System.out)
    // afin d'éviter de polluer la console lors de l'exécution des tests.
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    /**
     * Avant chaque test, on redirige la sortie standard vers un flux interne.
     * Cela permet de masquer les affichages verbeux de l'algorithme Branch & Bound.
     */
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * Après chaque test, on rétablit la sortie standard normale pour ne pas impacter le reste du programme.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    /**
     * Vérifie l'optimalité de la solution sur un cas trivial.
     * Avec 2 maisons et 2 générateurs identiques, l'algorithme exact doit impérativement trouver
     * une dispersion nulle (équilibre parfait).
     */
    @Test
    void testOptimaliteTriviale() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);

        reseau.ajouterMaison("M1", TypeConsommation.BASSE); // 10 kWh
        reseau.ajouterMaison("M2", TypeConsommation.BASSE); // 10 kWh

        SolverBranchBound solver = new SolverBranchBound(reseau, 1.0);
        solver.solve();

        // Mise à jour des indicateurs du réseau après résolution
        reseau.updateTauxUtilisation();

        // Le coût (dispersion + surcharge) doit être strictement nul dans cette configuration idéale.
        assertEquals(0.0, reseau.disp(), 0.0001, "L'algorithme exact doit trouver la solution optimale (coût nul ici).");
        assertTrue(reseau.verifierConnexions(), "Toutes les maisons doivent être connectées à un générateur.");
    }

    /**
     * Vérifie que l'algorithme Branch & Bound (exact) fournit une solution au moins équivalente
     * à l'algorithme glouton (heuristique).
     * Le coût de la solution optimale doit être inférieur ou égal au coût de l'heuristique.
     */
    @Test
    void testComparaisonVersusHeuristique() {
        Reseau reseau = new Reseau();

        // Configuration propice à tester les limites d'un algorithme glouton
        reseau.ajouterGenerateur("G1", 30);
        reseau.ajouterGenerateur("G2", 30);

        reseau.ajouterMaison("M1", TypeConsommation.NORMAL); // 20
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);  // 10
        reseau.ajouterMaison("M3", TypeConsommation.NORMAL); // 20
        reseau.ajouterMaison("M4", TypeConsommation.BASSE);  // 10

        // Exécution de l'algorithme glouton pour établir une référence
        SolverGreedyMaison greedy = new SolverGreedyMaison(reseau, 1.0);
        greedy.solve();
        double coutHeuristique = reseau.calculerCout(1.0);

        // Exécution de l'algorithme Branch & Bound
        // SolverBranchBound utilise la solution gloutonne comme borne initiale.
        SolverBranchBound bb = new SolverBranchBound(reseau, 1.0);
        bb.solve();
        double coutOptimal = reseau.calculerCout(1.0);

        // Vérification : Coût Optimal <= Coût Heuristique
        assertTrue(coutOptimal <= coutHeuristique + 0.0001,
                "L'algorithme exact ne peut pas produire un résultat pire que l'heuristique initiale.");
    }

    /**
     * Vérifie le comportement de l'algorithme dans un scénario de surcharge inévitable.
     * L'algorithme doit trouver une solution valide (toutes maisons connectées) même si le coût est élevé.
     */
    @Test
    void testResilienceEnSurcharge() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("Petit_Generateur", 10);

        // La demande (40) excède largement la capacité (10).
        reseau.ajouterMaison("Maison_Gourmande", TypeConsommation.FORTE);

        SolverBranchBound solver = new SolverBranchBound(reseau, 1.0);
        solver.solve();

        // Vérifie que l'algorithme se termine et connecte la maison, malgré la surcharge.
        assertTrue(reseau.verifierConnexions(), "La maison doit être connectée malgré la surcharge.");
        assertEquals(1, reseau.getConnexions().size(), "Le nombre de connexions doit être correct.");
    }
}