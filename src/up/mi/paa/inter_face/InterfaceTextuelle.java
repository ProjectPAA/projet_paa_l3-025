package up.mi.paa.inter_face;

import java.util.Scanner;

import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.TypeConsommation;



public class InterfaceTextuelle {
	private static Scanner scan = new Scanner(System.in);
	private static Reseau reseau = new Reseau();
	
	// Ajouter de generateur depuis le saisie de user
	private static void handleAjouterGenerateur() {
		
		System.out.println("Entrez le nom et la capacité (ex: G1 60):");
		String[] ligne = scan.nextLine().split(" ");
		
		try {
			String nomGenerateur = ligne[0];
			int capaciteMax = Integer.parseInt(ligne[1]);
			
			reseau.ajouterGenerateur(nomGenerateur, capaciteMax);
		} catch (NumberFormatException e) {
			System.out.println("=> ERREUR : La capacité doit être un nombre");
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer un nom ET une capacité.");
			
		}
				
	}
	
	// Ajouter maison 
	public static void handleAjouterMaison() {
		System.out.println("Entrez le nom et type de consommation ex: M1 NORMALE :");
		String[] ligne = scan.nextLine().split(" ");
		// on gere les erreurs 
		try {
			String nomMaison = ligne[0];
			TypeConsommation type =  TypeConsommation.valueOf(ligne[1].toUpperCase());
			
			reseau.ajouterMaison(nomMaison, type);
		}catch (IllegalArgumentException e) {
			System.out.println("=> ERREUR : Type de consommation invalid. Utilisez BASE, NORMALE ou FORTE");
		}catch (ArrayIndexOutOfBoundsException e){
			System.out.println("=> ERREUR : Vous devez entrer un non ET un type (ex: M1 NORMALE).");
		}
		
	}
	
	// Ajouter connexion
	public static void handleAjouterConnexion() {
		System.out.println("Donner de la maison et du générateur ex : M1 G1");
		String[] ligne = scan.nextLine().split(" ");
		try {
			String nomMaison = ligne[0];
			String nomGenerateur = ligne[1];
			if(reseau.getMaisons().containsKey(nomMaison) && reseau.getGenerateurs().containsKey(nomGenerateur)) {
				reseau.ajouterConnexion(nomMaison, nomGenerateur);
			}
			else if(reseau.getMaisons().containsKey(nomGenerateur) && reseau.getGenerateurs().containsKey(nomMaison)) {
				reseau.ajouterConnexion(nomGenerateur, nomMaison);
			}
			else {
				System.out.println("=> ERREUR : La maison ou le générateur n'existe pas.");
			}
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer une maison ET un générateur.");
		}
		
		
	}
	
	// Verifier si une ou plusieurs maisons ne sont pas connectés
	public static boolean verifierConnexion() {
		return reseau.verifierConnexions();	
	}
	
	// Le menu principal	
	private static void lancerMenuPrincipal() {
	boolean enCours = true;
		System.out.println("============== Menu =================");
		while(enCours) {
			
			System.out.println("1. Ajouter un générateur.");
			System.out.println("2. Ajouter une maison.");
			System.out.println("3. Ajouter une connexion.");
			System.out.println("4. Fin.");
			
			String choix = scan.nextLine();
			switch(choix) {
			case "1": handleAjouterGenerateur();
			break;
			case "2": handleAjouterMaison();
			break;
			case "3": handleAjouterConnexion();
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
		
		Sytem.out.println("Vous avez quitter le menu.");
	}
	public static void main(String[] args) {
		
		System.out.println("Bienvenue....");
		
		lancerMenuPrincipal();
	
	}

	
}
