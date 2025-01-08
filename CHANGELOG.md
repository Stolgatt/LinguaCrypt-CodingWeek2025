# Changelog

Toutes les modifications notables apportées à ce projet seront documentées dans ce fichier.

Le format est basé sur [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), et ce projet respecte la [gestion sémantique de versions](https://semver.org/spec/v2.0.0.html).

---

## [0.2.6] - 2025-01-07

### Ajouté
- **Compteur de temps pour les tours** :
  - Ajout de la classe `TimerController` pour gérer le compte à rebours des tours.
  - Mise à jour de l’interface utilisateur avec un composant `Label` dans `GameView` pour afficher le temps restant.
  - Intégration des événements de fin de compte à rebours dans `GameController`.

### Modifié
- Mise à jour de `GameConfigurationDialog` pour inclure une option de configuration de la durée des tours.

---

## [0.2.5] - 2025-01-07

### Ajouté
- **Boîte de dialogue pour fin de tour** :
  - Ajout de la classe `EndOfTurnDialog` pour afficher une boîte modale permettant de confirmer la fin d’un tour.
  - Intégration avec `GameController` pour pauser la logique de jeu jusqu’à validation de l’utilisateur.

### Modifié
- Ajustement de la méthode `nextTurn()` dans `GameController` pour inclure l’appel à `EndOfTurnDialog`.

---

## [0.2.4] - 2025-01-07

### Ajouté
- **Cartes avec images** :
  - Mise à jour de la classe `Card` pour inclure un champ `urlImage` permettant d’afficher des images sur les cartes.
  - Mise à jour de `Grid` pour supporter les cartes avec images lors de l’initialisation.
  - Intégration des images par défaut dans les ressources du projet.
  - Choixdu mode de jeux dans le menu principal

---

## [0.2.3] - 2025-01-07

### Ajouté
- **Support des thèmes personnalisés** :
  - Ajout de la classe utilitaire `ThemeLoader` pour charger, sauvegarder et gérer des thèmes personnalisés depuis un fichier JSON.
  - Intégration des mots personnalisés dans `Grid` via une méthode d’initialisation dynamique.

### Modifié
- Refactorisation de `BankCard` pour inclure le chargement des thèmes par défaut via `ThemeLoader`.

---

## [0.2.2] - 2025-01-07

### Ajouté
- **Interface de gestion des thèmes** :
  - Création de la classe `CustomThemeDialog` pour permettre aux utilisateurs de créer, éditer ou supprimer des thèmes.
  - Intégration des fonctionnalités de gestion des thèmes dans `MainMenuController` via un nouveau bouton.

### Modifié
- Ajout d’un mécanisme de validation des mots ajoutés dans les thèmes personnalisés.

---

## [0.2.1] - 2025-01-07

### Ajouté
- **Thèmes prédéfinis** :
  - Chargement initial des thèmes prédéfinis à partir de `themes.json`.
  - Intégration avec `Grid` pour utiliser ces thèmes dans les grilles du jeu.
  - Ajout de la méthode `getWordsForTheme(String themeName)` dans `ThemeLoader`.

---

## [0.2.0] - 2025-01-07

### Ajouté
- **Grilles dynamiques par thème** :
  - Mise à jour de `Grid` pour permettre la génération dynamique de cartes basées sur des thèmes.
  - Ajout de la logique pour sélectionner aléatoirement des mots depuis une liste fournie.

### Fixé
- Correction d’un bug dans `Grid` où des mots pouvaient être assignés à plusieurs cartes.

---

## [0.1.5] - 2025-01-06

### Ajouté
- **Sauvegarde et restauration des parties** :
  - Ajout de la classe utilitaire `GameUtils` pour gérer la sauvegarde et la restauration des parties.
  - Utilisation de la sérialisation pour enregistrer l’état du jeu dans un fichier `game_save.dat`.

### Modifié
- Mise à jour de `MenuBarController` pour inclure les actions de sauvegarde et de chargement.

---

## [0.1.4] - 2025-01-06

### Ajouté
- **Barre de menu** :
  - Ajout de la classe `MenuBarView` pour la gestion de la barre de menu.
  - Options ajoutées : "Sauvegarder", "Charger", "Quitter".

---

## [0.1.3] - 2025-01-06

### Ajouté
- **Dialogues de victoire/défaite** :
  - Ajout de la classe `EndOfGameDialog` pour afficher les résultats du jeu.
  - Intégration avec `GameController` pour déclencher les dialogues en cas de victoire ou de défaite.

---

## [0.1.2] - 2025-01-06

### Ajouté
- **Mécanique de jeu** :
  - Implémentation de la logique de changement de tour dans `GameController`.
  - Ajout de la méthode `flipCard()` dans `Card` pour gérer l’état des cartes.

---

## [0.1.1] - 2025-01-06

### Ajouté
- **Configuration du jeu** :
  - Ajout de la classe `GameConfigurationDialog` pour permettre la configuration des paramètres du jeu.
  - Paramètres supportés : taille de la grille, niveau de difficulté, thème.
  - Validation des entrées utilisateur ajoutée dans `GameConfiguration`.

---

## [0.1.0] - 2025-01-06

### Ajouté
- **Initialisation du projet** :
  - Mise en place de l’architecture de base avec JavaFX.
  - Création de la classe `Main` et des fichiers FXML pour le menu principal et la vue du jeu.
  - Ajout des classes `Card`, `Grid`, et `Game` pour modéliser les éléments de base du jeu.

