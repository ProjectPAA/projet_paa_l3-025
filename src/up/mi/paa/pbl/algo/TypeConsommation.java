package up.mi.paa.pbl.algo;


/**
 * Enumération des possibles types de consommation des {@link Maison}s en accord avec les contraintes de la description du projet.
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public enum TypeConsommation {
	
	/**
	 * Valeur BASSE, 10 kWh.
	 */
	BASSE(10), 
	/**
	 * Valeur NORMAL, 20kWh.
	 */
	NORMAL(20), 
	/**
	 * Valeur FORTE, 40 kWh.
	 */
	FORTE(40);
	
	private final int demande;
	
	private TypeConsommation(int val) {
		this.demande = val;
	}
	
	/**
	 * Retourne le nombre de kWh demandés par une {@link Maison} de ce {@code TypeConsommation}.
	 * @return Le nombre de kWh demandés par une {@link Maison} de ce {@code TypeConsommation}.
	 */
	public int getDemande() {
		return this.demande;
	}
}
