package up.mi.paa.solvers;

import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.Generateur;

import java.util.Random;

public class SolverNaive extends Solver {
	
	/**
	 * Implémentation de l'algorithme naif donné en description du projet. Ne change pas le reseau si on n'a pas réussi a faire moins cher. 
	 * 
	 * Execute un nombre par défaut d'itérations.
	 *
	 * @param reseau Reseau a traiter
	 * @param lambda lamda dans le calcul du cout
	 */
	public static void solve(Reseau reseau, double lambda) {
		SolverNaive.solve(reseau, lambda, 1000);	//Un k est nécéssaire pour cet algorithme, si on est appelé sans, on choisi un valeur par défaut.
	}

	/**
	 * Implémentation de l'algorithme naif donné en description du projet. Ne change pas le reseau si on n'a pas réussi a faire moins cher.
	 * 
	 * @param reseau Reseau a traiter
	 * @param lambda lamda dans le calcul du cout
	 * @param k nombre d'itérations a executer avant de s'arreter
	 */
	public static void solve(Reseau reseau, double lambda, int k) {
		String[] maisonNames = {};
		String[] generateurNames = {};
		maisonNames = reseau.getMaisons().keySet().toArray(maisonNames);	//Random gives us indices. Using the Strings makes the syntax more readable because we'll get reseau's attributes' attributes multiple times, and those are keyed by these Strings.
		generateurNames = reseau.getGenerateurs().keySet().toArray(generateurNames);
		Random random = new Random();
		
		int i = 0;
		while (i < k) {
			String chosenMaisonName = maisonNames[random.nextInt(maisonNames.length)];
			String chosenGenerateurName = generateurNames[random.nextInt(generateurNames.length)];
			String oldGenerateurName = reseau.getConnexions().get(reseau.getMaisons().get(chosenMaisonName)).getNom();
			double oldCost = reseau.calculerCout(lambda);
			
			reseau.modifierConnexion(chosenMaisonName, oldGenerateurName, chosenGenerateurName);
			if (reseau.calculerCout(lambda) >= oldCost) { //If better then keep <=> if worse then undo. (eventually equivalent, but that's enough since we're not threading) Equal sign to keep behaviour where we only change the network if we can do better. 
				reseau.modifierConnexion(chosenMaisonName, chosenGenerateurName, oldGenerateurName);
			}
			i++;
		}
	}

}
