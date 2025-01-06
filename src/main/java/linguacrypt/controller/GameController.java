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
        view.setonCardClicked(this::CardClicked);
        view.setOnGiveHint(this::giveHint);
    }

    public Game getGame() {
        return game;
    }

    public void nextTurn() {
        int currentTurn = game.getTurn();
        game.setTurn((currentTurn + 1) % 2);
        game.setTurnBegin(0);
        game.notifierObservateurs();
    }

    public void CardClicked(int row, int col) {
        if (game.isTurnBegin()!=2){return;}
        if (game.getGrid().getCard(row, col).isSelected()){return;}
        game.flipCard(row,col);

        if (game.isWinning() == -1){
            int turn = game.getTurn();
            int color = game.getGrid().getCard(row, col).getCouleur();
            if (color == 3){
                game.setIsWin(2);
            }
            else {
                if (turn + 1 != color){
                    nextTurn();
                }
                game.increaseTryCounter();
                if (game.getCurrentTryCount() == game.getCurrentNumberWord() +1){
                    nextTurn();
                    game.setCurrentTryCount(0);
                }
            }

        }

        game.notifierObservateurs();
    }

    public void giveHint(){
        game.setTurnBegin(1);
        game.notifierObservateurs();
    }
}
