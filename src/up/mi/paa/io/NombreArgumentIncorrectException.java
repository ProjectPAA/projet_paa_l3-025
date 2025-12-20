package up.mi.paa.io;


/**
 * Exception signalant que le nombre d'arguments dans une ligne de {@linkplain java.io.File fichier} est incorrect (différent de deux).
 * @see ChargeurReseau#traiterLigne(String, Reseau)
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class NombreArgumentIncorrectException extends Exception{
	/**
	 * Version de cette classe pour la compatibilité des objets sérialisés.
	 */
	static final long serialVersionUID = 2;
	
	/**
	 * Exception signalant que le nombre d'arguments dans une ligne de {@link java.io.File fichier} est incorrect.
	 * @see ChargeurReseau#traiterLigne(String, Reseau)
	 * @param message Texte de l'exception
	 */
	public NombreArgumentIncorrectException(String message) {
		super(message);
	}
}