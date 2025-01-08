package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.view.*;


public class MainMenuController {

    MainMenuView view;
    ApplicationContext context = ApplicationContext.getInstance();

    public void setView(MainMenuView menuView){
        this.view = menuView;
        view.setOnCreateGame(this::handleCreateGame);
        view.setOnAddCustomTheme(this::handleAddCustomTheme);
        view.setOnProfileMenu(this::handleProfileMenu);
    }

    private void handleProfileMenu(ActionEvent event) {
        context.getRoot().setCenter(context.getProfileMenuNode());


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
                case 2:                                 // Solo Game Mode
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

    public void handleAddCustomTheme() {
        
        new CustomThemeDialog();
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
