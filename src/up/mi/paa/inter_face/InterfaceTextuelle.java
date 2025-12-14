package up.mi.paa.inter_face;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import up.mi.paa.io.ChargeurReseau;
import up.mi.paa.io.FormatParentheseInvalideException;
import up.mi.paa.io.NombreArgumentIncorrectException;
import up.mi.paa.io.SauvegardeurReseau;
import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.Reseau;
import up.mi.paa.pbl.algo.TypeConsommation;
import up.mi.paa.solvers.SolverGreedyGenerateur;
import up.mi.paa.solvers.SolverNaive;



public class InterfaceTextuelle { 
	
	// Scanner unique pour tout le programme (Clavier)
	private static Scanner scan = new Scanner(System.in);
	private static Reseau reseau = new Reseau();
	
	/* ==========================================
	 * MÉTHODES PARTIE 1 (Mode Manuel)
	 * ========================================== */
	
	private static void handleAjouterGenerateur() {
		
		System.out.println("Entrez le nom et la capacité (ex: G1 60):");
		String[] ligne = scan.nextLine().trim().split("\\s+");
		
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

		System.out.println("Entrez le nom et type de consommation ex: M1 (BASSE/NORMAL/FORTE) :");
		String[] ligne = scan.nextLine().trim().split("\\s+");

		// on gere les erreurs 
		try {
			String nomMaison = ligne[0];
			TypeConsommation type =  TypeConsommation.valueOf(ligne[1].toUpperCase());
			
			reseau.ajouterMaison(nomMaison, type);
		}catch (IllegalArgumentException e) {
			System.out.println("=> ERREUR : Type de consommation invalid. Utilisez BASSE, NORMALE ou FORTE");
		}catch (ArrayIndexOutOfBoundsException e){
			System.out.println("=> ERREUR : Vous devez entrer un non ET un type (ex: M1 NORMALE).");
		}
		
	}
	
