package up.mi.paa.solvers;

import java.util.Iterator;

import up.mi.paa.pbl.Generateur;
import up.mi.paa.pbl.Maison;
import up.mi.paa.pbl.Reseau;

import java.util.ArrayList;


/**
 * <p>Cette classe implémente un algorithme constructif, glouton, heuristique.</p>
 * <p>Il trie les {@link Reseau#getGenerateurs() générateurs} par capacité. Il parcourt ensuite les {@link Reseau#getMaisons() maisons} et les connecte au générateur qui minimise le {@link Reseau#surcharge(double) surcharge} absolu ajouté. (Le premier qui a suffisamment de capacité libre, ou, si aucun générateur n'en a suffisamment, celui de plus grande capacité créera le plus petit surcharge absolu.)</p>
 * <p>Son heuristique se résume en : minimiser le surcharge ajouté à chaque connexion par le triage des générateurs.</p>
 * <p>VIDE les {@linkplain Reseau#getConnexions() connexions} avant de commencer.</p>
 * <p>En O(n<sup>2</sup>).</p>
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class SolverGreedyGenerateur extends Solver {

	/**
	 * Constructeur à partir du {@link Reseau} à traiter.
	 * @param reseau Le {@link Reseau} à traiter.
	 * @param lambda Le λ donnant la pénalisation de surcharge dans le calcul du coût. (Non utilisé par cet algorithme.)
	 */
	public SolverGreedyGenerateur(Reseau reseau, double lambda) {
		super(reseau, lambda);
	}
	
	/**
	  * <p>Cette classe implémente un algorithme constructif, glouton, heuristique.</p>
	  * <p>Il trie les {@link Reseau#getGenerateurs() générateurs} par capacité. Il parcourt ensuite les {@link Reseau#getMaisons() maisons} et les connecte au générateur qui minimise le {@link Reseau#surcharge(double) surcharge} absolu ajouté. (Le premier qui a suffisamment de capacité libre, ou, si aucun générateur n'en a suffisamment, celui de plus grande capacité créera le plus petit surcharge absolu.)</p>
	  * <p>Son heuristique se résume en : minimiser le surcharge ajouté à chaque connexion par le triage des générateurs.</p>
	  * <p>VIDE les {@linkplain Reseau#getConnexions() connexions} avant de commencer.</p>
	  * <p>En O(n<sup>2</sup>).</p>
	 */
	public void solve() {
		reseau.getConnexions().clear();
		
		ArrayList<Generateur> genArray;
		genArray = new ArrayList<Generateur>(reseau.getGenerateurs().values());
		genArray.sort(null); //natural ordering is based on CapaciteMax.
		
		
		Iterator<Maison> iterMaison = reseau.getMaisons().values().iterator();
		while (iterMaison.hasNext() && !Thread.currentThread().isInterrupted()) {
			Maison maison = iterMaison.next();
			boolean connected = false;
			
			
			Iterator<Generateur> iterGen = genArray.iterator();
			Generateur gen = null;
			boolean demandFits = false;
			while (iterGen.hasNext() && !connected && !Thread.currentThread().isInterrupted()) {
				gen = iterGen.next();
				if (maison.getTypeConsommation().getDemande() <= gen.getCapaciteMAx() - (reseau.getTauxUtilisation().get(gen.getNom())*gen.getCapaciteMAx())) {
				//if demand <= remaining capacity then
					demandFits = true;
					reseau.ajouterConnexion(maison.getNom(), gen.getNom(), true);
					connected = true;
				}
				else {
					demandFits = false;
				}
			}//end loop on generateurs
			
			if (!demandFits && !connected && gen != null) {
				//Having seen every generator, we couldn't connect to any without going over-capacity. By connecting to the last seen (maximal because sort()), we are augmenting surcharge by the least possible amount.
				reseau.ajouterConnexion(maison.getNom(), gen.getNom(), true);
			}
			if (reseau.getTauxUtilisation().get(gen.getNom()) >= 1.0) {
				genArray.remove(gen);
			}
			if (genArray.isEmpty()) {//Every generateur is overloaded. The capacity-based order is still heuristically correct to minimise overloading.
				genArray = new ArrayList<Generateur>(reseau.getGenerateurs().values());
				genArray.sort(null);
			}
		}
		//end loop on maisons
        System.out.println("Greedy Generateur terminé avec coût minimal trouvé : " + reseau.calculerCout(lambda));
	}
}
