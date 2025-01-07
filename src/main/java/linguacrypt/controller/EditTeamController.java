package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.view.EditTeamView;
import linguacrypt.view.GameView;

import java.io.IOException;

public class EditTeamController {
    private Game game;
    private EditTeamView view;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameView.fxml"));
            Scene gameScene = new Scene(loader.load(), 1000, 1000);

            // Set the game instance in the game view controller
            GameView gameView = loader.getController();

            GameController gameController = new GameController(game, gameView);
            gameView.setGame(gameController.getGame());

            // Switch to the game scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(gameScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
