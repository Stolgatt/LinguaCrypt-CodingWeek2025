package linguacrypt.controller;

import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.view.DialogBox.CustomThemeDialog;
import linguacrypt.view.DialogBox.GameConfigurationDialog;
import linguacrypt.view.MainMenuView;
import linguacrypt.view.DialogBox.PictGameConfigurationDialog;
import linguacrypt.view.DialogBox.SoloModeConfigurationDialog;

public class MainMenuController {

    private MainMenuView view;
    private ApplicationContext context = ApplicationContext.getInstance();

    public void setView(MainMenuView view) {
        this.view = view;
        view.setOnPlayGame(this::handlePlayGame);
        view.setOnProfileMenu(this::handleProfileMenu);
        view.setOnAddCustomTheme(this::handleAddCustomTheme);
    }

    private void handlePlayGame() {
        // Retrieve game configuration and personalize settings
        GameConfiguration config = GameConfiguration.getInstance();
        switch (config.getGameMode()) {
            case 0: // Words Game Mode
                context.getLocalMenuView().showGameView();
                context.getLocalMenuView().loadExistingThemes();
                context.getRoot().setCenter(context.getLocalNode());

                break;
            case 1: // Picture Game Mode
            context.getLocalMenuView().showGameView();
            context.getLocalMenuView().loadExistingThemes();
            context.getRoot().setCenter(context.getLocalNode());

                break;
            case 2: // Solo Game Mode
                context.getLocalMenuView().showSoloView();
                context.getLocalMenuView().loadExistingThemes();
                context.getRoot().setCenter(context.getLocalNode());
                break;
            default:
                config.setGameMode(0);
                break;
        }

    }

    private void handleProfileMenu() {
        context.getRoot().setCenter(context.getProfileMenuNode());
    }

    private void handleAddCustomTheme() {
        // Add custom theme logic
        context.getEditThemeView().loadExistingThemes();
        context.getRoot().setCenter(context.getEditThemeNode());
    }
}
