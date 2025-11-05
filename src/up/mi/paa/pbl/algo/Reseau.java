package up.mi.paa.pbl.algo;
import java.util.HashMap;
import java.util.Map;

// Classe Reseau
public class Reseau {
	private Map<String, Maison> maisons;
	private Map<String, Generateur> generateurs;
	private Map<Maison, Generateur> connexions; 
	
	public Reseau() {
		this.maisons = new HashMap<>();
		this.generateurs = new HashMap<>();
		this.connexions = new HashMap<>();
	}

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
		if(this.generateurs.containsKey(nom)) {
			this.generateurs.get(nom).setCapaciteMax(capaciteMax);
			System.out.println("Générateur " + this.generateurs.get(nom) + " mis à jour.");
		}else {
			this.generateurs.put(nom, new Generateur(nom, capaciteMax));
			System.out.println("Ok : Générateur " + nom + " ajouté avec succés.");
		}
	}
	
	// Ajout de Maison
	public void ajouterMaison(String nom, TypeConsommation t) {
		if(this.maisons.containsKey(nom)) {
			this.maisons.get(nom).setType(t);
			System.out.println("Maison " + this.maisons.get(nom) + " mis à jour.");
		}else {
			this.maisons.put(nom, new Maison(nom, t));
			System.out.println("Ok : Maison " + nom + " ajouté avec succés.");
		}
	}
	
	// Connexion ajout
	public void ajouterConnexion(String nomMaison, String nomGenerateur) {
		Maison maison = this.maisons.get(nomMaison);
		Generateur generateur = this.generateurs.get(nomGenerateur);
		
		// Est ce que maison existe
		if(maison == null) {
			return;
		}
		//Est ce que generateur existe
		if(generateur == null) {
			return;
		}
		// Maison et Generateur existe donc on fait la connexion
		this.connexions.put(maison, generateur);
		System.out.println("Ok : Connexion " + nomMaison + " => " + nomGenerateur + "ajoutée.");
	}
	
	// Verifier si une ou plusieurs maisons ne sont pas connectés
		public boolean verifierConnexions() {			
			
			for(Maison maison : this.maisons.values()) {
				// Si maison n'est pas dans la map de collecitons connexions
				if(!this.connexions.containsKey(maison)) {
					System.out.println("=> Maison non connecté : " + maison.getNom());
					return false; // on a trouvé un problème
				}
			}
			return true; // Aucun maison non connectée trouvée
			
		}

	
}
