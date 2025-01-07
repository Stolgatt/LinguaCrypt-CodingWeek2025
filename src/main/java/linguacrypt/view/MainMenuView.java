package linguacrypt.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import linguacrypt.controller.GameController;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;

import java.io.IOException;

/**
 * Main menu view controller for handling main menu actions.
 */
public class MainMenuView {

    @FXML
    private Button createGameButton; // Button to create a new game

    /**
     * Initializes the main menu view and sets up button actions.
     */
    @FXML
    public void initialize() {
        createGameButton.setOnAction(this::handleCreateGame);
    }

    /**
     * Handles the action event when the "Create Game" button is clicked.
     * Loads the game configuration, initializes the game, and switches to the game view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void handleCreateGame(ActionEvent event) {
        try {
            // Retrieve game configuration and personalize settings
            GameConfiguration config = GameConfiguration.getInstance();
            if (GameConfigurationDialog.showGameConfigurationDialog()){
                // Initialize Game with GameConfiguration's settings
                Game game = new Game(config);


                // Load the game view from FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameView.fxml"));
                Scene gameScene = new Scene(loader.load(), 1000, 1000);

                // Set the game instance in the game view controller
                GameView gameView = loader.getController();

                GameController gameController = new GameController(game, gameView);
                gameView.setGame(gameController.getGame());

                // Switch to the game scene
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(gameScene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action event when the "Exit" button is clicked.
     * Exits the application.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
