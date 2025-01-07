package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.view.GameConfigurationDialog;
import linguacrypt.view.PictGameConfigurationDialog;
import linguacrypt.view.MainMenuView;
import linguacrypt.view.CustomThemeDialog;


public class MainMenuController {

    MainMenuView view;
    ApplicationContext context = ApplicationContext.getInstance();

    public void setView(MainMenuView menuView){
        this.view = menuView;
        view.setOnCreateGame(this::handleCreateGame);
        view.setOnAddCustomTheme(this::handleAddCustomTheme);
    }

    public void handleCreateGame(ActionEvent event) {

            // Retrieve game configuration and personalize settings
            GameConfiguration config = GameConfiguration.getInstance();
            switch (config.getGameMode()) {
                case 0:                                 // Words Game Mode
                    GameConfigurationDialog dialog = new GameConfigurationDialog();
                    if (dialog.showGameConfigurationDialog()) {
                        config.setTheme(dialog.getSelectedTheme());
                        context.setGame(new Game(config));
                        context.getRoot().setCenter(context.getEditTeamNode());
                    }
                    break;
                case 1:                                 // Picture Game Mode
                    PictGameConfigurationDialog pictDialog = new PictGameConfigurationDialog();
                    if (pictDialog.showGameConfigurationDialog()) {
                        context.setGame(new Game(config));
                        context.getRoot().setCenter(context.getEditTeamNode());
                    }
                    break;
                default:
                    config.setGameMode(0);
                    break;
            }
            
    }

    public void handleAddCustomTheme() {
        
        new CustomThemeDialog();
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
