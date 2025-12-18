package up.mi.paa.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import up.mi.paa.pbl.Generateur;
import up.mi.paa.pbl.Maison;
import up.mi.paa.pbl.Reseau;

import java.io.FileWriter;

/**
 * Classe responsable de l'exportation (sauvegarde)d'un réseau électrique vers un fichier texte.
 * Le fichier génére resepcte strictement le format imposé par le sujet(PArtie 2) pour pouvoir être rechargé ultérieurement par le ChargeurReseau.
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class SauvegardeurReseau {
	
	/**
	 * Sauvegarde l'état actuel du réseau dans un fichier texte.
	 * <p>
	 * L'écriture respecte l'ordre imposé :
	 * </p>
	 * <ol>
	 * <li>Les génératuers : <code>generateur(nom,capacite).</code></li>
	 * <li>Les maisons : <code>maison(nom, TYPE).</code></li>
	 * <li>Les connexions : <code>connexion(gen,maison).</code></li>
	 * </ol>
	 * * @param réseau L'objet Reseau contenant les données à exporter.
	 * @param cheminFichier Le chemin absolu ou relatif du fichier à créer (ex : "sauvegarde.txt").
	 * @throws IOException En cas de problème d'accès au disque ou d'écriture.
	 */
	public void sauvegarder(Reseau reseau, String cheminFichier) throws IOException {

		System.out.println("Sauvegarde en cours vers : " + cheminFichier);

		// On utilise PrintWriter qui est le plus simple pour écrire du texte ligne par ligne
		try (PrintWriter writer = new PrintWriter(new FileWriter(cheminFichier))) {

			// Ecriture des générateurs dans le fichier
			for (Generateur gen : reseau.getGenerateurs().values()) {
				writer.println("generateur(" + gen.getNom() + "," + gen.getCapaciteMax() + ").");
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

			System.out.println("Sauvegarde terminée avec succès !");
		}
	}

}
