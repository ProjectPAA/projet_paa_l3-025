package up.mi.paa.pbl.algo;
import java.util.HashMap;

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
	
	public TypeConsommation getTypeConsommation() {
		return this.type;
	}
	
	public void setType(TypeConsommation t) {
		this.type = t;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Maison)) {
			return Boolean.FALSE;
		} else {
			return this.nom.equals(((Maison) other).getNom()); //la maison est entièrement identifié par son nom.
		}
	}
	
	@Override
	public int hashCode() {
		return this.nom.hashCode(); //la maison est entièrement identifié par son nom.
	}
	
	@Override
    public String toString() {
        return "Maison " + nom + " (Consommation: " + this.getTypeConsommation().name() + ", " + this.getTypeConsommation().getDemande() + "kWh)";
    }
	

}
