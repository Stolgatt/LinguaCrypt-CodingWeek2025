# LinguaCrypt ![nvm version](https://img.shields.io/badge/version-v0.2.6-yellow.svg)

LinguaCrypt est une implémentation en Java du jeu de société Codenames, construite avec JavaFX et suivant le modèle architectural MVC (Modèle-Vue-Contrôleur).

## Sommaire
1. [Introduction](#introduction)
2. [Prérequis](#prérequis)
3. [Démarrage rapide](#démarrage-rapide)
4. [Documentation utilisateur](#documentation-utilisateur)
5. [Documentation technique](#documentation-technique)
   - [Vision globale de l'application](#vision-globale-de-lapplication)
   - [Architecture du projet](#architecture-du-projet)
   - [Fonctionnement du flux d’application](#fonctionnement-du-flux-dapplication)
   - [Patterns identifiés](#patterns-identifiés)
6. [Documentation supplémentaire](#documentation-supplémentaire)
7. [Structure du projet](#structure-du-projet)

## Introduction
LinguaCrypt est un projet réalisé dans le cadre de la CodingWeek 2025. Il s’agit d’une adaptation du jeu de société **Codenames™**, mettant en avant des compétences en programmation orientée objet, en développement d’interfaces graphiques (JavaFX) et en conception logicielle (modèle MVC).

Pour plus de détails sur la planification et l’avancement, consultez la [Feuille de route](roadmap.md).
Pour une vue détaillée des versions et des changements, consultez le [Changelog](changelog.md).

## Prérequis
- Java 22 ou supérieur
- Gradle 8.0 ou supérieur
- JavaFX (inclus dans la configuration du projet)

## Démarrage rapide
### Récupération et exécution du projet
1. **Cloner le dépôt** :
   ```bash
   git clone https://gitlab.telecomnancy.univ-lorraine.fr/projets/2425/pcd2k25/codingweek/grp18.git
   ```
2. **Compiler et exécuter le projet** :
   ```bash
   gradle build
   gradle run
   ```

### Utilisation de la dernière version stable
1. Accédez à la page des versions sur GitLab : [Versions GitLab](https://gitlab.telecomnancy.univ-lorraine.fr/projets/2425/pcd2k25/codingweek/grp18/-/releases).
2. Téléchargez le fichier JAR de la dernière version stable.
3. Exécutez le fichier JAR :
   ```bash
   java -jar LinguaCrypt-<version>.jar
   ```

## Documentation utilisateur
### Fonctionnement de l'application
LinguaCrypt est un jeu au tour par tour opposant deux équipes : Bleu et Rouge. Chaque équipe a un maître-espion qui fournit des indices pour aider son équipe à découvrir leurs cartes sans toucher la carte "Assassin".

### Principales fonctionnalités :
- **Paramétrage du jeu** :
  - Taille de la grille
  - Niveau de difficulté
  - Nombre de joueurs
  - Minuteur pour les tours
  - Sélection de thèmes personnalisés
- **Sauvegarde et restauration** :
  - Sauvegardez votre partie à tout moment et reprenez-la plus tard.
- **Indications et gameplay** :
  - Donnez des indices en tant que maître-espion.
  - Sélectionnez les cartes pour révéler leurs couleurs.
  - Atteignez vos objectifs tout en évitant les erreurs fatales.

### Comment jouer :
1. Lancer l'application.
2. Accéder au menu principal et choisissez votre mode de jeux (Mots ou Images).
3. Démarrer une partie en suivant les instructions affichées (configuration, équipes, ect...).
4. Sauvegarder ou charger une partie à partir de la barre de menu.
5. Donnez des indices en tant que maitre espion en cliquant sur le bouton approprié.
6. Cliquez sur les cartes pour révéler leurs couleurs.
7. Depusi le menu principale vous pouvez modifié les thématiques (en ajouter ou ajouter des mots aux thèmes existants).

## Documentation technique
### Vision globale de l'application
LinguaCrypt repose sur des patterns clés :

- **MVC (Modèle-Vue-Contrôleur)** :
  - Séparation claire entre la logique de gestion des données, les interactions utilisateur, et l’interface visuelle.
- **Singleton** : Utilisé dans `GameConfiguration` pour gérer un accès unique aux paramètres du jeu.
- **Observer** : Implémenté pour notifier les vues des changements dans l’état du jeu.
- **Sérialisation** : Gère la sauvegarde et le chargement de l’état du jeu.

### Architecture du projet

#### Modèle (`src/main/java/linguacrypt/model/`)
- **`Game`** :
  - Cœur de la logique du jeu, gère les équipes, les tours, et la grille.
  - Notifie les observateurs des mises à jour.
- **`Grid`** :
  - Structure représentant la grille de cartes.
  - Initialisation aléatoire des mots selon les thèmes.
- **`Card`** :
  - Contient les informations sur les cartes : mot, image, couleur, état.
  - Méthodes : `flipCard()` pour changer l’état d’une carte.
- **`GameConfiguration`** :
  - Singleton permettant de gérer les paramètres du jeu (taille de la grille, niveau de difficulté, etc.).
- **`Player` et `Team`** :
  - Représentent les joueurs et équipes avec leurs rôles et caractéristiques.

#### Vue (`src/main/java/linguacrypt/view/`)
- **`MainMenuView`** : Interface utilisateur pour le menu principal.
- **`GameView`** : Interface principale pendant une partie (grille, minuteur, scores).
- **`CustomThemeDialog`** : Fenêtre modale pour gérer les thèmes personnalisés.
- **FXML** : Situés dans `src/main/resources/FXML/`, définissent la structure des interfaces.

#### Contrôleur (`src/main/java/linguacrypt/controller/`)
- **`GameController`** :
  - Gère les interactions utilisateur et met à jour le modèle et la vue.
  - Méthodes : `nextTurn()`, `selectCard()`.
- **`MainMenuController`** : Navigation dans le menu principal.
- **`MenuBarController`** : Actions de sauvegarde, chargement, et sortie.
- **`TimerController`** : Gère le compte à rebours des tours.

### Fonctionnement de l’application
1. **Initialisation** :
   - `Main.java` lance l’application en chargeant le menu principal.
2. **Configuration** :
   - Les paramètres utilisateur sont saisis via `GameConfigurationDialog` et sauvegardés dans `GameConfiguration`.
3. **Gameplay** :
   - `GameController` initialise la grille et coordonne les interactions entre la vue (`GameView`) et le modèle (`Game`).
4. **Sauvegarde et restauration** :
   - Géré par `GameUtils`, qui utilise la sérialisation pour persister l’état actuel du jeu.

## Documentation supplémentaire
- Voir la [Feuille de route](roadmap.md) pour le suivi des jalons.
- Consulter le [Changelog](changelog.md) pour détails sur les versions.
- C'est tout

## Structure du projet
```
linguacrypt/
├── src/
│   ├── main/
│   │   ├── java/linguacrypt/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── view/
│   │   │   └── Main.java
│   │   └── resources/
│   │       ├── FXML/
│   │       └── codenames.txt
│   └── test/
│       └── java/
└── build.gradle
```

## Changelog
Consultez le fichier [Changelog](changelog.md) pour une liste détaillée des versions, des changements et des évolutions.

