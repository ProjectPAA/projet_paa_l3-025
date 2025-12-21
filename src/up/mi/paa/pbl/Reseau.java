package up.mi.paa.pbl;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>Classe représentant une {@code Reseau} électrique sous les contraintes de la description du projet.</p>
 * 
 * <p>Dans notre approche, plusieurs {@code Reseau}s disjoints peuvent exister dans un même runtime (même si <i>nos</i> classes ne profitent pas de cette possibilité); mais les {@code Reseau}s intersectant ou les versions diverses d'un même {@code Reseau} ne sont pas permis. En conséquence, les instances de {@code Reseau} ne sont pas censées être dupliquées.</p> 
 * <p>De même, les {@link Maison}s et {@link Generateur}s sont uniques par nom par {@code Reseau}.</p>
 * 
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class Reseau {
	/**
	 * Le {@link Map} associant une {@link Maison} dans le {@code Reseau} à son nom. Il assure l'unicité par nom des {@linkplain Maison}s dans le {@code Reseau}.
	 */
	private Map<String, Maison> maisons;
	/**
	 * Le {@link Map} associant un {@link Generateur} dans le {@code Reseau} à son nom. Il assure l'unicité par nom des {@linkplain Maison}s dans le {@code Reseau}.
	 */
	private Map<String, Generateur> generateurs;
	/**
	 * Le {@link Map} qui aux {@link Maison}s (dans le {@code Reseau}) associe le {@link Generateur} (dans le {@code Reseau}) auquel elles sont connectées. Il assure que chaque {@linkplain Maison} soit liée à au plus un {@linkplain Generateur}.
	 */
	private Map<Maison, Generateur> connexions;
	/**
	 * Le {@link Map} associant aux noms des {@link Generateur}s dans le {@code Reseau} leur taux d'utilisation (demande connectée / {@link Generateur#getCapaciteMax() capacité maximale}). Étant un attribut dérivé des autres, il n'est pas nécessaire de le Sérialiser, et donc il est transient. Son mis-à-jour est géré automatiquement sur accès.
	 */
	private transient Map<String, Double> tauxUtilisation;
	/**
	 * Moyen algébrique des {@link tauxUtilisation}, mis-à-jour avec eux.
	 */
	private transient double tauxUtilisationMoyen = 0;
	/**
	 * Le {@link Reseau#hashCode()} du {@code Reseau} calculé quand les valeurs transientes (attributs dérivés) ont été mis-à-jour. Il permet de détécter quand il est nécéssaire de les recalculer (ou pas) à cause d'un changement de l'état du {@code Reseau}. 
	 */
	private transient int hashQuandTauxUtilisationCalcule;

	/**
	 * Constructeur d'un nouveau (vide) {@code Reseau}.
	 */
	public Reseau() {
		this.maisons = new HashMap<>();
		this.generateurs = new HashMap<>();
		this.connexions = new HashMap<>();
		this.tauxUtilisation = new HashMap<String, Double>();
	}

	/**
	 * Retourne le {@link Map} qui aux {@link Maison}s associe le {@link Generateur} auquel elles sont connectées.
	 * @return Le {@link Map} qui aux {@link Maison}s associe le {@link Generateur} auquel elles sont connectées.
	 */
	public Map<Maison, Generateur> getConnexions() {
		return connexions;
	}

	/**
	 * Retourne le {@link Map} associant une {@link Maison} dans le {@code Reseau} à son nom.
	 * @return Le {@link Map} associant une {@link Maison} dans le {@code Reseau} à son nom.
	 */
	public Map<String, Maison> getMaisons() {
		return maisons;
	}

	/**
	 * Retourne le {@link Map} associant un {@link Generateur} dans le {@code Reseau} à son nom.
	 * @return Le {@link Map} associant un {@link Generateur} dans le {@code Reseau} à son nom.
	 */
	public Map<String, Generateur> getGenerateurs() {
		return generateurs;
	}
	
	/**
	 * Retourne le {@link Map} associant aux noms des {@link Generateur}s dans le {@code Reseau} leur taux d'utilisation (demande connectée / {@link Generateur#getCapaciteMax() capacité maximale}).
	 * @return Le {@link Map} associant aux noms des {@link Generateur}s dans le {@code Reseau} leur taux d'utilisation (demande connectée / {@link Generateur#getCapaciteMax() capacité maximale}).
	 */
	public Map<String, Double> getTauxUtilisation(){
		this.updateTauxUtilisation();
		return tauxUtilisation;
	}

	/**
	 * <p>Ajoute un {@link Generateur} au {@code Reseau}. Si un Generateur avec le même nom existe déjà, il est mis-à-jour.</p>
	 * <p>De façon générale, il assure qu'au retour de la méthode, {@link Reseau#generateurs generateurs} contient un {@linkplain Generateur} de nom et de capacité maximale demandé.</p>
	 * 
	 * @param nom Le nom du {@link Generateur} à ajouter.
	 * @param capaciteMax La capacité maximale à ajouter.
	 */
	public void ajouterGenerateur(String nom, int capaciteMax) {
		ajouterGenerateur(nom, capaciteMax, false);
	}
	
	/**
	 * <p>Ajoute un {@link Generateur} au {@code Reseau}. Si un Generateur avec le même nom existe déjà, il est mis-à-jour.</p>
	 * <p>De façon générale, il assure qu'au retour de la méthode, {@link Reseau#generateurs generateurs} contient un {@linkplain Generateur} de nom et de capacité maximale demandé.</p>
	 * 
	 * @param nom Le nom du {@link Generateur} à ajouter.
	 * @param capaciteMax La capacité maximale à ajouter.
	 * @param noPrint S'il est {@code true} alors cette méthode n'effectue aucun print. Sinon, il en effectue.
	 */
	public void ajouterGenerateur(String nom, int capaciteMax, boolean noPrint) {
		if (this.generateurs.containsKey(nom)) {
			this.generateurs.get(nom).setCapaciteMax(capaciteMax);
			if (!noPrint) {
				System.out.println("Générateur " + this.generateurs.get(nom) + " mis à jour.");
			}
		} else {
			this.generateurs.put(nom, new Generateur(nom, capaciteMax));
			if (!noPrint){
				System.out.println("Ok : Générateur " + nom + " ajouté avec succés.");
			}
		}
	}

	/**
	 * <p>Ajoute une {@link Maison} au {@code Reseau}. Si une Maison avec le même nom existe déjà, elle est mis-à-jour.</p>
	 * <p>De façon générale, il assure qu'au retour de la méthode, {@link Reseau#maisons maisons} contient une {@linkplain Maison} de nom et de {@linkplain TypeConsommation type de consommation} demandé.</p>
	 * 
	 * @param nom Le nom de la {@link Maison} à ajouter.
	 * @param t Le {@linkplain TypeConsommation type de consommation} de la {@link Maison} à ajouter.
	 */
	public void ajouterMaison(String nom, TypeConsommation t) {
		ajouterMaison(nom, t, false);
	}
	
	/**
	 * <p>Ajoute une {@link Maison} au {@code Reseau}. Si une Maison avec le même nom existe déjà, elle est mis-à-jour.</p>
	 * <p>De façon générale, il assure qu'au retour de la méthode, {@link Reseau#maisons maisons} contient une {@linkplain Maison} de nom et de {@linkplain TypeConsommation type de consommation} demandé.</p>
	 * 
	 * @param nom Le nom de la {@link Maison} à ajouter.
	 * @param t Le {@linkplain TypeConsommation type de consommation} de la {@link Maison} à ajouter.
	 * @param noPrint S'il est {@code true} alors cette méthode n'effectue aucun print. Sinon, il en effectue.
	 */
	public void ajouterMaison(String nom, TypeConsommation t, boolean noPrint) {
		if (this.maisons.containsKey(nom)) {
			this.maisons.get(nom).setType(t);
			if (!noPrint) {
				System.out.println("Maison " + this.maisons.get(nom) + " mis à jour.");
			}
		} else {
			this.maisons.put(nom, new Maison(nom, t));
			if (!noPrint) {
				System.out.println("Ok : Maison " + nom + " ajouté avec succès.");
			}
		}
	}

	/**
	 * <p>Ajoute une connexion entre la {@linkplain Maison} et le {@linkplain Generateur} dont les noms sont passés en argument au {@code Reseau}. Si au moins un des paramètres ne correspond pas à un élément du bonne catégorie du {@code Reseau} alors cette méthode ne fait rien.</p>
	 * <p>De façon générale, cette méthode <i>fait de son mieux</i> (best-effort) pour qu'au retour la {@linkplain Maison} soit connectée au {@linkplain Generateur} demandé.</p>
	 * @param nomMaison Le nom de la {@link Maison} à connecter. (Censé être élément de {@link Reseau#maisons maisons}.)
	 * @param nomGenerateur Le nom du {@link Generateur} à connecter. (Censé être élément de {@link Reseau#generateurs generateurs}.)
	 */
	public void ajouterConnexion(String nomMaison, String nomGenerateur) {
		ajouterConnexion(nomMaison, nomGenerateur, false);
	}
	
	/**
	 * <p>Ajoute une connexion entre la {@linkplain Maison} et le {@linkplain Generateur} dont les noms sont passés en argument au {@code Reseau}. Si au moins un des paramètres ne correspond pas à un élément du bonne catégorie du {@code Reseau} alors cette méthode ne fait rien.</p>
	 * <p>De façon générale, cette méthode <i>fait de son mieux</i> (best-effort) pour qu'au retour la {@linkplain Maison} soit connectée au {@linkplain Generateur} demandé.</p>
	 * @param nomMaison Le nom de la {@link Maison} à connecter. (Censé être élément de {@link Reseau#maisons maisons}.)
	 * @param nomGenerateur Le nom du {@link Generateur} à connecter. (Censé être élément de {@link Reseau#generateurs generateurs}.)
	 * @param noPrint S'il est {@code true} alors cette méthode n'effectue aucun print. Sinon, il en effectue.
	 */
	public void ajouterConnexion(String nomMaison, String nomGenerateur, boolean noPrint) {
		Maison maison = this.maisons.get(nomMaison);
		Generateur generateur = this.generateurs.get(nomGenerateur);

		// Est ce que maison existe
		if (maison == null) {
			return;
		}
		// Est ce que generateur existe
		if (generateur == null) {
			return;
		}
		// Maison et Generateur existe donc on fait la connexion
		this.connexions.put(maison, generateur);
		if (!noPrint) {
			System.out.println("Ok : Connexion " + nomMaison + " => " + nomGenerateur + " ajoutée.");
		}
	}

	/**
	 * 
	 * Supprime une {@link Reseau#connexions connexion} entre une {@link Reseau#maisons maison} et un {@link Reseau#generateurs generateur} si elle existe. Ne fait aucun changement s'il n'existe pas de {@linkplain Reseau#connexions connexion} entre la {@linkplain Reseau#maisons maison} et le {@linkplain Reseau#generateurs generateur} dont les noms ont été spécifiés.
	 * @param nomMaison Le nom de la {@link Reseau#maisons maison} dans la {@link Reseau#connexions connexion} à supprimer.
	 * @param nomGenerateur Le nom du {@link Reseau#generateurs generateur} dans la {@link Reseau#connexions connexion} à supprimer.
	 */
	public void supprimerConnexion(String nomMaison, String nomGenerateur) {
		supprimerConnexion(nomMaison, nomGenerateur, false);
	}
	
	/**
	 * 
	 * Supprime une {@link Reseau#connexions connexion} entre une {@link Reseau#maisons maison} et un {@link Reseau#generateurs generateur} si elle existe. Ne fait aucun changement s'il n'existe pas de {@linkplain Reseau#connexions connexion} entre la {@linkplain Reseau#maisons maison} et le {@linkplain Reseau#generateurs generateur} dont les noms ont été spécifiés.
	 * @param nomMaison Le nom de la {@link Reseau#maisons maison} dans la {@link Reseau#connexions connexion} à supprimer.
	 * @param nomGenerateur Le nom du {@link Reseau#generateurs generateur} dans la {@link Reseau#connexions connexion} à supprimer.
	 * @param noPrint S'il est {@code true} alors cette méthode n'effectue aucun print. Sinon, il en effectue.
	 */
	public void supprimerConnexion(String nomMaison, String nomGenerateur, boolean noPrint) {
		Maison maison = this.maisons.get(nomMaison);
		Generateur generateur = this.generateurs.get(nomGenerateur);

		// Vérifie si maison existe
		if (maison == null) {
			if (!noPrint) {
				System.out.println("Erreur : la maison '" + nomMaison + "' n'existe pas.");
			}
			return;
		}

		// Vérifie si générateur existe
		if (generateur == null) {
			if (!noPrint) {
				System.out.println("Erreur : le générateur '" + nomGenerateur + "' n'existe pas.");
			}
			return;
		}

		// Vérifie si la maison est bien connectée à ce générateur
		Generateur genActuel = this.connexions.get(maison);
		if (genActuel == null) {
			if (!noPrint) {
				System.out.println("Erreur : la maison '" + nomMaison + "' n'est connectée à aucun générateur.");
			}
			return;
		}

		if (!genActuel.equals(generateur)) {
			if (!noPrint) {
				System.out.println("Erreur : la maison '" + nomMaison + "' est connectée à '" + genActuel.getNom() + "', pas à '" + nomGenerateur + "'.");
			}
			return;
		}

		// Suppression effective de la connexion
		this.connexions.remove(maison);
		if (!noPrint) {
			System.out.println("Connexion supprimée : " + nomMaison + " n'est plus connectée à " + nomGenerateur + ".");
		}
	}

	/**
	 * Modifie à quel {@link Reseau#generateurs generateur} une {@link Reseau#maisons maison} est connectée. N'effectue aucun changement si la {@linkplain #maisons maison} n'est pas connectée au {@linkplain #generateurs generateur} spécifié, ou si le nouveau {@linkplain #generateurs generateur} est mal-spécifié.
	 * 
	 * @param nomMaison Le nom de la {@link #maisons maison} dont la connexion est à changer.
	 * @param nomAncienGen Le nom du {@link #generateurs generateur} auquel la {@linkplain #maisons maison} est actuellement connectée.
	 * @param nomNouveauGen Le nom du {@link #generateurs generateur} auqeul on veut que la {@linkplain #maisons maison} soit connectée. 
	 */
	public void modifierConnexion(String nomMaison, String nomAncienGen, String nomNouveauGen) {
		modifierConnexion(nomMaison, nomAncienGen, nomNouveauGen, false);
	}
	
	/**
	 * Modifie à quel {@link Reseau#generateurs generateur} une {@link Reseau#maisons maison} est connectée. N'effectue aucun changement si la {@linkplain #maisons maison} n'est pas connectée au {@linkplain #generateurs generateur} spécifié, ou si le nouveau {@linkplain #generateurs generateur} est mal-spécifié.
	 * 
	 * @param nomMaison Le nom de la {@link #maisons maison} dont la connexion est à changer.
	 * @param nomAncienGen Le nom du {@link #generateurs generateur} auquel la {@linkplain #maisons maison} est actuellement connectée.
	 * @param nomNouveauGen Le nom du {@link #generateurs generateur} auqeul on veut que la {@linkplain #maisons maison} soit connectée. 
	 * @param noPrint S'il est {@code true} alors cette méthode n'effectue aucun print. Sinon, il en effectue.
	 */
	public void modifierConnexion(String nomMaison, String nomAncienGen, String nomNouveauGen, boolean noPrint) {

		Maison maison = this.maisons.get(nomMaison);
		Generateur ancienGen = this.generateurs.get(nomAncienGen);
		Generateur nouveauGen = this.generateurs.get(nomNouveauGen);

		// return c'est pour arreter la methode si on rentre dans les si
		if (maison == null) {
			if (!noPrint) {
				System.out.println("Erreur: La maison '" + nomMaison + "' n'existe pas.");
			}
			return;
		}

		// Verification de l'ancien generateur
		if (ancienGen == null) {
			if (!noPrint) {
				System.out.println("Erreur: L'ancien générateur '" + nomAncienGen + "' n'existe pas.");
			}
			return;
		}

		// Verification du nouveau generateur
		if (nouveauGen == null) {
			if (!noPrint) {
				System.out.println("Erreur: Le nouveau générateur '" + nomNouveauGen + "' n'existe pas.");
			}
			return;
		}

		Generateur genActuel = this.connexions.get(maison);

		if (genActuel == null || !genActuel.equals(ancienGen)) {
			// Un message d'erreur
			String nomGenActuel;
			if (genActuel == null) {
				nomGenActuel = "rien";
			} else {
				nomGenActuel = genActuel.getNom();
			}
			
			if (!noPrint) {
				System.out.println("Erreur: La connexion '" + nomMaison + " -> " + nomAncienGen + "' n'existe pas.");
				System.out.println(" (La maison '" + nomMaison + "' est connectée à '" + nomGenActuel + "')");
			}
			return;
		}

		// On effectue la modification
		// On modifie la map par la nouvelle maison au nouveau generateur
		this.connexions.put(maison, nouveauGen);

		if (!noPrint) {
			System.out.println("Connexion modifiée: " + nomMaison + " est maintenant connectée à " + nomNouveauGen + ".");
		}
	}

	// Verifier si une ou plusieurs maisons ne sont pas connectés
	/**
	 * Décide si toutes les maisons sont connectées, en accord avec les contraintes de la description du projet.
	 * 
	 * @return {@code true} si et seulement si toutes les maisons sont connectées, {@code false} sinon.
	 */
	public boolean verifierConnexions() {
		return verifierConnexions(false);
	}
	
	/**
	 * Décide si toutes les maisons sont connectées, en accord avec les contraintes de la description du projet.
	 * 
	 * @return {@code true} si et seulement si toutes les maisons sont connectées, {@code false} sinon.
	 * @param noPrint S'il est {@code true} alors cette méthode n'effectue aucun print. Sinon, il en effectue.
	 */
	public boolean verifierConnexions(boolean noPrint) {

		for (Maison maison : this.maisons.values()) {
			// Si maison n'est pas dans la map de collecitons connexions
			if (!this.connexions.containsKey(maison)) {
				if (!noPrint) {
					System.out.println("=> Maison non connecté : " + maison.getNom());
				}
				return false; // on a trouvé un problème
			}
		}
		return true; // Aucun maison non connectée trouvée
	}

		/**
		 * Affiche le réseau sur le flux de sortie par défaut.
		 */
		public void afficherReseau() {
			System.out.println("--------- ETAT DU RESEAU -------");
			// Affichage du generateur 
			for(Generateur generateur : this.generateurs.values()) {
				System.out.println(generateur.toString());
			}
			
			for(Maison maison : this.maisons.values()) {
				System.out.println(maison.toString());
			}
			System.out.println("Affichage de connexions");
			for(Map.Entry<Maison, Generateur> entree : this.connexions.entrySet()) {
				Maison maison = entree.getKey();
				Generateur generateur = entree.getValue();
				System.out.println(maison.getNom() + " ===> " + generateur.getNom());
			}
			
			System.out.println("--------- Fin : ETAT DU RESEAU -------");
		}
		
		/**
		 * Affiche le réseau et son coûtsur le flux de sortie par défaut.
		 * @param lambda Le λ donnant la pénalisation de surcharge dans le calcul du coût.
		 */
		public void afficherReseau(double lambda) {
			System.out.println("--------- ETAT DU RESEAU -------");
			// Affichage du generateur 
			for(Generateur generateur : this.generateurs.values()) {
				System.out.println(generateur.toString());
			}
			
			for(Maison maison : this.maisons.values()) {
				System.out.println(maison.toString());
			}
			System.out.println("Affichage de connexions");
			for(Map.Entry<Maison, Generateur> entree : this.connexions.entrySet()) {
				Maison maison = entree.getKey();
				Generateur generateur = entree.getValue();
				System.out.println(maison.getNom() + " ===> " + generateur.getNom());
			}
			
			System.out.println("Son coût est : " + this.calculerCout(lambda) + " pour lambda : " + lambda);
			
			System.out.println("--------- Fin : ETAT DU RESEAU -------");
		}
		
	
	/**
	 * Met-à-jour les variables privées transients si nécessaire. Il n'y a <i>pas besoin</i> qu'un utilisateur de la classe <i>l'appelle manuellement</i>.
	 */
	public void updateTauxUtilisation() {
		if (this.hashCode() != this.hashQuandTauxUtilisationCalcule) {
			// Si le réseau a changé, on recalcule tous les taux d'utilisation et avec la meme boucle on calcul et mémoise le moyen.
			this.tauxUtilisation.clear();
			this.tauxUtilisationMoyen = 0;
			for (Generateur gen : generateurs.values()) {
				double charge = 0;
				for (Maison m : maisons.values()) {
					if (connexions.get(m)!=null && connexions.get(m).equals(gen)) {
						charge += m.getTypeConsommation().getDemande();
					}
				}
				this.tauxUtilisation.put(gen.getNom(), charge / gen.getCapaciteMax());
				this.tauxUtilisationMoyen += charge / gen.getCapaciteMax();
			}
			this.tauxUtilisationMoyen = tauxUtilisationMoyen / tauxUtilisation.size();
		}
	}
	
	/**
	 * Retourne la dispersion du {@code Reseau} dans son état actuel, en accord avec la description du projet.
	 * @return La dispersion du {@code Reseau}.
	 */
	public double disp() {
		this.updateTauxUtilisation();
		//Disp:
		double disp = 0;
		for (double util:tauxUtilisation.values()) {
			disp += Math.abs(util - tauxUtilisationMoyen);
		}
		//Disp done.
		return disp;
	}
	
	/**
	 * Retourne le surcharge du {@code Reseau} dans son état actuel, en accord avec la description du projet.
	 * @param lambda La valeur de λ à utiliser pour pénaliser la surcharge.
	 * @return Le surcharge du {@code Reseau}.
	 */
	public double surcharge(double lambda) {
		this.updateTauxUtilisation();
				// int count_surcharge = 0;
				double totalSurcharge = 0; // On fait la somme, pas un comptage
				for (double util:tauxUtilisation.values()) {
					// C'est la formule du PDF (Projet Partie 1)
					totalSurcharge += Math.max(0, util - 1.0);
					//if (util > 1.0)
						//count_surcharge++;
				}
				//return lambda*count_surcharge;
				return lambda*totalSurcharge;
	}
	
	/**
	 * Retourne le coût total du {@code Reseau} dans son état actuel, en accrod avec la description du projet.
	 * @param lambda La valeur de λ à utiliser pour pénaliser la surcharge.
	 * @return Le coût total du {@code Reseau}.
	 */
	public double calculerCout(double lambda) {
		return this.disp() + this.surcharge(lambda);
	}
	
	/**
	 * Calcul un hash représentant le {@code Reseau} en respectant la spécification dans {@link Object#hashCode() Object}. Ne garantit pas certainement la non-collision pour les {@code Reseau}s très grandes.
	 * @return Le hash représentant le {@code Reseau} en respectant la spécification dans {@link Object#hashCode() Object}.
	 */
	@Override
	public int hashCode() {
		return 7*this.maisons.hashCode() + 13*this.generateurs.hashCode() + 19*this.connexions.hashCode(); //Les multiplications font que le Reseau contenant que la Maison et le Generateur M et G n'a pas la même hash que le Reseau contenant la Maison et le Generateur G et M. (car leur hash est le hash de leur nom.)
		//TODO make sure no collisions for very large networks.
	}
	
	/**
	 * <p>Retourne {@code true} si et seulement si ce {@code Reseau} et {@code other} contiennent les mêmes {@link #generateurs} et les mêmes {@link #maisons} et si elles sont {@link #connexions connectées} aux mêmes générateurs dans les deux. Retourne {@code false} si {@code other} n'est pas une instance de {@code Reseau}.</p>
	 * <p>Cette méthode respecte la spécification dans {@link Object#equals(Object) Object}.</p>
	 * @param other L'Objet auquel comparer cette {@code Reseau}.
	 * @return {@code true} si et seulement si ce {@code Reseau} et {@code other} contiennent les mêmes {@link #generateurs} et les mêmes {@link #maisons} et si elles sont {@link #connexions connectées} aux mêmes générateurs dans les deux. Retourne {@code false} si {@code other} n'est pas une instance de {@code Reseau}.
	 */
	@Override
	public boolean equals(Object other) {	// Ce n'est pas vraiment nécessaire de redéfinir ça, mais vu qu'on est là...
		if (other == null || !(other instanceof Reseau)) {
			return Boolean.FALSE;
		}
		else {
			return maisons.equals(((Reseau) other).maisons) && generateurs.equals(((Reseau) other).generateurs) && connexions.equals(((Reseau) other).connexions);
		}
	}

	/**
	 * <p>Retourne un nouveau {@code Reseau} contenant les mêmes {@link Reseau#maisons}, les mêmes {@link Reseau#generateurs} et les mêmes {@link Reseau#connexions} entre eux qui ceci.</p>
	 * <p>En particulier, elle crée des nouveaux attributs {@linkplain Reseau#maisons}, {@linkplain Reseau#generateurs} et {@linkplain Reseau#connexions} contenant les mêmes (non pas de copies) des clés et valeurs.</p>
	 * <p>Cette méthode ne permet pas de construire des {@code Reseau}x différents dans la demande des maisons ou la capacité des générateurs. Tout changement sur un sera refleté sur toutes ses copies.</p>
	 * @return un nouveau {@code Reseau} contenant les mêmes {@link Reseau#maisons}, les mêmes {@link Reseau#generateurs} et les mêmes {@link Reseau#connexions} entre eux qui ceci.
	 */
	@Override
	public Reseau clone() {
		Reseau cloneOfThis = new Reseau();
		/* On ne clone pas toutes les maisons et tous les générateurs individuellement. On se permet ça, car notre clone sert qu'à permettre l'approche portfolio en parallèle sur notre Reseau.
		 * S'il y avait raison qu'un algorithme change les propriétés d'une maison ou un générateur, alors ça ne marcherait plus.*/
		cloneOfThis.getMaisons().putAll(this.maisons);
		cloneOfThis.getGenerateurs().putAll(this.generateurs);
		cloneOfThis.getConnexions().putAll(this.connexions);
		return cloneOfThis;
	}

}
