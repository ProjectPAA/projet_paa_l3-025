package up.mi.paa.pbl.algo;

/**
 * <p>Classe qui représente les Maisons en accord avec les contraintes de la description du projet.</p> 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class Maison {
	/**
	 * Le {@link String} représentant le nom de la {@code Maison}.
	 */
	private String nom;
	/**
	 * Le {@link TypeConsommation type de consommation} de la {@code Maison}.
	 */
	private TypeConsommation type;
	
	
	/**
	 * Constructeur de {@code Maison} à partir de son nom et type de consommation.
	 * @param n Le {@link nom} de la {@code Maison}.
	 * @param t Le {@link type type de consommation} de la {@code Maison}.
	 */
	public Maison(String n, TypeConsommation t) {
		this.nom = n;
		this.type = t;
	}
	
	
	/**
	 * Retourne le {@link String} représentant le nom de la {@code Maison}.
	 * @return Le {@link String} représentant le nom de la {@code Maison}.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * Retourne le {@link TypeConsommation type de consommation} de la {@code Maison}.
	 * @return Le {@link TypeConsommation type de consommation} de la {@code Maison}.
	 */
	public TypeConsommation getTypeConsommation() {
		return this.type;
	}
	
	/**
	 * Change le {@link TypeConsommation type de consommation} de la {@code Maison}.
	 * @param t Le {@link TypeConsommation type de consommation} de la {@code Maison}.
	 */
	public void setType(TypeConsommation t) {
		this.type = t;
	}
	
	/**
	 * <p>Retourne {@code True} si et seulement si le {@link nom} et le {@link type type de consommation} de cette {@code Maison} sont égaux à ceux de la {@code Maison} en paramètres. Retourne {@False} si {@code other} n'est pas une instance de {@code Maison}.</p>
	 * <p>Cette méthode respecte la spécification dans {@link Object#equals(Object) Object}.</p>
	 * @param other L'Objet auquel comparer cette {@code Maison}.
	 * @return {@code True} si et seulement si le {@link nom} et le {@link type type de consommation} de cette {@code Maison} sont égaux à ceux de la {@code Maison} en paramètres. Retourne {@False} si {@code other} n'est pas une instance de {@code Maison}.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Maison)) {
			return Boolean.FALSE;
		} else {
			return this.nom.equals(((Maison) other).getNom()) && this.type.equals(((Maison) other).getTypeConsommation());
		}
	}
	
	/**
	 * Calcul un hash représentatif de la {@code Maison} en prenant en compte le {@link nom} et le {@link type type de consommation}. Cette méthode est en accord avec la spécification en {@link Object#hashCode() Object}.
	 *@return Un hash représentatif de la {@code Maison}.
	 */
	@Override
	public int hashCode() {
		return nom.hashCode()*type.getDemande();
	}
	
	/**
	 * Retourne une représentation en {@linkplain String} de cette Maison.
	 * @return Une représentation en {@linkplain String} de cette Maison.
	 */
	@Override
    public String toString() {
        return "Maison " + nom + " (Consommation: " + this.getTypeConsommation().name() + ", " + this.getTypeConsommation().getDemande() + "kWh)";
    }
	

}
