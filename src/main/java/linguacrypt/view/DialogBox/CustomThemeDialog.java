package linguacrypt.view.DialogBox;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.game.Theme;
import linguacrypt.utils.ThemeLoader;

import java.util.List;
import java.util.ArrayList;

public class CustomThemeDialog {
    private TextField themeNameField;
    private TextArea wordsTextArea;
    private ComboBox<String> existingThemesComboBox;

    public CustomThemeDialog() {        
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Custom Theme");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        existingThemesComboBox = new ComboBox<>();
        loadExistingThemes();

        // Add listener to load words when a theme is selected
        existingThemesComboBox.setOnAction(e -> loadWordsForSelectedTheme());

        themeNameField = new TextField();
        themeNameField.setPromptText("Enter new theme name (leave empty to modify existing)");

        wordsTextArea = new TextArea();
        wordsTextArea.setPromptText("Enter words separated by commas");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveTheme());

        layout.getChildren().addAll(
                new Label("Select Existing Theme:"),
                existingThemesComboBox,
                new Label("New Theme Name:"),
                themeNameField,
                new Label("Words (comma separated):"),
                wordsTextArea,
                saveButton
        );

        Scene scene = new Scene(layout, 400, 300);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void loadExistingThemes() {
        existingThemesComboBox.getItems().clear();
        List<Theme> themes = ThemeLoader.loadThemes(GameConfiguration.getInstance().getGameMode());
        for (Theme theme : themes) {
            existingThemesComboBox.getItems().add(theme.getName());
        }
    }

    private void loadWordsForSelectedTheme() {
        String selectedTheme = existingThemesComboBox.getValue();
        if (selectedTheme != null) {
            Theme theme = ThemeLoader.getThemeByName(selectedTheme, GameConfiguration.getInstance().getGameMode());
            if (theme != null) {
                // Load existing words into the text area
                wordsTextArea.setText(String.join(", ", theme.getWords()));
                themeNameField.setText(""); // Clear the new theme name field
            } else {
                // Clear the text area if the theme is not found
                wordsTextArea.clear();
            }
        } else {
            // Clear the text area if no theme is selected
            wordsTextArea.clear();
        }
    }

    private void saveTheme() {
        String selectedTheme = existingThemesComboBox.getValue();
        String newThemeName = themeNameField.getText().trim();
        String words = wordsTextArea.getText().trim();

        if (!newThemeName.isEmpty()) {
            // Create a new theme
            Theme newTheme = new Theme();
            newTheme.setName(newThemeName);
            newTheme.setWords(List.of(words.split(",")));
            ThemeLoader.addTheme(newTheme, GameConfiguration.getInstance().getGameMode());
        } else if (selectedTheme != null) {
            // Modify existing theme
            Theme existingTheme = ThemeLoader.getThemeByName(selectedTheme, GameConfiguration.getInstance().getGameMode());
            List<String> existingWords = existingTheme.getWords();

            // Create a new list for updated words
            List<String> updatedWords = new ArrayList<>();

            // Split the input words and trim them
            String[] newWordsArray = words.split(",");
            for (String newWord : newWordsArray) {
                String trimmedWord = newWord.trim();
                if (!trimmedWord.isEmpty()) {
                    updatedWords.add(trimmedWord); // Add new word if it's not empty
                }
            }

            // Update the existing words list
            existingWords.clear(); // Clear the existing words
            existingWords.addAll(updatedWords); // Add the updated words

            existingTheme.setWords(existingWords);
            ThemeLoader.updateTheme(existingTheme, GameConfiguration.getInstance().getGameMode());
        }

        // Reload the themes to reflect the changes
        loadExistingThemes();

        // Close the dialog
        ((Stage) themeNameField.getScene().getWindow()).close();
    }
}