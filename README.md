# Projet PAA : Optimisation de Réseau Électrique (Partie 1/2)

**Auteurs :**

  * NIMAGA Mamadou
  * ZHENG Jacques
  * MOLNÁR Zalán

**Date :** Le 21 Décembre 2025
**Université :** Université Paris Cité - Licence 3 Informatique/Mathématique - Informatique & Applications
**UFR de Mathématiques et Informatique**

## 1\. Description du Projet

Ce projet a pour but de modéliser et d'optimiser un réseau de distribution d'électricité. Il permet de connecter des maisons à des générateurs tout en minimisant le coût total du réseau (basé sur la dispersion équitable de la charge et la surcharge des générateurs).

Le programme propose deux modes de fonctionnement :

1.  **Mode Manuel (Partie 1) :** Construction interactive du réseau pas à pas.
2.  **Mode Fichier (Partie 2) :** Chargement d'un réseau existant, résolution automatique par algorithmes et sauvegarde.

## 2\. Documentation

Pour la documentation, veuillez vous référer au Javadoc dans le répertoire /doc. (Nous recommendons de commencer à index.html ou à overview-tree.html) 

## 3\. Comment exécuter le programme

On propose un fichier exécutable, projet_praa_executable.jar à la racine de l'archive rendue. Pour l'exécuter, il suffit de faire (depuis la racine de l'arborescence) :

```bash
java -jar projet_praa_executable.jar
```

Mais si vous préférez, vous pouvez compiler vous-même notre projet : 

### Compilation

La classe principale contenant la méthode `main` est :
**`up.mi.paa.inter_face.InterfaceTextuelle`**

Placez-vous à la racine du projet (dossier contenant `src`) et compilez avec :

```bash
javac -d bin -sourcepath src src/up/mi/paa/inter_face/InterfaceTextuelle.java
```

### Exécution

**Option A : Lancer le Mode Manuel (Partie 1)**
Ne passez aucun argument pour construire le réseau à la main.

```bash
java -cp bin up.mi.paa.inter_face.InterfaceTextuelle
```

**Option B : Lancer le Mode Automatique (Partie 2)**
Passez le chemin d'un fichier de configuration en argument.

```bash
java -cp bin up.mi.paa.inter_face.InterfaceTextuelle reseau_test.txt
```

## 4\. Fonctionnalités Implémentées

Nous avons implémenté l'ensemble des fonctionnalités demandées dans le sujet :

  * **Lecture de fichier robuste :** Le programme charge les fichiers `.txt` en respectant la syntaxe stricte (Générateurs, Maisons, Connexions). Gestion des erreurs précise (numéro de ligne, type d'erreur).
  * **Menu interactif :** Adaptation du menu selon le mode de lancement (Manuel vs Fichier).
  * **Algorithme Naïf (Recherche Locale) :** Optimisation par échanges aléatoires de connexions sur `k` itérations.
  * **Sauvegarde :** Exportation de l'état actuel du réseau dans un fichier texte respectant le format d'entrée.

### Fonctionnalités Bonus / Améliorations

  * **Algorithmes additionnels :** Nous avons ajouté plusieurs algorithmes alternatifs de résolution (voir section 4).
  * **Comparaison de performance :** L'interface affiche le temps d'exécution (en ms) et le pourcentage de réduction du coût après optimisation.
  * **Gestion des formats :** Support des deux formats de déclaration de maison (`maison(M1, NORMAL)` et `M1 20kW`).

## 5\. Algorithmes de Résolution

Le programme propose une approche "en portfolio" pour optimiser le réseau. Il exécute quatre algorithmes différents (le naïf, deux gloutons basés sur deux heuristiques différents, et un algorithme de type Branch & Bound (Brancher entre Bornes)) en parallèle et retient le meillure résultat obtenu. 
Si le programme (au moins un des algorithmes) travaillerait plus d'une minute, on les force à arrêter, et on prend en compte le(s) résultat(s) partiel(s) obtenu(s) (s'il est(sont) valide(s)). Dans tous les cas, le programme ne travail pas comme ça plus que soixante secondes.

Voici une description briève des algorithmes qu'on exécute lors de cette phase: 

### A. Algorithme Naïf (Random Local Search) - *Demandé par le sujet*

Cet algorithme part de la solution existante et tente de l'améliorer par petites touches.

  * **Principe :** À chaque itération, on choisit une maison et un générateur au hasard. On tente de connecter la maison à ce nouveau générateur. Si le coût total diminue, on garde le changement. Sinon, on annule.
  * **Avantage :** Très précis si on lui laisse du temps (ex: 10 000 itérations).
  * **Inconvénient :** Peut être lent, et peut converger vers un minimum local même si un minimal global plus petit existe.

### B. Algorithme Glouton Generateur (Heuristic Greedy Construction) - *Algorithme Bonus*

Cet algorithme repart de zéro (il efface les connexions existantes) pour construire une solution heuristiquement optimale. En générale, son heuristique est moins performant que celui de Glouton Maison.

  * **Principe :** Il trie les générateurs par capacité. Il parcourt ensuite les maisons et les connecte au générateur qui minimise le surcharge absolu ajouté. (Le premier qui a suffisamment de capacité libre, ou, si aucun générateur n'en a suffisamment, celui de plus grande capacité créera le plus petit surcharge absolu.)
  * **Avantage :** Extrêmement rapide (O(n<sup>2</sup>)) et donne une solution "bonne". Ne nécéssite pas l'existance d'une solution préalable. Il est déterministe.
  * **Note :** Comme il réinitialise le réseau, il peut parfois donner un coût supérieur à celle de la solution préexistante. (Si cette solution était déjà optimale, sur un Réseau où l'heuristique ne le trouve pas, par exemple.) Il ne prend pas en compte la valeur de λ.

### C. Algorithme Glouton Maison (Heuristic Greedy Construction) - *Algorithme Bonus*

Cet algorithme repart de zéro (il efface les connexions existantes) pour construire une solution heuristiquement optimale. En générale, son heuristique est plus performant que celui de Glouton Generateur.

  * **Principe :** Il trie les maisons par demande décroissante. Puis pour chaque maison dans l'ordre, il parcourt les générateurs et la connecte à celui dont le taux de charge sera le moins élevé après la connexion. (Limitant à la fois l'augmentation de surcharge et de dispersion.)
  * **Avantage :** Extrêmement rapide (O(n<sup>2</sup>)) et donne une solution "bonne". Ne nécéssite pas l'existance d'une solution préalable. Il est déterministe.
  * **Note :** Comme il réinitialise le réseau, il peut parfois donner un coût supérieur à celle de la solution préexistante. (Si cette solution était déjà optimale, sur un Réseau où l'heuristique ne le trouve pas, par exemple.) Il ne prend pas en compte la valeur de λ.

### D. Algorithme Branches entre Bornes (Branch and Bound Possibility-Space Search) - *Algorithme Bonus*

Cet algorithme fait une recherche dans l'espace des solutions possibles, en arêtant l'exploration dès qu'on est certaine qu'une branche ne contient pas la solution optimale.

  * **Principe :** On explore systèmatiquement (en profondeur) les connexions possibles, gardant en mémoire le surcharge sur la branche courante. Dès que cela dépasse le meilleure coût total vu avant, on arête d'explorer la branche courante : elle ne peut pas contenir la solution optimale.
  * **Avantage :** Donne la meilleure solution possible. Ne nécéssite pas l'existance d'une solution préalable. Il est déterministe.
  * **Note :** Extrêmement lente (plusieurs minutes sur les réseaux exemples).

## 6\. État du projet

  * **Fonctionnalités manquantes :** Aucune.
  * **Problèmes connus :** Aucun bug majeur détecté. Le parsing est strict : assurez-vous que le fichier d'entrée se termine bien par des points `.` à chaque ligne.
