package up.mi.paa.pbl.algo;


public enum TypeConsommation {
	BASE(10), NORMALE(20), FORTE(40);
	
	private final int demande;
	
	private TypeConsommation(int val) {
		this.demande = val;
	}
	
	public int getDemande() {
		return this.demande;
	}
}
