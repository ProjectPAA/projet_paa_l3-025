package up.mi.paa.solvers;
import up.mi.paa.pbl.algo.Reseau;

/**
 * <p>Cette classe abstraite est la généralisation de tout algorithme/approche permettant de résoudre ou approcher la solution du problème de la minimisation du coût d'un {@link Reseau}.</p>
 * <p>Elle est aussi un exemple du patron de conception {@code Strategy}.</p>
 * <p>Les classes enfantes <i>doivent</i> implémenter un constructeur se comportant comme :</p> 
 * <p>{@code public ConcreteSolver(Reseau reseau) {this.reseau = reseau;}}</p>
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public abstract class Solver {
	/**
	 * Le {@link Reseau} sur lequel le Solver va travailler.
	 */
	protected Reseau reseau;
	
	/**
	 * Ce constructeur n'est là que pour des raison syntaxiques. 
	 */
	public Solver() {}
	
	/**
	 * La méthode qui va modifier {@link #reseau} pour qu'il contienne les connexions d'une configuration (plus) optimale en termes de coût.
	 * @param lambda La valeur de λ à utiliser pour pénaliser la surcharge.
	 */
	public abstract void solve(double lambda);	//Tout algorithme aura besoin de lambda car lambda intervient dans la fonction d'utilité.
}
