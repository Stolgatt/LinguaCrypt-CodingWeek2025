package linguacrypt.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.networking.Message;
import linguacrypt.networking.MessageType;
import linguacrypt.view.DialogBox.EndOfTurnDialog;
import linguacrypt.view.gameView.GameView;
import linguacrypt.view.gameView.GameViewUtils;

public class GameController {
    private Game game;
    private GameView view;
    private ApplicationContext context = ApplicationContext.getInstance();
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

    public void setGame(Game game){
        this.game = game;
    }

    public void nextTurn() {
        int currentTurn = game.getTurn();
        game.setCurrentHint(null);
        game.setTurn((currentTurn + 1) % 2);
        game.setTurnBegin(0);
        game.setNbTour(game.getNbTour() + 1);
        context.broadcastGameUpdate();
        game.notifierObservateurs();
    }

    public void CardClicked(int row, int col) {
        if (game.isTurnBegin()!=2 || (context.getServer() != null && context.getServer().getServerUser().getPlayer().getIsSpy())
                                  || (context.getClient() != null && context.getClient().getUser().getPlayer().getIsSpy())){return;}
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
                    view.getTimerController().stopTimer();
                    EndOfTurnDialog.showEndOfTurnDialog(() -> {
                        game.setCurrentTryCount(0);
                        nextTurn();
                        view.resetTimer();
                    });
                }
                game.increaseTryCounter();
                if (game.getCurrentTryCount() == game.getCurrentNumberWord() +1){
                    view.getTimerController().stopTimer();
                    EndOfTurnDialog.showEndOfTurnDialog(() -> {
                        game.setCurrentTryCount(0);
                        nextTurn();
                        view.resetTimer();
                    });
                }

            }

        }

        
        context.broadcastGameUpdate();
        game.notifierObservateurs();
    }

    public void giveHint(String hint, String count) {
        if (count == null || count.isEmpty()){
            GameViewUtils.showError("Mettez au moins 0.");
            game.setTurnBegin(0);
            game.notifierObservateurs();
            return;
        }
        int number = Integer.parseInt(count);
        if (hint==null || hint.isEmpty()){
            GameViewUtils.showError("Un mot doit être donner.");
            game.setTurnBegin(0);
            game.notifierObservateurs();
            return;
        }
        if (hint.trim().isEmpty() || hint.contains(" ")) {
            GameViewUtils.showError("Le mot doit être unique et sans espaces.");
            game.setTurnBegin(0);
            game.notifierObservateurs();
            return;
        }
        game.setTurnBegin(2);

        game.setCurrentHint(hint);
        game.setCurrentNumberWord(number);

    
        context.broadcastGameUpdate();
        game.notifierObservateurs();
    }

    
}
