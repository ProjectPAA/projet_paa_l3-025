package up.mi.paa.solvers;

import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.Generateur;

import java.util.Iterator;
import java.util.ArrayList;


public class GreedySolver extends Solver {

	/**
	 * A Greedy approach to constructing a solution. This isn't tinkering based, but constructive, based on the heuristic that connecting while causing the least surcharge and filling low-but-sufficient-capacity generators is optimal. 
	 * 
	 * CLEARS reseau.connexions.
	 * 
	 * @param reseau Reseau à résoudre
	 * @param lambda coût du surcharge (non-utilisé par cet algorithme)
	 */
	public static void solve(Reseau reseau, int lambda) {
		reseau.getConnexions().clear();
		
		ArrayList<Generateur> genArray;
		genArray = new ArrayList<Generateur>(reseau.getGenerateurs().values());
		genArray.sort(null); //natural ordering is based on CapaciteMax.
		
		
		Iterator<Maison> iterMaison = reseau.getMaisons().values().iterator();
		while (iterMaison.hasNext()) {
			Maison maison = iterMaison.next();
			boolean connected = false;
			
			
			Iterator<Generateur> iterGen = genArray.iterator();
			Generateur gen = null;
			boolean demandFits = false;
			while (iterGen.hasNext() && !connected) {
				gen = iterGen.next();
				if (maison.getTypeConsommation().getDemande() <= gen.getCapaciteMAx() - (reseau.getTauxUtilisation().get(gen.getNom())*gen.getCapaciteMAx())) {
				//if demand <= remaining capacity then
					demandFits = true;
					reseau.ajouterConnexion(maison.getNom(), gen.getNom());
					connected = true;
				}
				else {
					demandFits = false;
				}
			}//end loop on generateurs
			
			if (!demandFits && !connected && gen != null) {
				//Having seen every generator, we couldn't connect to any without going over-capacity. By connecting to the last seen (maximal because sort()), we are augmenting surcharge by the least possible amount.
				reseau.ajouterConnexion(maison.getNom(), gen.getNom());
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
	}
}
