package linguacrypt.view;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.application.Platform;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Theme;

import java.util.Optional;
import java.util.List;
import linguacrypt.utils.ThemeLoader;

/**
 * This class provides a dialog box for configuring game settings.
 * Users can customize parameters such as difficulty level, grid size, theme, and time per turn.
 */
public class GameConfigurationDialog {

    private ComboBox<String> themeComboBox;

    /**
     * Displays a dialog box to configure game settings.
     * The dialog pre-fills fields with default values from the GameConfiguration instance.
     * Users can edit the values, and the configuration is updated upon confirmation.
     */
    public boolean showGameConfigurationDialog() {
        // Retrieve the current configuration instance
        GameConfiguration config = GameConfiguration.getInstance();

        while (true) {
            // Create input fields for configuration parameters
            TextField difficultyField = new TextField(String.valueOf(config.getDifficultyLevel()));
            TextField maxTeamField = new TextField(String.valueOf(config.getMaxTeamMember()));
            TextField gridSizeField = new TextField(String.valueOf(config.getGridSize()));
            TextField nbPlayerField = new TextField(String.valueOf(config.getNbPlayer()));
            TextField timeTurnField = new TextField(String.valueOf(config.getTimeTurn()));

            // Create a grid layout to organize input fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Add labels and input fields to the grid
            grid.add(new Label("Level of the AI (" + config.getMinDiffLevel() + " - " + config.getMaxDiffLevel() + "):"), 0, 0);
            grid.add(difficultyField, 1, 0);
            grid.add(new Label("Number of Players:"), 0, 1);
            grid.add(nbPlayerField, 1, 1);
            grid.add(new Label("Max Team Members:"), 0, 2);
            grid.add(maxTeamField, 1, 2);
            grid.add(new Label("Grid Size:"), 0, 3);
            grid.add(gridSizeField, 1, 3);
            grid.add(new Label("Time per Turn (s) [-1 for infinite]:"), 0, 4);
            grid.add(timeTurnField, 1, 4);

            // Create a ComboBox for themes
            themeComboBox = new ComboBox<>();
            List<Theme> themes = ThemeLoader.loadThemes(0);
            for (Theme theme : themes) {
                themeComboBox.getItems().add(theme.getName());
                themeComboBox.getSelectionModel().selectFirst();
            }

            // Add ComboBox to the dialog layout
            grid.add(new Label("Select Theme:"), 0, 5);
            grid.add(themeComboBox, 1, 5);

            // Create a dialog box for input
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Game Configuration");
            dialog.setHeaderText("Please configure the game settings");
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Automatically focus the first input field
            Platform.runLater(difficultyField::requestFocus);

            // Handle user input from the dialog box
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                // If the user clicks 'Cancel', exit the loop and return to the menu
                return false;
            }

            try {
                // Validate input values
                int difficulty = Integer.parseInt(difficultyField.getText());
                int nbPlayers = Integer.parseInt(nbPlayerField.getText());
                int maxTeam = Integer.parseInt(maxTeamField.getText());
                int gridSize = Integer.parseInt(gridSizeField.getText());
                int timeTurn = Integer.parseInt(timeTurnField.getText());

                if (difficulty < config.getMinDiffLevel() || difficulty > config.getMaxDiffLevel()) {
                    throw new IllegalArgumentException("AI level must be between " + config.getMinDiffLevel() + " and " + config.getMaxDiffLevel());
                }
                if (nbPlayers < 4) {
                    throw new IllegalArgumentException("Number of players must be at least 4.");
                }
                int playerPerTeam = (nbPlayers-1)/2 +1;
                if (maxTeam < playerPerTeam || maxTeam >= (nbPlayers - 2)) {
                    throw new IllegalArgumentException("Max team members must be between " + playerPerTeam +" and " + (nbPlayers - 2));
                }
                if (gridSize < 5 || gridSize > config.getMaxGridSize()) {
                    throw new IllegalArgumentException("Grid size must be between 5 and " + config.getMaxGridSize() +".");
                }
                if (timeTurn != -1 && (timeTurn < 0 || timeTurn > config.getMaxTimeTurn())) {
                    throw new IllegalArgumentException("Time per turn must be -1 or a positive value <= " + config.getMaxTimeTurn());
                }

                // Save the validated input values into the configuration instance
                config.setDifficultyLevel(difficulty);
                config.setMaxTeamMember(maxTeam);
                config.setGridSize(gridSize);
                config.setWordTheme(themeComboBox.getValue());
                config.setNbPlayer(nbPlayers);
                config.setTimeTurn(timeTurn);

                // Show a success alert if the values are saved correctly
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Configuration saved successfully!");
                successAlert.showAndWait();

                // Exit the loop after successful configuration
                break;

            } catch (IllegalArgumentException e) {
                // Show an error alert if the input values are invalid
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
        return true;
    }

    // After user confirms, set the selected theme in the configuration
    public String getSelectedTheme() {
        return themeComboBox.getValue();
    }
}
