package up.mi.paa.inter_face;

import java.util.Scanner;

import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.TypeConsommation;



public class InterfaceTextuelle {
	private static Scanner scan = new Scanner(System.in);
	private static Reseau reseau = new Reseau();
	
	// Ajouter de generateur depuis le saisie de user
	private static void handleAjouterGenerateur() {
		
		System.out.println("Entrez le nom et la capacité :");
		String[] ligne = scan.nextLine().split(" ");
		
		String nomGenerateur = ligne[0];
		int capaciteMax = Integer.parseInt(ligne[1]);
		
		reseau.ajouterGenerateur(nomGenerateur, capaciteMax);
				
	}
	
	// Ajouter maison 
	public static void handleAjouterMaison() {
		System.out.println("Entrez le nom et type de consommation ex: M1 NORMAL :");
		String[] ligne = scan.nextLine().split(" ");
		String nomMaison = ligne[0];
		TypeConsommation type =  TypeConsommation.valueOf(ligne[1].toUpperCase());
		
		reseau.ajouterMaison(nomMaison, type);
	}
	
	// Ajouter connexion
	public static void handleAjouterConnexion() {
		System.out.println("Donner de la maison et du générateur ex : M1 G1");
		String[] ligne = scan.nextLine().split(" ");
		String nomMaison = ligne[0];
		String nomGenerateur = ligne[1];
		
		if(reseau.getMaisons().containsKey(nomMaison) && reseau.getGenerateurs().containsKey(nomGenerateur)) {
			reseau.ajouterConnexion(nomMaison, nomGenerateur);
		}
		if(reseau.getMaisons().containsKey(nomGenerateur) && reseau.getGenerateurs().containsKey(nomMaison)) {
			reseau.ajouterConnexion(nomMaison, nomGenerateur);
		}
	}
	
	// Verifier si une ou plusieurs maisons ne sont pas connectés
	public static boolean verifierConnexion() {
		return reseau.verifierConnexions();	
	}
	
	// Le menu principal	
	private static void lancerMenuPrincipal() {
	boolean enCours = true;
		
		while(enCours) {
			System.out.println("1. Ajouter un générateur.");
			System.out.println("2. Ajouter une maison.");
			System.out.println("3. Ajouter une connexion.");
			System.out.println("4. Fin.");
			
			String choix = scan.nextLine();
			switch(choix) {
			case "1" : handleAjouterGenerateur();
			break;
			case "2" : handleAjouterMaison();
			break;
			case "3" : handleAjouterConnexion();
			break;
			case "4":
			if(verifierConnexion()) {
				enCours = false;
			}else {
				System.out.println("La (es) configuration(s) n'est (ne sont) pas correct ! Veuillez corriger.");
			}
			break;
			default:System.out.println("Saisir incorrect !");
			break;
				
			}
		}
	}
	public static void main(String[] args) {
		
		System.out.println("Bienvenue....");
		
		lancerMenuPrincipal();
	
	}

	
}
