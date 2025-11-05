package up.mi.paa.pbl.algo;

public class Generateur {

	private String nom;
	private int capaciteMax;
	
	
	public Generateur(String n, int val) {
		this.nom = n;
		this.capaciteMax = val;
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
	
	
}