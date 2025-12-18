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
import up.mi.paa.solvers.SolverGreedyMaison;
import up.mi.paa.solvers.SolverNaive;
import up.mi.paa.solvers.Solver;
import up.mi.paa.solvers.SolverBranchBound;

/**
 * Classe principale gérant l'intégration textuelle avec l'utilisateur (CLI).
 * Cette classe permet de lancer soit le mode manuel (Partie 1), soit le mode fichier (Partie 2)
 * en fonction des arguments passés au programme.
 * * Elle gère les menus, la saisie utilisateur et l'appel aux diférentes focntionnalité
 * (chargement, sauvegarde, modification du réseau, algorithmes de résolution).
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class InterfaceTextuelle {

	/**  Scanner unique pour lire les entrées clavier tout au long de l'excécution du programme. */
	private static Scanner scan = new Scanner(System.in);
	
	/** Instance du réseau manipulé par l'application. */
	private static Reseau reseau = new Reseau();

	/* ==========================================
	 *  MÉTHODES PARTIE 1 (Mode Manuel)
	 * ========================================== */

	/**
	 * Gère l'ajout interactif d'un générateur au réseau.
	 * Demande à l'utiliateur le nom et la capacité maximale du générateur.
	 */
	private static void handleAjouterGenerateur() {

		System.out.println("Entrez le nom et la capacité (ex: G1 60):");
		String[] ligne = scan.nextLine().trim().split("\\s+");

		try {
			String nomGenerateur = ligne[0];
			int capaciteMax = Integer.parseInt(ligne[1]);

			reseau.ajouterGenerateur(nomGenerateur, capaciteMax);
		} catch (NumberFormatException e) {
			System.out.println("=> ERREUR : La capacité doit être un nombre");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer un nom ET une capacité.");

		}

	}

	/**
	 * Gère l'ajout interactif d'une maison au réseau.
	 * Demande à l'utiliateur le nom et le type de consommation (BASSE, NORMAL, FORTE).
	 */
	private static void handleAjouterMaison() {

		System.out.println("Entrez le nom et type de consommation ex: M1 (BASSE/NORMAL/FORTE) :");
		String[] ligne = scan.nextLine().trim().split("\\s+");

		// on gere les erreurs
		try {
			String nomMaison = ligne[0];
			TypeConsommation type = TypeConsommation.valueOf(ligne[1].toUpperCase());

			reseau.ajouterMaison(nomMaison, type);
		} catch (IllegalArgumentException e) {
			System.out.println("=> ERREUR : Type de consommation invalid. Utilisez BASSE, NORMALE ou FORTE");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer un non ET un type (ex: M1 NORMALE).");
		}

	}

	/**
	 * Gère l'ajout interactif d'une connexion entre une maison et un générateur.
	 * L'utlisateur peut saisir les noms dans n'importe quel ordre.
	 */
	private static void handleAjouterConnexion() {
		System.out.println("Donner de la maison et du générateur ex : M1 G1");
		String[] ligne = scan.nextLine().trim().split("\\s+");
		try {
			if (ligne.length < 2)
				throw new ArrayIndexOutOfBoundsException();
			String nom1 = ligne[0];
			String nom2 = ligne[1];

			// Logique pour trouver qui est qui
			if (reseau.getMaisons().containsKey(nom1) && reseau.getGenerateurs().containsKey(nom2)) {
				reseau.ajouterConnexion(nom1, nom2);
			} else if (reseau.getMaisons().containsKey(nom2) && reseau.getGenerateurs().containsKey(nom1)) {
				reseau.ajouterConnexion(nom2, nom1);
			} else {
				System.out.println("=> ERREUR : La maison ou le générateur n'existe pas.");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer une maison ET un générateur.");
		}
	}

	/**
	 * Gère la suppression interactive d'une connexion existante.
	 * Vérifie que la connexion existe bien avant de la supprimer.
	 */
	private static void handleSupprimerConnexion() {
		System.out.println("Entrez la maison et le générateur à déconnecter (ex: M1 G1 ou G1 M1) :");
		String[] ligne = scan.nextLine().trim().split("\\s+");

		try {
			if (ligne.length < 2)
				throw new ArrayIndexOutOfBoundsException();

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
				System.out.println("=> ERREUR : La maison '" + nomMaison + "' est connectée à '" + genActuel.getNom()
						+ "', pas à '" + nomGenerateur + "'.");
				return;
			}

			// Supprime la connexion
			reseau.supprimerConnexion(nomMaison, nomGenerateur);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer deux noms séparés par un espace (ex: M1 G1).");
		}
	}

	/**
	 * Gère la modification interactive d'une connexion.
	 * Permet de changer le générateur auquel une maison est connectée.
	 */
	private static void handleModifierConnexion() {
		try {
			// Ancienne connexion
			System.out.println("Veuillez saisir la connexion que vous souhaitez modifier (ex: M1 G1 ou G1 M1):");
			String[] ancienneSaisie = scan.nextLine().trim().split("\\s+");
			if (ancienneSaisie.length < 2)
				throw new ArrayIndexOutOfBoundsException();

			Maison maison = null;
			Generateur ancienGen = null;

			// Identifie maison et générateur dans n'importe quel ordre
			for (String nom : ancienneSaisie) {
				if (reseau.getMaisons().containsKey(nom))
					maison = reseau.getMaisons().get(nom);
				if (reseau.getGenerateurs().containsKey(nom))
					ancienGen = reseau.getGenerateurs().get(nom);
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
				if (reseau.getMaisons().containsKey(nom))
					maisonNouvelle = reseau.getMaisons().get(nom);
				if (reseau.getGenerateurs().containsKey(nom))
					nouveauGen = reseau.getGenerateurs().get(nom);
			}

			// Si aucune maison est trouvée dans la nouvelle saisie, on garde la même que
			// l'ancienne
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

	/**
	 * Vérifie la validité des connexions du réseau.
	 * @return true si toutes les connexions sont validés, false sinon.
	 */
	private static boolean verifierConnexion() {
		return reseau.verifierConnexions();
	}

	/**
	 * Affiche l'état du réseau dans la console (Générateurs, Maisons et Connexions).
	 */
	private static void handleAfficherReseau() {
		reseau.afficherReseau();
	}

	/**
	 * Calcule affiche les coûts du réseau (Dispersion, Surcharge et Coût Total).
	 * Utilise une valeur de pénalité lambda fixée à 10.0.
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

	/**
	 * Lance le menu secondaire du Mode Manuel (Partie 1).
	 * Permet de calculer le coût, modifier des connexions ou afficher le réseau une fois la construction terminée.
	 */
	public static void lancerMenuSecondaire() {
		boolean enCours = true;
		while (enCours) {
			System.out.println("============== Menu Secondaire =================");
			System.out.println("1. Calculer.");
			System.out.println("2. Modifier.");
			System.out.println("3. Afficher le réseau.");
			System.out.println("4. Fin.");
			System.out.println("============== Fin : Menu Secondaire =================");

			String choix = scan.nextLine();
			switch (choix) {
			case "1":
				handleCalculerCout();
				break;
			case "2":
				handleModifierConnexion();
				break;
			case "3":
				handleAfficherReseau();
				;
				break;
			case "4":
				enCours = false;
				break;
			default:
				System.out.println("Saisir incorrect !");
				break;

			}
		}
		System.out.println("\nVous avez quitter le menu secondaire.");
	}

	/**
	 * Lance le menu principal du Mode Manuel (Partie 1).
	 * Permet la construction pas à pas du réseau (ajout d'éléments).
	 */
	private static void lancerMenuPrincipal() {
		boolean enCours = true;
		while (enCours) {
			System.out.println("\n============== Menu Principal =================");

			System.out.println("1. Ajouter un générateur.");
			System.out.println("2. Ajouter une maison.");
			System.out.println("3. Ajouter une connexion.");
			System.out.println("4. Supprimer une connexion.");
			System.out.println("5. Fin.");
			System.out.println("\n============== Fin : Menu Principal =================");

			String choix = scan.nextLine();
			switch (choix) {
			case "1":
				handleAjouterGenerateur();
				break;
			case "2":
				handleAjouterMaison();
				break;
			case "3":
				handleAjouterConnexion();
				break;
			case "4":
				handleSupprimerConnexion();
				break;
			case "5":
				if (verifierConnexion()) {
					enCours = false;
					lancerMenuSecondaire(); // Menu Secondaire
				} else {
					System.out.println("La (es) configuration(s) n'est (ne sont) pas correct ! Veuillez corriger.");
				}
				break;
			default:
				System.out.println("Saisir incorrect !");
				break;

			}
		}

		System.out.println("\nVous avez quitter le menu principal.");
	}

	/*
	 * ========================================== 
	 * MÉTHODES PARTIE 2 (Automatique)
	 * ==========================================
	 */
	/**
	 * Lance le menu principal du Mode Fichier (Partie 2).
	 * Permet la résolution automatique et la sauvegarde d'un réseau chargé depuis un fichier.
	 * @param reseauPartie2 Le réseau chargé depuis le fichier.
	 */
	private static void lancerMenuPartie2(Reseau reseauPartie2) {

		boolean enCours = true; // Si enCours est false on quitte le menu.

		while (enCours) {
			System.out.println("\n============== Menu Principal Partie 2 =================");

			System.out.println("1. Résolution automatique.");
			System.out.println("2. Sauvegarder la solution actuelle.");
			System.out.println("3. Fin.");
			System.out.println("\n============== Fin : Menu Principal =================");

			int choix = 0;
			try {
				choix = scan.nextInt();
				scan.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("-> ERREUR : Entré Invalide. Veuillez saisir un nombre entier valide.");
				scan.nextLine(); // Clean input invalid
				continue;
			}
			switch (choix) {
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
			default:
				System.out.println("Choix incorrect !");
				break;
			}
		}
	}

	/**
	 * Gère la sauvegarde du réseau actuel dans un fichier texte.
	 * Demande à l'utilisateur le nom  du fichier de destionation.
	 * @param network Le réseau à sauvegarder.
	 */
	private static void handleSauvegarderReseau(Reseau network) {
		System.out.println("Entrez le nom du fichier de sauvegarde (ex: save.txt) :");
		String nomFichier = scan.nextLine().trim();

		// Vérification de l'extension du fichier
		if (!nomFichier.endsWith(".txt")) {
			nomFichier += ".txt";
		}

		try {
			SauvegardeurReseau sauvegardeur = new SauvegardeurReseau();
			sauvegardeur.sauvegarder(network, nomFichier);
		} catch (IOException e) {
			System.out.println("=> ERREUR lors de la sauvegarde : " + e.getMessage());
		}
	}

	/**
	 * Gère le menu de sélection de l'algorithme de résolution automatique.
	 * Propose différents algorithmes (Naïf, Gloutons, Branch and Bound) et excécution celui choisi par l'utilisateur.
	 * Affiche ensuite les statistiques de performance (temps, gain, de coût).
	 * @param network Le réseau à optimiser.
	 */
	private static void handleResolutionAutomatique(Reseau network) {
		// --- Paramètres communs ---
		double lambda = 10.0;
		/*
		 * (TODO Si necesssaire => Optionnel : On peut demander à l'utilisateur
		 * System.out.println("Entrez la pénalité Lambda (défaut 10) : "); Si jamais on
		 * fait ça alors on doit gérer l'exception aussi try {...} ...
		 * 
		 */
		
		// Calcul du coût AVANT optimisation
		double coutAvant = network.calculerCout(lambda);
		System.out.println("\nCoût initial du réseau : " + String.format("%.4f", coutAvant));
		System.out.println("Optimisation en cours...");

		long startTime = System.currentTimeMillis();

		//Execution des algorithmes en parallele
		//HARDCODED
		
		Reseau[] networks = {network.clone(), network.clone(), network.clone(), network.clone()};
		Solver[] solvers = {new SolverNaive(networks[0], lambda), new SolverGreedyGenerateur(networks[1], lambda), new SolverGreedyMaison(networks[2], lambda), new SolverBranchBound(networks[3], lambda)};
		Thread[] solverThreads = new Thread[4];
		System.out.println("Lancement des algorithmes (veuillez attendre au plus une minute)");
		for (int i=0; i<4; i++) {
			solverThreads[i] = new Thread(solvers[i]);
			solverThreads[i].run();
		}
		//On attend au plus une minute, mais moins si tous les threads se terminent.
		boolean allDone = false;
		int i_sleep=0;
		while (i_sleep<59 && !allDone && !Thread.currentThread().isInterrupted()) {	//59 secondes pour que les threads ayent le temps de se quitter et qu'on puisse faire des comparaisons dans la minute
			allDone = true;
			for (int j=0; j<4; j++) { 		//This for could be a while, but then the counter variable would escalate in scope.
				if (solverThreads[j].getState() != Thread.State.TERMINATED) {
					allDone = false;
				}
			}
			if (!allDone) {
				System.out.println("Encore en cours. " + (i_sleep+1) + " d'au plus 60 secondes passées.");
				try{Thread.sleep(1000);}
				catch (InterruptedException ie) {
					Thread.currentThread().interrupt();	//thrower may clear Interrupted status.
					//Setting this breaks from the for. This should never execute, because of how the Thread hierarchy is laid out.
					
				}
			}
			i_sleep++;
		}
		// Either we left after working the time allotted, or we were interrupted. In any case, we should interrupt child threads.
			for (int j=0; j<4; j++) {
				solverThreads[j].interrupt();
			}
			for (int j=0; j<4; j++) {
				try{solverThreads[j].join();}
				catch(InterruptedException ie) {
					//if we are interrupted while recovering an exiting thread
					j--; //We'll try again. We cannot leave threads executing when we go to compare costs, as Reseau is not thread-safe.
					Thread.interrupted(); //clears currentThread Interrupted status.
					/*Our Thread hierarchy means that no other thread should be interrupting us, and thus this shouldn't cause an infinite loop.
					 *The exception to this are things like Ctrl+C. TODO
					 *TODO Test this*/
				}
			}
		
		//Compare results
		for (Reseau solvedNetwork:networks) {
			if (solvedNetwork.verifierConnexions() && solvedNetwork.calculerCout(lambda) < network.calculerCout(lambda)) {
				network = solvedNetwork;
			}
		}
		System.out.println("Résolution automatique terminée.");
		
		long endTime = System.currentTimeMillis();

		// --- Résultats ---
		double coutApres = network.calculerCout(lambda);

		System.out.println("\n--- RESULTATS DE L'OPTIMISATION ---");
		System.out.println("Algorithme temriné en " + (endTime - startTime) + " ms.");
		System.out.println("Coût AVANT : " + String.format("%.4f", coutAvant));
		System.out.println("Coût APRES : " + String.format("%.4f", coutApres));

		if (coutApres < coutAvant) {
			double gain = coutAvant - coutApres;
			double pourcentage = (gain / coutAvant) * 100;
			System.out
					.println("<Validé> SUCCES : Le coût a été réduit de " + String.format("%.2f", pourcentage) + "%).");

		} else if (coutApres == coutAvant) {
			System.out.println(
					"<!> Aucun changement : L'algorithme n'a pas trouvé de meilleur solution ou le réseau était déjà optimal.");

		} else {
			// Théoriquement impossible ....
			System.out.println("|X| -> ATTENTION : Le coût a augmenté ! (Ce comportement est anormal.");

		}
		System.out.println("--------------------------------------------");

	}

	// --- MAIN ----

	/**
	 * Point d'entrée du programme.
	 * Analyse les arguments de la ligne de commande pour déterminer le mode de lancement.
	 * - Aucun argument : Lance le Mode Mannuel (Partie 1).
	 * - Un argument (chemin du fichier) : Lance le Mode Fichier (Partie 2).
	 * @param args Arguments passés en ligne de commande.
	 */
	public static void main(String[] args) {

		System.out.println("Bienvenue dans le gestionnaire de réseau électrique.");
		// CAS 1 : Aucun argument -> Mode Manuel (Partie 1)

		if (args.length == 0) {
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
	} // end Main

} // end class InterfaceTextuelle
