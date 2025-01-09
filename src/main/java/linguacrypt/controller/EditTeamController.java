package linguacrypt.controller;

import javafx.event.ActionEvent;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.view.EditTeamView;

public class EditTeamController {
    private Game game;
    private EditTeamView view;
    ApplicationContext context =ApplicationContext.getInstance();

    public EditTeamController(Game game,EditTeamView view) {
        this.game = game;
        setView(view);
    }

    public void setView(EditTeamView view) {
        this.view = view;
        view.setOnStartGame(this::startGame);
    }
    public Game getGame() {
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void startGame(ActionEvent event) {
        // Switch to the game scene
        context.getRoot().setCenter(context.getGameNode());
        game.setStartTime(System.currentTimeMillis());
        game.setNbTour(1);
        game.notifierObservateurs();
    }
}
