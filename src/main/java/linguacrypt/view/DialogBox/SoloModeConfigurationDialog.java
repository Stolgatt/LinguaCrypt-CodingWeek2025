package linguacrypt.view.DialogBox;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.players.Player;
import linguacrypt.model.game.Theme;
import linguacrypt.model.statistique.PlayerStat;
import linguacrypt.utils.ThemeLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoloModeConfigurationDialog {

    private ComboBox<String> themeComboBox;
    private Game game;
    ApplicationContext context = ApplicationContext.getInstance();

    public boolean showSoloGameConfigurationDialog() {
        // Retrieve the current configuration instance
        GameConfiguration config = GameConfiguration.getInstance();

        while (true) {
            // Create input fields for configuration parameters
            TextField difficultyField = new TextField(String.valueOf(config.getDifficultyLevel()));
            TextField gridSizeField = new TextField(String.valueOf(config.getGridSize()));
            TextField timeTurnField = new TextField(String.valueOf(config.getTimeTurn()));
            TextField nameField = new TextField();

            ToggleGroup roleGroup = new ToggleGroup();
            RadioButton spyRadioButton = new RadioButton("Rôle Espion");
            spyRadioButton.setToggleGroup(roleGroup);
            spyRadioButton.setSelected(true); // Default = Spy
            RadioButton agentRadioButton = new RadioButton("Rôle Agent");
            agentRadioButton.setToggleGroup(roleGroup);

            // Create a grid layout to organize input fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Add labels and input fields to the grid
            grid.add(new Label("Level of the AI (" + config.getMinDiffLevel() + " - " + config.getMaxDiffLevel() + "):"), 0, 0);
            grid.add(difficultyField, 1, 0);
            grid.add(new Label("Grid Size:"), 0, 1);
            grid.add(gridSizeField, 1, 1);
            grid.add(new Label("Time per Turn (s) [-1 for infinite]:"), 0, 2);
            grid.add(timeTurnField, 1, 2);
            grid.add(new Label("Nom du joueur:"), 0, 3);
            grid.add(nameField, 1, 3);
            grid.add(new Label("Choisir un rôle:"), 0, 4);
            grid.add(spyRadioButton, 1, 4);
            grid.add(agentRadioButton, 2, 4);

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
                int gridSize = Integer.parseInt(gridSizeField.getText());
                int timeTurn = Integer.parseInt(timeTurnField.getText());
                String name = nameField.getText();
                boolean spy = spyRadioButton.isSelected();
                if (difficulty < config.getMinDiffLevel() || difficulty > config.getMaxDiffLevel()) {
                    throw new IllegalArgumentException("AI level must be between " + config.getMinDiffLevel() + " and " + config.getMaxDiffLevel());
                }
                if (gridSize < 5 || gridSize > config.getMaxGridSize()) {
                    throw new IllegalArgumentException("Grid size must be between 5 and " + config.getMaxGridSize() +".");
                }
                if (timeTurn != -1 && (timeTurn < 0 || timeTurn > config.getMaxTimeTurn())) {
                    throw new IllegalArgumentException("Time per turn must be -1 or a positive value <= " + config.getMaxTimeTurn());
                }

                // Save the validated input values into the configuration instance
                config.setDifficultyLevel(difficulty);
                config.setGridSize(gridSize);
                config.setWordTheme(themeComboBox.getValue());
                config.setTimeTurn(timeTurn);
                game = new Game(config);
                tryAddPlayer(name,spy);
                context.setGame(game);


                // Show a success alert if the values are saved correctly
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Configuration saved successfully!");
                successAlert.showAndWait();

                // Exit the loop after successful configuration
                break;

            } catch (IllegalArgumentException | IOException | ClassNotFoundException e) {
                // Show an error alert if the input values are invalid
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
        return true;
    }
    public void tryAddPlayer(String playerName,boolean isSpy) throws IOException, ClassNotFoundException {
        ArrayList<Player> playerList = game.getgConfig().getPlayerList();
        if (playerList == null){
            playerList = new ArrayList<>();
        }
        boolean find = false;
        for (Player p : playerList){
            if (p.getName().equals(playerName)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Profil existant");
                alert.setHeaderText("Le joueur existe déjà !");
                alert.setContentText("Un joueur portant ce nom existe déjà. Voulez-vous utiliser ce profil existant ?");

                ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        p.setIsSpy(isSpy);
                        game.setUpSoloGame(p);
                    }
                });
                find = true;
                break;
            }
        }
        if (!find) {
            Player newPlayer = new Player(playerName,isSpy,"",new PlayerStat());
            game.setUpSoloGame(newPlayer);
        }
    }

}

