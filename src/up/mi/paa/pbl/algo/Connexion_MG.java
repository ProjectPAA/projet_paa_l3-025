package up.mi.paa.pbl.algo;

import java.util.HashSet;

public class Connexion_MG {
	private Maison maison;
	private Generateur generateur;
	private static HashSet<Connexion_MG> setConnexion_MG = new HashSet<Connexion_MG>();
	
	public Connexion_MG(Maison maison, Generateur generateur) {
		this.maison = maison;
		this.generateur = generateur;
		Connexion_MG.setConnexion_MG.add(this);
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
	
	@Override
	public int hashCode() {
		return (251*this.maison.hashCode())+this.generateur.hashCode();
		/*Il y aura des collisions, mais il n'est pas nécessaire de les éviter.
		 * La multiplication avec un premier évite juste ("M", "G").hashCode == ("G", "M").hashCode, où les deux paires sont (instanceof Maison, instanceof Generateur)...*/
	}
	
	public Maison getMaison() {
		return maison;
	}

	public Generateur getGenerateur() {
		return generateur;
	}
	
	public void update(Generateur generateur) {
		this.update(this.maison, generateur);
	}
	
	public void update(Maison maison) {
		this.update(maison, this.generateur);
	}

	public void update(Maison maison, Generateur generateur) {
		Connexion_MG.setConnexion_MG.remove(this);
		this.generateur = generateur;
		this.maison = maison;
		Connexion_MG.setConnexion_MG.add(this);
	}
	/*TODO Il est possible de conserver une référence a une connexion_MG qui n'est plus dans le hashSet. 
	 * 	   Je ne sais pas comment régler ça.*/
}
