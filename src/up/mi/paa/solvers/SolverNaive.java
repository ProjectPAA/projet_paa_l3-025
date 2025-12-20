package up.mi.paa.solvers;

import java.util.Random;

import up.mi.paa.pbl.Reseau;

/**
 * Cette classe implémente l'algorithme naif donné dans la description de la seconde partie du projet.
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class SolverNaive extends Solver {
	
	/**
	 * @param reseau Le {@link Reseau} à traiter.
	 * @param lambda Le λ donnant la pénalisation de surcharge dans le calcul du coût.
	 */
	public SolverNaive(Reseau reseau, double lambda) {
		super(reseau, lambda);
	}
	
	/**
	 * <p>Implémentation de l'algorithme naif donné en description du projet. Ne change pas le reseau si on n'a pas réussi a faire moins cher.</p> 
	 * 
	 * <p>Execute un nombre par défaut d'itérations.</p>
	 *
	 */
	public void solve() {
		this.solve(1000);	//Un k est nécéssaire pour cet algorithme, si on est appelé sans, on choisi un valeur par défaut.
	}

	/**
	 * <p>Implémentation de l'algorithme naif donné en description du projet. Ne change pas le reseau si on n'a pas réussi a faire moins cher.</p>
	 * 
	 * @param k nombre d'itérations a executer avant de s'arreter
	 */
	public void solve(int k) {
		String[] maisonNames = {};
		String[] generateurNames = {};
		maisonNames = reseau.getMaisons().keySet().toArray(maisonNames);	//Random gives us indices. Using the Strings makes the syntax more readable because we'll get reseau's attributes' attributes multiple times, and those are keyed by these Strings.
		generateurNames = reseau.getGenerateurs().keySet().toArray(generateurNames);
		Random random = new Random();
		
		int i = 0;
		while (i < k && !Thread.currentThread().isInterrupted()) {
			String chosenMaisonName = maisonNames[random.nextInt(maisonNames.length)];
			String chosenGenerateurName = generateurNames[random.nextInt(generateurNames.length)];
			String oldGenerateurName = reseau.getConnexions().get(reseau.getMaisons().get(chosenMaisonName)).getNom();
			double oldCost = reseau.calculerCout(lambda);
			
			reseau.modifierConnexion(chosenMaisonName, oldGenerateurName, chosenGenerateurName, true);
			if (reseau.calculerCout(lambda) >= oldCost) { //If better then keep <=> if worse then undo. (eventually equivalent, but that's enough since no other thread accesses these structures concurrently) Equal sign to keep behaviour where we only change the network if we can do better. 
				reseau.modifierConnexion(chosenMaisonName, chosenGenerateurName, oldGenerateurName, true);
			}
			i++;
		}
        System.out.println("Algorithm Naif terminé avec coût minimal trouvé : " + reseau.calculerCout(lambda));
	}

}
