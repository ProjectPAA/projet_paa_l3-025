package up.mi.paa.pbl.algo;

public class Connexion_MG {
	private Maison maison;
	private Generateur generateur;
	
	public Connexion_MG(Maison maison, Generateur generateur) {
		this.maison = maison;
		this.generateur = generateur;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Connexion_MG)) {
			return Boolean.FALSE;
		}
		else {
			return (this.maison.equals(((Connexion_MG) other).getMaison()) && this.generateur.equals(((Connexion_MG) other).getGenerateur()));
		}
	}
	
	//TODO HashCode in a similar fashion

	public Maison getMaison() {
		return maison;
	}

	public Generateur getGenerateur() {
		return generateur;
	}
}
