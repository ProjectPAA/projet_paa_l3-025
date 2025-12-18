package up.mi.paa.solvers;

import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.Reseau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Cette classe implémente un algorithme augmentatif, basé sur la recherche bornée dans l'espace des possibilités.</p>
 * <p>Il explore systèmatiquement (en profondeur) les connexions possibles, gardant en mémoire le surcharge sur la branche courante. Dès que cela dépasse le meilleure coût total vu avant, on arête d'explorer la branche courante : elle ne peut pas contenir la solution optimale.</p>
 * <p>Note: Il est très lent. (Plusieurs minutes sur les réseaux exemples).</p>
 * 
 * @author Jacques ZHENG
 * @author Mamadou NIMAGA DIT
 * @author Zalán MOLNÁR
 */
public class SolverBranchBound extends Solver {

    // Variables pour stocker le meilleur résultat
    /**
     * Variable interne permettant la mémoïsation de la borne.
     */
    private double meilleurCoutGlobal;
    /**
     * Variable interne permettant la mémoïsation de la borne.
     */
    private Map<Maison, Generateur> meilleureConfiguration;

    // Tableaux pour la vitesse (beaucoup plus rapide que les Maps)
    /**
     * Variable interne permettant un accès plus efficace (dans ce cas particulier, où les indices sont connues).
     */
    private Maison[] maisonsArr;
    /**
     * Variable interne permettant un accès plus efficace (dans ce cas particulier, où les indices sont connues).
     */
    private Generateur[] generateursArr;
    /**
     * Variable interne permettant un accès plus efficace (dans ce cas particulier, où les indices sont connues).
     */
    private int[] chargesActuelles;
    /**
     * Variable interne permettant un accès plus efficace (dans ce cas particulier, où les indices sont connues).
     */
    private Generateur[] affectationsCourantes; // Pour se souvenir qui est connecté à qui

    /**
     * Constructeur à partir du {@link Reseau} à traiter.
	 * @param reseau Le {@link Reseau} à traiter.
     */
    public SolverBranchBound(Reseau reseau, double lambda) {
    	super(reseau, lambda);
    }

    /**
     * <p>Algorithme augmentatif, basé sur la recherche bornée dans l'espace des possibilités.</p>
     * <p>Il explore systèmatiquement (en profondeur) les connexions possibles, gardant en mémoire le surcharge sur la branche courante. Dès que cela dépasse le meilleure coût total vu avant, on arête d'explorer la branche courante : elle ne peut pas contenir la solution optimale.</p>
 	 * <p>Note: Il est très lent. (Plusieurs minutes sur les réseaux exemples).</p>
 	 * @param lambda coût du surcharge.
     */
    @Override
    public void solve() {
        long debut = System.currentTimeMillis();
        System.out.println("Recherche de la solution optimale (Branch & Bound)...");

        // Si on n'a pas encore de connexions permettant de couper les mauvaises branches dès la 1ère milliseconde, on lance le Greedy Maison.
        // Else on part de la configuration existante. (Comme ça on est extensible, et refait pas de travail si on a déjà la solution optimale.)
        // Cela permet de couper les mauvaises branches dès la 1ère milliseconde.
        if (reseau.getConnexions().isEmpty()) {
        	SolverGreedyMaison greedy = new SolverGreedyMaison(this.reseau, this.lambda);
        	greedy.solve();
        }

        this.meilleurCoutGlobal = this.reseau.calculerCout(lambda);
        this.meilleureConfiguration = new HashMap<>(this.reseau.getConnexions());
        System.out.println("   > Score de départ : " + this.meilleurCoutGlobal);

        // on préparation des tableaux rapides
        this.reseau.getConnexions().clear();
        this.maisonsArr = this.reseau.getMaisons().values().toArray(new Maison[0]);
        this.generateursArr = this.reseau.getGenerateurs().values().toArray(new Generateur[0]);
        this.chargesActuelles = new int[generateursArr.length];
        this.affectationsCourantes = new Generateur[maisonsArr.length];

        // Trie les maisons (40kW -> 20kW -> 10kW) cela accélère la détection des impasses par x100
        Arrays.sort(maisonsArr, (m1, m2) -> Integer.compare(m2.getTypeConsommation().getDemande(), m1.getTypeConsommation().getDemande()));

        backtrack(0, lambda, 0.0);

        // Application du résultat final
        if (!this.meilleureConfiguration.isEmpty()) {
            this.reseau.getConnexions().clear();
            this.reseau.getConnexions().putAll(this.meilleureConfiguration);
            long duree = System.currentTimeMillis() - debut;
            System.out.println("Terminé en " + duree + " ms.");
            System.out.println("Coût minimal absolu : " + this.meilleurCoutGlobal);
        }
    }

