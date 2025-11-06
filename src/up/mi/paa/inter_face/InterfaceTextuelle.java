package up.mi.paa.inter_face;

import java.util.Scanner;

import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
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
	private static void handleAjouterMaison() {
		System.out.println("Entrez le nom et type de consommation ex: M1 (BASE/NORMALE/FORTE) :");
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
	private static void handleAjouterConnexion() {
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

	private static void handleModifierConnexion() {
		try {
			// Demande l'ancienne connexion
			System.out.println("Veuillez saisir la connexion que vous souhaitez modifier (ex: M1 G1):");
			String[] ligneAncienne = scan.nextLine().trim().split("\\s+");

			// On vérifie que l'utilisateur a bien tapé 2 mots
			if (ligneAncienne.length < 2)
				throw new ArrayIndexOutOfBoundsException();

			String nomMaison = ligneAncienne[0];
			String nomAncienGen = ligneAncienne[1];

			Maison maison = reseau.getMaisons().get(nomMaison);
			Generateur genAttendu = reseau.getGenerateurs().get(nomAncienGen);

			// Vérification des null
			if (maison == null) {
				System.out.println("=> ERREUR : La maison '" + nomMaison + "' n'existe pas.");
				return;
			}
			if (genAttendu == null) {
				System.out.println("=> ERREUR : L'ancien générateur '" + nomAncienGen + "' n'existe pas.");
				return;
			}

			// Vérification que la connexion est existante
			Generateur genActuel = reseau.getConnexions().get(maison);

			if (genActuel == null || !genActuel.equals(genAttendu)) {
				String nomGenActuel = (genActuel == null) ? "rien" : genActuel.getNom();
				System.out.println("=> ERREUR : La connexion '" + nomMaison + " -> " + nomAncienGen + "' n'existe pas.");
				System.out.println(" (La maison '" + nomMaison + "' est connectée à '" + nomGenActuel + "')");
				return;
			}

			System.out.println("Veuillez saisir la nouvelle connexion (ex: M1 G2):");
			String[] ligneNouvelle = scan.nextLine().trim().split("\\s+");

			if (ligneNouvelle.length < 2) throw new ArrayIndexOutOfBoundsException();
			String nomMaisonNouvelle = ligneNouvelle[0];
			String nomNouveauGen = ligneNouvelle[1];

			// Vérification que la maison est la même
			if (!nomMaison.equals(nomMaisonNouvelle)) {
				System.out.println("=> ERREUR : La maison doit être la même dans les deux saisies.");
				System.out.println(" (Vous avez saisi '" + nomMaison + "' puis '" + nomMaisonNouvelle + "')");
				return;
			}

			reseau.modifierConnexion(nomMaison, nomAncienGen, nomNouveauGen);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Format incorrect. Vous devez entrer deux noms séparés par un espace.");
		}
	}
	// Verifier si une ou plusieurs maisons ne sont pas connectés
	private static boolean verifierConnexion() {
		return reseau.verifierConnexions();	
	}
	
	// Afficher le reseau 
	private static void handleAfficherReseau() {
		reseau.afficherReseau();
	}
	
	/**
	 * Gére l'option 1 du menu 2 : Calculer le coût du réseau.
	 */
	private static void handleCalculerCout() {
		double lambda = 10.0;
		
		System.out.println("Calcul du coût du réseau (avec lambda = " + lambda + ")...");
		// Appelle la méthode disp()
	    double disp = reseau.disp();

	    // Appelle la méthode surcharge()
	    double surcharge = reseau.surcharge(lambda);

	    // Calcule le coût total
	    double coutTotal = disp + surcharge;

	    // Affiche les 3 résultats, comme demandé par le PDF 
	    System.out.println("--------- RÉSULTAT DU CALCUL ---------");
	    System.out.println("Dispersion (Disp(S)) : " + disp);
	    System.out.println("Surcharge (Surcharge(S)) : " + surcharge);
	    System.out.println("COÛT TOTAL (Cout(S)) : " + coutTotal);
	    System.out.println("--------------------------------------");
	}
	
	// Menu Secondaire 
	public static void lancerMenuSecondaire() {
		boolean enCours = true;
		while(enCours) {
			System.out.println("============== Menu Secondaire =================");
			System.out.println("1. Calculer.");
			System.out.println("2. Modifier.");
			System.out.println("3. Afficher le réseau.");
			System.out.println("4. Fin.");
			System.out.println("============== Fin : Menu Secondaire =================");
			
			String choix = scan.nextLine();
			switch(choix) {
			case "1": handleCalculerCout();
			break;
			case "2":handleModifierConnexion();
			break;
			case "3": handleAfficherReseau();;
			break;
			case "4":
				enCours = false;
			break;
			default:System.out.println("Saisir incorrect !");
			break;
				
			}
		}
		System.out.println("\nVous avez quitter le menu secondaire.");
		}
	
	
	// Le menu principal	
	private static void lancerMenuPrincipal() {
	boolean enCours = true;
		while(enCours) {
			System.out.println("\n============== Menu Principal =================");
			
			System.out.println("1. Ajouter un générateur.");
			System.out.println("2. Ajouter une maison.");
			System.out.println("3. Ajouter une connexion.");
			System.out.println("4. Fin.");
			System.out.println("\n============== Fin : Menu Principal =================");
			
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
				lancerMenuSecondaire(); // Menu Secondaire
			}else {
				System.out.println("La (es) configuration(s) n'est (ne sont) pas correct ! Veuillez corriger.");
			}
			break;
			default:System.out.println("Saisir incorrect !");
			break;
				
			}
		}
		
		System.out.println("\nVous avez quitter le menu principal.");
	}
	public static void main(String[] args) {
		
		System.out.println("Bienvenue....");
		
		lancerMenuPrincipal();
	
	}

	
}
