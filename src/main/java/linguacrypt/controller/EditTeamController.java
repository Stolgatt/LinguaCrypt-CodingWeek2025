package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.view.EditTeamView;
import linguacrypt.view.GameView;

import java.io.IOException;

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

        //view.setOnAddPlayer(this::addPlayer);
        view.setOnStartGame(this::startGame);
    }
    public Game getGame() {
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    /*
    public void addPlayer(Player player,int teamId) {
        if (teamId == 0){
            game.getBlueTeam().addPlayer(player);
        }
        else{
            game.getRedTeam().addPlayer(player);
        }
    }
     */
    //@FXML
    public void startGame(ActionEvent event) {
        // Switch to the game scene
        context.getRoot().setCenter(context.getGameNode());
    }
}
