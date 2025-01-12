# LinguaCrypt ![nvm version](https://img.shields.io/badge/version-v0.5.1-green.svg)

LinguaCrypt est une implémentation avancée en Java du jeu de société **Codenames**, développée avec JavaFX et suivant le modèle architectural MVC (Modèle-Vue-Contrôleur). Cette version inclut des fonctionnalités telles que le mode multijoueur, une intelligence artificielle, et un système de thèmes personnalisés.

Vidéo de présentation : https://youtu.be/EADEyPsou2U

## Sommaire
1. [Introduction](#introduction)
2. [Prérequis](#prérequis)
3. [Démarrage rapide](#démarrage-rapide)
4. [Documentation utilisateur](#documentation-utilisateur)
5. [Documentation technique](#documentation-technique)
   - [Architecture du projet](#architecture-du-projet)
   - [Gestion du multijoueur](#gestion-du-multijoueur)
   - [IA et Mode Solo](#ia-et-mode-solo)
6. [Structure du projet](#structure-du-projet)
7. [Changelog](#changelog)

## Introduction
LinguaCrypt est un projet réalisé dans le cadre de la CodingWeek 2025. Il propose une adaptation fidèle et innovante de **Codenames** avec des fonctionnalités  comme le mode multijoueur en ligne, des statistiques avancées, et une expérience utilisateur intuitive.

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
   java --module-path path/to/sdk/lib  --add-modules javafx.controls,javafx.fxml -jar path/to/jar
   ```
Attention il vous faudra un JRE récents pour l'exécuter.
## Documentation utilisateur
### Fonctionnement de l'application
LinguaCrypt est un jeu au tour par tour opposant deux équipes : Bleu et Rouge. Chaque équipe a un maître-espion qui fournit des indices pour aider son équipe à découvrir leurs cartes sans toucher la carte "Assassin".

### Principales fonctionnalités :
- **Multijoueur en ligne** :
  - Hébergez ou rejoignez des parties en ligne.
  - Chat en temps réel.
  - Mise à jour dynamique des équipes et du jeu.
- **Mode solo avec IA** :
  - Jouez contre une intelligence artificielle adaptée à différents niveaux de difficulté.
- **Personnalisation des thèmes** :
  - Créez ou modifiez des thèmes personnalisés (mots ou images).
- **Sauvegarde et reprise** :
  - Sauvegardez votre partie à tout moment et reprenez-la plus tard.
- **Interface intuitive** :
  - Gestion simple des paramètres de partie (taille de la grille, limite de temps, etc.).

### Comment jouer :
1. Lancez l'application.
2. Accédez au menu principal et choisissez un mode de jeu (Multijoueur, Solo, etc.).
3. Configurez la partie : thèmes, équipes, limite de temps.
4. Lancez le jeu et suivez les instructions affichées.
5. Donnez des indices en tant que maître-espion ou faites des choix stratégiques en tant qu'agent.
6. Sauvegardez ou chargez une partie via la barre de menu.

## Documentation technique
### Architecture du projet
LinguaCrypt suit une architecture MVC (Modèle-Vue-Contrôleur) bien définie :

- **Modèle (`model/`)** :
  - Gère la logique métier et l'état du jeu. Les classes comme `Game`, `Grid`, `Card` représentent les éléments fondamentaux du jeu.
  - Des modules supplémentaires, comme `AIUtils` et `GameStat`, permettent d'étendre les fonctionnalités (IA, statistiques).

- **Vue (`view/`)** :
  - Les fichiers FXML dans `resources/FXML/` fournissent les layouts statiques pour les interfaces utilisateur.
  - Les classes associées (par ex. `GameView`) mettent à jour dynamiquement l'interface graphique en fonction des modifications du modèle.

- **Contrôleurs (`controller/`)** :
  - Ces classes (comme `GameController`) gèrent les interactions utilisateur à travers de callback défini dans les classes view et connectent la Vue au Modèle. Par exemple, lorsqu'un joueur sélectionne une carte, le contrôleur met à jour l'état du jeu via le modèle et actualise l'interface.

Cette structure permet une maintenance facile, une extensibilité du code, et une séparation claire des responsabilités.

### Gestion du multijoueur
LinguaCrypt utilise une topologie réseau de type "Listen Server" :
- Lorsqu'un client crée une nouvelle salle, il devient un serveur qui héberge la partie pour les autres clients.
- Les autres joueurs envoient des requêtes pour rejoindre la salle et interagissent avec le serveur pour synchroniser l'état du jeu.

#### Structure réseau :
1. **Serveur (`Server.java`)** :
   - Maintient une instance centralisée de la partie (`Game`).
   - Gère les connexions des clients via des `ClientHandler`.
   - Diffuse des mises à jour à tous les clients (par ex. `GAME_UPDATE`, `TURN_UPDATE`).

2. **Client (`Client.java`)** :
   - Envoie des requêtes au serveur (par ex. "sélectionner une carte").
   - Reçoit et applique les mises à jour envoyées par le serveur.

3. **Messages (`Message.java`, `MessageType.java`)** :
   - Utilisés pour structurer les échanges entre le serveur et les clients (par ex. `HINT_UPDATE`, `GAME_OVER`).

Cette architecture garantit une synchronisation efficace et une expérience fluide pour les joueurs.

### IA et Mode Solo
- **IA stratégique** :
  - Implémentée dans `AIAgent` et `AISpy`, elle prend des décisions en fonction des cartes disponibles et des indices donnés.
  - Elle utilise une API pour récupérer des synonymes et porposer un indice.

- **Mode Solo** :
  - Fonctionne sans connexion réseau, en utilisant un contrôleur local (`SoloGameController`).
  - Permet une expérience rapide et adaptée pour un seul joueur.

## Structure du projet
```
linguacrypt/
├── src/
│   ├── main/
│   │   ├── java/linguacrypt/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── networking/
│   │   │   ├── utils/
│   │   │   ├── view/
│   │   │   └── Main.java
│   │   └── resources/
│   │       ├── FXML/
│   │       └── codenames.txt
│   └── test/
└── build.gradle
```

## Changelog
### Version 0.5.1
- Version fonctionnel mais loin d'être fini

