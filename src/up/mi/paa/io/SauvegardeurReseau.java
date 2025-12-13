package up.mi.paa.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.io.FileWriter;

import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.Reseau;

public class SauvegardeurReseau {
	
	/**
	 * Sauvegarde l'état actuel du réseai dans un fichier texte. Le format :
	 * Générateurs -> Maisons -> Connexions. * @param réseau à sauvegarder
	 * 
	 * @param cheminFichier Le chemin où cérer le fichier
	 * @throws IOException En cas de problème d'écriture
	 */
	public void sauvegarder(Reseau reseau, String cheminFichier) throws IOException {

		System.out.println("Sauvegarde en cours vers : " + cheminFichier);

		// On utilise PrintWriter qui est le plus simple pour écrire du texte
		try (PrintWriter writer = new PrintWriter(new FileWriter(cheminFichier))) {

			// Ecriture des générateurs dans le fichier
			for (Generateur gen : reseau.getGenerateurs().values()) {
				writer.println("generateur(" + gen.getNom() + "," + gen.getCapaciteMAx() + ").");
			}

			// Ecriture des Maisons dans le fichier
			for (Maison maison : reseau.getMaisons().values()) {
				writer.println("maison(" + maison.getNom() + "," + maison.getTypeConsommation().name() + ").");
			}

			// Ecriture des conexions entre générateurs et maisons dans le fichier
			for (Map.Entry<Maison, Generateur> entree : reseau.getConnexions().entrySet()) {
				Maison maison = entree.getKey();
				Generateur generateur = entree.getValue();
				writer.println("connexion(" + generateur.getNom() + "," + maison.getNom() + ").");
			}

			System.out.println("Sauvegarde terminée avec succèes !");
		}
	}

}
