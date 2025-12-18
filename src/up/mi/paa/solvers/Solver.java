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
public abstract class Solver implements Runnable {
	/**
	 * Le {@link Reseau} sur lequel le Solver va travailler.
	 */
	protected Reseau reseau;
	protected double lambda;
	
	/**
	 * Constructeur par défaut explicité pour le rendre private. (au lieu de friendly)
	 */
	private Solver() {}
	
	/**
	 * Constructeur à utiliser par les sous-classes concrètes en appelant {@code super(Reseau, lambda)}.
	 * 
	 * @param reseau Le {@link Reseau} à traiter.
	 * @param lambda Le λ donnant la pénalisation de surcharge dans le calcul du coût.
	 */
	public Solver(Reseau reseau, double lambda) {
		this(); 	//Constructeur par défaut explicité pour le rendre private. (au lieu de friendly)
		this.reseau = reseau;
		this.lambda = lambda;
	}
	
	/**
	 * La méthode qui va modifier {@link #reseau} pour qu'il contienne les connexions d'une configuration (plus) optimale en termes de coût.
	 * @param lambda La valeur de λ à utiliser pour pénaliser la surcharge.
	 */
	public abstract void solve();	//Tout algorithme aura besoin de lambda car lambda intervient dans la fonction d'utilité.
	
	public void run() {
		this.solve();	//"this" relance la résolution des références depuis l'objet courant, pour assurer qu'on utilise le bon lambda et la bonne implémentation. Au moment où j'écris ça, c'est overkill de faire ça, mais pour l'extensibilité.
	}
}
