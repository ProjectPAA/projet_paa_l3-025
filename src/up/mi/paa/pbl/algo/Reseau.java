package up.mi.paa.pbl.algo;
import java.util.NoSuchElementException;

import java.util.HashMap;
import java.util.Map;
public class Reseau {
	private Map<String, Maison> maisons;
	private Map<String, Generateur> generateurs;
	private Map<String, String> connexions; // First String is for name Maison et second for Generator
	
	public Reseau() {
		this.maisons = new HashMap<>();
		this.generateurs = new HashMap<>();
		this.connexions = new HashMap<>();
	}

	public Map<String, String> getConnexions() {
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
	public void ajouterConnexion(String nomMaison, String nomGenerateur) throws NoSuchElementException {
		if(!this.maisons.containsKey(nomMaison)) {
			throw new NoSuchElementException();
		}
		if(!this.generateurs.containsKey(nomGenerateur)) {
			throw new NoSuchElementException();
		}
		// Tout est Ok
		this.connexions.put(nomMaison, nomGenerateur);
		System.out.println("Ok : Connexion " + nomMaison + " => " + nomGenerateur + "ajoutée.");
	}
	
}
