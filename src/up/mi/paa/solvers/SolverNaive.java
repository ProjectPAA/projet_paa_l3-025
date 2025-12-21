package up.mi.paa.solvers;

import java.util.Random;

import up.mi.paa.pbl.Generateur;
import up.mi.paa.pbl.Maison;
import up.mi.paa.pbl.Reseau;

/**
 * Cette classe implémente l'algorithme naif donné dans la description de la seconde partie du projet.
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA
 * @author Zalán MOLNÁR
 */
public class SolverNaive extends Solver {
	
	/**
	 * Constructeur à partir du {@link Reseau} à traiter.
	 * @param reseau Le {@link Reseau} à traiter.
	 * @param lambda Le λ donnant la pénalisation de surcharge dans le calcul du coût.
	 */
	public SolverNaive(Reseau reseau, double lambda) {
		super(reseau, lambda);
	}
	
	/**
	 * <p>Implémentation de l'algorithme naif donné en description du projet. Ne change pas le reseau si on n'a pas réussi à faire moins cher.</p>
	 * 
	 * <p>Execute un nombre par défaut d'itérations.</p>
	 *
	 */
	public void solve() {
		this.solve(1000);	//Un k est nécessaire pour cet algorithme, si on est appelé sans et on choisit une valeur par défaut.
	}

	/**
	 * <p>Implémentation de l'algorithme naif donné en description du projet. Ne change pas le réseau si on n'a pas réussi à faire moins cher.</p>
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
			
			// On verifie si la maison a déjà un générateur
			Maison m = reseau.getMaisons().get(chosenMaisonName);
			Generateur gActuel = reseau.getConnexions().get(m);
			String oldGenerateurName = (gActuel != null) ? gActuel.getNom() : null;
			double oldCost = reseau.calculerCout(lambda);
			
			// On tente la nouvelle connexion
			reseau.ajouterConnexion(chosenMaisonName, chosenGenerateurName, true);
			
			if (reseau.calculerCout(lambda) >= oldCost) { //If better then keep <=> if worse then undo. (eventually equivalent, but that's enough since no other thread accesses these structures concurrently) Equal sign to keep behaviour where we only change the network if we can do better. 
				// Echec : on doit remettre l'ancien état
				if(oldGenerateurName != null) {
					// Elle avait un générateur, on lui redonne
					reseau.ajouterConnexion(chosenMaisonName, oldGenerateurName, true);
				}else {
					// Elle n'avait rien, on la déconnecte
					reseau.getConnexions().remove(m);
				}
			}
			i++;
		}
        System.out.println("Algorithm Naif terminé avec coût minimal trouvé : " + reseau.calculerCout(lambda));
	}

}
