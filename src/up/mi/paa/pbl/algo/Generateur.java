package up.mi.paa.pbl.algo;


public class Generateur implements Comparable<Generateur>{ //Note: this class has a natural ordering that is inconsistent with equals.
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
	public int hashCode() {
		return this.nom.hashCode()*this.capaciteMax;
	}
	
	@Override
    public String toString() {
        return "Générateur " + nom + " (Capacité: " + this.getCapaciteMAx() + "kWh)";
    }
	
	/**
	 * Compares based on capacity. Inconsistent with equals.
	 */
	public int compareTo(Generateur other) {
		return ((Integer) this.capaciteMax).compareTo((Integer) other.getCapaciteMAx());
	}
}

