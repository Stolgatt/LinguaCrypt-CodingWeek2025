package linguacrypt.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import linguacrypt.model.GameConfiguration;
import linguacrypt.ApplicationContext;

public class MainMenuView {

    @FXML
    private StackPane dynamicContent;

    @FXML
    private VBox networkModeSelectionBox;

    @FXML
    private VBox gameModeSelectionBox;

    @FXML
    private VBox mainMenuBox;

    @FXML
    private StackPane playModeButtons;

    @FXML
    private Button localButton;

    @FXML
    private Button multiplayerButton;

    @FXML
    private Button wordGameButton;

    @FXML
    private Button pictGameButton;

    @FXML
    private Button playButton;

    @FXML
    private Button hostButton;

    @FXML
    private Button joinButton;

    @FXML
    private VBox multiButtons;

    @FXML
    private Button profileMenuButton;

    @FXML
    private Button customThemeButton;

    @FXML
    private Button createSoloGameButton;
    @FXML
    private Button exitButton;

    @FXML
    private Button backToGameModeButton;

    @FXML
    private Button backToNetworkModeButton;

    private Runnable onPlayGame;
    private Runnable onProfileMenu;
    private Runnable onAddCustomTheme;
    private ApplicationContext context = ApplicationContext.getInstance();
    @FXML
    public void initialize() {
        // Network Mode Selection
        localButton.setOnAction(e -> selectNetworkMode(false));
        multiplayerButton.setOnAction(e -> selectNetworkMode(true));

        // Game Mode Selection
        wordGameButton.setOnAction(e -> selectGameMode(0));
        pictGameButton.setOnAction(e -> selectGameMode(1));


        
        createSoloGameButton.setOnAction(e -> {
            GameConfiguration.getInstance().setGameMode(2);         // Solo Game Mode
            onPlayGame.run();});

        // Main Menu Buttons
        playButton.setOnAction(e -> onPlayGame.run());
        profileMenuButton.setOnAction(e -> onProfileMenu.run());
        customThemeButton.setOnAction(e -> onAddCustomTheme.run());

        // Back Button
        backToGameModeButton.setOnAction(this::handleBackToGameModeButton);
        backToNetworkModeButton.setOnAction(this::handleBackToNetworkModeButton);

    }

    public void setOnPlayGame(Runnable onPlayGame) {
        this.onPlayGame = onPlayGame;
    }

    public void setOnProfileMenu(Runnable onProfileMenu) {
        this.onProfileMenu = onProfileMenu;
    }

    public void setOnAddCustomTheme(Runnable onAddCustomTheme) {
        this.onAddCustomTheme = onAddCustomTheme;
    }

    private void selectNetworkMode(boolean isMultiplayer) {
        // Hide network mode selection screen
        networkModeSelectionBox.setVisible(false);

        // Show game mode selection
        gameModeSelectionBox.setVisible(true);

        // Update buttons based on network mode
        playButton.setVisible(!isMultiplayer);
        createSoloGameButton.setVisible(!isMultiplayer);
        multiButtons.setVisible(isMultiplayer);
        hostButton.setVisible(isMultiplayer);
        joinButton.setVisible(isMultiplayer);
        hostButton.setOnAction(event -> context.getMPMenuView().showHostView());
        joinButton.setOnAction(event -> context.getMPMenuView().showJoinView());

    }

    private void selectGameMode(int mode) {
        // Set the game mode globally
        linguacrypt.model.GameConfiguration.getInstance().setGameMode(mode);

        // Switch to main menu options
        gameModeSelectionBox.setVisible(false);
        mainMenuBox.setVisible(true);
    }

    private void handleBackToGameModeButton(ActionEvent event) {
        // Return to game mode selection
        mainMenuBox.setVisible(false);
        networkModeSelectionBox.setVisible(false);
        gameModeSelectionBox.setVisible(true);
    }

    private void handleBackToNetworkModeButton(ActionEvent event) {
        // Return to Network mode selection
        mainMenuBox.setVisible(false);
        gameModeSelectionBox.setVisible(false);
        networkModeSelectionBox.setVisible(true);
    }


    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
