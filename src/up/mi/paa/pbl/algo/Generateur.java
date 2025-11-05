package up.mi.paa.pbl.algo;

import java.util.HashMap;

<<<<<<< HEAD
	private String nom;
	private int capaciteMax;
	
	
	public Generateur(String n, int val) {
		this.nom = n;
		this.capaciteMax = val;
=======
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
>>>>>>> master
	}
	
	public String getNom() {
		return this.nom;
	}
	
<<<<<<< HEAD
	public int getCapaciteMAx() {
		return this.capaciteMax;
	}
	
	public void setCapaciteMax(int val) {
		this.capaciteMax = val;
	}
	
	
}
=======
	public int getCapacite() {
		return Generateur.mapGenerateursCapacites.get(this);
	}
	
	@Override
    public String toString() {
        return "Générateur " + nom + " (Capacité: " + this.getCapacite() + "kWh)";
    }
}
>>>>>>> master
