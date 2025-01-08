# Cahier des charges

## Projet LinguaCrypt - Coding Week 2025

### Groupe 18 :
- Baptiste PACHOT
- Aymeric ROBERT
- Alexis CHAVY

---

## 1. Objectif du Projet
Le projet consiste à développer **LinguaCrypt**, une version clonée du jeu **CodeNames** en utilisant **Java**. Ce logiciel doit permettre :
- De jouer à CodeNames.
- D'éditer des cartes.
- D'offrir des fonctionnalités supplémentaires pour enrichir l'expérience de jeu.

---

## 2. Fonctionnalités Principales

### A. Paramètres

#### Configuration des paramètres
- **Priorité** : Haute  
- **Détails** :
  - Affichage des paramètres dans la fenêtre de jeu :
    - Difficulté de l'IA
    - Nombre de joueur
    - Nombre de joueur par équipe
    - Taille de la grille
    - Thème(s) de mots choisi
    - Temps de réflexion pour les maître-espions et les agents (-1 pour un temps infini et sinon un temps en secondes <= 300 secondes (5 minutes))

#### Modification des paramètres
- **Priorité** : Haute  
- **Détails** :
  - Taille de la grille modifiable (par défaut 5x5, maximum 15x15).
  - Choix des thématiques de mots.
  - Nombre de joueurs ajustable.
  - Temps de réflexion configurable (par défaut désactivé, -1).
  - Respect des règles officielles du jeu [CodeName](https://iello.fr/wp-content/uploads/2016/10/Codenames_rulebook_FR-web_2018.pdf).

#### Ajout de mots personnalisés
- **Priorité** : Moyenne  
- **Détails** :
  - Création de liste de mots personnalisés avec nom de thématique.
  - Choix de cette thématique ou ajout aux mots existants.
  - utilisation des thèmes choisis.

### B. Partie

#### Création des équipes
- **Priorité** : Haute
- **Détails** :
    - Affichage d'un panneau de gestion des équipes
    - possibilité d'ajout d'un joueur en maitre-espion ou en agent dans l'équipe rouge ou l'équipe bleue.
    - Sauvegarde des profils des joueurs pour les statistiques
    - Possibilité de récupération d'un profil de joueur déjà existant

#### Lancement d'une partie
- **Priorité** : Haute  
- **Détails** :
  - Création de la grille avec mots aléatoires.
  - Attribution aléatoire des couleurs (Rouge, Bleu, Noir).
  - Version **1 seul écran** : A la fin d'un tour, un bouton apparait pour démarrer le tour suivant, la grille des espions n'est alors pas dévoilée automatiquement.
    

#### Apparence des cartes
- **Priorité** : Haute  
- **Détails** :
  - Cartes libres : Fond blanc avec texte par dessus.
  - Cartes dévoilées : Rouge, Bleu, Noir ou Blanc selon leur assignation avec toujours le mot par dessus.

#### Sauvegarde et chargement
- **Priorité** : Moyenne  
- **Détails** :
  - Sauvegarde des cartes et de leur état ainsi que de la configuration des paramètres.
  - Chargement avec restauration complète de l'état du jeu, des équipes et des paramètres.

#### Tours de jeu
- **Maître-espion**
  - **Priorité** : Haute  
  - **Détails** :
    - Affichage des cartes avec couleurs visibles.
    - Entrée d'un mot-indice et d'un nombre.

- **Agents**
  - **Priorité** : Haute  
  - **Détails** :
    - Affichage des cartes sans couleurs pour les cartes libres et avec couleurs pour celle déjà dévoilées auparavant.
    - Sélection d'un mots à révéler.
    - Continuation ou fin du tour selon résultat.

#### Victoire
- **Priorité** : Haute  
- **Détails** :
  - Découverte de la totalité des mots de la couleur de son équipe ou découverte par l'équipe adverse du mot noir.
  - Affichage d'un écran de victoire.

---

## 3. Fonctionnalités Complémentaires

### Minuteurs (Sablier)
- **Maîtres-espions** :
  - **Priorité** : Moyenne  
  - **Détails** :
    - Fin automatique du tour après le temps imparti.

- **Agents** :
  - **Priorité** : Moyenne  
  - **Détails** :
    - Fin automatique du tour après le temps imparti.

### Version avec Images
- **Priorité** : Moyenne  
- **Détails** :
  - Utilisation d'images au lieu de mots.
  - Possibilité de sélection de thématiques pour les images;

### Mode Solo
- **Priorité** : Moyenne  
- **Détails** :
  - Joueur unique en agent avec indices préprogrammés.

### Statistiques de Jeu
- **Priorité** : Basse  
- **Détails** :
  - Suivi et affichage des statistiques des parties et des profiles de joueurs enregistrés.

---

## 4. Fonctionnalités Avancées

### Mode Coopératif à Deux Joueurs
- **Priorité** : Basse  
- **Détails** :
  - Mode coopératif inspiré de la version Duo du jeu.

### Jeu en Réseau
- **Priorité** : Basse  
- **Détails** :
  - Jeu en ligne avec :
    - Création et partage de sessions.
    - Choix d'équipe et de rôle.

### Intelligence Artificielle
- **Priorité** : Basse  
- **Détails** :
  - Mode solo amélioré avec IA.

---

## Contraintes Techniques
- Développement en **Java**.
- Architecture modulaire.
- Compatibilité avec navigateurs et tailles d'écran variés.

---

## Livrables
1. Code source documenté.
2. Manuel utilisateur.
3. Rapport de tests.
4. Déploiement sous forme d'exécutable ou d'application web prête à l'emploi.

---

## Lexique
- **Maître-Espion** : joueur donnant des indices.
- **Agent** : joueur devinant les mots.

