package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.GameConfigurationDialog;
import linguacrypt.view.EditTeamView;
import linguacrypt.view.MainMenuView;

import java.io.IOException;

public class MainMenuController {

    MainMenuView view;

    public void setView(MainMenuView menuView){
        this.view = menuView;
        view.setOnCreateGame(this::handleCreateGame);
    }

    public void handleCreateGame(ActionEvent event) {
        try {
            // Retrieve game configuration and personalize settings
            GameConfiguration config = GameConfiguration.getInstance();
            GameConfigurationDialog dialog = new GameConfigurationDialog();
            if (dialog.showGameConfigurationDialog()) {
                config.setTheme(dialog.getSelectedTheme());
                Game game = new Game(config);

                // Load the game view from FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/editTeam.fxml"));
                Scene gameScene = new Scene(loader.load(), 1000, 1000);

                // Set the game instance in the game view controller
                EditTeamView editTeamView = loader.getController();

                EditTeamController editTeamController = new EditTeamController(game, editTeamView);
                editTeamView.setGame(editTeamController.getGame());

                // Switch to the game scene
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(gameScene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
