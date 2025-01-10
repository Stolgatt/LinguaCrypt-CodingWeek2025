package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import linguacrypt.ApplicationContext;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.game.Theme;
import linguacrypt.utils.ThemeLoader;

import java.util.ArrayList;
import java.util.List;

public class EditThemeView {
    @FXML
    private TextField themeNameField;
    @FXML
    private TextArea wordsTextArea;

    @FXML
    private ComboBox<String> existingThemesComboBox;


    private ApplicationContext context = ApplicationContext.getInstance();

    @FXML
    private void initialize() {
        loadExistingThemes();

        existingThemesComboBox.setOnAction(e -> loadWordsForSelectedTheme());
    }

    @FXML
    private void save(){
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
    }

    @FXML
    private void goBackToMainMenu() {
        context.getRoot().setCenter(context.getMainMenuNode());
    }
    public void loadExistingThemes() {
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
}
