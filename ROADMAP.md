# **Feuille de route - Projet CodeNames**

## **Résumé du projet**
- **Objectif** : Développer une application jouable basée sur le jeu "CodeNames" avec différentes fonctionnalités, modes de jeu, et options avancées.
- **Durée** : 5 jours (Jour 1 à Jour 5)
- **Technologies** : JavaFX, Gradle
- **Structure du projet** : Architecture MVC avec support de JavaFX.

---

## **Table des contenus**
1. [Jalon 1 : Version basique jouable (Jour 1)](#jalon-1--version-basique-jouable-jour-1)
2. [Jalon 2 : Fonctionnalités thématiques (Jour 2)](#jalon-2--fonctionnalités-thématiques-jour-2)
3. [Jalon 3 : Modes solo et duo (Jour 3)](#jalon-3--modes-solo-et-duo-jour-3)
4. [Jalon 4 : IA et multijoueur (Jour 4)](#jalon-4--ia-et-multijoueur-jour-4)
5. [Jalon 5 : Finalisation et livraison (Jour 5)](#jalon-5--finalisation-et-livraison-jour-5)
6. [Progression en cours ](#progrès-global)

---

## **Légende des Statuts**
- 🟢 **Complété** : La tâche est terminée et validée.
- 🟠 **En cours** : La tâche est en cours de réalisation.
- 🔴 **Non commencé** : La tâche n’a pas encore été entamée.

---

## **Jalon 1 : Version basique jouable (Jour 1)**

| **Tâche**                                      | **Responsable** | **Difficulté** | **Statut**   | **Tags**              |
|------------------------------------------------|-----------------|----------------|--------------|-----------------------|
| Implémenter la logique des clics sur les cartes | Baptiste         | 3/5            | 🟢 Complété  | `Backend`, `jour 1`   |
| Implémenter la logique de changement de tour    | Baptiste         | 2/5            | 🟢 Complété  | `Backend`, `jour 1`   |
| Ajouter l'interface de grille            | Aymeric        | 3/5            | 🟢 Complété  | `UI`, `jour 1`        |
| Implémenter l'interface de sélection des indices| Alexis          | 3/5            | 🟢 Complété  | `UI`, `Backend`, `jour 1` |
| Ajouter un bouton "Fin de tour"                | Alexis        | 2/5            | 🟢 Complété  | `UI`, `jour 1`        |
| Sauvegarder et restaurer une partie             | Aymeric         | 4/5            | 🟢 Complété  | `Backend`, `jour 1`   |

**Résumé :**
Toutes les tâches prévues pour le jour 1 ont été complétées avec succès. Le système de base est fonctionnel et jouable.

---

## **Jalon 2 : Fonctionnalités thématiques (Jour 2)**

| **Tâche**                                      | **Responsable** | **Difficulté** | **Statut**   | **Tags**              |
|------------------------------------------------|-----------------|----------------|--------------|-----------------------|
| Ajouter les thèmes de mots                     | Baptiste         | 3/5            | 🟢 Complété  | `Backend`, `jour 2`   |
| Ajouter une interface pour sélectionner les thèmes | Aymeric      | 3/5            | 🟢 Complété  | `UI`, `jour 2`        |
| Supporter les cartes avec des images           | Alexis        | 4/5            | 🟠 En cours  | `Backend`, `jour 2`   |
| Ajouter un compte à rebours pour les tours     | Alexis        | 4/5            | 🟢 Complété  | `UI`, `jour 2`        |
| Ajouter une interface pour éditer/ajouter des thèmes | Aymeric     | 3/5            | 🟢 Complété  | `UI`, `jour 2`        |
| Sauvegarder et charger les thèmes personnalisés| Aymeric          | 3/5            | 🟢 Complété  | `Backend`, `jour 2`   |
| Ajouter un tableau de statistique des joueurs           | Baptiste        | 3/5            | 🟠 En cours  | `Backend`, `jour 2`   |


**Résumé :**
Le jalon 2 est terminé. Le jeu permet désormais de choisir et personnaliser des thèmes, d'utiliser des images, et de gérer un minuteur pour les tours.
La fonctionalité de statistique est en cours 

---

## **Jalon 3 : Modes solo et duo (Jour 3)**

| **Tâche**                                      | **Responsable** | **Difficulté** | **Statut**   | **Tags**              |
|------------------------------------------------|-----------------|----------------|--------------|-----------------------|
| Implémenter la logique du mode solo            | Baptiste         | 4/5            | 🔴 Non commencé | `Backend`, `jour 3`   |
| Ajouter une interface pour le mode solo        | Aymeric         | 3/5            | 🔴 Non commencé | `UI`, `jour 3`        |
| Implémenter la logique du mode duo             | Baptiste        | 4/5            | 🔴 Non commencé | `Backend`, `jour 3`   |
| Ajouter une interface pour le mode duo         | Aymeric        | 3/5            | 🔴 Non commencé | `UI`, `jour 3`        |
| Ajouter des indices préprogrammés pour le mode solo | Alexis       | 4/5            | 🔴 Non commencé | `Backend`, `jour 3`   |
| Améliorer l'interface de sélection des indices | Alexis          | 3/5            | 🔴 Non commencé | `UI`, `jour 3`        |

**Résumé :**
Les tâches du jour 3 se concentreront sur les nouveaux modes de jeu (Solo et Duo), avec des interfaces et des logiques dédiées.

---

## **Jalon 4 : IA et multijoueur (Jour 4)**

| **Tâche**                                      | **Responsable** | **Difficulté** | **Statut**   | **Tags**              |
|------------------------------------------------|-----------------|----------------|--------------|-----------------------|
| Implémenter une IA pour le maître-espion       | Baptiste         | 5/5            | 🔴 Non commencé | `Backend`, `jour 4`   |
| Implémenter une IA pour les joueurs            | Baptiste         | 4/5            | 🔴 Non commencé | `Backend`, `jour 4`   |
| Ajouter une fonctionnalité multijoueur en réseau | Aymeric       | 5/5            | 🔴 Non commencé | `Backend`, `jour 4`   |
| Ajouter une interface pour le multijoueur      | Aymeric        | 4/5            | 🔴 Non commencé | `UI`, `jour 4`        |
| Synchroniser les données du jeu en multijoueur | Alexis          | 5/5            | 🔴 Non commencé | `Backend`, `jour 4`   |
| Gérer les erreurs réseau                       | Alexis          | 4/5            | 🔴 Non commencé | `Backend`, `jour 4`   |

**Résumé :**
Le jalon 4 inclut l'ajout de l'IA et des fonctionnalités réseau pour jouer en multijoueur.

---

## **Jalon 5 : Finalisation et livraison (Jour 5)**

| **Tâche**                                      | **Responsable** | **Difficulté** | **Statut**   | **Tags**              |
|------------------------------------------------|-----------------|----------------|--------------|-----------------------|
| Tester toutes les fonctionnalités              | Alexis         | 4/5            | 🔴 Non commencé | `Testing`, `jour 5`   |
| Corriger les bugs critiques                    | Aymeric         | 5/5            | 🔴 Non commencé | `Bug`, `jour 5`       |
| Rédiger la documentation utilisateur           | Alexis        | 3/5            | 🔴 Non commencé | `Documentation`, `jour 5` |
| Rédiger la documentation technique             | Baptiste        | 4/5            | 🔴 Non commencé | `Documentation`, `jour 5` |
| Enregistrer une vidéo de démonstration         | Baptiste          | 3/5            | 🔴 Non commencé | `Presentation`, `jour 5` |
| Préparer une version exécutable (JAR)          | Aymeric          | 4/5            | 🔴 Non commencé | `Release`, `jour 5`   |

**Résumé :**
Le dernier jalon inclut la finalisation, les tests, la correction des bugs, et la création des livrables finaux (vidéo, JAR, documentation).

---

## **Progrès global**
- **Tâches complétées** : 11
- **Tâches en cours** : 1 En cours
- **Tâches restantes** : 18