	// Ajouter connexion
	private static void handleAjouterConnexion() {
		System.out.println("Donner de la maison et du générateur ex : M1 G1");
		String[] ligne = scan.nextLine().trim().split("\\s+");
		try {
			if (ligne.length < 2) throw new ArrayIndexOutOfBoundsException();
			String nom1 = ligne[0];
			String nom2 = ligne[1];
			
			// Logique pour trouver qui est qui
			if(reseau.getMaisons().containsKey(nom1) && reseau.getGenerateurs().containsKey(nom2)) {
				reseau.ajouterConnexion(nom1, nom2);
			}
			else if(reseau.getMaisons().containsKey(nom2) && reseau.getGenerateurs().containsKey(nom1)) {
				reseau.ajouterConnexion(nom2, nom1);
			}
			else {
				System.out.println("=> ERREUR : La maison ou le générateur n'existe pas.");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer une maison ET un générateur.");
		}
	}

	private static void handleSupprimerConnexion() {
		System.out.println("Entrez la maison et le générateur à déconnecter (ex: M1 G1 ou G1 M1) :");
		String[] ligne = scan.nextLine().trim().split("\\s+");

		try {
			if (ligne.length < 2) throw new ArrayIndexOutOfBoundsException();

			String nom1 = ligne[0];
			String nom2 = ligne[1];

			// Identifie qui est la maison et qui est le générateur
			String nomMaison = null;
			String nomGenerateur = null;

			if (reseau.getMaisons().containsKey(nom1))
				nomMaison = nom1;
			if (reseau.getGenerateurs().containsKey(nom1))
				nomGenerateur = nom1;
			if (reseau.getMaisons().containsKey(nom2))
				nomMaison = nom2;
			if (reseau.getGenerateurs().containsKey(nom2))
				nomGenerateur = nom2;

			// Vérifie l'existence des deux
			if (nomMaison == null || nomGenerateur == null) {
				System.out.println("=> ERREUR : La maison ou le générateur n'existe pas.");
				return;
			}

			Maison maison = reseau.getMaisons().get(nomMaison);
			Generateur generateur = reseau.getGenerateurs().get(nomGenerateur);

			// Vérifie si la connexion existe
			Generateur genActuel = reseau.getConnexions().get(maison);
			if (genActuel == null) {
				System.out.println("=> ERREUR : La maison '" + nomMaison + "' n'est connectée à aucun générateur.");
				return;
			}
			if (!genActuel.equals(generateur)) {
				System.out.println("=> ERREUR : La maison '" + nomMaison + "' est connectée à '"
						+ genActuel.getNom() + "', pas à '" + nomGenerateur + "'.");
				return;
			}

			// Supprime la connexion
			reseau.supprimerConnexion(nomMaison, nomGenerateur);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer deux noms séparés par un espace (ex: M1 G1).");
		}
	}

	private static void handleModifierConnexion() {
		try {
			// Ancienne connexion
			System.out.println("Veuillez saisir la connexion que vous souhaitez modifier (ex: M1 G1 ou G1 M1):");
			String[] ancienneSaisie = scan.nextLine().trim().split("\\s+");
			if (ancienneSaisie.length < 2) throw new ArrayIndexOutOfBoundsException();


		
			Maison maison = null;
			Generateur ancienGen = null;

			// Identifie maison et générateur dans n'importe quel ordre
			for (String nom : ancienneSaisie) {
				if (reseau.getMaisons().containsKey(nom)) maison = reseau.getMaisons().get(nom);
				if (reseau.getGenerateurs().containsKey(nom)) ancienGen = reseau.getGenerateurs().get(nom);
			}

			// Validation
			if (maison == null || ancienGen == null || !ancienGen.equals(reseau.getConnexions().get(maison))) {
				System.out.println("=> ERREUR : La connexion saisie n'existe pas ou est incorrecte.");
				return;
			}

			// Nouvelle connexion
			System.out.println("Veuillez saisir la nouvelle connexion (ex: M1 G2 ou G2 M1):");
			String[] nouvelleSaisie = scan.nextLine().trim().split("\\s+");
			if (nouvelleSaisie.length < 2)
				throw new ArrayIndexOutOfBoundsException();

			Maison maisonNouvelle = null;
			Generateur nouveauGen = null;

			// Identifie la maison et le nouveau générateur dans n'importe quel ordre
			for (String nom : nouvelleSaisie) {
				if (reseau.getMaisons().containsKey(nom)) maisonNouvelle = reseau.getMaisons().get(nom);
				if (reseau.getGenerateurs().containsKey(nom)) nouveauGen = reseau.getGenerateurs().get(nom);
			}

			// Si aucune maison est trouvée dans la nouvelle saisie, on garde la même que l'ancienne
			if (maisonNouvelle == null)
				maisonNouvelle = maison;

			// Validation du générateur
			if (nouveauGen == null) {
				System.out.println("=> ERREUR : Le nouveau générateur n'existe pas.");
				return;
			}

// Supprime l'ancienne connexion et établie la nouvelle connexion
			reseau.modifierConnexion(maison.getNom(), ancienGen.getNom(), nouveauGen.getNom());


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
	 * Gérer l'option 1 du menu 2 : Calculer le coût du réseau.
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
			System.out.println("4. Supprimer une connexion.");
			System.out.println("5. Fin.");
			System.out.println("\n============== Fin : Menu Principal =================");
			
			String choix = scan.nextLine();
			switch(choix) {
			case "1": handleAjouterGenerateur();
			break;
			case "2": handleAjouterMaison();
			break;
			case "3": handleAjouterConnexion();
			break;
			case "4": handleSupprimerConnexion();
			break;
			case "5":
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
	
	
	/* ==========================================
	 * MÉTHODES PARTIE 2 (Automatique)
	 * ========================================== */
	
	private static void lancerMenuPartie2(Reseau reseauPartie2) {
		
		boolean enCours = true; // Si enCours est false on quitte le menu.
		
		while(enCours) {
			System.out.println("\n============== Menu Principal Partie 2 =================");
			
			System.out.println("1. Résolution automatique.");
			System.out.println("2. Sauvegarder la solution actuelle.");
			System.out.println("3. Fin.");
			System.out.println("\n============== Fin : Menu Principal =================");
			
			
			int choix = 0;
			try {
				choix = scan.nextInt();
				scan.nextLine();
			}catch (InputMismatchException e) {
				System.out.println("-> ERREUR : Entré Invalide. Veuillez saisir un nombre entier valide.");
				scan.nextLine(); // Clean input invalid
				continue;
			} 
			switch(choix) {
				case 1: 
					handleResolutionAutomatique(reseauPartie2);
				break;
				case 2: 
					System.out.println("Sauvegarder...");
					handleSauvegarderReseau(reseauPartie2);
				break;
				case 3:
					enCours = false;
					System.out.println("Au revoir.");
				break;
				default : 
					System.out.println("Choix incorrect !");
					break; 
				}
			}
	}
	
	/**
	 * Cette méthode recupère le nom du fichier pour sauvegarder le dans ce fichier
	 * @param network Le réseau.
	 */
	private static void handleSauvegarderReseau(Reseau network) {
		System.out.println("Entrez le nom du fichier de sauvegarde (ex: save.txt) :");
		String nomFichier = scan.nextLine().trim();
		
		// Vérification de l'extension du fichier
		if(!nomFichier.endsWith(".txt")) {
			nomFichier += ".txt";
		}
		
		try {
			SauvegardeurReseau sauvegardeur = new SauvegardeurReseau();
			sauvegardeur.sauvegarder(network, nomFichier);
		}catch(IOException e) {
			System.out.println("=> ERREUR lors de la sauvegarde : " + e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @param network 
	 */
	private static void handleResolutionAutomatique(Reseau network) {
		System.out.println("\n--- Résolution Automatique ---");
		System.out.println("Choississez l'algorithme à utiliser :");
		System.out.println("1. Algorithme Naïf (Aléatoire - Sujet)");
		System.out.println("2. Algorithme Glouton (Greedy - Bonus)");
		System.out.print("Votre choix :");
		
		int choixAlgo = 1; // Par défaut
		try {
			String  input = scan.nextLine().trim();
			if(!input.isEmpty()) {
				choixAlgo = Integer.parseInt(input);
			}
		}catch (NumberFormatException e) {
			System.out.println("-> Saisie invalide. Utilisation de l'algorithme Naïf par défaut");
		}
		
		// --- Paramètres communs ---
		double lambda = 10.0;
		/* (TODO Si necesssaire => Optionnel : On peut demander à l'utilisateur
		 * System.out.println("Entrez la pénalité Lambda (défaut 10) : ");
		 * Si jamais on fait ça alors on doit gérer l'exception aussi
		 * try {...} ...
 		 * 
		 */
		
		// Calcul du coût AVANT optimisation
		double coutAvant = network.calculerCout(lambda);
		System.out.println("\nCoût initial du réseau : " + String.format("%.4f", coutAvant));
		System.out.println("Optimisation en cours...");
		
		long startTime = System.currentTimeMillis(); 
		
		// --- Excécution de l'algorithme choisi ---
		if(choixAlgo == 2) {
			// --- Algorithme GLOUTON (Greedy) ---
			System.out.println(">> Lancement du SolverGreedyGenerateur...");
			SolverGreedyGenerateur solver = new SolverGreedyGenerateur(network);
			solver.solve(lambda);
			
		}else {
			// --- Algorithme NAÏF (Par défaut) ---
			System.out.println(">> Lancement du SolverNaive...");
			
			// Demande de k (nombre d'itérations) pour Naif seulement.
			System.out.println("Entrez le nombre d'essaies 'k' (par défaut 10000)");
			int k = 10000;
			try {
				String inputK = scan.nextLine().trim();
				if(!inputK.isEmpty()) {
					k = Integer.parseInt(inputK);
				}
			}catch (NumberFormatException e) {
				System.out.println("-> Saisie invalide. Utilisation de k=10_000");
			}
			
			SolverNaive solver = new SolverNaive(network);
			solver.solve(lambda, k);
		}
		
		long endTime = System.currentTimeMillis();
		
		// --- Résultats ---
		double coutApres = network.calculerCout(lambda);
		
		System.out.println("\n--- RESULTATS DE L'OPTIMISATION ---");
		System.out.println("Algorithme temriné en " + (endTime - startTime) + " ms.");
		System.out.println("Coût AVANT : " + String.format("%.4f", coutAvant));
		System.out.println("Coût APRES : " + String.format("%.4f", coutApres));
		
		if(coutApres < coutAvant) {
			double gain = coutAvant - coutApres;
			double pourcentage = (gain / coutAvant) * 100;
			System.out.println("<Validé> SUCCES : Le coût a été réduit de " + String.format("%.2f", pourcentage) + "%).");
			
		} else if(coutApres == coutAvant) {
			System.out.println("<!> Aucun changement : L'algorithme n'a pas trouvé de meilleur solution ou le réseau était déjà optimal.");
			
		} else {
			// Théoriquement impossible ....
			System.out.println("X ATTENTION : Le coût a augmenté ! (Ce comportement est anormal.");
			
		}
		System.out.println("--------------------------------------------");
		
		}
	
	// --- MAIN ----
	
	public static void main(String[] args) {
		
System.out.println("Bienvenue dans le gestionnaire de réseau électrique.");
		// CAS 1 : Aucun argument -> Mode Manuel (Partie 1)
		
		if(args.length == 0) {
			System.out.println("Mode : Construction Manuelle");
			lancerMenuPrincipal();
		}
		// CAS 2 : Argument présent -> Mode Fichier (Partie 2)
		else {
			String cheminFichier = args[0];
			System.out.println("Mode : Chargement Fichier (" + cheminFichier + ")");
			
			ChargeurReseau chargeur = new ChargeurReseau();
		
						try {
							Reseau reseauPartie2 = chargeur.charger(cheminFichier);
							
							// Si le chargement réussit, on lance le menu 2
							lancerMenuPartie2(reseauPartie2);
						} catch (FileNotFoundException | IllegalArgumentException | NombreArgumentIncorrectException
								| FormatParentheseInvalideException e) {
							// Gestion propre des erreurs de chargement
							System.out.println("ERREUR FATALE lors du chargement :");
							System.out.println(e.getMessage());
							System.exit(1); // On quitte car le fichier est invalide
						} catch (Exception e) {
							System.out.println("Erreur inattendue : " + e.getMessage());
							e.printStackTrace();
						}
					
		}
		
	
		
		
		/**
		 * 
		 * 
		// Test lecture du fichier
		ChargeurReseau chargeur = new ChargeurReseau();
		try {
			// Mets le bon chemin vers ton fichier test
			chargeur.charger("/home/lecteur/eclipse-workspace/project-paa-l3-025/src/reseau_test.txt"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 */
	} // end Main

	
} // end class InterfaceTextuelle
