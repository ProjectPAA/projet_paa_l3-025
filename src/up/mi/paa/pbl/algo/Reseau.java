package up.mi.paa.pbl.algo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class Reseau {
	private Collection<Maison> maisons;
	private Collection<Generateur> generateurs;
	private Collection<Connexion_MG> connexions;
	
	public Reseau() {
		this(new ArrayList<Maison>(), new ArrayList<Generateur>(), new ArrayList<Connexion_MG>());
	}
	
	public Reseau(Collection<Maison> Maisons, Collection<Generateur> Generateurs, Collection<Connexion_MG> Connexions) {
		this.maisons = Maisons;
		this.generateurs = Generateurs;
		this.connexions = Connexions;
	}
	
	/*Je suppose que les générateurs ont un namespace à eux, et ils décident s'il faut créer/mettre à jour, par encapsulation.
	 * Side effect: un même générateur *pourrait* apartenir à deux réseaux, donc deux instances du problème, mais vu que tout est saisi à clavieur ou chargé des fichier, et qu'on opère avec des mis-à-jour,
	 * 				ça se réalisera jamais de façon cohérent.*/
	public void addMaison(Maison maison) {
		this.maisons.add(maison);
	}
	
	public void addGenerateur(Generateur generateur) {
		this.generateurs.add(generateur);
	}
	
	public void addConnexion(Connexion_MG connexion) throws NoSuchElementException {
		if (!(maisons.contains(connexion.getMaison()) && generateurs.contains(connexion.getGenerateur()))){	//TODO CRITICAL check name-coherence with Connexion_MG
		//si la maison ou le générateur de la connexion n'est pas dans le réseau, alors la connexion peut pas être dans le réseau non plus.
			throw new NoSuchElementException();
		}
		else {
			this.connexions.add(connexion);
		}
	}
	
	//TODO check an element being in one of the collections
	//TODO iterators over elements
	
}
