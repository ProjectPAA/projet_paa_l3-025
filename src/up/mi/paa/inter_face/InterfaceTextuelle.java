package up.mi.paa.inter_face;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import up.mi.paa.io.ChargeurReseau;
import up.mi.paa.io.FormatParentheseInvalideException;
import up.mi.paa.io.NombreArgumentIncorrectException;
import up.mi.paa.io.SauvegardeurReseau;
import up.mi.paa.pbl.Generateur;
import up.mi.paa.pbl.Maison;
import up.mi.paa.pbl.Reseau;
import up.mi.paa.pbl.TypeConsommation;
import up.mi.paa.solvers.SolverGreedyGenerateur;
import up.mi.paa.solvers.SolverGreedyMaison;
import up.mi.paa.solvers.SolverNaive;
import up.mi.paa.solvers.Solver;
import up.mi.paa.solvers.SolverBranchBound;

/**
 * Classe principale gérant l'intégration textuelle avec l'utilisateur (CLI).
 * Cette classe permet de lancer soit le mode manuel (Partie 1), soit le mode fichier (Partie 2)
 * en fonction des arguments passés au programme.
 * Elle gère les menus, la saisie utilisateur et l'appel aux différentes fonctionnalités
 * (chargement, sauvegarde, modification du réseau, algorithmes de résolution).
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA
 * @author Zalán MOLNÁR
 */
public class InterfaceTextuelle {

	/**  Scanner unique pour lire les entrées clavier tout au long de l'exécution du programme. */
	private static Scanner scan = new Scanner(System.in);
	
	/** Instance du réseau manipulé par l'application. */
	private static Reseau reseau = new Reseau();

	/* ==========================================
	 *  MÉTHODES PARTIE 1 (Mode Manuel)
	 * ========================================== */

	/**
	 * Gère l'ajout interactif d'un générateur au réseau.
	 * Demande à l'utilisateur le nom et la capacité maximale du générateur.
	 */
	private static void handleAjouterGenerateur() {

		System.out.println("Entrez le nom et la capacité (ex: G1 60):");
		String[] ligne = scan.nextLine().trim().split("\\s+");

		try {
			String nomGenerateur = ligne[0];
			int capaciteMax = Integer.parseInt(ligne[1]);

			reseau.ajouterGenerateur(nomGenerateur, capaciteMax);
		} catch (NumberFormatException e) {
			System.out.println("=> ERREUR : La capacité doit être un nombre.");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer un nom et une capacité.");

		}

	}

