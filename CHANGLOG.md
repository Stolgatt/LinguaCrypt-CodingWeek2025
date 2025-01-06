# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), 
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.2.0] - 2025-01-06

### Added
- **Main Menu**:
  - Added `MainMenuController` to handle navigation.
  - Created `main.fxml` for the main menu, including "Create Game" and "Exit" buttons.
- **Game View**:
  - Added `game_view.fxml` for displaying the game grid dynamically.
  - Implemented `GameViewController` to render the grid using `GridPane`.
- **Game Integration**:
  - Connected `MainMenuController` to initialize the game and navigate to the game view.
  - Added separation of concerns by introducing `GameController` to handle game logic independently from the UI.
  
### Changed
- Refactored existing FXML controllers to adhere to MVC principles.
- Updated `Grid` and `Card` to work seamlessly with the dynamic UI setup.

### Fixed
- Resolved potential issues with pathing for FXML files.

---

## [0.1.0] - 2025-01-05

### Added
- **Initial Setup**:
  - Basic JavaFX setup with `Main` class and initial `main.fxml` layout.
  - Added initial `Grid` and `Card` model classes for handling the game board and individual cards.
  - Created the basic game logic with the `Game` class to initialize and print a static grid.
