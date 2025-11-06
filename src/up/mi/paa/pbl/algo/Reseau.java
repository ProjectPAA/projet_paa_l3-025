package up.mi.paa.pbl.algo;

import java.util.HashMap;
import java.util.Map;

// Classe Reseau
public class Reseau {
	private Map<String, Maison> maisons;
	private Map<String, Generateur> generateurs;
	private Map<Maison, Generateur> connexions;
	private transient Map<String, Double> tauxUtilisation;
	private transient double tauxUtilisationMoyen = 0;
	private transient int hashQuandTauxUtilisationCalcule;

	public Reseau() {
		this.maisons = new HashMap<>();
		this.generateurs = new HashMap<>();
		this.connexions = new HashMap<>();
		this.tauxUtilisation = new HashMap<String, Double>();
;	}

	public Map<Maison, Generateur> getConnexions() {
		return connexions;
	}

	public Map<String, Maison> getMaisons() {
		return maisons;
	}

	public Map<String, Generateur> getGenerateurs() {
		return generateurs;
	}

	// Ajouter un Generateur
	public void ajouterGenerateur(String nom, int capaciteMax) {
		if (this.generateurs.containsKey(nom)) {
			this.generateurs.get(nom).setCapaciteMax(capaciteMax);
			System.out.println("Générateur " + this.generateurs.get(nom) + " mis à jour.");
		} else {
			this.generateurs.put(nom, new Generateur(nom, capaciteMax));
			System.out.println("Ok : Générateur " + nom + " ajouté avec succés.");
		}
	}

	// Ajout de Maison
	public void ajouterMaison(String nom, TypeConsommation t) {
		if (this.maisons.containsKey(nom)) {
			this.maisons.get(nom).setType(t);
			System.out.println("Maison " + this.maisons.get(nom) + " mis à jour.");
		} else {
			this.maisons.put(nom, new Maison(nom, t));
			System.out.println("Ok : Maison " + nom + " ajouté avec succés.");
		}
	}

	// Connexion ajout
	public void ajouterConnexion(String nomMaison, String nomGenerateur) {
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
		System.out.println("Ok : Connexion " + nomMaison + " => " + nomGenerateur + " ajoutée.");
	}

	// Verifier si une ou plusieurs maisons ne sont pas connectés
	public boolean verifierConnexions() {

		for (Maison maison : this.maisons.values()) {
			// Si maison n'est pas dans la map de collecitons connexions
			if (!this.connexions.containsKey(maison)) {
				System.out.println("=> Maison non connecté : " + maison.getNom());
				return false; // on a trouvé un problème
			}
		}
		return true; // Aucun maison non connectée trouvée
	}

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
		
		// To Do to string pour reseau
	
	public void updateTauxUtilisation() {
		if (this.hashCode() != this.hashQuandTauxUtilisationCalcule) {
			// Si le réseau a changé, on recalcule tous les taux d'utilisation et avec la meme boucle on calcul et mémoise le moyen.
			this.tauxUtilisation.clear();
			this.tauxUtilisationMoyen = 0;
			for (Generateur gen : generateurs.values()) {
				double charge = 0;
				for (Maison m : maisons.values()) {
					if (connexions.get(m).equals(gen)) {
						charge += m.getTypeConsommation().getDemande();
					}
				}
				this.tauxUtilisation.put(gen.getNom(), charge / gen.getCapaciteMAx());
				this.tauxUtilisationMoyen += charge / gen.getCapaciteMAx();
			}
			this.tauxUtilisationMoyen = tauxUtilisationMoyen / tauxUtilisation.size();
		}
	}
	
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
	
	public double calculerCout(double lambda) {
		return this.disp() + this.surcharge(lambda);
	}
	
<<<<<<< HEAD
=======
	// La redefinition des ces deux fonctions ne sont pas utiles
	
	@Override
	public int hashCode() {
		return 7*this.maisons.hashCode() + 13*this.generateurs.hashCode() + 19*this.connexions.hashCode(); //Les multiplications font que le Reseau contenant que la Maison et le Generateur M et G n'a pas la même hash que le Reseau contenant la Maison et le Generateur G et M. (car leur hash est le hash de leur nom.)
		//TODO make sure no collisions for very large networks.
	}
	
	@Override
	public boolean equals(Object other) {	//C'est pas vraiment nécessaire de redéfinir ça, mais vu qu'on est là...
		if (other == null || !(other instanceof Reseau)) {
			return Boolean.FALSE;
		}
		else {
			return maisons.equals(((Reseau) other).maisons) && generateurs.equals(((Reseau) other).generateurs) && connexions.equals(((Reseau) other).connexions);
		}
	}

	// TODO to string pour reseau
>>>>>>> 3f0e5e8b5961bfe9c4ad855870d5dd577d83fdab

}
