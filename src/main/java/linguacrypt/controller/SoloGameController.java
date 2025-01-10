package linguacrypt.controller;

import linguacrypt.model.players.AI.AIAgent;
import linguacrypt.model.Game;
import linguacrypt.model.game.Hint;
import linguacrypt.view.DialogBox.EndOfTurnDialog;
import linguacrypt.view.gameView.GameViewUtils;
import linguacrypt.view.gameView.SoloGameView;

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
        if (currentTurnStep == 2) {
            if (!game.getBlueTeam().getPlayers().get(0).getIsSpy()){
                game.spyAIPlay();
                game.increaseNbTour();
            }
            else {
                try {
                    List<String> words = ((AIAgent) game.getBlueTeam().getPlayers().get(1)).tryFindHint(new Hint(game.getCurrentHint(),game.getCurrentNumberWord()), game.getGrid());
                    int id = 0;
                    if (words.isEmpty()) {
                        game.setTurnBegin(0);
                        System.out.println("EHEHEHEHE");
                    }
                    while (game.isTurnBegin()==2 && id<words.size()) {
                        for (int i = 0; i < game.getGrid().getGrid().length; i++) {
                            for (int j = 0; j < game.getGrid().getGrid()[0].length; j++) {
                                if (game.getGrid().getCard(i,j).getWord().equals(words.get(id))){
                                    ((AIAgent) game.getBlueTeam().getPlayers().get(1)).addGuess(words.get(id));
                                    CardClicked(i,j);
                                }
                            }
                        }
                        id++;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                game.setTurnBegin(0);
                game.increaseNbTour();
            }
        }
        game.notifierObservateurs();
    }

    public void CardClicked(int row, int col) {
        if (game.isTurnBegin()!=2){return;}
        if (game.getGrid().getCard(row, col).isSelected()){return;}

        game.flipCard(row,col);
        int color = game.getGrid().getCard(row, col).getCouleur();

        //Noir trouve
        if (game.isWinning()==0){}
        else if (color == 3){
            game.setIsWin(2);
        }
        else {
            //Si on a une couleur différente
            if (1 != color){
                view.getTimerController().stopTimer();
                EndOfTurnDialog.showEndOfTurnDialog(() -> {
                    game.setCurrentTryCount(0);
                    nextTurnStep();
                    view.resetTimer();
                });
            }
            else{
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
        }
        game.isWinning();
        game.notifierObservateurs();
    }

    public void giveHint(String hint, String count) {
        if (count == null || count.isEmpty()){
            GameViewUtils.showError("Mettez au moins 0.");
            game.setTurnBegin(0);
            game.notifierObservateurs();
            return;
        }
        if (!count.matches("\\d+")) {
            GameViewUtils.showError("Veuillez entrer uniquement des chiffres.");
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
        if (!game.getGrid().isWordValid(hint)){
            GameViewUtils.showError("Le mot ne doit pas être ou contenir un mot de la grille.");
            game.setTurnBegin(0);
            game.notifierObservateurs();
            return;
        }
        ((AIAgent) game.getBlueTeam().getPlayers().get(1)).clearLastGuess();
        game.setTurnBegin(2);
        game.setCurrentHint(hint);
        game.setCurrentNumberWord(number);
        game.notifierObservateurs();
        nextTurnStep();
    }
}