    /**
     * Partie récursif de l'algorithme qui exécute la recherche dans l'arbre des possibilités.
     * @param index L'indice du générateur à traiter
     * @param lambda le coût du surcharge
     * @param surchargePartielle Le coût dû à la surcharge déjà présent sur la branche courante.
     */
    private void backtrack(int index, double lambda, double surchargePartielle) {
    	
    	if (Thread.currentThread().isInterrupted()) {
    		return;
    	}

        // Si la surcharge seule dépasse déjà le record, on stoppe.
        if (surchargePartielle * lambda >= this.meilleurCoutGlobal) {
            return;
        }

        // CAS DE BASE : Fin de l'arbre
        if (index == maisonsArr.length) {
            finaliserEtVerifier(lambda, surchargePartielle);
            return;
        }

        Maison maison = maisonsArr[index];
        int demande = maison.getTypeConsommation().getDemande();

        // On teste chaque générateur
        for (int i = 0; i < generateursArr.length; i++) {
            Generateur gen = generateursArr[i];

            int ancienneCharge = chargesActuelles[i];

            // Calcul du delta de surcharge
            double surchargeAvant = Math.max(0, (double)ancienneCharge / gen.getCapaciteMAx() - 1.0);
            int nouvelleCharge = ancienneCharge + demande;
            double surchargeApres = Math.max(0, (double)nouvelleCharge / gen.getCapaciteMAx() - 1.0);

            // On applique le mouvement
            chargesActuelles[i] = nouvelleCharge;
            affectationsCourantes[index] = gen; // On note l'affectation

            // Récursion
            backtrack(index + 1, lambda, surchargePartielle + (surchargeApres - surchargeAvant));

            // Backtrack (Annulation)
            chargesActuelles[i] = ancienneCharge;
        }
    }

    // Calcul précis du coût complet (Dispersion + Surcharge)
    /**
     * Décision si la branche explorée jusqu'à une feuille à donnée un meilleur coût ou pas que celle de référence.
     * @param lambda coût du surcharge
     * @param totalSurcharge surcharge présent sur la branche.
     */
    private void finaliserEtVerifier(double lambda, double totalSurcharge) {
        double moyenneTaux = 0;
        double[] taux = new double[generateursArr.length];

        // Calcul rapide de la dispersion sur tableaux
        for(int i=0; i<generateursArr.length; i++) {
            taux[i] = (double)chargesActuelles[i] / generateursArr[i].getCapaciteMAx();
            moyenneTaux += taux[i];
        }
        moyenneTaux /= generateursArr.length;

        double dispersion = 0;
        for(double t : taux) {
            dispersion += Math.abs(t - moyenneTaux);
        }

        double coutTotal = dispersion + (totalSurcharge * lambda);

        // Si on bat le record
        if (coutTotal < this.meilleurCoutGlobal) {
            this.meilleurCoutGlobal = coutTotal;

            // On reconstruit la Map pour la sauvegarde finale
            this.meilleureConfiguration.clear();
            for(int i=0; i<maisonsArr.length; i++) {
                this.meilleureConfiguration.put(maisonsArr[i], affectationsCourantes[i]);
            }
        }
    }
}