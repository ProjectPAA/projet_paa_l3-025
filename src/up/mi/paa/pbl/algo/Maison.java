package up.mi.paa.pbl.algo;

import up.mi.paa.problem.algo.TypeConsommation;

public class Maison {
	private String nom;
	private TypeConsommation type;
	
	
	// Constructeur
	public Maison(String n, TypeConsommation t) {
		
		this.nom = n;
		this.type = t;
	}
	
	
	public String getNom() {
		return this.nom;
	}
	
	public TypeConsommation getType() {
		return this.type;
	}
	
	public void setType(TypeConsommation t) {
		this.type = t;
	}
	
	
}
