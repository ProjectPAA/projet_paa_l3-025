package up.mi.paa.solvers;

import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.Reseau;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolverGreedyMaison extends Solver {

    private Reseau reseau;

    // Constructeur obligatoire pour récupérer le réseau
    public SolverGreedyMaison(Reseau reseau) {
        this.reseau = reseau;
    }

    /**
     * Algorithme Glouton Intelligent (Smart Greedy).
     * Trie les maisons et choisit le générateur le moins chargé à chaque étape.
     */
    @Override
    public void solve(double lambda) {
        System.out.println("Lancement de l'algo Smart Greedy (Équilibrage de charge)...");

        // On vide les connexions actuelles
        this.reseau.getConnexions().clear();

        // Trie les maisons : Les plus gourmandes (FORTE) en premier !
        List<Maison> maisonsTriees = new ArrayList<>(this.reseau.getMaisons().values());
        // Tri décroissant sur la demande (FORTE > NORMALE > BASSE)
        maisonsTriees.sort((m1, m2) -> Integer.compare(m2.getTypeConsommation().getDemande(), m1.getTypeConsommation().getDemande()));

        // Map pour suivre la charge actuelle des générateurs en temps réel
        Map<Generateur, Integer> chargeActuelle = new HashMap<>();
        for(Generateur g : this.reseau.getGenerateurs().values()) {
            chargeActuelle.put(g, 0);
        }

        // Pour chaque maison, choisir le générateur qui est le MOINS CHARGÉ
        for (Maison maison : maisonsTriees) {

            Generateur meilleurGen = null;
            double meilleurTauxPrevisionnel = Double.MAX_VALUE;
            int demandeMaison = maison.getTypeConsommation().getDemande();

            for (Generateur gen : this.reseau.getGenerateurs().values()) {

                // On calcule quel serait le taux si on ajoutait la maison ici
                double chargeSiAjout = chargeActuelle.get(gen) + demandeMaison;
                double tauxSiAjout = chargeSiAjout / gen.getCapaciteMAx();

                // On cherche à minimiser le taux max (pour équilibrer)
                if (tauxSiAjout < meilleurTauxPrevisionnel) {
                    meilleurTauxPrevisionnel = tauxSiAjout;
                    meilleurGen = gen;
                }
            }

            // On valide la connexion sur le meilleur candidat trouvé
            if (meilleurGen != null) {
                this.reseau.ajouterConnexion(maison.getNom(), meilleurGen.getNom());
                chargeActuelle.put(meilleurGen, chargeActuelle.get(meilleurGen) + demandeMaison);
            }
        }
        System.out.println("Smart Greedy terminé.");
    }
}