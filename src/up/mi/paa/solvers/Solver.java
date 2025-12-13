package up.mi.paa.solvers;
import up.mi.paa.pbl.algo.Reseau;

public abstract class Solver {
	/**
	 * This method SHOULD BE OVERRIDDEN by concrete Solvers. Since in java a method cannot be static and abstract (neither for a class nor an interface), as a workaround this throws UnsupportedOperationException. 
	 * 
	 * @throws UnsupportedOperationException Only concrete classes implementing actual solving algorithms should have this method called.
	 */
	public static void solve(Reseau reseau, double lambda) {	//Tout algorithme aura besoin de lambda car lambda intervient dans la fonction d'utilité.
		throw new UnsupportedOperationException();
	}
}
