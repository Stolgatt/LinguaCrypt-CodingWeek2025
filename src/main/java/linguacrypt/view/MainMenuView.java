package linguacrypt.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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
    private VBox localButtons;

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
            GameConfiguration.getInstance().setGameMode(2); // Solo Game Mode
            onPlayGame.run();
        });

        // Main Menu Buttons
        playButton.setOnAction(e -> onPlayGame.run());
        profileMenuButton.setOnAction(e -> onProfileMenu.run());
        customThemeButton.setOnAction(e -> onAddCustomTheme.run());

        // Back Buttons
        backToGameModeButton.setOnAction(this::handleBackToGameModeButton);
        backToNetworkModeButton.setOnAction(this::handleBackToNetworkModeButton);

        // Add effects to all buttons
        addEffectsToButtons();
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
        localButtons.setVisible(!isMultiplayer);
        hostButton.setVisible(isMultiplayer);
        joinButton.setVisible(isMultiplayer);
        hostButton.setOnAction(event -> context.getMPMenuView().showHostView());
        joinButton.setOnAction(event -> context.getMPMenuView().showJoinView());
    }

    private void selectGameMode(int mode) {
        // Set the game mode globally
        GameConfiguration.getInstance().setGameMode(mode);

        // Switch to main menu options
        gameModeSelectionBox.setVisible(false);
        mainMenuBox.setVisible(true);
        if (mode == 1) {
            // Pictogram mode: hide and unmanage "Play Solo"
            createSoloGameButton.setVisible(false);
            createSoloGameButton.setManaged(false);
        } else {
            // Other modes: show and manage "Play Solo"
            createSoloGameButton.setVisible(true);
            createSoloGameButton.setManaged(true);
        }
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

    /**
     * Adds hover and click effects to all buttons.
     */
    private void addEffectsToButtons() {
        addHoverAndClickEffects(localButton);
        addHoverAndClickEffects(multiplayerButton);
        addHoverAndClickEffects(wordGameButton);
        addHoverAndClickEffects(pictGameButton);
        addHoverAndClickEffects(playButton);
        addHoverAndClickEffects(hostButton);
        addHoverAndClickEffects(joinButton);
        addHoverAndClickEffects(profileMenuButton);
        addHoverAndClickEffects(customThemeButton);
        addHoverAndClickEffects(createSoloGameButton);
        addHoverAndClickEffects(exitButton);

        addHoverAndClickEffectsBack(backToGameModeButton);
        addHoverAndClickEffectsBack(backToNetworkModeButton);
    }

    private void addHoverAndClickEffectsBack(Button button) {
        // Hover Effect
        ScaleTransition hoverEnter = new ScaleTransition(Duration.millis(200), button);
        hoverEnter.setToX(1.1);
        hoverEnter.setToY(1.1);

        ScaleTransition hoverExit = new ScaleTransition(Duration.millis(200), button);
        hoverExit.setToX(1.0);
        hoverExit.setToY(1.0);

        button.setOnMouseEntered(e -> hoverEnter.playFromStart());
        button.setOnMouseExited(e -> hoverExit.playFromStart());

        // Click Effect
        button.setOnMousePressed((MouseEvent e) -> {
            button.setStyle(
                    "-fx-background-color: #ffc107; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 15 30; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #e6ae09; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 5, 0, 0, 1); -fx-min-width: 210;");
        });

        button.setOnMouseReleased((MouseEvent e) -> {
            button.setStyle(
                    "-fx-background-color: #ffc107; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 15 30; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #e6ae09; -f-min-width: 200.0;");
        });
    }

    /**
     * Adds hover and click effects to a button.
     *
     * @param button The button to which effects will be added.
     */
    private void addHoverAndClickEffects(Button button) {
        // Hover Effect
        ScaleTransition hoverEnter = new ScaleTransition(Duration.millis(200), button);
        hoverEnter.setToX(1.1);
        hoverEnter.setToY(1.1);

        ScaleTransition hoverExit = new ScaleTransition(Duration.millis(200), button);
        hoverExit.setToX(1.0);
        hoverExit.setToY(1.0);

        button.setOnMouseEntered(e -> hoverEnter.playFromStart());
        button.setOnMouseExited(e -> hoverExit.playFromStart());

        // Click Effect
        button.setOnMousePressed((MouseEvent e) -> {
            button.setStyle(
                    "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 15 30; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #388E3C; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 5, 0, 0, 1); -fx-min-width: 210;");
        });

        button.setOnMouseReleased((MouseEvent e) -> {
            button.setStyle(
                    "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 15 30; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-width: 2px; -fx-border-color: #388E3C; -f-min-width: 200.0;");
        });

    }
}
