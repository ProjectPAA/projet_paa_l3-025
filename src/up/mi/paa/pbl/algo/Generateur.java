package up.mi.paa.pbl.algo;

import java.util.HashMap;

public class Generateur {
	private String nom;
	private int capaciteMax;
	
	
	public Generateur(String n, int val) {
		this.nom = n;
		this.capaciteMax = val;
	}
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Generateur)) {
			return Boolean.FALSE;
		}
		else {
			return this.nom.equals(((Generateur) other).getNom()) && this.capaciteMax == ((Generateur) other).getCapaciteMAx();
		}
	}
	
	@Override
	public int hashCode() {
		return this.nom.hashCode()*this.capaciteMax;
	}
	
	public String getNom() {
		return this.nom;
	}

	public int getCapaciteMAx() {
		return this.capaciteMax;
	}
	
	public void setCapaciteMax(int val) {
		this.capaciteMax = val;
	}
	
	
	
	@Override
    public String toString() {
        return "Générateur " + nom + " (Capacité: " + this.getCapaciteMAx() + "kWh)";
    }
}

