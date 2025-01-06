# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), 
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.1.3] - 2025-01-06

### Added
- **Save/Load Feature**:
  - Added menu bar with save, load, and exit functionality
  - Implemented game state serialization
  - Added GameUtils class for handling save/load operations
  - Proper handling of observers during serialization

### Changed
- Updated GameView to include menu bar component
- Improved error handling for save/load operations

---

## [0.1.2] - 2025-01-06

### Added
- **Game Logic**:
  - Implemented turn-based gameplay system
  - Added spy hint system with word and number input
  - Added win/lose condition detection
  - Implemented card flipping mechanics
- **UI Features**:
  - Dynamic background color changes based on team turns
  - Card color reveal system (blue, red, neutral, assassin)
  - Added hint display system
  - Implemented winning and losing dialogue boxes
- **Game State Management**:
  - Added observer pattern for game state updates
  - Implemented turn counter and try counter

### Changed
- Enhanced Grid system to support card colors and selection states
- Updated GameController to handle game logic and state changes

---

## [0.1.1] - 2025-01-06

### Added
- **Game Configuration**:
  - Added GameConfiguration singleton class
  - Implemented configuration dialog with customizable settings:
    - AI difficulty level
    - Number of players
    - Team size
    - Grid size
    - Theme selection
    - Turn timer
  - Added input validation and error handling
- **Main Menu**:
  - Added MainMenuController for navigation
  - Created main.fxml with "Create Game" and "Exit" buttons
- **Game View**:
  - Added game_view.fxml for dynamic grid display
  - Implemented GameViewController with GridPane
- **Game Integration**:
  - Connected MainMenuController to game initialization
  - Added GameController for game logic

### Changed
- Refactored FXML controllers to follow MVC pattern
- Updated Grid and Card for dynamic UI integration

### Fixed
- Resolved FXML file path issues
- Fixed configuration validation edge cases

---

## [0.1.0] - 2025-01-06

### Added
- **Initial Setup**:
  - Basic JavaFX setup with Main class
  - Initial Grid and Card model classes
  - Basic game board initialization
  - Static grid display functionality
 