package up.mi.paa.test_unitaire;


import up.mi.paa.pbl.Reseau;
import up.mi.paa.pbl.TypeConsommation;
import up.mi.paa.solvers.SolverNaive;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestSolverNaive {

    /**
     * Teste que le Solver ne fait jamais augmenter le coût.
     * Test de l'algorithme Hill Climbing on ne garde que si c'est mieux ou égal (le coût).
     */
    @Test
    void testNonRegression() {
        // Mise en place d'un réseau simple mal fait
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);

        reseau.ajouterMaison("M1", TypeConsommation.FORTE); // 40
        reseau.ajouterMaison("M2", TypeConsommation.FORTE); // 40

        // Situation initiale très déséquilibrée : Tout le monde sur G1
        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G1"); // G1 est chargé, G2 est vide

        double coutInitial = reseau.calculerCout(1.0);

        SolverNaive solver = new SolverNaive(reseau, 1.0);
        // On lance 100 itérations (suffisant pour un petit réseau)
        solver.solve(100);

        double coutFinal = reseau.calculerCout(1.0);


        // Comme c'est aléatoire, on ne peut pas garantir qu'il aura trouvé la solution parfaite.
        // Mais on peut garantir qu'il n'a pas rendu la situation pire.
        assertTrue(coutFinal <= coutInitial, "Le coût ne doit jamais augmenter après passage du solver");

        if (coutFinal < coutInitial) {
            System.out.println("Bravo ! Le solver a optimisé le réseau.");
        } else {
            System.out.println("Le solver n'a pas trouvé mieux (c'est possible avec le hasard).");
        }
    }

    /**
     * Teste que le solver fonctionne même sur un réseau déjà parfait.
     * Il ne doit rien casser.
     */
    @Test
    void testSurReseauDejaParfait() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);
        reseau.ajouterConnexion("M1", "G1");

        double coutAvant = reseau.calculerCout(1.0);

        SolverNaive solver = new SolverNaive(reseau, 1.0);
        solver.solve(50);

        double coutApres = reseau.calculerCout(1.0);

        assertEquals(coutAvant, coutApres, 0.0001, "Le coût ne devrait pas bouger si on ne peut pas faire mieux");
    }
}