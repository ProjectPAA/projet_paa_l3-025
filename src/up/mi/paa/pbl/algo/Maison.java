package up.mi.paa.pbl.algo;

import java.util.HashMap;

public class Maison {
	private String nom;
	private static HashMap<Maison, Maison_Consommation> mapMaisonsConsommations = new  HashMap<Maison, Maison_Consommation>();
	
	public Maison(String nom, Maison_Consommation consommation) {
		this.nom = nom;
		Maison.mapMaisonsConsommations.put(this, consommation);	//mettre à jour la valeur dans le HashMap ou ajoute le paire le cas échéant.
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

	public String getNom() {
		return nom;
	}
	
	public Maison_Consommation getConsommation() {
		return Maison.mapMaisonsConsommations.get(this);
		/*Comme ça même si deux instances de Maison (avec des nom égaux) sont des objets différents,
		 *		leur consommation est forcé d'être la même et de suivre les m-à-j dynamiquement.*/
	}

}

/* friendly */ enum Maison_Consommation { // Mon compilateur a rejeté le friendly exprès.
	BASSE(10), NORMALE(20), FORTE(40);
	
	private int kWh;	//Je pense à faire << un_maison.getConsommation().getkWh() >> plus tard, qui me semble assez raisonnable comme phrase.
	
	private Maison_Consommation(int kWh) {
		this.kWh = kWh;
	}
	
	public int getkWh() {
		/*Je pense à faire << un_maison.getConsommation().getkWh() >> plus tard, qui me semble assez raisonnable comme phrase. */
		return this.kWh;
	}
}
