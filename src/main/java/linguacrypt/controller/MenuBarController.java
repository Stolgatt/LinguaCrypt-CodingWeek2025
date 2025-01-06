package linguacrypt.controller;

import linguacrypt.model.Game;

public class MenuBarController {
    private Game game;

    public MenuBarController(Game game) {
        this.game = game;
    }

    public void saveGame() {
        // TODO: Implement save game functionality
        System.out.println("Saving game...");
    }

    public void loadGame() {
        // TODO: Implement load game functionality
        System.out.println("Loading game...");
    }

    public void exit() {
        System.exit(0);
    }
} 