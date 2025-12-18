package up.mi.paa.test_unitaire;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import up.mi.paa.pbl.Maison;
import up.mi.paa.pbl.TypeConsommation;

class TestMaison {

    /**
     * Teste le constructeur et les accesseurs (Getters).
     * Vérifie que la maison est bien créée avec le bon nom et le bon type.
     */
    @Test
    void testInitialisation() {
        Maison m = new Maison("Villa Sud", TypeConsommation.BASSE);

        assertEquals("Villa Sud", m.getNom(), "Le nom de la maison est incorrect");
        assertEquals(TypeConsommation.BASSE, m.getTypeConsommation(), "Le type de consommation est incorrect");
    }

    /**
     * Teste le Setter (changement de type de consommation).
     */
    @Test
    void testModificationType() {
        Maison m = new Maison("Appartement", TypeConsommation.NORMAL);

        // On change le type de NORMAL à FORTE
        m.setType(TypeConsommation.FORTE);

        assertEquals(TypeConsommation.FORTE, m.getTypeConsommation(), "Le type aurait dû être modifié vers FORTE");
    }

    /**
     * Teste la méthode equals().
     * Deux maisons sont considérées égales si elles ont le même Nom ET le même Type.
     */
    @Test
    void testEquals() {
        Maison m1 = new Maison("MaMaison", TypeConsommation.NORMAL);
        Maison m2 = new Maison("MaMaison", TypeConsommation.NORMAL); // Identique
        Maison m3 = new Maison("MaMaison", TypeConsommation.BASSE);   // Type différent
        Maison m4 = new Maison("Voisins", TypeConsommation.NORMAL);   // Nom différent

        // Test sur une égalité parfaite
        assertEquals(m1, m2, "Deux maisons avec mêmes attributs doivent être égales");

        // Test sur une inégalité
        assertNotEquals(m1, m3, "Le type différent doit rendre l'égalité fausse");
        assertNotEquals(m1, m4, "Le nom différent doit rendre l'égalité fausse");
        assertNotEquals(m1, null, "L'égalité avec null doit être fausse");
        assertNotEquals(m1, "String", "L'égalité avec un autre type d'objet doit être fausse");
    }

    /**
     * Teste hashCode().
     * Vérifie la règle : si equals() est vrai, alors hashCode() doit être identique.
     */
    @Test
    void testHashCode() {
        Maison m1 = new Maison("TestHash", TypeConsommation.FORTE);
        Maison m2 = new Maison("TestHash", TypeConsommation.FORTE);

        assertEquals(m1.hashCode(), m2.hashCode(), "Le hashCode doit être le même pour deux objets égaux");
    }

    /**
     * Teste toString().
     * Vérifie simplement que la chaîne de caractères contient les infos utiles.
     */
    @Test
    void testToString() {
        Maison m = new Maison("Cabane", TypeConsommation.BASSE);
        String resultat = m.toString();

        // On vérifie que le texte généré contient le nom et le type
        assertTrue(resultat.contains("Cabane"), "Le toString doit contenir le nom de la maison");
        assertTrue(resultat.contains("BASSE"), "Le toString doit contenir le type de consommation");
    }
}