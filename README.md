# LinguaCrypt

LinguaCrypt is a Java-based implementation of the Codenames board game, built using JavaFX and following the MVC (Model-View-Controller) architectural pattern.

## Prerequisites
- Java 17 or higher
- Gradle 7.0 or higher
- JavaFX (included in build)

## Quick Start
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Build the project:
   ```bash
   gradle build
   ```
3. Run the application:
   ```bash
   gradle run
   ```

## Project Architecture

### MVC Pattern Implementation
The project follows a strict Model-View-Controller pattern:

#### Model (`src/main/java/linguacrypt/model/`)
- `Game`: Core game logic and state management
- `Grid`: Game board representation and card management
- `Card`: Individual card properties and state
- `GameConfiguration`: Singleton for game settings

#### View (`src/main/java/linguacrypt/view/`)
- `MainMenuView`: Entry point UI
- `GameView`: Main game interface
- `MenuBarView`: Game menu interface
- FXML files in `src/main/resources/FXML/`

#### Controller (`src/main/java/linguacrypt/controller/`)
- `GameController`: Game logic and event handling
- `MainMenuController`: Menu navigation
- `MenuBarController`: Menu actions (save/load)

### Key Components

#### Game Configuration
- Singleton pattern implementation
- Configurable parameters:
  - Grid size
  - Difficulty level
  - Number of players
  - Turn timer
  - Theme selection

#### Observer Pattern
- Used for real-time UI updates
- Implementation:
  - `Observer` interface
  - Game state notifications
  - View updates

#### Serialization
- Game state persistence
- Save/Load functionality
- Managed through `GameUtils`

## Game Flow

1. **Initialization**
   - Application starts at `Main.java`
   - Loads main menu via `MainMenuView`
   - Configuration through `GameConfigurationDialog`

2. **Game Setup**
   - Grid initialization
   - Card distribution
   - Team assignment

3. **Gameplay**
   - Turn-based mechanics
   - Spy master hints
   - Card selection
   - Win condition checking

4. **State Management**
   - Save game capability
   - Load game functionality
   - State persistence

## Game Features
- Configurable game settings (grid size, difficulty, players)
- Turn-based gameplay
- Save/Load game state
- Team-based play (Blue vs Red)
- Spy master hint system

## Project Structure
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

## Development

### Building
```bash
gradle clean build
```

### Running Tests
```bash
gradle test
```

### Running the Application
```bash
gradle run
```

## Version History
See [CHANGELOG.md](CHANGELOG.md) for version details.