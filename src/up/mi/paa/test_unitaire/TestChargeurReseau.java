package up.mi.paa.test_unitaire;

import up.mi.paa.io.ChargeurReseau;
import up.mi.paa.io.FormatParentheseInvalideException;
import up.mi.paa.io.NombreArgumentIncorrectException;
import up.mi.paa.pbl.Reseau;
import up.mi.paa.pbl.TypeConsommation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TestChargeurReseau {

    // Variables permettant de rediriger la sortie standard (System.out) pour éviter
    // de polluer la console avec les logs du chargeur durant les tests.
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
     * Crée un fichier temporaire contenant les lignes données et retourne son chemin absolu.
     * Le fichier sera supprimé automatiquement à la fin du test.
     */
    private String creerFichierTest(List<String> lignes) throws IOException {
        Path tempFile = Files.createTempFile("test_reseau", ".txt");
        Files.write(tempFile, lignes);
        File file = tempFile.toFile();
        file.deleteOnExit(); // Assure la suppression du fichier à la fin du programme
        return file.getAbsolutePath();
    }

    /**
     * Vérifie le chargement d'un fichier valide contenant un scénario complet.
     * Le réseau résultant doit contenir exactement les éléments décrits.
     */
    @Test
    void testChargementNominal() throws Exception {
        // Préparation d'un contenu valide
        List<String> contenu = List.of(
                "generateur(G1, 100).",
                "maison(M1, BASSE).",
                "maison(M2, FORTE).",
                "connexion(M1, G1)."
        );
        String chemin = creerFichierTest(contenu);

        ChargeurReseau chargeur = new ChargeurReseau();
        Reseau reseau = chargeur.charger(chemin);

        // Vérifications
        assertNotNull(reseau, "Le réseau chargé ne doit pas être null.");
        assertEquals(1, reseau.getGenerateurs().size(), "Il doit y avoir 1 générateur.");
        assertEquals(2, reseau.getMaisons().size(), "Il doit y avoir 2 maisons.");
        assertEquals(1, reseau.getConnexions().size(), "Il doit y avoir 1 connexion.");

        // Vérification du contenu spécifique
        assertTrue(reseau.getGenerateurs().containsKey("G1"));
        assertEquals(100, reseau.getGenerateurs().get("G1").getCapaciteMAx());
        assertEquals(TypeConsommation.BASSE, reseau.getMaisons().get("M1").getTypeConsommation());
    }

    /**
     * Vérifie que le chargeur lève une exception appropriée si le fichier n'existe pas.
     */
    @Test
    void testFichierInexistant() {
        ChargeurReseau chargeur = new ChargeurReseau();
        assertThrows(FileNotFoundException.class, () -> {
            chargeur.charger("chemin/vers/fichier/imaginaire.txt");
        }, "Une FileNotFoundException doit être levée si le fichier est introuvable.");
    }

    /**
     * Vérifie la détection d'une instruction inconnue dans le fichier.
     * Exemple : "centrale(C1, 1000)." au lieu de "generateur".
     */
    @Test
    void testInstructionInconnue() throws IOException {
        String chemin = creerFichierTest(List.of("trucInconnu(A, B)."));
        ChargeurReseau chargeur = new ChargeurReseau();

        assertThrows(IllegalArgumentException.class, () -> {
            chargeur.charger(chemin);
        }, "Une instruction inconnue doit provoquer une IllegalArgumentException.");
    }

    /**
     * Vérifie la validation syntaxique des parenthèses.
     * Une ligne mal formée (parenthèses manquantes ou inversées) doit être rejetée.
     */
    @Test
    void testFormatParentheseInvalide() throws IOException {
        // Cas : parenthèse fermante manquante
        String chemin = creerFichierTest(List.of("generateur(G1, 100."));
        ChargeurReseau chargeur = new ChargeurReseau();

        assertThrows(FormatParentheseInvalideException.class, () -> {
            chargeur.charger(chemin);
        }, "Une erreur de parenthèses doit lever FormatParentheseInvalideException.");
    }

    /**
     * Vérifie la validation du nombre d'arguments.
     * Exemple : un générateur défini sans sa puissance.
     */
    @Test
    void testNombreArgumentsIncorrect() throws IOException {
        // Cas : "generateur(G1)." -> Manque la puissance
        String chemin = creerFichierTest(List.of("generateur(G1)."));
        ChargeurReseau chargeur = new ChargeurReseau();

        assertThrows(NombreArgumentIncorrectException.class, () -> {
            chargeur.charger(chemin);
        }, "Un nombre d'arguments incorrect doit lever NombreArgumentIncorrectException.");
    }

    /**
     * Vérifie la robustesse lors d'une tentative de connexion avec des éléments inexistants.
     * Le chargeur doit gérer le cas qui ne crashe pas ni ne créer de connexion invalide.
     */
    @Test
    void testConnexionEntiteInexistante() throws Exception {
        // On définit une maison mais on essaie de la connecter à un générateur inconnu
        String chemin = creerFichierTest(List.of(
                "maison(M1, BASSE).",
                "connexion(M1, G_Fantome)."
        ));

        ChargeurReseau chargeur = new ChargeurReseau();
        Reseau reseau = chargeur.charger(chemin);

        // La maison M1 existe, mais la connexion ne doit pas avoir été créée
        assertTrue(reseau.getMaisons().containsKey("M1"));
        assertTrue(reseau.getConnexions().isEmpty(), "Aucune connexion ne doit être créée si le générateur n'existe pas.");
    }
}