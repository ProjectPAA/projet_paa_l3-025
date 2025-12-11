package up.mi.paa.io;

import up.mi.paa.pbl.algo.Reseau;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class ChargeurReseau {

	private int numeroLigne = 0; 
	private Reseau reseau;
	
	public ChargeurReseau() {
		reseau = new Reseau();
	}
	
	/**
	 * Lit le fichier et retourne un réseau rempli.
	 * @param cheminFichier : le chemin du fichier avec .txt
	 * @throws FileNotFoundException : Si le fichier n'existe pas.
	 * @return this.reseau : Retourne le réseau construit.
	 * */
	public Reseau charger(String cheminFichier) throws FileNotFoundException {
		
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
		
		return this.reseau;
		
	}
	
	/**
	 * Analyse une ligne unique et met à jour le réseau.
	 * @param ligne : Ligne lu dans le fichier
	 * @param reseau : Réseau à construire.
	 * */
	
	public void traiterLigne(String ligne, Reseau reseau) throws IllegalArgumentException{
		if(ligne.startsWith("generateur")) {
			// TODO : Parse générateur
			System.out.println("Ligne " + this.numeroLigne + " Générateur détecté -> " + ligne);
		} else if(ligne.startsWith("maison")) {
			// TODO : Parse Maison
			System.out.println("Ligne " + this.numeroLigne + " Maison détectée -> " + ligne);
		} else if(ligne.startsWith("connexion")) {
			// TODO : Parse Connexion
			System.out.println("Ligne " + this.numeroLigne + " Connexion détectée -> " + ligne);
		} else {
			throw new IllegalArgumentException("Erreur syntaxe ligne " + this.numeroLigne + "Instruction inconnue.");
		}
	}
}
 