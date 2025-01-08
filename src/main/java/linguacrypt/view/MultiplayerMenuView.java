package linguacrypt.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Theme;
import linguacrypt.utils.ThemeLoader;

public class MultiplayerMenuView {

    @FXML
    private VBox hostView, joinView;

    @FXML
    private TextField textFieldNicknameHost, textFieldNicknameJoin, textFieldAddress;

    @FXML
    private Label labelTeamSize, labelGridSize, labelTimer;

    @FXML
    private ComboBox<String> comboBoxTheme;

    private int teamSize = 4;
    private int gridSize = 5;
    private int timer = 60;

    private ApplicationContext context = ApplicationContext.getInstance();

    @FXML
    public void initialize() {
        // Initialize the labels
        labelTeamSize.setText(String.valueOf(teamSize));
        labelGridSize.setText(String.valueOf(gridSize));
        labelTimer.setText(String.valueOf(timer));

        List<Theme> themes = ThemeLoader.loadThemes();
            for (Theme theme : themes) {
                comboBoxTheme.getItems().add(theme.getName());
                comboBoxTheme.getSelectionModel().selectFirst();
            }
    }

    @FXML
    public void showHostView() {
        context.getRoot().setCenter(context.getMPMenuNode());
        hostView.setVisible(true);
        joinView.setVisible(false);
    }

    @FXML
    public void showJoinView() {
        context.getRoot().setCenter(context.getMPMenuNode());
        joinView.setVisible(true);
        hostView.setVisible(false);
    }

    @FXML
    private void decreaseTeamSize() {
        if (teamSize > 2) { // Minimum team size
            teamSize--;
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void increaseTeamSize() {
        if (teamSize < 10) { // Maximum team size
            teamSize++;
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void decreaseGridSize() {
        if (gridSize > 3) { // Minimum grid size
            gridSize--;
            labelGridSize.setText(String.valueOf(gridSize));
        }
    }

    @FXML
    private void increaseGridSize() {
        if (gridSize < 15) { // Maximum grid size
            gridSize++;
            labelGridSize.setText(String.valueOf(gridSize));
        }
    }

    @FXML
    private void decreaseTimer() {
        if (timer > 10) { // Minimum timer
            timer -= 10;
            labelTimer.setText(String.valueOf(timer));
        }
    }

    @FXML
    private void increaseTimer() {
        if (timer < 120) { // Maximum timer
            timer += 10;
            labelTimer.setText(String.valueOf(timer));
        }
    }

    @FXML
    private void createRoom() {
        String nickname = textFieldNicknameHost.getText();
        String selectedTheme = comboBoxTheme.getValue();

        if (nickname.isEmpty() || nickname.length() < 3 || nickname.length() > 15) {
            System.out.println("Nickname must be 3-15 characters long.");
            return;
        }

        if (selectedTheme == null || selectedTheme.isEmpty()) {
            System.out.println("Please select a theme.");
            return;
        }

        System.out.println("Creating room with:");
        System.out.println("Nickname: " + nickname);
        System.out.println("Team Size: " + teamSize);
        System.out.println("Grid Size: " + gridSize);
        System.out.println("Timer: " + timer);
        System.out.println("Theme: " + selectedTheme);

        // Configure GameConfiguration instance
        GameConfiguration config = GameConfiguration.getInstance();
        config.setMaxTeamMember(teamSize);
        config.setGridSize(gridSize);
        config.setTimeTurn(timer);
        config.setTheme(selectedTheme);

        // Create a new game instance
        Game game = new Game(config);

        // Transition to the lobby (implement lobby logic)
        System.out.println("Transitioning to the lobby...");
    }

    @FXML
    private void joinRoom() {
        String nickname = textFieldNicknameJoin.getText();
        String address = textFieldAddress.getText();

        if (nickname.isEmpty() || nickname.length() < 3 || nickname.length() > 15) {
            System.out.println("Nickname must be 3-15 characters long.");
            return;
        }

        if (address.isEmpty()) {
            System.out.println("Address must not be empty.");
            return;
        }

        System.out.println("Joining room with:");
        System.out.println("Nickname: " + nickname);
        System.out.println("Address: " + address);

        // Implement client logic here to join the server
    }

    @FXML
    private void goBackToMainMenu() {
        // Logic to return to the main menu
        System.out.println("Returning to main menu...");
    }
}
