package linguacrypt.view;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


/**
 * Main menu view controller for handling main menu actions.
 */
public class MainMenuView {

    private Consumer<ActionEvent> onCreateGame;

    @FXML
    private Button createGameButton; // Button to create a new game

    /**
     * Initializes the main menu view and sets up button actions.
     */
    @FXML
    public void initialize() {
        createGameButton.setOnAction(e -> {onCreateGame.accept(e);});
    }

    public void setOnCreateGame(Consumer<ActionEvent> onCreateGame){
        this.onCreateGame = onCreateGame;
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
