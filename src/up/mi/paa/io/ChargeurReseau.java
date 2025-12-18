package up.mi.paa.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import up.mi.paa.pbl.Reseau;
import up.mi.paa.pbl.TypeConsommation;


/**
 * Cette classe est responsable de charger un {@link up.mi.paa.pbl.Reseau} stocké en {@link File fichier} et de le transformer en objet de type {@linkplain up.mi.paa.pbl.Reseau}.
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class ChargeurReseau {

	/**
	 * L'ordinale de la ligne du fichier en cours de traitement.
	 */
	private int numeroLigne = 0; 
	/**
	 * Les {@link up.mi.paa.pbl.Reseau reseau} en cours de chargement.
	 */
	private Reseau reseau;
	
	/**
	 * Constructeur unique.
	 */
	public ChargeurReseau() {
		reseau = new Reseau();
	}
	
	/**
	 * Lit le fichier et retourne un réseau rempli.
	 * @param cheminFichier : le chemin du fichier avec .txt
	 * @throws FileNotFoundException : Si le fichier n'existe pas.
	 * @return this.reseau : Retourne le réseau construit.
	 * @throws FormatParentheseInvalideException 
	 * @throws NombreArgumentIncorrectException 
	 * @throws IllegalArgumentException 
	 * */
	public Reseau charger(String cheminFichier) throws FileNotFoundException, IllegalArgumentException, NombreArgumentIncorrectException, FormatParentheseInvalideException {
		
		File fichier = new File(cheminFichier);
		Scanner scanner = new Scanner(fichier);
		
		System.out.println("Debut du chargement de : " + cheminFichier);
		
		try {
			while(scanner.hasNextLine()) {
				String ligne = scanner.nextLine().trim();
				this.numeroLigne++;
				
				if(ligne.isEmpty())continue;
				
				// Analyse de la ligne
				traiterLigne(ligne, this.reseau);
			}
			
		} finally{
			scanner.close();
		}
		
		   System.out.println("\n=========== Réseau chargé avec succès. ===========================");
           System.out.println("Nombre de maisons : " + this.reseau.getMaisons().size());
           System.out.println("Nombre de générateurs : " + this.reseau.getGenerateurs().size());
           reseau.afficherReseau();
		return this.reseau;
		
	}
	
	/**
	 * Analyse une ligne unique et met-à-jour le réseau.
	 * @param ligne : Ligne lu dans le fichier
	 * @param reseau : Réseau à construire.
	 * */
	
	private void traiterLigne(String ligne, Reseau reseau) throws IllegalArgumentException, NombreArgumentIncorrectException, FormatParentheseInvalideException{
		if(ligne.startsWith("generateur")) {
		
			parserGenerateur(ligne);
		} else if(ligne.startsWith("maison")) {
		
			parserMaison(ligne);
		} else if(ligne.startsWith("connexion")) {
			
			parserConnexion(ligne);
		} else {
			throw new IllegalArgumentException("Erreur syntaxe ligne " + this.numeroLigne + "Instruction inconnue.");
		}
	}
	
	/**
	 * À partir d'une ligne contenant un {@link up.mi.paa.pbl.Generateur} cette méthode construit l'objet {@linkplain up.mi.paa.pbl.Generateur} correspondant.
	 * @param ligne La ligne contenant un {@link up.mi.paa.pbl.Generateur}.
	 * */	
	private void parserGenerateur(String ligne) throws FormatParentheseInvalideException, NombreArgumentIncorrectException{
		
			String[] args = extraireArguments(ligne);
			if(args.length != 2) {
				throw new NombreArgumentIncorrectException("Erreur : Nombre d'arguments incorrect pour un générateur.");
			}
			
			try {
				String nom = args[0];
				int puissance = Integer.parseInt(args[1]);
				this.reseau.ajouterGenerateur(nom, puissance, true);
			}catch(NumberFormatException e) {
				System.out.println("Erreur : La puisscance doit être en nombre (ex : 60).");
			}
		
	}
	
	/**
	 * À partir d'une ligne contenant une {@link up.mi.paa.pbl.Maison} cette méthode construit l'objet {@linkplain up.mi.paa.pbl.Maison} correspondant.
	 * @param ligne La ligne contenant une {@link up.mi.paa.pbl.Maison}.
	 * */
	private void parserMaison(String ligne) throws FormatParentheseInvalideException, NombreArgumentIncorrectException{
		if(ligne.contains("(") && ligne.contains(")")) {
			String[] args = extraireArguments(ligne);
			if(args.length != 2) {
				throw new NombreArgumentIncorrectException("Erreur : Nombre d'arguments incorrect pour un Maison.");
			}
			
			try {
				String nom = args[0];
				TypeConsommation type = TypeConsommation.valueOf(args[1].toUpperCase());
				this.reseau.ajouterMaison(nom, type, true);
			}catch(NumberFormatException e) {
				System.out.println("Erreur : La puisscance doit être en nombre (ex : 60).");
			}
		} else {
			String[] args = ligne.trim().split("\\s+");
			if(args.length != 2) {
				throw new NombreArgumentIncorrectException("Erreur : Nombre d'arguments incorrect pour un Maison.");
			}
			
			try {
				String nom = args[0].trim();
				TypeConsommation type = TypeConsommation.valueOf(args[1].trim().toUpperCase());
				this.reseau.ajouterMaison(nom, type, true);
			}catch(NumberFormatException e) {
				System.out.println("Erreur : La puisscance doit être en nombre (ex : 60).");
			}
			
		}
	}
	
	/**
	 * À partir d'une ligne contenant une {@link up.mi.paa.pbl.Reseau#getConnexions()} cette méthode construit l'objet {@linkplain up.mi.paa.pbl.Reseau#getConnexions()} correspondant.
	 * @param ligne La ligne contenant une {@link up.mi.paa.pbl.Reseau#getConnexions()}.
	 * */
	private void parserConnexion(String ligne) throws FormatParentheseInvalideException, NombreArgumentIncorrectException {
		String[] args = extraireArguments(ligne);
		if(args.length != 2) {
			throw new NombreArgumentIncorrectException("Erreur : Nombre d'arguments incorrect pour un générateur.");
		}
		
		try {
			String nomGenerateur;
			String nomMaison;
			if(this.reseau.getMaisons().containsKey(args[0].trim())) {
				nomMaison = args[0].trim();
				nomGenerateur = args[1].trim();
			}else {
				nomMaison = args[1].trim();
				nomGenerateur = args[0].trim();
			}
			if(nomMaison.isEmpty() || nomGenerateur.isEmpty()) {
				System.out.println("Erreur : La maison ou le générateur n'existe pas.");
				return;
			}
			// All is Ok.
			this.reseau.ajouterConnexion(nomMaison, nomGenerateur, true);
		}catch(NumberFormatException e) {
			throw new NumberFormatException("Ligne " + numeroLigne + " : La puissance doit être un entier valide.");
		}
	}
	
	/**
	 * Extrait les arguments de l'objet donnés dans la lignes
	 * @param ligne La ligne à traiter.
	 */
	private String[] extraireArguments(String ligne) throws IllegalArgumentException, FormatParentheseInvalideException{
		
		String[] chaineTabClean;
		if(ligne.endsWith(".")) {
			int indexParentheseOpen = ligne.indexOf('(');
			int indexParentheseClose = ligne.indexOf(')');
			if(indexParentheseClose == -1 || indexParentheseOpen == -1 || indexParentheseOpen > indexParentheseClose) {
				throw new FormatParentheseInvalideException("Erreur : La ligne ne contient pas de(s) parenthèse(s) ou la paranthèse fermante est avant parathèse ouvrante.");
			}
			String chaine = ligne.substring(indexParentheseOpen + 1, indexParentheseClose);
			
			String[] chaineTab = chaine.split(",");
			chaineTabClean = new String[chaineTab.length];
			for(int i = 0; i < chaineTab.length; i++) {
				chaineTabClean[i] = chaineTab[i].trim(); // Netoyage des espace
			}
		}else {
			throw new IllegalArgumentException("Erreur : La ligne doit finir par un point (.).");
		}
		return chaineTabClean;
	}
 	
}
 