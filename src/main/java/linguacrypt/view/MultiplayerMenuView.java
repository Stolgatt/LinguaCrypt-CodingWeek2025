package linguacrypt.view;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.game.Theme;
import linguacrypt.networking.Client;
import linguacrypt.networking.Server;
import linguacrypt.utils.ThemeLoader;

public class MultiplayerMenuView {

    @FXML
    private VBox hostView, joinView;

    @FXML
    private TextField textFieldNicknameHost, textFieldNicknameJoin, textFieldAddress;

    @FXML
    private Label labelTeamSize, labelGridSize, labelTimer;

    @FXML
    private ComboBox<String> comboBoxTheme, comboBoxTeam;

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

        //init teams combo box
        comboBoxTeam.getItems().addAll("Blue Team", "Red Team");
        comboBoxTeam.getSelectionModel().selectFirst();

        // Load themes
        List<Theme> themes = ThemeLoader.loadThemes(0);
        for (Theme theme : themes) {
            comboBoxTheme.getItems().add(theme.getName());
        }
        comboBoxTheme.getSelectionModel().selectFirst();

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
        if (teamSize > 2) {
            teamSize--;
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void increaseTeamSize() {
        if (teamSize < 10) {
            teamSize++;
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void decreaseGridSize() {
        if (gridSize > 3) {
            gridSize--;
            labelGridSize.setText(String.valueOf(gridSize));
        }
    }

    @FXML
    private void increaseGridSize() {
        if (gridSize < 15) {
            gridSize++;
            labelGridSize.setText(String.valueOf(gridSize));
        }
    }

    @FXML
    private void decreaseTimer() {
        if (timer > 10) {
            timer -= 10;
            labelTimer.setText(String.valueOf(timer));
        }
    }

    @FXML
    private void increaseTimer() {
        if (timer < 120) {
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

        // Configure GameConfiguration
        GameConfiguration config = GameConfiguration.getInstance();
        config.setMaxTeamMember(teamSize);
        config.setGridSize(gridSize);
        config.setTimeTurn(-1);
        config.setWordTheme(selectedTheme);

        // Create a new game instance
        context.setGame(new Game(config));

        // Transition to the lobby
        try {
            Server server = new Server(nickname);
            server.start();

            context.setServer(server);
            context.getLobbyView().initialize();
            context.getRoot().setCenter(context.getLobbyNode());
            context.getLobbyView().addPlayer(nickname, 0);
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }

    @FXML
    private void joinRoom() {
        String nickname = textFieldNicknameJoin.getText();
        String address = textFieldAddress.getText();
        int teamId = comboBoxTeam.getSelectionModel().getSelectedIndex();

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
        System.out.println("Team: " + teamId);

        try {
            Client client = new Client(address, Server.PORT, nickname, teamId);
            context.setClient(client);
            context.getLobbyView().initialize();
            context.getRoot().setCenter(context.getLobbyNode());
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    @FXML
    private void goBackToMainMenu() {
        System.out.println("Returning to main menu...");
        context.getRoot().setCenter(context.getMainMenuNode());
    }
}
