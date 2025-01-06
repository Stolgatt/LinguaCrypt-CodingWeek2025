package linguacrypt.controller;

import linguacrypt.model.Game;
import linguacrypt.utils.GameUtils;
import java.io.IOException;

public class MenuBarController {
    private Game game;

    public MenuBarController(Game game) {
        this.game = game;
    }

    public void saveGame() {
        try {
            System.out.println("Attempting to save game...");
            GameUtils.saveGame(game);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try {
            System.out.println("Attempting to load game...");
            Game loadedGame = GameUtils.loadGame();
            this.game.setGrid(loadedGame.getGrid());
            this.game.setTurn(loadedGame.getTurn());
            this.game.setCurrentHint(loadedGame.getCurrentHint());
            this.game.setCurrentNumberWord(loadedGame.getCurrentNumberWord());
            this.game.setTurnBegin(loadedGame.isTurnBegin());
            this.game.setIsWin(loadedGame.getIsWin());
            this.game.notifierObservateurs();
            System.out.println("Game loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Game getGame() {
        return game;
    }

    public void exit() {
        System.exit(0);
    }
} 