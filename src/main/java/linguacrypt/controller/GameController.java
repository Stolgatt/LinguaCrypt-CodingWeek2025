package linguacrypt.controller;

import linguacrypt.model.Game;
import linguacrypt.view.GameView;

public class GameController {
    private Game game;
    private GameView view;
    public GameController(Game game,GameView view) {
        this.game = game;
        setView(view);
    }

    public void setView(GameView view) {
        this.view = view;

        view.setOnNextTurn(this::nextTurn);
    }

    public Game getGame() {
        return game;
    }

    public void nextTurn() {
        int currentTurn = game.getTurn();
        game.setTurn((currentTurn + 1) % 2);
        game.setTurnBegin(true);
        game.notifierObservateurs();
    }
}
