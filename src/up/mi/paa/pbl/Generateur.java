package up.mi.paa.pbl;


/**
 * <p>Classe qui représente les Generateurs en accord avec les contraintes posées dans la description du projet.</p>
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class Generateur implements Comparable<Generateur>{ //Note: this class has a natural ordering that is inconsistent with equals.
	/**
	 * Le {@link String} représentant le nom du {@code Generateur}.
	 */
	private String nom;
	/**
	 * Le {@link Integer} donnant la capacité maximale du {@code Generateur}.
	 */
	private int capaciteMax;
	
	
	/**
	 * Constructeur du Generateur prennant en argument son nom et sa capacité.
	 * 
	 * @param n Le nom du Generateur.
	 * @param val La capacité maximale du Generateur.
	 */
	public Generateur(String n, int val) {
		this.nom = n;
		this.capaciteMax = val;
	}

	/**
	 * Decide si ce Generateur est égal à {@code other} en respectant la spécification dans {@link Object#equals(Object) Object}.
	 *
	 *@param other L'objet auquel comparer ce Generateur.
	 *@return {@code True} si et seulement si les noms et les capacités maximaux sont identiques. Si {@code other} n'est pas un Generateur, le retour est {@code False}.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Generateur)) {
			return Boolean.FALSE;
		}
		else {
			return this.nom.equals(((Generateur) other).getNom()) && this.capaciteMax == ((Generateur) other).getCapaciteMAx();
		}
	}
	
	
	
	/**
	 * Retourne le {@link String} représentant le nom du {@code Generateur}.
	 * @return Le {@link String} représentant le nom du {@code Generateur}.
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Retourne le {@link Integer} donnant la capacité maximale du {@code Generateur}.
	 * @return Le {@link Integer} donnant la capacité maximale du {@code Generateur}.
	 */
	public int getCapaciteMAx() {
		return this.capaciteMax;
	}
	
	/**
	 * Change le {@link Integer} donnant la capacité maximale du {@code Generateur}.
	 * @param val Le {@link Integer} donnant la capacité maximale du {@code Generateur}.
	 */
	public void setCapaciteMax(int val) {
		this.capaciteMax = val;
	}
	
	/**
	 * Calcul le hash représentant le {@code Generateur} en respectant la spécification dans {@link Object#hashCode() Object}.
	 * @return Le hash représentant le {@code Generateur} en respectant la spécification dans {@link Object#hashCode() Object}.
	 */
	@Override
	public int hashCode() {
		return this.nom.hashCode()*this.capaciteMax;
	}
	
	/**
	 * Retourne un {@link String} décrivant le {@code Generateur}.
	 * @return Le {@link String} décrivant le {@code Generateur}.
	 */
	@Override
    public String toString() {
        return "Générateur " + nom + " (Capacité: " + this.getCapaciteMAx() + "kWh)";
    }
	
	/**
	 * Compare ce {@code Generateur} avec {@code other} selon leur capacités maximales. Remarque: implémentation incohérente avec {@link Generateur#equals(Object) equals}.
	 * @param other Le {@code Generateur} avec lequel comparer le {@code Generateur} courant.
	 * @return Le comparaison de leur capacités maximales selon {@link Integer#compareTo(Integer)}.
	 */
	public int compareTo(Generateur other) {
		return ((Integer) this.capaciteMax).compareTo((Integer) other.getCapaciteMAx());
	}
}

