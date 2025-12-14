package up.mi.paa.solvers;

import up.mi.paa.pbl.algo.Generateur;
import up.mi.paa.pbl.algo.Maison;
import up.mi.paa.pbl.algo.Reseau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SolverOptimal extends Solver {

    private Reseau reseau;

    // Variables pour stocker le meilleur résultat
    private double meilleurCoutGlobal;
    private Map<Maison, Generateur> meilleureConfiguration;

    // Tableaux pour la vitesse (beaucoup plus rapide que les Maps)
    private Maison[] maisonsArr;
    private Generateur[] generateursArr;
    private int[] chargesActuelles;
    private Generateur[] affectationsCourantes; // Pour se souvenir qui est connecté à qui

    public SolverOptimal(Reseau reseau) {
        this.reseau = reseau;
    }

    @Override
    public void solve(double lambda) {
        long debut = System.currentTimeMillis();
        System.out.println("Recherche OPTIMALE (Branch & Bound)...");

        // En premier, on lance le Greedy pour avoir une barre à battre
        // Cela permet de couper les mauvaises branches dès la 1ère milliseconde.
        SolverSmartGreedy greedy = new SolverSmartGreedy(this.reseau);
        greedy.solve(lambda);

        this.meilleurCoutGlobal = this.reseau.calculerCout(lambda);
        this.meilleureConfiguration = new HashMap<>(this.reseau.getConnexions());
        System.out.println("   > Score de départ (Greedy) : " + this.meilleurCoutGlobal);

        // on préparation des tableaux rapides
        this.reseau.getConnexions().clear();
        this.maisonsArr = this.reseau.getMaisons().values().toArray(new Maison[0]);
        this.generateursArr = this.reseau.getGenerateurs().values().toArray(new Generateur[0]);
        this.chargesActuelles = new int[generateursArr.length];
        this.affectationsCourantes = new Generateur[maisonsArr.length];

        // Trie les maisons (40kW -> 20kW -> 10kW) cela accélère la détection des impasses par x100
        Arrays.sort(maisonsArr, (m1, m2) -> Integer.compare(m2.getTypeConsommation().getDemande(), m1.getTypeConsommation().getDemande()));

        // 4. Lancement du moteur
        backtrack(0, lambda, 0.0);

        // 5. Application du résultat final
        if (!this.meilleureConfiguration.isEmpty()) {
            this.reseau.getConnexions().clear();
            this.reseau.getConnexions().putAll(this.meilleureConfiguration);
            long duree = System.currentTimeMillis() - debut;
            System.out.println("Terminé en " + duree + " ms.");
            System.out.println("Coût minimal absolu : " + this.meilleurCoutGlobal);
        }
    }

    private void backtrack(int index, double lambda, double surchargePartielle) {

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