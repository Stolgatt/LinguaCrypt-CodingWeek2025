package linguacrypt.view;

import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

/**
 * Main menu view controller for handling main menu actions.
 */
public class MainMenuView {

    private Consumer<ActionEvent> onCreateGame;

    @FXML
    private Button createGameButton; // Button to create a new game
    @FXML
    private Button addCustomThemeButton; // Button to add custom theme words
    @FXML
    private Button exitButton; // Button to exit

    /**
     * Initializes the main menu view and sets up button actions.
     */
    @FXML
    public void initialize() {
        createGameButton.setOnAction(e -> {onCreateGame.accept(e);});
        addHoverEffect(createGameButton);
        addHoverEffect(addCustomThemeButton);
        addHoverEffect(exitButton);
        addSelectedEffect(createGameButton);
        addSelectedEffect(addCustomThemeButton);
        addSelectedEffect(exitButton);
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

    private void addHoverEffect(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        button.setOnMouseEntered((MouseEvent e) -> {
            scaleTransition.setFromX(1.0);
            scaleTransition.setFromY(1.0);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        button.setOnMouseExited((MouseEvent e) -> {
            scaleTransition.setFromX(1.1);
            scaleTransition.setFromY(1.1);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    private void addSelectedEffect(Button button) {
        button.setOnMousePressed((MouseEvent e) -> {
            button.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 1); -fx-pref-width: 200;");
        });

        button.setOnMouseReleased((MouseEvent e) -> {
            button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 1); -fx-pref-width: 200;");
        });
    }
}