	/**
	 * Gère l'ajout interactif d'une maison au réseau.
	 * Demande à l'utilisateur le nom et le type de consommation (BASSE, NORMAL, FORTE).
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
			System.out.println("=> ERREUR : Type de consommation invalide. Utilisez BASSE, NORMAL ou FORTE");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("=> ERREUR : Vous devez entrer un non ET un type (ex: M1 NORMAL).");
		}

	}

	/**
	 * Gère l'ajout interactif d'une connexion entre une maison et un générateur.
	 * L'utilisateur peut saisir les noms dans n'importe quel ordre.
	 */
	private static void handleAjouterConnexion() {
		System.out.println("Donner le nom de la maison et du générateur ex : M1 G1");
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
			System.out.println("=> ERREUR : Vous devez entrer une maison et un générateur.");
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
	 * Vérifie la validité des connexions du réseau.
	 * @return {@code true} si toutes les connexions sont validés, {@code false} sinon.
	 */
	private static boolean verifierConnexion() {
		return reseau.verifierConnexions();
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
					System.out.println("\nConstruction manuelle terminée. Passage aux outils de résolution.");
					System.out.println("Le réseau actuel : ");
					reseau.afficherReseau();
					lancerMenuPartie2(reseau, 10.0);
				} else {
					System.out.println("La (es) configuration(s) n'est (ne sont) pas correcte (s) ! Veuillez corriger.");
				}
				break;
			default:
				System.out.println("Saisie incorrecte !");
				break;

			}
		}

		System.out.println("\nVous avez quitté le menu principal.");
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
	 * @param lambda Le coefficient de pénalisation de la surcharge.
	 */
	private static void lancerMenuPartie2(Reseau reseauPartie2, double lambda) {

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
				System.out.println("-> ERREUR : Entrée Invalide. Veuillez saisir un nombre entier valide.");
				scan.nextLine(); // Clean input invalid
				continue;
			}
			switch (choix) {
			case 1:
				handleResolutionAutomatique(reseauPartie2, lambda);
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
	 * Demande à l'utilisateur le nom du fichier de destionation.
	 * @param reseauPartie2 Le réseau à sauvegarder.
	 */
	private static void handleSauvegarderReseau(Reseau reseauPartie2) {
		System.out.println("Entrez le nom du fichier de sauvegarde (ex: save.txt) :");
		String nomFichier = scan.nextLine().trim();

		// Vérification de l'extension du fichier
		if (!nomFichier.endsWith(".txt")) {
			nomFichier += ".txt";
		}

		try {
			SauvegardeurReseau sauvegardeur = new SauvegardeurReseau();
			sauvegardeur.sauvegarder(reseauPartie2, nomFichier);
		} catch (IOException e) {
			System.out.println("=> ERREUR lors de la sauvegarde : " + e.getMessage());
		}
	}

	/**
	 * Gère le menu de sélection de l'algorithme de résolution automatique.
	 * Propose différents algorithmes (Naïf, Gloutons, Branch and Bound) et exécute celui choisi par l'utilisateur.
	 * Affiche ensuite les statistiques de performance (temps, gain de coût).
	 * @param reseau Le réseau à optimiser.
	 * @param lambda Le coefficient de pénalisation de la surcharge.
	 */
	private static void handleResolutionAutomatique(Reseau reseau, double lambda) {
		
		// Calcul du coût AVANT optimisation
		double coutAvant = reseau.calculerCout(lambda);
		System.out.println("\nCoût initial du réseau : " + String.format("%.4f", coutAvant));
		System.out.println("Optimisation en cours...");

		long heureAuDebut = System.currentTimeMillis();

		//Execution des algorithmes en parallele
		//HARDCODED
		
		Reseau[] reseauClones = {reseau.clone(), reseau.clone(), reseau.clone(), reseau.clone()};
		Solver[] solvers = {new SolverNaive(reseauClones[0], lambda), new SolverGreedyGenerateur(reseauClones[1], lambda), new SolverGreedyMaison(reseauClones[2], lambda), new SolverBranchBound(reseauClones[3], lambda)};
		Thread[] solverThreads = new Thread[4];
		System.out.println("Lancement des algorithmes (veuillez attendre au plus une minute)");
		for (int i=0; i<4; i++) {
			solverThreads[i] = new Thread(solvers[i]);
			solverThreads[i].start();
		}
		//On attend au plus une minute, mais moins si tous les threads se terminent.
		boolean toutEstTermine = false;
		int i_sleep=0;
		while (i_sleep<59 && !toutEstTermine && !Thread.currentThread().isInterrupted()) {	//59 secondes pour que les threads ayent le temps de se quitter et qu'on puisse faire des comparaisons dans la minute
			toutEstTermine = true;
			for (int j=0; j<4; j++) { 		//This for could be a while, but then the counter variable would escalate in scope.
				if (solverThreads[j].getState() != Thread.State.TERMINATED) {
					toutEstTermine = false;
				}
			}
			if (!toutEstTermine) {
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
					 *The exception to this are things like Ctrl+C. 
					 */
				}
			}
		
		//Compare results
		for (Reseau reseauResolu:reseauClones) {
			if (reseauResolu.verifierConnexions() && reseauResolu.calculerCout(lambda) < reseau.calculerCout(lambda)) {
				reseau = reseauResolu;
			}
		}
		System.out.println("Résolution automatique terminée.");
		
		long heureALaFin = System.currentTimeMillis();

		// --- Résultats ---
		double coutApres = reseau.calculerCout(lambda);

		System.out.println("\n--- RESULTATS DE L'OPTIMISATION ---");
		System.out.println("Algorithme terminé en " + (heureALaFin - heureAuDebut) + " ms.");
		System.out.println("Coût AVANT : " + String.format("%.4f", coutAvant));
		System.out.println("Coût APRES : " + String.format("%.4f", coutApres));

		if (coutApres < coutAvant) {
			double gain = coutAvant - coutApres;
			double pourcentage = (gain / coutAvant) * 100;
			System.out
					.println("<Validé> SUCCES : Le coût a été réduit de " + String.format("%.2f", pourcentage) + "%).");

		} else if (coutApres == coutAvant) {
			System.out.println(
					"<!> Aucun changement : L'algorithme n'a pas trouvé de meilleure solution ou le réseau était déjà optimal.");

		} else {
			// Théoriquement impossible ....
			System.out.println("|X| -> ATTENTION : Le coût a augmenté ! (Ce comportement est anormal.");

		}
		System.out.println("--------------------------------------------");
		System.out.println("Le nouveau réseau est :");
		reseau.afficherReseau(lambda);
	}

	// --- MAIN ----

	/**
	 * Point d'entrée du programme.
	 * Analyse les arguments de la ligne de commande pour déterminer le mode de lancement.
	 * - Aucun argument : Lance le Mode Manuel (Partie 1).
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
				double lambda = 10.0; //par défaut
				if (args.length >= 2) {
					try {
						lambda = Double.parseDouble(args[1]);
					}
					catch(NumberFormatException nfe){
						System.out.println("\nERREUR : Le second paramètre (lambda) n'est pas un nombre valide. La valeur par défaut de 10.0 sera utilisée.");
						if (args[1].contains(",")){
							System.out.println("Tentez avec . au lieu de ,");
						}
					}
				}
				else {
					System.out.println("\nUn second paramètre (lambda) n'a pas été passé. La valeur par défaut de 10.0 sera utilisée.");
	
				}
				lancerMenuPartie2(reseauPartie2, lambda);
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
