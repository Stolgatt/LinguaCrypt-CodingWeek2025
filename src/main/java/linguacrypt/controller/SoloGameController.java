package linguacrypt.controller;

import linguacrypt.model.AI.AIAgent;
import linguacrypt.model.AI.AISpy;
import linguacrypt.model.Game;
import linguacrypt.model.Hint;
import linguacrypt.view.EndOfTurnDialog;
import linguacrypt.view.GameView;
import linguacrypt.view.SoloGameView;

import java.io.IOException;
import java.util.List;

public class SoloGameController {
    private Game game;
    private SoloGameView view;

    public SoloGameController(Game game,SoloGameView view) {
        this.game = game;
        setView(view);
    }
    public void setView(SoloGameView view) {
        this.view = view;

        view.setOnNextTurn(this::nextTurnStep);
        view.setonCardClicked(this::CardClicked);
        view.setOnGiveHint(this::giveHint);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void nextTurnStep(){
        int currentTurnStep = game.isTurnBegin();
        System.out.println("Nouvelle etape " + currentTurnStep);
        if (currentTurnStep == 2) {
            if (!game.getBlueTeam().getPlayers().get(0).getIsSpy()){
               game.spyAIPlay();
            }
            else {
                System.out.println("AIAgent Play");
                try {
                    List<String> words = ((AIAgent) game.getBlueTeam().getPlayers().get(1)).tryFindHint(new Hint(game.getCurrentHint(),game.getCurrentNumberWord()), game.getGrid());
                    System.out.println("L'agent pense a " +words);
                    int id = 0;
                    if (words.isEmpty()) {
                        System.out.println("N'a pas trouve de mot....");
                        game.setTurnBegin(0);
                    }
                    while (game.isTurnBegin()==2 && id<words.size()) {
                        for (int i = 0; i < game.getGrid().getGrid().length; i++) {
                            for (int j = 0; j < game.getGrid().getGrid()[0].length; j++) {
                                if (game.getGrid().getCard(i,j).getWord().equals(words.get(id))){
                                    CardClicked(i,j);
                                    System.out.println("AI Clicked on " +game.getGrid().getCard(i,j).getWord());
                                }
                            }
                        }
                        id++;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                game.setTurnBegin(0);
            }
        }
        game.notifierObservateurs();
    }

    public void CardClicked(int row, int col) {
        if (game.isTurnBegin()!=2){return;}
        if (game.getGrid().getCard(row, col).isSelected()){return;}
        game.flipCard(row,col);

        //if (game.isWinning() == -1){
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
                        nextTurnStep();
                        view.resetTimer();
                    });
                }
                game.increaseTryCounter();

                if (game.getCurrentTryCount() == game.getCurrentNumberWord() +1){
                    view.getTimerController().stopTimer();
                    EndOfTurnDialog.showEndOfTurnDialog(() -> {
                        game.setCurrentTryCount(0);
                        nextTurnStep();
                        view.resetTimer();
                    });
                }

            }

        //}

        game.notifierObservateurs();
    }

    public void giveHint(){
        game.setTurnBegin(1);
        game.notifierObservateurs();
    }
}
