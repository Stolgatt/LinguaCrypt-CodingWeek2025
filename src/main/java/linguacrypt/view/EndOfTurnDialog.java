package linguacrypt.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * This class is responsible for displaying a dialog to indicate the end of a turn.
 * The next turn will only start once the player confirms by clicking 'OK'.
 */
public class EndOfTurnDialog {

    /**
     * Displays an alert indicating that the turn has ended.
     * The callback provided will be executed when the user confirms by clicking 'OK'.
     *
     * @param onConfirmed The callback to execute when the user clicks 'OK'. This is typically used to start the next turn.
     */
    public static void showEndOfTurnDialog(Runnable onConfirmed) {
        // Use Platform.runLater to ensure the dialog is shown after any ongoing animations
        Platform.runLater(() -> {
            // Create an informational alert dialog to notify the user that the turn is finished
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("End of Turn");
            alert.setHeaderText(null); // No subtitle, only a message
            alert.setContentText("The turn has ended! Click 'OK' when you are ready to start the next turn.");

            // Add buttons (OK and Cancel)
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            // Display the dialog and wait asynchronously for the user's response
            Optional<ButtonType> result = alert.showAndWait();

            // If the user clicks 'OK', execute the provided callback
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (onConfirmed != null) { // Ensure the callback is not null
                    onConfirmed.run(); // Execute the callback after confirmation
                }
            }
        });
    }
}
