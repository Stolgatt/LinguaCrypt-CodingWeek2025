package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.GameConfigurationDialog;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public void handleCreateGame(ActionEvent event) throws IOException {
        GameConfiguration config = GameConfiguration.getInstance();
        GameConfigurationDialog dialog = new GameConfigurationDialog();
        if (dialog.showGameConfigurationDialog()) {
            config.setTheme(dialog.getSelectedTheme());
            Game game = new Game(config);
            // Load the game view...
        }
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
