package linguacrypt.controller;

import linguacrypt.model.Game;

public class GameController {
    private Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void nextTurn() {
        int currentTurn = game.getTurn();
        game.setTurn((currentTurn + 1) % 2); 
        System.out.println("Next turn: Team " + (game.getTurn() == 0 ? "Blue" : "Red"));
    }
}
