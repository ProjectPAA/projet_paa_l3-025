package up.mi.paa.pbl.algo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import java.util.HashMap;
import java.util.Map;
public class Reseau {
<<<<<<< HEAD
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
	public void ajouterConnexion(String nomMaison, String nomGenerateur) {
		if(!this.maisons.containsKey(nomMaison)) {
			return;
		}
		if(!this.generateurs.containsKey(nomGenerateur)) {
			return;
		}
		// Tout est Ok
		this.connexions.put(nomMaison, nomGenerateur);
		System.out.println("Ok : Connexion " + nomMaison + " => " + nomGenerateur + "ajoutée.");
	}
	
=======
	private Collection<Maison> maisons;
	private Collection<Generateur> generateurs;
	private Collection<Connexion_MG> connexions;
	
	public Reseau() {
		this(new ArrayList<Maison>(), new ArrayList<Generateur>(), new ArrayList<Connexion_MG>());
	}
	
	public Reseau(Collection<Maison> Maisons, Collection<Generateur> Generateurs, Collection<Connexion_MG> Connexions) {
		this.maisons = Maisons;
		this.generateurs = Generateurs;
		this.connexions = Connexions;
	}
	
	/*Je suppose que les générateurs ont un namespace à eux, et ils décident s'il faut créer/mettre à jour, par encapsulation.
	 * Side effect: un même générateur *pourrait* apartenir à deux réseaux, donc deux instances du problème, mais vu que tout est saisi à clavieur ou chargé des fichier, et qu'on opère avec des mis-à-jour,
	 * 				ça se réalisera jamais de façon cohérent.*/
	public void addMaison(Maison maison) {
		this.maisons.add(maison);
	}
	
	public void addGenerateur(Generateur generateur) {
		this.generateurs.add(generateur);
	}
	
	public void addConnexion(Connexion_MG connexion) throws NoSuchElementException {
		if (!(maisons.contains(connexion.getMaison()) && generateurs.contains(connexion.getGenerateur()))){
		//si la maison ou le générateur de la connexion n'est pas dans le réseau, alors la connexion peut pas être dans le réseau non plus.
			throw new NoSuchElementException();
		}
		else {
			this.connexions.add(connexion);
		}
	}
	
	//TODO check an element being in one of the collections
	//TODO iterators over elements
>>>>>>> master
	
}
