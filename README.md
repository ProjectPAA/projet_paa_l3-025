# Projet PAA : Optimisation de Réseau Électrique (Partie 1/2)

**Auteurs :**

  * NIMAGA Mamadou
  * Jacques Zheng
  * Zalán MOLNÁR

**Date :** Le 13 Décembre 2025
**Université :** Université Paris Cité - Licence 3 Informatique/Mathématique - Informatique & Applications
**UFR de Mathématiques et Informatique**

## 1\. Description du Projet

Ce projet a pour but de modéliser et d'optimiser un réseau de distribution d'électricité. Il permet de connecter des maisons à des générateurs tout en minimisant le coût total du réseau (basé sur la dispersion des câbles et la surcharge des générateurs).

Le programme propose deux modes de fonctionnement :

1.  **Mode Manuel (Partie 1) :** Construction interactive du réseau pas à pas.
2.  **Mode Fichier (Partie 2) :** Chargement d'un réseau existant, résolution automatique par algorithmes et sauvegarde.

## 2\. Comment exécuter le programme

La classe principale contenant la méthode `main` est :
**`up.mi.paa.inter_face.InterfaceTextuelle`**

### Compilation

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

## 3\. Fonctionnalités Implémentées

Nous avons implémenté l'ensemble des fonctionnalités demandées dans le sujet :

  * **Lecture de fichier robuste :** Le programme charge les fichiers `.txt` en respectant la syntaxe stricte (Générateurs, Maisons, Connexions). Gestion des erreurs précise (numéro de ligne, type d'erreur).
  * **Menu interactif :** Adaptation du menu selon le mode de lancement (Manuel vs Fichier).
  * **Algorithme Naïf (Recherche Locale) :** Optimisation par échanges aléatoires de connexions sur `k` itérations.
  * **Sauvegarde :** Exportation de l'état actuel du réseau dans un fichier texte respectant le format d'entrée.

### Fonctionnalités Bonus / Améliorations

  * **Algorithme Glouton (Greedy) :** Nous avons ajouté un second algorithme de résolution (voir section 4).
  * **Comparaison de performance :** L'interface affiche le temps d'exécution (en ms) et le pourcentage de réduction du coût après optimisation.
  * **Gestion des formats :** Support des deux formats de déclaration de maison (`maison(M1, NORMAL)` et `M1 20kW`).

## 4\. Algorithmes de Résolution

Le programme propose deux approches pour optimiser le réseau:

### A. Algorithme Naïf (Local Search) - *Demandé par le sujet*

Cet algorithme part de la solution existante et tente de l'améliorer par petites touches.

  * **Principe :** À chaque itération, on choisit une maison et un générateur au hasard. On tente de connecter la maison à ce nouveau générateur. Si le coût total diminue, on garde le changement. Sinon, on annule.
  * **Avantage :** Très précis si on lui laisse du temps (ex: 10 000 itérations).
  * **Inconvénient :** Peut être lent.

### B. Algorithme Glouton (Greedy Solver) - *Algorithme Efficace (Bonus)*

Cet algorithme repart de zéro (il efface les connexions existantes) pour construire une solution logique.

  * **Principe :** Il trie les générateurs par capacité. Il parcourt ensuite les maisons et tente de les connecter au premier générateur qui a assez de place disponible, afin de minimiser la surcharge.
  * **Avantage :** Extrêmement rapide (< 5ms) et donne une solution "bonne" instantanément.
  * **Note :** Comme il réinitialise le réseau, il peut parfois donner un coût légèrement supérieur à une solution "Naïve" qui aurait tourné très longtemps, mais il est beaucoup plus stable.

## 5\. État du projet

  * **Fonctionnalités manquantes :** Aucune.
  * **Problèmes connus :** Aucun bug majeur détecté. Le parsing est strict : assurez-vous que le fichier d'entrée se termine bien par des points `.` à chaque ligne.
