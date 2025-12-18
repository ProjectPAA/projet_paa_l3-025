package up.mi.paa.test_unitaire;

import up.mi.paa.solvers.SolverGreedyGenerateur;
import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.TypeConsommation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestSolverGreedyGenerateur {

    /**
     * Vérifie que l'algorithme privilégie le plus petit générateur ayant une capacité suffisante.
     * Cela correspond à une stratégie de type "First Fit" sur une liste triée par capacité croissante.
     */
    @Test
    void testChoixPetitGenerateurSuffisant() {
        Reseau reseau = new Reseau();
        // Création de générateurs de capacités croissantes
        reseau.ajouterGenerateur("G_Petit", 20);
        reseau.ajouterGenerateur("G_Moyen", 50);
        reseau.ajouterGenerateur("G_Gros", 100);

        // Cette maison (10 kWh) peut entrer dans n'importe quel générateur.
        // L'algorithme doit sélectionner le plus petit capable de l'accueillir.
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);

        SolverGreedyGenerateur solver = new SolverGreedyGenerateur(reseau);
        solver.solve(1.0);

        Maison m1 = reseau.getMaisons().get("M1");
        Generateur genChoisi = reseau.getConnexions().get(m1);

        assertNotNull(genChoisi);
        assertEquals("G_Petit", genChoisi.getNom(), "Le générateur sélectionné doit être le plus petit suffisant.");
    }

    /**
     * Vérifie que l'algorithme utilise le générateur suivant lorsque le plus petit est saturé.
     */
    @Test
    void testRemplissageProgressif() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G_Petit", 20);
        reseau.ajouterGenerateur("G_Gros", 100);

        // M1 (20 kWh) sature complètement G_Petit.
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);

        // M2 (10 kWh) ne peut pas entrer dans G_Petit, elle doit être placée sur G_Gros.
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        SolverGreedyGenerateur solver = new SolverGreedyGenerateur(reseau);
        solver.solve(1.0);

        // Vérification des affectations
        assertEquals("G_Petit", reseau.getConnexions().get(reseau.getMaisons().get("M1")).getNom());
        assertEquals("G_Gros", reseau.getConnexions().get(reseau.getMaisons().get("M2")).getNom());
    }

    /**
     * Vérifie le comportement en cas de surcharge inévitable.
     * L'algorithme doit assigner la maison au générateur de plus grande capacité pour minimiser la surcharge absolue.
     */
    @Test
    void testGestionSurcharge() {
        Reseau reseau = new Reseau();
        // Deux générateurs trop petits pour la demande
        reseau.ajouterGenerateur("Mini_1", 5);
        reseau.ajouterGenerateur("Mini_2", 10);

        // La maison demande 40 kWh, ce qui excède toutes les capacités.
        reseau.ajouterMaison("M_Geante", TypeConsommation.FORTE);

        SolverGreedyGenerateur solver = new SolverGreedyGenerateur(reseau);
        solver.solve(1.0);

        Maison m = reseau.getMaisons().get("M_Geante");
        Generateur g = reseau.getConnexions().get(m);

        // Le choix doit se porter sur le générateur ayant la plus grande capacité (Mini_2)
        assertEquals("Mini_2", g.getNom(), "En surcharge, le générateur de plus grande capacité doit être choisi.");
    }

    /**
     * Vérifie que les connexions préexistantes sont correctement supprimées avant l'exécution de l'algorithme.
     */
    @Test
    void testNettoyage() {
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);

        // Ajout d'une connexion invalide ou manuelle
        reseau.ajouterConnexion("M1", "G_Inexistant");

        SolverGreedyGenerateur solver = new SolverGreedyGenerateur(reseau);
        solver.solve(1.0);

        // Si verifierConnexions renvoie true, l'algorithme a correctement réinitialisé et recalculé les liens.
        assertTrue(reseau.verifierConnexions());
    }
}