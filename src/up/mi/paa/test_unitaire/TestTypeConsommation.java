package up.mi.paa.test_unitaire;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import up.mi.paa.pbl.TypeConsommation;

class TestTypeConsommation {

    /**
     * Teste que chaque type renvoie la bonne valeur de demande (kWh).
     */
    @Test
    void testValeursDeConsommation() {
        // Vérification des valeurs fixes définies dans ton sujet
        assertEquals(10, TypeConsommation.BASSE.getDemande(), "BASSE doit valoir 10 kWh");
        assertEquals(20, TypeConsommation.NORMAL.getDemande(), "NORMAL doit valoir 20 kWh");
        assertEquals(40, TypeConsommation.FORTE.getDemande(), "FORTE doit valoir 40 kWh");
    }

    /**
     * Vérifie qu'on a bien exactement 3 types de consommation.
     * Utile pour détecter si quelqu'un rajoute un type sans prévenir.
     */
    @Test
    void testNombreDeTypes() {
        // On vérifie la longueur du tableau des valeurs
        assertEquals(3, TypeConsommation.values().length, "Il ne devrait y avoir que 3 types de consommation");
    }
}