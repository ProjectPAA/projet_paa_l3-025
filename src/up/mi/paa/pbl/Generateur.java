package up.mi.paa.pbl;


/**
 * <p>Classe qui représente les générateurs en accord avec les contraintes posées dans la description du projet.</p>
 * <p>Remarque : Cette classe possède un ordre naturel incohérent avec {@link Generateur#equals(Object) equals}.</p>
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA
 * @author Zalán MOLNÁR
 */
public class Generateur implements Comparable<Generateur>{ //Note: this class has a natural ordering that is inconsistent with equals.
	/**
	 * Le {@link String} représentant le nom du {@code Generateur}.
	 */
	private String nom;
	/**
	 * L'{@link Integer} donnant la capacité maximale du {@code Generateur}.
	 */
	private int capaciteMax;
	
	
	/**
	 * Constructeur du {@code Generateur} prenant en argument son nom et sa capacité.
	 * 
	 * @param n Le nom du {@code Generateur}.
	 * @param val La capacité maximale du {@code Generateur}.
	 */
	public Generateur(String n, int val) {
		this.nom = n;
		this.capaciteMax = val;
	}

	/**
	 * Décide si ce Generateur est égal à {@code other} en respectant la spécification dans {@link Object#equals(Object) Object}.
	 *
	 *@param other L'objet auquel comparer ce {@code Generateur}.
	 *@return {@code True} si et seulement si les noms et les capacités maximaux sont identiques. Si {@code other} n'est pas un {@code Generateur}, le retour est {@code False}.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Generateur)) {
			return Boolean.FALSE;
		}
		else {
			return this.nom.equals(((Generateur) other).getNom()) && this.capaciteMax == ((Generateur) other).getCapaciteMax();
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
	public int getCapaciteMax() {
		return this.capaciteMax;
	}
	
	/**
	 * Change l'{@link Integer} donnant la capacité maximale du {@code Generateur}.
	 * @param val L'{@link Integer} donnant la capacité maximale du {@code Generateur}.
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
        return "Générateur " + nom + " (Capacité: " + this.getCapaciteMax() + "kWh)";
    }
	
	/**
	 * Compare ce {@code Generateur} avec {@code other} selon leurs capacités maximales. Remarque : implémentation incohérente avec {@link Generateur#equals(Object) equals}.
	 * @param other Le {@code Generateur} avec lequel comparer le {@code Generateur} courant.
	 * @return Le comparaison de leurs capacités maximales selon {@link Integer#compareTo(Integer)}.
	 */
	public int compareTo(Generateur other) {
		return ((Integer) this.capaciteMax).compareTo((Integer) other.getCapaciteMax());
	}
}

