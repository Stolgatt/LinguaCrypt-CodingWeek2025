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

public class MainView {

    @FXML
    public void handleCreateGame(ActionEvent event) {
        try {
            // Crée un jeu à partir de la configuration
            GameConfiguration config = GameConfiguration.getInstance();
            Game game = new Game(config);
            GameController gameController = new GameController(game);

            // Charge la vue de la grille
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameView.fxml"));
            Scene gameScene = new Scene(loader.load(), 1000, 1000);

            // Passe le jeu au GameViewController
            GameView gameView = loader.getController();
            gameView.setGame(gameController.getGame());

            // Change la scène
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
