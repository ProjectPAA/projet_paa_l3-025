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
			return this.nom.equals(((Maison) other).getNom()) && this.type.equals(((Maison) other).getTypeConsommation());
		}
	}
	
	@Override
	public int hashCode() {
		return nom.hashCode()*type.getDemande();
	}
	
	@Override
    public String toString() {
        return "Maison " + nom + " (Consommation: " + this.getTypeConsommation().name() + ", " + this.getTypeConsommation().getDemande() + "kWh)";
    }
	

}
