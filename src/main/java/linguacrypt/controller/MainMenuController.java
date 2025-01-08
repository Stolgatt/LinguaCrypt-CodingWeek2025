package linguacrypt.controller;

import javafx.event.ActionEvent;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.view.CustomThemeDialog;
import linguacrypt.view.GameConfigurationDialog;
import linguacrypt.view.MainMenuView;
import linguacrypt.view.PictGameConfigurationDialog;
import linguacrypt.view.SoloModeConfigurationDialog;

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
                GameConfigurationDialog dialog = new GameConfigurationDialog();
                if (dialog.showGameConfigurationDialog()) {
                    config.setWordTheme(dialog.getSelectedTheme());
                    context.setGame(new Game(config));
                    context.getRoot().setCenter(context.getEditTeamNode());
                }
                break;
            case 1: // Picture Game Mode
                PictGameConfigurationDialog pictDialog = new PictGameConfigurationDialog();
                if (pictDialog.showGameConfigurationDialog()) {
                    config.setPictTheme(pictDialog.getSelectedTheme());
                    context.setGame(new Game(config));
                    context.getRoot().setCenter(context.getEditTeamNode());
                }
                break;
            case 2: // Solo Game Mode
                SoloModeConfigurationDialog soloDialog = new SoloModeConfigurationDialog();
                if (soloDialog.showSoloGameConfigurationDialog()) {
                    context.getRoot().setCenter(context.getSoloGameNode());
                }
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
        new CustomThemeDialog();
    }
}
