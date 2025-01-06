package linguacrypt.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.controller.GameController;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;

import java.io.IOException;

public class MainMenuView {

    @FXML
    public void handleCreateGame(ActionEvent event) {
        try {
            GameConfiguration config = GameConfiguration.getInstance();
            Game game = new Game(config);
            GameController gameController = new GameController(game);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameView.fxml"));
            Scene gameScene = new Scene(loader.load(), 1000, 1000);


            GameView gameView = loader.getController();
            gameView.setGame(gameController.getGame());

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(gameScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
