package up.mi.paa.pbl.algo;

import java.util.HashMap;

public class Generateur {
	private String nom;
	private static HashMap<Generateur, Integer> mapGenerateursCapacites = new HashMap<Generateur, Integer>();
	
	public Generateur(String nom, int capacite) {
		this.nom = nom;
		Generateur.mapGenerateursCapacites.put(this, capacite);	//mettre à jour la valeur dans le HashMap ou ajoute le paire le cas échéant.
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Generateur)) {
			return Boolean.FALSE;
		}
		else {
			return this.nom.equals(((Generateur) other).getNom());
		}
	}
	
	@Override
	public int hashCode() {
		return this.nom.hashCode();
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public int getCapacite() {
		return Generateur.mapGenerateursCapacites.get(this);
	}
}
