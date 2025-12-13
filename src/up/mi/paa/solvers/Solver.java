package up.mi.paa.solvers;
import up.mi.paa.pbl.algo.Reseau;

public abstract class Solver {
	protected Reseau reseau;
	
	public abstract void solve(double lambda);	//Tout algorithme aura besoin de lambda car lambda intervient dans la fonction d'utilité.
}
