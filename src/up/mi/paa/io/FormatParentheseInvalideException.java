package up.mi.paa.io;

/**
 * Exception signalant que les parenthèses ouvrantes et fermantes ne sont pas dans le bon ordre lors de l'interprétation d'un {@linkplain java.io.File fichier}.
 * @see ChargeurReseau#traiterLigne(String, Reseau)
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class FormatParentheseInvalideException extends Exception{
	/**
	 * Exception signalant que les parenthèses ouvrantes et fermantes ne sont pas dans le bon ordre lors de l'interprétation d'un {@linkplain java.io.File fichier}.
	 * @see ChargeurReseau#traiterLigne(String, Reseau)
	 * @param message Texte de l'exception
	 */
	public FormatParentheseInvalideException(String message) {
		super(message);
	}
}
