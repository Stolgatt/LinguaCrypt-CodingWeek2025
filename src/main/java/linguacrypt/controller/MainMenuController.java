package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public void handleCreateGame(ActionEvent event) {
        try {



            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameView.fxml"));
            Scene gameScene = new Scene(loader.load(), 1000, 1000);

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
