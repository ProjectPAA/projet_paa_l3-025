package up.mi.paa.test_unitaire;

import up.mi.paa.io.SauvegardeurReseau;
import up.mi.paa.pbl.Reseau;
import up.mi.paa.pbl.TypeConsommation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TestSauvegardeurReseau {

    // Variables permettant de rediriger la sortie standard (System.out) pour éviter
    // de polluer la console avec les messages de confirmation de sauvegarde.
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    /**
     * Redirection du flux de sortie avant chaque test.
     */
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * Restauration du flux de sortie standard après chaque test.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    /**
     * Vérifie que la sauvegarde d'un réseau complet (Générateurs + Maisons + Connexions)
     * produit un fichier respectant strictement le format attendu.
     * @throws IOException En cas d'erreur de lecture/écriture sur le fichier temporaire.
     */
    @Test
    void testSauvegardeReseauComplet() throws IOException {
        // 1. Préparation des données (Arrangement)
        Reseau reseau = new Reseau();
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);
        // Connexion de M1 sur G1
        reseau.ajouterConnexion("M1", "G1");

        // Création d'un fichier temporaire pour recevoir la sauvegarde
        Path tempFile = Files.createTempFile("test_sauvegarde", ".txt");
        String cheminFichier = tempFile.toString();

        // Exécution de la méthode à tester (Action)
        SauvegardeurReseau sauvegardeur = new SauvegardeurReseau();
        sauvegardeur.sauvegarder(reseau, cheminFichier);

        // Vérification du contenu du fichier (Assertion)
        List<String> lignes = Files.readAllLines(tempFile);

        // On vérifie que le fichier n'est pas vide
        assertFalse(lignes.isEmpty(), "Le fichier de sauvegarde ne doit pas être vide.");

        // On vérifie la présence et le format exact des lignes
        // donc on vérifie que la liste contient bien les lignes attendues.

        assertTrue(lignes.contains("generateur(G1,100)."),
                "Le fichier doit contenir la définition du générateur G1.");

        assertTrue(lignes.contains("maison(M1,BASSE)."),
                "Le fichier doit contenir la définition de la maison M1.");

        // Attention à l'ordre des arguments dans la connexion : connexion(GEN, MAISON)
        assertTrue(lignes.contains("connexion(G1,M1)."),
                "Le fichier doit contenir la connexion entre G1 et M1.");

        // Nettoyage : suppression du fichier temporaire
        Files.delete(tempFile);
    }

    /**
     * Vérifie le comportement de la sauvegarde avec un réseau vide.
     * Le fichier doit être créé mais ne doit contenir aucune instruction.
     */
    @Test
    void testSauvegardeReseauVide() throws IOException {
        Reseau reseau = new Reseau();
        Path tempFile = Files.createTempFile("test_sauvegarde_vide", ".txt");

        SauvegardeurReseau sauvegardeur = new SauvegardeurReseau();
        sauvegardeur.sauvegarder(reseau, tempFile.toString());

        List<String> lignes = Files.readAllLines(tempFile);

        assertTrue(lignes.isEmpty(), "Un réseau vide doit produire un fichier vide (ou sans instructions).");

        Files.delete(tempFile);
    }

    /**
     * Vérifie la gestion des exceptions d'entrées/sorties.
     * Si le chemin est invalide (dossier protégé ou inexistant), une IOException doit être levée.
     */
    @Test
    void testErreurEcriture() {
        Reseau reseau = new Reseau();
        SauvegardeurReseau sauvegardeur = new SauvegardeurReseau();

        // Tentative d'écriture dans un chemin impossible (ex: un répertoire système ou vide sur certains OS)
        // Sur Linux/Mac "/" est racine (lecture seule souvent), sur Windows "Z:/inconnu".
        // Une méthode fiable est de pointer vers un dossier qui n'existe pas.
        String cheminInvalide = "DossierInexistant/fichier.txt";

        assertThrows(IOException.class, () -> {
            sauvegardeur.sauvegarder(reseau, cheminInvalide);
        }, "Une IOException doit être levée si le chemin est inaccessible.");
    }
}