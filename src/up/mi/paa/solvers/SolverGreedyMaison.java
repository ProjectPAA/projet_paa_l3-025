package up.mi.paa.solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import up.mi.paa.pbl.Generateur;
import up.mi.paa.pbl.Maison;
import up.mi.paa.pbl.Reseau;

/**
 * <p>Cette classe implémente un algorithme constructif, glouton, heuristique.</p>
 * <p>Il trie les {@link Reseau#getMaisons() maisons} par demande décroissante. Puis pour chaque maison dans l'ordre, il parcourt les {@link Reseau#getGenerateurs() générateurs} et la connecte à celui dont le {@link Reseau#getTauxUtilisation() taux d'utilisation} sera le moins élevé après la connexion. (Limitant à la fois l'augmentation de {@link Reseau#surcharge(double) surcharge} et de {@link Reseau#disp() dispersion}.)</p>
 * <p>Son heuristique se résume en : minimiser les taux de surcharge par le triage par masion.</p>
 * <p>VIDE les {@linkplain Reseau#getConnexions() connexions} avant de commencer.</p>
 * <p>En O(n<sup>2</sup>).</p>
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class SolverGreedyMaison extends Solver {

    /**
     * Constructeur à partir du {@link Reseau} à traiter.
	 * @param reseau Le {@link Reseau} à traiter.
	 * @param lambda Le λ donnant la pénalisation de surcharge dans le calcul du coût. (Non utilisé par cet algorithme.)
     */
    public SolverGreedyMaison(Reseau reseau, double lambda) {
    	super(reseau, lambda);
    }

    /**
     * <p>Algorithme constructif, glouton, heuristique.</p>
     * <p>Il trie les {@link Reseau#getMaisons() maisons} par demande décroissante. Puis pour chaque maison dans l'ordre, il parcourt les {@link Reseau#getGenerateurs() générateurs} et la connecte à celui dont le {@link Reseau#getTauxUtilisation() taux d'utilisation} sera le moins élevé après la connexion. (Limitant à la fois l'augmentation de {@link Reseau#surcharge(double) surcharge} et de {@link Reseau#disp() dispersion}.)</p>
     * <p>Son heuristique se résume en : minimiser les taux de surcharge par le triage par masion.</p>
     * <p>VIDE les {@linkplain Reseau#getConnexions() connexions} avant de commencer.</p>
     * <p>En O(n<sup>2</sup>).</p>
     */
    @Override
    public void solve() {

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
        Iterator<Maison> maisonsTrieesIter = maisonsTriees.iterator();
        while (maisonsTrieesIter.hasNext() && !Thread.currentThread().isInterrupted()) {
        	Maison maison = maisonsTrieesIter.next();
        	
            Generateur meilleurGen = null;
            double meilleurTauxPrevisionnel = Double.MAX_VALUE;
            int demandeMaison = maison.getTypeConsommation().getDemande();

            Iterator<Generateur> thisReseauGenerateursIter = this.reseau.getGenerateurs().values().iterator();
            while (thisReseauGenerateursIter.hasNext() && !Thread.currentThread().isInterrupted()) { //On est obligé de vérifier si le thread ne doit pas s'arrêter dans toutes les boucles, pour que le thread s'arrête "in a timely manner" même s'il y a Integer.VALUE_MAX generateurs, par exemple.
            	Generateur gen = thisReseauGenerateursIter.next();
            	
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
                this.reseau.ajouterConnexion(maison.getNom(), meilleurGen.getNom(), true);
                chargeActuelle.put(meilleurGen, chargeActuelle.get(meilleurGen) + demandeMaison);
            }
        }
        System.out.println("Greedy Maison terminé avec coût minimal trouvé : " + reseau.calculerCout(lambda));
    }
}