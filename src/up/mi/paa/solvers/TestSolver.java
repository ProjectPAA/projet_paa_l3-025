package up.mi.paa.solvers;

import up.mi.paa.io.ChargeurReseau;
import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.solvers.SolverGreedyMaison;
import up.mi.paa.solvers.SolverGreedyGenerateur;


public class TestSolver {

    public static void main(String[] args) {
        // Source de l'endroit où le ficher se trouve
        String fichierTest = "src/instance_tres_grande1.txt";

        System.out.println("=== LANCEMENT DU TEST AUTOMATIQUE ===");
        System.out.println("Fichier : " + fichierTest);

        try {
            ChargeurReseau chargeur = new ChargeurReseau();
            Reseau reseau = chargeur.charger(fichierTest);

            System.out.println("Réseau chargé avec succès.");
            System.out.println("Nombre de maisons : " + reseau.getMaisons().size());
            System.out.println("Nombre de générateurs : " + reseau.getGenerateurs().size());
            System.out.println("-----------------------------------");

            double lambda = 10.0;

            // On instancie Solver

            // Solver solver = new SolverGreedyGenerateur(reseau);
            Solver solver = new SolverBranchBound(reseau);

            long debut = System.currentTimeMillis();
            solver.solve(lambda); // Lancement du calcul
            long fin = System.currentTimeMillis();

            System.out.println("-----------------------------------");
            System.out.println("RÉSULTATS APRÈS OPTIMISATION :");
            System.out.println("Temps de calcul : " + (fin - debut) + " ms");

            // On récupère les scores calculés par la classe Reseau
            double cout = reseau.calculerCout(lambda);
            double disp = reseau.disp();
            double surcharge = reseau.surcharge(lambda);

            System.out.println(" > Dispersion : " + String.format("%.4f", disp));
            System.out.println(" > Surcharge  : " + String.format("%.4f", surcharge));
            System.out.println(" > COÛT TOTAL : " + String.format("%.4f", cout));
            System.out.println("-----------------------------------");

            // Vérification visuelle (Optionnel : Affiche qui est connecté à qui)
            // reseau.afficherReseau();

        } catch (Exception e) {
            System.err.println("ERREUR durant le test :");
            e.printStackTrace();
        }
    }
}